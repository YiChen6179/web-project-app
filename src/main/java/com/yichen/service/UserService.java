package com.yichen.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yichen.entity.User;
import com.yichen.vo.UserVO;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户服务接口
 */
public interface UserService extends IService<User> {
    /**
     * 分页查询用户列表，支持按用户名模糊查询
     * @param current 当前页
     * @param size 每页数量
     * @param username 用户名（可选）
     * @return 用户VO分页数据
     */
    Page<UserVO> listUsers(Integer current, Integer size, String username);
    
    /**
     * 根据ID获取用户
     * @param id 用户ID
     * @return 用户VO
     */
    UserVO getUserById(Long id);

    @Transactional(rollbackFor = Exception.class)
    boolean removeById(Long id);
}