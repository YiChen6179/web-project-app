package com.yichen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yichen.entity.ParkingLot;
import com.yichen.entity.ParkingRecord;
import com.yichen.entity.ParkingSpot;
import com.yichen.mapper.DashboardMapper;
import com.yichen.mapper.ParkingLotMapper;
import com.yichen.mapper.ParkingRecordMapper;
import com.yichen.mapper.ParkingSpotMapper;
import com.yichen.service.DashboardService;
import com.yichen.vo.DashboardDataVO;
import com.yichen.vo.ParkingLotVO;
import com.yichen.vo.ParkingRecordVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private DashboardMapper dashboardMapper;
    
    @Autowired
    private ParkingLotMapper parkingLotMapper;
    
    @Autowired
    private ParkingSpotMapper parkingSpotMapper;
    
    @Autowired
    private ParkingRecordMapper parkingRecordMapper;

    @Override
    public DashboardDataVO getDashboardData() {
        return DashboardDataVO.builder()
                .totalParkingLots(getTotalParkingLots())
                .totalParkingSpots(getTotalParkingSpots())
                .currentUsedSpots(getCurrentUsedSpots())
                .registeredVehicles(getRegisteredVehicles())
                .parkingLotOccupancies(getParkingLotOccupancies())
                .recentParkingRecords(getRecentParkingRecords())
                .build();
    }

    @Override
    public Integer getTotalParkingLots() {
        return dashboardMapper.getTotalParkingLots();
    }

    @Override
    public Integer getTotalParkingSpots() {
        return dashboardMapper.getTotalParkingSpots();
    }

    @Override
    public Integer getCurrentUsedSpots() {
        return dashboardMapper.getCurrentUsedSpots();
    }

    @Override
    public Integer getRegisteredVehicles() {
        return dashboardMapper.getRegisteredVehicles();
    }

    @Override
    public List<ParkingLotVO> getParkingLotOccupancies() {
        // 获取所有停车场
        List<ParkingLot> parkingLots = parkingLotMapper.selectList(null);
        if (parkingLots.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 获取所有车位
        List<ParkingSpot> allSpots = parkingSpotMapper.selectList(null);
        
        // 按停车场ID对车位进行分组
        Map<Long, List<ParkingSpot>> spotsByZoneId = allSpots.stream()
                .collect(Collectors.groupingBy(ParkingSpot::getParkingZoneId));
                
        // 获取每个停车场的占用情况（最多5个）
        List<ParkingLotVO> result = new ArrayList<>();
        
        for (ParkingLot lot : parkingLots) {
            if (result.size() >= 5) {
                break;
            }
            
            // 计算该停车场的总车位数和已使用车位数
            int totalSpots = 0;
            int usedSpots = 0;
            
            // 查询该停车场下的所有区域ID
            List<Long> zoneIds = parkingLotMapper.getZoneIdsByLotId(lot.getId());
            
            for (Long zoneId : zoneIds) {
                List<ParkingSpot> spots = spotsByZoneId.getOrDefault(zoneId, new ArrayList<>());
                totalSpots += spots.size();
                usedSpots += spots.stream().filter(spot -> spot.getStatus() == 1).count();
            }
            
            // 创建VO对象
            ParkingLotVO vo = new ParkingLotVO();
            BeanUtils.copyProperties(lot, vo);
            vo.setTotalSpot(totalSpots);
            vo.setUsedSpot(usedSpots);
                    
            result.add(vo);
        }
        
        return result;
    }

    @Override
    public List<ParkingRecordVO> getRecentParkingRecords() {
        // 查询最近的5条停车记录
        LambdaQueryWrapper<ParkingRecord> wrapper = Wrappers.lambdaQuery();
        wrapper.orderByDesc(ParkingRecord::getId);
        wrapper.last("LIMIT 5");
        
        List<ParkingRecord> records = parkingRecordMapper.selectList(wrapper);
        
        // 转换为VO对象
        return records.stream().map(record -> {
            ParkingRecordVO vo = new ParkingRecordVO();
            BeanUtils.copyProperties(record, vo);
            return vo;
        }).collect(Collectors.toList());
    }
} 