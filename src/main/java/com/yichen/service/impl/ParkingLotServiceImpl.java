package com.yichen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yichen.mapper.ParkingLotMapper;
import com.yichen.entity.ParkingLot;
import com.yichen.service.ParkingLotService;
import org.springframework.stereotype.Service;

/**
 * 停车场服务实现类
 */
@Service
public class ParkingLotServiceImpl extends ServiceImpl<ParkingLotMapper, ParkingLot> implements ParkingLotService {

    @Override
    public Page<ParkingLot> listParkingLots(Integer current, Integer size, String name) {
        Page<ParkingLot> page = new Page<>(current, size);
        LambdaQueryWrapper<ParkingLot> queryWrapper = new LambdaQueryWrapper<>();
        
        if (name != null && !name.isEmpty()) {
            queryWrapper.like(ParkingLot::getLotName, name);
        }
        
        return getBaseMapper().selectPage(page, queryWrapper);
    }
} 