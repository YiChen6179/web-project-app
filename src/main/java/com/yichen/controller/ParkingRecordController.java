package com.yichen.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yichen.entity.ParkingRecord;
import com.yichen.service.ParkingRecordService;
import com.yichen.vo.Result;
import com.yichen.vo.ParkingRecordVO;
import com.yichen.utils.BeanConverter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parking-record")
@Api(tags = "停车记录管理", description = "提供停车记录的增删改查和车辆出场接口")
@RequiredArgsConstructor
public class ParkingRecordController {

    private final ParkingRecordService parkingRecordService;
    private final BeanConverter beanConverter;

    @GetMapping("/list")
    @ApiOperation(value = "获取停车记录列表", notes = "分页获取停车记录列表，可根据车辆ID和状态进行筛选")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    public Result<Page<ParkingRecordVO>> list(
            @ApiParam(value = "页码", defaultValue = "1", example = "1") 
            @RequestParam(defaultValue = "1") Integer current,
            
            @ApiParam(value = "每页条数", defaultValue = "10", example = "10") 
            @RequestParam(defaultValue = "10") Integer size,
            
            @ApiParam(value = "车辆ID", example = "1") 
            @RequestParam(required = false) Long vehicleId,
            
            @ApiParam(value = "状态(0-空闲中，1-停车中)", example = "1", allowableValues = "0, 1")
            @RequestParam(required = false) Integer status) {
        
        Page<ParkingRecord> page = parkingRecordService.listParkingRecords(current, size, vehicleId, status);
        Page<ParkingRecordVO> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(beanConverter.convertList(page.getRecords(), ParkingRecordVO.class));
        return Result.success(result);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "根据ID获取停车记录", notes = "通过停车记录ID获取停车详细信息")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 404, message = "停车记录不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    public Result<ParkingRecordVO> getById(
            @ApiParam(value = "停车记录ID", required = true, example = "1") 
            @PathVariable Long id) {
        ParkingRecord parkingRecord = parkingRecordService.getById(id);
        if (parkingRecord == null) {
            return Result.error("停车记录不存在");
        }
        ParkingRecordVO parkingRecordVO = beanConverter.convert(parkingRecord, ParkingRecordVO.class);
        return Result.success(parkingRecordVO);
    }

    @PostMapping
    @ApiOperation(value = "添加停车记录", notes = "创建新的停车记录，通常用于车辆入场")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 400, message = "参数错误"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    public Result<ParkingRecordVO> add(
            @ApiParam(value = "停车记录信息", required = true) 
            @RequestBody ParkingRecordVO parkingRecordVO) {
        ParkingRecord parkingRecord = beanConverter.convert(parkingRecordVO, ParkingRecord.class);
        ParkingRecord result = parkingRecordService.createParkingRecord(parkingRecord);
        ParkingRecordVO resultVO = beanConverter.convert(result, ParkingRecordVO.class);
        return Result.success(resultVO);
    }

    @PutMapping
    @ApiOperation(value = "更新停车记录", notes = "修改现有停车记录信息")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 400, message = "参数错误"),
        @ApiResponse(code = 404, message = "停车记录不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    public Result<Void> update(
            @ApiParam(value = "停车记录信息", required = true) 
            @RequestBody ParkingRecordVO parkingRecordVO) {
        if (parkingRecordVO.getId() == null) {
            return Result.error("停车记录ID不能为空");
        }
        ParkingRecord parkingRecord = beanConverter.convert(parkingRecordVO, ParkingRecord.class);
        boolean success = parkingRecordService.updateById(parkingRecord);
        if (success) {
            return Result.success();
        }
        return Result.error("更新停车记录失败");
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除停车记录", notes = "通过停车记录ID删除停车记录")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 404, message = "停车记录不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    public Result<Void> delete(
            @ApiParam(value = "停车记录ID", required = true, example = "1") 
            @PathVariable Long id) {
        boolean success = parkingRecordService.removeById(id);
        if (success) {
            return Result.success();
        }
        return Result.error("删除停车记录失败");
    }

    @PostMapping("/{id}/exit")
    @ApiOperation(value = "车辆出场", notes = "记录车辆出场时间，计算停车时长，并修改停车位状态为空闲")
    @ApiResponses({
        @ApiResponse(code = 200, message = "操作成功", response = Result.class),
        @ApiResponse(code = 400, message = "参数错误"),
        @ApiResponse(code = 404, message = "停车记录不存在"),
        @ApiResponse(code = 500, message = "服务器内部错误")
    })
    public Result<ParkingRecordVO> exitParking(
            @ApiParam(value = "停车记录ID", required = true, example = "1") 
            @PathVariable Long id) {
        ParkingRecord parkingRecord = parkingRecordService.exitParking(id);
        ParkingRecordVO parkingRecordVO = beanConverter.convert(parkingRecord, ParkingRecordVO.class);
        return Result.success(parkingRecordVO);
    }
} 