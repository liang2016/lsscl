<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%><%@include file="/WEB-INF/tags/decl.tagf"%><%--
--%><%@tag body-content="empty"%><%--
--%><%@attribute name="head" type="java.lang.Boolean"%>
<c:choose>
  <c:when test="${head}">
     <div><div class="rtop">
     	<div class="r1"></div>
     	<div class="r2"></div>
     	<div class="r3"></div>
     	<div class="r4"></div></div>
     	<div style="height:45px;background-color: white;"><img src="images/loginTitle.png" style="position: relative;left: 80px;top: -10px"/></div>
     </div>
  </c:when>
  <c:otherwise>
    <div class="rtop">
    	<div class="r4"></div>
   		 <div class="r3"></div>
   		 <div class="r2"></div>
    	<div class="r1"></div>
    </div>
  </c:otherwise>
</c:choose>
