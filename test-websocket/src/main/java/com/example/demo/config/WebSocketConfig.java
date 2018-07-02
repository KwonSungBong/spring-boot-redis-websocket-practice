package com.example.demo.config;

import com.example.demo.handler.ExchangeSocketConnectHandler;
import com.example.demo.handler.ExchangeSocketDisconnectHandler;
import com.example.demo.handler.ExchangeSocketHandshakeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/socket-io").setHandshakeHandler(new ExchangeSocketHandshakeHandler()).withSockJS();
    }

    @Bean
    public ExchangeSocketConnectHandler webSocketConnectHandler() {
        return new ExchangeSocketConnectHandler();
    }

    @Bean
    public ExchangeSocketDisconnectHandler webSocketDisconnectHandler() {
        return new ExchangeSocketDisconnectHandler();
    }

}
