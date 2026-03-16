package com.bite.forum.controller;

import com.bite.forum.common.AppResult;
import com.bite.forum.common.ResultCode;
import com.bite.forum.config.AppConfig;
import com.bite.forum.model.Message;
import com.bite.forum.model.User;
import com.bite.forum.services.IMessageService;
import com.bite.forum.services.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 站内信消息控制器
 * 处理私信发送、未读统计、列表查询及回复逻辑
 *
 * @author 温悦成
 * @since 2026-03-16
 */
@RestController
@RequestMapping("/message")
@Tag(name = "MessageController", description = "消息模块")
public class MessageController {

    @Autowired
    private IMessageService messageService;

    @Autowired
    private IUserService userService;

    /**
     * 发送站内信
     *
     * @param request       HTTP请求
     * @param receiveUserId 接收者ID
     * @param content       消息正文
     * @return 统一响应结果
     */
    @Operation(summary = "发送消息")
    @PostMapping("/send")
    public AppResult<Void> send(HttpServletRequest request,
                                @Parameter(description = "接收者") @NonNull Long receiveUserId,
                                @Parameter(description = "消息内容") @NonNull String content) {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        if (user.getState() == 1) {
            return AppResult.failed(ResultCode.FAILED_USER_BANNED);
        }
        if (user.getId().equals(receiveUserId)) {
            return AppResult.failed(ResultCode.FAILED_USER_EXISTS);
        }
        User receiveUser = userService.selectById(receiveUserId);
        if (receiveUser == null || receiveUser.getState() == 1) {
            return AppResult.failed(ResultCode.FAILED_UNAUTHORIZED);
        }

        Message message = new Message();
        message.setPostUserId(user.getId());
        message.setReceiveUserId(receiveUserId);
        message.setContent(content);
        messageService.create(message);
        return AppResult.success();
    }

    /**
     * 获取当前登录用户的未读消息总数
     *
     * @param request HTTP请求
     * @return 未读状态的消息数量
     */
    @Operation(summary = "获取未读消息数")
    @GetMapping("/getUnreadCount")
    public AppResult<Integer> getUnreadCount(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        return AppResult.success(messageService.selectUnreadCount(user.getId()));
    }

    /**
     * 获取当前登录用户的所有消息列表
     *
     * @param request HTTP请求
     * @return 消息列表
     */
    @Operation(summary = "获取消息列表")
    @GetMapping("/getAll")
    public AppResult<List<Message>> getAll(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        return AppResult.success(messageService.selectByReceiveUserId(user.getId()));
    }

    /**
     * 将单条消息标记为已读
     *
     * @param request HTTP请求
     * @param id      站内信ID
     * @return 统一响应结果
     */
    @Operation(summary = "标记已读")
    @PostMapping("/markRead")
    public AppResult<Void> markRead(HttpServletRequest request,
                                    @Parameter(description = "站内信") @NonNull Long id) {
        Message message = messageService.selectById(id);
        if (message == null) {
            return AppResult.failed(ResultCode.FAILED_NOT_EXISTS);
        }
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        if (!message.getReceiveUserId().equals(user.getId())) {
            return AppResult.failed(ResultCode.FAILED_UNAUTHORIZED);
        }
        messageService.updateStateById(id, (byte) 1);
        return AppResult.success();
    }

    /**
     * 回复已收到的站内信
     *
     * @param request   HTTP请求
     * @param repliedId 被回复的消息ID
     * @param content   响应内容
     * @return 统一响应结果
     */
    @Operation(summary = "回复消息")
    @PostMapping("/reply")
    public AppResult<Void> reply(HttpServletRequest request,
                                 @Parameter(description = "要回復的站內信id") @NonNull Long repliedId,
                                 @Parameter(description = "消息内容") @NonNull String content) {
        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute(AppConfig.USER_SESSION);
        if (user.getState() == 1) {
            return AppResult.failed(ResultCode.FAILED_USER_BANNED);
        }
        Message existMessage = messageService.selectById(repliedId);
        if (existMessage == null) {
            return AppResult.failed(ResultCode.FAILED_NOT_EXISTS);
        }
        if (user.getId().equals(existMessage.getPostUserId())) {
            return AppResult.failed(ResultCode.FAILED_USER_EXISTS);
        }

        Message message = new Message();
        message.setPostUserId(user.getId());
        message.setReceiveUserId(existMessage.getPostUserId());
        message.setContent(content);
        messageService.reply(repliedId, message);
        return AppResult.success();
    }
}
