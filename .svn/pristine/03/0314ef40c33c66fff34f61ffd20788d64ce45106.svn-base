<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp"%>
<%@page import="com.serotonin.mango.Common"%>
<%@page import="com.serotonin.mango.DataTypes"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<tag:page dwr="TextRenderDwr">
<script type="text/javascript">
	window.onload =initData;
	function initData(){
		TextRenderDwr.findAll(allDB);
	}  
	
	function allDB(renders){
		clearList();
		for(var i=0;i<renders.length;i++){
		    var checkTr=document.createElement("tr");
		    var checkTd=document.createElement("td");
		    var checkTd1=document.createElement("td");
		    var checkTd2=document.createElement("td");
		    checkTd.innerHTML="<b>"+renders[i].name+"</b>";
		    checkTd1.innerHTML=convertDataType(renders[i].dataType);  //common.edit
		    checkTd2.innerHTML="<img id='edit"+renders[i].id+"' class='ptr'  src='images/icon_comp_edit.png' alt='<fmt:message key="common.edit"></fmt:message>' title='<fmt:message key="common.edit"></fmt:message>' onclick='editDetails("+renders[i].id+")'>";
		    checkTr.appendChild(checkTd);
		    checkTr.appendChild(checkTd1);
		    checkTr.appendChild(checkTd2);
		     if(i%2==0)
		       checkTr.className="row";
		    else
		       checkTr.className="rowAlt";
		    $("settings").appendChild(checkTr);
		 }
	}
	//编辑
	function editDetails(id){
		setUserMessage("");
		clearTextRenderer();
		startFlicker("edit"+id);
		show("deleteTypeImg");
		show("editTable");
		$set("id",id);
		TextRenderDwr.findById(id,editDetailsCB);
	}
	//获取需要编辑的行数据
	function editDetailsCB(response){
		$set("name",response.name);
		$set("dataType",response.dataType);
		TextRenderDwr.showTextRenderer(response.dataType,showTextRendererCB);
	}
	function clearList(){
	 var oTbody=$("settings");
		var arTr=oTbody.getElementsByTagName("tr");
		for(var i=arTr.length-1;i>=0;i--){
		  oTbody.removeChild(arTr[i]);
		  }
	}
	
    function dataTypeChange(){
	   	var currentDataType = $get("dataType");
	   	TextRenderDwr.newEditPoint(showDetails);
	   	TextRenderDwr.showTextRenderer(currentDataType,showTextRendererCB);
    }
  
	//保存
	function saveDetails(){
	 var id=$get("id");
	 var dataType=$get("dataType");
	 var name=$get("name");
	 TextRenderDwr.edit(id,dataType,name,saveDetailsCB);
	}
	
		//保存一行配置CB
	function saveDetailsCB(response){
		startImageFader("saveTypeImg");
	    if (response.hasMessages)
            showDwrMessages(response.messages, "genericMessages");
       else{     
		$set("id",response.data.renderVo.id);
		$set("name",response.data.renderVo.name);
		setUserMessage("<fmt:message key="common.saveSuccess"/>");
		stopImageFader("saveTypeImg");
		stopFlicker();
		initData();
		}
	}
	 function setUserMessage(message) {
        if (message)
            $set("userMessage", message);
        else {
            $set("userMessage","");
            hideGenericMessages("genericMessages");
        }
    }
    
    //开启一个新的图标闪烁并切关闭上一个打开的闪烁
	var lastFlicker="";
	function startFlicker(id){
  		if(lastFlicker!=""){
  			stopImageFader(lastFlicker);
  		}
 		lastFlicker = id;
		startImageFader(lastFlicker);
  	}
  	  	//关闭上一个图标的闪烁
  	function stopFlicker(){
  		if(lastFlicker!=""){
  			stopImageFader(lastFlicker);
  		}
  	}
	
		//为添加新的数据现实编辑框
	function showDetails(){
		setUserMessage("");
		clearTextRenderer();
		rangeTypeChange();
		$j("#textRendererSelect").html("");
	//	hideMessage();
		var currentDataType = $get("dataType");
		TextRenderDwr.showTextRenderer(currentDataType,showTextRendererCB);
	}
	
	function add(id){
		startFlicker("initAdd");
		show("editTable");
		hide("deleteTypeImg");
		$set("id",id);
		$set("name","");
		 if(id==-1){
		 	TextRenderDwr.newEditPoint(showDetails);
		 }	
		 else{
		 
		 }
	}
	    //当注册范围变化时触发事件：当选择{卷的状态,输入状态}时，数据类型不可选择&& 数据类型默认为二进制
    function rangeTypeChange(){
    	var rangeValue = $j("#range").val();
    	if(rangeValue=="1"||rangeValue=="2"){
    		$j("#dataType").attr("disabled","disabled");
    		$j("#dataType").val("1");
    		dataTypeChange();
    	}else{
    		$j("#dataType").attr("disabled",false);
    	}
    	var currentDataType = $j("#dataType").val();
    	var dis = $j("#dataType").attr("disabled");
    	if(currentDataType==<c:out value="<%=DataTypes.BINARY%>"/>&&(dis==false||dis=="disabled")){
    		$j("#bit_tr").show();
    	}else{
    		$j("#bit_tr").hide();
    	}
    }
    
		//提交渲染器信息
	function commitTextRenderer(){
		textRendererEditor.save(saveDetails);
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
    
    function deleteType(){
    	startImageFader("deleteTypeImg");
    	var id=$get("id");
    	TextRenderDwr.deleteRender(id,deleteDB);
    }
    function deleteDB(response){
     if (response.hasMessages)
     	showDwrMessages(response.messages, "genericMessages");
     else{
     	stopImageFader("deleteTypeImg");
		hide("editTable");
		lastFlicker='';
		initData();
     }
    }
</script>

<table>
	<tr> 
		<td valign="top">
			 <table class="borderDiv marR marB">
				<tr>
					<td>
						 <span class="smallTitle"><fmt:message key="textRender.setting"></fmt:message></span>
						 <img id="initAdd" src="images/icon_ds_add.png" alt="<fmt:message key="common.add"></fmt:message>" title="<fmt:message key="common.add"></fmt:message>" class="ptr"
						onclick="add(-1)" border="0">
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
				<tbody id="settings">
				
				</tbody>
			</table>
			</td>
			<td valign="top">
			<table id="editTable" class="borderDiv marR marB" style="display: none;">	
				<tr>
					 <td colspan="2" align="right">
					 	<img id="saveTypeImg" alt="<fmt:message key='common.save'/>"
									title="<fmt:message key='common.save'/>" class="ptr"
									onclick="commitTextRenderer()" src="../images/save.png">
									<input type="hidden" id="id" value="-1">
						&nbsp;
						<img id="deleteTypeImg" alt="<fmt:message key='common.delete'/>"
									title="<fmt:message key='common.delete'/>" class="ptr"
									src="../images/delete.png" onclick="deleteType()">			
					 </td>
				</tr>
				<tr>
              		<td colspan="2" id="userMessage" class="formError"></td>
            	</tr>
				<tr>
					<td  class="formLabelRequired"><fmt:message key="pointDetails.name"></fmt:message></td>
					<td class="formField"><input type="text" id="name" /></td>
				</tr>
				<tr>
					<td  class="formLabelRequired">
						<fmt:message key="dsEdit.pointDataType"></fmt:message>
					</td>
					<td class="formField">
						 <select id="dataType" 
											onchange="dataTypeChange()">
							<option value="<c:out value="<%=DataTypes.UNKNOWN%>"/>">
								<fmt:message key="common.unknown" />
							</option>
							<option
								value="<c:out value="<%=DataTypes.BINARY%>"/>">
								<fmt:message key="common.dataTypes.binary" />
							</option>
							<option
								value="<c:out value="<%=DataTypes.MULTISTATE%>"/>">
								<fmt:message key="common.dataTypes.multistate" />
							</option>
							<option value="<c:out value="<%=DataTypes.NUMERIC%>"/>">
								<fmt:message key="common.dataTypes.numeric" />
							</option>
							<option
								value="<c:out value="<%=DataTypes.ALPHANUMERIC%>"/>">
								<fmt:message key="common.dataTypes.alphanumeric" />
							</option>
							<option
								value="<c:out value="<%=DataTypes.IMAGE%>"/>">
								<fmt:message key="common.dataTypes.image" />
							</option>
						</select>
					 </td>
				</tr>
				<tr>
					 <td colspan="2">
						<%@ include file="/WEB-INF/jsp/pointEdit/textRenderer3.jsp" %>
					</td>
				</tr>
				<tr>
					<td><table><tbody id="genericMessages"></tbody></table></td>
				</tr>
				
			</table>
		</td>
	</tr>
</table>
</tag:page>