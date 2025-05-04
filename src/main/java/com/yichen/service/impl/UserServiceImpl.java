package com.yichen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yichen.exception.ConstraintViolationException;
import com.yichen.mapper.UserMapper;
import com.yichen.entity.User;
import com.yichen.service.ConstraintService;
import com.yichen.service.UserService;
import com.yichen.utils.BeanConverter;
import com.yichen.utils.BusinessValidationUtil;
import com.yichen.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 用户服务实现类
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final BeanConverter beanConverter;
    private final ConstraintService constraintService;

    @Override
    public Page<UserVO> listUsers(Integer current, Integer size, String username) {
        Page<User> page = new Page<>(current, size);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        
        if (username != null && !username.isEmpty()) {
            queryWrapper.like(User::getUsername, username);
        }
        
        Page<User> result = getBaseMapper().selectPage(page, queryWrapper);
        
        // 使用转换工具类转换为VO
        List<UserVO> userVOList = beanConverter.convertList(result.getRecords(), UserVO.class);
        
        // 创建VO的分页对象
        Page<UserVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(userVOList);
        
        return voPage;
    }

    @Override
    public UserVO getUserById(Long id) {
        User user = getById(id);
        return beanConverter.convert(user, UserVO.class);
    }
    
    /**
     * 重写updateById方法，增加约束检查
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateById(User entity) {
        // 获取原始实体
        User original = getById(entity.getId());
        BusinessValidationUtil.checkNotNull(original, "用户不存在");
        
        // 检查用户名是否被修改且已被使用
        String username = entity.getUsername();
        if (username != null && !username.isEmpty() && !username.equals(original.getUsername())) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getUsername, username)
                       .ne(User::getId, entity.getId());
            long count = count(queryWrapper);
            BusinessValidationUtil.check(count == 0, "该用户名已被使用");
        }
        
        return super.updateById(entity);
    }
    
    /**
     * 重写save方法，增加约束检查
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(User entity) {
        // 检查用户名是否唯一
        String username = entity.getUsername();
        if (username != null && !username.isEmpty()) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getUsername, username);
            long count = count(queryWrapper);
            BusinessValidationUtil.check(count == 0, "该用户名已被使用");
        }
        
        return super.save(entity);
    }
    
    /**
     * 重写removeById方法，增加约束检查
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Long id) {
        if (!constraintService.canDeleteUser(id)) {
            throw new ConstraintViolationException("无法删除该用户，存在关联的车辆信息");
        }
        return super.removeById(id);
    }
} 