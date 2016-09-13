<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<%-- The snippet used for table charts in rollovers --%>
<%@ include file="/WEB-INF/snippet/common.jsp" %>
<c:choose>
  <c:when test="${empty chartData}"><fmt:message key="common.noData"/></c:when>
  <c:otherwise>
    <c:forEach items="${chartData}" var="historyPointValue">
      ${mango:pointValueTime(historyPointValue)} - ${mango:htmlText(point, historyPointValue)}
      <c:if test="${historyPointValue.annotated}">
        (<fmt:message key="${historyPointValue.sourceDescriptionKey}">
          <fmt:param>
            <c:choose>
              <c:when test="${empty historyPointValue.sourceDescriptionArgument}"><fmt:message key="common.deleted"/></c:when>
              <c:otherwise>${historyPointValue.sourceDescriptionArgument}</c:otherwise>
            </c:choose>
          </fmt:param>
        </fmt:message>)
      </c:if>
      <br/>
    </c:forEach>
  </c:otherwise>
</c:choose>