package com.yichen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yichen.exception.ConstraintViolationException;
import com.yichen.mapper.ParkingLotMapper;
import com.yichen.entity.ParkingLot;
import com.yichen.service.ConstraintService;
import com.yichen.service.ParkingLotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 停车场服务实现类
 */
@Service
@RequiredArgsConstructor
public class ParkingLotServiceImpl extends ServiceImpl<ParkingLotMapper, ParkingLot> implements ParkingLotService {

    private final ConstraintService constraintService;

    
    /**
     * 重写removeById方法，增加约束检查
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Long id) {
        if (!constraintService.canDeleteParkingLot(id)) {
            throw new ConstraintViolationException("无法删除该停车场，存在关联的停车区域");
        }
        return super.removeById(id);
    }



    @Override
    public Page<ParkingLot> getParkingLotsWithStatisticsPage(Integer current, Integer size,String name) {
        List<ParkingLot> parkingLots = getBaseMapper().selectWithStatisticsPage(current-1, size,  name);
        return new Page<ParkingLot>(current, size).setRecords(parkingLots);
    }

    @Override
    public ParkingLot getByIdWithStatistics(Long id) {
        return getBaseMapper().getByIdWithStatistics(id);
    }
} 