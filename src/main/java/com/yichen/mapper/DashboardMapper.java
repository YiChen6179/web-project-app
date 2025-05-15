package com.yichen.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DashboardMapper {
    
    /**
     * 获取当前使用中的车位数
     * @return 使用中的车位数
     */
    @Select("SELECT COUNT(*) FROM parking_spot WHERE status = 1")
    Integer getCurrentUsedSpots();
    
    /**
     * 获取总停车场数
     * @return 停车场数量
     */
    @Select("SELECT COUNT(*) FROM parking_lot")
    Integer getTotalParkingLots();
    
    /**
     * 获取总车位数
     * @return 车位数量
     */
    @Select("SELECT COUNT(*) FROM parking_spot")
    Integer getTotalParkingSpots();
    
    /**
     * 获取注册车辆数
     * @return 注册车辆数
     */
    @Select("SELECT COUNT(*) FROM vehicle")
    Integer getRegisteredVehicles();
} 