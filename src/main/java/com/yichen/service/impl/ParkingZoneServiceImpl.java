package com.yichen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yichen.mapper.ParkingZoneMapper;
import com.yichen.entity.ParkingZone;
import com.yichen.service.ParkingZoneService;
import org.springframework.stereotype.Service;

/**
 * 停车区服务实现类
 */
@Service
public class ParkingZoneServiceImpl extends ServiceImpl<ParkingZoneMapper, ParkingZone> implements ParkingZoneService {

    @Override
    public Page<ParkingZone> listParkingZones(Integer current, Integer size, String zoneName, Long parkingLotId) {
        Page<ParkingZone> page = new Page<>(current, size);
        LambdaQueryWrapper<ParkingZone> queryWrapper = new LambdaQueryWrapper<>();
        
        if (zoneName != null && !zoneName.isEmpty()) {
            queryWrapper.like(ParkingZone::getZoneName, zoneName);
        }
        
        if (parkingLotId != null) {
            queryWrapper.eq(ParkingZone::getParkingLotId, parkingLotId);
        }
        
        return getBaseMapper().selectPage(page, queryWrapper);
    }
} 