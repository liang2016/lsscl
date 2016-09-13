<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>
<%@page import="com.serotonin.mango.Common"%>
<%@page import="com.serotonin.mango.vo.event.EventHandlerVO"%>
<%@page import="com.serotonin.mango.DataTypes"%>
<tag:page dwr="SendSettingDwr" js="emailRecipients" onload="init">
<script type='text/javascript' src='/dwr/interface/FactorySettingDwr.js'></script>
<html>
<script type="text/javascript">
     function init() {
       SendSettingDwr.getInitData(initCB);
    }
	    var userSMS;
	    var emailRecipients;
	    var escalRecipients;
	    var escalRecipients2;
	    var inactiveRecipients;
        function initCB(data) {
          //emailRecipients
        emailRecipients = new mango.erecip.EmailRecipients("emailRecipients",
                "<sst:i18n key="eventHandlers.recipTestEmailMessage" escapeDQuotes="true"/>",
                data.mailingLists, data.users);
        emailRecipients.write("emailRecipients", "emailRecipients", null,
        		"<sst:i18n key="eventHandlers.emailRecipients" escapeDQuotes="true"/>");
        //escalRecipients 
        escalRecipients = new mango.erecip.EmailRecipients("escalRecipients",
                "<sst:i18n key="eventHandlers.escalTestEmailMessage" escapeDQuotes="true"/>",
                data.mailingLists, data.users);
        escalRecipients.write("escalRecipients", "escalRecipients", "escalationAddresses2",
        		"<sst:i18n key="eventHandlers.escalRecipients" escapeDQuotes="true"/>");
        
        //escalRecipients2 
         escalRecipients2 = new mango.erecip.EmailRecipients("escalRecipients2",
                "<sst:i18n key="eventHandlers.escalTestEmailMessage" escapeDQuotes="true"/>",
                data.mailingLists, data.users);
        escalRecipients2.write("escalRecipients2", "escalRecipients2", "escalationAddresses22",
        		"<sst:i18n key="eventHandlers.escalRecipients" escapeDQuotes="true"/>");
        //inactiveRecipients 
        inactiveRecipients = new mango.erecip.EmailRecipients("inactiveRecipients",
                "<sst:i18n key="eventHandlers.inactiveTestEmailMessage" escapeDQuotes="true"/>",
                data.mailingLists, data.users);
        inactiveRecipients.write("inactiveRecipients", "inactiveRecipients", "inactiveAddresses2",
        		"<sst:i18n key="eventHandlers.inactiveRecipients" escapeDQuotes="true"/>");
        emailRecipients.updateRecipientList();
        escalRecipients.updateRecipientList();
        escalRecipients2.updateRecipientList();
        inactiveRecipients.updateRecipientList();
        FactorySettingDwr.getSettingById($get("scopeId"),setDB);
    }
  function setDB(handler){
                $set("id",handler.setting.id)
                 emailRecipients.updateRecipientList(handler.setting.activeRecipients);
                 $set("sendEscalation", handler.setting.sendEscalation);
                 $set("escalationDelayType", handler.setting.escalationDelayType);
                 $set("escalationDelay", handler.setting.escalationDelay);
                 escalRecipients.updateRecipientList(handler.setting.escalationRecipients);
                 
                 $set("sendEscalation2", handler.setting.sendEscalation2);
                 $set("escalationDelayType2", handler.setting.escalationDelayType2);
                 $set("escalationDelay2", handler.setting.escalationDelay2);
                 escalRecipients2.updateRecipientList(handler.setting.escalationRecipients2);
                 $set("useSMS", handler.setting.useSMS);
                
                 $set("sendInactive", handler.setting.sendInactive);
                 $set("inactiveOverride", handler.setting.inactiveOverride);
                 inactiveRecipients.updateRecipientList(handler.setting.inactiveRecipients);
        //handlerTypeChanged();
        activeActionChanged();
        inactiveActionChanged();         
        sendEscalationChanged();
        sendEscalationChanged2();
        sendInactiveChanged();
     }
     function activeActionChanged() {
        var action = $get("activeAction");
        if (action == <c:out value="<%= EventHandlerVO.SET_ACTION_POINT_VALUE %>"/>) {
            hide("activeValueToSetRow");
        }
        else if (action == <c:out value="<%= EventHandlerVO.SET_ACTION_STATIC_VALUE %>"/>) {
        	show("activeValueToSetRow");
        }
        else {
        	hide("activeValueToSetRow");
        }
    }
    
    function inactiveActionChanged() {
        var action = $get("inactiveAction");
        if (action == <c:out value="<%= EventHandlerVO.SET_ACTION_POINT_VALUE %>"/>) {
            hide("inactiveValueToSetRow");
        }
        else if (action == <c:out value="<%= EventHandlerVO.SET_ACTION_STATIC_VALUE %>"/>) {
        	show("inactiveValueToSetRow");
        }
        else {
        	hide("inactiveValueToSetRow");
        }
    }
    
        function sendEscalationChanged() {
        if ($get("sendEscalation")) {
        	show("escalationAddresses1");
            show("escalationAddresses2");
        }
        else {
        	hide("escalationAddresses1");
        	hide("escalationAddresses2");
        }
    }
        
        function sendEscalationChanged2() {
        if ($get("sendEscalation2")) {
        	show("escalationAddresses21");
            show("escalationAddresses22");
        }
        else {
        	hide("escalationAddresses21");
        	hide("escalationAddresses22");
        }
    }
     function sendInactiveChanged() {
        if ($get("sendInactive")) {
            show("inactiveAddresses1");
            inactiveOverrideChanged();
        }
        else {
            hide("inactiveAddresses1");
            hide("inactiveAddresses2");
        }
    }
    
    
       function inactiveOverrideChanged() {
        if ($get("inactiveOverride"))
            show("inactiveAddresses2");
        else
            hide("inactiveAddresses2");
    }
    function saveEmialSetting(){
    		hideGenericMessages("genericMessages");
            var emailList = emailRecipients.createRecipientArray();
            var escalList = escalRecipients.createRecipientArray();
            var escalList2 = escalRecipients2.createRecipientArray();
            var inactiveList = inactiveRecipients.createRecipientArray();
            var checkedSMS=$("useSMS");
            if(checkedSMS.checked)
              userSMS=true;
            else
              userSMS=false;
           //default factoryId=43

           FactorySettingDwr.save($get("id"),$get("scopeId"),emailList,$get("sendEscalation"), $get("escalationDelayType"), $get("escalationDelay"),escalList,$get("sendEscalation2"),$get("escalationDelayType2"), $get("escalationDelay2"),escalList2,$get("sendInactive"),$get("inactiveOverride"),inactiveList,userSMS,info);
    }
    function info(response){
    	if (response.hasMessages){
            showDwrMessages(response.messages, $("genericMessages"));
            hide("userMessage");
            }
        else {
        	hide("genericMessages");
            var handler = response.data.handler;
            setUserMessage("<fmt:message key="eventHandlers.saved"/>");
           $("id").value=response.data.handler.id;
           escalRecipients.updateRecipientList(response.data.handler.escalationRecipients);
        }
    }
      function setUserMessage(msg) {
        showMessage("userMessage", msg);
    }
</script>  
  <body>
        
     <table id="handler<c:out value="<%= EventHandlerVO.TYPE_EMAIL %>"/>" class="borderDivPadded">
          <tbody> 
          <tr id="activeValueToSetRow">
              <td class="formLabel"><fmt:message key="eventHandlers.valueToSet"/></td>
              <td class="formField" id="activeValueToSetContent">
              <input type="hidden" id="id"
						value="<c:out value="<%=Common.NEW_ID%>"/>">
						<input type="hidden" id="scopeId"
						value="${sessionUser.currentScope.id}">
              </td>
            </tr>
             <tr id="inactiveValueToSetRow">
              <td class="formLabel"><fmt:message key="eventHandlers.valueToSet"/></td>
              <td class="formField" id="inactiveValueToSetContent"></td>
            </tr>
            <tr align="right">
              <td><tag:img png="cog" title="eventHandlers.eventHandlers"/><span class="smallTitle"><fmt:message key="eventHandlers.eventHandler.default"/></span></td>
              <td><label id="userMessage" class="formError"></label>  <tag:img id="saveImg" png="save" title="common.save" onclick="saveEmialSetting()"/></td>
            </tr>
            </tbody>
            <tbody id="emailRecipients">
            </tbody>
            <tr>
             <td class="formLabelRequired"><fmt:message key="eventHandlers.useSMS"></fmt:message>  </td>
              <td class="formField"><input id="useSMS" type="checkbox"> </td> 
             </tr>
             <tr><td class="horzSeparator" colspan="2"></td></tr>
            <tr>
              <td class="formLabelRequired"><fmt:message key="eventHandlers.escal"/></td>
              <td class="formField"><input id="sendEscalation" type="checkbox" onclick="sendEscalationChanged()"/></td>
            </tr>
            
            <tr id="escalationAddresses1">
              <td class="formLabelRequired"><fmt:message key="eventHandlers.escalPeriod"/></td>
              <td class="formField">
                <input id="escalationDelay" type="text" class="formShort"/>
                <select id="escalationDelayType">
                  <tag:timePeriodOptions min="true" h="true" d="true"/>
                </select>
              </td>
            </tr>
           <!-- <tr><td class="horzSeparator" colspan="2"></td></tr> --> 
            <tbody id="escalRecipients"></tbody>
            
            <!--escalRecipients  -->
             <tr><td class="horzSeparator" colspan="2"></td></tr>
            <tr>
              <td class="formLabelRequired"><fmt:message key="eventHandlers.escal2"/></td>
              <td class="formField"><input id="sendEscalation2" type="checkbox" onclick="sendEscalationChanged2()"/></td>
            </tr>
            
            <tr id="escalationAddresses21">
              <td class="formLabelRequired"><fmt:message key="eventHandlers.escalPeriod"/></td>
              <td class="formField">
                <input id="escalationDelay2" type="text" class="formShort"/>
                <select id="escalationDelayType2">
                  <tag:timePeriodOptions min="true" h="true" d="true"/>
                </select>
              </td>
            </tr>
            <tbody id="escalRecipients2"></tbody>
              <!--escalRecipients2  -->
              
              
            <tr><td class="horzSeparator" colspan="2"></td></tr>
            <tr>
              <td class="formLabelRequired"><fmt:message key="eventHandlers.inactiveNotif"/></td>
              <td class="formField"><input id="sendInactive" type="checkbox" onclick="sendInactiveChanged()"/></td>
            </tr>
            
            <tr id="inactiveAddresses1">
              <td class="formLabelRequired"><fmt:message key="eventHandlers.inactiveOverride"/></td>
              <td class="formField"><input id="inactiveOverride" type="checkbox" onclick="inactiveOverrideChanged()"/></td>
            </tr>
            <tbody id="inactiveRecipients"></tbody>
            <tbody id="genericMessages"></tbody>
          </table>
  </body>
</html>
</tag:page>