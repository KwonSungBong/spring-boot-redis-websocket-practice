/*
* Project : 06.socket
* File Name : ExchangeSocketDisconnectHandler.java
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import co.kr.trebit.exchange.socket.common.dao.ExchangeSocketSessionRepository;

/**
 *
 * @author JEANPRO
 * @since 2018. 2. 6.
 * @version 1.0
 */
public class ExchangeSocketDisconnectHandler implements ApplicationListener<SessionDisconnectEvent> {

	private Logger L = LoggerFactory.getLogger(getClass());

	@Autowired
	private ExchangeSocketSessionRepository webSocketSessionRepository;

	@Override
	public void onApplicationEvent(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
		//L.info("Disconnect!");
		//L.info(headerAccessor.toString());
		webSocketSessionRepository.removeSocketSession(headerAccessor);
	}
}
