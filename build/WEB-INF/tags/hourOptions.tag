<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%><%@include file="/WEB-INF/tags/decl.tagf"%><%--
--%><%@tag body-content="empty"%><%--
--%><%@attribute name="sst" type="java.lang.Boolean"%>
<c:choose>
  <c:when test="${sst}">
    <c:forEach begin="0" end="23" var="i"><sst:option value="${i}">${mango:padZeros(i, 2)}</sst:option></c:forEach>
  </c:when>
  <c:otherwise>
    <c:forEach begin="0" end="23" var="i"><option value="${i}">${mango:padZeros(i, 2)}</option></c:forEach>
  </c:otherwise>
</c:choose>