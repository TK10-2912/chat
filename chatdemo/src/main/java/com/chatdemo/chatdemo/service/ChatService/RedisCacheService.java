package com.chatdemo.chatdemo.service.ChatService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

//@Service
public class RedisCacheService {

//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;
//
//    public void cacheChatStatus(String chatId, String status) {
//        redisTemplate.opsForValue().set("chat:status:" + chatId, status, 1, TimeUnit.HOURS);
//    }
//
//    public String getChatStatus(String chatId) {
//        return (String) redisTemplate.opsForValue().get("chat:status:" + chatId);
//    }
}
