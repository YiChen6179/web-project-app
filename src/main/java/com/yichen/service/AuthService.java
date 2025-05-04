package com.yichen.service;

import com.yichen.vo.UserVO;

public interface AuthService {
    UserVO login(String username, String password);
    boolean logout();
} 