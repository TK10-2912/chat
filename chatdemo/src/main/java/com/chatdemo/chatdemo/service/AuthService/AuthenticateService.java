package com.chatdemo.chatdemo.service.AuthService;

import com.chatdemo.chatdemo.Reponse.Authen.AuthReponse;
import com.chatdemo.chatdemo.Reponse.Authen.UserRegisterDto;
import com.chatdemo.chatdemo.Request.Authen.RegisterDto;
import com.chatdemo.chatdemo.entity.User;
import com.chatdemo.chatdemo.iservice.IAuthenticate;
import com.chatdemo.chatdemo.jwt.JwtUtil;
import com.chatdemo.chatdemo.repository.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticateService implements IAuthenticate {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public AuthReponse authenticate(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        System.out.println(userOpt.get().getPassword());
        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
            User user = userOpt.get();
            String token = jwtUtil.generateToken(user.getEmail());
            String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());
            user.setRefreshToken(refreshToken);
            userRepository.save(user);
            return new AuthReponse(token, refreshToken, user.getId().toString(), user.getUsername());
        }
        return null;
    }

    @Override
    public UserRegisterDto register(RegisterDto register) {
        // Kiểm tra email đã tồn tại chưa
        Optional<User> userOpt = userRepository.findByEmail(register.getEmail());
        if (userOpt.isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + register.getEmail());
        }

        // Tạo user mới
        User user = new User();
        user.setUsername(register.getUsername());
        user.setPassword(passwordEncoder.encode(register.getPassword()));
        user.setEmail(register.getEmail());
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());

        // Lưu user vào MongoDB
        try {
            userRepository.save(user);
        } catch (org.springframework.dao.DuplicateKeyException e) {
            throw new IllegalArgumentException("Email already exists in database: " + register.getEmail());
        }

        return new UserRegisterDto(user.getUsername(), user.getEmail());
    }

    public String refreshToken(String refreshToken) {
        if (jwtUtil.validateToken(refreshToken, jwtUtil.extractEmail(refreshToken))) {
            String email = jwtUtil.extractEmail(refreshToken);
            return jwtUtil.generateToken(email);
        }
        return null;
    }

    public List<User> getUser() {
        return userRepository.findAll();
    }

    public User findByUsername(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return user;
        }
        return null;
    }

    public ObjectId getUserIdByUsername(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return user.getId();
        }
        return null;
    }

    public User getUserbyId(String id) {
        Optional<User> userOpt = userRepository.findById(new ObjectId(id));
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return user;
        }
        return null;
    }
    public Optional<User> findByEmail(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        return userOpt;
    }

//    public String verifyFirebaseToken(String idToken) throws Exception {
//        try {
//            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
//            String uid = decodedToken.getUid();
//            String email = decodedToken.getEmail();
//            return generateJwtToken(uid, email);
//        } catch (Exception e) {
//            throw new Exception("Invalid Firebase token: " + e.getMessage());
//        }
//    }

}
