package com.example.demo.client;

import com.example.demo.handler.TestStompFrameHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import java.lang.reflect.Type;

//레디스에 넣은 사용자 세션을 사용하는 부분
@Component
public class ExchangeStompSessionHandler implements StompSessionHandler {

    private ExchangeWebSocketClient exchangeWebSocketClient;

    private StompSession session;

    @Autowired
    private TestStompFrameHandler testStompFrameHandler;

    public void setExchangeWebSocketClient(ExchangeWebSocketClient exchangeWebSocketClient) {
        this.exchangeWebSocketClient = exchangeWebSocketClient;
    }

    @Override
    public void afterConnected(StompSession stompSession, StompHeaders stompHeaders) {
        System.out.println("afterConnected");
        this.session = session;
        session.subscribe("/user/info/master", this);
        session.send("/app/master/info", null);
    }

    @Override
    public void handleException(StompSession stompSession, StompCommand stompCommand, StompHeaders stompHeaders, byte[] bytes, Throwable exception) {
//        .error("handleException {}", exception.getMessage());
    }

    @Override
    public void handleTransportError(StompSession stompSession, Throwable exception) {
//        L.error("handleTransportError: {}", throwable.getMessage());
        if(exception instanceof ConnectionLostException) {
            // 서버소켓이 내려간 경우
            retryConnect();
        }
        else if(exception instanceof ResourceAccessException) {
            // 서버소켓에 연결을 못하는 경우
            retryConnect();
        }

    }

    @Override
    public Type getPayloadType(StompHeaders stompHeaders) {
//        L.info("getPayloadType : " + headers);
        return Object.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
//        L.info("headers : " + headers);
        byte[] body = (byte[])payload;
        String json = new String(body);
//        L.info(json);
        session.subscribe("/info/test", testStompFrameHandler);
        System.out.println("handleFrame");
    }

    private void retryConnect() {
        long millis = 5000;
//        L.warn("Retry connect to server every {}ms...", millis);
        try {
            Thread.sleep(millis);
        }
        catch(InterruptedException e) {
//            L.warn(e.getMessage());
        }
        exchangeWebSocketClient.connect();
    }

}
