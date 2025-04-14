package com.yichen.dao;

import com.yichen.po.User;

import java.util.List;

public interface UserMapper {
    List<User> getAllUsers();
    User getUserById(Long id);
    int addUser(User user);
    int updateUser(User user);
    int deleteUser(Long id);
}
