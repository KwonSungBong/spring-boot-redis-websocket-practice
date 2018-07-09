package com.example.demo.client.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;

@Service
public class DemoStompFrameHandler implements StompFrameHandler {

	private Logger L = LoggerFactory.getLogger(getClass());

	@Override
	public Type getPayloadType(StompHeaders headers) {
		return Object.class;
	}

	@Override
	public void handleFrame(StompHeaders headers, Object payload) {
		byte[] body = (byte[])payload;
		String json = new String(body);
	}
}
