<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<%--
DATE FORMATTING PATTERN
--%>
<c:set var="MDY" value="MM/dd/yyyy"/>
<c:set var="MY" value="MM/yyyy"/>
<c:set var="MD" value="MM/dd"/>
<c:set var="D" value="dd"/>
<c:set var="hms" value="HH:mm:ss"/>
<c:set var="hm" value="HH:mm"/>
<c:set var="h" value="HH"/>
<c:set var="m" value="mm"/>
<c:set var="s" value="ss"/>
<c:set var="FULL_DATE" value="MM/dd/yyyy HH:mm:ss"/>
<c:set var="now" value="<%= new java.util.Date() %>" />

<spring:eval var="WEB_SOCKET_URL" expression="@environment.getProperty('web.socket.url')"/>
