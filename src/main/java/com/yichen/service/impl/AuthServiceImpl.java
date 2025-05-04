package com.yichen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yichen.dao.UserMapper;
import com.yichen.entity.User;
import com.yichen.service.AuthService;
import com.yichen.utils.BeanConverter;
import com.yichen.utils.PasswordEncoder;
import com.yichen.utils.UserSession;
import com.yichen.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserSession userSession;
    private final BeanConverter beanConverter;

    @Override
    public UserVO login(String username, String password) {
        // 根据用户名查询
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(queryWrapper);
        
        // 判断用户是否存在以及密码是否正确
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            // 设置用户会话
            User sessionUser = new User();
            sessionUser.setId(user.getId());
            sessionUser.setUsername(user.getUsername());
            sessionUser.setEmail(user.getEmail());
            sessionUser.setTel(user.getTel());
            userSession.setCurrentUser(sessionUser);
            
            // 返回用户VO对象
            return beanConverter.convert(user, UserVO.class);
        }
        
        return null;
    }

    @Override
    public boolean logout() {
        // 清除用户会话
        userSession.clear();
        return true;
    }
} 