<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp"%>
<%@page import="com.serotonin.mango.Common"%>
<%@page import="com.serotonin.modbus4j.code.DataType"%>
<%@page import="com.serotonin.modbus4j.code.RegisterRange"%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page language="java"
	import="com.serotonin.mango.vo.dataSource.modbus.ModbusPointLocatorVO"%>
<tag:page dwr="ACPTypeAttrDwr" onload="initTree">


<script type="text/javascript">
  	//tree 属性菜单对象，selectType当前选中的型号
  	var tree,selectType,selectAttr,machineRoot,systemRoot;
  	//判断是添加还是更新操作
  	var isAdd=true;
  	//记录上个闪烁图标的ID
  	var lastFlicker="";
  	var currentPoint;
  	var pointListColumnFunctions = new Array();
  	var pointListOptions;
  	
  	//开启一个新的图标闪烁并切关闭上一个打开的闪烁
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
  
  	//属性菜单单击事件
  	var TreeClickHandler = function() {
        this.handle = function(message) {
            var widget = message.source;
            if (widget.isFolder) {
                selectType = widget;
            }else{
            	selectAttr = widget;
            }
        }
    }
  	//显示某个型号下的属性列表
	function showList(typeId,typename,type){
		stopImageFader("addConfigImg");
        stopImageFader("addMachineTypeImg");
        stopImageFader("addSystemTypeImg");
        if(currentPoint){
       		stopImageFader("editMetaImg"+ currentPoint.id);
       		currentPoint.id=-1;
        }
       		
        lastFlicker="";
		if(type==0){
			$j("#typeTitle").html("<fmt:message key='acp.type.details' /> ");
		}else{
			$j("#typeTitle").html("<fmt:message key='system.type.details' /> ");
		}
		$j("#config").html("");
		$j("#attrId2").html("");
		$j("#message").html("");
		$j("#attrDiv").hide();
		if(typeId!=parseInt($j("#typeId").val())){
			$j("#typename_message").html("");
		}
		$j("#configDetails").hide();
		$j("#deleteTypeImg").show();
		$j("#listTable").show(); 
		$j("#list").show();
		$j("#typeId").val(typeId);
		$j("#type").val(type);
		ACPTypeAttrDwr.getAcpTypeBase(typeId,function(response){
			var typeInfo=response.data.type;
			$j("#typename").val(typeInfo.typename);
			$j("#warnCount").val(typeInfo.warnCount);
			$j("#alarmCount").val(typeInfo.alarmCount);
		});
		if(type==0){//系统
				$j("#acpWarnAndAlarm").show();
		}else{
			$j("#acpWarnAndAlarm").hide();
		}
		ACPTypeAttrDwr.findTypeAttrRelationByType(typeId,showListCB);
		ACPTypeAttrDwr.getACPAttrId(typeId,getacpAttrs);
		if(type==0){
			ACPTypeAttrDwr.getSystemStatistics(1,statisticsDB);
			ACPTypeAttrDwr.getPointModels(typeId,writePointList);
			//showWarnningCodeList();
		}else{
			ACPTypeAttrDwr.getSystemStatistics(0,statisticsDB);
		}
		$j("#statisticsConfiguration").show();
		//show meta
		
	}
	//显示某个型号下的属性列表 CB
	function showListCB(response){
		pointsArray.length=0;
		$j("#configList").html("");
		var list = response.data.typeattrList;
		if(list==null)return;
		for(var i =0;i<list.length;i++){
			var vo = list[i];
			var localtor = response.data.locatorList[i];
			var dataType =convertDataType(localtor.modbusDataType);//convertDataType(vo.dataType);
			var rendererType = response.data.rendererTypeList[i];
			rendererType = convertRendererType(rendererType);
			var range = convertRange(localtor.range);//convertRange(vo.range);
			if(i%2==0){
				$j("#configList").append("<tr  class='row'><td><b>"+vo.acpAttrVO.attrname+"</b></td><td>"+range+"</td><td>"+dataType+"</td><td>"+localtor.multiplier+"</td><td>"+localtor.additive+"</td><td>"+localtor.offset+"</td><td>"+rendererType+"</td><td><img id='configEditImg"+vo.id+"' class='ptr' onclick='editDetails("+vo.id+")' src='../images/icon_ds_edit.png' ></td></tr>");
				//$j("#configList").append("<tr  class='row'><td><b>"+vo.acpAttrVO.attrname+"<b></td><td>"+localtor.modbusDataType+"</td><td>"+localtor.range+"</td><td>"+localtor.multiplier+"</td><td>"+localtor.additive+"</td><td>"+localtor.pointLocator).offset+"</td><td>"+vo+"</td><td><img class='ptr' onclick='editDetails("+vo.id+")' src='../images/icon_ds_edit.png' ></td></tr>");
			}else{
				$j("#configList").append("<tr class='rowAlt'><td><b>"+vo.acpAttrVO.attrname+"</b></td><td>"+range+"</td><td>"+dataType+"</td><td>"+localtor.multiplier+"</td><td>"+localtor.additive+"</td><td>"+localtor.offset+"</td><td>"+rendererType+"</td><td><img id='configEditImg"+vo.id+"' class='ptr' onclick='editDetails("+vo.id+")' src='../images/icon_ds_edit.png' ></td></tr>");
				//$j("#configList").append("<tr class='rowAlt'><td><b>"+vo.acpAttrVO.attrname+"<b></td><td>"+localtor.modbusDataType+"</td><td>"+localtor.range+"</td><td>"+localtor.multiplier+"</td><td>"+localtor.additive+"</td><td>"+localtor.pointLocator).offset+"</td><td>"+vo+"</td><td><img class='ptr' onclick='editDetails("+vo.id+")' src='../images/icon_ds_edit.png' ></td></tr>");
			}
			
		 pointsArray[pointsArray.length] = {
            id : vo.acpAttrVO.id, 
            name : vo.acpAttrVO.attrname,
            type : dataType
        };
		}
	}
	//删除某个型号下的一行属性配置
	function deleteDetails(){
		ACPTypeAttrDwr.canDeleteAttr($j("#attrId").val(),deleteDetailsValidate);
	}
	//验证此行配置是否可以被删除
	function deleteDetailsValidate(flag){
		if(flag){
			if(confirm("<fmt:message key='acp.typeattr.confirmDelete'/>")){
				ACPTypeAttrDwr.deleteConfig($j("#id").val(),$j("#attrId").val(),deleteDetailsCB);	
			}
		}else{
			alert("<fmt:message key='acp.attr.message.cantDelete'/>");
		}
	}
	//删除某个型号下的一行属性配置CB
	function deleteDetailsCB(){
        var attrname = $j("#attrname").val();
        for(var i= 0;i < selectType.children.length;i++){
        	attr = selectType.children[i];
        	if(attr.object.attrname==attrname){
        		selectType.removeNode(attr);
        	}
        } 
		showList($j("#typeId").val());
		//stopImageFader("addConfigImg");
		$j("#configDetails").hide();
	}
	var currentFader = "";
	//为编辑某行配置显示编辑框
	function editDetails(id){ 
		$j("#bit_tr").hide();
		 clearTextRenderer();
		 $j("#configDetails").show();
		 if(lastFlicker!="addMachineTypeImg"&&lastFlicker!="addSystemTypeImg"){
		 	stopFlicker();
		 }
		 if(id==-1){
		 	lastFlicker="addConfigImg";
		 	startImageFader(lastFlicker);
		 	ACPTypeAttrDwr.newEditPoint(showDetails);
		 }else{
		 	lastFlicker="configEditImg"+id;
		 	startImageFader(lastFlicker);
		 	ACPTypeAttrDwr.findById(id,editDetailsCB);
		 }
	}
	//为添加新的数据现实编辑框
	function showDetails(){
		clearTextRenderer();
		$j("#textRendererSelect").html("");
		hideMessage();
		$j("#id").val(-1);
		$j("#attrId").val("-1");
		$j("#attrname").val("");
		$j("#dataType").val(2);
		$j("#range").val(3);
		$j("#multiplier").val("1");
		$j("#additive").val("0");
		$j("#offset").val("0"); 
		rangeTypeChange();
		ACPTypeAttrDwr.showTextRenderer(3,showTextRendererCB);
	}
	
	var temp_old_attr;//原来的属性名称，为了在更新之后，遍历树形菜单，才能找到原来属性的名称，再把它改成更新之后的
	//获取需要编辑的行数据
	function editDetailsCB(response){
		var typeattr = response.data.typeattr;
		var localtor = response.data.localtor;
		hideMessage();
		temp_old_attr = typeattr.acpAttrVO.attrname;
		$j("#id").val(typeattr.id);
		$j("#attrId").val(typeattr.acpAttrVO.id);
		$j("#attrname").val(typeattr.acpAttrVO.attrname);
		$j("#dataType").val(localtor.modbusDataType);
		if(localtor.modbusDataType==<c:out value="<%=DataType.BINARY%>"/>){
			$j("#bit").val(localtor.bit);
		}
		$j("#range").val(localtor.range);
		$j("#multiplier").val(localtor.multiplier);
		$j("#additive").val(localtor.additive);
		$j("#offset").val(localtor.offset); 
		rangeTypeChange();
		ACPTypeAttrDwr.showTextRenderer(localtor.modbusDataType,showTextRendererCB);
		
		//ACPTypeAttrDwr.getImplementation(data.dataType,showTextRenderer);
		
		//selectAttr.object.typename = $j("#attrname").val();
       	//selectAttr.titleNode.innerHTML = "<img src='images/icon_comp.png'/> "+ $j("#attrname").val();
       	//var attrNodes = selectType.childs;
       	//alert(attrNodes);
       	
	}
	//清空渲染器
	function clearTextRenderer(){
		$set("textRendererAnalogFormat", "");
        $set("textRendererAnalogSuffix", "");
		$set("textRendererBinaryZero", "");
		$set("textRendererBinaryOne", "");
		$set("textRendererPlainSuffix", "");
		$set("textRendererRangeFormat", "");
		$set("textRendererTimeFormat", "");
        $set("textRendererTimeConversionExponent", "");
        $set("textRendererRangeFrom","");
        $set("textRendererRangeTo","");
        $set("textRendererRangeText","");
        textRendererEditor.removeAllMultistateValue();
        textRendererEditor.removeAllRangeValue();
	}
	
	//现实渲染器信息
	function showTextRendererCB(response){
		clearTextRenderer();
		$j("#textRendererSelect").html("");
		var content = "";
		for(var i =0;i<response.data.definitionList.length;i++){
			var data = response.data.definitionList;
			var textRenderer = response.data.textRenderer;
			var temp = data[i].nameKey;
			var name = data[i].name;
			if(temp=="textRenderer.analog"){
				content += "<option value='"+data[i].name+"'><fmt:message key='textRenderer.analog'/></option>";
			}else if(temp=="textRenderer.binary"){
				content += "<option value='"+data[i].name+"'><fmt:message key='textRenderer.binary'/></option>";
			}else if(temp=="textRenderer.multistate"){
				content += "<option value='"+data[i].name+"'><fmt:message key='textRenderer.multistate'/></option>";
			}else if(temp=="textRenderer.none"){
				content += "<option value='"+data[i].name+"'><fmt:message key='textRenderer.none'/></option>";
			}else if(temp=="textRenderer.plain"){
				content += "<option value='"+data[i].name+"'><fmt:message key='textRenderer.plain'/></option>";
			}else if(temp=="textRenderer.range"){
				content += "<option value='"+data[i].name+"'><fmt:message key='textRenderer.range'/></option>";
			}else if(temp=="textRenderer.time"){
				content += "<option value='"+data[i].name+"'><fmt:message key='textRenderer.time'/></option>";
			} 
		}
		$j("#textRendererSelect").html(content);
		if(textRenderer==null){
        	currentTextRenderer = $("textRendererSelect").value;
			textRendererEditor.change();
			$set("textRendererAnalogFormat", "0.00");
			return;
		}
		var typename = textRenderer.typeName;
		$j("#textRendererSelect").val(typename);
        if(typename=="textRendererAnalog"){
            $set("textRendererAnalogFormat", textRenderer.format);
        	$set("textRendererAnalogSuffix", textRenderer.suffix);
        }else if(typename=="textRendererBinary"){
            $set("textRendererBinaryZero", textRenderer.zeroLabel);
            textRendererEditor.handlerBinaryZeroColour(textRenderer.zeroColour);
            $set("textRendererBinaryOne", textRenderer.oneLabel);
            textRendererEditor.handlerBinaryOneColour(textRenderer.oneColour);
        }else if(typename=="textRendererMultistate"){
            var valueList = textRenderer.multistateValues
            for(var i =0;i<valueList.length;i++){
        	  	var msValue = valueList[i];
              	textRendererEditor.addMultistateValue(msValue.key,msValue.text, msValue.colour);
            }
        }else if(typename=="textRendererNone"){
        }else if(typename=="textRendererPlain"){
         	$set("textRendererPlainSuffix", textRenderer.suffix);
        }else if(typename=="textRendererRange"){
         	$set("textRendererRangeFormat", textRenderer.format);
         	var valueList = textRenderer.rangeValues;
         	for(var i =0;i<valueList.length;i++){
         		var rgValue = valueList[i];
         		textRendererEditor.addRangeValue(rgValue.from,rgValue.to,rgValue.text,rgValue.colour);
         	}
        }else if(typename=="textRendererTime"){
            $set("textRendererTimeFormat", textRenderer.format);
            $set("textRendererTimeConversionExponent", textRenderer.conversionExponent);
        }else{
            dojo.debug("Unknown text renderer: "+textRenderer.typeName);
        }
        currentTextRenderer = typename;
		textRendererEditor.change();
		
	}

	//隐藏错误信息
	function hideMessage(){
		$j("#details_message").html("");
		$j("#attrname_message").html("");
		$j("#additive_message").html(""); 
		$j("#offset_message").html("");
	}
	//提交渲染器信息
	function commitTextRenderer(){
		textRendererEditor.save(saveDetails);
		stopImageFader("addConfigImg");
	}
	//保存一行配置
	function saveDetails(){
		hideMessage();
		var attrname = $j("#attrname").val();
	   	var multiplier = $j("#multiplier").val();
		var additive = $j("#additive").val();
		var offset = $j("#offset").val();
		var bit = $j("#bit").val();
		if(attrname.trim()==""){
			$j("#attrname_message").html("<fmt:message key='acp.typeattr.validateNull'/>");
			return;
		}else if(multiplier.trim()==""){
			 multiplier = 1;
		}else if(additive.trim()==""){
			$j("#additive_message").html("<fmt:message key='acp.typeattr.validateNull'/>");
			return;
		}else if(additive.trim()==""){
			additive = 0;
		}else if(offset.trim()==""){
			offset = 0;
		}else if(parseInt(offset)>1024){
			$j("#offset_message").html("<fmt:message key='acp.typeattr.passSafeValue'/>");
			return;
		}
		if($j("#bit_tr").attr("disabled")){
			bit = -1;
		}else{
			if(parseInt(bit)<0){
				$j("#bit_message").html("<fmt:message key='dsEdit.acp.volume.validation'/>");
				return;
			}
		}
		isAdd = true;
		var id = $j("#id").val(); 
		if(parseInt(id)!=-1){
			isAdd=false;
		}
		ACPTypeAttrDwr.edit(
			id,
			$j("#typeId").val(),
			$j("#attrId").val(),
			attrname,
			$j("#dataType").val(),
			$j("#range").val(),
			multiplier,
			additive,
			offset,
			bit,
			saveDetailsCB
		);
	}
	//保存一行配置CB
	function saveDetailsCB(vo){
		hideMessage();
    	if(isAdd){
    		var attrNode = dojo.widget.createWidget("TreeNode", {
		          title: "<img src='images/icon_comp.png'/> "+ vo.acpAttrVO.attrname,
		          object: vo.acpAttrVO
		    });
			selectType.addChild(attrNode);
			$j("#details_message").html("<fmt:message key='acp.typeattr.saveSuccess'/>");
    	}else{
			$j("#details_message").html("<fmt:message key='acp.typeattr.updateSuccess'/>");
			for(var i= 0;i < selectType.children.length;i++){
	        	attr = selectType.children[i];
	        	if(attr.object.attrname==temp_old_attr){
	        		attr.object.attrname = vo.acpAttrVO.attrname;
       				attr.titleNode.innerHTML ="<img src='images/icon_comp.png'/> "+ vo.acpAttrVO.attrname;
	        	}
      		}
    	}
    	$j("#id").val(vo.id);
    	//stopImageFader("addConfigImg");
    	ACPTypeAttrDwr.findTypeAttrRelationByType($j("#typeId").val(),showListCB); 
	}
	//初始化树形菜单
    function initTree() {
    	$j("#statisticsConfiguration").hide();
        ACPTypeAttrDwr.getTree(initTreeCB);
	    tree = dojo.widget.manager.getWidgetById('tree');
        dojo.event.topic.subscribe("tree/titleClick", new TreeClickHandler(), 'handle');
      
        var pointListColumnHeaders = new Array();
        pointListColumnFunctions.push(function(p) { return "<b>"+ p.metaName +"</b>"; });
        pointListColumnFunctions.push(function(p) { return p.dp.dataTypeMessage; });
        pointListColumnFunctions.push(function(p) {
                return writeImage("editMetaImg"+ p.id, null, "icon_comp_edit", "<fmt:message key="common.edit"/>", "editMetaPoint("+ p.id +")");
        });

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
        
    } 
    //初始化树形菜单CB
    function initTreeCB(response) {
    	var types =  response.data.types;
    	var attrsList = response.data.attrsList;
    	machineRoot = dojo.widget.createWidget("TreeNode", {
	    	title: "<img onclick='clearProfile()' src='images/folder_brick.png'/><span onclick='clearProfile()'><fmt:message key='acp.type.config'/></span><img id='addMachineTypeImg' class='ptr' border='0' onclick='addType(0)' src='images/icon_ds_add.png'/>",
	        isFolder: "true"
		});
		systemRoot = dojo.widget.createWidget("TreeNode", {
	    	title: "<img onclick='clearProfile()' src='images/folder_brick.png'/><span onclick='clearProfile()'><fmt:message key='system.type.config'/></span><img id='addSystemTypeImg' class='ptr' border='0' onclick='addType(1)' src='images/icon_ds_add.png'/>",
	        isFolder: "true"
		});
		tree.addChild(machineRoot);
        tree.addChild(systemRoot);
        for (var i=0; i<types.length; i++){
               var typeNode = dojo.widget.createWidget("TreeNode", {
	                title: "<img onclick='showList("+types[i].id+",\""+types[i].typename+"\","+types[i].type+")' src='images/folder_brick.png'/> <span onclick='showList("+types[i].id+",\""+types[i].typename+"\","+types[i].type+")' >"+ types[i].typename+"</span>",
	                isFolder: "true",
	                object: types[i]
		        });
		        if(types[i].type==0){
			        machineRoot.addChild(typeNode);
		        }else{
		        	systemRoot.addChild(typeNode);
		        }
		        var attrs = attrsList[i];
		        for(var j =0;j<attrs.length;j++){
		        	var attrNode = dojo.widget.createWidget("TreeNode", {
		                title: "<img onclick='clearProfile()' src='images/icon_comp.png'/><span onclick='clearProfile()'>"+ attrs[j].attrname+"</span>",
		                object: attrs[j]
		        	});
		        	typeNode.addChild(attrNode);
		        	typeNode.expand;
		        }
        }
        hide("loadingImg");
        show("treeDiv");
    }
    
    function clearProfile(){
    	$j("#configDetails").hide();
    	$j("#statisticsConfiguration").hide();
    	$j("#list").hide();
    	stopFlicker();
    }
    
    //显示 ‘添加型号’ 面板
    function addType(type){
    	$j("#statisticsConfiguration").hide();
    	$j("#type").val(type);
		var type = $j("#type").val();
		if(type==0){
			$j("#typeTitle").html("<fmt:message key='acp.type.details' /> ");
			startFlicker("addMachineTypeImg");
		}else{
			$j("#typeTitle").html("<fmt:message key='system.type.details' /> ");
			startFlicker("addSystemTypeImg");
		}
    	
    	$j("#configDetails").hide();
    	$j("#typename_message").html("");
    	$j("#deleteTypeImg").hide();
    	$j("#typeId").val("-1");
    	$j("#typename").val("");
    	$j("#warnCount").val("0");
    	$j("#alarmCount").val("0");
    	$j("#listTable").hide();
    	$j("#list").show();
    	if(type==0){
    		$j("#acpWarnAndAlarm").show();
    	}
    	else{
    		$j("#acpWarnAndAlarm").hide();
    		hide("metaInfo");
    	}
    }
	//保存型号
    function saveType(){
    	startImageFader("saveTypeImg");
    	$j("#typename_message").html("");
    	var typeid = parseInt($j("#typeId").val());
    	var typename = $j("#typename").val();
    	validatetype(typeid,typename);
    }
    //验证型号信息
    function validatetype(id,typename){
    	if(typename.trim().length==0){
    		stopImageFader("saveTypeImg");
    		$j("#typename_message").html("<fmt:message key='acp.type.isnull'/>");
    		return;
    	}
    	ACPTypeAttrDwr.validateType(id,typename,validateTypeCB);
    }
    //验证型号信息CB
    function validateTypeCB(count){
	    if(count==0){
	    	var typeid = parseInt($j("#typeId").val());
    		var typename = $j("#typename").val();
    		var type = $j("#type").val();
	    	isAdd=true;
	    	if(typeid>0){
	    		isAdd=false;
	    	}
	    	var warnCount = $j("#warnCount").val();
	    	var alarmCount =$j("#alarmCount").val();
	    	var reg=/^(\d{0,},){0,}\d{1,}$/;
	    	if(!(reg).test(warnCount)){
	    		alert("<fmt:message key='acp.type.code.error'/>");
	    		stopImageFader("saveTypeImg");
	    		return;
	    	}
	    	if(!(reg).test(alarmCount)){
	    		alert("<fmt:message key='acp.type.code.error'/>");
	    		stopImageFader("saveTypeImg");
	    		return;
	    	}
	    	ACPTypeAttrDwr.editType(typeid,typename,type,warnCount,alarmCount,saveTypeCB);
	    }else{
	    	stopImageFader("saveTypeImg");
	    	$j("#typename_message").html("<fmt:message key='acp.type.nameIsExist'/>");
	    }
    }
    //保存型号信息CB
    function saveTypeCB(type){
		if(isAdd){//do add the node
			var typeNode = dojo.widget.createWidget("TreeNode", {
		        title: "<img onclick='showList("+type.id+",\""+type.typename+"\","+type.type+")' src='images/folder_brick.png'/> <span onclick='showList("+type.id+",\""+type.typename+"\","+type.type+")' >"+ type.typename+"</span>",
		        isFolder: "true",
			});
			if(type.type==0){
				machineRoot.addChild(typeNode);
			}else{
				systemRoot.addChild(typeNode);
			}
			selectType=typeNode;
			$j("#typename_message").html("<fmt:message key='acp.type.saveSuccess'/>");
		}else{
			//do update the node
			selectType.object.typename = type.typename;
	        selectType.titleNode.innerHTML = "<img onclick='showList("+type.id+",\""+type.typename+"\","+type.type+")' src='images/folder_brick.png'/> <span onclick='showList("+type.id+",\""+type.typename+"\","+type.type+")' >"+ type.typename+"</span>";
	    	$j("#typename_message").html("<fmt:message key='acp.type.updateSuccess'/>");
		}
		$j("#typeId").val(type.id);
    	$j("#typename").val(type.typename);
    	$j("#warnCount").val(type.warnCount);
    	$j("#alarmCount").val(type.alarmCount);
    	stopImageFader("saveTypeImg");
    	stopImageFader("addMachineTypeImg");
    	stopImageFader("addSystemTypeImg");
    	showList(type.id,type.typename,type.type);
    }
    
    //删除型号
    function deleteType(){
    	//startImageFader("deleteTypeImg");
    	var typeId = $j("#typeId").val();
    	ACPTypeAttrDwr.canDeleteType(typeId,deleteTypeValidate); 
    }
    //删除型号验证
    function deleteTypeValidate(flag){
    	if(flag){
    		var typeId = $j("#typeId").val();
	    	if (selectType.children.length > 0) {
	            if (confirm("<fmt:message key='acp.type.confirmDeleteAll'/>")){
	            	ACPTypeAttrDwr.deleteType(typeId,deleteTypeCB);
	            }
	        }else{
	        	if (confirm("<fmt:message key='acp.type.confirmDelete'/>")){
	            	ACPTypeAttrDwr.deleteType(typeId,deleteTypeCB);
	            }
	        }
	     }else{
	     	alert("<fmt:message key='acp.type.message.cantDelete'/>");
	     	//stopImageFader("deleteTypeImg");
	     }
    }
    //删除型号CB
    function deleteTypeCB(typeId){
    	while (selectType.children.length > 0) {
            var child = selectType.children[0];
            selectType.removeNode(child);
        }
        selectType.parent.removeNode(selectType);
        $j("#list").hide();
        $j("#configDetails").hide();
        //stopImageFader("deleteTypeImg");
    }
    //转换注册范围,根据编号显示名称
    function convertRange(key){
    	var ranges = $j("#range > option");
    	for(var i =0;i<ranges.length;i++){
    		var range = ranges[i];
    		if(key==range.value){
    			return $j(range).html();
    		}
    	}
    	return "Error";
    }
    //转换数据类型，根据编号显示名称
    function convertDataType(key){
    	var dataTypes = $j("#dataType > option");
    	for(var i =0;i<dataTypes.length;i++){
    		var dt = dataTypes[i];
    		if(key==dt.value){
    			return $j(dt).html();
    		}
    	}
    	return "Error";
    }
    //转换渲染器类型，根据key，现实名称
    function convertRendererType(key){
    	if (key == "textRendererAnalog") return "<fmt:message key='textRenderer.analog'/>";
        else if (key == "textRendererBinary") return "<fmt:message key='textRenderer.binary'/>";
        else if (key == "textRendererMultistate") return "<fmt:message key='textRenderer.multistate'/>";
        else if (key == "textRendererNone") return "<fmt:message key='textRenderer.none'/>";
        else if (key == "textRendererPlain") return "<fmt:message key='textRenderer.plain'/>";
        else if (key == "textRendererRange") return "<fmt:message key='textRenderer.range'/>";
        else if (key == "textRendererTime") return "<fmt:message key='textRenderer.time'/>";
        else  return "<fmt:message key='acp.typeattr.textRendererError'/>";
    }
    //数据类型改变触发的事件：根据数据类型查找支持的渲染器类型
    function dataTypeChange(){
    	var currentDataType = $j("#dataType").val();
    	var dis = $j("#dataType").attr("disabled");
    	if(currentDataType==<c:out value="<%=DataType.BINARY%>"/>&&(dis==false||dis=="disabled")){
    		$j("#bit_tr").show();
    		$j("#bit").val(0);
    	}else{
    		$j("#bit_tr").hide();
    	}
    	ACPTypeAttrDwr.showTextRenderer(currentDataType,showTextRendererCB);
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
    	if(currentDataType==<c:out value="<%=DataType.BINARY%>"/>&&(dis==false||dis=="disabled")){
    		$j("#bit_tr").show();
    	}else{
    		$j("#bit_tr").hide();
    	}
    }
    
    //LIUJIANKUN
    var dpType=new Array();
	function getacpAttrs(response){
		 var data=response.data.list;
		 dpType=response.data.locatorList;
	     var attr=$("attrId2");
	     attr.length=0;
		 attr.options[attr.length] = new Option("<fmt:message key='common.noSelected'/>",-1);
		 for(var i=0;i<data.length;i++){
			 attr.options[attr.length] = new Option(data[i].attrname,data[i].id);
		 }
	}
	function statisticsDB(data){
		for(var i=0;i<data.length;i++){
	        var checkTr=document.createElement("tr");
		    var checkTd=document.createElement("td");
			var checkTd1=document.createElement("td");
			var checkTd2=document.createElement("td"); 
			checkTd.innerHTML="<b>"+ data[i].statisticsName+"</b>";
			checkTd.id="config"+data[i].id;
			checkTd1.innerHTML="<span id='"+data[i].dataType+"'> <fmt:message key='common.noSelected'/></span> ";
			checkTd1.id="null";
			checkTd2.innerHTML="<img  class='ptr' id='configEditImage"+data[i].id+"' src='images/icon_comp_edit.png' alt='<fmt:message key='common.edit'/>' title='<fmt:message key='common.edit'/>' onclick='editConfig("+data[i].id+",null,"+data[i].dataType+")'>";
			checkTr.appendChild(checkTd);
			checkTr.appendChild(checkTd1);
			checkTr.appendChild(checkTd2);
			if(i%2==0){
				checkTr.className="row";
			}else{
				checkTr.className="rowAlt";
			}
		    $("config").appendChild(checkTr); 
		}
		ACPTypeAttrDwr.getACPAttrStatisticsVOByACPId($j("#typeId").val(),configDB)
	}
	function configDB(response){
	    //查询的统计配置
		var result=response.data.result;
		for(var i=0;i<result.length;i++){
		//解析数据类型
		    var localtor = response.data.locatorList[i];
		    var dataType =localtor.modbusDataType;
		    var id="config"+result[i].statisticsVO.id;
		    //找到对应的统计参数,所在的td
		    var configTd=document.getElementById(id);
		    if(configTd==null){
		    	continue;
		    }
		    var tr=configTd.parentNode;
		    tr.childNodes[1].innerHTML="<span id='"+dataType+"'>"+result[i].attrVO.attrname+"</span>";
		    tr.childNodes[1].id=result[i].attrVO.id;
		    tr.childNodes[2].innerHTML="<img  class='ptr' id='configEditImage"+result[i].statisticsVO.id+"'  src='images/icon_comp_edit.png' alt='<fmt:message key='common.edit'/>' title='<fmt:message key='common.edit'/>' onclick='editConfig("+result[i].statisticsVO.id+","+result[i].attrVO.id+","+dataType+")'>";
		 }
	}
	function editConfig(statisticsId,attrId,dataType){
		startFlicker("configEditImage"+statisticsId);
		if(attrId!=null)
			attrId=$("config"+statisticsId).parentNode.childNodes[1].id;
		$("statisticsName").innerHTML=$("config"+statisticsId).innerHTML;
	    ACPTypeAttrDwr.getACPAttrId($get("typeId"),function (response){
	     var data=response.data.list;
		 dpType=response.data.locatorList;
	     var attr=$("attrId2");
	     attr.length=0;
		 attr.options[attr.length] = new Option("<fmt:message key='common.noSelected'/>",-1);
		 for(var i=0;i<data.length;i++){
			 attr.options[attr.length] = new Option(data[i].attrname,data[i].id);
			 	if(attrId==data[i].id){
			        attr.value=data[i].id;
					}
			 }
			 if(attrId!=null){
			$("oldAttrId").value=attrId; 
			}else{
		  		$("oldAttrId").value=-1;
		  		$("attrId2").options[0].selected=true;
			}
	    });
		//if(currentFader!="")stopImageFader(currentFader);
		//if(tempFader2!="")stopImageFader(tempFader2);
		//tempFader2= "configEditImage"+statisticsId;
		//startImageFader("configEditImage"+statisticsId);
		//currentFader = "configEditImage"+statisticsId;
		$("message").innerHTML="";
		$("dataType2").value=dataType;
		$("statisticsId").value=statisticsId;
		show("attrDiv");
	}
    
    function save(){
    	stopFlicker();
	    var statisticsId=$("statisticsId").value;
	    var attrId=$("attrId2").value;
	    if(attrId!=-1){
		    var selected=$("attrId2").selectedIndex;
		    var dp=dpType[selected-1].modbusDataType;
		    if($("dataType2").value!=dp){
		       $("message").innerHTML="<fmt:message key='acp.statistic.dataError'/>";
		       return false;
		    }
	    }
	    var oldAttrId=$("oldAttrId").value;
	    if(attrId==oldAttrId){
	     $("message").innerHTML="<fmt:message key='common.saveSuccess'/>";
	    	return;
	     }
	    ACPTypeAttrDwr.updataACPTypeConfig(statisticsId,attrId,oldAttrId,function(){
	        var td="config"+statisticsId;
	        $(td).parentNode.childNodes[1].childNodes[0].innerHTML=$("attrId2").options[$("attrId2").selectedIndex].text;
	        $(td).parentNode.childNodes[1].id=$("attrId2").options[$("attrId2").selectedIndex].value;
	        $("oldAttrId").value=attrId;
	        $("message").innerHTML="<fmt:message key='common.saveSuccess'/>";
	        //if(tempFader2!="")stopImageFader(tempFader2);
		});
    }
    //LIUJIANKUN
    function clearMessage(){
     $("message").innerHTML="";
    }
    
    function editMetaPoint(pointId){
      if (currentPoint)
            stopImageFader("editMetaImg"+ currentPoint.id);
        ACPTypeAttrDwr.getPointModel(pointId, editPointCB);
    	updatePointsList();
    }
    
    function editPointCB(response) { 
    	showMessage("pointMessage");
        currentPoint = response.data.meta.dp;
        var metaPoint= response.data.meta;
        currentPoint.id=metaPoint.id;
        display("pointDeleteImg", metaPoint.id != <c:out value="<%= Common.NEW_ID %>"/>);
        var locator = metaPoint.dp.pointLocator;
        
        $set("acpMetaName", metaPoint.metaName);
        var cancel;
        if (typeof editPointCBImpl == 'function') cancel = editPointCBImpl(locator);
        if (!cancel) {
            startImageFader("editMetaImg"+ currentPoint.id);
            show("acpMetaPointInfo");
        }
    }
    
    function saveMetaPoint(){
     startImageFader("pointSaveImg");
     var locator = currentPoint.pointLocator;
   	 savePointImpl(locator);
    }
    
    function saveMetaPointCB(response) {
        stopImageFader("pointSaveImg");
        if (response.hasMessages)
            showDwrMessages(response.messages);
        else {
            writePointList(response.data.metaPoint);
            //editMetaPoint(response.data.id);
            show("pointDeleteImg");
            showMessage("pointMessage", "<fmt:message key="dsEdit.pointSaved"/>");
        }
    }
    
     function writePointList(points) {
         writeContextArray();
	     if (typeof writePointListImpl == 'function') writePointListImpl(points);
	     
	     if (!points)
	         return;
	     if (currentPoint)
	         stopImageFader("editMetaImg"+ currentPoint.id);
	     dwr.util.removeAllRows("metaBody");
	     dwr.util.addRows("metaBody", points, pointListColumnFunctions, pointListOptions);
	 }
	 
	 function deleteMetaPoint(){
	 	startImageFader("pointDeleteImg");
	 	var id=currentPoint.id;
	 	ACPTypeAttrDwr.deletePointModels(id,function (){
	 		hide("acpMetaPointInfo");
	 		currentPoint.id=-1;
	 		stopImageFader("pointDeleteImg");
	 		ACPTypeAttrDwr.getPointModels(typeId,writePointList);
	 	});
	 	
	 }
</script>
<table>
	<tr>
		<td valign="top">
			<div class="borderDiv marR marB" style="float: left;">
				<table>
					<tr>
						<td>
							<span class="smallTitle"><fmt:message key="acp.attr.list" />&nbsp;&nbsp;&nbsp;</span>
						</td>
						<td>
							
						</td>
					</tr>
					<tr>
						<td>
							<tag:img png="hourglass" id="loadingImg"/>
							<div id="treeDiv" style="display: none;">
								<div dojoType="Tree" id="tree" toggle="wipe"
									DNDAcceptTypes="tree" widgetId="tree"></div>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</td>
		<td colspan="3">
			<div id="list" style="float: left; display: none">
				<div class="borderDiv marR marB">
					<table id="typeTable">
						<tr>
							<td>
								<input type="hidden" name='type' id="type">
								<span id="typeTitle" class="smallTitle"> </span>
							</td>
							<td align="right">
								<img id="saveTypeImg" alt="<fmt:message key='common.save'/>"
									title="<fmt:message key='common.save'/>" class="ptr"
									onclick="saveType()" src="../images/save.png">
								&nbsp;
								<img id="deleteTypeImg" alt="<fmt:message key='common.delete'/>"
									title="<fmt:message key='common.delete'/>" class="ptr"
									src="../images/delete.png" onclick="deleteType()">
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<input type="hidden" name='typeId' id="typeId">
							</td>
						</tr>
						<tr style="float: left;">
							<td>
								<b><fmt:message key="acp.type.typename" />:</b>
							</td>
							<td>
								<input type="text" id="typename" name="typename"/>
							</td>
						</tr>
						<tr>
							<td colspan="2" id="acpWarnAndAlarm" style="display: none;">
								<table><tr>
								<td>
									<b><fmt:message key="acp.type.warnCount" />:</b>
								</td>
								<td>
									<input type="text" id="warnCount" name="warnCount" value="0" size="50"/>
									<span id="warnCountTitle" class="ctxmsg formError"><fmt:message key="acp.type.parrent" /></span>
								</td>
								</tr>
								<tr>
								<td>
									<b><fmt:message key="acp.type.alarmCount" />:</b>
								</td>
								<td>
									<input type="text" id="alarmCount" name="alarmCount" value="0" size="50"/>
									<span id="alarmCountTitle" class="ctxmsg formError"><fmt:message key="acp.type.parrent" /></span>
								</td>
								</tr></table>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<b><span id="typename_message" style="color: red"></span> </b>
							</td>
						</tr>
					</table>
					<table id="listTable">
						<thead>
							<tr>
								<td class="horzSeparator" colspan="8">
								</td>
							</tr>
							<tr>
								<td colspan="7" class="smallTitle">
									<fmt:message key="acp.attr.list" />
								</td>
								<td align="right">
									<img id="addConfigImg" alt="<fmt:message key='common.add'/>"
										title="<fmt:message key='common.add'/>" class="ptr" border="0"
										onclick="editDetails(-1)" src="images/icon_ds_add.png">
								</td>
							</tr>
							<tr class="rowHeader">
								<td>
									<fmt:message key="acp.attr.attrname" />
								</td>
								<td>
									<fmt:message key="dsEdit.modbus.registerRange" />
								</td>
								<td>
									<fmt:message key="dsEdit.pointDataType" />
								</td>
								<td>
									<fmt:message key="dsEdit.modbus.multiplier" />
								</td>
								<td>
									<fmt:message key="dsEdit.modbus.additive" />
								</td>
								<td>
									<fmt:message key="dsEdit.modbus.offset" />
								</td>
								<td>
									<fmt:message key="pointEdit.text.props" />
								</td>
								<td></td>
							</tr>
						</thead>
						<tbody id="configList"></tbody>
					</table>
				</div>
				
				<div id="metaInfo"  class="borderDiv marR marB">
					<table>
				          <tr>
				            <td class="smallTitle"><fmt:message key="dsEdit.meta.points"/></td>
				            <td colspan="2" align="right">
				              <tag:img id="editMetaImg${applicationScope['constants.Common.NEW_ID']}" png="icon_comp_add"
				                      onclick="editMetaPoint(${applicationScope['constants.Common.NEW_ID']})" />
				            </td>
				          </tr>
				          <tr class="rowHeader">
				          	<td><fmt:message key="acp.meta.name"/></td>
				          	<td><fmt:message key="acp.meta.dataType"/></td>
				          	<td></td>
				          </tr>
				          <tbody id="metaBody">
				          </tbody>
				        </table>
				         <div id="acpMetaPointInfo" style="display: none;" class="borderDiv marR marB">
				        	<table>
				        		 <tr>
						            <td>
						              <span class="smallTitle"><fmt:message key="dsEdit.points.details"/></span>
						            </td>
						            <td></td>
						            <td align="right">
						              <tag:img id="pointSaveImg" png="save" onclick="saveMetaPoint()" title="common.save"/>
						              <tag:img id="pointDeleteImg" png="delete" onclick="deleteMetaPoint()" title="common.delete" />
						            </td>
						        </tr>
						        <tr>
						        	<td colspan="3">
						        		<table>
						        			<tbody id="pointMessage" class="formError"></tbody>
						        		</table>
						        	</td>
						        </tr>
				        		<tr>
				        			 <td class="formLabelRequired"><fmt:message key="dsEdit.acpmeta.name"/></td>
				        			<td class="formField"><input id="acpMetaName" type="text"/></td>
				        		</tr>
				        		   <jsp:include page="dataSourceEdit/editAcpMeta.jsp"/>
				        	</table>
				       </div>
				</div>
				<br>
				<div id="statisticsConfiguration">
					<div class="borderDiv marR marB" style="float: left;">
						<table>
							<thead>
								<tr>
									<td colspan="3">
										<span class="smallTitle"><fmt:message
												key="acp.typeattr.statisticConfig" /> </span>
									</td>
								</tr>
								<tr class="rowHeader">
									<td>
										<fmt:message key="acp.statistic.statisticName" />
									</td>
									<td>
										<fmt:message key="acp.attr.attrname" />
									</td>
									<td></td>
								</tr>
							</thead>
							<tbody id="config">
							</tbody>
						</table>
					</div>
					<div class="borderDiv marR marB"
						style="float: left; display: none;" id="attrDiv">
						<table>
							<tr>
								<td class="smallTitle">
									<fmt:message key="acp.attr.attrname" />
								</td>
								<td>
									<img align="right" alt="<fmt:message key='common.save'/>"
										title="<fmt:message key='common.save'/>" class="ptr"
										id="addImg" src="images/save.png" onclick="save()">
								</td>
							</tr>
							<tr>
								<td>
									<b id="message" style="color: red;"></b>
								</td>
							</tr>
							<tr>
								<td>
									<input type="hidden" id="statisticsId" value="-1">
									<input type="hidden" id="dataType2" value="-1">
								</td>
							</tr>
							<tr class="rowHeader">
								<td>
									<fmt:message key="acp.statistic.statisticName" />
								</td>
								<td>
									<fmt:message key="acp.attr.attrname" />
								</td>
							</tr>
							<tr>
								<td>
									<b id="statisticsName"></b>
								</td>

								<td>
									<input type="hidden" id="oldAttrId" value="-1">
									<select id="attrId2" onchange="clearMessage()">
										<option value="-1">
											<fmt:message key="common.noSelected" />
										</option>
									</select>
								</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
			</td>
			<td valign="top">
			<div style="float: left; display: none" id="configDetails"
				class="borderDiv marR marB">
				<table id="defailsTable">
					<tr>
						<td class="smallTitle">
							<fmt:message key="acp.attr.configuration" />
						</td>
						<td align="right">
							<img id="saveDetailsImg" alt="<fmt:message key='common.save'/>"
								title="<fmt:message key='common.save'/>" class="ptr"
								onclick="commitTextRenderer()" src="../images/save.png">
							&nbsp;
							<img id="deleteDetailsImg"
								alt="<fmt:message key='common.delete'/>"
								title="<fmt:message key='common.delete'/>" class="ptr"
								src="../images/delete.png" onclick="deleteDetails()">
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<b><span style="color: red" id="details_message"></span> </b>
						</td>
					</tr>
					<tr>
						<td>
							<b><fmt:message key="acp.attr.attrname" /> </b>
						</td>
						<td>
							<input type="text" id="attrname" name="attrname" />
							<input type="hidden" id="attrId" name="attrId" />
							<input type="hidden" id="id" name="id" />
						</td>
					</tr>
					<tr>
						<td></td>
						<td>
							<b><span style="color: red" id="attrname_message"></span> </b>
						</td>
					</tr>
					<tr>
						<td>
							<b><fmt:message key="dsEdit.modbus.registerRange" /> </b>
						</td>
						<td>
							<select id="range" onchange="rangeTypeChange()">
								<option value="<c:out value="<%=RegisterRange.COIL_STATUS%>"/>">
									<fmt:message key="dsEdit.modbus.coilStatus" />
								</option>
								<option value="<c:out value="<%=RegisterRange.INPUT_STATUS%>"/>">
									<fmt:message key="dsEdit.modbus.inputStatus" />
								</option>
								<option
									value="<c:out value="<%=RegisterRange.HOLDING_REGISTER%>"/>">
									<fmt:message key="dsEdit.modbus.holdingRegister" />
								</option>
								<option
									value="<c:out value="<%=RegisterRange.INPUT_REGISTER%>"/>">
									<fmt:message key="dsEdit.modbus.inputRegister" />
								</option>
								<option
									value="<c:out value="<%=RegisterRange.HOLDING_REGISTER_88%>"/>">
									<fmt:message key="dsEdit.modbus.holdingRegister_88" />
								</option>
							</select>
						</td>
					</tr>
					<tr>
						<td>
							<b><fmt:message key="dsEdit.pointDataType" /> </b>
						</td>
						<td>
							<select id="dataType" disabled="disabled"
								onchange="dataTypeChange()">
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
					<tr>
						<td>
							<fmt:message key="dsEdit.modbus.multiplier" />
						</td>
						<td>
							<input type="text" id="multiplier" name="multiplier">
						</td>
					</tr>
					<tr>
						<td></td>
						<td>
							<b><span style="color: red" id="multiplier_message"></span> </b>
						</td>
					</tr>
					<tr>
						<td>
							<fmt:message key="dsEdit.modbus.additive" />
						</td>
						<td>
							<input type="text" id="additive" name="additive">
						</td>
					</tr>
					<tr>
						<td></td>
						<td>
							<b><span style="color: red" id="additive_message"></span> </b>
						</td>
					</tr>
					<tr>
						<td>
							<b><fmt:message key="dsEdit.modbus.offset" /> </b>
						</td>
						<td>
							<input type="text" id="offset" name="offset">
						</td>
					</tr>
					<tr>
						<td></td>
						<td>
							<b><span style="color: red" id="offset_message"></span> </b>
						</td>
					</tr>
					<tr id="bit_tr">
						<td>
							<b><fmt:message key="dsEdit.modbus.bit" /> </b>
						</td>
						<td>
							<input type="text" value="0" id="bit" name="bit">
						</td>
					</tr>
					<tr>
						<td></td>
						<td>
							<b><span style="color: red" id="bit_message"></span> </b>
						</td>
					</tr>
					<tr>
						<td class="horzSeparator" colspan="2"> 
						</td>
					</tr>
					<tr>
						<%@ include file="/WEB-INF/jsp/pointEdit/textRenderer2.jsp"%>
					</tr>
				</table>
			</div>
</td>
</tr>
</table>
</tag:page>