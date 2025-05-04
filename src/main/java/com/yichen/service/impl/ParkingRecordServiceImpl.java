package com.yichen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yichen.mapper.ParkingRecordMapper;
import com.yichen.entity.ParkingRecord;
import com.yichen.service.ParkingRecordService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 停车记录服务实现类
 */
@Service
public class ParkingRecordServiceImpl extends ServiceImpl<ParkingRecordMapper, ParkingRecord> implements ParkingRecordService {

    @Override
    public Page<ParkingRecord> listParkingRecords(Integer current, Integer size, Long vehicleId, Integer status) {
        Page<ParkingRecord> page = new Page<>(current, size);
        LambdaQueryWrapper<ParkingRecord> queryWrapper = new LambdaQueryWrapper<>();
        
        if (vehicleId != null) {
            queryWrapper.eq(ParkingRecord::getVehicleId, vehicleId);
        }
        
        if (status != null) {
            queryWrapper.eq(ParkingRecord::getStatus, status);
        }
        
        return getBaseMapper().selectPage(page, queryWrapper);
    }

    @Override
    public ParkingRecord exitParking(Long id) {
        ParkingRecord record = getById(id);
        if (record == null) {
            return null;
        }
        
        // 设置离场时间和状态
        record.setExitTime(LocalDateTime.now());
        record.setStatus(1); // 1-已完成
        
        // 计算停车费用（示例：每小时10元）
        LocalDateTime entryTime = record.getEntryTime();
        LocalDateTime exitTime = record.getExitTime();
        
        if (entryTime != null && exitTime != null) {
            // 计算小时差
            long hours = exitTime.getHour() - entryTime.getHour();
            if (hours < 0) {
                hours = 0;
            }
            record.setFee(hours * 10.0);
        }
        
        updateById(record);
        return record;
    }
} 