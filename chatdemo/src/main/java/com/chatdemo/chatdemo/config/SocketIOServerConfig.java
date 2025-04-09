package com.chatdemo.chatdemo.config;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class SocketIOServerConfig {

    @Value("${socketio.host}")
    private String host;

    @Value("${socketio.port}")
    private int port;

    @Bean
    public SocketIOServer socketIOServer() {
        Configuration config = new Configuration();
        config.setHostname(host);
        config.setPort(port);
        config.setOrigin("http://localhost:9596"); // Cho phép origin từ frontend
        SocketIOServer server = new SocketIOServer(config);

        // Xử lý kết nối
        server.addConnectListener(client -> {
            System.out.println("Client connected: " + client.getSessionId());
        });

        // Xử lý ngắt kết nối
        server.addDisconnectListener(client -> {
            System.out.println("Client disconnected: " + client.getSessionId());
        });

        server.start();
        System.out.println("Socket.IO server started on " + host + ":" + port);
        return server;
    }
}
