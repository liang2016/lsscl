<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>
<%@page import="com.serotonin.mango.web.servlet.ImageValueServlet"%>
<c:if test="${!empty point.pointLocator.webcamLiveFeedCode}"><a href="webcam_live_feed.htm?pointId=${point.id}" target="webcamLiveFeed"></c:if>
<img src="<%= ImageValueServlet.servletPath %>${pointValue.time}_${point.id}.${pointValue.value.typeExtension}?w=80&h=80" alt="<fmt:message key="common.genThumb"/>" border="0"/>
<c:if test="${!empty point.pointLocator.webcamLiveFeedCode}"></a></c:if>