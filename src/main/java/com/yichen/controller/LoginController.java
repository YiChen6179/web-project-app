package com.yichen.controller;

import com.yichen.service.AuthService;
import com.yichen.vo.LoginVO;
import com.yichen.vo.Result;
import com.yichen.vo.TokenVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Api(tags = "认证管理", description = "提供用户登录、登出等认证接口")
@RequiredArgsConstructor
public class LoginController {
    
    private final AuthService authService;
    
    @PostMapping("/login")
    @ApiOperation(value = "用户登录", notes = "通过用户名和密码进行登录认证，返回Token")
    @ApiResponses({
        @ApiResponse(code = 200, message = "登录成功", response = Result.class),
        @ApiResponse(code = 400, message = "用户名或密码不正确"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    public Result<TokenVO> login(
            @ApiParam(value = "登录信息", required = true) 
            @RequestBody LoginVO loginVO) {
        TokenVO tokenVO = authService.login(loginVO.getUsername(), loginVO.getPassword());
        
        if (tokenVO == null) {
            return Result.error("用户名或密码不正确");
        }
        
        return Result.success("登录成功", tokenVO);
    }
    
    @PostMapping("/logout")
    @ApiOperation(value = "用户登出", notes = "退出当前登录状态(无状态认证下客户端只需丢弃token)")
    @ApiResponses({
        @ApiResponse(code = 200, message = "登出成功", response = Result.class),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    public Result<Void> logout() {
        // 无状态认证下，服务端无需处理登出
        return Result.success("登出成功");
    }
} 