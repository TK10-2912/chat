package com.chatdemo.chatdemo.repository;

import com.chatdemo.chatdemo.entity.Chat;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ChatRepository extends MongoRepository<Chat, String> {

    // Trả về danh sách chatId
    @Query("{$or: [ " +
            "{ 'type': 'PRIVATE', 'participants.userId': ?0 }, " +
            "{ 'type': 'GROUP', 'listMembers.userId': ?0 } " +
            "]}")
    List<Chat> findChatsByUserId(String userId);

    // Trả về danh sách chatId dưới dạng String
    default List<ObjectId> findChatIdsByUserId(String userId) {
        return findChatsByUserId(userId).stream()
                .map(Chat::getId) // Trả về _id dưới dạng String
                .toList();
    }
}
