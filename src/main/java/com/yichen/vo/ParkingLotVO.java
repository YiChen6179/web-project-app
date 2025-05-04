package com.yichen.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "停车场视图对象", description = "用于前后端交互的停车场数据")
public class ParkingLotVO {
    @ApiModelProperty(value = "停车场ID", example = "1", position = 1)
    private Long id;
    
    @ApiModelProperty(value = "停车场名称", example = "中央广场停车场", required = true, position = 2)
    private String lotName;
    
    @ApiModelProperty(value = "停车场地址", example = "北京市朝阳区中央广场B1层", required = true, position = 3)
    private String address;
    
    @ApiModelProperty(value = "总车位数", example = "100", position = 4)
    private Integer totalSpot;
    
    @ApiModelProperty(value = "可用车位数", example = "50", position = 5)
    private Integer availableSpot;
}
