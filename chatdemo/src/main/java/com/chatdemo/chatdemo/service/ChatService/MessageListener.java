package com.chatdemo.chatdemo.service.ChatService;

import com.chatdemo.chatdemo.config.RabbitMQConfig;
import com.chatdemo.chatdemo.entity.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

//@Service
public class MessageListener {
//
//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;
//
//    @RabbitListener(queues = RabbitMQConfig.CHAT_QUEUE)
//    public void receiveMessage(Message message, @Header("amqp_receivedRoutingKey") String routingKey) {
//        String chatId = routingKey.split("\\.")[1];
//        messagingTemplate.convertAndSend("/topic/chat/" + chatId, message);
//    }
}