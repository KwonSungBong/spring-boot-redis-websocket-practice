package com.example.demo.client;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import java.lang.reflect.Type;

public class DemoStompFrameHandler implements StompFrameHandler {

	@Override
	public Type getPayloadType(StompHeaders headers) {
		return DemoMessage.class;
	}

	@Override
	public void handleFrame(StompHeaders headers, Object payload) {
		DemoMessage demoMessage = (DemoMessage) payload;
		try {
			System.out.println("Hello, Spring! : " + demoMessage.getName());
		} catch (Throwable t) {
		} finally {
		}
	}
}
