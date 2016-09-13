<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<%--
  This snippet supports all data types.
--%>
<%@ include file="/WEB-INF/snippet/common.jsp" %>
<c:if test="${!empty disabled}">
  <tag:img png="warn" title="common.pointWarning"/> <fmt:message key="common.pointWarning"/><br/>
</c:if>
<c:if test="${pointRT.attributes.UNRELIABLE}">
  <tag:img png="warn" title="common.valueUnreliable"/> <fmt:message key="common.valueUnreliable"/>
  <tag:img png="arrow_refresh" title="common.refresh" onclick="WatchListDwr.forcePointRead(${point.id})"/><br/>
</c:if>
<c:forEach items="${events}" var="event">
  <tag:eventIcon event="${event}"/>
  ${sst:time(event.activeTimestamp)} - <sst:i18n message="${event.message}"/>
  <tag:alarmAck event="${event}"/><br/>
</c:forEach>