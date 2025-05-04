package com.yichen.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Token视图对象，用于登录成功后返回token和用户信息
 */
@Data
@ApiModel(value = "Token视图对象", description = "登录成功后返回的Token和用户信息")
public class TokenVO {
    @ApiModelProperty(value = "认证令牌", example = "eyJhbGciOiJIUzI1NiJ9...", position = 1)
    private String token;
    
    @ApiModelProperty(value = "用户信息", position = 2)
    private UserVO user;
} 