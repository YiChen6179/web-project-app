package com.yichen.service;

/**
 * 实体逻辑约束服务接口
 * 处理实体间的逻辑外键约束关系
 */
public interface ConstraintService {
    
    /**
     * 检查停车场是否可以删除
     * @param parkingLotId 停车场ID
     * @return 如果可以删除返回true，否则返回false
     */
    boolean canDeleteParkingLot(Long parkingLotId);
    
    /**
     * 检查停车区域是否可以删除
     * @param parkingZoneId 停车区域ID
     * @return 如果可以删除返回true，否则返回false
     */
    boolean canDeleteParkingZone(Long parkingZoneId);
    
    /**
     * 检查停车位是否可以删除
     * @param parkingSpotId 停车位ID
     * @return 如果可以删除返回true，否则返回false
     */
    boolean canDeleteParkingSpot(Long parkingSpotId);
    
    /**
     * 检查停车位是否有进行中的停车记录
     * @param parkingSpotId 停车位ID
     * @return 如果没有进行中的停车记录返回true，否则返回false
     */
    boolean hasNoActiveParkingRecords(Long parkingSpotId);
    
    /**
     * 检查用户是否可以删除
     * @param userId 用户ID
     * @return 如果可以删除返回true，否则返回false
     */
    boolean canDeleteUser(Long userId);
    
    /**
     * 检查车辆是否可以删除
     * @param vehicleId 车辆ID
     * @return 如果可以删除返回true，否则返回false
     */
    boolean canDeleteVehicle(Long vehicleId);
    
    /**
     * 检查车辆是否有进行中的停车记录
     * @param vehicleId 车辆ID
     * @return 如果没有进行中的停车记录返回true，否则返回false
     */
    boolean vehicleHasNoActiveParkingRecords(Long vehicleId);
    
    /**
     * 根据车牌号检查车辆是否有进行中的停车记录
     * @param plateNumber 车牌号
     * @return 如果没有进行中的停车记录返回true，否则返回false
     */
    boolean plateNumberHasNoActiveParkingRecords(String plateNumber);
    
    /**
     * 检查车牌号是否唯一
     * @param plateNumber 车牌号
     * @param excludeVehicleId 排除的车辆ID（可选，用于更新时排除自身）
     * @return 如果车牌号唯一返回true，否则返回false
     */
    boolean isPlateNumberUnique(String plateNumber, Long excludeVehicleId);
} 