package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import co.kr.trebit.exchange.socket.biz.client.ExchangeWebSocketClient;
import co.kr.trebit.exchange.socket.boot.handler.ExchangeSocketConnectHandler;
import co.kr.trebit.exchange.socket.boot.handler.ExchangeSocketDisconnectHandler;
import co.kr.trebit.exchange.socket.boot.handler.ExchangeSocketHandshakeHandler;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/info");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/socket-io").setAllowedOrigins("*").setHandshakeHandler(new ExchangeSocketHandshakeHandler()).withSockJS();
    }

    @Bean
    public ExchangeSocketConnectHandler webSocketConnectHandler() {
        return new ExchangeSocketConnectHandler();
    }

    @Bean
    public ExchangeSocketDisconnectHandler webSocketDisconnectHandler() {
        return new ExchangeSocketDisconnectHandler();
    }

    @Bean
    public ExchangeWebSocketClient exchangeWebSocketClient() {
        return new ExchangeWebSocketClient();
    }
}
