package com.example.demo.handler;

import com.example.demo.repository.ExchangeSocketSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

public class ExchangeSocketDisconnectHandler implements ApplicationListener<SessionDisconnectEvent> {

	private Logger L = LoggerFactory.getLogger(getClass());

	@Autowired
	private ExchangeSocketSessionRepository webSocketSessionRepository;

	@Override
	public void onApplicationEvent(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		webSocketSessionRepository.removeSocketSession(headerAccessor);
	}
}
