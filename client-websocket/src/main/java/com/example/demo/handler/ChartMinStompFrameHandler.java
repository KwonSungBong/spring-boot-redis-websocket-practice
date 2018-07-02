//package com.example.demo.handler;
//
//import java.lang.reflect.Type;
//import java.util.Map;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.simp.stomp.StompFrameHandler;
//import org.springframework.messaging.simp.stomp.StompHeaders;
//import org.springframework.stereotype.Service;
//
//import com.google.gson.Gson;
//
//import co.kr.trebit.exchange.socket.common.svc.ScheduleService;
//
//@Service
//public class ChartMinStompFrameHandler implements StompFrameHandler {
//
//	private Logger L = LoggerFactory.getLogger(getClass());
//
//	@Autowired
//	private ScheduleService scheduleServiceImpl;
//
//	@Override
//	public Type getPayloadType(StompHeaders headers) {
//		return Object.class;
//	}
//
//	@Override
//	public void handleFrame(StompHeaders headers, Object payload) {
//		byte[] body = (byte[])payload;
//		String json = new String(body);
//		L.debug(json);
//		scheduleServiceImpl.broadCast(headers.getDestination(), new Gson().fromJson(json, Map.class));
//	}
//}
