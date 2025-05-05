package com.yichen.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yichen.entity.ParkingLot;
import com.yichen.service.ParkingLotService;
import com.yichen.vo.Result;
import com.yichen.vo.ParkingLotVO;
import com.yichen.utils.BeanConverter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parking-lot")
@Api(tags = "停车场管理", description = "提供停车场的增删改查接口")
@RequiredArgsConstructor
public class ParkingLotController {

    private final ParkingLotService parkingLotService;
    private final BeanConverter beanConverter;

    @GetMapping("/list")
    @ApiOperation(value = "获取停车场列表", notes = "分页获取停车场列表，可根据停车场名称进行筛选")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    public Result<Page<ParkingLotVO>> list(
            @ApiParam(value = "页码", defaultValue = "1", example = "1") 
            @RequestParam(defaultValue = "1") Integer current,
            
            @ApiParam(value = "每页条数", defaultValue = "10", example = "10") 
            @RequestParam(defaultValue = "10") Integer size,
            
            @ApiParam(value = "停车场名称", example = "中央广场停车场") 
            @RequestParam(required = false) String name) {
        
        Page<ParkingLot> page = parkingLotService.getParkingLotsWithStatisticsPage(current, size, name);
        Page<ParkingLotVO> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(beanConverter.convertList(page.getRecords(), ParkingLotVO.class));
        return Result.success(result);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID获取停车场", notes = "通过停车场ID获取停车场详细信息")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 404, message = "停车场不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    public Result<ParkingLotVO> getById(
            @ApiParam(value = "停车场ID", required = true, example = "1") 
            @PathVariable Long id) {
        ParkingLot parkingLot = parkingLotService.getByIdWithStatistics(id);
        if (parkingLot == null) {
            return Result.error("停车场不存在");
        }
        ParkingLotVO parkingLotVO = beanConverter.convert(parkingLot, ParkingLotVO.class);
        return Result.success(parkingLotVO);
    }

    @PostMapping
    @ApiOperation(value = "添加停车场", notes = "创建新的停车场")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 400, message = "参数错误"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    public Result<ParkingLotVO> add(
            @ApiParam(value = "停车场信息", required = true) 
            @RequestBody ParkingLotVO parkingLotVO) {
        ParkingLot parkingLot = beanConverter.convert(parkingLotVO, ParkingLot.class);
        boolean success = parkingLotService.save(parkingLot);
        if (success) {
            ParkingLotVO resultVO = beanConverter.convert(parkingLot, ParkingLotVO.class);
            return Result.success(resultVO);
        }
        return Result.error("添加停车场失败");
    }

    @PutMapping
    @ApiOperation(value = "更新停车场", notes = "修改现有停车场信息")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 400, message = "参数错误"),
        @ApiResponse(code = 404, message = "停车场不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    public Result<Void> update(
            @ApiParam(value = "停车场信息", required = true) 
            @RequestBody ParkingLotVO parkingLotVO) {
        if (parkingLotVO.getId() == null) {
            return Result.error("停车场ID不能为空");
        }
        ParkingLot parkingLot = beanConverter.convert(parkingLotVO, ParkingLot.class);
        boolean success = parkingLotService.updateById(parkingLot);
        if (success) {
            return Result.success();
        }
        return Result.error("更新停车场失败");
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除停车场", notes = "通过停车场ID删除停车场")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 404, message = "停车场不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    public Result<Void> delete(
            @ApiParam(value = "停车场ID", required = true, example = "1") 
            @PathVariable Long id) {
        boolean success = parkingLotService.removeById(id);
        if (success) {
            return Result.success();
        }
        return Result.error("删除停车场失败");
    }
} 