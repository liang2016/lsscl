<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<%@ include file="/WEB-INF/snippet/common.jsp" %>
<c:if test="${!empty startsAndRuntimes}">
  <b><fmt:message key="common.stats.start"/></b>: ${sst:fullTime(start)}<br/>
  <b><fmt:message key="common.stats.end"/></b>: ${sst:fullTime(end)}<br/>
  <table>
    <tr>
      <th><fmt:message key="common.value"/></th>
      <th><fmt:message key="common.stats.starts"/></th>
      <th><fmt:message key="common.stats.runtime"/></th>
    </tr>
  <c:forEach items="${startsAndRuntimes}" var="sar">
    <tr>
      <td>${mango:htmlTextValue(point, sar.mangoValue)}</td>
      <td align="right">${sar.starts}</td>
      <td align="right"><fmt:formatNumber value="${sar.proportion}" pattern="0%"/></td>
    </tr>
  </c:forEach>
  </table>
</c:if>
<c:if test="${!empty average}">
  <c:choose>
    <c:when test="${noData}">
      <b><fmt:message key="common.noData"/></b><br/>
    </c:when>
    <c:otherwise>
      <b><fmt:message key="common.stats.start"/></b>: ${sst:fullTime(start)}<br/>
      <b><fmt:message key="common.stats.end"/></b>: ${sst:fullTime(end)}<br/>
      <b><fmt:message key="common.stats.min"/></b>: ${mango:specificHtmlTextValue(point, minimum)} @ ${sst:time(minTime)}<br/>
      <b><fmt:message key="common.stats.max"/></b>: ${mango:specificHtmlTextValue(point, maximum)} @ ${sst:time(maxTime)}<br/>
      <b><fmt:message key="common.stats.avg"/></b>: ${mango:specificHtmlTextValue(point, average)}<br/>
      <c:if test="${!empty sum}">
        <b><fmt:message key="common.stats.sum"/></b>: ${mango:specificHtmlTextValue(point, sum)}<br/>
      </c:if>
    </c:otherwise>
  </c:choose>
</c:if>
<b><fmt:message key="common.stats.logEntries"/></b>: ${logEntries}