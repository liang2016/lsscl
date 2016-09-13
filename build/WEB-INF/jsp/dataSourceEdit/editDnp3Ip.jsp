<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>
<%@page import="br.org.scadabr.vo.dataSource.dnp3.Dnp3IpDataSourceVO"%>

<script type="text/javascript">
  
function saveDataSourceImpl() {
    DataSourceEditDwr.saveDNP3IpDataSource($get("dataSourceName"), $get("dataSourceXid"),
  		  $get("sourceAddress"), $get("slaveAddress"), $get("host"), $get("port"), 
  		  $get("staticPollPeriods"), $get("rbePollPeriods"),
  		  $get("rbePeriodType"), $get("timeout"), $get("retries"), saveDataSourceCB);
}
</script>

<tr>
  <td class="formLabelRequired"><fmt:message key="dsEdit.dnp3Ip.host"/></td>
  <td class="formField"><input id="host" type="text" value="${dataSource.host}"/></td>
</tr>

<tr>
  <td class="formLabelRequired"><fmt:message key="dsEdit.dnp3Ip.port"/></td>
  <td class="formField"><input id="port" type="text" value="${dataSource.port}"/></td>
</tr>
