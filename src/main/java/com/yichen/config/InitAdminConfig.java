package com.yichen.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yichen.mapper.UserMapper;
import com.yichen.entity.User;
import com.yichen.utils.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 初始化管理员账号
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class InitAdminConfig implements CommandLineRunner {
    
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) {
        // 检查是否已存在管理员账号
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, "admin");
        User admin = userMapper.selectOne(queryWrapper);
        
        if (admin == null) {
            // 不存在则创建管理员账号
            User newAdmin = new User();
            newAdmin.setUsername("admin");
            newAdmin.setPassword(passwordEncoder.encode("admin123")); // 初始密码
            newAdmin.setEmail("admin@example.com");
            newAdmin.setTel("13800138000");
            
            userMapper.insert(newAdmin);
            log.info("已创建管理员账号，用户名：admin，密码：admin123");
        } else {
            log.info("管理员账号已存在，无需创建");
        }
    }
} 