<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>
<%@page import="com.serotonin.mango.Common"%>

<tag:page dwr="DataSourceEditDwr" onload="init">
  <script type="text/javascript">
    var currentPoint;
    var pointListColumnFunctions = new Array();
    var pointListOptions;
    
    function init() {
        var pointListColumnHeaders = new Array();
        var acpListColumnHeaders = new Array();//copy to acp
        pointListColumnHeaders.push("<fmt:message key="dsEdit.name"/>");
        pointListColumnFunctions.push(function(p) { return "<b>"+ p.name +"</b>"; });
        pointListColumnHeaders.push("<fmt:message key="dsEdit.pointDataType"/>");
        pointListColumnFunctions.push(function(p) { return p.dataTypeMessage; });
        pointListColumnHeaders.push("<fmt:message key="dsEdit.status"/>");
        pointListColumnFunctions.push(function(p) {
                var id = "toggleImg"+ p.id;
                var onclick = "togglePoint("+ p.id +")";
                if (p.enabled)
                    return writeImage(id, null, "brick_go", "<fmt:message key="common.enabledToggle"/>", onclick);
                return writeImage(id, null, "brick_stop", "<fmt:message key="common.disabledToggle"/>", onclick);
        });
        if (typeof appendPointListColumnFunctions == 'function')
            appendPointListColumnFunctions(pointListColumnHeaders, pointListColumnFunctions);
        pointListColumnHeaders.push("");
        pointListColumnFunctions.push(function(p) {
                return writeImage("editImg"+ p.id, null, "icon_comp_edit", "<fmt:message key="common.edit"/>", "editPoint("+ p.id +")");
        });
        var headers = $("pointListHeaders");
        var td;
        for (var i=0; i<pointListColumnHeaders.length; i++) {
            td = document.createElement("td");
            if (typeof(pointListColumnHeaders[i]) == "string")
                td.innerHTML = pointListColumnHeaders[i];
            else
                pointListColumnHeaders[i](td);
            headers.appendChild(td);
        }
        
        pointListOptions = {
                rowCreator: function(options) {
                    var tr = document.createElement("tr");
                    tr.mangoId = "p"+ options.rowData.id;
                    tr.className = "row"+ (options.rowIndex % 2 == 0 ? "" : "Alt");
                    return tr;
                },
                cellCreator: function(options) {
                    var td = document.createElement("td");
                    if (options.cellNum == 2)
                        td.align = "center";
                    return td;
                }
        };
        
        var dsStatus = $("dsStatusImg");
        setDataSourceStatusImg(${dataSource.enabled}, dsStatus);
        hide(dsStatus);
        
        if (typeof initImpl == 'function') initImpl();
        var dsidStr = document.getElementById("currentDsid").value;
        if(dsidStr!=""){
        	var dsid = parseInt(dsidStr);
        	DataSourceEditDwr.editInit(dsid,initCB);
        }        
      	showMessage("dataSourceMessage");
        showMessage("pointMessage");
        if(typeof('force')!='undefined')
        	DataSourceEditDwr.getDateRangeDefaults(<c:out value="<%= Common.TimePeriods.DAYS %>"/>, 1, function(data) { setDateRangeForNoCheck(data); });
    }
    
    function initCB(response) {
        writePointList(response.data.points); 
        writeAlarms(response.data.alarms); 
        <c:choose>
            <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.MODBUS_IP']}">
			    writeAcpAndPointList(response.data.acpList,response.data.pointsOfAcpList); 
			    writeSystemAndPointList(response.data.systemList,response.data.pointsOfSystemList);
			</c:when>
        </c:choose> 
        <c:if test="${!empty param.pid}">
          // Default the selection if the parameter was provided.
          editPoint(${param.pid});
        </c:if>
    }
     
    function saveDataSource() {
        startImageFader("dsSaveImg", true);
        hideContextualMessages($("dataSourceProperties"));
        saveDataSourceImpl();
    }
    
    function saveDataSourceCB(response) {
    	var dsid = response.data.id; 
    	$set("currentDsid",dsid);
        stopImageFader("dsSaveImg");
        if (response.hasMessages)
            showDwrMessages(response.messages, "dataSourceGenericMessages");
        else {
            showMessage("dataSourceMessage", "<fmt:message key="dsEdit.saved"/>");
            DataSourceEditDwr.editInit(dsid,function(response){
			    writePointList(response.data.points);
			    <c:choose>
			    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.MODBUS_IP']}">
	            	writeAcpAndPointList(response.data.acpList,response.data.pointsOfAcpList); 
				    writeSystemAndPointList(response.data.systemList,response.data.pointsOfSystemList);
			    </c:when>
			    </c:choose>
            });
            
        }
        getAlarms();
    }
    
    function toggleDataSource() {
        if (typeof toggleDataSourceImpl == 'function') toggleDataSourceImpl();
        
        var imgNode = $("dsStatusImg");
        if (!hasImageFader(imgNode)) {
            DataSourceEditDwr.toggleEditDataSource(toggleDataSourceCB);
            startImageFader(imgNode);
        }
    }
    
    function toggleDataSourceCB(result) {
        var imgNode = $("dsStatusImg");
        stopImageFader(imgNode);
        setDataSourceStatusImg(result.enabled, imgNode);
        getAlarms();
    }
    
    function togglePoint(pointId) {
        DataSourceEditDwr.togglePoint(pointId, togglePointCB);
        startImageFader("toggleImg"+ pointId, true);
    }
    
    function togglePointInAcp(pointId) {
        DataSourceEditDwr.togglePoint(pointId, togglePointInAcpCB);
        startImageFader("toggleImg"+ pointId, true);
    }
    
    function togglePointInAcpCB(response) {
        stopImageFader("toggleImg"+ response.data.id);
        if(response.data.enabled){
        	$("toggleImg"+response.data.id).src="images/brick_go.png";
        }else{
        	$("toggleImg"+response.data.id).src="images/brick_stop.png";
        }
    }
    function togglePointCB(response) {
        stopImageFader("toggleImg"+ response.data.id);
        writePointList(response.data.points);
    }
    
    function deletePoint() {
        if (confirm("<fmt:message key="dsEdit.deleteConfirm"/>")) {
            DataSourceEditDwr.deletePoint(currentPoint.id, deletePointCB);
            startImageFader("pointDeleteImg", true);
        }
    }
    
    function deletePointCB(points) {
        stopImageFader("pointDeleteImg");
        hide("pointDetails");
        currentPoint = null;
        writePointList(points);
    }
    
    function writePointList(points) {
        if (typeof writePointListImpl == 'function') writePointListImpl(points);
        
        if (!points)
            return;
        show("pointProperties");
        show("alarmsTable");
        show("dsStatusImg");
    
        if (currentPoint)
            stopImageFader("editImg"+ currentPoint.id);
        dwr.util.removeAllRows("pointsList");
        dwr.util.addRows("pointsList", points, pointListColumnFunctions, pointListOptions);
    }
    function addPoint(ref) {
        if (!dojo.html.isShowing("pointProperties")) {
            alert("<fmt:message key="dsEdit.saveWarning"/>");
            return;
        }
        
        if (currentPoint)
            stopImageFader("editImg"+ currentPoint.id);
        
        startImageFader("editImg"+ <c:out value="<%= Common.NEW_ID %>"/>);
        hideContextualMessages("pointProperties");
        
        addPointImpl(ref);
    }
    
    function editPoint(pointId) { 
        if (currentPoint)
            stopImageFader("editImg"+ currentPoint.id);
        DataSourceEditDwr.getPoint(pointId, editPointCB);
        hideContextualMessages("pointProperties");
        <c:choose>
            <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.MODBUS_IP']}">
            	stopFlicker();
	            hide("systemDetails");
	    		hide("acpDetails");
			</c:when>
        </c:choose> 
    }
    //copy to acp
   
    
    function editPointCB(point) { 
        currentPoint = point;
        display("pointDeleteImg", point.id != <c:out value="<%= Common.NEW_ID %>"/>);
        var locator = currentPoint.pointLocator;
        
        $set("name", currentPoint.name);
        $set("xid", currentPoint.xid);
        var cancel;
        if (typeof editPointCBImpl == 'function') cancel = editPointCBImpl(locator);
        if (!cancel) {
            startImageFader("editImg"+ point.id);
            show("pointDetails");
        }
    }
    
    //copy to acp
    
    
    function cancelEditPoint() {
        if (currentPoint) {
            stopImageFader("editImg"+ currentPoint.id);
            currentPoint = null;
            hide("pointDetails");
        }
    }
    
    function savePoint() {
        startImageFader("pointSaveImg", true);
        hideContextualMessages("pointProperties");
        var locator = currentPoint.pointLocator;
        
        // Prevents DWR warnings
        delete locator.configurationDescription;
        delete locator.dataTypeMessage;
        //alert(locator);
        savePointImpl(locator);
    }
    
    function savePointCB(response) {
        stopImageFader("pointSaveImg");
        if (response.hasMessages)
            showDwrMessages(response.messages);
        else {
            writePointList(response.data.points);
            editPoint(response.data.id);
            showMessage("pointMessage", "<fmt:message key="dsEdit.pointSaved"/>");
        }
    }
    
    // copy to acp
   
    
    function getAlarms() {
        DataSourceEditDwr.getAlarms(writeAlarms);
    }
    
    function writeAlarms(alarms) {
        dwr.util.removeAllRows("alarmsList");
        if (alarms.length == 0) {
            show("noAlarmsMsg");
            hide("alarmsList");
        }
        else {
            hide("noAlarmsMsg");
            show("alarmsList");
            dwr.util.addRows("alarmsList", alarms, [
                    function(alarm) {
                        var div = document.createElement("div");
                        var img = document.createElement("img");
                        setAlarmLevelImg(alarm.alarmLevel, img);
                        div.appendChild(img);
                        
                        var span = document.createElement("span");
                        span.innerHTML = alarm.prettyActiveTimestamp +": "+ alarm.message;
                        div.appendChild(span);
                        
                        return div; 
                    }],
                    {
                        cellCreator: function(options) {
                            var td = document.createElement("td");
                            td.className = "formError";
                            return td;
                        }
                    });
        }
    }
    
    function alarmLevelChanged(eventId) {
        var alarmLevel = $get("alarmLevel"+ eventId);
        DataSourceEditDwr.updateEventAlarmLevel(eventId, alarmLevel);
        setAlarmLevelImg(alarmLevel, "alarmLevelImg"+ eventId);
    }
 
 
    function editPointMetaCB(response) {
    	if (response.hasMessages){
    		alert(response.messages[0].genericMessage);
    		return;
    	}
        //currentPoint = response.data.metaPoint.dp;
        var metaPoint= response.data.metaPoint;
       // currentPoint.id=metaPoint.id;
       // display("pointDeleteImg", metaPoint.id != <c:out value="<%= Common.NEW_ID %>"/>);
        var locator = metaPoint.dp.pointLocator;
        
       // $set("acpMetaName", metaPoint.metaName);
        var cancel;
        if (typeof editPointCBImpl == 'function') cancel = editPointCBImpl(locator);
        if (!cancel) {
           //startImageFader("editMetaImg"+ currentPoint.id);
            //show("acpMetaPointInfo");
        }
    }
    
  </script>
  <input type="hidden" value="${dsid}" id="currentDsid"  />
  <table class="borderDiv marB" cellpadding="0" cellspacing="0" id="alarmsTable" style="display:none;"><tr><td>
    <table width="100%">
      <tr>
        <td class="smallTitle"><fmt:message key="dsEdit.currentAlarms"/></td>
        <td align="right"><tag:img png="control_repeat_blue" title="common.refresh" onclick="getAlarms()"/></td>
      </tr>
    </table>
    <table>
      <tr id="noAlarmsMsg"><td><b><fmt:message key="dsEdit.noAlarms"/></b></td></tr>
      <tbody id="alarmsList"></tbody>
    </table>
  </td></tr></table>
  
  <c:choose>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.VIRTUAL']}">
      <jsp:include page="dataSourceEdit/editVirtual.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.MODBUS_SERIAL']}">
      <jsp:include page="dataSourceEdit/editModbus.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.MODBUS_IP']}">
      <jsp:include page="dataSourceEdit/editModbus.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.SPINWAVE']}">
      <jsp:include page="dataSourceEdit/editSpinwave.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.SNMP']}">
      <jsp:include page="dataSourceEdit/editSnmp.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.SQL']}">
      <jsp:include page="dataSourceEdit/editSql.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.HTTP_RECEIVER']}">
      <jsp:include page="dataSourceEdit/editHttpReceiver.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.ONE_WIRE']}">
      <jsp:include page="dataSourceEdit/editOneWire.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.META']}">
      <jsp:include page="dataSourceEdit/editMeta.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.BACNET']}">
      <jsp:include page="dataSourceEdit/editBacnetIp.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.HTTP_RETRIEVER']}">
      <jsp:include page="dataSourceEdit/editHttpRetriever.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.POP3']}">
      <jsp:include page="dataSourceEdit/editPop3.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.NMEA']}">
      <jsp:include page="dataSourceEdit/editNmea.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.GALIL']}">
      <jsp:include page="dataSourceEdit/editGalil.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.HTTP_IMAGE']}">
      <jsp:include page="dataSourceEdit/editHttpImage.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.EBI25']}">
      <jsp:include page="dataSourceEdit/editEBI25.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.VMSTAT']}">
      <jsp:include page="dataSourceEdit/editVMStat.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.VICONICS']}">
      <jsp:include page="dataSourceEdit/editViconics.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.M_BUS']}">
      <jsp:include page="dataSourceEdit/editMBus.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.OPEN_V_4_J']}">
      <jsp:include page="dataSourceEdit/editOpenV4J.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.DNP3_IP']}">
      <jsp:include page="dataSourceEdit/editDnp3.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.DNP3_SERIAL']}">
      <jsp:include page="dataSourceEdit/editDnp3.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.PACHUBE']}">
      <jsp:include page="dataSourceEdit/editPachube.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.PERSISTENT']}">
      <jsp:include page="dataSourceEdit/editPersistent.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.OPC']}">
      <jsp:include page="dataSourceEdit/editOpc.jsp"/>
    </c:when>
    <c:when test="${dataSource.type.id == applicationScope['constants.DataSourceVO.Types.JMX']}">
      <jsp:include page="dataSourceEdit/editJmx.jsp"/>
    </c:when>
  </c:choose>
</tag:page>