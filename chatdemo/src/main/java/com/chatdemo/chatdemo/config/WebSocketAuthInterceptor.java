package com.chatdemo.chatdemo.config;

import com.chatdemo.chatdemo.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

//@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {

//    @Autowired
//    private JwtUtil jwtUtil;
//
//    @Override
//    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
//
//        // Kiểm tra nếu message là CONNECT
//        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
//            String authorizationHeader = accessor.getFirstNativeHeader("Authorization");
//
//            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
//                throw new SecurityException("Missing or invalid Authorization header");
//            }
//
//            // Trích xuất token từ header
//            String token = authorizationHeader.substring(7); // Loại bỏ "Bearer "
//            try {
//                // Trích xuất username từ token
//                String username = jwtUtil.extractUsername(token);
//
//                // Xác thực token
//                if (username != null && jwtUtil.validateToken(token, username)) {
//                    // Tạo Authentication và lưu vào SecurityContext (nếu bạn dùng Spring Security)
//                    Authentication auth = new UsernamePasswordAuthenticationToken(username, null, null);
//                    SecurityContextHolder.getContext().setAuthentication(auth);
//
//                    // Thêm thông tin user vào header để sử dụng trong các message sau
//                    accessor.setUser(auth);
//                } else {
//                    throw new SecurityException("Invalid or expired token");
//                }
//            } catch (Exception e) {
//                throw new SecurityException("Token validation failed: " + e.getMessage());
//            }
//        }
//
//        return message;
//    }
//
//    @Override
//    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
//        // Có thể log hoặc xử lý sau khi gửi message (tuỳ chọn)
//    }
//
//    @Override
//    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
//        // Có thể xử lý sau khi hoàn tất gửi (tuỳ chọn)
//    }
//
//    @Override
//    public boolean preReceive(MessageChannel channel) {
//        return true; // Cho phép nhận message
//    }
//
//    @Override
//    public Message<?> postReceive(Message<?> message, MessageChannel channel) {
//        return message; // Trả về message không thay đổi
//    }
//
//    @Override
//    public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {
//        // Có thể xử lý sau khi nhận message (tuỳ chọn)
//    }
}