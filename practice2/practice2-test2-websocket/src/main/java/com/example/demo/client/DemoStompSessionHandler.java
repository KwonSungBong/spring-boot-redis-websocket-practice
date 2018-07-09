package com.example.demo.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.*;

import java.lang.reflect.Type;

@Slf4j
public class DemoStompSessionHandler extends StompSessionHandlerAdapter {

    private DemoStompFrameHandler demoStompFrameHandler = new DemoStompFrameHandler();

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
//        log.info("New session established : " + session.getSessionId());
//        session.subscribe("/topic/messages", this);
//        log.info("Subscribed to /topic/messages");
//        session.send("/app/chat", getSampleMessage());
//        log.info("Message sent to websocket server");
        session.subscribe("/info/master", this);
        session.send("/app/master/info", "{\"name\":\"Client\"}");

        //localhost:9005/test
        session.subscribe("/info/demo", demoStompFrameHandler);
        session.send("/app/hello", "Spring");

//        session.subscribe("/info/demo", new StompFrameHandler() {
//            @Override
//            public Type getPayloadType(StompHeaders headers) {
//                return DemoMessage.class;
//            }
//
//            @Override
//            public void handleFrame(StompHeaders headers, Object payload) {
//                DemoMessage demoMessage = (DemoMessage) payload;
//                try {
//                    System.out.println("Hello, Spring! : " + demoMessage.getName());
//                } catch (Throwable t) {
//                } finally {
//                }
//            }
//        });
//        try {
//            session.send("/app/hello", "Spring");
//        } catch (Throwable t) {
//        }
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        log.error("Got an exception", exception);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return DemoMessage.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        DemoMessage msg = (DemoMessage) payload;
        log.info("Received : " + msg);
    }

    /**
     * A sample message instance.
     * @return instance of <code>Message</code>
     */
    private DemoMessage getSampleMessage() {
        return new DemoMessage("DEMO");
    }

}
