package com.yichen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yichen.mapper.UserMapper;
import com.yichen.entity.User;
import com.yichen.service.UserService;
import com.yichen.utils.BeanConverter;
import com.yichen.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户服务实现类
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final BeanConverter beanConverter;

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
} 