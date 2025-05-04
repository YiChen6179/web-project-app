package com.yichen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yichen.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {
}
