//package com.xxxx.server.controller;
//
//import com.mornd.server.pojo.Admin;
//import com.mornd.server.pojo.ChatMsg;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Controller;
//
//import javax.annotation.Resource;
//import java.time.LocalDateTime;
//
///**
// * @author bin
// * @date 2022/2/28 - 14:58
// * websocket控制层
// */
//@Controller
//public class WebSocketController {
//    @Resource
//    private SimpMessagingTemplate simpMessagingTemplate;
//
//    @MessageMapping("/ws/chat")
//    public void handleMessage(Authentication authentication, ChatMsg chatMsg){
//        Admin admin = (Admin) authentication.getPrincipal();
//        chatMsg.setFrom(admin.getUsername());
//        chatMsg.setFormNickName(admin.getName());
//        chatMsg.setDate(LocalDateTime.now());
//        //发送
//        simpMessagingTemplate.convertAndSendToUser(chatMsg.getTo(),"/queue/chat",chatMsg);
//
//    }
//}
