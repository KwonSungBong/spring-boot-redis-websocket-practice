package com.example.demo.handler;

import com.example.demo.vo.ExchangePrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

public class ExchangeSocketHandshakeHandler extends DefaultHandshakeHandler {

	private Logger L = LoggerFactory.getLogger(getClass());

	@Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
		Principal principal = request.getPrincipal();
        if(principal == null) {
            principal = new ExchangePrincipal();
            String uniqueName = UUID.randomUUID().toString();
            ((ExchangePrincipal)principal).setName(uniqueName);
        }
        return principal;
    }
}
