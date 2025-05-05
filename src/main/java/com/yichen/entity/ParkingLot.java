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
@TableName("parking_lot")
public class ParkingLot {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String lotName;
    private String address;

    // 非持久化字段 - 停车位总数
    @TableField(exist = false)
    private Integer totalSpot;

    // 非持久化字段 - 正在使用的车位数
    @TableField(exist = false)
    private Integer usedSpot;
}