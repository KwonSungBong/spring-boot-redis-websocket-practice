/*
* Project : 06.socket
* File Name : ExchangeSocketConnectHandler.java
* Author : JEANPRO
* Date : 2018. 2. 6.
*
* Modification Information
* Date         Reviser      Description
* ------------ ----------- ---------------------------
* 2018. 2. 6..   JEANPRO       최초작성
*
*/
package com.example.demo.listener;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import co.kr.trebit.core.common.util.StringUtils;
import co.kr.trebit.exchange.socket.common.code.ParameterName;
import co.kr.trebit.exchange.socket.common.dao.ExchangeSocketSessionRepository;

/**
 *
 * @author JEANPRO
 * @since 2018. 2. 6.
 * @version 1.0
 */
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
