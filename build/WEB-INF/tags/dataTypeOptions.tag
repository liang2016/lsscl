<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%><%@include file="/WEB-INF/tags/decl.tagf" %><%--
--%><%@tag body-content="empty" %><%--
--%><%@attribute name="excludeBinary" type="java.lang.Boolean" %><%--
--%><%@attribute name="excludeMultistate" type="java.lang.Boolean" %><%--
--%><%@attribute name="excludeNumeric" type="java.lang.Boolean" %><%--
--%><%@attribute name="excludeAlphanumeric" type="java.lang.Boolean" %><%--
--%><%@attribute name="excludeImage" type="java.lang.Boolean" %><%--
--%><%@tag import="com.serotonin.mango.DataTypes"%><%--
--%><c:if test="${!excludeBinary}"><option value="<%= DataTypes.BINARY %>"><fmt:message key="common.dataTypes.binary"/></option></c:if>
<c:if test="${!excludeMultistate}"><option value="<%= DataTypes.MULTISTATE %>"><fmt:message key="common.dataTypes.multistate"/></option></c:if>
<c:if test="${!excludeNumeric}"><option value="<%= DataTypes.NUMERIC %>"><fmt:message key="common.dataTypes.numeric"/></option></c:if>
<c:if test="${!excludeAlphanumeric}"><option value="<%= DataTypes.ALPHANUMERIC %>"><fmt:message key="common.dataTypes.alphanumeric"/></option></c:if>
<c:if test="${!excludeImage}"><option value="<%= DataTypes.IMAGE %>"><fmt:message key="common.dataTypes.image"/></option></c:if>