package com.yichen.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("parking_spot")
public class ParkingSpot {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long parkingZoneId;
    private String spotNumber;
    private Double length;
    private Double width;
    private Integer status; // 0-空闲，1-占用

    @TableField(exist = false)
    private String parkingLotName;
}
