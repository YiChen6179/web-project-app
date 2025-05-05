package com.yichen.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(value = "停车记录视图对象", description = "用于前后端交互的停车记录数据")
public class ParkingRecordVO {
    @ApiModelProperty(value = "停车记录ID", example = "1", position = 1)
    private Long id;
    
    @ApiModelProperty(value = "停车位ID", example = "1", required = true, position = 2)
    private Long parkingSpotId;
    
    @ApiModelProperty(value = "入场时间", example = "2023-05-20T10:30:00", position = 3)
    private LocalDateTime entryTime;
    
    @ApiModelProperty(value = "出场时间", example = "2023-05-20T12:30:00", position = 4)
    private LocalDateTime exitTime;
    
    @ApiModelProperty(value = "停车时长(分钟)", example = "120", position = 5)
    private Long parkingTime;
    
    @ApiModelProperty(value = "状态", example = "1", notes = "0-停车中，1-已完成", allowableValues = "0, 1", position = 6)
    private Integer status;
    
    @ApiModelProperty(value = "车牌号", example = "京A12345", position = 7)
    private String plateNumber;
    
    @ApiModelProperty(value = "车辆ID", example = "1", required = true, position = 8)
    private Long vehicleId;
}
