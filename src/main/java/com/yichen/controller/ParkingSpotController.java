package com.yichen.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yichen.entity.ParkingSpot;
import com.yichen.service.ParkingSpotService;
import com.yichen.vo.Result;
import com.yichen.vo.ParkingSpotVO;
import com.yichen.utils.BeanConverter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parking-spot")
@Api(tags = "停车位管理", description = "提供停车位的增删改查接口")
@RequiredArgsConstructor
@Slf4j
public class ParkingSpotController {

    private final ParkingSpotService parkingSpotService;
    private final BeanConverter beanConverter;

    @GetMapping("/list")
    @ApiOperation(value = "获取停车位列表", notes = "分页获取停车位列表，可根据车位号、区域ID和状态进行筛选")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    public Result<Page<ParkingSpotVO>> list(
            @ApiParam(value = "页码", defaultValue = "1", example = "1") 
            @RequestParam(defaultValue = "1") Integer current,
            
            @ApiParam(value = "每页条数", defaultValue = "10", example = "10") 
            @RequestParam(defaultValue = "10") Integer size,
            
            @ApiParam(value = "车位号", example = "A-001") 
            @RequestParam(required = false) String spotNumber,
            
            @ApiParam(value = "停车区ID", example = "1") 
            @RequestParam(required = false) Long zoneId,
            
            @ApiParam(value = "状态(0-空闲，1-占用)", example = "0", allowableValues = "0, 1") 
            @RequestParam(required = false) Integer status) {
        
        Page<ParkingSpot> page = parkingSpotService.listParkingSpots(current, size, spotNumber, zoneId, status);
        Page<ParkingSpotVO> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(beanConverter.convertList(page.getRecords(), ParkingSpotVO.class));
        return Result.success(result);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID获取停车位", notes = "通过停车位ID获取停车位详细信息")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 404, message = "停车位不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    public Result<ParkingSpotVO> getById(
            @ApiParam(value = "停车位ID", required = true, example = "1") 
            @PathVariable Long id) {
        ParkingSpot parkingSpot = parkingSpotService.getById(id);
        if (parkingSpot == null) {
            return Result.error("停车位不存在");
        }
        ParkingSpotVO parkingSpotVO = beanConverter.convert(parkingSpot, ParkingSpotVO.class);
        return Result.success(parkingSpotVO);
    }

    @PostMapping
    @ApiOperation(value = "添加停车位", notes = "创建新的停车位")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 400, message = "参数错误"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    public Result<ParkingSpotVO> add(
            @ApiParam(value = "停车位信息", required = true) 
            @RequestBody ParkingSpotVO parkingSpotVO) {
        ParkingSpot parkingSpot = beanConverter.convert(parkingSpotVO, ParkingSpot.class);
        boolean success = parkingSpotService.save(parkingSpot);
        if (success) {
            ParkingSpotVO resultVO = beanConverter.convert(parkingSpot, ParkingSpotVO.class);
            return Result.success(resultVO);
        }
        return Result.error("添加停车位失败");
    }

    @PutMapping
    @ApiOperation(value = "更新停车位", notes = "修改现有停车位信息")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 400, message = "参数错误"),
        @ApiResponse(code = 404, message = "停车位不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    public Result<Void> update(
            @ApiParam(value = "停车位信息", required = true) 
            @RequestBody ParkingSpotVO parkingSpotVO) {
        if (parkingSpotVO.getId() == null) {
            return Result.error("停车位ID不能为空");
        }
        ParkingSpot parkingSpot = beanConverter.convert(parkingSpotVO, ParkingSpot.class);
        boolean success = parkingSpotService.updateById(parkingSpot);
        if (success) {
            return Result.success();
        }
        return Result.error("更新停车位失败");
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除停车位", notes = "通过停车位ID删除停车位")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 404, message = "停车位不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    public Result<Void> delete(
            @ApiParam(value = "停车位ID", required = true, example = "1") 
            @PathVariable Long id) {
        boolean success = parkingSpotService.removeById(id);
        if (success) {
            return Result.success();
        }
        return Result.error("删除停车位失败");
    }

    //根据停车场获取所属停车位
    @PostMapping("/listByParkingLot")
    @ApiOperation(value = "根据停车场获取所属停车位", notes = "根据停车场名称获取所属停车位")
    public Result<List<ParkingSpotVO>> listByParkingLot(
            @ApiParam(value = "停车场名称", required = true)
            @RequestParam String parkingLotName,
            @ApiParam(value = "状态(0-空闲，1-占用)", example = "0", allowableValues = "0, 1")
            @RequestParam Integer status){
        List<ParkingSpot> parkingSpotList = parkingSpotService.listByParkingLot(parkingLotName,status);
        List<ParkingSpotVO> parkingSpotVOS = beanConverter.convertList(parkingSpotList, ParkingSpotVO.class);
        return Result.success(parkingSpotVOS);
    }
} 