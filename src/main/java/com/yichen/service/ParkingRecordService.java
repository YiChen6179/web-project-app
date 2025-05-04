package com.yichen.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yichen.entity.ParkingRecord;

/**
 * 停车记录服务接口
 */
public interface ParkingRecordService extends IService<ParkingRecord> {
    /**
     * 分页查询停车记录列表，支持按车辆ID和状态筛选
     * @param current 当前页
     * @param size 每页数量
     * @param vehicleId 车辆ID（可选）
     * @param status 状态（可选）
     * @return 停车记录分页数据
     */
    Page<ParkingRecord> listParkingRecords(Integer current, Integer size, Long vehicleId, Integer status);
    
    /**
     * 车辆出场处理
     * @param id
     * @return 更新后的停车记录
     */
    ParkingRecord exitParking(Long id);
} 