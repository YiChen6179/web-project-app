package com.yichen.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yichen.entity.ParkingZone;

/**
 * 停车区服务接口
 */
public interface ParkingZoneService extends IService<ParkingZone> {
    /**
     * 分页查询停车区列表，支持按区域名称和停车场ID筛选
     * @param current 当前页
     * @param size 每页数量
     * @param zoneName 区域名称（可选）
     * @param parkingLotId 停车场ID（可选）
     * @return 停车区分页数据
     */
    Page<ParkingZone> listParkingZones(Integer current, Integer size, String zoneName, Long parkingLotId);
}