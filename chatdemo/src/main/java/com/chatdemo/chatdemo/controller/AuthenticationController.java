package com.chatdemo.chatdemo.controller;

import com.chatdemo.chatdemo.Reponse.Authen.AuthReponse;
import com.chatdemo.chatdemo.Reponse.Authen.UserRegisterDto;
import com.chatdemo.chatdemo.Request.Authen.LoginDto;
import com.chatdemo.chatdemo.Request.Authen.RegisterDto;
import com.chatdemo.chatdemo.jwt.JwtUtil;
import com.chatdemo.chatdemo.service.AuthService.AuthenticateService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticateService authenticateService;
    @Autowired
    private JwtUtil iwtUltil;
    @Autowired
    private FirebaseAuth firebaseAuth;
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody  RegisterDto user) throws FirebaseAuthException {
        firebaseAuth.createUser(
                new com.google.firebase.auth.UserRecord.CreateRequest()
                        .setEmail(user.getEmail())
                        .setPassword(user.getPassword())
                        .setDisplayName(user.getUsername())
        );
        authenticateService.register(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginRequest) {
        AuthReponse authResponse = authenticateService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());
        if (authResponse != null) {
            return ResponseEntity.ok(authResponse);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refreshToken");
        String newToken = authenticateService.refreshToken(refreshToken);
        if (newToken != null) {
            return ResponseEntity.ok(newToken);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
    }
    @PostMapping("/moblogin")
    public ResponseEntity<?> mobileLogin(@RequestHeader("Authorization") String firebaseToken) {
        System.out.println(firebaseToken);
        try {
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(firebaseToken);
            String email = decodedToken.getEmail();

            // Đảm bảo user tồn tại trong MongoDB
            authenticateService.findByEmail(email).orElseThrow(
                    () -> new IllegalArgumentException("User not found. Please register first.")
            );

            String accessToken = iwtUltil.generateToken(email);
            String refreshToken = iwtUltil.generateRefreshToken(email);

            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);

            return ResponseEntity.ok(tokens);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid Firebase token: " + e.getMessage());
        }
    }
//    @PostMapping("/verify")
//    public ResponseEntity<?> login(@RequestHeader("Authorization") String firebaseToken) {
//        try {
//            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(firebaseToken);
//            String uid = decodedToken.getUid();
//            String jwt = jwt.generateToken(uid);
//            return ResponseEntity.ok(new AuthResponse(jwt));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
//        }
//    }
}
