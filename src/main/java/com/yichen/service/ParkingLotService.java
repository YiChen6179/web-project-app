package com.yichen.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yichen.entity.ParkingLot;

/**
 * 停车场服务接口
 */
public interface ParkingLotService extends IService<ParkingLot> {
    /**
     * 分页查询停车场列表，支持按名称模糊查询
     * @param current 当前页
     * @param size 每页数量
     * @param name 停车场名称（可选）
     * @return 停车场分页数据
     */
    Page<ParkingLot> listParkingLots(Integer current, Integer size, String name);
} 