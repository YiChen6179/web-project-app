package com.yichen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yichen.entity.ParkingLot;
import com.yichen.exception.ConstraintViolationException;
import com.yichen.mapper.ParkingZoneMapper;
import com.yichen.entity.ParkingZone;
import com.yichen.service.ConstraintService;
import com.yichen.service.ParkingLotService;
import com.yichen.service.ParkingZoneService;
import com.yichen.utils.BusinessValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 停车区服务实现类
 */
@Service
@RequiredArgsConstructor
public class ParkingZoneServiceImpl extends ServiceImpl<ParkingZoneMapper, ParkingZone> implements ParkingZoneService {

    private final ConstraintService constraintService;
    private final ParkingLotService parkingLotService;

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
    
    /**
     * 重写updateById方法，增加约束检查
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(ParkingZone entity) {
        // 检查停车场是否存在
        Long parkingLotId = entity.getParkingLotId();
        if (parkingLotId != null) {
            ParkingLot parkingLot = parkingLotService.getById(parkingLotId);
            BusinessValidationUtil.checkNotNull(parkingLot, "关联的停车场不存在");
        }
        
        return super.updateById(entity);
    }
    
    /**
     * 重写save方法，增加约束检查
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(ParkingZone entity) {
        // 检查停车场是否存在
        Long parkingLotId = entity.getParkingLotId();
        if (parkingLotId != null) {
            ParkingLot parkingLot = parkingLotService.getById(parkingLotId);
            BusinessValidationUtil.checkNotNull(parkingLot, "关联的停车场不存在");
        }
        
        return super.save(entity);
    }
    
    /**
     * 重写removeById方法，增加约束检查
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Long id) {
        if (!constraintService.canDeleteParkingZone(id)) {
            throw new ConstraintViolationException("无法删除该停车区域，存在关联的停车位");
        }
        return super.removeById(id);
    }
} 