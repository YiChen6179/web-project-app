package com.yichen.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yichen.entity.ParkingSpot;

/**
 * 停车位服务接口
 */
public interface ParkingSpotService extends IService<ParkingSpot> {
    /**
     * 分页查询停车位列表，支持按车位号、区域ID和状态筛选
     * @param current 当前页
     * @param size 每页数量
     * @param spotNumber 车位号（可选）
     * @param zoneId 区域ID（可选）
     * @param status 状态（可选）
     * @return 停车位分页数据
     */
    Page<ParkingSpot> listParkingSpots(Integer current, Integer size, String spotNumber, Long zoneId, Integer status);
} 