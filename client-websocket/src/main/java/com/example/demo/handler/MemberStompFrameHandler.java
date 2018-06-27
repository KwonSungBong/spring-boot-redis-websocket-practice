package com.example.demo.handler;

import java.lang.reflect.Type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import co.kr.trebit.exchange.entity.socket.out.LastCmplVo;
import co.kr.trebit.exchange.socket.biz.inform.InformService;

@Service
public class MemberStompFrameHandler implements StompFrameHandler {

	private Logger L = LoggerFactory.getLogger(getClass());

	@Autowired
    private InformService informService;

	@Override
	public Type getPayloadType(StompHeaders headers) {
		return Object.class;
	}

	@Override
	public void handleFrame(StompHeaders headers, Object payload) {
		byte[] body = (byte[])payload;
		String json = new String(body);
		L.debug(json);
		LastCmplVo [] lastCmplVos = new Gson().fromJson(json, LastCmplVo[].class);
		for(LastCmplVo lastCmplVo : lastCmplVos) {
			informService.inform(lastCmplVo);
		}
	}
}
