<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<%@ include file="/WEB-INF/snippet/common.jsp" %>
<c:choose>
  <c:when test="${empty image}"><tag:img png="icon_comp" title="common.noImage"/></c:when>
  <c:otherwise>
    <img src="${image}" width="${pointComponent.width}" height="${pointComponent.height}" alt=""/>
  </c:otherwise>
</c:choose>
<c:if test="${pointComponent.displayText}">
  <div style="position:absolute;left:${pointComponent.textX}px;top:${pointComponent.textY}px;">
    <%@ include file="/WEB-INF/snippet/basicContent.jsp" %>
  </div>
</c:if>