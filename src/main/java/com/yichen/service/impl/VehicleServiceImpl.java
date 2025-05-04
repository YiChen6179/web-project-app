package com.yichen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yichen.entity.User;
import com.yichen.exception.ConstraintViolationException;
import com.yichen.mapper.VehicleMapper;
import com.yichen.entity.Vehicle;
import com.yichen.service.ConstraintService;
import com.yichen.service.UserService;
import com.yichen.service.VehicleService;
import com.yichen.utils.BusinessValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 车辆服务实现类
 */
@Service
@RequiredArgsConstructor
public class VehicleServiceImpl extends ServiceImpl<VehicleMapper, Vehicle> implements VehicleService {

    private final ConstraintService constraintService;
    private final UserService userService;

    @Override
    public Page<Vehicle> listVehicles(Integer current, Integer size, String plateNumber, Long userId) {
        Page<Vehicle> page = new Page<>(current, size);
        LambdaQueryWrapper<Vehicle> queryWrapper = new LambdaQueryWrapper<>();
        
        if (plateNumber != null && !plateNumber.isEmpty()) {
            queryWrapper.like(Vehicle::getPlateNumber, plateNumber);
        }
        
        if (userId != null) {
            queryWrapper.eq(Vehicle::getUserId, userId);
        }
        
        return getBaseMapper().selectPage(page, queryWrapper);
    }
    
    /**
     * 重写updateById方法，增加约束检查
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(Vehicle entity) {
        // 获取原始实体
        Vehicle original = getById(entity.getId());
        BusinessValidationUtil.checkNotNull(original, "车辆不存在");
        
        // 检查用户是否存在
        Long userId = entity.getUserId();
        if (userId != null && !userId.equals(original.getUserId())) {
            User user = userService.getById(userId);
            BusinessValidationUtil.checkNotNull(user, "关联的用户不存在");
        }
        
        // 检查车牌号是否被修改且已被使用
        String plateNumber = entity.getPlateNumber();
        String originalPlateNumber = original.getPlateNumber();
        if (plateNumber != null && !plateNumber.isEmpty() && !plateNumber.equals(originalPlateNumber)) {
            // 检查新车牌号是否唯一
            boolean isUnique = constraintService.isPlateNumberUnique(plateNumber, entity.getId());
            BusinessValidationUtil.check(isUnique, "该车牌号已被其他车辆使用");
            
            // 如果车牌号变更，检查原车牌号是否有进行中的停车记录
            boolean noActiveRecords = constraintService.plateNumberHasNoActiveParkingRecords(originalPlateNumber);
            BusinessValidationUtil.check(noActiveRecords, "该车辆有进行中的停车记录，无法修改车牌号");
        }
        
        return super.updateById(entity);
    }
    
    /**
     * 重写save方法，增加约束检查
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(Vehicle entity) {
        // 检查用户是否存在
        Long userId = entity.getUserId();
        if (userId != null) {
            User user = userService.getById(userId);
            BusinessValidationUtil.checkNotNull(user, "关联的用户不存在");
        }
        
        // 检查车牌号是否唯一
        String plateNumber = entity.getPlateNumber();
        if (plateNumber != null && !plateNumber.isEmpty()) {
            boolean isUnique = constraintService.isPlateNumberUnique(plateNumber, null);
            BusinessValidationUtil.check(isUnique, "该车牌号已被使用");
        }
        
        return super.save(entity);
    }
    
    /**
     * 重写removeById方法，增加约束检查
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Long id) {
        // 检查是否有活跃的停车记录
        boolean noActiveRecords = constraintService.vehicleHasNoActiveParkingRecords(id);
        BusinessValidationUtil.check(noActiveRecords, "该车辆有进行中的停车记录，无法删除");
        
        // 检查是否有历史停车记录
        if (!constraintService.canDeleteVehicle(id)) {
            throw new ConstraintViolationException("无法删除该车辆，存在关联的停车记录");
        }
        
        return super.removeById(id);
    }
} 