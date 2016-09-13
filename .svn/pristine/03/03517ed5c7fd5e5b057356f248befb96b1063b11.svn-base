<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp"%>
<%@page import="com.serotonin.mango.Common"%>
<tag:page dwr="ZoneDwr">
<script>
function save(){
	var sure=window.confirm("<fmt:message key="scope.delete.confirm"/>");
	  if(sure){
			var subzoneIds =document.getElementsByName("subzone");
		    var parentIds=document.getElementsByName("parentId");
		    var subzone= new Array();
		    var parent =new Array();
		    var zoneId=$get("zoneId");
			if(subzoneIds.length>0&&parentIds.length>0){
			    for(var i=0;i<subzoneIds.length;i++){
				  subzone[i]=subzoneIds[i].value;
				  parent[i]=parentIds[i].value;
			   }
		   }
		   ZoneDwr.deleteScope(subzone,parent,zoneId, function (data){
			   	if(data==true){
			   		 alert("<fmt:message key="zone.delete.success"/>");
			        window.location='zone_list.shtm';
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
				<td>
					<input type="hidden" id="zoneId" value="${zoneid}">
					<fmt:message key="subZone.name"></fmt:message>
				</td>
				<td>
					<fmt:message key="user.admin"></fmt:message>
				</td>
				<td>
					<fmt:message key="zone.select"></fmt:message>
				</td>
			</tr>
			<c:forEach var="subzone" items="${subZoneList}" varStatus="index">
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
					<input type="hidden" name="subzone" value="${subzone.id}">
				</td>
				<td>
					${subzone.scopeUser.username}
				</td>
				<td>
					<select name="parentId">
						<c:forEach items="${zoneList}" var="zone">
							<c:if test="${zoneid!=zone.id}">
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
					<input type="button" id="save" onclick="save();" value="<fmt:message key="scope.delete.sure"/>">
				</td>
			</tr>
		</table>
	</body>
</tag:page>