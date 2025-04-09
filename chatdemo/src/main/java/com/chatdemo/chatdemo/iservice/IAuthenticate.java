package com.chatdemo.chatdemo.iservice;

import com.chatdemo.chatdemo.Reponse.Authen.AuthReponse;
import com.chatdemo.chatdemo.Reponse.Authen.UserLoginDto;
import com.chatdemo.chatdemo.Reponse.Authen.UserRegisterDto;
import com.chatdemo.chatdemo.Request.Authen.LoginDto;
import com.chatdemo.chatdemo.Request.Authen.RegisterDto;
import com.chatdemo.chatdemo.entity.User;

public interface IAuthenticate {
    AuthReponse authenticate(String username , String password);

    UserRegisterDto register(RegisterDto register);
}
