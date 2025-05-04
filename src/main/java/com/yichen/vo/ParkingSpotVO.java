package com.yichen.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "停车位视图对象", description = "用于前后端交互的停车位数据")
public class ParkingSpotVO {
    @ApiModelProperty(value = "停车位ID", example = "1", position = 1)
    private Long id;
    
    @ApiModelProperty(value = "所属停车区ID", example = "1", required = true, position = 2)
    private Long parkingZoneId;
    
    @ApiModelProperty(value = "停车位状态", example = "0", notes = "0-空闲，1-占用", allowableValues = "0, 1", position = 3)
    private Integer status; // 0-空闲，1-占用
}
