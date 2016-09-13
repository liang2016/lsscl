<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
--%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp"%>

<%@page import="com.serotonin.mango.Common"%>
<%@page import="com.serotonin.modbus4j.code.DataType"%>
<%@page import="com.serotonin.modbus4j.code.RegisterRange"%>
<tag:page dwr="StatisticsDWR" onload="init">
	<script type="text/javascript">
	function init(){
	StatisticsDWR.initConfig(configCB);
	}
	function configCB(DB){
	var acpConfig=DB.data.acpListConfig;
	var acpSystemConfig=DB.data.acpSystemConfigList;
    var acpSystem="";
    var acp="";
	for(var i=0;i<acpSystemConfig.length;i++){
	    var checkTr=document.createElement("tr");
	    var checkTd=document.createElement("td");
	    var checkTd1=document.createElement("td");
	    var checkTd2=document.createElement("td");
	    checkTd.innerHTML="<b>"+acpSystemConfig[i].statisticsName+"</b>";
	    checkTd1.innerHTML=convertDataType(acpSystemConfig[i].dataType);  //common.edit
	    checkTd2.innerHTML="<img id='system"+acpSystemConfig[i].id+"' class='ptr'  src='images/icon_comp_edit.png' alt='<fmt:message key="common.edit"></fmt:message>' title='<fmt:message key="common.edit"></fmt:message>' onclick='editConfig("+acpSystemConfig[i].id+",0)'>";
	    checkTr.appendChild(checkTd);
	    checkTr.appendChild(checkTd1);
	    checkTr.appendChild(checkTd2);
	     if(i%2==0)
	       checkTr.className="row";
	    else
	       checkTr.className="rowAlt";
	    $("acpSystem").appendChild(checkTr);
	 }

	for(var i=0;i<acpConfig.length;i++){
	    var checkTr=document.createElement("tr");
	    var checkTd=document.createElement("td");
	    var checkTd1=document.createElement("td");
	    var checkTd2=document.createElement("td");
	    checkTd.innerHTML="<b>"+acpConfig[i].statisticsName+"</b>";
	    checkTd1.innerHTML=convertDataType(acpConfig[i].dataType);
	    checkTd2.innerHTML="<img id='acp"+acpConfig[i].id+"' class='ptr'  src='images/icon_comp_edit.png' alt='<fmt:message key="common.edit"></fmt:message>' title='<fmt:message key="common.edit"></fmt:message>' onclick='editConfig("+acpConfig[i].id+",1)'>";
	    checkTr.appendChild(checkTd);
	    checkTr.appendChild(checkTd1);
	    checkTr.appendChild(checkTd2);
	    if(i%2==0)
	       checkTr.className="row";
	    else
	       checkTr.className="rowAlt";
	     $("acp").appendChild(checkTr);
	 }
	
	}
	var img="00";
	//编辑系统
	function editConfig(id,type){
    stopImageFader("deleteImg", true);
	if(img!="00")
	 stopImageFader(img, true);
	if(type==0)
		 img="system"+id
	else
		 img="acp"+id
	 startImageFader(img, true);
	 $("message").innerHTML="";
	 show("editConfig");
	 show("deleteImg");
	 StatisticsDWR.getStatisticsConfigById(id,statisticsCB);
	 }
	 function statisticsCB(data){
	   $("useType").value=data.useType;
	   $("id").value=data.id;
	   $("pointName").value=data.statisticsName;
	   var selectDataTypes=$("dataType");
	   for(var i=0;i<selectDataTypes.length;i++){
	   if(selectDataTypes.options[i].value==data.dataType){
	     selectDataTypes.options[i].selected = true; 
	    }
	   }
	 }
	 //保存
	 function save(){
	  startImageFader("saveImg", true);
	 $("message").innerHTML="";
	 var id=$("id").value;
	 var useType=$("useType").value;
	 var paramname=$("pointName").value;
	 var dataType=$("dataType").value;
	 if(paramname.trim()==""){
	 	$("message").innerHTML="<fmt:message key="statistics.config.attr.null"></fmt:message>";
	  return false;
	 }
	 StatisticsDWR.saveStatisticsConfig(id,useType,dataType,paramname,saveCB);
	 }
	 function saveCB(response){
	     if(response.hasMessages)
                showDwrMessages(response.messages);
         $("id").value=response.data.statisticsVo.id;
         $("useType").value=response.data.statisticsVo.useType;
	     $("message").innerHTML="<fmt:message key="statistic.saved"></fmt:message>";	 
		 stopImageFader("saveImg", true);
		 if(img!="00")
		 	stopImageFader(img, true);
		 stopImageFader(addImg, true);
		 //hide("editConfig");
	   //document.removeChild("acpSystem");
      //document.getElementById("acpSystem").removeChild(1);
     clearTr();
	 init();
	 }
	 var addImg="initAdd";
	 function addASConfig(useType){
	 stopImageFader(addImg, true);
	 if(useType==0)
	 	addImg="initAdd";
	 else
	  	addImg="initAddAcp";	
	 startImageFader(addImg, true);
	   $("message").innerHTML="";
	   show("editConfig");
	   hide("deleteImg");
	   $("useType").value=useType;
	   $("id").value=-1;
	   $("pointName").value="";
	 }
	 function deleteConfig(id){
      startImageFader("deleteImg", true);
	  StatisticsDWR.deleteStatisticsConfig(id.value,function(response){
	   if(response.hasMessages){
	    stopImageFader("deleteImg", true);
	   alert(response.messages[0].genericMessage);
	   return false;
	   }
	   hide("editConfig");
	   clearTr();
	   init();
	   stopImageFader("deleteImg", true);
	   img="00";
	   });
	 }
//清除tr
    function clearTr(){
        var oTbody=$("acpSystem");
		var arTr=oTbody.getElementsByTagName("tr")
		for(var i=arTr.length-1;i>=0;i--){
		  oTbody.removeChild(arTr[i]);
		// oTbody.deleteRow(i); 
		}
		
		
	    var acpTbody=$("acp");
		var acpTr=acpTbody.getElementsByTagName("tr")
		for(var i=acpTr.length-1;i>=0;i--){
		 acpTbody.removeChild(acpTr[i]);
		}
    }
    function convertDataType(key){
    	var dataTypes = $("dataType").options;
    	for(var i =0;i<dataTypes.length;i++){
    		var dt = dataTypes[i];
    		if(key==dt.value){
    			return $(dt).innerHTML;
    		}
    	}
    	return "Error";
    }
	</script>
	<table>
		<tr>
		<td>
		<table 	class="borderDiv marR marB" style="float: left;">
		<thead>
			<tr>
				<td colspan="3">
					<span class="smallTitle"><fmt:message key="statistics.acp.config"></fmt:message><tag:help id="acpSystemConfig"/></span>
					<img id="initAdd" src="images/icon_ds_add.png" alt="<fmt:message key="common.add"></fmt:message>" title="<fmt:message key="common.add"></fmt:message>" class="ptr"
						onclick="addASConfig(0)" border="0">
				</td>
			</tr>
			<tr class="rowHeader">
				<td>
					<b><fmt:message key="pointDetails.name"></fmt:message></b>
				</td>
				<td>
					<b><fmt:message key="dsEdit.pointDataType"></fmt:message></b>
				</td>
				<td>
				</td>
			</tr>
		</thead>
		<tbody id="acpSystem">
		</tbody>

		<thead>
			<tr>
				<td colspan="3">
					<hr>
					<span class="smallTitle"><fmt:message key="statistics.commpressior.config"></fmt:message><tag:help id="acpConfig"/></span>
					<img id="initAddAcp" src="images/icon_ds_add.png" alt="<fmt:message key="common.add"></fmt:message>" title="<fmt:message key="common.add"></fmt:message>" class="ptr"
						onclick="addASConfig(1)" border="0">
				</td>
			</tr>
			<tr class="rowHeader">
				<td>
					<b><fmt:message key="pointDetails.name"></fmt:message></b>
				</td>
				<td>
					<b><fmt:message key="dsEdit.pointDataType"></fmt:message></b>
				</td>
				<td>

				</td>
			</tr>
		</thead>

		<tbody id="acp">

		</tbody>
	</table>
	</td>
<td valign="top">	
</div>
<div id="editConfig" class="borderDivPadded"
	style="display: none; float: left;">
	<table>
		<tr>
			<td class="smallTitle">
				<fmt:message key="statistics.detalis"></fmt:message>
				<input id="id" type="hidden">
				<input id="useType" type="hidden">
			</td>
			<td align="right">
				<img id="saveImg" alt="<fmt:message key="common.save"></fmt:message>" class="ptr" id="addImg" src="images/save.png"
					onclick="save()">
				<img alt="<fmt:message key="common.delete"></fmt:message>" id="deleteImg" class="ptr" onclick="deleteConfig($('id'))"
					id="deleteImg" src="images/delete.png">
			</td>
		</tr>
		<tr>
			<td>
				<b style="color: red;" id="message"></b>
			</td>
		</tr>
		<tr>
			<td class="formLabelRequired">
				<fmt:message key="pointDetails.name"></fmt:message>
			</td>
			<td class="formField"> 
				<input type="text" id="pointName" class="formField">
			</td>
		</tr>
		<tr>
			<td class="formLabelRequired"">
				<fmt:message key="dsEdit.pointDataType"></fmt:message>
			</td>
			<td>
				<select id="dataType" class="formField">
					<option value="<c:out value="<%=DataType.BINARY%>"/>">
						<fmt:message key="dsEdit.modbus.modbusDataType.binary" />
					</option>
					<option
						value="<c:out value="<%=DataType.TWO_BYTE_INT_UNSIGNED%>"/>">
						<fmt:message key="dsEdit.modbus.modbusDataType.2bUnsigned" />
					</option>
					<option
						value="<c:out value="<%=DataType.TWO_BYTE_INT_SIGNED%>"/>">
						<fmt:message key="dsEdit.modbus.modbusDataType.2bSigned" />
					</option>
					<option value="<c:out value="<%=DataType.TWO_BYTE_BCD%>"/>">
						<fmt:message key="dsEdit.modbus.modbusDataType.2bBcd" />
					</option>
					<option
						value="<c:out value="<%=DataType.FOUR_BYTE_INT_UNSIGNED%>"/>">
						<fmt:message key="dsEdit.modbus.modbusDataType.4bUnsigned" />
					</option>
					<option
						value="<c:out value="<%=DataType.FOUR_BYTE_INT_SIGNED%>"/>">
						<fmt:message key="dsEdit.modbus.modbusDataType.4bSigned" />
					</option>
					<option
						value="<c:out value="<%=DataType.FOUR_BYTE_INT_UNSIGNED_SWAPPED%>"/>">
						<fmt:message
							key="dsEdit.modbus.modbusDataType.4bUnsignedSwapped" />
					</option>
					<option
						value="<c:out value="<%=DataType.FOUR_BYTE_INT_SIGNED_SWAPPED%>"/>">
						<fmt:message key="dsEdit.modbus.modbusDataType.4bSignedSwapped" />
					</option>
					<option value="<c:out value="<%=DataType.FOUR_BYTE_FLOAT%>"/>">
						<fmt:message key="dsEdit.modbus.modbusDataType.4bFloat" />
					</option>
					<option
						value="<c:out value="<%=DataType.FOUR_BYTE_FLOAT_SWAPPED%>"/>">
						<fmt:message key="dsEdit.modbus.modbusDataType.4bFloatSwapped" />
					</option>
					<option value="<c:out value="<%=DataType.FOUR_BYTE_BCD%>"/>">
						<fmt:message key="dsEdit.modbus.modbusDataType.4bBcd" />
					</option>
					<option
						value="<c:out value="<%=DataType.EIGHT_BYTE_INT_UNSIGNED%>"/>">
						<fmt:message key="dsEdit.modbus.modbusDataType.8bUnsigned" />
					</option>
					<option
						value="<c:out value="<%=DataType.EIGHT_BYTE_INT_SIGNED%>"/>">
						<fmt:message key="dsEdit.modbus.modbusDataType.8bSigned" />
					</option>
					<option
						value="<c:out value="<%=DataType.EIGHT_BYTE_INT_UNSIGNED_SWAPPED%>"/>">
						<fmt:message
							key="dsEdit.modbus.modbusDataType.8bUnsignedSwapped" />
					</option>
					<option
						value="<c:out value="<%=DataType.EIGHT_BYTE_INT_SIGNED_SWAPPED%>"/>">
						<fmt:message key="dsEdit.modbus.modbusDataType.8bSignedSwapped" />
					</option>
					<option value="<c:out value="<%=DataType.EIGHT_BYTE_FLOAT%>"/>">
						<fmt:message key="dsEdit.modbus.modbusDataType.8bFloat" />
					</option>
					<option
						value="<c:out value="<%=DataType.EIGHT_BYTE_FLOAT_SWAPPED%>"/>">
						<fmt:message key="dsEdit.modbus.modbusDataType.8bFloatSwapped" />
					</option>
					<option value="<c:out value="<%=DataType.CHAR%>"/>">
						<fmt:message key="dsEdit.modbus.modbusDataType.char" />
					</option>
					<option value="<c:out value="<%=DataType.VARCHAR%>"/>">
						<fmt:message key="dsEdit.modbus.modbusDataType.varchar" />
					</option>
				</select>
			</td>
		</tr>
	</table>
	</div>
	</td>
	</tr>
	</table>
</tag:page>