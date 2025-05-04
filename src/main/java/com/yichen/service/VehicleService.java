package com.yichen.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yichen.entity.Vehicle;
import org.springframework.transaction.annotation.Transactional;

/**
 * 车辆服务接口
 */
public interface VehicleService extends IService<Vehicle> {
    /**
     * 分页查询车辆列表，支持按车牌号和用户ID筛选
     * @param current 当前页
     * @param size 每页数量
     * @param plateNumber 车牌号（可选）
     * @param userId 用户ID（可选）
     * @return 车辆分页数据
     */
    Page<Vehicle> listVehicles(Integer current, Integer size, String plateNumber, Long userId);

    @Transactional(rollbackFor = Exception.class)
    boolean removeById(Long id);
}