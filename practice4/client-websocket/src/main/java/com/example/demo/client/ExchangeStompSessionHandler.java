package com.example.demo.client;

import com.example.demo.vo.Result;
import com.google.gson.Gson;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import java.lang.reflect.Type;

@Component
public class ExchangeStompSessionHandler implements StompSessionHandler {

    private ExchangeWebSocketClient exchangeWebSocketClient;

    private StompSession session;

    public void setExchangeWebSocketClient(ExchangeWebSocketClient exchangeWebSocketClient) {
        this.exchangeWebSocketClient = exchangeWebSocketClient;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        System.out.println("afterConnected");
        this.session = session;
        session.subscribe("/info/master", this);
        session.send("/app/master/info", null);
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        byte[] body = (byte[])payload;
        String json = new String(body);
        Result masterResult = new Gson().fromJson(json, Result.class);
        System.out.println("handleFrame" + masterResult);
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
//        .error("handleException {}", exception.getMessage());
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
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
    public Type getPayloadType(StompHeaders connectedHeaders) {
//        L.info("getPayloadType : " + headers);
        return Object.class;
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
