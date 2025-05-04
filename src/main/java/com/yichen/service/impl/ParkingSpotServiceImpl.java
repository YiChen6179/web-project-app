package com.yichen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yichen.entity.ParkingZone;
import com.yichen.exception.ConstraintViolationException;
import com.yichen.mapper.ParkingSpotMapper;
import com.yichen.entity.ParkingSpot;
import com.yichen.service.ConstraintService;
import com.yichen.service.ParkingSpotService;
import com.yichen.service.ParkingZoneService;
import com.yichen.utils.BusinessValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 停车位服务实现类
 */
@Service
@RequiredArgsConstructor
public class ParkingSpotServiceImpl extends ServiceImpl<ParkingSpotMapper, ParkingSpot> implements ParkingSpotService {

    private final ConstraintService constraintService;
    private final ParkingZoneService parkingZoneService;

    @Override
    public Page<ParkingSpot> listParkingSpots(Integer current, Integer size, String spotNumber, Long zoneId, Integer status) {
        Page<ParkingSpot> page = new Page<>(current, size);
        LambdaQueryWrapper<ParkingSpot> queryWrapper = new LambdaQueryWrapper<>();
        
        if (spotNumber != null && !spotNumber.isEmpty()) {
            queryWrapper.like(ParkingSpot::getSpotNumber, spotNumber);
        }
        
        if (zoneId != null) {
            queryWrapper.eq(ParkingSpot::getParkingZoneId, zoneId);
        }
        
        if (status != null) {
            queryWrapper.eq(ParkingSpot::getStatus, status);
        }
        
        return getBaseMapper().selectPage(page, queryWrapper);
    }
    
    /**
     * 重写updateById方法，增加约束检查
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(ParkingSpot entity) {
        // 获取原始实体
        ParkingSpot originalSpot = getById(entity.getId());
        BusinessValidationUtil.checkNotNull(originalSpot, "停车位不存在");
        
        // 检查停车区域是否存在
        Long parkingZoneId = entity.getParkingZoneId();
        if (parkingZoneId != null && !parkingZoneId.equals(originalSpot.getParkingZoneId())) {
            ParkingZone parkingZone = parkingZoneService.getById(parkingZoneId);
            BusinessValidationUtil.checkNotNull(parkingZone, "关联的停车区域不存在");
        }
        
        // 检查状态变更
        if (originalSpot.getStatus() != entity.getStatus()) {
            // 如果状态从占用变为空闲，需要检查是否有进行中的停车记录
            if (originalSpot.getStatus() == 1 && entity.getStatus() == 0) {
                // 通过约束服务检查是否有进行中的停车记录
                boolean noActiveRecords = constraintService.hasNoActiveParkingRecords(entity.getId());
                BusinessValidationUtil.check(noActiveRecords, "该停车位有进行中的停车记录，无法变更为空闲状态");
            }
        }
        
        return super.updateById(entity);
    }
    
    /**
     * 重写save方法，增加约束检查
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(ParkingSpot entity) {
        // 检查停车区域是否存在
        Long parkingZoneId = entity.getParkingZoneId();
        if (parkingZoneId != null) {
            ParkingZone parkingZone = parkingZoneService.getById(parkingZoneId);
            BusinessValidationUtil.checkNotNull(parkingZone, "关联的停车区域不存在");
        }
        
        // 检查车位号在同一区域内是否唯一
        if (parkingZoneId != null && entity.getSpotNumber() != null && !entity.getSpotNumber().isEmpty()) {
            LambdaQueryWrapper<ParkingSpot> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(ParkingSpot::getParkingZoneId, parkingZoneId)
                       .eq(ParkingSpot::getSpotNumber, entity.getSpotNumber());
            
            long count = count(queryWrapper);
            BusinessValidationUtil.check(count == 0, "该区域内已存在相同车位号，请更换车位号");
        }
        
        return super.save(entity);
    }
    
    /**
     * 重写removeById方法，增加约束检查
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Long id) {
        if (!constraintService.canDeleteParkingSpot(id)) {
            throw new ConstraintViolationException("无法删除该停车位，存在关联的停车记录");
        }
        return super.removeById(id);
    }
    
    /**
     * 检查停车位当前是否可用（状态为空闲）
     * @param id 停车位ID
     * @return 如果可用返回true，否则返回false
     */
    public boolean isSpotAvailable(Long id) {
        ParkingSpot spot = getById(id);
        return spot != null && spot.getStatus() == 0; // 0表示空闲
    }
} 