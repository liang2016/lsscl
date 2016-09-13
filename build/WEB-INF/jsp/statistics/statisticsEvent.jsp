<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>
<%@page import="com.serotonin.mango.vo.report.ReportVO"%>
<%@page import="com.serotonin.mango.Common"%>
<tag:page onload="setFocus" dwr="StatisticsEventDwr">
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>  
   <script type='text/javascript' src='/dwr/interface/ZoneDwr.js'></script>
	<script type='text/javascript' src='/dwr/interface/FactoryDwr.js'></script>
<c:if test='${mango:envBoolean("ssl.on", false)}'>
  <c:if test='${pageContext.request.scheme == "http"}'>
    <c:redirect url='https://${pageContext.request.serverName}:${mango:envString("ssl.port", "8443")}${requestScope["javax.servlet.forward.request_uri"]}'/>
  </c:if>
</c:if>
<style type="text/css">
			   #openInfo{
			   text-align: left;
			   font-size: 12px;
			   }
			  #map_canvas {
				float: right;
				}
			  #selectTable tr td{
				font-size: 12px;
				}
		</style>
	<script type="text/javascript">
		var firstData=null;
		var map = null;
		var geocoder;
		var googleUnAble=false;
	   function initialize() {
	   	if(typeof(google)!='undefined'){  
			geocoder = new google.maps.Geocoder();
		}
		else{
			alert("<fmt:message key="google.failed"></fmt:message>");
			return;
		}	
	   try{  
	      /** if (GBrowserIsCompatible()) {*/
	     	 var myLatlng = new google.maps.LatLng(32.4419,112.1419);  
                var myOptions = {  
                    zoom: 4,  
                    center:myLatlng,  
                    mapTypeId: google.maps.MapTypeId.ROADMAP  
                };  
                 map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);  
				google.maps.event.addListener(map, "dblclick", function() {
				var zoomSize=map.getZoom();
					if(zoomSize==8){
						getSunZones();
					}
				   if(zoomSize==12){
				   		getFactory();
				   }
				});
					//geocoder = new google.maps.ClientGeocoder();
					// map.setCenter(new GLatLng(32.4419, 112.1419), 4);
					 if(firstData!=null){
						for( var i = 0; i < firstData.length; i++) {
							var zone = firstData[i];
							//create marker = new GIcon(G_DEFAULT_ICON);
							//ico.image
							var ico  = "images/google_ico/pyramid-southam_blue.png";
							var shadIco = "images/google_ico/shadow-pyramid-southam_blue.png";
				            createMarker("<fmt:message key="login.zone"></fmt:message>",zone,ico,shadIco);
						}
					}
				/**}
			  	else{
			     alert('你使用的浏览器不支持 Google Map!');    
				}*/
				
			}
			catch(e){
				 googleUnAble=true;
			}
				getAllMarkers();  
	   }
		//这里假设了 每个地点type对应的图标都是以是以types数组中同下标的图片	
		var types = [ 'info', 'warning', 'exigence', 'dying' ];//设置图标类型
		var paths = [ 'green', 'red', 'blue', 'green', 'yellow' ];//设置图标颜色
	function createMarkers() {
		   $("zones").length=0;
	      	getZones();
	     	//getSunZones();
	 }
   
   //加载所有
   function getAllMarkers(){
    //区域
     getZones();
     //子区域
    // getSunZones();
     //获得所有工厂
     //getFactory();
     
   }
   
	//获取区域
	function getZones() {
		   var scopeType=${sessionUser.currentScope.scopetype};
		   if(scopeType==0){//总部
		   		StatisticsEventDwr.getZones(getAllZones);
	      	}
	      	else if(scopeType==1){//区域
	      		hide("zoneTr");
	      		getSunZones();
	      	}
	      	else if(scopeType==2){//子区域
	      		hide("zoneTr");
	      		hide("subZoneTr");
	      		getFactory();
	      	}
	      	else{
	      	hide("zoneTr");
	      	hide("subZoneTr");
	      	hide("factoryTr");
	      	$("factoryError").innerHTML="error";
	      	}
	}
	
	//根据区域编号获得区域
	function seachZones(zone){
	var markname=zone.options[zone.selectedIndex].text;
	    for(var i=0;i<zoneMarkers.length;i++){
	      if(markname==zoneMarkers[i][0]){
	      zoneMarkers[i][1].setMap(map);
	        //zoneMarkers[i][1].show();
	       continue;
	      }
	       zoneMarkers[i][1].setMap(null);
	      //zoneMarkers[i][1].hide();
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
	      	sZoneMarkers[i][1].setMap(map);
	       // sZoneMarkers[i][1].show();
	       continue;
	      }
	      sZoneMarkers[i][1].setMap(null);
	      //sZoneMarkers[i][1].hide();
	    }
	    /*
	    //显示上级区域标识
	    if(${sessionUser.homeScope.id==sessionUser.currentScope.id}){
	     var zone=$("zones");
	     var selecZone=zone.options[zone.selectedIndex].text;
	    for(var i=0;i<zoneMarkers.length;i++){
	      if(selecZone==zoneMarkers[i][0]){
	          zoneMarkers[i][1].setMap(map);
	      }
	     }
	    }
	    */
	 }
	
	//加载区域
	function getAllZones(data) {
	 firstData=data;
	 clreaSelect("zones","zoneError");
	 clreaSelect("factoryName","factoryError");
	 if(data.length<1){
		   $("zoneError").innerHTML="<fmt:message key="login.error.loadZoon"></fmt:message>";
		   return false;
		}
      $("zones").options[$("zones").length] = new Option("<fmt:message key="login.selsct"></fmt:message>",-1);
		for ( var i = 0; i < data.length; i++) {
		//添加下了列表option
		   addOptions(data[i],"zones");
			var zone = data[i];
			//create marker
			if (googleUnAble==false){
			//= new GIcon(G_DEFAULT_ICON);
			//ico.image
			var ico  = "images/google_ico/pyramid-southam_blue.png";
			var shadIco  = "images/google_ico/shadow-pyramid-southam_blue.png";
            createMarker("<fmt:message key="login.zone"></fmt:message>",zone,ico,shadIco);
		 }
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
        clreaSelect("sZones","subZoneError");
      var selectInfo=select.value;
	 if(selectInfo==""||selectInfo==-1){
	    return false;
	  }
      $("factoryName").length=0;
	  var id=selectInfo.split(",")[0];
	  StatisticsEventDwr.getSubZonesByZId(id,subZones);
	}
	//获得所有子区域
   function getSunZones(){
     clreaSelect("sZones","subZoneError");
	  StatisticsEventDwr.getAllSubZones(subZones);
	}
	
	function subZones(data){
		 if(data.length<1){
		   $("subZoneError").innerHTML="<fmt:message key="login.error.loadSubZoon"></fmt:message>";
		   return false;
		}
		if(firstData!=null){
	  		firstData=data;
	  	}
	    $("sZones").options[$("sZones").length] = new Option("<fmt:message key="login.selsct"></fmt:message>",-1);
	    for(var i=0;i<data.length;i++){
	     var object=data[i];
	     addOptions(object,"sZones");
         if (googleUnAble==false){
	     	//create marker
	     	//= new GIcon(G_DEFAULT_ICON);
 			//ico.image 
 			var ico = "images/google_ico/pyramid-egypt.png";
 			var shadIco = "images/google_ico/shadow-pyramid-egypt.png";
	      	createMarker("<fmt:message key="login.subZone"></fmt:message>",object,ico,shadIco);
	      }
	 }
	}
	//
	function getFactorySelect(select){
		 var markname=select.options[select.selectedIndex].text;
	      for(var i=0;i<factoryMarkers.length;i++){
	      if(markname==factoryMarkers[i][0]){
	      	factoryMarkers[i][1].setMap(map);
	       // sZoneMarkers[i][1].show();
	       continue;
	      }
	      factoryMarkers[i][1].setMap(null);
	      //sZoneMarkers[i][1].hide();
	    }
	}
	//获得根据子区域获得工厂
	function getSelectFactory(select){
	  seachSunZones(select);
	  var selectInfo=select.value;
      clreaSelect("factoryName","factoryError");
	  var id=selectInfo.split(",")[0];
	  if(id<0){
	 	 return false;
	  }
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
	  StatisticsEventDwr.getFactories(factory);
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
	 	if(firstData!=null){
	  		firstData=data;
	  	}
	    $("factoryName").options[$("factoryName").length] = new Option("<fmt:message key="login.selsct"></fmt:message>",-1);
	    for(var i=0;i<data.length;i++){
	     var object=data[i];
	     if(object.disabled)
	    	 continue;
	     addOptions(object,"factoryName");
	     if (googleUnAble==false){
	     //= new GIcon(G_DEFAULT_ICON);
			//ico.image
	     	var ico = "images/google_ico/factory-blue.PNG";
	     	var shadIco = "images/google_ico/shadow-factory-blue.png";
	     	createMarker("<fmt:message key="login.factory"></fmt:message>",object,ico,shadIco);
	     }
	 }
	}
    var zoneMarkers=new Array();
	var sZoneMarkers=new Array();
	var factoryMarkers=new Array();
	//添加标记
	function createMarker(type,object,ico,shadIco){
		//create marker
	       var markerOptions = {
				icon : ico
			};
			
		var image = new google.maps.MarkerImage(ico);
		var point =new google.maps.LatLng(object.lon,object.lat); //获取坐标  
			
			
		var shadow = new google.maps.MarkerImage(shadIco);
	      // Shapes define the clickable region of the icon.
	      // The type defines an HTML <area> element 'poly' which
	      // traces out a polygon as a series of X,Y points. The final
	      // coordinate closes the poly by connecting to the first
	      // coordinate.
      
       /** var shape = {
		      coord: [1, 1, 1, 26, 32, 26, 32 , 1],
		      type: 'poly'
		  };
		  */    	
		// var marker = new new google.maps.Marker({position:point,map:map,icon:image,title:'43'}); //创建标记
		 var marker = new google.maps.Marker({
     			 position: point,
     			 map: map,
      			 shadow: shadow,
      			 icon: image,
      			// shape: shape,
       			 zIndex: 5
   			 });
		    
		    var promptContent ="<font class='smallTitle'>"+type+":</font><font class='formLabelRequired'>";
		    if(type=="<fmt:message key="login.zone"></fmt:message>"){
		     	promptContent+="<a href='subzone_list.shtm?zoneId="+object.id+"'>"+object.scopename+"</a>";
		    }
		    if(type=="<fmt:message key="login.subZone"></fmt:message>"){
		    	promptContent+="<a href='factory_list.shtm?subzoneId="+object.id+"'>"+object.scopename+"</a>"; 
		    }
		     if(type=="<fmt:message key="login.factory"></fmt:message>"){
		     	promptContent+="<a href='watch_list.shtm?factoryId="+object.id+"'>"+object.scopename+"</a>";  
		    }
			
			promptContent+="</font></br>"+"<font class='smallTitle'><fmt:message key="login.factory.comment"></fmt:message>:</font><font class='formLabelRequired'>"+object.description+"</font>";

		    addListener(marker, point, promptContent,object.id,object.scopetype);
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
		  //  map.addOverlay(marker); //将标记添加进地图 
	}
	
	function reset() {
		removeAllMarkers();
		getAllMarkers();
	}

	//事件绑定
	function addListener(pointMarker, latlng, content, id,type) {
		 google.maps.event.addListener(pointMarker, "click", function() {
		  var drspec1=false;
          var drspec2=false;
          if($("drspec1").checked)
          	drspec1=true;
          if($("drspec2").checked)
          	drspec2=true;
          var startTs=$get("fromYear")+"/"+$get("fromMonth")+"/"+$get("fromDay")+"/"+$get("fromHour")+":"+$get("fromMinute")+":"+$get("fromSecond");
          var endTs=$get("toYear")+"/"+$get("toMonth")+"/"+$get("toDay")+"/"+$get("toHour")+":"+$get("toMinute")+":"+$get("toSecond");
		  var fromNone=$get("fromNone");
		  var toNone= $get("toNone");
		  var eventLevel;
		  var info="";
		  if($get("escalation2")){
		  	eventLevel=3;
		  	info="<fmt:message key="event.statistics.warn.red"/>";
		  }
		   if($get("escalation1")){
		   	eventLevel=2;
		   	info="<fmt:message key="event.statistics.warn.orange"/>";
		  }
		   if($get("active")){
		   	eventLevel=1;
		   	info="<fmt:message key="event.statistics.warn.yellow"/>";
		  }	
		   StatisticsEventDwr.getEventCount(id,type,drspec1,drspec2,startTs,fromNone,endTs,toNone,eventLevel,function (response){	
			contentTemp=content+"<br><font class='smallTitle'>"+info+":</font><font class='formLabelRequired'>"+response.warn+"</font>";
			//parent.map.openInfoWindow(latlng,contentTemp);
			var infowindow = new google.maps.InfoWindow(
			      { content: contentTemp,
			        size: new google.maps.Size(50,50)
			      });
			infowindow.open(map,pointMarker);
			//DSInfo = "";
			});
		});
	}
	function getEventCount(id,type){
		  var drspec1=false;
          var drspec2=false;
          if($("drspec1").checked)
          	drspec1=true;
          if($("drspec2").checked)
          	drspec2=true;
          var startTs=$get("fromYear")+"/"+$get("fromMonth")+"/"+$get("fromDay")+"/"+$get("fromHour")+":"+$get("fromMinute")+":"+$get("fromSecond");
          var endTs=$get("toYear")+"/"+$get("toMonth")+"/"+$get("toDay")+"/"+$get("toHour")+":"+$get("toMinute")+":"+$get("toSecond");
		  var fromNone=$get("fromNone");
		  var toNone= $get("toNone");
		  var eventLevel;
		  if($get("escalation2")){
		  	eventLevel=3;
		  }
		   if($get("escalation1")){
		   	eventLevel=2;
		  }
		   if($get("active")){
		   	eventLevel=1;
		  }	
		   StatisticsEventDwr.getEventCount(id,type,drspec1,drspec2,startTs,fromNone,endTs,toNone,eventLevel,
		   	function (response){
		     return response;	
		   });
	}
	//清除所有标记
	function removeAllMarkers() {
		map.clearOverlays();
	}
	//获取坐标
	function updateAddress() {
	 	 show("warnTr");
	 	 $("eventCount").innerHTML="<fmt:message key="events.search.searching"/>";
          var drspec1=false;
          var drspec2=false;
          if($("drspec1").checked)
          	drspec1=true;
          if($("drspec2").checked)
          	drspec2=true;
          var startTs=$get("fromYear")+"/"+$get("fromMonth")+"/"+$get("fromDay")+"/"+$get("fromHour")+":"+$get("fromMinute")+":"+$get("fromSecond");
          var endTs=$get("toYear")+"/"+$get("toMonth")+"/"+$get("toDay")+"/"+$get("toHour")+":"+$get("toMinute")+":"+$get("toSecond");
		  var fromNone=$get("fromNone");
		  var toNone= $get("toNone");
		  var eventLevel;
		  if($get("escalation2")){
		  	eventLevel=3;
		  }
		   if($get("escalation1")){
		   	eventLevel=2;
		  }
		   if($get("active")){
		   	eventLevel=1;
		  }
          //removeAllMarkers();
	   var factoryInfo=$("factoryName").value.split(",");
	    if(factoryInfo[0]==""||factoryInfo[0]=="<fmt:message key="login.selsct"></fmt:message>"||factoryInfo[0]==-1){
	   //   var text=$("factoryName").options[$("factoryName").selectedIndex].text;
	      var  info= $("sZones").value.split(",");
	      //  text=$("sZones").options[$("sZones").selectedIndex].text;
			if(info[0]==-1||info[0]==""||info[0]=="<fmt:message key="login.selsct"></fmt:message>"){
			    var  info= $("zones").value.split(",");
			   // text=$("zones").options[$("zones").selectedIndex].text;
			    if(info[0]==-1||info[0]==""||info[0]=="<fmt:message key="login.selsct"></fmt:message>"){
			   	     var scopeId=info[0];
			    	 StatisticsEventDwr.getEventCount(1,0,drspec1,drspec2,startTs,fromNone,endTs,toNone,eventLevel,function (response){
			     	 $("eventCount").innerHTML=response.warn;
			     	});
			     }
			    else{
			     update(info,5);
			       var scopeId=info[0];
			    	 StatisticsEventDwr.getEventCount(scopeId,1,drspec1,drspec2,startTs,fromNone,endTs,toNone,eventLevel,function (response){
			     	 $("eventCount").innerHTML=response.warn;
			     	});
			     
			    }
			    
			}else{
				update(info,8);
				var scopeId=info[0];
				StatisticsEventDwr.getEventCount(scopeId,2,drspec1,drspec2,startTs,fromNone,endTs,toNone,eventLevel,function (response){
			     	 $("eventCount").innerHTML=response.warn;
			     	 });
			 }
	    }
	    else{
	     update(factoryInfo,12);
	    	var scopeId=factoryInfo[0];
			StatisticsEventDwr.getEventCount(scopeId,3,drspec1,drspec2,startTs,fromNone,endTs,toNone,eventLevel,function (response){
			     	 $("eventCount").innerHTML=response.warn;

			     });
	    }
	}
	
	function update(info,size){
		if (googleUnAble==true){
	 		return;
		}
	    var address=info[1]+","+info[2];
				 geocoder.geocode({ 'address': address}, function(location,status) {
					map.setCenter(location[0].geometry.location);
					map.setZoom(size);
				});
	}
	 function updateDateRangeFields() {
        var dateRangeType = $get("dateRangeType");
        if ($("drspec1").checked) {
            setDisabled("fromYear", true);
            setDisabled("fromMonth", true);
            setDisabled("fromDay", true);
            setDisabled("fromHour", true);
            setDisabled("fromMinute", true);
            setDisabled("fromSecond", true);
            setDisabled("fromNone", true);
            setDisabled("toYear", true);
            setDisabled("toMonth", true);
            setDisabled("toDay", true);
            setDisabled("toHour", true);
            setDisabled("toMinute", true);
            setDisabled("toSecond", true);
            setDisabled("toNone", true);
        }
        else {
            var inception = $get("fromNone");
            setDisabled("fromYear", inception);
            setDisabled("fromMonth", inception);
            setDisabled("fromDay", inception);
            setDisabled("fromHour", inception);
            setDisabled("fromMinute", inception);
            setDisabled("fromSecond",inception);
            setDisabled("fromNone", false);
            
            var now = $get("toNone");
            setDisabled("toYear", now);
            setDisabled("toMonth", now);
            setDisabled("toDay", now);
            setDisabled("toHour", now);
            setDisabled("toMinute", now);
            setDisabled("toSecond", now);
            setDisabled("toNone", false);
        }
    }
    
</script>
  <script type="text/javascript">
    compatible = false;
    function setFocus() {
        //检测浏览器版本后 加载地图
        initialize();
        StatisticsEventDwr.getDateRangeDefaults(<c:out value="<%= Common.TimePeriods.DAYS %>"/>, 1, function(data) { setDateRange(data); });
    }
  </script>
 <body>
 <table align="center">
 <tr>
  <td valign="top">
  	 <div id="map_canvas" class="borderDiv">
	 </div>
  </td>
  <td valign="top">
      <div id="selectTable" class="borderDiv" style="width: 100%;">
		<table width="100%">
			<tr>
				<td class="smallTitle titlePadding" colspan="2"><fmt:message key="events.search"/><tag:help id="eventsSearch"/></td>
			</tr>
			<tr>
			 	<td class="horzSeparator" colspan="2"></td>
			</tr>
			<tr>
				<td class="smallTitle" colspan="2"><fmt:message key="scope.search"></fmt:message></td>
			</tr>
			<tr id="zoneTr">
				<td class="formLabelRequired">
				<fmt:message key="login.zone"></fmt:message>
				</td>
				<td class="formField">
					<select id="zones" onchange="getSelectSunZones(this)">
					</select>
					<br/><font id="zoneError" style="font-size: 12px; color: red;"></font>
				</td>
			</tr>
			<tr id="subZoneTr">
				<td class="formLabelRequired">
					<fmt:message key="login.subZone"></fmt:message>
				</td>
				<td class="formField">
					<select id="sZones" onchange="getSelectFactory(this)">
					</select>
					<br/>
					<font id="subZoneError" style="font-size: 12px; color: red;"></font>
				</td>
			</tr>
			<tr id="factoryTr">
					<td class="formLabelRequired">
						<fmt:message key="login.factory.name"></fmt:message>
					</td>
					<td class="formField">
						<select id="factoryName" onchange="getFactorySelect(this)">
						</select>
						<br/>
						<font id="factoryError" style="font-size: 12px; color: red;"></font></td>
			</tr>
			<tr>
				  <td class="horzSeparator" colspan="2"></td>
				</tr>
			<tr>
				 	<td colspan="2" class="smallTitle"><fmt:message key="reports.dateRange"/></td>
                 </tr>
             <tr>
                 	<td colspan="2">
                 		<table>
                 			<tr>
                 			     <td class="formLabelRequired">
			                    	 <input type="radio" name="dateRangeType"  id="drspec1" 
			                             onchange="updateDateRangeFields()"/><label for="drspec1"><fmt:message key="event.statistics.now.day"/></label>
			                    </td>
			                 	 <td class="formLabelRequired">
			                    	<input type="radio" name="dateRangeType" id="drspec2" 
			                           checked="checked"  onchange="updateDateRangeFields()"/><label for="drspec2"><fmt:message key="reports.specificDates"/></label>
			                 	 </td>
                 			</tr>
                 		</table>
                 	</td>
               </tr>
               <tr>
                    <td colspan="2">
                 		<tag:dateRange/>
  					</td>
 				</tr>
 				<tr>
				  <td class="horzSeparator" colspan="2"></td>
				</tr>
				<tr>
					<td class="smallTitle"><fmt:message key="events.search.type"/></td>
					<td>
					 <table>
					  <tr>
					   <td>
					   <table  style="padding-left:40px;" >
					   		<tr>
					   			<td align="left" class="formLabelRequired">
					 	  		<input type="radio" name="eventType" value="1" id="active"  checked="checked"
	                            	onchange=""/><label for="active"><fmt:message key="event.statistics.warn.yellow"></fmt:message></label>
						   		</td>
					   		</tr>
					   		<tr>
					   			<td  align="left" class="formLabelRequired">
					 	  		<input type="radio" name="eventType" value="2" id="escalation1" 
	                           		 onchange=""/><label for="escalation1"><fmt:message key="event.statistics.warn.orange"></fmt:message></label>
						   		</td>
					   		</tr>
					   		<tr>
					   			<td align="left" class="formLabelRequired">
					 	  			<input type="radio" name="eventType" value="3" id="escalation2" 
	                          		  onchange=""/><label for="escalation2"><fmt:message key="event.statistics.warn.red"></fmt:message></label>
						   		</td>
					   		</tr>
					   </table>
					   </td>
				  </tr>
				 </table>
				</td>
				</tr>
				<tr>
				  <td class="horzSeparator" colspan="2"></td>
				</tr>
				<tr id="warnTr" style="cellspacing:20px; display: none;" >
					<td class="smallTitle"><fmt:message key="events.count"/></td>
					<td class="formField"><label class="formError" id="eventCount">0</td>
				</tr>
				<tr>
					<td colspan="2" align="center" class="formField">
						<input type="button" onclick="reset()"value="<fmt:message key="login.factory.selectReset"></fmt:message>">&nbsp;&nbsp;&nbsp;&nbsp;
		           		<input type="button" value="<fmt:message key="login.search"></fmt:message>" onclick="updateAddress()">
		           	</td>
				</tr>
			</table>
		</div>
	</td>
	</tr>
	</table>
	</body>
</tag:page>