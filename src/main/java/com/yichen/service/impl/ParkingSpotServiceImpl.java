package com.yichen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yichen.mapper.ParkingSpotMapper;
import com.yichen.entity.ParkingSpot;
import com.yichen.service.ParkingSpotService;
import org.springframework.stereotype.Service;

/**
 * 停车位服务实现类
 */
@Service
public class ParkingSpotServiceImpl extends ServiceImpl<ParkingSpotMapper, ParkingSpot> implements ParkingSpotService {

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
} 