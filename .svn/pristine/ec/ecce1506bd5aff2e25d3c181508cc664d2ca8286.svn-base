<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@include file="/WEB-INF/tags/decl.tagf"%>
<%@tag import="com.serotonin.mango.Common"%>
<%@attribute name="pointHelpId" required="true"%>

<table cellpadding="0" cellspacing="0" id="acpProperties" style="display:none;">
  <tr>
    <td valign="top">
      <div class="borderDiv marR marB">
        <table width="100%">
          <tr>
            <td class="smallTitle"><fmt:message key="dsEdit.acp"/></td>
            <td align="right">
              <tag:img id="acpAddImg" png="icon_comp_add"
                      onclick="editAcp(${applicationScope['constants.Common.NEW_ID']})" />
            </td>
          </tr>
        </table>
        <table cellspacing="1"> 
          <thead>
	         <tr class="rowHeader">
	             <td><fmt:message key="dsEdit.name"/></td>
	             <td><fmt:message key="common.serial.NO"/></td>
	             <td><fmt:message key="dsEdit.acp.acptype"/></td>
	             <td><fmt:message key="dsEdit.acp.offset"/></td> 
	             <td><fmt:message key="dsEdit.acp.volume"/></td> 
	             <td><fmt:message key="dsEdit.acp.pressure"/></td> 
	             <td></td>
	         </tr>
          </thead>
          <tbody id="acpsList2" >
          </tbody>
        </table>
      </div>
    </td>

    <td valign="top">
      <div id="acpDetails" class="borderDiv marB" style="display: none;">
        <table width="100%">
          <tr>
            <td>
              <span class="smallTitle"><fmt:message key="dsEdit.acps.details"/></span>
              <tag:help id="acpInfo"/>
            </td>
            <td align="right">
              <tag:img id="acpSaveImg" png="save" onclick="saveAcp()" title="common.save"/>
              <tag:img id="acpDeleteImg" png="delete" onclick="deleteAcp()" title="common.delete" />
            </td>
          </tr>
        </table>
        <div id="acpMessage" class="ctxmsg formError"></div>
        
        <table>
        	<tr id="acp_success"  style="display:none;font-weight:bold" > 
	         	<td colspan="2" class="formField" class="ctxmsg formError" style="color:red;font-weight:bold"  ><fmt:message key="dsEdit.acp.editsuccess"/></td>
	        </tr>
        	<tr>
	          	<td class="formLabelRequired"><fmt:message key="dsEdit.acp.acpname"/></td>
	         	<td class="formField">
	         		<input type="hidden" id="acpid" name="acpid" />
	         		<input type="text" id="acpname"/>
	         		<input type="hidden" id="factoryId" name="factoryId" /> 
	         	</td>
	        </tr>
	        <tr id="acp_message"  style="display:none" >
	          	<td></td>
	         	<td class="formField" class="ctxmsg formError" style="color:red;font-weight:bold"  ><fmt:message key="dsEdit.acp.acpname"/></td>
	        </tr>
	        <tr>
	          	<td class="formLabelRequired"><fmt:message key="common.serial.NO"/></td>
	         	<td class="formField"><input type="text" id="acpxid" name="acpxid" /></td>
	        </tr>
	        <tr id="acpxid_message" style="display:none" >
	          	<td></td>
	         	<td class="formField" class="ctxmsg formError"  style="color:red;font-weight:bold"  ><fmt:message key="common.serial.NO"/></td>
	        </tr>
	        <tr>
	         	<td class="formLabelRequired"><fmt:message key="dsEdit.acp.acptype"/></td>
	        	<td class="formField"><select id="acptype" name="acptype" > </select></td>
	        </tr>
	        <tr>
	        	<td class="formLabelRequired"><fmt:message key="dsEdit.acp.offset"/></td>
	        	<td class="formField"><input type="text" id="acpoffset" name="acpoffset" /></td>
	        </tr>
	        <tr id="offset_message"  style="display:none" >
	          	<td></td>
	         	<td class="formField" class="ctxmsg formError" style="color:red;font-weight:bold"  ><fmt:message key="dsEdit.acp.offset.validation"/></td>
	        </tr>
	        <tr>
	        	<td class="formLabelRequired"><fmt:message key="dsEdit.acp.volume" /></td>
	        	<td class="formField"><input type="text" id="acpvolume" name="acpvolume" /></td>
	        </tr>
	        <tr id="volume_message"  style="display:none" >
	          	<td></td>
	         	<td class="formField" class="ctxmsg formError" style="color:red;font-weight:bold"  ><fmt:message key="dsEdit.acp.volume.validation"/></td>
	        </tr>
	           <tr>
	        	<td class="formLabelRequired"><fmt:message key="dsEdit.acp.pressure" /></td>
	        	<td class="formField"><input type="text" id="pressure" name="pressure" /></td>
	        </tr>
	        <tr id="pressure_message"  style="display:none" >
	          	<td></td>
	         	<td class="formField" class="ctxmsg formError" style="color:red;font-weight:bold"  ><fmt:message key="dsEdit.acp.pressure.validation"/></td>
	        </tr>
          <jsp:doBody/>
        </table> 
      </div>
    </td>
  </tr>
</table> 