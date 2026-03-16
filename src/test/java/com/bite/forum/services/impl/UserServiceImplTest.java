package com.bite.forum.services.impl;

import com.bite.forum.model.User;
import com.bite.forum.services.IUserService;
import com.bite.forum.utils.MD5Utils;
import com.bite.forum.utils.UUIDUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserServiceImplTest {
    @Autowired
    private IUserService iUserService;

    @Test
    void createNormalUser() {
        User user = new User();
        user.setUsername("zhangsan");
        user.setNickname("lisi");
        String password = "123456";
        String salt = UUIDUtils.UUID_32();
        String ciphertext = MD5Utils.md5Salt(password,salt);
        user.setPassword(ciphertext);
        user.setSalt(salt);
        iUserService.createNormalUser(user);
        System.out.println(user);
    }

    @Test
    void selectByUserName() {
        User user = iUserService.selectByUserName("zhangsan");
        System.out.println(user);

    }

    @Test
    void login() {
        User user = iUserService.login("zhangsa","12346");
        System.out.println(user);
    }

    @Test
    void selectById() {
        User user = iUserService.selectById(2l);
        System.out.println(user);

    }

    @Test
    void addOneArticleCountById() {
        iUserService.addOneArticleCountById(1l);
    }

    @Test
    void subOneArticleCountById() {
        iUserService.subOneArticleCountById(1l);

    }

    @Test
    void modifyInfo() {
        User user = new User();
        user.setId(3l);
        user.setUsername("何源峰");
        user.setNickname("何哥");
        user.setGender((byte)0);
        user.setEmail("he@163.com");
        user.setPhoneNum("123456789");
        user.setRemark("我是何哥");
        iUserService.modifyInfo(user);
    }

    @Test
    void modifyPassword() {
        iUserService.modifyPassword(2l,"123456","12345");

    }
}