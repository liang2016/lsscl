<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<%@ include file="/WEB-INF/snippet/common.jsp" %>
<%@page import="com.serotonin.mango.web.servlet.ImageValueServlet"%>
<c:if test="${!empty point.pointLocator.webcamLiveFeedCode}"><a href="webcam_live_feed.htm?pointId=${point.id}" target="webcamLiveFeed"></c:if>
<c:choose>
  <c:when test="${empty error}"><img src="<%= ImageValueServlet.servletPath %>${pointValue.time}_${point.id}.${imageType}<c:if test="${!empty scalePercent}">?p=${scalePercent}</c:if>" alt="" border="0"/></c:when>
  <c:otherwise><span class="simpleRenderer"/><fmt:message key="${error}"/></span></c:otherwise>
</c:choose>
<c:if test="${!empty point.pointLocator.webcamLiveFeedCode}"><a href=""></a></c:if>