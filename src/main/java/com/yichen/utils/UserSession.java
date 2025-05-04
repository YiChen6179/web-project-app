package com.yichen.utils;

import com.yichen.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

/**
 * 用户会话，存储当前登录用户信息
 * 使用@SessionScope注解，使得每个会话拥有独立的实例
 */
@Component
@SessionScope
public class UserSession {
    
    private User currentUser;
    private boolean authenticated = false;
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
        this.authenticated = currentUser != null;
    }
    
    public boolean isAuthenticated() {
        return authenticated;
    }
    
    public void clear() {
        this.currentUser = null;
        this.authenticated = false;
    }
    
    public Long getCurrentUserId() {
        return currentUser != null ? currentUser.getId() : null;
    }
} 