package com.yichen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yichen.entity.*;
import com.yichen.mapper.*;
import com.yichen.service.ConstraintService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 实体逻辑约束服务实现类
 */
@Service
@RequiredArgsConstructor
public class ConstraintServiceImpl implements ConstraintService {

    private final ParkingZoneMapper parkingZoneMapper;
    private final ParkingSpotMapper parkingSpotMapper;
    private final ParkingRecordMapper parkingRecordMapper;
    private final VehicleMapper vehicleMapper;
    private final UserMapper userMapper;

    @Override
    public boolean canDeleteParkingLot(Long parkingLotId) {
        // 检查是否有关联的停车区域
        LambdaQueryWrapper<ParkingZone> zoneQuery = new LambdaQueryWrapper<>();
        zoneQuery.eq(ParkingZone::getParkingLotId, parkingLotId);
        return parkingZoneMapper.selectCount(zoneQuery) == 0;
    }

    @Override
    public boolean canDeleteParkingZone(Long parkingZoneId) {
        // 检查是否有关联的停车位
        LambdaQueryWrapper<ParkingSpot> spotQuery = new LambdaQueryWrapper<>();
        spotQuery.eq(ParkingSpot::getParkingZoneId, parkingZoneId);
        return parkingSpotMapper.selectCount(spotQuery) == 0;
    }

    @Override
    public boolean canDeleteParkingSpot(Long parkingSpotId) {
        // 检查是否有关联的停车记录，尤其是未完成的记录
        LambdaQueryWrapper<ParkingRecord> recordQuery = new LambdaQueryWrapper<>();
        recordQuery.eq(ParkingRecord::getParkingSpotId, parkingSpotId);
        
        // 可以选择只检查活跃记录（status = 1, 表示正在进行的停车）
        // recordQuery.eq(ParkingRecord::getStatus, 1);
        
        // 或者检查所有记录，根据业务需求决定
        return parkingRecordMapper.selectCount(recordQuery) == 0;
    }
    
    /**
     * 检查停车位是否有进行中的停车记录
     * @param parkingSpotId 停车位ID
     * @return 如果没有进行中的停车记录返回true，否则返回false
     */
    @Override
    public boolean hasNoActiveParkingRecords(Long parkingSpotId) {
        LambdaQueryWrapper<ParkingRecord> recordQuery = new LambdaQueryWrapper<>();
        recordQuery.eq(ParkingRecord::getParkingSpotId, parkingSpotId)
                  .eq(ParkingRecord::getStatus, 0); // 0表示进行中
        return parkingRecordMapper.selectCount(recordQuery) == 0;
    }

    @Override
    public boolean canDeleteUser(Long userId) {
        // 检查是否有关联的车辆
        LambdaQueryWrapper<Vehicle> vehicleQuery = new LambdaQueryWrapper<>();
        vehicleQuery.eq(Vehicle::getUserId, userId);
        return vehicleMapper.selectCount(vehicleQuery) == 0;
    }

    @Override
    public boolean canDeleteVehicle(Long vehicleId) {
        // 检查是否有关联的停车记录
        LambdaQueryWrapper<ParkingRecord> recordQuery = new LambdaQueryWrapper<>();
        recordQuery.eq(ParkingRecord::getVehicleId, vehicleId);
        
        // 可以选择只检查活跃记录
        // recordQuery.eq(ParkingRecord::getStatus, 1);
        
        return parkingRecordMapper.selectCount(recordQuery) == 0;
    }
    
    /**
     * 检查车辆是否有进行中的停车记录
     * @param vehicleId 车辆ID
     * @return 如果没有进行中的停车记录返回true，否则返回false
     */
    @Override
    public boolean vehicleHasNoActiveParkingRecords(Long vehicleId) {
        LambdaQueryWrapper<ParkingRecord> recordQuery = new LambdaQueryWrapper<>();
        recordQuery.eq(ParkingRecord::getVehicleId, vehicleId)
                  .eq(ParkingRecord::getStatus, 0); // 0表示进行中
        return parkingRecordMapper.selectCount(recordQuery) == 0;
    }
    
    /**
     * 根据车牌号检查车辆是否有进行中的停车记录
     * @param plateNumber 车牌号
     * @return 如果没有进行中的停车记录返回true，否则返回false
     */
    @Override
    public boolean plateNumberHasNoActiveParkingRecords(String plateNumber) {
        LambdaQueryWrapper<ParkingRecord> recordQuery = new LambdaQueryWrapper<>();
        recordQuery.eq(ParkingRecord::getPlateNumber, plateNumber)
                  .eq(ParkingRecord::getStatus, 0); // 0表示进行中
        return parkingRecordMapper.selectCount(recordQuery) == 0;
    }
    
    /**
     * 检查车牌号是否唯一
     * @param plateNumber 车牌号
     * @param excludeVehicleId 排除的车辆ID（可选，用于更新时排除自身）
     * @return 如果车牌号唯一返回true，否则返回false
     */
    @Override
    public boolean isPlateNumberUnique(String plateNumber, Long excludeVehicleId) {
        LambdaQueryWrapper<Vehicle> vehicleQuery = new LambdaQueryWrapper<>();
        vehicleQuery.eq(Vehicle::getPlateNumber, plateNumber);
        
        if (excludeVehicleId != null) {
            vehicleQuery.ne(Vehicle::getId, excludeVehicleId);
        }
        
        return vehicleMapper.selectCount(vehicleQuery) == 0;
    }
} 