package com.yichen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yichen.entity.ParkingLot;
import org.apache.ibatis.annotations.Select;
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
    
    /**
     * 根据停车场ID获取其包含的所有区域ID
     * @param lotId 停车场ID
     * @return 区域ID列表
     */
    @Select("SELECT id FROM parking_zone WHERE parking_lot_id = #{lotId}")
    List<Long> getZoneIdsByLotId(Long lotId);
}