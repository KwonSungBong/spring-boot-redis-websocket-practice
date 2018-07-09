package com.example.demo.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

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
