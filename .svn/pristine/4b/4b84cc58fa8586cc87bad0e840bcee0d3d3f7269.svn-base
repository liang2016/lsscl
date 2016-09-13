<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>

<%@ include file="/WEB-INF/jsp/include/tech.jsp"%>
<tag:page dwr="ZoneDwr" onload="initialize">
<script type="text/javascript" src="http://api.map.baidu.com/api?v=1.2"></script>
			<style type="text/css">
#openInfo {
	text-align: left;
	font-size: 12px;
}

#map_canvas {
	height: 700px;
	width: 700px;
	float: right;
}	
</style>

<script type="text/javascript">
	function init(){
	   var subZoneListTbody = $("subZoneTbody");
		var subZoneTr=subZoneListTbody.getElementsByTagName("tr");
		 //清空表中的行和列
		 for(var i=subZoneTr.length-1; i>=0;i--){
		  subZoneListTbody.removeChild(subZoneTr[i]);
		 }
		 
	}
	var map = null;
	var basePoint = new BMap.Point( 112.1419,32.4419);
	function initBaiduMap(){
			map = new BMap.Map("map_canvas");
			map.centerAndZoom(basePoint,5);
            map.enableScrollWheelZoom();    //启用滚轮放大缩小，默认禁用
            map.enableContinuousZoom();
            
	}
</script>
<script type="text/javascript">
			var scopeId=${sessionUser.currentScope.id};
			var map = null;
			var geocoder;
			 var marker;
	        function initialize() {   
	        
				   initBaiduMap();
		 getAllMarkers();
	   }
	function createMarkers() {
		   $("zones").length=0;
	      	getZones();
	 }
   
   //加载所有
   function getAllMarkers(){
   	removeAllMarkers();
    //区域
     getZones();

   }
   
	//获取子区域
	function getZones() {
		ZoneDwr.getSubZonesByZId(scopeId,getAllZones);
	}
	
	//加载区域
	function getAllZones(data) {
		var ico;
		var shadIco;
		for ( var i = 0; i < data.length; i++) {
			var zone = data[i];
			//create marker
			ico= "images/google_ico/pyramid-egypt.png";
			shadIco= "images/google_ico/shadow-pyramid-egypt.png";
            createMarker("<fmt:message key="subZone.name"></fmt:message>",zone,ico,shadIco);
		
		var checkTr=document.createElement("tr");
	    var checkTd=document.createElement("td");
	    var checkTd1=document.createElement("td");
	    var checkTd2=document.createElement("td"); 
	    var checkTd3=document.createElement("td"); 
	    var checkTd4=document.createElement("td"); 
	    var checkTd5=document.createElement("td");  
        checkTd.innerHTML="<b>"+data[i].scopename+"</b>";    
        checkTd1.innerHTML=data[i].scopeUser.username; 
        checkTd2.innerHTML="<img class='ptr'title='<fmt:message key='header.zoneList'></fmt:message>' alt='<fmt:message key='header.zoneList'></fmt:message>' src='images/google_ico/arrow_left.png' onclick='window.location=\"zone_list.shtm\"'>&nbsp;&nbsp;";  
        checkTd2.innerHTML+="<img class='ptr' title='<fmt:message key='zone.enter'></fmt:message>' alt='<fmt:message key='zone.enter'></fmt:message>' src='images/google_ico/arrow_right.png' onclick='window.location=\"factory_list.shtm?subzoneId="+zone.id+"\"'>"; 
        checkTd3.innerHTML="<img class='ptr' title='<fmt:message key='zone.edit'></fmt:message>' alt='<fmt:message key='zone.edit'></fmt:message>' src='images/scope/edit_zone.png'	onclick='getZonesById("+zone.id+")'>";    
 		checkTd4.innerHTML="<img class='ptr' title='<fmt:message key='zone.delete'></fmt:message>'alt='<fmt:message key='zone.delete'></fmt:message>'src='images/google_ico/factory_delete.png' onclick='window.location=\"subzone_delete.shtm?subzoneId="+zone.id+"\"'>";
		checkTr.appendChild(checkTd);
	    checkTr.appendChild(checkTd1);
	    checkTr.appendChild(checkTd2);
	    checkTr.appendChild(checkTd3);			
        checkTr.appendChild(checkTd4);
	    if(i%2==0)
	       checkTr.className="row";
	    else
	       checkTr.className="rowAlt";
        $("subZoneTbody").appendChild(checkTr);
		
		}
	}
    var zoneMarkers=new Array();
	var sZoneMarkers=new Array();
	var zoneMarkers=new Array();
	//添加标记
//添加标记
	function createMarker(type,object,ico,shadIco){
		//create marker
	    var icon = new BMap.Icon(ico, new BMap.Size(32, 32), {
                anchor: new BMap.Size(10, 30)
        });
		var mkr = new BMap.Marker(new BMap.Point(object.lat,object.lon), {
                icon: icon
         });  
		map.addOverlay(mkr); //将标记添加进地图 
		
		var promptContent ="<b id='zoneStyle'>"+type+":</b><a href='subzone_list.shtm?zoneId="+object.id+"'>"+ object.scopename+ "</a></br>"+"<b id='commentStyle'><fmt:message key="login.zone.comment"></fmt:message>:</b>"+object.description;
		mkr.addEventListener("click",function(e){
                var info_html = promptContent;
                this.openInfoWindow(new BMap.InfoWindow(info_html));
        });	}
	
	function reset() {
		removeAllMarkers();
		getAllMarkers();
	}
	//清除所有标记
	function removeAllMarkers() {
		map.clearOverlays();
	}
	//获取坐标
	function updateAddress() {
		//removeAllMarkers();
	   var zoneInfo=$("zoneName").value.split(",");
	    if(zoneInfo[0]==""||zoneInfo[0]=="<fmt:message key="login.selsct"></fmt:message>"){
	   //   var text=$("zoneName").options[$("zoneName").selectedIndex].text;
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
	     update(zoneInfo,12);
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
	   for(var i=0;i<zoneMarkers.length;i++){
	    if(zoneMarkers[i][0]=="北京"){
	    //变化图标
          zoneMarkers[i][1].setImage("images/google_ico/zone_exception.gif");
	    }
	  }
	}

	 function getaddressInfo() {
	    removeAllMarkers();
	    if(marker)
	    	marker.setMap(null);
	    var address=$('address').value;
	    $("errorMessage").innerHTML="<fmt:message key="google.search.loading"></fmt:message>" + address;
      	if (map){
		var options = {renderOptions: {map: map, panel: "results"},onSearchComplete:function(ret){
                    var point = ret.getPoi(0).point;
                    $("lon").value = point.lat;
                    $("lat").value = point.lng
					$("errorMessage").innerHTML = "";
        }};
      	 var myLocalsearch = new BMap.LocalSearch(map,options);
		 myLocalsearch.search(address);
      }
    }

	 function clearzoneError(){
	  $("errorMessage").innerHTML="";
	  $("lon").value="";
	  $("lat").value="";
	 }
	 function showsubZoneDiv(){
	    $("errorMessage").innerHTML="";
	    $("userMessage").innerHTML="";
	    $("addzone").style.display="block";
	   	$("id").value="";
	   	$("scopeId").value="";
		$("scopename").value="";
		$("address").value="";
		$("description1").value="";
		$("lon").value="";
		$("lat").value="";
		$("enlargenum").value="";
	  	 }
	
</script>
<script type="text/javascript">
            //保存工厂
             function saveScope(obj){
            	 $("userMessage").innerHTML="";
                 $("enlargenum").value=map.getZoom();
                 var scope=new Object;
				 scope.name=$("scopename").value;
				 scope.lon=$("lon").value;
				 scope.lat=$("lat").value;
				 scope.description=$("description1").value;
				if(scope.name.trim()==""){
				  $("errorMessage").innerHTML="<fmt:message key="FactoryList.factory.error.nameNull"></fmt:message>";
				  return false;
				}
				 if(scope.zoneId==-1){
				   $("fZonesError").innerHTML="<fmt:message key="FactoryList.factory.error.zoneNull"></fmt:message>";
				    return false;
				 }
				 if(scope.description.trim()==""){
				   $("errorMessage").innerHTML="<fmt:message key="FactoryList.factory.error.commentNull"></fmt:message>";
                   return false;				   
				 }
				 	ZoneDwr.editScope($("id").value,$("scopename").value,$("address").value,
						$("description1").value,null,$("lon").value,$("lat").value,$("enlargenum").value,2,${sessionUser.currentScope.id},0,false,
					 function(response){
					 if(response.hasMessages){
					     showDwrMessages(response.messages, $("errorMessageSave"));
					 }
					 else{
						 $set("id",response.data.scope.id);
						 $("userMessage").innerHTML="<fmt:message key="subzone.saved"></fmt:message>";
						 $("errorMessage").innerHTML="";
						 init();
						 if(typeof(marker)!='undefined') 
							 marker.setMap(null);
						}
					});
				}
				
		//获得要修改数据并且封装
		function setScopeVo(){
		var ScopeVo=new Object;
		ScopeVo.id=$("id").value;
		ScopeVo.scopename=$("scopename");
	    ScopeVo.address=$("address").value;
		ScopeVo.description=$("description1").value;
		ScopeVo.lon=$("lon").value;
		ScopeVo.lat=$("lat").value;
		ScopeVo.enlargenum=$("enlargenum").value;
		ZoneDwr.updateZone(ScopeVo,returnData);
		}		
		function returnData(data){
		alert(data);
		}		
            //添加一行信息
            
           	//获得指定的区域(根据编号)
		function getZonesById(zoneid){
		  showsubZoneDiv();
		  ZoneDwr.selectSubZoneById(zoneid,editInfo);
		}
		function editInfo(zone){
			$("id").value=zone.id;
			$("scopeId").value=zone.id;
			$("scopename").value=zone.scopename;
			$("address").value=zone.address;
			$("description1").value=zone.description;
			$("lon").value=zone.lon;
			$("lat").value=zone.lat;
			$("enlargenum").value=zone.enlargenum;
			$("zoneLogo").src=zone.backgroundFilename;
		} 
        function checkScope(){
        	var scopeId=$("scopeId").value;
        	if(scopeId==""||scopeId==null){
        		alert("<fmt:message key="scope.isNull"></fmt:message>");
        		return false;
        	}
        }       
</script>
<table align="center">
	<tr>
		<td valign="top">
			<div id="map_canvas" style="float: left;" class="borderDiv marR marB">
			</div>
		</td>
		<td valign="top">
			<div style="float: left;">
				<table  class="borderDiv marB marR">
					<thead>
					<tr class="rowHeader">
						<td>
							<b><fmt:message key="subZone.name"></fmt:message> </b>
						</td>
						<td>
							<fmt:message key="user.admin"></fmt:message>
						</td>
						<td colspan="3">
							<img class="ptr"
								title="<fmt:message key="subZone.add"></fmt:message>"
								alt="<fmt:message key="subZone.add"></fmt:message>"
								src="images/google_ico/factory_add.png"
								onclick="showsubZoneDiv()">
						</td>
					</tr>
					<tr>
					</tr>
					</thead>
					<tbody id="subZoneTbody">
					</tbody>
				</table>
			</div>
		</td>
		<td valign="top">
			<div class="borderDiv marR marB" style="float: left; display: none;"
				id="addzone">
					<table>
						<tr class="rowHeader">
							<td class="smallTitle">
								<fmt:message key="subzone.edit.properties"></fmt:message>
							</td>
							<td>
								<img class="ptr" align="right"
									title="<fmt:message key="zone.edit.properties"></fmt:message>"
									alt="<fmt:message key="zone.edit.properties"></fmt:message>"
									src="images/save_add.png"
									onclick="javascript:saveScope(document.scopeForm)">
							</td>
						</tr>
						<tr style="display: none">
							<td colspan="2">
								<input type="text" id="id" name="id">
							</td>
						</tr>
						<tr>
						<td colspan="2">
							<label class="formError" id="userMessage"></label>
						</td>
						</tr>
						<tr>
							<td class="formLabelRequired">
								<fmt:message key="subzone.name"></fmt:message>
							</td>
							<td class="formField">
								<input type="text" id="scopename" name="scopename">
							</td>
						</tr>
						<!-- 地址 -->
						<tr>
							<td class="formLabelRequired">
								<fmt:message key="zone.address"></fmt:message>
							</td>
							<td class="formField">
								<input type="text" id="address" name="address"
									onblur="getaddressInfo()">
							</td>
						</tr>
						<tr>
							<td class="formLabelRequired">
								<fmt:message key="zone.comment"></fmt:message>
							</td>
							<td class="formField">
								<textarea id="description1" name="description1" rows="5" cols="18"></textarea>
							</td>
						</tr>
						<tr>
							<td class="formLabelRequired">
								<fmt:message key="zone.lat"></fmt:message>
							</td>
							<td class="formField">
								<input type="text" id="lon" name="lon">
							</td>
						</tr>
						<tr>
							<td class="formLabelRequired">
								<fmt:message key="zone.lon"></fmt:message>
							</td>
							<td class="formField">
								<input type="text" id="lat" name="lat">
							</td>
						</tr>
						<tr>
							<td class="formLabelRequired">
								<fmt:message key="zone.enlargenum"></fmt:message>
							</td>
							<td class="formField">
								<input type="text" id="enlargenum" name="enlargenum">
							</td>
						</tr>
						<tr>
							<td colspan="2">
  								<form name="scope" action="/subzone_edit.shtm?zoneId=${sessionUser.currentScope.id}" method="post" enctype="multipart/form-data" onsubmit="return checkScope()">
  									  <table>
  									 <spring:bind path="form.backgroundImageMP">
						                <tr>
						                  <td class="formLabelRequired"><fmt:message key="viewEdit.background"/></td>
						                  <td class="formField">
						                    <input id="logo" type="file" name="backgroundImageMP"/>
						                  </td>
						                  <td class="formError">${status.errorMessage}</td>
						                </tr>
						              </spring:bind>
						              <spring:bind path="form.scope.id">
						              	<tr>
											<td>
												<input type="hidden" id="scopeId" name="scope.id" value="${status.value}">
											</td>
											 <td  colspan="2" class="formError">${status.errorMessage}</td>
										</tr>
						              </spring:bind>
						              <tr>
						                <td colspan="2" align="center">
						                  <input type="submit" name="upload" value="<fmt:message key="viewEdit.upload"/>"/>
						                  <input type="submit" name="clearImage" value="<fmt:message key="viewEdit.clearImage"/>"/>
						                </td>
						                <td></td>
						              </tr>
						               <tr>
						                <td colspan="3" align="center">
						                  <img alt="<fmt:message key="scope.logo"/>" id="zoneLogo" src="">
						                </td>
						                <td></td>
						              </tr>
						            </table>
  								</form>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<label id="errorMessage" class="formError"></label>
							</td>
						</tr>
						<tbody id="errorMessageSave" class="formError" style="display:none;"></tbody>
					</table>
			</div>
		</td>
	</tr>
</table>
</tag:page>
