package com.yichen.user;

import com.yichen.mapper.UserMapper;
import com.yichen.entity.User;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

public class TestUser {
    @Test
    public void testAdd() {
        SqlSession sqlSession = MybatisUtil.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = User.builder()
                .username("yichen")
                .tel("18839616261")
                .password("123456")
                .email("yichen@163.com")
                .build();
        mapper.addUser(user);
        System.out.println(user);
        sqlSession.commit();
    }
}
