package com.chatdemo.chatdemo.controller;

import com.chatdemo.chatdemo.entity.Message;
import com.chatdemo.chatdemo.service.ChatService.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {

//    @Autowired
//    private ChatService chatService;
//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;
//
//    @MessageMapping("/app/chat/send/{chatId}")
//    @SendTo("/topic/public")
//    public void sendMessage(@DestinationVariable String chatId, @Payload Message message) {
//        System.out.println("Received message: " + message);
//        chatService.sendMessage(chatId, message);
//        messagingTemplate.convertAndSend("/topic/chat/" + chatId, message);
//    }
}
