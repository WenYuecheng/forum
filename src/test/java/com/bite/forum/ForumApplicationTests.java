package com.bite.forum;

import com.bite.forum.dao.UserMapper;
import com.bite.forum.model.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@SpringBootTest
class ForumApplicationTests {
    @Resource
    private DataSource dataSource;
    @Resource
    private UserMapper userMapper;
    @Test
    void testConnection() throws SQLException {
        System.out.println("dataSource======="+dataSource.getClass());
        Connection conn = dataSource.getConnection();
        System.out.println("conn======="+conn);
    }

    @Test
    void testMybatis(){
        User user = userMapper.selectByPrimaryKey(1l);
        System.out.println("user========================="+user);
        System.out.println(user.getUsername());
    }


    @Test
    void contextLoads() {
    }

}
