<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>

<%@page import="com.serotonin.mango.vo.dataSource.vmstat.VMStatDataSourceVO"%>
<%@page import="com.serotonin.mango.vo.dataSource.vmstat.VMStatPointLocatorVO"%>
<script type="text/javascript">
  function saveDataSourceImpl() {
      DataSourceEditDwr.saveVMStatDataSource($get("dataSourceName"), $get("dataSourceXid"), $get("pollSeconds"),
              $get("outputScale"), saveDataSourceCB);
  }
  
  function appendPointListColumnFunctions(pointListColumnHeaders, pointListColumnFunctions) {
      pointListColumnHeaders[pointListColumnHeaders.length] = "<fmt:message key="dsEdit.vmstat.attribute"/>";
      pointListColumnFunctions[pointListColumnFunctions.length] =
              function(p) { return p.pointLocator.configurationDescription; };
  }
  
  function editPointCBImpl(locator) {
      $set("attributeId", locator.attributeId);
  }
  
  function savePointImpl(locator) {
      delete locator.settable;
      delete locator.dataTypeId;
      
      locator.attributeId = $get("attributeId");
      
      DataSourceEditDwr.saveVMStatPointLocator(currentPoint.id, $get("xid"), $get("name"), locator, savePointCB);
  }
</script>

<c:set var="dsDesc"><fmt:message key="dsEdit.vmstat.desc"/></c:set>
<c:set var="dsHelpId" value="vmstatDS"/>
<%@ include file="/WEB-INF/jsp/dataSourceEdit/dsHead.jspf" %>
  <tr>
    <td class="formLabelRequired"><fmt:message key="dsEdit.vmstat.pollSeconds"/></td>
    <td class="formField"><input id="pollSeconds" type="text" value="${dataSource.pollSeconds}"/></td>
  </tr>
  <tr>
    <td class="formLabelRequired"><fmt:message key="dsEdit.vmstat.outputScale"/></td>
    <td class="formField">
      <sst:select id="outputScale" value="${dataSource.outputScale}">
        <tag:exportCodesOptions sst="true" optionList="<%= VMStatDataSourceVO.OUTPUT_SCALE_CODES.getIdKeys() %>"/>
      </sst:select>
    </td>
  </tr>
<%@ include file="/WEB-INF/jsp/dataSourceEdit/dsEventsFoot.jspf" %>

<tag:pointList pointHelpId="vmstatPP">
  <tr>
    <td class="formLabelRequired"><fmt:message key="dsEdit.vmstat.attribute"/></td>
    <td class="formField">
      <select id="attributeId">
        <tag:exportCodesOptions optionList="<%= VMStatPointLocatorVO.ATTRIBUTE_CODES.getIdKeys() %>"/>
      </select>
    </td>
  </tr>
</tag:pointList>