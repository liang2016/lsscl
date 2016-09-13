<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>
<tag:page dwr="AppDatasourceSettingDwr" onload="init">
  <script>
    function init() {
    }
    
    function toggleDataSource(dataSourceId) {
        var imgNode = $("dsImg"+ dataSourceId);
        if (!hasImageFader(imgNode)) {
            AppDatasourceSettingDwr.toggleDataSource(dataSourceId, function(result) {
                updateStatusImg($("dsImg"+ result.id), result.enabled, true);
            });
            startImageFader(imgNode);
        }
    }
    
    function toggleDataPoint(dataPointId) {
        var imgNode = $("dpImg"+ dataPointId);
        if (!hasImageFader(imgNode)) {
            AppDatasourceSettingDwr.toggleDataPoint(dataPointId, function(result) {
                updateStatusImg($("dpImg"+ result.data.id), result.data.enabled, false);
            });
            startImageFader(imgNode);
        }
    }
    
    function addAcp() {
        window.location = "appAcpEdit.shtm?factoryId="+<c:out value="${sessionUser.currentScope.id}" />;
    }
    function deleteAcp(aid){
        if(confirm('<fmt:message key="scope.delete.confirm"/>')){
	    	AppDatasourceSettingDwr.deleteAcp(aid, function(response) {
	               window.location = window.location;
	        });
        }
    }
  </script>
  <table cellspacing="0" cellpadding="0">
    <tr>
      <td>
        <tag:img png="icon_ds" title="dsList.dataSources"/>
        <span class="smallTitle"><fmt:message key="app.acps"/></span>
        <tag:help id="dataSourceList"/>
      </td>
      <td align="right" id="dataSourceTypesContent">
        <tag:img png="icon_ds_add" title="common.add" onclick="addAcp()"/>
      </td>
    </tr>
    
    <tr>
      <td colspan="2">
        <table cellspacing="0" cellpadding="0" border="0">
          <tr>
            <td colspan="2">
              <table cellspacing="1" cellpadding="0" border="0">
                <tr class="rowHeader">
                  <td><sst:listSort labelKey="app.acpname" field="name" paging="${paging}"/></td>
                  <td><sst:listSort labelKey="dsList.status" field="enabled" paging="${paging}"/></td>
                </tr>
				<c:set var="hideText"><fmt:message key="dsList.hide"/></c:set>
                <c:set var="showText"><fmt:message key="dsList.show"/></c:set>
                <c:forEach items="${paging.data}" var="listParent">
                  <tr class="row" id="dataSourceRow${listParent.id}">
                    <td><b>${listParent.name}</b></td>
					<td>
                      <a href="appAcpEdit.shtm?acpid=${listParent.id}&factoryId=${sessionUser.currentScope.id}"><tag:img png="icon_ds_edit"
                              title="common.edit"/></a>
                      <%--tag:img png="arrow_out" title="dsList.show" onclick="togglePanelVisibility2(this, 'points${listParent.id}', '${hideText}', '${showText}');"/--%>
					  <tag:img png="icon_ds_delete" title="common.delete" id="deleteDataSourceImg${listParent.id}" 
                              onclick="deleteAcp(${listParent.id})"/>
                    </td>
                  </tr>
                  
                </c:forEach>
              </table>
            </td>
          </tr>
          
        </table>  
      </td>
    </tr>
  </table>
</tag:page>