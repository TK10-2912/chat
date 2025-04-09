package com.chatdemo.chatdemo.Reponse.Authen;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthReponse {
    String token;
    String refreshToken;
    String userId;
    String username;
}
