package com.yichen.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yichen.entity.User;
import com.yichen.service.UserService;
import com.yichen.vo.Result;
import com.yichen.vo.UserVO;
import com.yichen.utils.BeanConverter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Api(tags = "用户管理", description = "提供用户的增删改查接口")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final BeanConverter beanConverter;

    @GetMapping("/list")
    @ApiOperation(value = "获取用户列表", notes = "分页获取用户列表，可根据用户名进行筛选")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    public Result<Page<UserVO>> list(
            @ApiParam(value = "页码", defaultValue = "1", example = "1") 
            @RequestParam(defaultValue = "1") Integer current,
            
            @ApiParam(value = "每页条数", defaultValue = "10", example = "10") 
            @RequestParam(defaultValue = "10") Integer size,
            
            @ApiParam(value = "用户名", example = "zhangsan") 
            @RequestParam(required = false) String username) {
        
        Page<UserVO> result = userService.listUsers(current, size, username);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID获取用户", notes = "通过用户ID获取用户详细信息")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 404, message = "用户不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    public Result<UserVO> getById(
            @ApiParam(value = "用户ID", required = true, example = "1") 
            @PathVariable Long id) {
        UserVO user = userService.getUserById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        return Result.success(user);
    }

    @PostMapping
    @ApiOperation(value = "添加用户", notes = "创建新用户")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 400, message = "参数错误"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    public Result<UserVO> add(
            @ApiParam(value = "用户信息", required = true) 
            @RequestBody UserVO userVO) {
        User user = beanConverter.convert(userVO, User.class);
        boolean success = userService.addUser(user);
        if (success) {
            UserVO resultVO = userService.getUserById(user.getId());
            return Result.success(resultVO);
        }
        return Result.error("添加用户失败");
    }

    @PutMapping
    @ApiOperation(value = "更新用户", notes = "修改现有用户信息")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 400, message = "参数错误"),
        @ApiResponse(code = 404, message = "用户不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    public Result<Void> update(
            @ApiParam(value = "用户信息", required = true) 
            @RequestBody UserVO userVO) {
        if (userVO.getId() == null) {
            return Result.error("用户ID不能为空");
        }
        User user = beanConverter.convert(userVO, User.class);
        boolean success = userService.updateUser(user);
        if (success) {
            return Result.success();
        }
        return Result.error("更新用户失败");
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除用户", notes = "通过用户ID删除用户")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 404, message = "用户不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    public Result<Void> delete(
            @ApiParam(value = "用户ID", required = true, example = "1") 
            @PathVariable Long id) {
        boolean success = userService.deleteUser(id);
        if (success) {
            return Result.success();
        }
        return Result.error("删除用户失败");
    }
} 