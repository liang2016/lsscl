<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%@ include file="/WEB-INF/jsp/include/center.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<script src="http://maps.google.com/maps?file=api&amp;v=2&amp;sensor=true&amp;key=ABQIAAAAtKwCpUwD4KauHZtm_GbAYBTat9lnGr2G5sZZp_uzG6-X6xmkshS8dBRgcgWu3mJffPC5wrn6TPMDaQ" type="text/javascript"></script> 
<script type='text/javascript' src='/dwr/interface/FactoryDwr.js'></script>
<tag:centerHeader dwr="ZoneDwr" >
	<html>
		<head>
			<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
			<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
			<style type="text/css">
					   #openInfo{
					   text-align: left;
					   font-size: 12px;
					   }
						#map_canvas {
							height: 700px;
							width: 900px;
							float: right;
						}
						span {
							size: 12px;
						}
						
						#zoneStyle{
						text-align: left;
						font-size: 12px;
						}
						#commentStyle{
					    text-align: left;
						font-size: 12px;
						}
					   .tableInfo tr td{
						 text-align: right;
						 font-size: 12px;
						}
                        select {
	                         width: 100px;
                         }
                       #myTable{
						     border-collapse: collapse;/* 边框合并属性  */
							 width:600px;
						}
						#myTable tr{
						     border: 1px solid #FF9933;
						}
					    #myTable tr td{
						     border: 1px solid #FF9933;
						}
						
						.factoryAdd{
						     border:1px solid #FF9933;
							 width:400px;
						 }
						 

			</style>
	<title>lsscl</title>
	<script type='text/javascript' src='/dwr/engine.js'></script>
	<script type='text/javascript' src='/dwr/util.js'></script>
 <!-- lsscl.com api密钥
 <script type="text/javascript"
     src="http://maps.google.com/maps?file=api&amp;v=2&amp;sensor=true_or_false&amp;key=ABQIAAAARPClpEwFGojpl6KDNPFE9BQ_IGHK68UEJgu7oc75Cl14esgqoBQJUDL6aSRrptbhOlWNsZN-08kgPA" ></script>
-->
	
	
	
	<script type="text/javascript">
		
			var map = null;
			var geocoder = null;


	   function initialize() {     
	       if (GBrowserIsCompatible()) {       
	            map= new GMap2(document.getElementById("map_canvas"));        
	           	map.setCenter(new GLatLng(32.4419, 112.1419), 4);
				map.addControl(new GLargeMapControl());
				map.addControl(new GScaleControl());
				map.addControl(new GMapTypeControl());
				map.addControl(new GOverviewMapControl());
				geocoder = new google.maps.ClientGeocoder();
				getAllMarkers();
				}
				else{
			     alert('你使用的浏览器不支持 Google Map!');    
				}
				   
	   }
		//这里假设了 每个地点type对应的图标都是以是以types数组中同下标的图片	
		var types = [ 'info', 'warning', 'exigence', 'dying' ];//设置图标类型
		var paths = [ 'green', 'red', 'blue', 'green', 'yellow' ];//设置图标颜色
	function createMarkers() {
		   $("zones").length=0;
	      	getZones();
	     	getSunZones();
	 }
   
   //加载所有
   function getAllMarkers(){
    map.setCenter(new GLatLng(32.4419, 112.1419), 4);
    //区域
     getZones();
     //子区域
     getSunZones();
     //获得所有工厂
     getFactory();
   }
   
	//获取区域
	function getZones() {
		ZoneDwr.getZones(getAllZones);
	}
	
	//根据区域编号获得区域
	function seachZones(zone){
 	var markname=zone.options[zone.selectedIndex].text;
	    for(var i=0;i<zoneMarkers.length;i++){
	      if(markname==zoneMarkers[i][0]){
	        zoneMarkers[i][1].show();
	       continue;
	      }
	      zoneMarkers[i][1].hide();
	    }
	//    var selectValue=zone.value;
	   // getSelectSunZones(selectValue);
     }
	//根据子区域编号获得工厂,并隐藏其他子区域
	function seachSunZones(subZone){
	   //获得当前下啦列表选择的子区域
	   var markname=subZone.options[subZone.selectedIndex].text;
	      for(var i=0;i<sZoneMarkers.length;i++){
	      if(markname==sZoneMarkers[i][0]){
	        sZoneMarkers[i][1].show();
	       continue;
	      }
	      sZoneMarkers[i][1].hide();
	    }
	    //显示上级区域标识
	     var zone=$("zones");
	     var selecZone=zone.options[zone.selectedIndex].text;
	     for(var i=0;i<zoneMarkers.length;i++){
	      if(selecZone==zoneMarkers[i][0]){
	        zoneMarkers[i][1].show();
	      }
	    }
	 }
	
	//加载区域
	function getAllZones(data) {
	  clreaSelect("zones","zoneError");
	  $("f_zones").length=0;
	 if(data.length<1){
		   $("zoneError").innerHTML="<fmt:message key="login.error.loadZoon"></fmt:message>";
		   return false;
		}
		var ico = new GIcon(G_DEFAULT_ICON);
		for ( var i = 0; i < data.length; i++) {
		//添加下了列表option
		    addOptions(data[i],"zones");
		    var f_zone=$("f_zones");
		    f_zone.options[f_zone.length] = new Option(data[i].scopename,data[i].id);
		    
			var zone = data[i];
			//create marker
			ico.image = "images/google_ico/pyramid-southam_blue.png";
            createMarker("<fmt:message key="login.zone"></fmt:message>",zone,ico);
		}
	}
	//添加下拉列表
	function addOptions(object,type){
	   var selectObj=$(type);
	   selectObj.options[selectObj.length] = new Option(object.scopename,[object.id,object.lon,object.lat]);
	}
	//获得子区域根据区域编号
	function getSelectSunZones(select){
	  //保留上级区域
        seachZones(select);
      //这里加载选择的区域
      
      var selectInfo=select.value;
	 if(selectInfo==""||selectInfo=="<fmt:message key="login.selsct"></fmt:message>"){
	    return false;
	  }
	  clreaSelect("sZones","subZoneError");
      $("factoryName").length=0;
	  var id=selectInfo.split(",")[0];
	  ZoneDwr.getSubZonesByZId(id,subZones);
	}
	//获得所有子区域
   function getSunZones(){
     clreaSelect("sZones","subZoneError");
	  ZoneDwr.getAllSubZones(subZones);
	}
	
	function subZones(data){
		 if(data.length<1){
		   $("subZoneError").innerHTML="<fmt:message key="login.error.loadSubZoon"></fmt:message>";
		   return false;
		}
	   var ico = new GIcon(G_DEFAULT_ICON);
	   for(var i=0;i<data.length;i++){
	     var object=data[i];
	       addOptions(object,"sZones");
	     	//create marker
			ico.image = "images/google_ico/pyramid-egypt.png";
	      createMarker("<fmt:message key="login.subZone"></fmt:message>",object,ico);
	 }
	}
	//获得根据子区域获得工厂
	function getSelectFactory(select){
	  seachSunZones(select);
	  var selectInfo=select.value;
      clreaSelect("factoryName","factoryError");
	  var id=selectInfo.split(",")[0];
	  FactoryDwr.getFactoriesBySZId(id,factory);
	}
	//清空下拉列表和错误内容
	function clreaSelect(select,erroeLable){
	  $(select).length=0;
	  $(erroeLable).innerHTML="";
	}
    //获得所有工厂
	function getFactory(){
	  clreaSelect("factoryName","factoryError");
	  FactoryDwr.getFactories(factory);
	}
	
	//获得指定工厂
	function getFactoryBuId(factoryId){
	  clreaSelect("factoryName","factoryError");
	  FactoryDwr.getFactoriesById(factoryId,factory);
	}
	
	function factory(data){
	 if(data.length<1){
		   $("factoryError").innerHTML="<fmt:message key="login.error.loadfactory"></fmt:message>";
		   return false;
	 	}
	    var ico = new GIcon(G_DEFAULT_ICON);
	    for(var i=0;i<data.length;i++){
	     ico.image = "images/google_ico/factory-blue.PNG";
	     var object=data[i];
	     addOptions(object,"factoryName");
	     createMarker("<fmt:message key="login.factory"></fmt:message>",object,ico);
	 }
	}
    var zoneMarkers=new Array();
	var sZoneMarkers=new Array();
	var factoryMarkers=new Array();
	//添加标记
	function createMarker(type,object,ico){
		//create marker
	       var markerOptions = {
				icon : ico
			};
			var point = new GLatLng(object.lon,object.lat); //获取坐标  	
		    var marker = new GMarker(point, markerOptions); //创建标记
		    var promptContent ="<b id='zoneStyle'>"+type+":</b>"+ object.scopename+ "</br>"+"<b id='commentStyle'><fmt:message key="login.factory.comment"></fmt:message>:</b>"+object.description;
		    addListener(marker, point, promptContent,object.id);
		    promptContent = "";
		    if(type="<fmt:message key="login.zone"></fmt:message>"){
		    zoneMarkers.push([object.scopename,marker]);
		    }
		     if(type="<fmt:message key="login.subZone"></fmt:message>"){
		    sZoneMarkers.push([object.scopename,marker]);
		    }
		     if(type="<fmt:message key="login.factory"></fmt:message>"){
		     factoryMarkers.push([object.scopename,marker]);
		    }
		    map.addOverlay(marker); //将标记添加进地图 
	}
	
	function reset() {
		removeAllMarkers();
		getAllMarkers();
	}

	//事件绑定
	function addListener(pointMarker, latlng, content, id) {

//		FactoryMapDwr.getDataSourcesByFactoryId(id, function(data) {
	//		if (data.length < 1) {
		//		DSInfo = "<span>暂无信息</span></br>";
			//}
			//for ( var n = 0; n < data.length; n++) {
				//DSInfo = "<span>" + DSInfo + data[n].name + "</span></br>";
			//}
		//});
		//绑定事件
		GEvent.addListener(pointMarker, "click", function() {
		content="<div id='openInfo'>"+content+"</div>"
			parent.map.openInfoWindow(latlng, content);
			DSInfo = "";
		});
	}
	//调节提示内容格式
	function getMessage(content) {
		return content + "-_-";
	}
	//清除所有标记
	function removeAllMarkers() {
		map.clearOverlays();
	}
	//获取坐标
	function updateAddress() {
		//removeAllMarkers();
	   var factoryInfo=$("factoryName").value.split(",");
	    if(factoryInfo[0]==""||factoryInfo[0]=="<fmt:message key="login.selsct"></fmt:message>"){
	   //   var text=$("factoryName").options[$("factoryName").selectedIndex].text;
	      var  info= $("sZones").value.split(",");
	      //  text=$("sZones").options[$("sZones").selectedIndex].text;
			if(info[0]==""||info[0]=="<fmt:message key="login.selsct"></fmt:message>"){
			    var  info= $("zones").value.split(",");
			   // text=$("zones").options[$("zones").selectedIndex].text;
			    if(info[0]==""||info[0]=="<fmt:message key="login.selsct"></fmt:message>"){
			     return false;
			     }
			    else{
			    update(info,5);
			    }
			    
			}else{
				update(info,8);
			 }
	    }
	    else{
	     update(factoryInfo,12);
	    }
		
	}
	
	function update(info,size){
	    var address=info[1]+","+info[2];
				geocoder.getLatLng(address, function(location) {
					map.setCenter(location, size);
					document.getElementById("x_point").value = location.y;
					document.getElementById("y_point").value = location.x;
				});
	}
	//检测工厂状态
	setInterval (function (){
	 changeMarker();
	},1000);
	
	
	function changeMarker(){
	   for(var i=0;i<factoryMarkers.length;i++){
	    if(factoryMarkers[i][0]=="北京"){
	    //变化图标
          factoryMarkers[i][1].setImage("images/google_ico/factory_exception.gif");
	    }
	  }
	}
	
	 function deletefactory(factoryId){
	 var  gnl=confirm("<fmt:message key="factoryList.factory.delete.deletealarm"></fmt:message>");
	 if(gnl==true){
	 FactoryDwr.deleteFactoryById(factoryId,function callBack(data){
	   if(data==factoryId){
            document.all("myTable").deleteRow($("factory"+factoryId).rowIndex); 
            //window.location='lssclMap.shtm';
	    }
	    else{
	    alert("<fmt:message key="FactoryList.factory.error.deleteFactory"></fmt:message>");
	    }
	  });
	  }
	  else{
	   return false;
	  }
	 }
	 
	    function getaddressInfo() {
	    var address=$('f_name').value;
      if (geocoder) {
        geocoder.getLatLng(
          address,
          function(point) {
            if (!point) {
            $("factoryErrow").innerHTML="不能解析: " + address;
            } else {
            map.setCenter(point, 13);
		    $("f_lon").value=point.y;
		    $("f_lat").value=point.x;
		    var marker = new google.maps.Marker(point,{ draggable : true });
		   	GEvent.addListener(marker, "dragend", function() {
			  $("f_lon").value=marker.getPoint().y;
		      $("f_lat").value=marker.getPoint().x;
		  });
		    map.addOverlay(marker);
              marker.openInfoWindow(document.createTextNode(address));
            }
          }
        );
      }
    }

	 function clearFactoryError(){
	  $("factoryErrow").innerHTML="";
	  $("f_lon").value="";
	  $("f_lat").value="";
	 }
	 function initAddFactory(){
	   $("addFactory").style.display="block";
	   var id=$("f_zones").options[$("f_zones").selectedIndex].value;
	    ZoneDwr.getSubZonesByZId(id,add_option);
	  	 }
	 function getf_sub_zone(select){
	      var id=select.value;
	     ZoneDwr.getSubZonesByZId(id,add_option);
	 }
	 function add_option(data){
	   $("f_sZones").length=0;
	   $("f_sZones").options[0] = new Option("<fmt:message key="FactoryList.factory.subZone.null"></fmt:message>",-1);
	   for(var i=0;i<data.length;i++){
	   $("f_sZones").options[$("f_sZones").length] = new Option(data[i].scopename,data[i].id);
	  }
	 }
	 
	
</script>
		</head>
		<body bgcolor="#FFFFFF" onload="initialize()" onunload="GUnload()">
		<div id="map_canvas"  style="float: left;margin: 30px; ">
		</div>
         <div id="selectTable" style="float: left;margin:30px; " class="tableInfo">
					<table>
					<tr>
						<td>
						<fmt:message key="login.zone"></fmt:message>
						</td>
						<td>
							<select id="zones" onchange="getSelectSunZones(this)">

							</select>
						</td>
						<td id="zoneError" style="font-size: 12px; color: red;"></td>
					</tr>
						<tr>
						<td>
							<fmt:message key="login.subZone"></fmt:message>
						</td>
						<td>
							<select id="sZones" onchange="getSelectFactory(this)">
					
							</select>
						</td>
						<td id="subZoneError" style="font-size: 12px; color: red;"></td>
					</tr>
						<tr>
							<td>
								<fmt:message key="login.factory.name"></fmt:message>
							</td>
							<td>
								<select id="factoryName">
						
								</select>
							</td>
							
							<td id="factoryError" style="font-size: 12px; color: red;"></td>
						</tr>
						<tr><td></td>
							<td>
							<input type="button" onclick="reset()"value="<fmt:message key="login.factory.selectReset"></fmt:message>">&nbsp;&nbsp;&nbsp;&nbsp;
				           <input type="button" value="<fmt:message key="login.search"></fmt:message>" onclick="updateAddress()"></td>
						    <td></td>
						</tr>
						<tr>
							<td>
								<fmt:message key="login.factory.LON"></fmt:message>
							</td>
							<td>
								<input type="text" name="x_point" id="x_point">
							</td>
						</tr>
						<tr>
							<td>
								<fmt:message key="login.factory.LAT"></fmt:message>
							</td>
							<td>
								<input type="text" onclick="changeMarker()" name="y_point" id="y_point">
							</td>
						</tr>
						</table>
			</div>
            <div style="float: left;" class="tableInfo">
            <table id="myTable">
                <tr>
                   <td><b><fmt:message key="login.factory.name"></fmt:message></b></td>
                   <td><b><fmt:message key="login.zone"></fmt:message></b></td>
                   <td><b><fmt:message key="login.subZone"></fmt:message></b></td>
                   <td><b><fmt:message key="FactoryList.factory.industry"></fmt:message></b></td>
                   <td><b><fmt:message key="FactoryList.factory.principal"></fmt:message></b></td>
                   <td><b><fmt:message key="FactoryList.factory.alarmsSum"></fmt:message></b></td>
                   <td><b><fmt:message key="FactoryList.factory.alarms.3days"></fmt:message></b></td>
                   <td><b><fmt:message key="FactoryList.factory.alarms.7days"></fmt:message></b></td>
                   <td><b><img alt="<fmt:message key="FactoryList.factory.add"></fmt:message>" src="images/google_ico/factory_add.png" onclick="initAddFactory()"></b></td>
                   </tr>
                   <c:forEach var="factory" items="${factoryList}">
                   <tr id="factory${factory.id}">
	                   <td title="${factory.name}">
		                   <c:if test="${fn:length(factory.name)>5}">
		                    ${fn:substring(factory.name,0,5)}...
		                   </c:if>
		                    <c:if test="${fn:length(factory.name)<=5}">
		                    ${factory.name}
		                   </c:if>
	                   </td>
	                   <td title="${factory.zone} ">
	                        <c:if test="${fn:length(factory.zone)>5}">
		                    ${fn:substring(factory.zone,0,5)}...
		                   </c:if>
		                    <c:if test="${fn:length(factory.zone)<=5}">
		                     ${factory.zone} 
		                   </c:if>
	                     
	                   </td>
	                   <td title="${factory.subZone}">
	                   <c:if test="${factory.subZone==null}">
                      <fmt:message key="FactoryList.factory.subZone.null"></fmt:message>
	                   </c:if>
	                       <c:if test="${fn:length(factory.subZone)>5}">
		                    ${fn:substring(factory.subZone,0,5)}...
		                   </c:if>
		                    <c:if test="${fn:length(factory.subZone)<=5}">
		                     ${factory.subZone} 
		                   </c:if>
	                   </td>
	                   <td>未知</td>
	                   <td>admin</td>
	                   <td>${eventCount}</td>
	                   <td>2</td>
	                   <td>1</td>
	                   <td><img alt="<fmt:message key="FactoryList.factory.login"></fmt:message>" src="images/google_ico/factory_go_in.png" onclick="window.location='watch_list.shtm'">  
	                   <img alt="<fmt:message key="FactoryList.factory.disable"></fmt:message>" src="images/google_ico/factory_disable.png" onclick="javascript:alert('暂未开启此功能')">
	                   <img alt="<fmt:message key="FactoryList.factory.delete"></fmt:message>" src="images/google_ico/factory_delete.png" onclick="deletefactory(${factory.id})">
	                   </td>
                   </tr>
                   </c:forEach>
              </table>
            </div>
            <script type="text/javascript">
            //保存工厂
             function save_factory(){
               var factoryObject=new Object;
				 factoryObject.name=$("f_name").value;
				 factoryObject.lon=$("f_lon").value;
				 factoryObject.lat=$("f_lat").value;
				 factoryObject.zoneId=parseInt($("f_zones").options[$("f_zones").selectedIndex].value);
				 factoryObject.SZId=parseInt($("f_sZones").options[$("f_sZones").selectedIndex].value);
				 factoryObject.comment=$("f_comment").value;
				if(factoryObject.name.trim()==""){
				  $("factoryErrow").innerHTML="<fmt:message key="FactoryList.factory.error.nameNull"></fmt:message>";
				}
				 if(factoryObject.zoneId==-1){
				   $("fZonesError").innerHTML="<fmt:message key="FactoryList.factory.error.zoneNull"></fmt:message>";
				    return false;
				 }
				 if(factoryObject.comment.trim()==""){
				   $("fCommentError").innerHTML="<fmt:message key="FactoryList.factory.error.commentNull"></fmt:message>";
                   return false;				   
				 }
				 FactoryDwr.savefactory(factoryObject,function callback(data){
                    if(data==1){
	                   $("addMessage").innerHTML="<fmt:message key="FactoryList.factory.save.succeed"></fmt:message>"
	                   window.location='lssclMap.shtm';
                    }
                    else{
                    $("addMessage").innerHTML="<fmt:message key="FactoryList.factory.save.failure"></fmt:message>"
                    }
				 });
				}
            //添加一行信息
            
            </script>
            <div  class="tableInfo" style="float: left; margin: 10px; display: none;" id="addFactory" >
            <table class="factoryAdd">
            <tr>
            <td><b><fmt:message key="FactoryList.factory.add"></fmt:message></b>&nbsp;&nbsp; <label style="color: red;" id="addMessage"></label> </td>
            <td><img alt="<fmt:message key="FactoryList.factory.add"></fmt:message>" src="images/save_add.png" onclick="save_factory()">  </td>
            </tr>
            <tr>
              <td><fmt:message key="login.factory.name"></fmt:message> </td><td><input type="text" id="f_name" onfocus="clearFactoryError()" onblur="getaddressInfo()"></td>
            </tr>
             <tr>
            <td colspan="2">
             <label id="factoryErrow" style="color: red;" ></label>
            </td>
            </tr>
            <tr>
              <td><fmt:message key="login.factory.LON"></fmt:message></td><td><input disabled="disabled" type="text" id="f_lon"> </td>
            </tr>
             <tr>
              <td><fmt:message key="login.factory.LAT"></fmt:message></td><td><input disabled="disabled" type="text" id="f_lat"> </td>
            </tr>
             <tr>
              <td><fmt:message key="login.zone"></fmt:message></td><td>
              	<select id="f_zones" onchange="getf_sub_zone(this)">
                 <option value="-1"><fmt:message key="FactoryList.factory.error.zoneNull"></fmt:message> </option>
				</select>
               </td>
            </tr>
             <tr>
	            <td colspan="2">
	             <label id="fZonesError" style="color: red;"></label>
	            </td>
            </tr>
             <tr>
              <td><fmt:message key="login.subZone"></fmt:message></td><td>
              <select id="f_sZones">
					<option value="-1"> <fmt:message key="FactoryList.factory.subZone.null"></fmt:message></option>
			 </select>
			 </td>
            </tr>
             <tr>
              <td><fmt:message key="login.factory.comment"></fmt:message></td><td><textarea id="f_comment" rows="5" cols="20"></textarea>     </td>
            </tr>
                <tr>
	            <td colspan="2">
	             <label id="fCommentError" style="color: red;"></label>
	            </td>
            </tr>
            </table>
            </div>
		</body>
	</html>
</tag:centerHeader>
