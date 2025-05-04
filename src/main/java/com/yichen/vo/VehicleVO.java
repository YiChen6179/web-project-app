package com.yichen.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "车辆视图对象", description = "用于前后端交互的车辆数据")
public class VehicleVO {
    @ApiModelProperty(value = "车辆ID", example = "1", position = 1)
    private Long id;
    
    @ApiModelProperty(value = "所属用户ID", example = "1", required = true, position = 2)
    private Long userId;
    
    @ApiModelProperty(value = "车牌号", example = "京A12345", required = true, position = 3, 
                    notes = "车牌号格式：省份简称+字母+数字/字母，如：京A12345")
    private String plateNumber;
    
    @ApiModelProperty(value = "车辆类型", example = "轿车", position = 4)
    private String vehicleType;
    
    @ApiModelProperty(value = "车辆颜色", example = "白色", position = 5)
    private String vehicleColor;
}
