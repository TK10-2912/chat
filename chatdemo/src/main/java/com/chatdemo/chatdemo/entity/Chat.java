package com.chatdemo.chatdemo.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "chats")

public class Chat {
    ObjectId id;
    String type; // "PRIVATE" hoặc "GROUP"
    String idAdmin;
    List<PermissionGroup> listPermissionGroup; // list quyền của group
    List<Participant> participants;
    GroupInfo groupInfo; // Cho GROUP
    List<Message> listMessages;
    Date createdAt;
    Date updatedAt;
}

