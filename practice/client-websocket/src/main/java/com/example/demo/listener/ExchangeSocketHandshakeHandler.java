/**
 * Project : 06.socket
 * File Name : ExchangeSocketHandshakeHandler.java
 * Author : JEANPRO
 * Date : 2018. 2. 7.
 *
 * Modification Information
 * Date         Reviser      Description
 * ------------ ----------- ---------------------------
 * 2018. 2. 7..   JEANPRO       최초작성
 *
 */
package com.example.demo.listener;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import co.kr.trebit.exchange.socket.common.dto.ExchangePrincipal;

/**
*
* @author JEANPRO
* @since 2018. 2. 7.
* @version 1.0
*/
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
