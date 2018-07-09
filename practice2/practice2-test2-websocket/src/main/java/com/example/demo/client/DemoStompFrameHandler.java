package com.example.demo.client;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;

public class DemoStompFrameHandler implements StompFrameHandler {

	@Override
	public Type getPayloadType(StompHeaders headers) {
		return Object.class;
	}

	@Override
	public void handleFrame(StompHeaders headers, Object payload) {
		byte[] body = (byte[])payload;
		String json = new String(body);
		System.out.println("TEST : " + json);
	}
}
