package com.yichen.utils;

import com.yichen.entity.User;
import org.springframework.stereotype.Component;

/**
 * 用户上下文，存储当前线程的用户信息
 */
@Component
public class UserContext {
    
    private static final ThreadLocal<User> currentUser = new ThreadLocal<>();
    
    public User getCurrentUser() {
        return currentUser.get();
    }
    
    public void setCurrentUser(User user) {
        currentUser.set(user);
    }
    
    public void clear() {
        currentUser.remove();
    }
    
    public Long getCurrentUserId() {
        return getCurrentUser() != null ? getCurrentUser().getId() : null;
    }
    
    public String getCurrentUsername() {
        return getCurrentUser() != null ? getCurrentUser().getUsername() : null;
    }
} 