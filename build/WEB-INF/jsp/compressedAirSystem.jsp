<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
--%>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp"%>
<%@page import="com.serotonin.mango.Common"%>
<tag:page dwr="CASDWR" onload="init">
	<style>
.contextMenu {
	display: none;
}

.member {
	border: 2px solid #FFFFFF; padding-bottom : 3px;
	padding-left: 3px;
	padding-right: 3px;
	padding-top: 3px;
	padding-bottom: 3px;
	background-color: #F0F0F0;
}

select {
	width: 80px;
}
</style>
	<script type="text/javascript" src="../js/jquery.simple.tree.js"></script>

	<script>
      var $j = jQuery.noConflict();//以$j来替代jQuery中的$,同时区分mootools
      	function init(){
      	$j("img").addClass("ptr");
	     initTree();
	    var factoryACP=new Object();

	}
		//记录工厂的所有空压机,回调函数
	 function callData(data){
				factoryACP=data;  
		}
    </script>
	<script>
   var tree;
    function initTree() {
        
        //ACPTypeAttrDwr.getTree(initTreeCB);
        CASDWR.initTree($get("scopeId"),initTreeCB);
	    tree = dojo.widget.manager.getWidgetById('tree');
        dojo.event.topic.subscribe("tree/titleClick", new TreeClickHandler(), 'handle');
        //setErrorMessage();
    } 
   
   function initTreeCB(data){
     var ACPSystem=data.casList;
     var acpList=data.acpList;
     var dpList=data.dpList;
     var cdpList=data.compressorDpList;
     for(var i=0;i<ACPSystem.length;i++){
     var typeNode = dojo.widget.createWidget("TreeNode", {
	                title: "<img onclick='initCompressedSystem("+ACPSystem[i].id+")' src='images/folder_brick.png'/> <span onclick='initCompressedSystem("+ACPSystem[i].id+")' >"+ ACPSystem[i].systemname+"</span>",
	                isFolder: "true",
	                object: ACPSystem[i]
		        });
		        tree.addChild(typeNode);//添加系统
		        
		        for(var j=0;j<acpList.length;j++){
		        //如果空压机所属的系统编号=系统编号,添加到tree上
		        if(acpList[j].compressorId==ACPSystem[i].id){
		           var attrNode1 = dojo.widget.createWidget("TreeNode", {
		                title: "<img src='images/folder_brick.png' onclick='initCompressor("+acpList[j].id+")'/><span onclick='initCompressor("+acpList[j].id+")' > "+ acpList[j].acpname+"</span>",
		                isFolder: "true",
		                object: acpList[j]
		        	});
		        	typeNode.addChild(attrNode1); 
		        	for(var k=0;k<cdpList.length;k++){
		        	 if(cdpList[k].parentId==acpList[j].id){
		        	   var attrNode = dojo.widget.createWidget("TreeNode", {
		                title: "<img src='images/icon_comp.png' onclick='hideDiv()'/> "+ "<span onclick='hideDiv()'>"+ cdpList[k].name+"</span>",
		                object: cdpList[k]
		        	});
		        	attrNode1.addChild(attrNode);
		        	}
		        	}
		          }
		        }
	            for(var j=0;j<dpList.length;j++){
	               //如果这个点在系统下面,添加 
		           if(dpList[j].parentId==ACPSystem[i].id){
		             var attrNode = dojo.widget.createWidget("TreeNode", {
		                title: "<img src='images/icon_comp.png' onclick='hideDiv()'/> "+ "<span onclick='hideDiv()'>"+dpList[j].name+"</span>",
		                object: dpList[j]
		        	});
		        	typeNode.addChild(attrNode);
		          
		           }
		           }
     }
   hide("loadingImg");
   }
 var TreeClickHandler = function() {
        this.handle = function(message) {
        	//setErrorMessage();
            var widget = message.source;
            if (widget.isFolder) {
                selectedFolderNode = widget;
            }
            else
                hide("folderEditDiv");
        }
        show("treeDiv");
    }

	</script>
	<script type="text/javascript">
//初始化下拉列表
function addOption(){
 CASDWR.getACPsByFactoryId($get("scopeId"),function(data){
		var select=document.getElementById("acpSelect");
      	select.length=0;
   		for(var i=0;i<data.length;i++){
	   		select[select.length] = new Option(data[i].acpname,data[i].id);
      //  options+="<option values='"+data[i].id+"'>"+data[i].acpname+"</option>";
       }
 	});
}

function hideDiv(){
		 $j("#acp").hide();
		 $j("#system").hide();
}
//将下拉列表的选项添加到div
function addOpToDiv(){
	var obj = document.getElementById("acpSelect"); 
	var index = obj.selectedIndex; // 选中索引
	if(index==-1)
		return;
	var text = obj.options[index].text; // 选中文本
	var value = obj.options[index].value; // 选中值 
	obj.remove(index);
    var newDiv="<div id='acpDiv"+value+"'  class='member'><span class='ptr'  onclick='initCompressor("+value+")'>"+text+"<img class='ptr' id='"+value+"' title='"+text+"' src='images/delete.png' width='12' height='12' align='right' onclick='deleteDiv(this)'></span></div>";
    $j("#acpMember").html($j("#acpMember").html()+newDiv);
    initCompressor(value);
}

//删除系统中的空压机成员
function deleteDiv(div){
    div.parentNode.parentNode.parentNode.removeChild(div.parentNode.parentNode);
    var select=document.getElementById("acpSelect");
    select[select.length] = new Option(div.title,div.id);
}
</script>

	<script type="text/javascript">
		//根据压缩空气系统编号获得系统
		function initCompressedSystem(id){
		 	stopImageFader("addImg", true);
		 	$j("#deleteImg").show();
		 	$j("#systemId").val(id);
			$j("#acp").hide();
			hide("acpSystemMessageDwrError");
			hide("acpMemberInfo");
			show("systemMemberInfo");
			$j("#system").show();
			var scopeId=$j("#scopeId").val();
		 	CASDWR.getACPsByFactoryId(scopeId,callData);
		 	CASDWR.getACPSystemAttrById(scopeId,id,commpressor);
		}
		function commpressor(data){
			$j("#acpSystemMemberMessage").html("");
			$j("#acpSystemMessage").html("");
			//添加默认配置
			var systemConfig=data.sc;
		    addAcpSystem(systemConfig);
			
		    addOption();
		    var memberDiv=""
		    $j("#acpMember").html("");
		     //循环系统中的空压机
		    for(var i=0;i<data.acpList.length;i++){
		    // alert(data.acpList[i].acpname);
		     //给div中添加空压机名称
		     memberDiv+="<div id='acpDiv"+data.acpList[i].id+"' class='member'><span class='ptr' onclick='initCompressor("+data.acpList[i].id+")'>"+data.acpList[i].acpname+"<img id='"+data.acpList[i].id+"' title='"+data.acpList[i].acpname+"' src='images/delete.png' width='12' height='12' align='right'  onclick='deleteDiv(this)'></sapn></div>"
		   	//删除select中与div重复的选项
		     var selectOp=document.getElementById("acpSelect");
		     for(var j=0;j<selectOp.options.length;j++){   
			 if(selectOp.options[j].text==data.acpList[i].acpname)
                selectOp.remove(j);   
			 }   
		    }
		    //空压机系统中的数据点
		    var points=data.dpList;
		    var statistics=data.statistics;
		    var dpList=data.ps;
		     //打印统计参数和数据点
		    $j("#point").html("");
		    $j("#Statistics").html("");
		    $j("#point").html(addSelect(statistics));

		     // 初始化数据点与统计匹配
		    var spans=$j("#point").find("span");
		    var selects=$j("#point").find("select");
		    //这里参数0表示操作系统
		    initSystemData(0,dpList,spans,selects);
		   
		   // writePoint(points);
           // addOption();
           //给下拉列表填充数据
            
            var otherPoint=data.otherPoint;
            var selectArr=$j("#point").find("select");
            addotherPoint(otherPoint,selectArr);
       
          	$j("#systemname").val(data.ACPS.systemname);
		    $j("#sXid").val(data.ACPS.xid);
		    $j("#acpMember").html(memberDiv);
		  }
		  //根据空压机编号获得数据点
		  function initCompressor(id){
		     stopImageFader("saveAcpImg", true);
		     startImageFader("addMemberImg", true); 
		     hide("acpSystemMessageDwrError");
		  	 //记录工厂的所有空压机
		     $j("#acpMessage").html("");
		     $j("#deleteImg").show();
		  	 $j("#acp").show();
		  	 if($j("#system").css("display")=="none")
		  	 	$j("#deleteAcpImg").hide();
		  	 else
		  	 	$j("#deleteAcpImg").show();
		   	 CASDWR.getACPById($get("scopeId"),id,acpData);
		  }
		  function acpData(data){
		    $j("#acpId").val(data.acp.id);
		    $j("#acpName").val(data.acp.acpname);
		    $j("#acpXid").val(data.acp.xid);
		    
		   //空压机系统中的数据点
		    var statistics=data.statistics;
		     //打印统计参数和数据点
		    $j("#acppoint").html("");
		    // $j("#Statistics").html("");
		    $j("#acppoint").html(addSelect(statistics));
		     //填充空压机统计配置
		    var dpList=data.ps;
		    var spans=$j("#acppoint").find("span");
		    var selects=$j("#acppoint").find("select");
		    //这里是1表示操作空压机
		    initSystemData(1,dpList,spans,selects);
		    //填充空压机成员配置
		    var dpList2=data.ps2;
		    initSystemData(0,dpList2,spans,selects);
		  //填充下拉列表
		    var otherPoint=data.otherPoint;
            var selectArr=$j("#acppoint").find("select");
            addotherPoint(otherPoint,selectArr);
            show("acpMemberInfo");
            stopImageFader("addMemberImg", true); 
		  }
		  //将数据点显示到对应的td
		  function writePoint(points){
		  	 $j("#point").html("");
		  	 var pointMember="";
		  	 for(var i=0;i<points.length;i++){
		     pointMember+="<div>"+points[i].name+"</div>";
		   }
		  	 $j("#point").html(pointMember);
		  }
		  //添加数据点对应
		  function addSelect(s){
		     var select="<table>";
		     for(var i=0;i<s.length;i++){
	     	     select+="<tr><td> <span id='"+s[i].id+"'>"+s[i].statisticsName+":</span> </td><td><SELECT style='width: auto;' id='"+s[i].id+"'></SELECT></td></tr>";
		     }
		    // var select="<span id='1'>系统压力</span> <SELECT id='1'><option value='1'>数据点1</option> </SELECT><br><span id='2'>系统温度</span> <SELECT id='3'><option value='2'>数据点2</option></SELECT><br><span id='4'>系统电流</span> <SELECT id='3'><option value='4'>数据点4</option></SELECT><br><span id='5'>系统温度</span> <SELECT id='5'><option value='5'>数据点5</option></SELECT><br><span id='6'>系统压力</span> <SELECT id='6'><option value='6'>数据点6</option></SELECT><br><span id='7'>系统温度</span> <SELECT id='7'><option value='7'>数据点7</option><option value='8'>数据点8</option></SELECT>";
	          return select+"</table>";
	        
		  }
		  //初始化压缩空气系统的数据点
		  function initSystemData(type,pointStatistics,spans,selects){
		      for(var i=0;i<spans.length;i++){
		     	 for(var j=0;j<pointStatistics.length;j++){
		      		if(spans[i].id==pointStatistics[j].statisticsVO.id){
				        selects[i].options[selects[i].length]=new Option(pointStatistics[j].dataPointVO.name,pointStatistics[j].dataPointVO.id);   
                		if(type==1){
                			selects[i].disabled=true;
                		}
                		if(type==2){
                		    selects[i].length=0;
                		    selects[i].options[selects[i].length]=new Option(pointStatistics[j].dataPointVO.name,pointStatistics[j].dataPointVO.id);
                			//selects[i].value=pointStatistics[j].dataPointVO.id;
                		}	     
		       		}
		      	 }
		      } 
		  }
		  //填充下拉列表
		  function addotherPoint(otherPoint,selectArr){

               for(var i=0;i<selectArr.length;i++){
               	selectArr[i].options[selectArr[i].length]=new Option("<fmt:message key='common.noSelected'/>",-1);
                for(var j=0;j<otherPoint.length;j++){
                //重复的选项不再添加
                var selectNow=selectArr[i];
                var repeat=false;
                for(var k=0;k<selectNow.length;k++){
                  if(selectNow.options[k].value==otherPoint[j].id){
                   repeat=true;
                  // selectNow.remove(k);
                   }
                  }
                  if(repeat){
                  continue;
                  }
                    selectArr[i].options[selectArr[i].length]=new Option(otherPoint[j].name,otherPoint[j].id);  
                  }
             }
               stopImageFader("initAddImg", true);
         }
         function saveSystemBase(){
         	var id=$j("#systemId").val(); 
         	var acpSystem=new Object();
			acpSystem.xid=$j("#sXid").val();
			acpSystem.systemname=$j("#systemname").val();
			acpSystem.factoryId=$j("#scopeId").val();
			if(acpSystem.xid.trim()=="" || acpSystem.systemname.trim()==""){
		     	 $j("#acpSystemMessage").html("<fmt:message key="acp.name.null"/>");
		     	 	return false;
		      }
		    CASDWR.saveSystemBase(id,acpSystem,function(response){
			    if (response.hasMessages){
			   		 showDwrMessages(response.messages,$("acpSystemMessageDwrError"));
			    }
			    else{
			     	$j("#acpSystemMessage").html("<fmt:message key="acp.saved"/>");
					$j("#systemId").val(response.data.acpSystemVo.id);
					$j("#acpSystemMessageDwrError").html("");
					show("systemMemberInfo");
					reload();
			    }
		    });
         }
		  //添加
		  function save(){
			  startImageFader("addImg", true);
			  var spans=$j("#point").find("span");
			  var selects=$j("#point").find("select");
			  var statistics=new Array();
			  var points=new Array();
			  var compressors=new Array();
			  for(var i=0;i<spans.length;i++){
			  	 if(selects[i].value==""||selects[i].value==-1){
			  	  	continue;
			  	 }
			  	 points.push(selects[i].value);
			  	 statistics.push(spans[i].id);
			  } 
			  var acps=$j(".member").find("img");
			     for(var i=0;i<acps.length;i++){
			       compressors[i]=acps[i].id;
			   } 
			  var id=$j("#systemId").val(); 
		      if(!checkPoint(points)){
		     	 $j("#acpSystemMessage").html("<fmt:message key="acp.point.same"/>");
		     		 return false;
		      }
			  CASDWR.save(id,statistics,points,compressors,function (response){
			   if (response.hasMessages){
			    	$j("#acpSystemMemberMessage").html("");
			    	showDwrMessages(response.messages,$("acpSystemMessageDwrError"));
			   }else{
				   $j("#acpSystemMemberMessage").html("<fmt:message key="acp.saved"/>");
				   $j("#acpSystemMessageDwrError").html("");
				   reload();
			  	}
			  	 stopImageFader("addImg", true);
			  });
		  }
		  //执行保存空压机操作
		  function saveAcp(){
		  	startImageFader("saveAcpImg", true);
		  	var spans=$j("#acppoint").find("span");
		  	var selects=$j("#acppoint").find("select");
		  	var points=new Array();
		  	var update=new Array();
		  	var add=new Array();
		  	var compressors=new Array();
		  	for(var i=0;i<spans.length;i++){
			   	//points[i]=selects[i].value;
			   		if(selects[i].disabled==true){
			   //禁用的只执行更新操作
			   			update.push([spans[i].id,selects[i].value]);
			   			points.push(selects[i].value);
				   }else{
				   //执行添加操作
					  	 if(selects[i].value==""||selects[i].value==-1){
					  	  	continue;
					  	 }
					  	 points.push(selects[i].value);
					   	 add.push([spans[i].id,selects[i].value]);
				   }
		  } 

		  var id=$j("#acpId").val(); 
		  var acp=new Object();
		  acp.xid=$j("#acpXid").val();
		  acp.acpname=$j("#acpName").val();
		  
		  
		  if(!checkPoint(points)){
		    $j("#acpMessage").html("<fmt:message key="acp.add.same.dataPoint"/>");
	       return false;
	      }
		   CASDWR.updateACPMember(id,update,add,function(){
		   $j("#acpMessage").html("<fmt:message key="acp.saved"/>");
		  	 stopImageFader("saveAcpImg", true);
		  	 $j("#acpSystemMessageDwrError").html("");
		  	 reload();
		   });
		  
		  }
		  
		  //初始化添加压缩空气系统
		  function initAdd(){
		   	startImageFader("initAddImg", true);
		   	hide("acpMemberInfo");
		   	hide("deleteImg");
	       	hide("systemMemberInfo");
		   	show("system");
		   	addOption();
		   	CASDWR.initAdd($get("scopeId"),initAddData);
		   	$j("#acpSystemMessage").html("");
		  }
		  function initAddData(data){
		   	$j("#systemname").val("");
		    $j("#sXid").val(data.xid);
		    $j("#acpMember").html("");
		    $j("#systemId").val("-1");
		     //空压机系统中的数据点
		    var statistics=data.statistics;
		    var otherPoint=data.otherPoint;
		    var systemConfig=data.sc;
		    addAcpSystem(systemConfig);
		     //打印统计参数和数据点
		    // addSelect(statistics);
		    $j("#point").html("");
		    $j("#point").html(addSelect(statistics));
	        var selectArr=$j("#point").find("select");
            addotherPoint(otherPoint,selectArr);
		  }
		 function addAcpSystem(acps){
		  if(acps==-1)
		  	return;
		   var select=$("scOption");
		   select.length=1;
		   for(var i=0;i<acps.length;i++){
		    	select.options[select.length] = new Option(acps[i].acpname,acps[i].id);
		   }
		 } 
		 
		 function getDefaultconfig(acpId){
		 	CASDWR.getSystemDefaultConfig(acpId,function(dpList){
			 	 var spans=$j("#point").find("span");
			     var selects=$j("#point").find("select");
			    //这里参数2表示操作系统默认点
			    initSystemData(2,dpList,spans,selects);
		 	});
		 }
		 
		 
		  //删除压缩空气系统
		  function deleteSystem(){
		    startImageFader("deleteImg", true);
			var id=$j("#systemId").val();
		    CASDWR.deleteCompressedSystem(id,function (){
			    $j("#acpSystemMessage").html("<fmt:message key="acp.deleted"/>");
			    stopImageFader("deleteImg", true);
			    $j("#system").hide();
			    reload();
		    });
		  }
		 //重新加载tree 
		 function reload(){
       		while (tree.children.length > 0)
        	tree.removeNode(tree.children[0]);
        	initTree();
        }
		 
		 function checkPoint(points){
		  	for(var i=0;i<points.length;i++){
		  		for(var j=0;j<points.length;j++){
		  			if(points[i]==points[j] && !(i==j)&&(points[i]!="") ){
		  			
		    		return false;
             		}
		    	}
		   }
		  return true;
		 } 
		 function deleteAcpMember(){
		 startImageFader("deleteAcpImg", true);
		  var acpId=$j("#acpId").val();
		  CASDWR.clearAcpMember(acpId,function(data){
		    $j("#acp").hide();
		     var div=document.getElementById(acpId);
		     if(div!=null){
			     div.parentNode.parentNode.removeChild(div.parentNode);
			   // deleteDiv(div);
			    var acpSelect=document.getElementById("acpSelect");
			    for(var j=0;j<acpSelect.options.length;j++){   
					if(acpSelect.options[j].value==acpId)
		               acpSelect.remove(j);   
				 } 
			}
			stopImageFader("deleteAcpImg", true);
		  });
		 }
</script>
	<table>
		<tr>
			<td valign="top">
				<table width="100%" class="borderDivPadded">
					<tr>
						<td>
							<span class="smallTitle"><fmt:message key="acp.setting"/></span>
						</td>
							<td>
							<img align="right" alt="<fmt:message key="common.add"/>" id="initAddImg" src="images/add.png"
								onclick="initAdd()">
								<input type="hidden" id="scopeId" value="${sessionUser.currentScope.id}">
						  </td>
						</tr>
						<tr>
						<td colspan="2">
							<tag:img png="hourglass" id="loadingImg"/>
							<div id="treeDiv" style="display: none;">
							<div dojoType="Tree" id="tree"  DNDAcceptTypes="tree" widgetId="tree">
							</div>
						</td>
						</tr>
				</table>
			</td>
			<td valign="top">
				<div id="system" style="display: none;">
				<table>
					<tr>
						<td colspan="2">
							<table class="borderDivPadded">
								<tr>
									<td>
										<input type="hidden" id="systemId">
									</td>
								</tr>
								<tr>
									<td class="smallTitle">
										<fmt:message key="acp.property"/><tag:help id="acpSystemAttr"/>
									</td>
									<td align="right">
										<img alt="<fmt:message key="common.add"/>" id="addImg" src="images/save.png" onclick="saveSystemBase()">
										<img alt="<fmt:message key="common.delete"/>" id="deleteImg" src="images/delete.png"
											onclick="deleteSystem()">
									</td>
								</tr>
								<tr>
										<td colspan="2">
											<b id="acpSystemMessage" style="color: red;" /></b>
										</td>
								</tr>
								<tr>
									<td class="formLabelRequired">
									<fmt:message key="acp.system.name"/>
									</td>
									<td class="formField">
										<Input type="text" id="systemname">
									</td>
								</tr>
								<tr>
									<td class="formLabelRequired">
										 <fmt:message key="common.xid"/>
									</td>
									<td class="formField">
										<Input type="text" id="sXid">
									</td>
		
									</tr>
									<tr>
									<td colspan="2">
									</td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td>
							<table id="systemMemberInfo" class="borderDivPadded">
							<thead>
								<tr>
									<td class="smallTitle">
										<fmt:message key="acp.system.member"/><tag:help id="acpSystemMember"/>
									</td>
									<td align="right">
										<img alt="<fmt:message key="common.add"/>" id="addMemberImg" src="images/save.png" onclick="save()">
									</td>
								</tr>
								<tr>
									<td colspan="2">
										<span id="acpSystemMemberMessage" class="formError" /></span>
									</td>
								</tr>
								<tr>
									<td class="formLabelRequired"><fmt:message key="acp.system.points"/></td>
									<td colspan="2" align="center">
										<select id="scOption" onchange="getDefaultconfig(this.value)">
										<option value="-1"></option>
										</select>							     
									</td>
								</tr>
								<tr>
									<td colspan="2" class="horzSeparator">
									</td>
								</tr>
								<tr>
									<td class="formLabelRequired" id="statistics">
									 <fmt:message key="acp.member.dataPoint"></fmt:message>
									</td>
									<td class="formField">
										<div id="point"
											style="border: 1px solid #FF9933; padding: 15px; margin: 5, 5, 5, 5;">
										</div>
									</td>
		
								</tr>
								<tr>
									<td colspan="2">
									</td>
								</tr>
								<tr>
									<td colspan="2" class="horzSeparator">
									</td>
								</tr>
								<tr>
									<td class="formLabelRequired">
										<fmt:message key="air.compressior"></fmt:message>
									</td>
									<td class="formField">
										<SELECT id="acpSelect">
										</SELECT>
										<img id="addAcpImg" alt="<fmt:message key="common.add"/>" src="images/add.png"
											onclick="addOpToDiv()">
									</td>
								</tr>
								<tr>
									<td></td>
									<td></td>
								</tr>
								<tr>
									<td></td>
									<td align="center">
										<div id="acpMember">
										</div>
									</td>
								</tr>
							</thead>
								<tbody id="acpSystemMessageDwrError" class="formError" style="display:none;">
								</tbody>
							</table>
						</td>
						<td valign="top">
							<table id="acpMemberInfo"  class="borderDivPadded" style="display: none;">
								<tr>
									<td id="compressorId">
									</td>
								</tr>
								<tr>
									<td class="smallTitle">
										<fmt:message key="air.compressior.property"></fmt:message>
									</td>
									<td align="right">
										<img alt="<fmt:message key="common.add"/>" id="saveAcpImg" src="images/save.png"
											onclick="saveAcp()">
										<img alt="<fmt:message key="common.delete"/>" id="deleteAcpImg" src="images/delete.png"
											onclick="deleteAcpMember()">
									</td>
								</tr>
								<tr>
									<td>
										<b id="acpMessage" style="color: red;" />
									</td>
								</tr>
								<tr>
									<td class="formField">
										<input id="acpId" type="hidden">
									</td>
		
								</tr>
								<tr>
									<td class="formLabelRequired">
										<fmt:message key="air.compressior.name"/>
									</td>
									<td class="formField">
										<input type="text" id="acpName" disabled="disabled">
									</td>
								</tr>
								<tr>
									<td class="formLabelRequired">
										  <fmt:message key="common.xid"/>
									</td>
									<td class="formField">
										<input type="text" id="acpXid" disabled="disabled">
									</td>
								</tr>
								<tr>
									<td colspan="2">
									</td>
								</tr>
								<tr>
									<td colspan="2" class="horzSeparator">
									</td>
								</tr>
								<tr>
									<td class="formLabelRequired">
										 <fmt:message key="acp.member.dataPoint"/>
									</td>
									<td class="formField">
										<div id="acppoint" class="member"
											style="border: 1px solid #FF9933; padding: 15px; margin: 5, 5, 5, 5;">
										</div>
									</td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
				</div>
			</td>
		</tr>
	</table>
</tag:page>
