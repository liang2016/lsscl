<%--
  
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2013 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>
<html>
<head>
  <title>
  <c:choose>
    <c:when test="${!empty instanceDescription}">${instanceDescription}</c:when>
    <c:otherwise><fmt:message key="header.title"/></c:otherwise>
  </c:choose>
  </title>
  <meta http-equiv="content-type" content="application/xhtml+xml;charset=utf-8"/>
  <meta http-equiv="Content-Style-Type" content="text/css" />
  <meta name="Copyright" content="&copy;2010-2013 Lsscl Technologies Inc."/>
  <meta name="DESCRIPTION" content="<fmt:message key='common.description'/>"/>
  <meta name="KEYWORDS" content="<fmt:message key='common.keyWords'/>"/>
  <link href="resources/common.css" type="text/css" rel="stylesheet"/>
  <link rel="icon" href="images/favicon.ico"/>
  <link rel="shortcut icon" href="images/favicon.ico"/>
</head>
<body>
<script language="JavaScript">window.location="login.htm";</script>
<a style="font-size: 13px; color: #804000; font-family: Verdana, Arial, Helvetica, sans-serif;" href="login.htm">IReach, by IR</a>
</body>
</html>