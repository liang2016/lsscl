<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp"%>
<%@page import="com.serotonin.mango.Common"%>
<tag:page dwr="ZoneDwr">
	<script>
function sure(){
	var sure=window.confirm("<fmt:message key="scope.delete.confirm"/>");
	  if(sure){
	    	var factoryIds =document.getElementsByName("factoryId");
		    var parentIds=document.getElementsByName("parentId");
		    var factorys= new Array();
		    var parent =new Array();
		    var zoneId=$get("subZoneId");
		    if(factoryIds.length>0&&parentIds.length){
			    for(var i=0;i<factoryIds.length;i++){
				  factorys[i]=factoryIds[i].value;
				  parent[i]=parentIds[i].value;
			   }
		   }
		   ZoneDwr.deleteScope(factorys,parent,zoneId,function (data){
			   	if(data==true){
			   		alert("<fmt:message key="zone.delete.success"/>");
			        window.location='subzone_list.shtm?zoneId=${sessionUser.currentScope.id}';
			   	}
			   	else{
			   		alert("<fmt:message key="zone.delete.failed"/>");
			   		}
			   	});

	  }else{
	    return false;
	  }
}
</script>
	<body>
		<table>
			<tr class="rowHeader">
				<td><input type="hidden" id="subZoneId" value="${subZoneId}">
					<fmt:message key="factory.name"></fmt:message>
				</td>
				<td>
					<fmt:message key="user.admin"></fmt:message>
				</td>
				<td>
					<fmt:message key="subzone.select"></fmt:message>
				</td>
			</tr>
			<c:forEach var="subzone" items="${factoryList}" varStatus="index">
				<c:choose>
					<c:when test="${index.index%2==0}">
						<tr class="row">
					</c:when>
					<c:otherwise>
						<tr class="rowAlt">
					</c:otherwise>
				</c:choose>
				<td>
					${subzone.scopename }
					<input type="hidden" name="factoryId" value="${subzone.id}">
				</td>
				<td>
					${subzone.scopeUser.username}
				</td>
				<td>
					<select name="parentId">
						<c:forEach items="${subZoneList}" var="zone">
							<c:if test="${subZoneId!=zone.id}">
								<option value="${zone.id}">
									${zone.scopename}
								</option>
							</c:if>
						</c:forEach>
					</select>
				</td>
				</tr>
			</c:forEach>
			<tr>
				<td colspan="3" align="right">
					<input type="button" id="save" onclick="sure();" value="<fmt:message key="scope.delete.sure"/>">
				</td>
			</tr>
		</table>
	</body>
</tag:page>