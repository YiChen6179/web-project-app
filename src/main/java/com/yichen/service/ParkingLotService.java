package com.yichen.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yichen.entity.ParkingLot;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ParkingLotService extends IService<ParkingLot> {

    @Transactional(rollbackFor = Exception.class)
    boolean removeById(Long id);


    /**
     * 分页查询停车场及其统计信息
     * @param current 当前页码
     * @param size 每页数量
     * @param name 停车场名称
     * @return 分页后的停车场列表
     */
    Page<ParkingLot> getParkingLotsWithStatisticsPage(Integer current, Integer size , String name);

    ParkingLot getByIdWithStatistics(Long id);
}