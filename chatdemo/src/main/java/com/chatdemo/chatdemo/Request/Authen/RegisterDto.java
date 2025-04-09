package com.chatdemo.chatdemo.Request.Authen;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterDto {
    String username;
    String password;
    String email;
}
