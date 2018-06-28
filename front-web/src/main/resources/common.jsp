<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<spring:eval var="WEB_SOCKET_URL" expression="@environment.getProperty('web.socket.url')"/>
