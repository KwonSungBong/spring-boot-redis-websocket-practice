package com.example.demo.client;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.ConnectionLostException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import com.google.gson.Gson;

import co.kr.trebit.exchange.entity.db.vo.code.MarketCrncyVo;
import co.kr.trebit.exchange.entity.db.vo.code.MarketVo;
import co.kr.trebit.exchange.entity.socket.broker.BROKER_ENUM;
import co.kr.trebit.exchange.entity.socket.help.SocketClientHelper;
import co.kr.trebit.exchange.legacy.common.svc.MasterDataService;
import co.kr.trebit.exchange.socket.biz.handler.ChartDayStompFrameHandler;
import co.kr.trebit.exchange.socket.biz.handler.ChartMinStompFrameHandler;
import co.kr.trebit.exchange.socket.biz.handler.CmplStompFrameHandler;
import co.kr.trebit.exchange.socket.biz.handler.CoinStatStompFrameHandler;
import co.kr.trebit.exchange.socket.biz.handler.MemberStompFrameHandler;
import co.kr.trebit.exchange.socket.biz.handler.OrderStompFrameHandler;

@Service
public class ExchangeStompSessionHandler implements StompSessionHandler {

	private Logger L = LoggerFactory.getLogger(getClass());

	@Autowired
	private MasterDataService masterDataService;

	@Autowired
	private MemberStompFrameHandler memberStompFrameHandler;

	@Autowired
	private CoinStatStompFrameHandler coinStatStompFrameHandler;

	@Autowired
	private ChartMinStompFrameHandler chartMinStompFrameHandler;

	@Autowired
	private ChartDayStompFrameHandler chartDayStompFrameHandler;

	@Autowired
	private OrderStompFrameHandler orderStompFrameHandler;

	@Autowired
	private CmplStompFrameHandler cmplStompFrameHandler;

	private ExchangeWebSocketClient exchangeWebSocketClient;

	private StompSession session;

	public void setExchangeWebSocketClient(ExchangeWebSocketClient exchangeWebSocketClient) {
		this.exchangeWebSocketClient = exchangeWebSocketClient;
	}

	@Override
	public Type getPayloadType(StompHeaders headers) {
		L.info("getPayloadType : " + headers);
		return Object.class;
	}

	@Override
	public void handleFrame(StompHeaders headers, Object payload) {
		L.info("headers : " + headers);
		byte[] body = (byte[])payload;
		String json = new String(body);
		L.info(json);
		// 서버 레디스 정보 기준으로 초기화
		SocketClientHelper socketClientHelper = new Gson().fromJson(json, SocketClientHelper.class);
		masterDataService.setMarketList(socketClientHelper.getMarketVos());
		masterDataService.setCrncyList(socketClientHelper.getCrncyVos());
		masterDataService.setMarketCrncyList(socketClientHelper.getMarketCrncyVos());
		initSubscribe();
	}

	@Override
	public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
		this.session = session;
		session.subscribe("/info/master", this);
		session.send("/app/master/info", null);
	}

	@Override
	public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
		L.error("handleException {}", exception.getMessage());
	}

	@Override
	public void handleTransportError(StompSession session, Throwable exception) {
		L.error("handleTransportError: {}", exception.getMessage());
		if(exception instanceof ConnectionLostException) {
			// 서버소켓이 내려간 경우
			retryConnect();
		}
		else if(exception instanceof ResourceAccessException) {
			// 서버소켓에 연결을 못하는 경우
			retryConnect();
		}
	}

	private void initSubscribe() {
		session.subscribe(BROKER_ENUM.INFORM.getBroker(), memberStompFrameHandler);
		MarketVo [] marketVos = masterDataService.selectMarketList();
		Map<String,List<MarketCrncyVo>> marketCrncyMap = masterDataService.getMarketCrncyMap();
		for(MarketVo marketVo : marketVos) {
			String baseCrncyCd = marketVo.getBaseCrncyCd();
			session.subscribe(BROKER_ENUM.COINSTAT.getBroker(baseCrncyCd), coinStatStompFrameHandler);
			session.subscribe(BROKER_ENUM.CHART_MIN.getBroker(baseCrncyCd), chartMinStompFrameHandler);
			session.subscribe(BROKER_ENUM.CHART_DAY.getBroker(baseCrncyCd), chartDayStompFrameHandler);
			List<MarketCrncyVo> marketCrncyVos = marketCrncyMap.get(baseCrncyCd);
			for(MarketCrncyVo marketCrncyVo : marketCrncyVos) {
				String crncyCd = marketCrncyVo.getCrncyCd();
				session.subscribe(BROKER_ENUM.ORDER.getBroker(baseCrncyCd, crncyCd), orderStompFrameHandler);
				session.subscribe(BROKER_ENUM.CMPL.getBroker(baseCrncyCd, crncyCd), cmplStompFrameHandler);
			}
		}
	}

	private void retryConnect() {
		long millis = 5000;
		L.warn("Retry connect to server every {}ms...", millis);
		try {
			Thread.sleep(millis);
		}
		catch(InterruptedException e) {
			L.warn(e.getMessage());
		}
		exchangeWebSocketClient.connect();
	}
}
