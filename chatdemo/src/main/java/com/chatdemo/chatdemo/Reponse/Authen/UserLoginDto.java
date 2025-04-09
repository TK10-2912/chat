package com.chatdemo.chatdemo.Reponse.Authen;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.bson.types.ObjectId;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserLoginDto {
    ObjectId userId;
    String username;
    String accessToken;
}
