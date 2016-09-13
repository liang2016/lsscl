<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%><%@include file="/WEB-INF/tags/decl.tagf"%><%--
--%><%@attribute name="duration" type="java.lang.Integer" required="true"%><%--
--%><%@attribute name="durationType" required="true"%><%--
--%><%@attribute name="width" type="java.lang.Integer" required="true"%><%--
--%><%@attribute name="height" type="java.lang.Integer" required="true"%><%--
--%><sst:list var="chartPointList"/><%--
--%><jsp:doBody/><%--
--%><mango:chart duration="${duration}" durationType="${durationType}" width="${width}" height="${height}"><%--
  --%><c:forEach items="${chartPointList}" var="chartPoint"><%--
    --%><mango:chartPoint xid="${chartPoint.xid}" color="${chartPoint.color}"/><%--
  --%></c:forEach><%--
--%></mango:chart><%--
--%><img id="c${componentId}" src="images/hourglass.png" border="0"/><%--
--%><script type="text/javascript">
  mango.view.custom.functions["c${componentId}"] = function(value) { $("c${componentId}").src = value; }
</script>