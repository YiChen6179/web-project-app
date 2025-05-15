package com.yichen.service;

import com.yichen.vo.DashboardDataVO;
import com.yichen.vo.ParkingLotVO;
import com.yichen.vo.ParkingRecordVO;

import java.util.List;

public interface DashboardService {
    
    /**
     * 获取仪表盘所有数据
     * @return 仪表盘数据VO
     */
    DashboardDataVO getDashboardData();
    
    /**
     * 获取总停车场数量
     * @return 停车场数量
     */
    Integer getTotalParkingLots();
    
    /**
     * 获取总车位数量
     * @return 车位数量
     */
    Integer getTotalParkingSpots();
    
    /**
     * 获取当前已使用车位数
     * @return 已使用车位数
     */
    Integer getCurrentUsedSpots();
    
    /**
     * 获取注册车辆数
     * @return 注册车辆数
     */
    Integer getRegisteredVehicles();
    
    /**
     * 获取停车场占用率数据(最多5条)
     * @return 停车场占用率列表
     */
    List<ParkingLotVO> getParkingLotOccupancies();
    
    /**
     * 获取最近停车记录(最多5条)
     * @return 最近停车记录列表
     */
    List<ParkingRecordVO> getRecentParkingRecords();
} 