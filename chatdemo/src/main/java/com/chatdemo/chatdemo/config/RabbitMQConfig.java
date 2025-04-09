package com.chatdemo.chatdemo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class RabbitMQConfig {
//    public static final String CHAT_QUEUE = "chat-queue";
//    public static final String CHAT_EXCHANGE = "chat-exchange";
//    public static final String ROUTING_KEY = "chat.*";
//
//    @Bean
//    public Queue queue() {
//        return new Queue(CHAT_QUEUE, true);
//    }
//
//    @Bean
//    public TopicExchange exchange() {
//        return new TopicExchange(CHAT_EXCHANGE);
//    }
//
//    @Bean
//    public Binding binding(Queue queue, TopicExchange exchange) {
//        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
//    }
}
