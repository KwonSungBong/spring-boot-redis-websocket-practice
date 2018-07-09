package com.example.demo.handler;

import com.example.demo.repository.DemoSocketSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.util.List;

public class DemoSocketConnectHandler implements ApplicationListener<SessionConnectEvent> {

	private Logger L = LoggerFactory.getLogger(getClass());

	@Autowired
	private DemoSocketSessionRepository webSocketSessionRepository;

	@Override
	public void onApplicationEvent(SessionConnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

		List<String> userIdHeader = headerAccessor.getNativeHeader("userId");
		if(userIdHeader != null && !userIdHeader.isEmpty()) {
			String userId = userIdHeader.get(0);
			if(!StringUtils.isEmpty(userId) && !"0".equals(userId)) {
				webSocketSessionRepository.addSocketSession(userId, headerAccessor);
			}
		}
	}
}
