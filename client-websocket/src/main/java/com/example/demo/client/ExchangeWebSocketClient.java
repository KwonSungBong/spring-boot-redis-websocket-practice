package com.example.demo.client;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.converter.SimpleMessageConverter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

public class ExchangeWebSocketClient {

	private Logger L = LoggerFactory.getLogger(getClass());

	@Value("${web.socket.url}")
    private String webSocketUrl;

	@Autowired
	private ExchangeStompSessionHandler exchangeStompSessionHandler;

	private String URL;

	@PostConstruct
    public void init(){
		URL = webSocketUrl + "/socket-io";
		exchangeStompSessionHandler.setExchangeWebSocketClient(this);
		connect();
    }

	public void connect() {
		List<Transport> transports = new ArrayList<Transport>(1);
		transports.add(new WebSocketTransport(new StandardWebSocketClient()));
		transports.add(new RestTemplateXhrTransport());

		WebSocketClient client = new SockJsClient(transports);

		WebSocketStompClient stompClient = new WebSocketStompClient(client);
		stompClient.setMessageConverter(new SimpleMessageConverter());

		stompClient.connect(URL, exchangeStompSessionHandler);
	}
}
