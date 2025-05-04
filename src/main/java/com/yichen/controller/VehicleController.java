package com.yichen.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yichen.entity.Vehicle;
import com.yichen.service.VehicleService;
import com.yichen.vo.Result;
import com.yichen.vo.VehicleVO;
import com.yichen.utils.BeanConverter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vehicle")
@Api(tags = "车辆管理")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;
    private final BeanConverter beanConverter;

    @GetMapping("/list")
    @ApiOperation("获取车辆列表")
    public Result<Page<VehicleVO>> list(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("每页条数") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("车牌号") @RequestParam(required = false) String plateNumber,
            @ApiParam("用户ID") @RequestParam(required = false) Long userId) {
        
        Page<Vehicle> page = vehicleService.listVehicles(current, size, plateNumber, userId);
        Page<VehicleVO> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(beanConverter.convertList(page.getRecords(), VehicleVO.class));
        return Result.success(result);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID获取车辆")
    public Result<VehicleVO> getById(@ApiParam("车辆ID") @PathVariable Long id) {
        Vehicle vehicle = vehicleService.getById(id);
        if (vehicle == null) {
            return Result.error("车辆不存在");
        }
        VehicleVO vehicleVO = beanConverter.convert(vehicle, VehicleVO.class);
        return Result.success(vehicleVO);
    }

    @PostMapping
    @ApiOperation("添加车辆")
    public Result<VehicleVO> add(@RequestBody VehicleVO vehicleVO) {
        Vehicle vehicle = beanConverter.convert(vehicleVO, Vehicle.class);
        boolean success = vehicleService.save(vehicle);
        if (success) {
            VehicleVO resultVO = beanConverter.convert(vehicle, VehicleVO.class);
            return Result.success(resultVO);
        }
        return Result.error("添加车辆失败");
    }

    @PutMapping
    @ApiOperation("更新车辆")
    public Result<Void> update(@RequestBody VehicleVO vehicleVO) {
        if (vehicleVO.getId() == null) {
            return Result.error("车辆ID不能为空");
        }
        Vehicle vehicle = beanConverter.convert(vehicleVO, Vehicle.class);
        boolean success = vehicleService.updateById(vehicle);
        if (success) {
            return Result.success();
        }
        return Result.error("更新车辆失败");
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除车辆")
    public Result<Void> delete(@ApiParam("车辆ID") @PathVariable Long id) {
        boolean success = vehicleService.removeById(id);
        if (success) {
            return Result.success();
        }
        return Result.error("删除车辆失败");
    }
} 