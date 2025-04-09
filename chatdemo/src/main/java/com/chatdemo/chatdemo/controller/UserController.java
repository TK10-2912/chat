package com.chatdemo.chatdemo.controller;

import com.chatdemo.chatdemo.Reponse.Authen.UserDto;
import com.chatdemo.chatdemo.entity.User;
import com.chatdemo.chatdemo.service.AuthService.AuthenticateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    @Autowired private AuthenticateService userService;
    @GetMapping("/getAllusers")
    public ResponseEntity<List<UserDto>> getUsers() {
        System.out.println("adasdasdadsadasdsadasd");
        List<User> users = userService.getUser();
        List<UserDto> userDtos = new ArrayList<>(); // Khai báo đúng cách

        for (User user : users) {
            userDtos.add(new UserDto(user.getId().toString(), user.getUsername()));
        }
        return ResponseEntity.ok(userDtos);
    }
}
