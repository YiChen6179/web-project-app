package com.yichen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yichen.entity.ParkingRecord;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingRecordMapper extends BaseMapper<ParkingRecord> {
    
    /**
     * 根据车位ID获取停车场名称
     * @param spotId 车位ID
     * @return 停车场名称
     */
    @Select("SELECT l.lot_name FROM parking_lot l " +
            "INNER JOIN parking_zone z ON l.id = z.parking_lot_id " +
            "INNER JOIN parking_spot s ON z.id = s.parking_zone_id " +
            "WHERE s.id = #{spotId}")
    String getLotNameBySpotId(Long spotId);
}
