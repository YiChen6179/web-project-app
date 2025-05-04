package com.yichen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yichen.mapper.VehicleMapper;
import com.yichen.entity.Vehicle;
import com.yichen.service.VehicleService;
import org.springframework.stereotype.Service;

/**
 * 车辆服务实现类
 */
@Service
public class VehicleServiceImpl extends ServiceImpl<VehicleMapper, Vehicle> implements VehicleService {

    @Override
    public Page<Vehicle> listVehicles(Integer current, Integer size, String plateNumber, Long userId) {
        Page<Vehicle> page = new Page<>(current, size);
        LambdaQueryWrapper<Vehicle> queryWrapper = new LambdaQueryWrapper<>();
        
        if (plateNumber != null && !plateNumber.isEmpty()) {
            queryWrapper.like(Vehicle::getPlateNumber, plateNumber);
        }
        
        if (userId != null) {
            queryWrapper.eq(Vehicle::getUserId, userId);
        }
        
        return getBaseMapper().selectPage(page, queryWrapper);
    }
} 