package com.yichen.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "停车区视图对象", description = "用于前后端交互的停车区数据")
public class ParkingZoneVO {
    @ApiModelProperty(value = "停车区ID", example = "1", position = 1)
    private Long id;
    
    @ApiModelProperty(value = "所属停车场ID", example = "1", required = true, position = 2)
    private Long parkingLotId;
    
    @ApiModelProperty(value = "停车区名称", example = "A区", required = true, position = 3)
    private String zoneName;
    
    @ApiModelProperty(value = "所在楼层", example = "B1", position = 4)
    private String floor;
}
