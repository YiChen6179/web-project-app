package com.yichen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yichen.entity.ParkingSpot;
import com.yichen.entity.Vehicle;
import com.yichen.exception.ConstraintViolationException;
import com.yichen.mapper.ParkingRecordMapper;
import com.yichen.entity.ParkingRecord;
import com.yichen.service.ParkingRecordService;
import com.yichen.service.ParkingSpotService;
import com.yichen.service.VehicleService;
import com.yichen.utils.BusinessValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 停车记录服务实现类
 */
@Service
@RequiredArgsConstructor
public class ParkingRecordServiceImpl extends ServiceImpl<ParkingRecordMapper, ParkingRecord> implements ParkingRecordService {

    private final ParkingSpotService parkingSpotService;
    private final VehicleService vehicleService;

    @Override
    public Page<ParkingRecord> listParkingRecords(Integer current, Integer size, Long vehicleId, Integer status) {
        Page<ParkingRecord> page = new Page<>(current, size);
        LambdaQueryWrapper<ParkingRecord> queryWrapper = new LambdaQueryWrapper<>();
        
        if (vehicleId != null) {
            queryWrapper.eq(ParkingRecord::getVehicleId, vehicleId);
        }
        
        if (status != null) {
            queryWrapper.eq(ParkingRecord::getStatus, status);
        }
        
        return getBaseMapper().selectPage(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ParkingRecord exitParking(Long id) {
        ParkingRecord record = getById(id);
        BusinessValidationUtil.checkNotNull(record, "停车记录不存在");
        
        // 检查记录状态是否为进行中
        BusinessValidationUtil.check(record.getStatus() == 0, "该记录已完成，不能重复出场");
        
        // 设置离场时间和状态
        record.setExitTime(LocalDateTime.now());
        record.setStatus(1); // 1-已完成
        
        // 计算停车时长
        LocalDateTime entryTime = record.getEntryTime();
        LocalDateTime exitTime = record.getExitTime();
        
        if (entryTime != null && exitTime != null) {
            // 计算停车时长（分钟）
            Duration duration = Duration.between(entryTime, exitTime);
            record.setParkingTime(duration.toMinutes());
        }
        
        // 更新停车位状态为空闲
        Long spotId = record.getParkingSpotId();
        if (spotId != null) {
            ParkingSpot spot = parkingSpotService.getById(spotId);
            if (spot != null) {
                spot.setStatus(0); // 0-空闲
                parkingSpotService.updateById(spot);
            }
        }
        
        updateById(record);
        return record;
    }
    
    /**
     * 重写updateById方法，增加约束检查
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(ParkingRecord entity) {
        // 获取原始记录
        ParkingRecord original = getById(entity.getId());
        BusinessValidationUtil.checkNotNull(original, "要更新的停车记录不存在");
        
        // 检查车位ID是否被修改
        Long originalSpotId = original.getParkingSpotId();
        Long newSpotId = entity.getParkingSpotId();
        if (newSpotId != null && !newSpotId.equals(originalSpotId)) {
            // 如果停车位变更了，检查新停车位是否存在和是否可用
            ParkingSpot newSpot = parkingSpotService.getById(newSpotId);
            BusinessValidationUtil.checkNotNull(newSpot, "新指定的停车位不存在");
            
            // 只有在记录状态为进行中（0）时才需要检查
            if (entity.getStatus() == null || entity.getStatus() == 0) {
                BusinessValidationUtil.check(newSpot.getStatus() == 0, "新指定的停车位已被占用");
                
                // 如果原车位存在，将其状态改为空闲
                if (originalSpotId != null) {
                    ParkingSpot originalSpot = parkingSpotService.getById(originalSpotId);
                    if (originalSpot != null) {
                        originalSpot.setStatus(0); // 0-空闲
                        parkingSpotService.updateById(originalSpot);
                    }
                }
                
                // 更新新车位状态为占用
                newSpot.setStatus(1); // 1-占用
                parkingSpotService.updateById(newSpot);
            }
        }
        
        // 检查车辆ID是否被修改
        Long vehicleId = entity.getVehicleId();
        if (vehicleId != null && !vehicleId.equals(original.getVehicleId())) {
            // 检查新车辆是否存在
            Vehicle vehicle = vehicleService.getById(vehicleId);
            BusinessValidationUtil.checkNotNull(vehicle, "新指定的车辆不存在");
            
            // 同步更新车牌号
            entity.setPlateNumber(vehicle.getPlateNumber());
        }
        
        // 检查状态是否从进行中(0)变为已完成(1)
        Integer originalStatus = original.getStatus();
        Integer newStatus = entity.getStatus();
        if (originalStatus == 0 && newStatus != null && newStatus == 1 && entity.getExitTime() == null) {
            // 状态变为已完成但未设置离场时间，则自动设置
            entity.setExitTime(LocalDateTime.now());
            
            // 计算停车时长
            LocalDateTime entryTime = entity.getEntryTime() != null ? entity.getEntryTime() : original.getEntryTime();
            if (entryTime != null) {
                Duration duration = Duration.between(entryTime, entity.getExitTime());
                entity.setParkingTime(duration.toMinutes());
            }
            
            // 更新停车位状态为空闲
            if (newSpotId != null) {
                ParkingSpot spot = parkingSpotService.getById(newSpotId);
                if (spot != null) {
                    spot.setStatus(0); // 0-空闲
                    parkingSpotService.updateById(spot);
                }
            }
        }
        
        return super.updateById(entity);
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ParkingRecord createParkingRecord(ParkingRecord record) {
        // 检查必要字段
        BusinessValidationUtil.checkNotNull(record.getParkingSpotId(), "停车位ID不能为空");
        BusinessValidationUtil.checkNotNull(record.getPlateNumber(), "车牌号不能为空");
        
        // 检查停车位是否存在且可用
        Long spotId = record.getParkingSpotId();
        ParkingSpot spot = parkingSpotService.getById(spotId);
        BusinessValidationUtil.checkNotNull(spot, "停车位不存在");
        BusinessValidationUtil.check(spot.getStatus() == 0, "该停车位已被占用");
        
        // 检查车辆是否存在
        String plateNumber = record.getPlateNumber();
        LambdaQueryWrapper<Vehicle> vehicleQuery = new LambdaQueryWrapper<>();
        vehicleQuery.eq(Vehicle::getPlateNumber, plateNumber);
        Vehicle vehicle = vehicleService.getOne(vehicleQuery);
        
        // 如果车辆存在，设置vehicleId
        if (vehicle != null) {
            record.setVehicleId(vehicle.getId());
        }
        
        // 设置入场时间和状态
        record.setEntryTime(LocalDateTime.now());
        record.setStatus(0); // 0-进行中
        
        // 更新停车位状态为占用
        spot.setStatus(1); // 1-占用
        parkingSpotService.updateById(spot);
        
        // 保存停车记录
        save(record);
        return record;
    }
} 