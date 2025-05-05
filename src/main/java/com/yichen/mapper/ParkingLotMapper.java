package com.yichen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yichen.entity.ParkingLot;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingLotMapper extends BaseMapper<ParkingLot> {
    /**
     * 查询停车场及其统计信息
     */
    long countWithStatistics();

    List<ParkingLot> selectWithStatisticsPage(int current, int size,  String name);

    ParkingLot getByIdWithStatistics(Long id);
}