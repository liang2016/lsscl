<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<%@ include file="/WEB-INF/snippet/common.jsp" %>
<script type="text/javascript">
<!--
/* Do not display text on a fading background. Instead, let it
fade in and out once or twice, then load a new page.        */

//-->

      
      
</script>
<c:set var="content"><%--
  --%><c:choose><%--
    --%><c:when test="${displayPointName}">${pointComponent.name}:&nbsp;<b>${mango:htmlText(point, pointValue)}</b></c:when><%--
    --%><c:otherwise>${mango:htmlText(point, pointValue)}</c:otherwise><%--
  --%></c:choose><%--
--%></c:set>
<c:if test="${!empty styleAttribute}"><div style="${styleAttribute}"></c:if>
<c:choose>
  <c:when test='${!empty viewComponent}'>
    <c:choose>
      <c:when test='${empty viewComponent.bkgdColorOverride}'>
        <span class="simpleRenderer"style="font-weight:${pointComponent.fontWeight};font-size:${pointComponent.fontSize}px;"/>${content}</span>
      </c:when>
      <c:when test='${viewComponent.bkgdColorOverride == "transparent"}'>
        <span class="simpleRenderer" style="font-weight:${pointComponent.fontWeight};font-size:${pointComponent.fontSize}px;background:transparent;border:0;"/>${content}</span>
      </c:when>
      <c:otherwise>
        <span class="simpleRenderer" style="font-weight:${pointComponent.fontWeight};font-size:${pointComponent.fontSize}px;background-color:${viewComponent.bkgdColorOverride};"/>${content}</span>
      </c:otherwise>
    </c:choose>
  </c:when>
  <c:otherwise>
  	
    <c:choose>
  		<c:when test="${pointComponent.defName == 'flashReport'}">
	   		<c:choose>
		      <c:when test='${pointComponent.flashColor}'>
	   			<c:choose>
	   				<c:when test='${pointComponent.flashflag}'>
  						<span class="flashClass" style="font-weight:${pointComponent.fontWeight};font-size:${pointComponent.fontSize}px;border:1;"/><a href="${pointComponent.linkUrl}">${content}</a></span>
	   				</c:when>
	   				<c:otherwise>
		        		<span class="simpleRenderer" style="font-weight:${pointComponent.fontWeight};font-size:${pointComponent.fontSize}px;;border:1;">${content}</span>
	   				</c:otherwise>
	   			</c:choose>
		      </c:when>
		      <c:otherwise>
		        <span class="simpleRenderer" style="font-weight:${pointComponent.fontWeight};font-size:${pointComponent.fontSize}px;;border:1;">${content}</span>
		      </c:otherwise>
	      	</c:choose>
	      	<span style="display:inline-block;" id="p${point.id}ChartMin" onmouseover="showChart(${point.id}, event, this);"
                    onmouseout="hideChart(${point.id}, event, this);">
                 <img alt="" src="images/icon_chart.png"/>
                 <div id="p${point.id}ChartLayer" class="labelDiv" style="visibility:hidden;top:0;left:0;"></div>
                 <textarea style="display:none;" id="p${point.id}Chart">
                 	<img src="chart/${pointComponent.time}_${pointComponent.duration}_${point.id}.png?w=400&h=150" width="400" height="150" alt="<fmt:message key="common.genChart"/>"/>
                 </textarea>
            </span>
	      	
  		</c:when>
  		<c:when test="${pointComponent.defName == 'flashSimple'}">
	   		<c:choose>
		      <c:when test='${pointComponent.flashColor}'>
	   			<c:choose>
	   				<c:when test='${pointComponent.flashflag}'>
  						<span class="flashClass" style="font-weight:${pointComponent.fontWeight};font-size:${pointComponent.fontSize}px;border:1;"/>${content}</span>
	   				</c:when>
	   				<c:otherwise>
		        		<span class="simpleRenderer" style="font-weight:${pointComponent.fontWeight};font-size:${pointComponent.fontSize}px;;border:1;">${content}</span>
	   				</c:otherwise>
	   			</c:choose>
		      </c:when>
		      <c:otherwise>
		        <span class="simpleRenderer" style="font-weight:${pointComponent.fontWeight};font-size:${pointComponent.fontSize}px;;border:1;">${content}</span>
		      </c:otherwise>
	      	</c:choose>
  		</c:when>
	    <c:otherwise>
	   		<c:choose>
		      <c:when test='${empty pointComponent.bkgdColorOverride}'>
		        <span class="simpleRenderer" style="font-weight:${pointComponent.fontWeight};font-size:${pointComponent.fontSize}px;">${content}</span>
		      </c:when>
		      <c:when test='${pointComponent.bkgdColorOverride == "transparent"}'>
		        <span class="simpleRenderer" style="font-weight:${pointComponent.fontWeight};font-size:${pointComponent.fontSize}px;background:transparent;border:0;"/>${content}</span>
		      </c:when> 
		      <c:otherwise>
		        <span class="simpleRenderer" style="font-weight:${pointComponent.fontWeight};font-size:${pointComponent.fontSize}px;background-color:${pointComponent.bkgdColorOverride};"/>${content}</span>
		      </c:otherwise>
	      	</c:choose>
	    </c:otherwise>
    </c:choose>
  </c:otherwise>
</c:choose>
<c:if test="${!empty styleAttribute}"></div></c:if>