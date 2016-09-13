<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>
<%@page import="com.serotonin.mango.vo.UserComment"%>
<%@page import="com.serotonin.mango.rt.event.type.EventType"%>
<%@page import="com.serotonin.mango.web.dwr.EventsDwr"%>
<tag:page dwr="EventsDwr" onload="init">
  <%@ include file="/WEB-INF/jsp/include/userComment.jsp" %>
  <style>
    .incrementControl { width: 2em; }
  </style>
  <script type="text/javascript">
    // Tell the log poll that we're interested in monitoring pending alarms.
    mango.longPoll.pollRequest.pendingAlarms = true;
    dojo.requireLocalization("dojo.i18n.calendar", "gregorian", null, "de,en,es,fi,fr,ROOT,hu,it,ja,ko,nl,pt,pt-br,sv,zh,zh-cn,zh-hk,zh-tw");
    dojo.requireLocalization("dojo.i18n.calendar", "gregorianExtras", null, "ROOT,ja,zh");
  
    function updatePendingAlarmsContent(content) {
        hide("hourglass");
        
        $set("pendingAlarms", content);
        if (content) {
            show("ackAllDiv");
            hide("noAlarms");
        }
        else {
            $set("pendingAlarms", "");
            hide("ackAllDiv");
            show("noAlarms");
        }
    }
    
    function doSearch(page, date) {
        setDisabled("searchBtn", true);
        $set("searchMessage", "<fmt:message key="events.search.searching"/>");
        EventsDwr.search($get("eventId"), $get("eventSourceType"), $get("eventStatus"), $get("alarmLevel"),
                $get("keywords"), page, date, function(results) {
            $set("searchResults", results.data.content);
            setDisabled("searchBtn", false);
            $set("searchMessage", results.data.resultCount);
        });
    }

    function jumpToDate(parent) {
        var div = $("datePickerDiv");
        var bounds = getAbsoluteNodeBounds(parent);
        div.style.top = bounds.y +"px";
        div.style.left = bounds.x +"px";
        var x = dojo.widget.byId("datePicker");
        x.show();
    }

    var dptimeout = null;
    function expireDatePicker() {
        dptimeout = setTimeout(function() { dojo.widget.byId("datePicker").hide(); }, 500);
    }

    function cancelDatePickerExpiry() {
        if (dptimeout) {
            clearTimeout(dptimeout);
            dptimeout = null;
        }
    }

    function jumpToDateClicked(date) {
    	/*  Click on the date when the control before start following the search 
        var x = dojo.widget.byId("datePicker");
        if (x.isShowing()) {
            x.hide();
            doSearch(0, date);
        }
        */
        //Now, click on the date in the text box control only need in reality the current time 
        var x = dojo.widget.byId("datePicker");
        x.hide();
        document.getElementById("underDate").value=date;
    }

    function newSearch() {
    	/*  Click on the button when search before calling these code
    	var x = dojo.widget.byId("datePicker");
        x.setDate(x.today);
        doSearch(0);
    	*/
    	// Now need to add date conditions for inquires the
        var x = dojo.widget.byId("datePicker");
        var underDate = document.getElementById("underDate");
        if(underDate.value==""){
        	//The default date is the current date 
        	x.setDate(x.today);
        	underDate.value = x.getDate();
        }
        doSearch(0,x.getDate());
    }
    
    function silenceAll() {
    	MiscDwr.silenceAll(function(result) {
    		var silenced = result.data.silenced;
    		for (var i=0; i<silenced.length; i++)
    			setSilenced(silenced[i], true);
    	});
    }

    dojo.addOnLoad(function() {
        var x = dojo.widget.byId("datePicker");
        x.hide();
        x.setDate(x.today);
        dojo.event.connect(x,'onValueChanged','jumpToDateClicked');
    });
    
    //When click clear date button call the method  
    function clearDate(){
    	document.getElementById("underDate").value="";
    }
    // When clicking on a page of this method. Execution
    function jumpToPage(pageNum){
    	var x = dojo.widget.byId("datePicker");
    	doSearch(pageNum,x.getDate());
    }
    function init(){
	    if(${sessionUser.homeScope.scopetype==3}){
    		hide("alarms");
    		return;
    		}
	    EventsDwr.init(function(response){
		   $set("alerts", response.data.content); 
		   hide("hourglass1"); 
    	});
    }
  </script>
<table align="center">
	<tr>
	<td align="center">
  <div class="borderDiv" style="clear:left;float:left;">
    <div class="smallTitle titlePadding"><fmt:message key="events.search"/></div>
    <div>
      <table>
        <tr>
          <td class="formLabel"><fmt:message key="events.id"/></td>
          <td class="formField"><input id="eventId" type="text"></td>
        </tr>
        <tr>
          <td class="formLabel"><fmt:message key="events.search.type"/></td>
          <td class="formField">
            <select id="eventSourceType">
              <option value="-1"><fmt:message key="common.all"/></option>
              <option value="<c:out value="<%= EventType.EventSources.DATA_POINT %>"/>"><fmt:message key="eventHandlers.pointEventDetector"/></option>
              <option value="<c:out value="<%= EventType.EventSources.SCHEDULED %>"/>"><fmt:message key="scheduledEvents.ses"/></option>
              <option value="<c:out value="<%= EventType.EventSources.COMPOUND %>"/>"><fmt:message key="compoundDetectors.compoundEventDetectors"/></option>
              <option value="<c:out value="<%= EventType.EventSources.DATA_SOURCE %>"/>"><fmt:message key="eventHandlers.dataSourceEvents"/></option>
              <option value="<c:out value="<%= EventType.EventSources.PUBLISHER %>"/>"><fmt:message key="eventHandlers.publisherEvents"/></option>
              <option value="<c:out value="<%= EventType.EventSources.MAINTENANCE %>"/>"><fmt:message key="eventHandlers.maintenanceEvents"/></option>
              <option value="<c:out value="<%= EventType.EventSources.SYSTEM %>"/>"><fmt:message key="eventHandlers.systemEvents"/></option>
              <option value="<c:out value="<%= EventType.EventSources.AUDIT %>"/>"><fmt:message key="eventHandlers.auditEvents"/></option>
            </select>
          </td>
        </tr>
        <tr>
          <td class="formLabel"><fmt:message key="common.status"/></td>
          <td class="formField">
            <select id="eventStatus">
              <option value="<c:out value="<%= EventsDwr.STATUS_ALL %>"/>"><fmt:message key="common.all"/></option>
              <option value="<c:out value="<%= EventsDwr.STATUS_ACTIVE %>"/>"><fmt:message key="common.active"/></option>
              <option value="<c:out value="<%= EventsDwr.STATUS_RTN %>"/>"><fmt:message key="event.rtn.rtn"/></option>
              <option value="<c:out value="<%= EventsDwr.STATUS_NORTN %>"/>"><fmt:message key="common.nortn"/></option>
            </select>
          </td>
        </tr>
        <tr>
          <td class="formLabel"><fmt:message key="common.alarmLevel"/></td>
          <td class="formField"><select id="alarmLevel"><tag:alarmLevelOptions allOption="true"/></select></td>
        </tr>
        <tr>
          <td class="formLabel"><fmt:message key="events.search.keywords"/></td>
          <td class="formField"><input id="keywords" type="text"/></td>
        </tr>
        <tr>
        <!-- ?? ????-->
          <td class="formLabel"><fmt:message key="events.search.underDate"/></td>
          <td>
          <input type="text" id="underDate" name="underDate" readonly="true" onfocus="jumpToDate(this);return false;"  />
          <input type="button" id="clearBtn" name="clearBtn" value="<fmt:message key="events.search.clearDate"/>"  onclick="clearDate()"     />
          </td>
        </tr>
        <tr>
          <td colspan="2" align="center">
            <input id="searchBtn" type="button" value="<fmt:message key="events.search.search"/>" onclick="newSearch()"/>
            <span id="searchMessage" class="formError"></span>
          </td>
        </tr>
      </table>
    </div>
  <div id="searchResults"></div>
  </div>
 <div  id="alarms" class="borderDiv marB"  style="float:left;">
   <div id="hourglass1" style="padding:6px;text-align:center;"><tag:img png="hourglass"/></div>
   <div style="float:left;">
  	 <div id="alerts" class="titlePadding" style="float:left;"></div>
   </div>
</div>
</td>
  </tr>
  <tr>
  <td>
  <div id="datePickerDiv" style="position:absolute; top:0px; left:0px;" onmouseover="cancelDatePickerExpiry()" onmouseout="expireDatePicker()">
    <div widgetId="datePicker" dojoType="datepicker" dayWidth="narrow" lang="${lang}"></div>
  </div>
</td>
</tr>
<tr  align="center">

</tr>
<tr  align="center">
<td valign="top"  align="center">
<div>
  <div class="borderDiv marB" style="float:left;">
    <div class="smallTitle titlePadding" style="float:left;">
      <tag:img png="flag_white" title="events.alarms"/>
      <fmt:message key="events.pending"/>
    </div>
    <div id="ackAllDiv" class="titlePadding" style="display:none;float:right;">
      <fmt:message key="events.acknowledgeAll"/>
      <tag:img png="tick" onclick="MiscDwr.acknowledgeAllPendingEvents()" title="events.acknowledgeAll"/>&nbsp;
      <fmt:message key="events.silenceAll"/>
      <tag:img png="sound_mute" onclick="silenceAll()" title="events.silenceAll"/><br/>
    </div>
    <div id="pendingAlarms" style="clear:both;"></div>
    <div id="noAlarms" style="display:none;padding:6px;text-align:center;">
      <b><fmt:message key="events.emptyList"/></b>
    </div>
    <div id="hourglass" style="padding:6px;text-align:center;"><tag:img png="hourglass"/></div>
  </div>
 </div>
 </td>
 </tr>
 </table> 
</tag:page>