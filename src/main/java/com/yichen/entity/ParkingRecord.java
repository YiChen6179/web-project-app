package com.yichen.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("parking_record")
public class ParkingRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long parkingSpotId;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private Long parkingTime;
    private Integer status;
    private String plateNumber;
    private Long vehicleId;
}
