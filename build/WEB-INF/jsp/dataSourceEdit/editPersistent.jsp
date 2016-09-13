<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>
<%@page import="com.serotonin.mango.DataTypes"%>
<%@page import="com.serotonin.mango.Common"%>

<script type="text/javascript">
  function initImpl() {
      hide("editImg"+ <c:out value="<%= Common.NEW_ID %>"/>);
  }
  
  function saveDataSourceImpl() {
      DataSourceEditDwr.savePersistentDataSource($get("dataSourceName"), $get("dataSourceXid"), $get("port"),
    		  $get("authorizationKey"), $get("acceptPointUpdates"), saveDataSourceCB);
  }
  
  function appendPointListColumnFunctions(pointListColumnHeaders, pointListColumnFunctions) {
  }
  
  function editPointCBImpl(locator) {
  }
  
  function savePointImpl(locator) {
      delete locator.settable;
      delete locator.dataTypeId;
      DataSourceEditDwr.savePersistentPointLocator(currentPoint.id, $get("xid"), $get("name"), locator, savePointCB);
  }
</script>

<c:set var="dsDesc"><fmt:message key="dsEdit.persistent.desc"/></c:set>
<c:set var="dsHelpId" value="persistentDS"/>
<%@ include file="/WEB-INF/jsp/dataSourceEdit/dsHead.jspf" %>
  <tr>
    <td class="formLabelRequired"><fmt:message key="dsEdit.persistent.port"/></td>
    <td class="formField"><input id="port" type="text" value="${dataSource.port}"/></td>
  </tr>
  
  <tr>
    <td class="formLabelRequired"><fmt:message key="dsEdit.persistent.authorizationKey"/></td>
    <td class="formField"><input id="authorizationKey" type="text" value="${dataSource.authorizationKey}"/></td>
  </tr>
  
  <tr>
    <td class="formLabelRequired"><fmt:message key="dsEdit.persistent.acceptPointUpdates"/></td>
    <td class="formField"><sst:checkbox id="acceptPointUpdates" selectedValue="${dataSource.acceptPointUpdates}"/></td>
  </tr>
<%@ include file="/WEB-INF/jsp/dataSourceEdit/dsEventsFoot.jspf" %>

<tag:pointList pointHelpId="persistentPP">
</tag:pointList>