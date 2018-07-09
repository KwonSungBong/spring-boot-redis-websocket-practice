package com.example.demo.config;

import com.example.demo.handler.DemoSocketConnectHandler;
import com.example.demo.handler.DemoSocketDisconnectHandler;
import com.example.demo.handler.DemoSocketHandshakeHandler;
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
        config.enableSimpleBroker("/info");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
//    	registry.addEndpoint("/socket-io").setAllowedOrigins("*").setHandshakeHandler(new DemoSocketHandshakeHandler()).withSockJS();
        registry.addEndpoint("/socket-io").withSockJS();
    }

    @Bean
    public DemoSocketConnectHandler webSocketConnectHandler() {
        return new DemoSocketConnectHandler();
    }

    @Bean
    public DemoSocketDisconnectHandler webSocketDisconnectHandler() {
        return new DemoSocketDisconnectHandler();
    }

}
