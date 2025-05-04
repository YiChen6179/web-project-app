package com.yichen.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yichen.entity.ParkingZone;
import com.yichen.service.ParkingZoneService;
import com.yichen.vo.Result;
import com.yichen.vo.ParkingZoneVO;
import com.yichen.utils.BeanConverter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parking-zone")
@Api(tags = "停车区管理", description = "提供停车区的增删改查接口")
@RequiredArgsConstructor
public class ParkingZoneController {

    private final ParkingZoneService parkingZoneService;
    private final BeanConverter beanConverter;

    @GetMapping("/list")
    @ApiOperation(value = "获取停车区列表", notes = "分页获取停车区列表，可根据区名称和停车场ID进行筛选")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    public Result<Page<ParkingZoneVO>> list(
            @ApiParam(value = "页码", defaultValue = "1", example = "1") 
            @RequestParam(defaultValue = "1") Integer current,
            
            @ApiParam(value = "每页条数", defaultValue = "10", example = "10") 
            @RequestParam(defaultValue = "10") Integer size,
            
            @ApiParam(value = "区名称", example = "A区") 
            @RequestParam(required = false) String zoneName,
            
            @ApiParam(value = "停车场ID", example = "1") 
            @RequestParam(required = false) Long parkingLotId) {
        
        Page<ParkingZone> page = parkingZoneService.listParkingZones(current, size, zoneName, parkingLotId);
        Page<ParkingZoneVO> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(beanConverter.convertList(page.getRecords(), ParkingZoneVO.class));
        return Result.success(result);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID获取停车区", notes = "通过停车区ID获取停车区详细信息")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 404, message = "停车区不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    public Result<ParkingZoneVO> getById(
            @ApiParam(value = "停车区ID", required = true, example = "1") 
            @PathVariable Long id) {
        ParkingZone parkingZone = parkingZoneService.getById(id);
        if (parkingZone == null) {
            return Result.error("停车区不存在");
        }
        ParkingZoneVO parkingZoneVO = beanConverter.convert(parkingZone, ParkingZoneVO.class);
        return Result.success(parkingZoneVO);
    }

    @PostMapping
    @ApiOperation(value = "添加停车区", notes = "创建新的停车区")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 400, message = "参数错误"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    public Result<ParkingZoneVO> add(
            @ApiParam(value = "停车区信息", required = true) 
            @RequestBody ParkingZoneVO parkingZoneVO) {
        ParkingZone parkingZone = beanConverter.convert(parkingZoneVO, ParkingZone.class);
        boolean success = parkingZoneService.save(parkingZone);
        if (success) {
            ParkingZoneVO resultVO = beanConverter.convert(parkingZone, ParkingZoneVO.class);
            return Result.success(resultVO);
        }
        return Result.error("添加停车区失败");
    }

    @PutMapping
    @ApiOperation(value = "更新停车区", notes = "修改现有停车区信息")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 400, message = "参数错误"),
        @ApiResponse(code = 404, message = "停车区不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    public Result<Void> update(
            @ApiParam(value = "停车区信息", required = true) 
            @RequestBody ParkingZoneVO parkingZoneVO) {
        if (parkingZoneVO.getId() == null) {
            return Result.error("停车区ID不能为空");
        }
        ParkingZone parkingZone = beanConverter.convert(parkingZoneVO, ParkingZone.class);
        boolean success = parkingZoneService.updateById(parkingZone);
        if (success) {
            return Result.success();
        }
        return Result.error("更新停车区失败");
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除停车区", notes = "通过停车区ID删除停车区")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 404, message = "停车区不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    public Result<Void> delete(
            @ApiParam(value = "停车区ID", required = true, example = "1") 
            @PathVariable Long id) {
        boolean success = parkingZoneService.removeById(id);
        if (success) {
            return Result.success();
        }
        return Result.error("删除停车区失败");
    }
} 