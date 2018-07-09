package com.example.demo.handler;

import com.example.demo.repository.ExchangeSocketSessionRepository;
import com.example.demo.vo.ParameterName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.util.List;

public class ExchangeSocketConnectHandler implements ApplicationListener<SessionConnectEvent> {

	private Logger L = LoggerFactory.getLogger(getClass());

	@Autowired
	private ExchangeSocketSessionRepository webSocketSessionRepository;

	@Override
	public void onApplicationEvent(SessionConnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		List<String> memNoHeader = headerAccessor.getNativeHeader(ParameterName.memNo.name());
		if(memNoHeader != null && !memNoHeader.isEmpty()) {
			String memNo = memNoHeader.get(0);
			// memNo(0) is anonymous!
			if(!StringUtils.isEmpty(memNo) && !"0".equals(memNo)) {
				//L.info("Connect! # memNo = {}", memNo);
				webSocketSessionRepository.addSocketSession(memNo, headerAccessor);
			}
		}
	}
}
