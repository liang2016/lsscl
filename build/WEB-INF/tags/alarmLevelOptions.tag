<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%><%@include file="/WEB-INF/tags/decl.tagf" %><%--
--%><%@tag body-content="empty" %><%--
--%><%@attribute name="allOption" type="java.lang.Boolean" %><%--
--%><%@attribute name="sst" type="java.lang.Boolean" %><%--
--%><%@tag import="com.serotonin.mango.rt.event.AlarmLevels"%><%--
--%><c:choose>
  <c:when test="${sst}">
    <c:if test="${allOption}">
      <sst:option value="-1"><fmt:message key="common.all"/></sst:option>
    </c:if>
    <sst:option value="<%= Integer.toString(AlarmLevels.NONE) %>"><fmt:message key="<%= AlarmLevels.NONE_DESCRIPTION %>"/></sst:option>
    <sst:option value="<%= Integer.toString(AlarmLevels.INFORMATION) %>"><fmt:message key="<%= AlarmLevels.INFORMATION_DESCRIPTION %>"/></sst:option>
    <sst:option value="<%= Integer.toString(AlarmLevels.URGENT) %>"><fmt:message key="<%= AlarmLevels.URGENT_DESCRIPTION %>"/></sst:option>
    <sst:option value="<%= Integer.toString(AlarmLevels.CRITICAL) %>"><fmt:message key="<%= AlarmLevels.CRITICAL_DESCRIPTION %>"/></sst:option>
    <sst:option value="<%= Integer.toString(AlarmLevels.LIFE_SAFETY) %>"><fmt:message key="<%= AlarmLevels.LIFE_SAFETY_DESCRIPTION %>"/></sst:option>
  </c:when>
  <c:otherwise>
    <c:if test="${allOption}">
      <option value="-1"><fmt:message key="common.all"/></option>
    </c:if>
    <option value="<%= AlarmLevels.NONE %>"><fmt:message key="<%= AlarmLevels.NONE_DESCRIPTION %>"/></option>
    <option value="<%= AlarmLevels.INFORMATION %>"><fmt:message key="<%= AlarmLevels.INFORMATION_DESCRIPTION %>"/></option>
    <option value="<%= AlarmLevels.URGENT %>"><fmt:message key="<%= AlarmLevels.URGENT_DESCRIPTION %>"/></option>
    <option value="<%= AlarmLevels.CRITICAL %>"><fmt:message key="<%= AlarmLevels.CRITICAL_DESCRIPTION %>"/></option>
    <option value="<%= AlarmLevels.LIFE_SAFETY %>"><fmt:message key="<%= AlarmLevels.LIFE_SAFETY_DESCRIPTION %>"/></option>
  </c:otherwise>
</c:choose>
