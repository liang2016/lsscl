<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%><%@include file="/WEB-INF/tags/decl.tagf"%><%--
--%><%@tag body-content="empty"%><%--
--%><c:if test="${point.pointLocator.relinquishable}"><%--
  --%><a href="#" onclick="mango.view.setPoint(${point.id}, '${componentId}', null); return false;"><fmt:message key="common.relinquish"/></a><%--
--%></c:if>