package com.yichen.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yichen.mapper.UserMapper;
import com.yichen.entity.User;
import com.yichen.service.AuthService;
import com.yichen.utils.BeanConverter;
import com.yichen.utils.JwtUtil;
import com.yichen.utils.PasswordEncoder;
import com.yichen.vo.TokenVO;
import com.yichen.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final BeanConverter beanConverter;
    private final JwtUtil jwtUtil;

    @Override
    public TokenVO login(String username, String password) {
        // 根据用户名查询
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(queryWrapper);
        
        // 判断用户是否存在以及密码是否正确
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            // 生成token
            String token = jwtUtil.generateToken(user.getId(), user.getUsername());
            
            // 创建并返回TokenVO
            TokenVO tokenVO = new TokenVO();
            tokenVO.setToken(token);
            tokenVO.setUser(beanConverter.convert(user, UserVO.class));
            
            return tokenVO;
        }
        
        return null;
    }

    @Override
    public boolean logout() {
        // 无状态认证不需要服务端处理登出
        return true;
    }
} 