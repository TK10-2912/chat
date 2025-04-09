package com.chatdemo.chatdemo.service.ChatService;

import com.chatdemo.chatdemo.Reponse.Chat.Group;
import com.chatdemo.chatdemo.entity.Chat;
import com.chatdemo.chatdemo.entity.Message;
import com.chatdemo.chatdemo.repository.ChatRepository;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ChatRepository chatRepository;
//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;

//    public void sendMessage(String chatId, Message message) {
//        // Lưu vào MongoDB
//        Chat chat = mongoTemplate.findById(chatId, Chat.class);
//        if (chat != null) {
//            message.setTimestamp(new Date());
//            chat.getListMessages().add(message);
//            mongoTemplate.save(chat);
//
//            // Gửi qua RabbitMQ
//            rabbitTemplate.convertAndSend(RabbitMQConfig.CHAT_EXCHANGE, "chat." + chatId, message);
//
//            // Cache vào Redis (ví dụ: lưu tin nhắn mới nhất của chat)
//            redisTemplate.opsForHash().put("chat:" + chatId, "latestMessage", message);
//        }
//    }

    public List<Message> getMessages(String chatId) {
        Chat chat = mongoTemplate.findById(chatId, Chat.class);
        return chat != null ? chat.getListMessages() : Collections.emptyList();
    }
    public List<Group> getGroupsForUser(String userId) {
        // Tạo query để tìm các chat có type là "GROUP" và userId nằm trong groupInfo.listMembers
        Query query = new Query(Criteria.where("type").is("GROUP")
                .and("groupInfo.listMembers.userId").is(new ObjectId(userId)));

        // Lấy danh sách Chat
        List<Chat> chats = mongoTemplate.find(query, Chat.class);

        // Ánh xạ từ Chat sang Group
        return chats.stream()
                .map(chat -> new Group(chat.getId().toString(), chat.getGroupInfo()))
                .collect(Collectors.toList());
    }
    public List<ObjectId> getChatsByUserId(String userId) {
        return chatRepository.findChatIdsByUserId(userId);
    }
}
