package com.chatdemo.chatdemo.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GroupInfo {
        String name;
        List<Member> listMembers;
}
