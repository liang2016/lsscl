<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>


<%@ include file="/WEB-INF/jsp/include/tech.jsp"%>
<%@page import="com.serotonin.mango.Common"%>
<%@page import="com.serotonin.mango.vo.event.EventHandlerVO"%>
<%@page import="com.serotonin.mango.DataTypes"%>
<tag:page dwr="SendSettingDwr" js="emailRecipients" onload="init">
<script>
  function init(){
  		SendSettingDwr.getInitData(initCB);
  }
   var emailRecipients;
   var escalRecipients;
  function initCB(data){
	    emailRecipients = new mango.erecip.EmailRecipients("emailRecipients",
	                "<sst:i18n key="eventHandlers.recipTestEmailMessage" escapeDQuotes="true"/>",
	                data.mailingLists, data.users);
	    emailRecipients.write("emailRecipients", "emailRecipients", null,
	        		"<sst:i18n key="eventHandlers.emailRecipients" escapeDQuotes="true"/>");
	 /*
	   escalRecipients = new mango.erecip.EmailRecipients("escalRecipients",
	                "<sst:i18n key="eventHandlers.escalTestEmailMessage" escapeDQuotes="true"/>",
	                data.mailingLists, data.users);
	    escalRecipients.write("escalRecipients", "escalRecipients", "escalationAddresses",
	        		"<sst:i18n key="eventHandlers.escalRecipients" escapeDQuotes="true"/>");
	   */
	   $("id").value=data.setting.id;
	   emailRecipients.updateRecipientList(data.setting.activeRecipients);
	 
	   //$("factoryUsers");
	 //escalRecipients.updateRecipientList(data.setting.escalationRecipients);
  }
  
 
  function saveSendSetting(){
	  	hideGenericMessages("genericMessages");
	   	var emailList = emailRecipients.createRecipientArray();
	   SendSettingDwr.saveSendSetting($get("id"),$get("scopeId"),2,emailList,saveEventHandlerCB);
  }
    
    function saveEventHandlerCB(response) {
        if (response.hasMessages){
            showDwrMessages(response.messages, $("genericMessages"));
            hide("userMessage");
            }
        else {
        	hide("genericMessages");
            var handler = response.data.handler;
            setUserMessage("<fmt:message key="eventHandlers.saved"/>");
           $("id").value=response.data.handler.id;
           emailRecipients.updateRecipientList(response.data.handler.activeRecipients);
        }
    }
    function setUserMessage(msg) {
        showMessage("userMessage", msg);
    }
   </script>

    
 <div class="borderDiv marB marR" style="float:left">
	<table width="100%">
		<tbody>
			<tr align="right">
				<td> <input type="hidden" id="id"  value="<c:out value="<%= Common.NEW_ID %>"/>">
					<tag:img png="cog" title="eventHandlers.eventHandlers"/>
    				<span  class="smallTitle"><fmt:message key="event.send.setting"/></span>
    				<input type="hidden" id="scopeId" value="${sessionUser.currentScope.id}">
    			</td>
				<td align="right">
					<label id="message" style="color: red; font-size: 12px;"></label>
					<tag:img id="saveImg" png="save" title="common.save"
						onclick="saveSendSetting()" />
				</td>
			</tr>
			<tr>
				 <td class="formError" id="userMessage"></td>
			</tr>
		</tbody>
		<tbody id="emailRecipients">
		</tbody>
	</table>
	<table>
		<tbody id="genericMessages"></tbody>
	</table>
</div>
</tag:page>
