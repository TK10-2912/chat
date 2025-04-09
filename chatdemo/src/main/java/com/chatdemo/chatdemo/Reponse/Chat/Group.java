package com.chatdemo.chatdemo.Reponse.Chat;

import com.chatdemo.chatdemo.entity.GroupInfo;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Group {
    String chatId;
    GroupInfo groupInfo;
}
