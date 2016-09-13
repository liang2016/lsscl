<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<%@ include file="/WEB-INF/snippet/common.jsp" %>
<fmt:message key="common.enterSetPoint"/>:<br/>
<input id="txtChange${componentId}" type="text" value="${mango:rawText(point, pointValue)}" 
        onkeypress="if (event.keyCode==13) $('txtSet${componentId}').onclick();"/>
<a id="txtSet${componentId}" class="ptr"
        onclick="mango.view.setPoint(${point.id}, '${componentId}', $('txtChange${componentId}').value);"><fmt:message key="common.set"/></a>
<tag:relinquish/>