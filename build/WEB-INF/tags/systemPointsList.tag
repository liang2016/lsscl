<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@include file="/WEB-INF/tags/decl.tagf"%>
<%@tag import="com.serotonin.mango.Common"%>
<%@attribute name="pointHelpId" required="true"%>

<table cellpadding="0" cellspacing="0" id="systemPointsProperties" style="display:none;">
  <tr>
    <td valign="top">
      <div class="borderDiv marR marB">
        <table width="100%">
          <tr>
            <td class="smallTitle"><fmt:message key="dsEdit.system"/></td>
            <td align="right">
              <tag:img id="systemAddImg" png="icon_comp_add"
                      onclick="editSystem(${applicationScope['constants.Common.NEW_ID']})" />
            </td>
          </tr>
        </table>
        <table cellspacing="1"> 
          <thead>
	         <tr class="rowHeader">
	             <td><fmt:message key="dsEdit.name"/></td>
	             <td><fmt:message key="dsEdit.system.type"/></td>
	             <td><fmt:message key="dsEdit.modbus.offset"/></td> 
	             <td></td>
	         </tr>
          </thead>
          <tbody id="systemList" >
          </tbody>
        </table>
      </div>
    </td>

    <td valign="top">
      <div id="systemDetails" class="borderDiv marB" style="display: none;">
        <table width="100%">
          <tr>
            <td>
              <span class="smallTitle"><fmt:message key="dsEdit.system.details"/></span>
              <tag:help id="acpSystemInfo"/>
            </td>
            <td align="right">
              <tag:img id="systemSaveImg" png="save" onclick="saveSystem()" title="common.save"/>
              <tag:img id="systemDeleteImg" png="delete" onclick="deleteSystem()" title="common.delete" />
            </td>
          </tr>
        </table>
        <div id="acpMessage" class="ctxmsg formError"></div>
        
        <table>
        	<tr id="system_success"  style="display:none;font-weight:bold" > 
	         	<td colspan="2" class="formField" class="ctxmsg formError" style="color:red;font-weight:bold"  ><fmt:message key="dsEdit.acp.editsuccess"/></td>
	        </tr>
        	<tr>
	          	<td class="formLabelRequired"><fmt:message key="dsEdit.system.name"/></td>
	         	<td class="formField">
	         		<input type="hidden" id="systemid" name=""systemid"" />
	         		<input type="text" id="systemname"/>
	         		<input type="hidden" id="factoryId" name="factoryId" /> 
	         	</td>
	        </tr>
	        <tr id="system_message"  style="display:none" >
	          	<td></td>
	         	<td class="formField" class="ctxmsg formError" style="color:red;font-weight:bold"  ><fmt:message key="dsEdit.acp.acpname"/></td>
	        </tr>
	        <tr>
	          	<td class="formLabelRequired"><fmt:message key="common.xid"/></td>
	         	<td class="formField"><input type="text" id="systemxid" name="systemxid" /></td>
	        </tr>
	        <tr id="system_xid_message" style="display:none" >
	          	<td></td>
	         	<td class="formField" class="ctxmsg formError"  style="color:red;font-weight:bold"  ><fmt:message key="common.xid"/></td>
	        </tr>
	        <tr>
	         	<td class="formLabelRequired"><fmt:message key="dsEdit.system.type"/></td>
	        	<td class="formField"><select id="systemtype" name="systemtype" > </select></td>
	        </tr>
	        <tr>
	        	<td class="formLabelRequired"><fmt:message key="dsEdit.acp.offset"/></td>
	        	<td class="formField"><input type="text" id="systemoffset" name="systemoffset" /></td>
	        </tr>
	        <tr id="system_offset_message"  style="display:none" >
	          	<td></td>
	         	<td class="formField" class="ctxmsg formError" style="color:red;font-weight:bold"  ><fmt:message key="dsEdit.acp.offset.validation"/></td>
	        </tr>
          <jsp:doBody/>
        </table> 
      </div>
    </td>
  </tr>
</table> 