package com.chatdemo.chatdemo.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Message {
    @Id
    String id;
    String senderId; // ObjectId của user gửi
    String content;
    Date timestamp;
    Date createdAt;
    Date updatedAt;
    Map<String, List<String>> reactions;
}
