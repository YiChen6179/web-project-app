package com.yichen.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDataVO {
    // 基本统计数据
    private Integer totalParkingLots;    // 总停车场数
    private Integer totalParkingSpots;   // 总车位数
    private Integer currentUsedSpots;    // 当前使用数
    private Integer registeredVehicles;  // 注册车辆数
    
    // 停车场占用率列表
    private List<ParkingLotVO> parkingLotOccupancies;
    
    // 最近停车记录
    private List<ParkingRecordVO> recentParkingRecords;
} 