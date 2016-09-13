<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%><%@include file="/WEB-INF/tags/decl.tagf"%><%--
--%><%@tag body-content="empty"%><%--
--%><%@attribute name="href" required="true"%><%--
--%><%@attribute name="png" required="true"%><%--
--%><%@attribute name="key" required="true"%><%--
--%><c:set var="text"><fmt:message key="${key}"/></c:set><%--
--%><a href="${href}"><tag:img png="${png}" onmouseout="if (typeof hMD == 'function') hMD();"
        onmouseover="if (typeof hMD == 'function') hMD('${text}', this);"/></a>