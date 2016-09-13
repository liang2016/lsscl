<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%><%@include file="/WEB-INF/tags/decl.tagf"%><%--
--%><%@tag body-content="empty"%><%--
--%><%@attribute name="id" rtexprvalue="true"%><%--
--%><%@attribute name="src"%><%--
--%><%@attribute name="png"%><%--
--%><%@attribute name="title"%><%--
--%><%@attribute name="onclick" rtexprvalue="true"%><%--
--%><%@attribute name="onmouseover"%><%--
--%><%@attribute name="onmouseout"%><%--
--%><%@attribute name="style"%><%--
--%><img<c:if test="${!empty id}"> id="${id}"</c:if><%--
--%><c:if test="${!empty src}"> src="${src}"</c:if><%--
--%><c:if test="${!empty png && empty src}"> src="images/${png}.png"</c:if><%--
--%><c:if test="${!empty title}"> alt="<fmt:message key="${title}"/>" title="<fmt:message key="${title}"/>"</c:if><%--
--%><c:if test="${!empty onclick}"> class="ptr" onclick="${onclick}"</c:if><%--
--%><c:if test="${!empty onmouseover}"> onmouseover="${onmouseover}"</c:if><%--
--%><c:if test="${!empty onmouseout}"> onmouseout="${onmouseout}"</c:if><%--
--%><c:if test="${!empty style}"> style="${style}"</c:if><%--
--%> border="0"/>