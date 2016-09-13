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
   	 var escalRecipients;
	  function initCB(data){
 		
   		escalRecipients = new mango.erecip.EmailRecipients("escalRecipients",
                "<sst:i18n key="eventHandlers.escalTestEmailMessage" escapeDQuotes="true"/>",
                data.mailingLists, data.users);
    	escalRecipients.write("escalRecipients", "escalRecipients", "escalationAddresses",
        		"<sst:i18n key="eventHandlers.escal" escapeDQuotes="true"/>");
   		
   $("id").value=data.setting.id;
   escalRecipients.updateRecipientList(data.setting.activeRecipients);
  }
  function saveSendSetting(){
 	hideGenericMessages("genericMessages");
   	var escalList = escalRecipients.createRecipientArray();
   	SendSettingDwr.saveSendSetting($get("id"),$get("scopeId"),1,escalList,saveEventHandlerCB);
  }
  function saveEventHandlerCB(response) {
        if (response.hasMessages){
            showDwrMessages(response.messages, $("genericMessages"));
            setUserMessage("userMessage","");
            }
        else {
        	hide("genericMessages");
            var handler = response.data.handler;
            setUserMessage("userMessage","<fmt:message key="eventHandlers.saved"/>");
           $("id").value=response.data.handler.id;
           escalRecipients.updateRecipientList(response.data.handler.escalationRecipients);
        }
    }
   function setUserMessage(type, msg) {
        if (msg)
            $set(type, msg);
        else
            $set(type, "");
    }
	</script>
	<table id="handler<c:out value="<%=EventHandlerVO.TYPE_EMAIL%>"/>"  class="borderDiv marB marR">
		<tbody>
			<tr align="right">
				<td>
					<input type="hidden" id="id"
						value="<c:out value="<%=Common.NEW_ID%>"/>">
					<tag:img png="cog" title="eventHandlers.eventHandlers" />
					<span class="smallTitle"><fmt:message key="event.send.setting"/></span>
					<input type="hidden" id="scopeId"
						value="${sessionUser.currentScope.id}">
				</td>
				<td align="right">
					<label id="message" style="color: red; font-size: 12px;"></label>
					<tag:img id="saveImg" png="save" title="common.save"
						onclick="saveSendSetting()" />
				</td>
			<tr>
				 <td class="formError" id="userMessage"></td>
			</tr>
			</tr>
		</tbody>

		<tbody id="escalRecipients"></tbody>
		<tbody id="genericMessages"></tbody>
	</table>
</tag:page>
