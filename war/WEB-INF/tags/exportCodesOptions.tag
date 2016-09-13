<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%><%@include file="/WEB-INF/tags/decl.tagf"%><%--
--%><%@tag body-content="empty"%><%--
--%><%@attribute name="optionList" type="java.util.List" required="true"%><%--
--%><%@attribute name="sst" type="java.lang.Boolean"%><%--
--%><c:forEach items="${optionList}" var="option">
  <c:choose>
    <c:when test="${sst}"><sst:option value="${option.key}"><fmt:message key="${option.value}"/></sst:option></c:when>
    <c:otherwise><option value="${option.key}"><fmt:message key="${option.value}"/></option></c:otherwise>
  </c:choose>
</c:forEach>