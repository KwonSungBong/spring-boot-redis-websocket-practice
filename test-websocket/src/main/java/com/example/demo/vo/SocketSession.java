package com.example.demo.vo;

public class SocketSession {
	private static final long serialVersionUID = -1616667627336749900L;

	private String memNo;
	private String sessionId;
	private ExchangePrincipal exchangePrincipal;

	public String getMemNo() {
		return memNo;
	}
	public void setMemNo(String memNo) {
		this.memNo = memNo;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public ExchangePrincipal getExchangePrincipal() {
		return exchangePrincipal;
	}
	public void setExchangePrincipal(ExchangePrincipal exchangePrincipal) {
		this.exchangePrincipal = exchangePrincipal;
	}
}
