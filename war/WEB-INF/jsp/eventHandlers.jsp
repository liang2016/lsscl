<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>
<%@page import="com.serotonin.mango.Common"%>
<%@page import="com.serotonin.mango.vo.event.EventHandlerVO"%>
<%@page import="com.serotonin.mango.DataTypes"%>
<c:set var="NEW_ID"><%= Common.NEW_ID %></c:set>
<tag:page dwr="EventHandlersDwr" js="emailRecipients" onload="init">
<script type='text/javascript' src='/dwr/interface/FactorySettingDwr.js'></script>
  <script>
    function init() {
        EventHandlersDwr.getInitData(initCB);
        var tree = dojo.widget.manager.getWidgetById('eventTypeTree');
        dojo.event.topic.subscribe("eventTypeTree/titleClick", new TreeClickHandler(), 'handle');
    }
    
    var allPoints;
    var defaultHandlerId;
    var emailRecipients;
    var escalRecipients;
    var escalRecipients2;
    var inactiveRecipients;
    
    function initCB(data) {
        <c:if test="${!empty param.ehid}">
          defaultHandlerId = ${param.ehid};
        </c:if>
    
        var i, j, k;
        var dp, ds, p, et;
        var pointNode, dataSourceNode, publisherNode, etNode, wid;
        
        allPoints = data.allPoints;
        
        emailRecipients = new mango.erecip.EmailRecipients("emailRecipients",
                "<sst:i18n key="eventHandlers.recipTestEmailMessage" escapeDQuotes="true"/>",
                data.mailingLists, data.users);
        emailRecipients.write("emailRecipients", "emailRecipients", null,
        		"<sst:i18n key="eventHandlers.emailRecipients" escapeDQuotes="true"/>");
        
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
       

         inactiveRecipients = new mango.erecip.EmailRecipients("inactiveRecipients",
                "<sst:i18n key="eventHandlers.inactiveTestEmailMessage" escapeDQuotes="true"/>",
                data.mailingLists, data.users);
        inactiveRecipients.write("inactiveRecipients", "inactiveRecipients", "inactiveAddresses2",
        		"<sst:i18n key="eventHandlers.inactiveRecipients" escapeDQuotes="true"/>");
        
        var pointRoot = dojo.widget.manager.getWidgetById('rootPoint');
        for (i=0; i<data.dataPoints.length; i++) {
            dp = data.dataPoints[i];
            pointNode = dojo.widget.createWidget("TreeNode", {
                    title: "<img src='images/icon_comp.png'/> "+ dp.name,
                    object: dp
            });
            pointRoot.addChild(pointNode);
            
            for (j=0; j<dp.eventTypes.length; j++) {
                et = dp.eventTypes[j];
                createEventTypeNode("ped"+ et.typeRef2, et, pointNode);
            }
        }
        pointRoot.expand();
        
        var scheduledRoot = dojo.widget.manager.getWidgetById('rootScheduled');
        for (i=0; i<data.scheduledEvents.length; i++) {
            et = data.scheduledEvents[i];
            createEventTypeNode("sch"+ et.typeRef1, et, scheduledRoot);
        }
        scheduledRoot.expand();
        
        var compoundRoot = dojo.widget.manager.getWidgetById('rootCompound');
        for (i=0; i<data.compoundEvents.length; i++) {
            et = data.compoundEvents[i];
            createEventTypeNode("ced"+ et.typeRef1, et, compoundRoot);
        }
        compoundRoot.expand();
          /*
        var dataSourceRoot = dojo.widget.manager.getWidgetById('rootDataSource');
        for (i=0; i<data.dataSources.length; i++) {
            ds = data.dataSources[i];
            dataSourceNode = dojo.widget.createWidget("TreeNode", {
                    title: "<img src='images/icon_ds.png'/> "+ ds.name,
                    object: ds
            });
            dataSourceRoot.addChild(dataSourceNode);
            
            for (j=0; j<ds.eventTypes.length; j++) {
                et = ds.eventTypes[j];
                createEventTypeNode("dse"+ et.typeRef1 +"/"+ et.typeRef2, et, dataSourceNode);
            }
        }
      
        if (data.publishers) {
            var publisherRoot = dojo.widget.manager.getWidgetById('rootPublisher');
            for (i=0; i<data.publishers.length; i++) {
                p = data.publishers[i];
                publisherNode = dojo.widget.createWidget("TreeNode", {
                        title: "<img src='images/transmit.png'/> "+ p.name,
                        object: p
                });
                publisherRoot.addChild(publisherNode);
                
                for (j=0; j<p.eventTypes.length; j++) {
                    et = p.eventTypes[j];
                    createEventTypeNode("pube"+ et.typeRef1 +"/"+ et.typeRef2, et, publisherNode);
                }
            }
        }
        if (data.maintenanceEvents) {
            var maintenanceRoot = dojo.widget.manager.getWidgetById('rootMaintenance');
            for (i=0; i<data.maintenanceEvents.length; i++) {
                et = data.maintenanceEvents[i];
                createEventTypeNode("maint"+ et.typeRef1, et, maintenanceRoot);
            }
        }
        
        if (data.systemEvents) {
            var systemRoot = dojo.widget.manager.getWidgetById('rootSystem');
            for (i=0; i<data.systemEvents.length; i++) {
                et = data.systemEvents[i];
                createEventTypeNode("sys"+ et.typeRef1, et, systemRoot);
            }
        }
      
        if (data.auditEvents) {
            var auditRoot = dojo.widget.manager.getWidgetById('rootAudit');
            for (i=0; i<data.auditEvents.length; i++) {
                et = data.auditEvents[i];
                createEventTypeNode("aud"+ et.typeRef1, et, auditRoot);
            }
        }
        */
        hide("loadingImg");
        show("tree");
        
        // Default the selection of the parameter was provided.
        if (selectedHandlerNode) {
            selectedHandlerNode.onTitleClick();
            var parent = selectedHandlerNode.parent;
            while (parent && parent.expand) {
                parent.expand();
                parent = parent.parent;
            }
        }
        defaultHandlerId = null;
        //add user to SMS
     //   for(var i=0;i<data.users.length;i++){
       //    $("SMSUserS").options[$("SMSUserS").length] = new Option(data.users[i].username,data.users[i].id);
        //}
    }
    function createEventTypeNode(widgetId, eventType, parent) {
        var node = dojo.widget.createWidget("TreeNode", {
                title: "<img id='"+ widgetId +"Img'/> "+ eventType.description,
                widgetId: widgetId,
                object: eventType
        });
        parent.addChild(node);
        setAlarmLevelImg(eventType.alarmLevel, $(widgetId +"Img"));
        addHandlerNodes(eventType.handlers, node);
    }
    
    function addHandlerNodes(handlers, parent) {
        for (var i=0; i<handlers.length; i++)
            parent.addChild(createHandlerNode(handlers[i]));
    }
    
    function createHandlerNode(handler) {
        var img = "images/cog_wrench.png";
        if (handler.handlerType == <c:out value="<%= EventHandlerVO.TYPE_EMAIL %>"/>)
            img = "images/cog_email.png";
        else if (handler.handlerType == <c:out value="<%= EventHandlerVO.TYPE_PROCESS %>"/>)
            img = "images/cog_process.png";
        var node = dojo.widget.createWidget("TreeNode", {
                title: "<img src='"+ img +"'/> <span id='"+ handler.id +"Msg'>"+ handler.message +"</span>",
                widgetId: "h"+ handler.id,
                object: handler
        });
        
        if (handler.id == defaultHandlerId)
            selectedHandlerNode = node;
        
        return node;
    }
    
    var selectedEventTypeNode;
    var selectedHandlerNode;
    
    var TreeClickHandler = function() {
        this.handle = function(message) {
            var widget = message.source;
            var wid = widget.widgetId;
            if (wid.startsWith("ped") || wid.startsWith("sch") || wid.startsWith("ced") || 
                    wid.startsWith("dse") || wid.startsWith("pube") || wid.startsWith("sys") || 
                    wid.startsWith("aud") || wid.startsWith("maint")) {
                selectedEventTypeNode = widget;
                selectedHandlerNode = null;
                showHandlerEdit();
            }
            else if (wid.startsWith("h")) {
                selectedHandlerNode = widget;
                selectedEventTypeNode = selectedHandlerNode.parent;
                showHandlerEdit();
            }
            else
                hide("handlerEditDiv");
        }
    }
    
    function showHandlerEdit() {
     	hideGenericMessages("genericMessages");
    	show("handlerEditDiv");
        setUserMessage("");
        // Set the target points.
        var pointSelect = $("targetPointSelect");
        dwr.util.removeAllOptions(pointSelect);
        for (var i=0; i<allPoints.length; i++) {
            dp = allPoints[i];
            if (dp.settable)
                pointSelect.options[pointSelect.options.length] = new Option(dp.name, dp.id);
        }
        if(${sessionUser.defaultRole.id>5})
            	hide("disableChangeTr");
        if (selectedHandlerNode) {
            $("saveImg").src = "images/save.png";
            show("deleteImg");
            
            // Put values from the handler object into the input controls.
            var handler = selectedHandlerNode.object;
            $set("handlerTypeSelect", handler.handlerType);
            $("handlerTypeSelect").disabled = true;
            $set("xid", handler.xid);
            $set("alias", handler.alias);
            $set("disabled", handler.disabled);
            $set("disableChange",handler.disableChange);
            if (handler.handlerType == <c:out value="<%= EventHandlerVO.TYPE_SET_POINT %>"/>) {
                $set("targetPointSelect", handler.targetPointId);
                $set("activeAction", handler.activeAction);
                $set("inactiveAction", handler.inactiveAction);
            }
            else if (handler.handlerType == <c:out value="<%= EventHandlerVO.TYPE_EMAIL %>"/>) {
                emailRecipients.updateRecipientList(handler.activeRecipients);
                $set("sendEscalation", handler.sendEscalation);
                $set("escalationDelayType", handler.escalationDelayType);
                $set("escalationDelay", handler.escalationDelay);

                $set("sendEscalation2", handler.sendEscalation2);
                $set("escalationDelayType2", handler.escalationDelayType2);
                $set("escalationDelay2", handler.escalationDelay2);
                
                $set("useSMS", handler.useSMS);
                $set("sendContory",handler.sendContory);
                escalRecipients.updateRecipientList(handler.escalationRecipients);

                escalRecipients2.updateRecipientList(handler.escalationRecipients2);

                $set("sendInactive", handler.sendInactive);
                $set("inactiveOverride", handler.inactiveOverride);
                inactiveRecipients.updateRecipientList(handler.inactiveRecipients);
            }
            else if (handler.handlerType == <c:out value="<%= EventHandlerVO.TYPE_PROCESS %>"/>) {
                $set("activeProcessCommand", handler.activeProcessCommand);
                $set("inactiveProcessCommand", handler.inactiveProcessCommand);
            }
        }
        else {
            $("saveImg").src = "images/save_add.png";
            hide("deleteImg");
            $("handlerTypeSelect").disabled = false;
            
            // Clear values that may be left over from another handler.
            $set("xid", "");
            $set("alias", "");
            $set("disabled", false);
			$set("disableChange",false);
            $set("activeAction", <c:out value="<%= EventHandlerVO.SET_ACTION_NONE %>"/>);
            $set("inactiveAction", <c:out value="<%= EventHandlerVO.SET_ACTION_NONE %>"/>);
            $set("sendEscalation", false);
            $set("escalationDelayType", <c:out value="<%= Common.TimePeriods.HOURS %>"/>);
            $set("escalationDelay", 1);

            $set("sendEscalation2", false);
            $set("escalationDelayType2", <c:out value="<%= Common.TimePeriods.HOURS %>"/>);
            $set("escalationDelay2", 1);
            
            $set("sendInactive", false);
            $set("inactiveOverride", false);
            $set("activeProcessCommand", "");
            $set("inactiveProcessCommand", "");
            FactorySettingDwr.getSettingById($get("scopeId"),setDB);
            // Clear the recipient lists.
            //emailRecipients.updateRecipientList();
            //escalRecipients.updateRecipientList();
          //  inactiveRecipients.updateRecipientList();
       
        }
        
        // Set the use source value checkbox.
        handlerTypeChanged();
        activeActionChanged();
        inactiveActionChanged();
        targetPointSelectChanged();
        sendEscalationChanged();
        sendEscalationChanged2();
        sendInactiveChanged();
    }
    function setDB(handler){
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
                 /*2016.2.20 控制指令添加*/
                 $set("sendControy",handler.setting.sendContory);
                 /*2016.2.20 控制指令添加*/
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
    var currentHandlerEditor;
    function handlerTypeChanged() {
        setUserMessage();
        var handlerId = $get("handlerTypeSelect");
        if (currentHandlerEditor) {
        	hide(currentHandlerEditor);
        	hide($(currentHandlerEditor.id +"Img"));
        }
        currentHandlerEditor = $("handler"+ handlerId);
        show(currentHandlerEditor);
        show($(currentHandlerEditor.id +"Img"));
    }
    
    function targetPointSelectChanged() {
        var selectControl = $("targetPointSelect");
        
        // Make sure there are points in the list.
        if (selectControl.options.length == 0)
            return;
        
        // Get the content for the value to set section.
        var targetPointId = selectControl.value;
        var activeValueStr = "";
        var inactiveValueStr = "";
        if (selectedHandlerNode) {
            activeValueStr = selectedHandlerNode.object.activeValueToSet;
            inactiveValueStr = selectedHandlerNode.object.inactiveValueToSet;
        }
        EventHandlersDwr.createSetValueContent(targetPointId, activeValueStr, "Active",
                function(content) { $("activeValueToSetContent").innerHTML = content; });
        EventHandlersDwr.createSetValueContent(targetPointId, inactiveValueStr, "Inactive",
                function(content) { $("inactiveValueToSetContent").innerHTML = content; });
        
        // Update the source point lists.
        var targetDataTypeId = getPoint(targetPointId).dataType;
        var activeSourceSelect = $("activePointId");
        dwr.util.removeAllOptions(activeSourceSelect);
        var inactiveSourceSelect = $("inactivePointId");
        dwr.util.removeAllOptions(inactiveSourceSelect);
        for (var i=0; i<allPoints.length; i++) {
            dp = allPoints[i];
            if (dp.id != targetPointId && dp.dataType == targetDataTypeId) {
                activeSourceSelect.options[activeSourceSelect.options.length] = new Option(dp.name, dp.id);
                inactiveSourceSelect.options[activeSourceSelect.options.length] = new Option(dp.name, dp.id);
            }
        }
        if (selectedHandlerNode) {
            $set(activeSourceSelect, selectedHandlerNode.object.activePointId);
            $set(inactiveSourceSelect, selectedHandlerNode.object.inactivePointId);
        }
    }
    
    function activeActionChanged() {
        var action = $get("activeAction");
        if (action == <c:out value="<%= EventHandlerVO.SET_ACTION_POINT_VALUE %>"/>) {
        	show("activePointIdRow");
            hide("activeValueToSetRow");
        }
        else if (action == <c:out value="<%= EventHandlerVO.SET_ACTION_STATIC_VALUE %>"/>) {
        	hide("activePointIdRow");
        	show("activeValueToSetRow");
        }
        else {
        	hide("activePointIdRow");
        	hide("activeValueToSetRow");
        }
    }
    
    function inactiveActionChanged() {
        var action = $get("inactiveAction");
        if (action == <c:out value="<%= EventHandlerVO.SET_ACTION_POINT_VALUE %>"/>) {
        	show("inactivePointIdRow");
            hide("inactiveValueToSetRow");
        }
        else if (action == <c:out value="<%= EventHandlerVO.SET_ACTION_STATIC_VALUE %>"/>) {
        	hide("inactivePointIdRow");
        	show("inactiveValueToSetRow");
        }
        else {
        	hide("inactivePointIdRow");
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
    
    function getPoint(id) {
        return getElement(allPoints, id);
    }
    
    function saveHandler() {
        setUserMessage();
        hideContextualMessages("scheduledEventDetails")
        hideGenericMessages("genericMessages")
        
        var handlerId = ${NEW_ID};
        if (selectedHandlerNode)
            handlerId = selectedHandlerNode.object.id;
        
        // Do some validation.
        var handlerType = $get("handlerTypeSelect");
        var xid = $get("xid");
        var alias = $get("alias");
        var disabled = $get("disabled");
        var disableChange=$get("disableChange");
        if (handlerType == <c:out value="<%= EventHandlerVO.TYPE_EMAIL %>"/>) {
            var emailList = emailRecipients.createRecipientArray();
            var escalList = escalRecipients.createRecipientArray();
            var escalList2 = escalRecipients2.createRecipientArray();
            var inactiveList = inactiveRecipients.createRecipientArray();
            var useSMS=setUseSMS();
            var sendContory=setSendControy();
            EventHandlersDwr.saveEmailEventHandler($get("scopeId"),selectedEventTypeNode.object.typeId,
                    selectedEventTypeNode.object.typeRef1, selectedEventTypeNode.object.typeRef2, handlerId, xid, alias,
                    disabled,disableChange, emailList,useSMS,sendContory, $get("sendEscalation"), $get("escalationDelayType"), $get("escalationDelay"), 
                    escalList,$get("sendEscalation2"), $get("escalationDelayType2"), $get("escalationDelay2"), 
                    escalList2,  $get("sendInactive"), $get("inactiveOverride"), inactiveList, saveEventHandlerCB);
        }
        else if (handlerType == <c:out value="<%= EventHandlerVO.TYPE_SET_POINT %>"/>) {
            EventHandlersDwr.saveSetPointEventHandler($get("scopeId"),selectedEventTypeNode.object.typeId,
                    selectedEventTypeNode.object.typeRef1, selectedEventTypeNode.object.typeRef2, handlerId, xid, alias,
                    disabled,disableChange, $get("targetPointSelect"), $get("activeAction"), $get("setPointValueActive"), 
                    $get("activePointId"), $get("inactiveAction"), $get("setPointValueInactive"), 
                    $get("inactivePointId"), saveEventHandlerCB);
        }
        else if (handlerType == <c:out value="<%= EventHandlerVO.TYPE_PROCESS %>"/>) {
            EventHandlersDwr.saveProcessEventHandler($get("scopeId"),selectedEventTypeNode.object.typeId,
                    selectedEventTypeNode.object.typeRef1, selectedEventTypeNode.object.typeRef2, handlerId, xid,
                    alias, disabled,disableChange, $get("activeProcessCommand"), $get("inactiveProcessCommand"), saveEventHandlerCB);
        }
    }
    
    function saveEventHandlerCB(response) {
        if (response.hasMessages)
            showDwrMessages(response.messages, $("genericMessages"));
        else {
            var handler = response.data.handler;
            setUserMessage("<fmt:message key="eventHandlers.saved"/>");
            if (!selectedHandlerNode) {
                selectedHandlerNode = createHandlerNode(handler);
                selectedEventTypeNode.addChild(selectedHandlerNode);
                selectedEventTypeNode.expand();
                selectedHandlerNode.onTitleClick();
            }
            else
                $set(handler.id +"Msg", handler.message);
            
            selectedHandlerNode.object = handler;
        }
    }
    
    function deleteHandler() {
        EventHandlersDwr.deleteEventHandler(selectedHandlerNode.object.id);
        selectedEventTypeNode.removeNode(selectedHandlerNode);
        hide("handlerEditDiv");
    }
    
    function setUserMessage(msg) {
        showMessage("userMessage", msg);
    }
    
    function testProcessCommand(nodeId) {
    	EventHandlersDwr.testProcessCommand($get(nodeId), function(msg) {
    		if (msg)
    			alert(msg);
    	});
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
    
    function setUseSMS(){
        var checked=$("useSMS");
     if(checked.checked){
    	 return true;
      }
      else{
    	  return false;
      }
    }
    
    function setSendControy(){
        var checked=$("sendContory");
     if(checked.checked){
    	 return true;
      }
      else{
    	  return false;
      }
    }
    
  </script>
  
  <table class="borderDiv marB"><tr><td>
    <tag:img png="cog" title="eventHandlers.eventHandlers"/>
    <span class="smallTitle"><fmt:message key="eventHandlers.eventHandlers"/></span>
    <tag:help id="eventHandlers"/>
    <input type="hidden" id="scopeId" value="${sessionUser.currentScope.id}">
  </td></tr></table>
  
  <table cellpadding="0" cellspacing="0">
    <tr>
      <td valign="top">
        <div class="borderDivPadded marR">
          <span class="smallTitle"><fmt:message key="eventHandlers.types"/></span>
          <div dojoType="TreeBasicController" widgetId="controller"></div>
          <img src="images/hourglass.png" id="loadingImg"/>
          <div id="tree" style="display:none;">
            <div dojoType="Tree" widgetId="eventTypeTree" listeners="controller" toggle="wipe">
              <div dojoType="TreeNode" title="<img src='images/bell.png'/> <fmt:message key="eventHandlers.pointEventDetector"/>" widgetId="rootPoint"></div>
              <div dojoType="TreeNode" title="<img src='images/clock.png'/> <fmt:message key="scheduledEvents.ses"/>" widgetId="rootScheduled"></div>
              <div dojoType="TreeNode" title="<img src='images/multi_bell.png'/> <fmt:message key="compoundDetectors.compoundEventDetectors"/>" widgetId="rootCompound"></div>
          <!-- <div dojoType="TreeNode" title="<fmt:message key="eventHandlers.dataSourceEvents"/>" widgetId="rootDataSource"></div> -->    
             <!-- <div dojoType="TreeNode" title="<fmt:message key="eventHandlers.publisherEvents"/>" widgetId="rootPublisher"></div>
               <div dojoType="TreeNode" title="<img src='images/hammer.png'/> <fmt:message key="eventHandlers.maintenanceEvents"/>" widgetId="rootMaintenance"></div> -->
         <!--     <div dojoType="TreeNode" title="<fmt:message key="eventHandlers.systemEvents"/>" widgetId="rootSystem"></div>-->
             <!-- <div dojoType="TreeNode" title="<fmt:message key="eventHandlers.auditEvents"/>" widgetId="rootAudit"></div> --> 
            </div>
          </div>
        </div>
      </td>
      
      <td valign="top">
        <div id="handlerEditDiv" class="borderDivPadded" style="display:none;">
          <table width="100%">
            <tr>
              <td class="smallTitle"><fmt:message key="eventHandlers.eventHandler"/></td>
              <td align="right">
                <tag:img id="deleteImg" png="delete" title="common.delete" onclick="deleteHandler();"/>
                <tag:img id="saveImg" png="save" title="common.save" onclick="saveHandler();"/>
              </td>
            </tr>
            <tr><td class="formError" id="userMessage"></td></tr>
          </table>
          
          <table width="100%">
            <tr>
              <td class="formLabelRequired"><fmt:message key="eventHandlers.type"/></td>
              <td class="formField">
                <select id="handlerTypeSelect" onchange="handlerTypeChanged()">
                  <option value="<c:out value="<%= EventHandlerVO. TYPE_EMAIL %>"/>"><fmt:message key="eventHandlers.type.email"/></option>
              <!-- <option value="<c:out value="<%= EventHandlerVO.TYPE_SET_POINT %>"/>"><fmt:message key="eventHandlers.type.setPoint"/></option>
                  <option value="<c:out value="<%= EventHandlerVO.TYPE_PROCESS %>"/>"><fmt:message key="eventHandlers.type.process"/></option> -->    
                </select>
                <tag:img id="handler1Img" png="cog_wrench" title="eventHandlers.type.setPointHandler" style="display:none;"/>
                <tag:img id="handler2Img" png="cog_email" title="eventHandlers.type.emailHandler" style="display:none;"/>
                <tag:img id="handler3Img" png="cog_process" title="eventHandlers.type.processHandler" style="display:none;"/>
              </td>
            </tr>
            
            <tr>
              <td class="formLabelRequired"><fmt:message key="common.xid"/></td>
              <td class="formField"><input type="text" id="xid"/></td>
            </tr>
            
            <tr>
              <td class="formLabelRequired"><fmt:message key="eventHandlers.alias"/></td>
              <td class="formField"><input id="alias" type="text"/></td>
            </tr>
            
            <tr>
              <td class="formLabelRequired"><fmt:message key="common.disabled"/></td>
              <td class="formField"><input type="checkbox" id="disabled"/></td>
            </tr>
            <tr id="disableChangeTr"  <c:if test="${sessionUser.homeScope.scopetype>2 }">style="display: none"</c:if>>
              <td class="formLabelRequired"><fmt:message key="common.disable.disableChange"/></td>
              <td class="formField"><input type="checkbox" id="disableChange"/></td>
            </tr>
            <tr><td class="horzSeparator" colspan="2"></td></tr>
          </table>
          
          <table id="handler<c:out value="<%= EventHandlerVO.TYPE_SET_POINT %>"/>" style="display:none" width="100%">
            <tr>
              <td class="formLabelRequired"><fmt:message key="eventHandlers.target"/></td>
              <td class="formField">
                <select id="targetPointSelect" onchange="targetPointSelectChanged()"></select>
              </td>
            </tr>
            
            <tr>
              <td class="formLabelRequired"><fmt:message key="eventHandlers.activeAction"/></td>
              <td class="formField">
                <select id="activeAction" onchange="activeActionChanged()">
                  <option value="<c:out value="<%= EventHandlerVO.SET_ACTION_NONE %>"/>"><fmt:message key="eventHandlers.action.none"/></option>
                  <option value="<c:out value="<%= EventHandlerVO.SET_ACTION_POINT_VALUE %>"/>"><fmt:message key="eventHandlers.action.point"/></option>
                  <option value="<c:out value="<%= EventHandlerVO.SET_ACTION_STATIC_VALUE %>"/>"><fmt:message key="eventHandlers.action.static"/></option>
                </select>
              </td>
            </tr>
          
            <tr id="activePointIdRow">
              <td class="formLabel"><fmt:message key="eventHandlers.sourcePoint"/></td>
              <td class="formField"><select id="activePointId"></select></td>
            </tr>
          
            <tr id="activeValueToSetRow">
              <td class="formLabel"><fmt:message key="eventHandlers.valueToSet"/></td>
              <td class="formField" id="activeValueToSetContent"></td>
            </tr>
            
            <tr>
              <td class="formLabelRequired"><fmt:message key="eventHandlers.inactiveAction"/></td>
              <td class="formField">
                <select id="inactiveAction" onchange="inactiveActionChanged()">
                  <option value="<c:out value="<%= EventHandlerVO.SET_ACTION_NONE %>"/>"><fmt:message key="eventHandlers.action.none"/></option>
                  <option value="<c:out value="<%= EventHandlerVO.SET_ACTION_POINT_VALUE %>"/>"><fmt:message key="eventHandlers.action.point"/></option>
                  <option value="<c:out value="<%= EventHandlerVO.SET_ACTION_STATIC_VALUE %>"/>"><fmt:message key="eventHandlers.action.static"/></option>
                </select>
              </td>
            </tr>
          
            <tr id="inactivePointIdRow">
              <td class="formLabel"><fmt:message key="eventHandlers.sourcePoint"/></td>
              <td class="formField"><select id="inactivePointId"></select></td>
            </tr>
          
            <tr id="inactiveValueToSetRow">
              <td class="formLabel"><fmt:message key="eventHandlers.valueToSet"/></td>
              <td class="formField" id="inactiveValueToSetContent"></td>
            </tr>
          </table>
            
          <table id="handler<c:out value="<%= EventHandlerVO.TYPE_EMAIL %>"/>" style="display:none" width="100%">
          	 <tr>
             	<td class="formLabelRequired"><fmt:message key="eventHandlers.useSMS"></fmt:message>  </td>
            	<td class="formField"><input id="useSMS" type="checkbox"> </td> 
             </tr>
             <tr>
             	<td class="formLabelRequired"><fmt:message key="eventHandlers.sendContory"></fmt:message>  </td>
            	<td class="formField"><input id="sendContory" type="checkbox"> </td> 
             </tr>
              <tr><td class="horzSeparator" colspan="2"></td></tr>
            <tbody id="emailRecipients"></tbody>
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
           
            <tbody id="escalRecipients"></tbody>
          
          
            <!--escalRecipients2  -->
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
          </table>
          
          <table id="handler<c:out value="<%= EventHandlerVO.TYPE_PROCESS %>"/>" style="display:none" width="100%">
            <tr>
              <td class="formLabelRequired"><fmt:message key="eventHandlers.activeCommand"/></td>
              <td class="formField">
                <input type="text" id="activeProcessCommand" class="formLong"/>
                <tag:img png="cog_go" onclick="testProcessCommand('activeProcessCommand')" title="eventHandlers.commandTest.title"/>
              </td>
            </tr>
            
            <tr>
              <td class="formLabelRequired"><fmt:message key="eventHandlers.inactiveCommand"/></td>
              <td class="formField">
                <input type="text" id="inactiveProcessCommand" class="formLong"/>
                <tag:img png="cog_go" onclick="testProcessCommand('inactiveProcessCommand')" title="eventHandlers.commandTest.title"/>
              </td>
            </tr>
          </table>
          
          <table>
            <tbody id="genericMessages"></tbody>
          </table>
        </div>
      </td>
    </tr>
  </table>
</tag:page>