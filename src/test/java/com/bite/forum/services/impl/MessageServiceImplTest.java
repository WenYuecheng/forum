package com.bite.forum.services.impl;

import com.bite.forum.model.Message;
import com.bite.forum.services.IMessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class MessageServiceImplTest {
    @Autowired
    private IMessageService messageService;

    @Test
    void create() {
        Message message = new Message();
        message.setPostUserId(2l);
        message.setReceiveUserId(1l);
        message.setContent("test");
        messageService.create(message);
    }

    @Test
    void selectUnreadCount() {
        System.out.println(messageService.selectUnreadCount(1l));

    }

    @Test
    void updateStateById() {
        messageService.updateStateById(1l, (byte) 2);

    }

    @Test
    void selectById() {
        System.out.println(messageService.selectById(1l));
    }
}