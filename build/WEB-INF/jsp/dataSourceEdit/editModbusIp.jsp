<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
    
     

     
     
     
     

     
     
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>
<%@page import="com.serotonin.mango.vo.dataSource.modbus.ModbusIpDataSourceVO"%>
<script type="text/javascript">
	
  var currentAcp,currentSystem;
  
  function scanImpl() {
      DataSourceEditDwr.modbusIpScan($get("timeout"), $get("retries"), $get("transportType"), $get("host"), 
              $get("port"), $get("encapsulated"), scanCB);
  }
  
  function locatorTestImpl(locator) {
      DataSourceEditDwr.testModbusIpLocator($get("timeout"), $get("retries"), $get("transportType"), $get("host"), 
              $get("port"), $get("encapsulated"), locator, locatorTestCB);
  }
  
  function dataTestImpl(slaveId, range, offset, length) {
      DataSourceEditDwr.testModbusIpData($get("timeout"), $get("retries"), $get("transportType"), $get("host"), 
              $get("port"), $get("encapsulated"), slaveId, range, offset, length, dataTestCB);
  }
  
  function saveDataSourceImpl() {
      DataSourceEditDwr.saveModbusIpDataSource($get("dataSourceName"), $get("dataSourceXid"), $get("updatePeriods"),
              $get("updatePeriodType"), $get("quantize"), $get("timeout"), $get("retries"), $get

("contiguousBatches"),
              $get("createSlaveMonitorPoints"), $get("maxReadBitCount"), $get("maxReadRegisterCount"), 
              $get("maxWriteRegisterCount"), $get("transportType"), $get("host"), $get("port"), $get

("encapsulated"),
              saveDataSourceCB);
  }
  
   /*************************** acp Information ********************************/
  function writeAcpAndPointList(acpList,pointsOfAcpList){
  		show("acpProperties");
  		if(acpList==null||pointsOfAcpList==null) return;
  		var content="";
  		var dsid = $get("currentDsid");
  		for(var i =0;i<acpList.length;i++){
  			var acp = acpList[i];
  			var pointsOfAcp = pointsOfAcpList[i];
  			content+="<tr class='row' id='acpRow"+acp.id+"' >";
	  			content+="<td><b>"+acp.acpname+"</b></td>";
	  			content+="<td><b>"+acp.xid+"</b></td>";
	  			content+="<td>"+acp.acpTypeVO.typename+"</td>";
	  			content+="<td>"+acp.offset+"</td>";
	  			content+="<td>"+acp.volume+"</td>";
	  			content+="<td>"+acp.pressure+"</td>";
	  			content+="<td>";
		  			content+="&nbsp;<img class='ptr' id='acpEditImg"+acp.id+"' title='<fmt:message key='common.edit' />' src='images/icon_ds_edit.png' onclick='editAcp("+acp.id+")' />&nbsp;";
		  			content+="&nbsp;<img id='acpToggleImg"+acp.id+"' class='ptr' title='<fmt:message key='dsList.show'/>' src='images/arrow_out.png'  onclick='togglePointList("+acp.id+",this,\"points"+acp.id+"\")' />&nbsp;";
	  				content+="&nbsp;<img class='ptr' id='points_on"+acp.id+"'+ style='display:none' src='images/brick_go.png' title='<fmt:message key='dsEdit.acp.points.enabled'/>' alt='<fmt:message key='dsEdit.acp.points.enabled'/>' onclick='toggleAcpList("+acp.id+","+dsid+",true)' />&nbsp;";
	  				content+="&nbsp;<img class='ptr' id='points_off"+acp.id+"' style='display:none' src='images/brick_stop.png' title='<fmt:message key='dsEdit.acp.points.disable'/>' alt='<fmt:message key='dsEdit.acp.points.disable'/>' onclick='toggleAcpList("+acp.id+","+dsid+",false)' />&nbsp;";
	  			content+="</td>";
  			content+="</tr>";
  			content+="<tr id='points"+acp.id+"' style='display:none'><td colspan='4'>";
	  			content+="<table cellspacing='1' cellpadding='0' border='0' style='font-size:11px;'>";
		  			content+="<tr>";
						content+="<td align='center'><b>&nbsp;<fmt:message key='dsEdit.name'/>&nbsp;</b></td>";
						content+="<td align='center'><b>&nbsp;<fmt:message key='dsEdit.pointDataType'/>&nbsp;</b></td>";
						content+="<td align='center'><b>&nbsp;<fmt:message key='dsEdit.status'/>&nbsp;</b></td>";
						content+="<td align='center'><b>&nbsp;<fmt:message key='dsEdit.modbus.slave'/>&nbsp;</b></td>";
						content+="<td align='center'><b>&nbsp;<fmt:message key='dsEdit.modbus.range'/>&nbsp;</b></td>";
						content+="<td align='center'><b>&nbsp;<fmt:message key='dsEdit.modbus.offset'/>&nbsp;</b></td>";
			 		content+="</tr>";
			for(var j = 0;j<pointsOfAcp.length;j++){
				var point = pointsOfAcp[j];
				  	content+="<tr id='pointRow"+point.id+"'>";
				  		content+="<td>&nbsp;"+point.name+"&nbsp;</td>";
				  		content+="<td>&nbsp;"+convertDataType(point.pointLocator.modbusDataType)+"&nbsp;</td>";
				if(point.enabled){
					  	content+="<td align='center'>&nbsp;<img id='toggleImg"+point.id+"' class='ptr' onclick='togglePointInAcp("+point.id+")' src='images/brick_go.png' >&nbsp;</td>";
				}else{
				  		content+="<td align='center'>&nbsp;<img id='toggleImg"+point.id+"' class='ptr' onclick='togglePointInAcp("+point.id+")' src='images/brick_stop.png' >&nbsp;</td>";
				}
				  		content+="<td align='center'>&nbsp;"+point.pointLocator.slaveId+"&nbsp;</td>";
				  		content+="<td>&nbsp;"+convertRange(point.pointLocator.range)+"&nbsp;</td>";
				  		content+="<td align='center'>&nbsp;"+point.pointLocator.offset+"&nbsp;</td>";
				  	content+="</tr>";
			}
				content+="</td></table>";
		  	content+="</tr>";
  		}
  		$j("#acpsList2").html(content);
  	}
  		
  	function togglePointList(id,bindImg,pointsRowName){
  		if($(pointsRowName).style.display=='none'){
  			bindImg.src="images/arrow_in.png";
  			bindImg.title="<fmt:message key='dsList.hide'/>"
  			$(pointsRowName).style.display="";
 			$("points_on"+id).style.display="";
 			$("points_off"+id).style.display="";
  		}else{
  			bindImg.src="images/arrow_out.png";
  			bindImg.title="<fmt:message key='dsList.show'/>"
  			$(pointsRowName).style.display="none";
  			$("points_on"+id).style.display="none";
 			$("points_off"+id).style.display="none";
  		}
  		hide("acpDetails");
  	}
  		
  		
    function convertRange(key){
    	var ranges = $("range").options;
    	for(var i =0;i<ranges.length;i++){
    		var range = ranges[i];
    		if(key==range.value){
    			return $(range).innerHTML;
    		}
    	}
    	return "Error";
    }
    function convertDataType(key){
    	var dataTypes = $("modbusDataType").options;
    	for(var i =0;i<dataTypes.length;i++){
    		var dt = dataTypes[i];
    		if(key==dt.value){
    			return $(dt).innerHTML;
    		}
    	}
    	return "Error";
    } 
    
    function deleteAcp(){
    	var dsid = $get("currentDsid");
    	var acpid = $get("acpid");
    	stopImageFader("acpEditImg"+acpid);
    	lastFlicker = "";
    	if(confirm("<fmt:message key="dsEdit.acp.deleteAcp"/>")==true){ 
    		if(confirm("<fmt:message key="dsEdit.acp.deleteAcpAndPoint"/>")==true){ 
    			DataSourceEditDwr.deleteACP(dsid,acpid, deleteAcpCB);
    		}else{
    			return;
    		}
    	} 
    }
    
    function deleteAcpCB(response){
    	hide("acpDetails");
    	writeAcpAndPointList(response.data.acpList,response.data.pointsOfAcpList);
    }
    
    function editAcp(acpId) { 
    	if(acpId==-1) startFlicker("acpAddImg");
    	else startFlicker("acpEditImg"+acpId);
    	hide("pointDetails");
    	hide("systemDetails");
    	DataSourceEditDwr.getACP(acpId, editAcpCB);  
    }
    
    function editAcpCB(response) {
    	document.getElementById("acptype").disabled=""; 
    	document.getElementById("acpoffset").disabled=""; 
    	show("acpDetails");
    	document.getElementById("acp_success").style.display="none"; 
    	document.getElementById("acp_message").style.display="none"; 
    	document.getElementById("acpxid_message").style.display="none"; 
    	document.getElementById("offset_message").style.display="none";
    	document.getElementById("volume_message").style.display="none";
    	document.getElementById("pressure_message").style.display="none";
        currentAcp = response.data.acp; 
        var acptypes = response.data.acptypeList; 
        dwr.util.removeAllOptions('acptype');    
    	dwr.util.addOptions('acptype',acptypes,"id","typename"); 
        if(currentAcp!=undefined){
        	show("acpDeleteImg");
        	$set("acpid",currentAcp.id);
	        $set("factoryId", currentAcp.factoryId); 
	        $set("acpname", currentAcp.acpname); 
	        $set("acpxid", currentAcp.xid); 
	        $set("acpoffset", currentAcp.offset);
	        $set("acpvolume", currentAcp.volume);
	        $set("pressure", currentAcp.pressure);
	        document.getElementById("acpoffset").disabled="disabled";
	        $set("acptype", currentAcp.acpTypeVO.id);
	        document.getElementById("acptype").disabled="disabled";
        }else{
        	hide("acpDeleteImg");
        	$set("acpid",-1);
        	$set("acpname","");  
	        $set("acpoffset", 0);
	        $set("acpvolume", 0);
	        $set("pressure", 0);
	       // DataSourceEditDwr.getACPUniqueXid(function(xid){
	        //	$set("acpxid",xid);  
	       // });
        } 
    }
    
     var acpid,acpoffset,acpxid,acpname,acpvolume,pressure ;
    function saveAcp() { 
    	startImageFader("acpSaveImg");
    	document.getElementById("acp_success").style.display="none"; 
    	document.getElementById("acp_message").style.display="none"; 
    	document.getElementById("acpxid_message").style.display="none"; 
    	document.getElementById("offset_message").style.display="none";
    	acpid = $get("acpid");
    	$set("factoryId",-1); 
    	acpoffset = $get("acpoffset").trim();
    	acpxid = $get("acpxid").trim();
    	acpname = $get("acpname").trim(); 
    	acpvolume = $get("acpvolume").trim();
    	pressure = $get("pressure").trim();
    	if(acpname==""){ 
    		document.getElementById("acp_message").style.display="";
    		stopImageFader("acpSaveImg");
    		return;
    	}else if(acpoffset==""||parseInt(acpoffset)>65535){
    		document.getElementById("offset_message").style.display="";
    		stopImageFader("acpSaveImg");
    		return;
    	}else if(acpvolume==""||parseInt(acpvolume)<=0){
    		document.getElementById("volume_message").style.display="";
    		stopImageFader("acpSaveImg");
    		return;
    	}
    	else if(pressure==""||parseInt(pressure)<=0){
    		document.getElementById("pressure_message").style.display="";
    		stopImageFader("acpSaveImg");
    		return;
    	}
    	DataSourceEditDwr.validateAcp(acpid,acpxid,typeValidateCB);
    }
    
    function typeValidateCB(flag){
    	if(flag){
    		DataSourceEditDwr.typeValidate($get("acptype"),validateAcpCB);
    	}else{
    		document.getElementById("acpxid_message").style.display=""; 
    		stopImageFader("acpSaveImg");
    	}
    }
    
    function validateAcpCB(hasAttr){
    	if(!hasAttr){
    		alert("<fmt:message key="dsEdit.acp.typeError"/>");
    		stopImageFader("systemSaveImg");
    		return;
    	}else{ 
			DataSourceEditDwr.editACP($get("currentDsid"),acpid,acpname,acpxid,$get

("acptype"),acpoffset,$get("factoryId"),0,$get("acpvolume"),$get("pressure"),saveAcpCB);
        }
    }
    
    function saveAcpCB(response){
    	hide("acpxid_message");
    	hide("offset_message");
    	hide("volume_message");
    	hide("pressure_message");
    	document.getElementById("acp_success").style.display="";
    	writeAcpAndPointList(response.data.acpList,response.data.pointsOfAcpList);
    	stopImageFader("acpSaveImg");
    	if(response.data.acpid!=-1){//change status from add to edit 
	    	editAcp(response.data.acpid);
    	}
    }
    
    function toggleAcpList(acpId,dsId,status){
    	DWREngine.setAsync(false);
    	DataSourceEditDwr.toggleAcp(acpId,dsId,status,function(response){
    		writeAcpAndPointList(response.data.acpList,response.data.pointsOfAcpList);
    	});
    	togglePointList(acpId,"acpToggleImg"+acpId,"points"+acpId);
    	DWREngine.setAsync(true);
    }
    
    function toggleSystemList(systemId,dsId,status){
    	DWREngine.setAsync(false);
    	DataSourceEditDwr.toggleSystem(systemId,dsId,status,function(response){
    		writeSystemAndPointList(response.data.systemList,response.data.pointsOfSystemList);
    	});
    	togglePointList(systemId,"systemToggleImg"+systemId,"points"+systemId);
    	DWREngine.setAsync(true);
    }
    
/*************************** system points Information ********************************/
  	function writeSystemAndPointList(systemList,pointsOfSystemList){
  		show("systemPointsProperties");
  		if(systemList==null||pointsOfSystemList==null) return;
  		var content="";
  		var dsid = $get("currentDsid");
  		for(var i =0;i<systemList.length;i++){
  			var system = systemList[i];
  			var pointsOfSystem = pointsOfSystemList[i];
  			content+="<tr class='row' id='systemRow"+system.id+"' >";
  			content+="<td><b>"+system.acpname+"</b></td>";
  			content+="<td>"+system.acpTypeVO.typename+"</td>";
	  			content+="<td>"+system.offset+"</td>";
	  			content+="<td>";
		  			content+="&nbsp;<img class='ptr' id='systemEditImg"+system.id+"' title='<fmt:message key='common.edit' />' src='images/icon_ds_edit.png' onclick='editSystem("+system.id+")' />&nbsp;";
		  			content+="&nbsp;<img id='systemToggleImg"+system.id+"' title='<fmt:message key='dsList.show'/>' class='ptr' src='images/arrow_out.png'    onclick='togglePointList("+system.id+",this,\"points"+system.id+"\")' />&nbsp;";
	  				content+="&nbsp;<img class='ptr'  id='points_on"+system.id+"'+ style='display:none'  src='images/brick_go.png' title='<fmt:message key='dsEdit.acp.points.enabled'/>' alt='<fmt:message key='dsEdit.acp.points.enabled'/>' onclick='toggleSystemList("+system.id+","+dsid+",true)' />&nbsp;";
	  				content+="&nbsp;<img class='ptr'  id='points_off"+system.id+"'+ style='display:none'  src='images/brick_stop.png' title='<fmt:message key='dsEdit.acp.points.disable'/>' alt='<fmt:message key='dsEdit.acp.points.disable'/>' onclick='toggleSystemList("+system.id+","+dsid+",false)' />&nbsp;";
	  			content+="</td>";
  			content+="</tr>";
  			content+="<tr id='points"+system.id+"' style='display:none'><td colspan='4'>";
	  			content+="<table cellspacing='1' cellpadding='0' border='0' style='font-size:11px;'>";
		  			content+="<tr>";
						content+="<td align='center'><b>&nbsp;<fmt:message key='dsEdit.name'/>&nbsp;</b></td>";
						content+="<td align='center'><b>&nbsp;<fmt:message key='dsEdit.pointDataType'/>&nbsp;</b></td>";
						content+="<td align='center'><b>&nbsp;<fmt:message key='dsEdit.status'/>&nbsp;</b></td>";
						content+="<td align='center'><b>&nbsp;<fmt:message key='dsEdit.modbus.slave'/>&nbsp;</b></td>";
						content+="<td align='center'><b>&nbsp;<fmt:message key='dsEdit.modbus.range'/>&nbsp;</b></td>";
						content+="<td align='center'><b>&nbsp;<fmt:message key='dsEdit.modbus.offset'/>&nbsp;</b></td>";
			 		content+="</tr>";
			for(var j = 0;j<pointsOfSystem.length;j++){
				var point = pointsOfSystem[j];
				  	content+="<tr id='pointRow"+point.id+"'>";
				  		content+="<td>&nbsp;"+point.name+"&nbsp;</td>";
				  		content+="<td>&nbsp;"+convertDataType(point.pointLocator.modbusDataType)+"&nbsp;</td>";
				if(point.enabled){
					  	content+="<td align='center'>&nbsp;<img id='toggleImg"+point.id+"' class='ptr' onclick='togglePointInAcp("+point.id+")' src='images/brick_go.png' >&nbsp;</td>";
				}else{
				  		content+="<td align='center'>&nbsp;<img id='toggleImg"+point.id+"' class='ptr' onclick='togglePointInAcp("+point.id+")' src='images/brick_stop.png' >&nbsp;</td>";
				}
				  		content+="<td align='center'>&nbsp;"+point.pointLocator.slaveId+"&nbsp;</td>";
				  		content+="<td>&nbsp;"+convertRange(point.pointLocator.range)+"&nbsp;</td>";
				  		content+="<td align='center'>&nbsp;"+point.pointLocator.offset+"&nbsp;</td>";
				  	content+="</tr>";
			}
				content+="</td></table>";
		  	content+="</tr>";
  		}
  		$j("#systemList").html(content);
  	}
  	
    function deleteSystem(){
    	var dsid = $get("currentDsid");
    	var systemid = $get("systemid");
    	stopImageFader("systemEditImg"+systemid);
    	lastFlicker = "";
    	if(confirm("<fmt:message key="dsEdit.system.deleteSystem"/>")==true){ 
    		if(confirm("<fmt:message key="dsEdit.system.deleteSystemAndPoint"/>")==true){ 
    			DataSourceEditDwr.deleteSystem(dsid,systemid, deleteSystemCB);
    		}else{
    			return;
    		}
    	}
    }
    
    function deleteSystemCB(response){
    	hide("systemDetails");
    	writeSystemAndPointList(response.data.systemList,response.data.pointsOfSystemList);
    }

    function editSystem(systemId) { 
    	if(systemId==-1) startFlicker("systemAddImg");
    	else startFlicker("systemEditImg"+systemId);
    	hide("pointDetails");
    	hide("acpDetails");    	
    	DataSourceEditDwr.getSystem(systemId, editSystemCB);  
    }
    
    function editSystemCB(response) {
    	document.getElementById("systemtype").disabled=""; 
    	document.getElementById("systemoffset").disabled=""; 
    	show("systemDetails");
    	document.getElementById("system_success").style.display="none"; 
    	document.getElementById("system_message").style.display="none"; 
    	document.getElementById("system_xid_message").style.display="none"; 
    	document.getElementById("system_offset_message").style.display="none";
        currentSystem = response.data.system; 
        var systemtypes = response.data.systemtypeList; 
        dwr.util.removeAllOptions('systemtype');    
    	dwr.util.addOptions('systemtype',systemtypes,"id","typename"); 
        if(currentSystem!=undefined){
        	show("systemDeleteImg");
        	$set("systemid",currentSystem.id);
	        $set("factoryId", currentSystem.factoryId); 
	        $set("systemname", currentSystem.acpname); 
	        $set("systemxid", currentSystem.xid); 
	        $set("systemoffset", currentSystem.offset);
	        document.getElementById("systemoffset").disabled="disabled";
	        $set("systemtype", currentSystem.acpTypeVO.id);
	        document.getElementById("systemtype").disabled="disabled";
        }else{
        	hide("systemDeleteImg");
        	$set("systemid",-1);
        	$set("systemname","");  
	        $set("systemoffset", 0);
	        DataSourceEditDwr.getACPUniqueXid(function(xid){
	        	$set("systemxid",xid);  
	        });
        } 
    }
    
    var systemid,systemoffset,systemxid,systemname;
    function saveSystem() { 
    	startImageFader("systemSaveImg");
    	document.getElementById("system_success").style.display="none"; 
    	document.getElementById("system_message").style.display="none"; 
    	document.getElementById("system_xid_message").style.display="none"; 
    	document.getElementById("system_offset_message").style.display="none";
    	systemid = $get("systemid");
    	$set("factoryId",-1); 
    	systemoffset = $get("systemoffset").trim();
    	systemxid = $get("systemxid").trim();
    	systemname = $get("systemname").trim(); 
    	if(systemname==""){ 
    		document.getElementById("system_message").style.display="";
    		stopImageFader("systemSaveImg");
    		return;
    	}else if(parseInt(systemoffset)>65535){
    		document.getElementById("system_offset_message").style.display="";
    		stopImageFader("systemSaveImg");
    		return;
    	}
    	DataSourceEditDwr.validateAcp(systemid,systemxid,function(flag){
    		if(flag){
    			DataSourceEditDwr.typeValidate($get("systemtype"),validateSystemCB);
	    	}else{
	    		document.getElementById("system_xid_message").style.display=""; 
	    		stopImageFader("systemSaveImg");
	    	}
    	});
    }
    
    
    function validateSystemCB(hasAttr){
    	if(!hasAttr){
    		alert("<fmt:message key="dsEdit.system.typeError"/>");
    		stopImageFader("systemSaveImg");
    		return;
    	}else{ 
			DataSourceEditDwr.editACP($get("currentDsid"),systemid,systemname,systemxid,$get

("systemtype"),systemoffset,$get("factoryId"),1,-1,0,0,saveSystemCB);
        }
    }
    
    function saveSystemCB(response){
    	document.getElementById("system_success").style.display="";
    	writeSystemAndPointList(response.data.systemList,response.data.pointsOfSystemList);
    	stopImageFader("systemSaveImg");
    	if(response.data.systemid!=-1){//change status from add to edit 
	    	editSystem(response.data.systemid);
    	}
    }
    
    
    var lastFlicker="";
    function startFlicker(id){
    	if(currentPoint!=null){
   			stopImageFader("editImg"+currentPoint.id);
   			stopImageFader("editImg-1");
   		}
    	if(lastFlicker!=""){
    		stopImageFader(lastFlicker);
    	}
    	lastFlicker=id;
    	startImageFader(lastFlicker);
    }
    function stopFlicker(){
	    if(lastFlicker!=""){
    		stopImageFader(lastFlicker);
	    }
    }
    function loadForce(id){
    	alert("<fmt:message key="dsEdit.modbusIp.force.begin"/>");
    	DataSourceEditDwr.forceSourceRead(id,$get("fromYear"), $get("fromMonth"), $get("fromDay"), 
      		  $get("fromHour"), $get("fromMinute"), $get("fromSecond"), $get("toYear"), 
    		  $get("toMonth"), $get("toDay"), $get("toHour"), $get("toMinute"), $get("toSecond"),function (){
    	})
    }
    
    
 
</script>

<tr>
  <td class="formLabelRequired"><fmt:message key="dsEdit.modbusIp.transportType"/></td>
  <td class="formField">
    <sst:select id="transportType" value="${dataSource.transportType}">
	  <sst:option value="<%= ModbusIpDataSourceVO.TransportType.UDP.toString() %>"><fmt:message 

key="dsEdit.modbusIp.transportType.udp"/></sst:option>
      <sst:option value="<%= ModbusIpDataSourceVO.TransportType.TCP.toString() %>"><fmt:message 

key="dsEdit.modbusIp.transportType.tcp"/></sst:option>
      <sst:option value="<%= ModbusIpDataSourceVO.TransportType.TCP_KEEP_ALIVE.toString() %>"><fmt:message 

key="dsEdit.modbusIp.transportType.tcpKA"/></sst:option>
    </sst:select>
  </td>
</tr>

<tr>
  <td class="formLabelRequired"><fmt:message key="dsEdit.modbusIp.host"/></td>
  <td class="formField"><input id="host" type="text" value="${dataSource.host}"/></td>
</tr>

<tr>
  <td class="formLabelRequired"><fmt:message key="dsEdit.modbusIp.port"/></td>
  <td class="formField"><input id="port" type="text" value="${dataSource.port}"/></td>
</tr>

<tr>
  <td class="formLabelRequired"><fmt:message key="dsEdit.modbusIp.encapsulated"/></td>
  <td class="formField"><sst:checkbox id="encapsulated" selectedValue="${dataSource.encapsulated}"/></td>
</tr>
<tr>
  <td class="formLabelRequired"><fmt:message key="dsEdit.modbusIp.force"/></td>
  <td></td>
</tr>
<tr>
  <td colspan="2">
     <tag:dateRangeNoCheck/>
     <input id="force" type="button" value="<fmt:message key="dsEdit.modbusIp.load"/>" onclick="loadForce(${dataSource.id})"/>
  </td>
</tr>