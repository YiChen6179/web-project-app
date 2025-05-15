package com.yichen.controller;

import com.yichen.service.DashboardService;
import com.yichen.vo.DashboardDataVO;
import com.yichen.vo.ParkingLotVO;
import com.yichen.vo.ParkingRecordVO;
import com.yichen.vo.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@Api(tags = "仪表盘API", description = "获取系统统计数据")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping()
    @ApiOperation(value = "获取仪表盘完整数据", notes = "返回仪表盘所需的所有数据，包括各种统计数据、停车场占用率和最近停车记录")
    public Result<DashboardDataVO> getDashboardData() {
        return Result.success(dashboardService.getDashboardData());
    }

    @GetMapping("/count/parking-lots")
    @ApiOperation(value = "获取停车场总数", notes = "返回系统中所有停车场的数量")
    public Result<Integer> getTotalParkingLots() {
        return Result.success(dashboardService.getTotalParkingLots());
    }

    @GetMapping("/count/parking-spots")
    @ApiOperation(value = "获取车位总数", notes = "返回系统中所有车位的数量")
    public Result<Integer> getTotalParkingSpots() {
        return Result.success(dashboardService.getTotalParkingSpots());
    }

    @GetMapping("/count/used-spots")
    @ApiOperation(value = "获取已使用车位数", notes = "返回当前正在使用的车位数量")
    public Result<Integer> getCurrentUsedSpots() {
        return Result.success(dashboardService.getCurrentUsedSpots());
    }

    @GetMapping("/count/vehicles")
    @ApiOperation(value = "获取注册车辆数", notes = "返回系统中注册的车辆总数")
    public Result<Integer> getRegisteredVehicles() {
        return Result.success(dashboardService.getRegisteredVehicles());
    }

    @GetMapping("/occupancy")
    @ApiOperation(value = "获取停车场占用率", notes = "返回停车场占用率数据，最多5条")
    public Result<List<ParkingLotVO>> getParkingLotOccupancies() {
        return Result.success(dashboardService.getParkingLotOccupancies());
    }

    @GetMapping("/recent-records")
    @ApiOperation(value = "获取最近停车记录", notes = "返回最近的停车记录，最多5条")
    public Result<List<ParkingRecordVO>> getRecentParkingRecords() {
        return Result.success(dashboardService.getRecentParkingRecords());
    }
} 