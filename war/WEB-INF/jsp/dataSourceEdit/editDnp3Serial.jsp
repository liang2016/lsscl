<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>
<%@page import="br.org.scadabr.vo.dataSource.dnp3.Dnp3SerialDataSourceVO"%>

<script type="text/javascript">
function saveDataSourceImpl() {
    DataSourceEditDwr.saveDNP3SerialDataSource($get("dataSourceName"), $get("dataSourceXid"),
  		  $get("sourceAddress"), $get("slaveAddress"), $get("commPortId"), $get("baudRate"), 
  		  $get("staticPollPeriods"), $get("rbePollPeriods"),
  		  $get("rbePeriodType"), $get("timeout"), $get("retries"), saveDataSourceCB);
}
</script>

<tr>
  <td class="formLabelRequired"><fmt:message key="dsEdit.dnp3Serial.commPortId"/></td>
  <td class="formField"><input id="commPortId" type="text" value="${dataSource.commPortId}"/></td>
</tr>

<tr>
  <td class="formLabelRequired"><fmt:message key="dsEdit.dnp3Serial.baud"/></td>
  <td class="formField">
    <sst:select id="baudRate" value="${dataSource.baudRate}">
      <sst:option>110</sst:option>
      <sst:option>300</sst:option>
      <sst:option>1200</sst:option>
      <sst:option>2400</sst:option>
      <sst:option>4800</sst:option>
      <sst:option>9600</sst:option>
      <sst:option>19200</sst:option>
      <sst:option>38400</sst:option>
      <sst:option>57600</sst:option>
      <sst:option>115200</sst:option>
      <sst:option>230400</sst:option>
      <sst:option>460800</sst:option>
      <sst:option>921600</sst:option>
    </sst:select>
  </td>
</tr>