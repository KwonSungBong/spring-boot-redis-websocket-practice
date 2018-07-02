package com.example.demo.repository;

import com.example.demo.vo.ExchangePrincipal;
import com.example.demo.vo.SocketSession;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExchangeSocketSessionRepository {

	private Logger L = LoggerFactory.getLogger(getClass());

	private final String REDIS_KEY_PREFIX = "socket-session-";

	private String REDIS_KEY;

	@Autowired
    private RedisCommonRepository redisCommonRepository;

	@PostConstruct
    public void init(){
		try {
			REDIS_KEY = REDIS_KEY_PREFIX + InetAddress.getLocalHost().getHostName();
		}
		catch (UnknownHostException e) {
			L.warn(e.getMessage());
			REDIS_KEY = REDIS_KEY_PREFIX + (int)(Math.random()*10000);
		}
		clearSocketSessionList();
    }

	private List<SocketSession> getSocketSessionList() {
		String list = redisCommonRepository.getValue(REDIS_KEY);
		if(StringUtils.isEmpty(list)) {
			return new ArrayList<SocketSession>();
		}
		SocketSession[] socketSessionArray = new Gson().fromJson(list, SocketSession[].class);
		List<SocketSession> socketSessionList = new ArrayList<SocketSession>();
		for(SocketSession socketSession : socketSessionArray) {
			socketSessionList.add(socketSession);
		}
		return socketSessionList;
	}

	private void setSocketSessionList(List<SocketSession> socketSessionList) {
		SocketSession[] socketSessionArray = null;
		if(socketSessionList == null) {
			socketSessionArray = new SocketSession[] {};
		}
		else {
			socketSessionArray = socketSessionList.toArray(new SocketSession[] {});
		}
		String json = new Gson().toJson(socketSessionArray);
		redisCommonRepository.addValue(REDIS_KEY, json);
	}

	public void clearSocketSessionList() {
		redisCommonRepository.addValue(REDIS_KEY, "[]");
	}

	public void addSocketSession(String memNo, StompHeaderAccessor headerAccessor) {
		addSocketSession(memNo, headerAccessor.getSessionId(), headerAccessor.getUser());
	}

	public void addSocketSession(String memNo, String sessionId, Principal principal) {
		SocketSession socketSession = new SocketSession();
		socketSession.setMemNo(memNo);
		socketSession.setSessionId(sessionId);
		socketSession.setExchangePrincipal((ExchangePrincipal)principal);
		updateSocketSessionList(socketSession, true);
	}

	public void removeSocketSession(StompHeaderAccessor headerAccessor) {
		SocketSession socketSessionVo = new SocketSession();
		socketSessionVo.setSessionId(headerAccessor.getSessionId());
		updateSocketSessionList(socketSessionVo, false);
	}

	private synchronized void updateSocketSessionList(SocketSession targetSocketSession, boolean isAdd) {
		List<SocketSession> socketSessionList = getSocketSessionList();
		if(isAdd) {
			socketSessionList.add(targetSocketSession);
		}
		else {
			for(SocketSession socketSession : socketSessionList) {
				String sessionId = targetSocketSession.getSessionId();
				if(sessionId.equals(socketSession.getSessionId())) {
					socketSessionList.remove(socketSession);
					break;
				}
			}
		}
		setSocketSessionList(socketSessionList);
	}

	public List<String> selectUser(String memNo) {
		List<String> users = new ArrayList<String>();
		for(SocketSession socketSession : getSocketSessionList()) {
			if(memNo.equals(socketSession.getMemNo())) {
				users.add(socketSession.getExchangePrincipal().getName());
			}
		}
		return users;
	}

	public int getCount() {
		return getSocketSessionList().size();
	}
}
