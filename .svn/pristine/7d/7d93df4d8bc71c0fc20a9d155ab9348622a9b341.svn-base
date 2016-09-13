<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>
<%@page import="com.serotonin.mango.Common"%>

<script type="text/javascript">

</script>

<c:set var="dsDesc"><fmt:message key="app.acpedit"/></c:set>
<c:set var="dsHelpId" value="metaDS"/>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>
<table cellpadding="0" cellspacing="0">
  <tr>
    <td valign="top">
      <div class="borderDiv marB marR" id="dataSourceProperties">
        <table width="100%">
          <tr>
            <td class="smallTitle">
              <tag:img png="icon_ds" title="common.edit"/>
              ${dsDesc}
              <tag:help id="${dsHelpId}"/>
            </td>
            <td align="right">
              <tag:img png="icon_ds" onclick="toggleDataSource()" id="dsStatusImg" style="display:none"/>
              <tag:img id="dsSaveImg" png="save" onclick="saveAcp()" title="common.save"/>
            </td>
          </tr>
        </table>
        <div id="dataSourceMessage" class="ctxmsg formError"></div>
        <table>
          <tr>
            <td class="formLabelRequired"><fmt:message key="app.acpname"/></td>
            <td class="formField"><input type="text" id="acpname" value="${acp.name}"/></td>
          </tr>
          <tr>
            <td class="formLabelRequired"><fmt:message key="dsEdit.acp.acptype"/></td>
            <td class="formField"><input type="text" id="acptype" value="${acp.type}"/></td>
          </tr>
           <tr>
            <td class="formLabelRequired"><fmt:message key="dsEdit.acp.volume"/></td>
            <td class="formField"><input type="text" id="acppower" value="${acp.power}"/></td>
          </tr>
        </table>
      </div>
    </td>
  </tr>
</table>

<table cellpadding="0" cellspacing="0" id="pointProperties" style="display:none;">
  <tr>
    <td valign="top" id="pointsTable" style="display:${acp.name!=null?'block':'none'};">
      <div class="borderDiv marR marB">
        <table width="100%">
          <tr>
            <td class="smallTitle"><fmt:message key="dsEdit.points.points"/></td>
            <td align="right">
              <tag:img id="editImg${applicationScope['constants.Common.NEW_ID']}" png="icon_comp_add"
                      onclick="editPoint(${applicationScope['constants.Common.NEW_ID']})" title="emport.added"/>
            </td>
          </tr>
        </table>
        <table cellspacing="1" >
          <tr class="rowHeader" id="pointListHeaders"></tr>
          <tbody id="pointsList"></tbody>
        </table>
      </div>
    </td>

    <td valign="top">
      <div id="pointDetails" class="borderDiv marR marB" style="display: none;">
        <table>
          <tr>
            <td>
              <span class="smallTitle"><fmt:message key="dsEdit.points.details"/></span>
              <tag:help id="${pointHelpId}"/>
            </td>
            <td align="right">
              <tag:img id="pointSaveImg" png="save" onclick="savePoint()" title="common.save"/>
              <tag:img id="pointDeleteImg" png="delete" onclick="deletePoint()" title="common.delete" />
              <tag:img png="save_add" onclick="saveAcpPoints()" title="app.saveAcpPoints"/>
            </td>
          </tr>
        </table>
        <div id="pointMessage" class="ctxmsg formError"></div>
        
        <table>
          <tr>
            <td class="formLabelRequired"><fmt:message key="dsEdit.points.name"/></td>
            <td class="formField"><input type="text" id="name"/></td>
          </tr>
          <tr>
            <td class="formLabelRequired"><fmt:message key="common.access.dataSource"/></td>
            <td class="formField">
	            <select id="dataSourceId" onchange="getAcps()">
	               	<c:forEach items="${dataSources}" var="dataSource">
	               		<option value="${dataSource.id}">${dataSource.name}-(<fmt:message key="${dataSource.type.key}"/>)</option>
	               	</c:forEach>
	            </select>
            </td>
          </tr>
          <tr>
            <td class="formLabelRequired"><fmt:message key="app.acpname"/></td>
            <td class="formField">
	            <select id="acpId" onchange="getAcpPoints()">
	            </select>
            </td>
          </tr>
          <tr>
            <td class="formLabelRequired"><fmt:message key="pointEdit.props.props"/></td>
            <td class="formField">
	            <select id="pointId">
	                <option>test</option>
	                <option>testDataSource</option>
	                <option>apc0003</option>
	            </select>
            </td>
          </tr>
          
        </table>
      </div>
    </td>
  </tr>
</table>