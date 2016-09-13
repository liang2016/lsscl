<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>
<%@page import="com.serotonin.mango.Common"%>
<%@page import="com.serotonin.mango.web.dwr.statistics.StatisticsInformationDwr"%>
<tag:page dwr="StatisticsInformationDwr" onload="init">
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>  
<script type="text/javascript">
	var map = null;
	var geocoder = null;
	function init(){
		StatisticsInformationDwr.potentialSearchInit(function(response){
			var childScope = response.data.childScope;
			var grandchildScope = response.data.grandchildScope;
			var factoryList = response.data.factoryList;
			// write the select 
			if(childScope!=null){
				$j("#zoneListTd").show();
				DWRUtil.removeAllOptions('zoneList');
				var childSelectHeader=document.createElement("OPTION");  
				childSelectHeader.value=-1;
				childSelectHeader.text="<fmt:message key='common.noSelected' />";
				var childselect=document.getElementById("zoneList");
				childselect.options.add(childSelectHeader);
	   			DWRUtil.addOptions('zoneList',childScope,"id","scopename");
			}
	   		if(grandchildScope!=null){
	   			$j("#subzoneListTd").show();
				DWRUtil.removeAllOptions('subzoneList');
				var grandChildSelectHeader=document.createElement("OPTION");  
				grandChildSelectHeader.value=-1;
				grandChildSelectHeader.text="<fmt:message key='common.noSelected' />";
				var grandChildselect = document.getElementById("subzoneList");
				grandChildselect.options.add(grandChildSelectHeader);
   				DWRUtil.addOptions('subzoneList',grandchildScope,"id","scopename");
	   		}
	   		if(factoryList!=null){
				// write factory list on the  google map ,please use var factoryList
				if(typeof(google)!='undefined'){  
					geocoder = new google.maps.Geocoder();
				}
				else{
					alert("<fmt:message key="google.failed"></fmt:message>");
					return;
				}	
				initMap(factoryList);
	   		}
		});
	}
	
	// childScope onchange event
	function refreshSubzoneList(childScope){
		if(childScope==-1){
			DWRUtil.removeAllOptions('subzoneList');
			var grandChildSelectHeader=document.createElement("OPTION");  
			grandChildSelectHeader.value=-1;
			grandChildSelectHeader.text="<fmt:message key='common.noSelected' />";
			var grandChildselect = document.getElementById("subzoneList");
			grandChildselect.options.add(grandChildSelectHeader);
		}else{
			StatisticsInformationDwr.getGrandchildScope(childScope,function(grandchildScope){
				DWRUtil.removeAllOptions('subzoneList');
	   			DWRUtil.addOptions('subzoneList',grandchildScope,"id","scopename");
			});
		}
	}
	 function initMap(scope) { 
		  if(scope==null)
			 	return;
	      /** if (GBrowserIsCompatible()) {*/      
	            var myLatlng = new google.maps.LatLng(32.4419,112.1419);  
                var myOptions = {  
                    zoom: 4,  
                    center:myLatlng,  
                    mapTypeId: google.maps.MapTypeId.ROADMAP  
                };  
                map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);    
	          /** 
	            map= new GMap2(document.getElementById("map_canvas"));        
	            map.setCenter(new GLatLng(39.9493, 116.3975), 13);  
	           	map.setCenter(new GLatLng(32.4419, 112.1419), 4);
				map.addControl(new GLargeMapControl());
				map.addControl(new GScaleControl());
				map.addControl(new GMapTypeControl());
				map.addControl(new GOverviewMapControl());
				geocoder = new google.maps.ClientGeocoder();
				*/
				getAllMarkers(scope); 
			/**}
		  	else{
		    	alert('Google Map Error!');    
			}*/
	   }
	  //load scope
   function getAllMarkers(scope){
  		map.setCenter(new google.maps.LatLng(32.4419, 112.1419));
  		map.setZoom(4);
        if(scope.length<1)
           return;
        else{
        	var ico;
        	var shadIco;
        	for(var i=0;i<scope.length;i++){
	        	var scopeTemp = scope[i];
				//create marker
				ico= "images/google_ico/factory-blue.PNG";
				shadIco="images/google_ico/shadow-factory-blue.png";
	            createMarker(scopeTemp.scopetype,scopeTemp,ico,shadIco);
        	}
        } 	  
   }  
   
   	function createMarker(type,object,ico,shadIco){
		//create marker
		var markerOptions = {
			icon : ico
		};
		var image = new google.maps.MarkerImage(ico);
		var point =new google.maps.LatLng(object.lon,object.lat); //get latlon
	    //var marker = new GMarker(point, markerOptions); //
	     var shadow = new google.maps.MarkerImage(shadIco);
	    /** var shape = {
		      coord: [1, 1, 1, 26, 32, 26, 32 , 1],
		      type: 'poly'
		  };
	    */
	     var marker = new google.maps.Marker({
     			  position: point,
     			  map: map,
      			  shadow: shadow,
      			  icon: image,
      			  //shape: shape,
       			 zIndex: 5
   			 });
	    
	    var promptContent ="<font class='smallTitle'>";
	    if(type==1){//zone
		    promptContent+="<fmt:message key="login.zone"></fmt:message>:</font><font class='formLabelRequired'>";
		    promptContent+="<a href='subzone_list.shtm?zoneId="+object.id+"'>"+object.scopename+"</a>";
	    }
	    if(type==2){//subzone
	    	promptContent+="<fmt:message key="login.subZone"></fmt:message>:</font><font class='formLabelRequired'>";
	    	promptContent+="<a href='factory_list.shtm?subzoneId="+object.id+"'>"+object.scopename+"</a>"; 
	    }
	    if(type==3){//factory
	     	promptContent+="<fmt:message key="login.factory"></fmt:message>:</font><font class='formLabelRequired'>";
	     	promptContent+="<a href='watch_list.shtm?factoryId="+object.id+"'>"+object.scopename+"</a>";  
	    }
		promptContent+="</font></br>"+"<font class='smallTitle'><fmt:message key="login.factory.comment"></fmt:message>:</font><font class='formLabelRequired'>"+object.description+"</font>";
	    addListener(marker, point, promptContent,object.id,object.scopetype);
	    promptContent = "";
	   // map.addOverlay(marker); //
	}
	// click the mark in google map
	function addListener(pointMarker, latlng, content, id,type) {
		google.maps.event.addListener(pointMarker, "click", function() {
		var tempContent=content;
		var infowindow = new google.maps.InfoWindow(
			      { content: tempContent,
			        size: new google.maps.Size(50,50)
			      });
			infowindow.open(map,pointMarker);
			//parent.map.openInfoWindow(latlng,tempContent);
			//DSInfo = "";
		});
	} 
	
	//search potential of saving
	function searchPotential(){
		$j("#noResult").hide();
		var childId = $get("zoneList");
		if(childId=="") childId=-1;
		var grandchildId = $get("subzoneList");
		if(grandchildId=="") grandchildId=-1;
		var countOfMachine = $get("countOfMachine");
		if(countOfMachine==""){countOfMachine=-1}
		var volumetricOfMachine = $get("volumetricOfMachine");
		if(volumetricOfMachine==""){volumetricOfMachine=-1}
		var unloadRate;
		if($get("unloadRate")){unloadRate=1;}
		else{unloadRate=0;}
		var pressureWave;
		if( $get("pressureWave")){pressureWave=1;}
		else{pressureWave=0;}
		var systemPressureDrop;
		if( $get("systemPressureDrop")){systemPressureDrop=1;}
		else{systemPressureDrop=0;}
		var cycle =$get("cycle");
		var hasRunningInSameTime =false;// document.getElementById("startInSameTime").checked;
		StatisticsInformationDwr.potentialSearch(childId,grandchildId,countOfMachine,volumetricOfMachine,unloadRate,pressureWave,systemPressureDrop,cycle,hasRunningInSameTime,function(response){
			//waiting
			var factoryList = response.data.factoryList;
			if(factoryList==null||factoryList.length==0){
				$j("#resultList").hide();
				$j("#factoryList").html("");
				$j("#noResult").show();
			}else{
				$j("#resultList").show();
				var content = "";
				for(var i=0;i<factoryList.length;i++){
					var factory = factoryList[i];
					var row = "<tr class='row'><td>"+(i+1)+"</td><td style='white-space:nowrap;' ><a href='/watch_list.shtm?factoryId="+factory.id+"'>"+factory.scopename+"</a></td><td  style='white-space:nowrap;'>"+factory.address+"</td><td>"+factory.lon+"</td><td>"+factory.lat+"</td></tr>"
					content += row;
				}
				$j("#factoryList").html(content);
				initMap(factoryList);
			}
		}); 
	} 
</script>
<table align="center">
	<tr>
		<td valign="top">
			<div id="map_canvas"  class="borderDiv marR marB">
			</div>
		</td>
		<td valign="top">
			<div  style="height: 600px;"  class="borderDiv marR marB"  >
				<table>
					<tr><td  colspan="2" class="smallTitle" ><fmt:message key="statistics.potential.search" /><tag:help id="potentialSearch"/></td></tr>
					<tr>
						<td style="display: none" id="zoneListTd" class="formLabelRequired"> 
							<fmt:message key="common.scope.type.zone" />
							<select id="zoneList" onchange="refreshSubzoneList(this.value)"></select>
						</td>
						<td style="display: none" id="subzoneListTd" class="formLabelRequired">
							<fmt:message key="common.scope.type.subzone" />
							<select id="subzoneList"></select>
						</td>  
					</tr>
					<tr><td colspan="2">&nbsp;</td></tr>
					<tr><td colspan="2" class="horzSeparator"></td></tr>
					<tr>
						<td class="formLabelRequired"><fmt:message key="statistics.potential.countOfMachine" /><fmt:message key="common.than" />:</td>
						<td class="formField"><input type="text" id="countOfMachine" name="countOfMachine" >&nbsp;<fmt:message key="common.quantifier.tai" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
					</tr>
					<tr>
						<td class="formLabelRequired"><fmt:message key="statistics.potential.volumetricOfMachine" /><fmt:message key="common.than" />:</td>
						<td class="formField"><input type="text" id="volumetricOfMachine" name="volumetricOfMachine" >&nbsp;KW&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
					</tr>
					<tr><td colspan="2">&nbsp;</td></tr>
					<tr><td colspan="2" class="horzSeparator"></td></tr>
					<tr>
						<td class="formLabelRequired"><fmt:message key="statistic.potential.pressureWave" />:</td>
						<td class="formField"><input type="checkbox" id="pressureWave" name="pressureWave" ></td>
					</tr>
					<tr>
						<td class="formLabelRequired"><fmt:message key="statistic.potential.unloadRate" />:</td>
						<td class="formField"><input type="checkbox" id="unloadRate" name="unloadRate" ></td>
					</tr>
					<tr>
						<td class="formLabelRequired"><fmt:message key="statistic.potential.systemPressureDrop" />:</td>
						<td class="formField"><input type="checkbox" id="systemPressureDrop" name="systemPressureDrop" ></td>
					</tr>
					<tr><td colspan="2">&nbsp;</td></tr>
					<tr><td colspan="2" class="horzSeparator"></td></tr>
					<tr><td class="formLabelRequired"><fmt:message key="statistic.information.changeCycle" />:</td><td class="formField"><input type="radio" name="cycle" value="<c:out value='<%=StatisticsInformationDwr.CYCLE_YESTERDAY%>' />" checked="checked" id="defaultCycle" /><fmt:message key="statistic.information.date.yesterday" /></td></tr>
<!--					<tr><td class="formLabelRequired"><fmt:message key="statistic.information.changeCycle" />:<tr><td></td><td class="formField"><input type="radio" name="cycle" value="<c:out value='<%=StatisticsInformationDwr.CYCLE_WEEK%>' />" checked="checked" id="defaultCycle"  /><fmt:message key="statistic.information.date.lastweek" /></td></tr>-->
					<tr><td></td><td class="formField"><input type="radio" name="cycle" value="<c:out value='<%=StatisticsInformationDwr.CYCLE_WEEK%>' />"  /><fmt:message key="statistic.information.date.lastweek" /></td></tr>
					<tr><td></td><td class="formField"><input type="radio" name="cycle" value="<c:out value='<%=StatisticsInformationDwr.CYCLE_MONTH%>' />" /><fmt:message key="statistic.information.date.lastmonth" /></td></tr>
					<tr><td></td><td class="formField"><input type="radio" name="cycle" value="<c:out value='<%=StatisticsInformationDwr.CYCLE_QUARTER%>' />" /><fmt:message key="statistic.information.date.lastquarter" /></td></tr>
					<!--  <tr>
						<td colspan="2" class="formLabelRequired"><input type="checkbox" name="startInSameTime" id="startInSameTime" /><fmt:message key="statistics.potential.startInSameTime" />&nbsp;&nbsp;&nbsp;&nbsp;</td>
					</tr>
					-->
					<tr><td colspan="2" class="horzSeparator"></td></tr>
					<tr><td colspan="2" class="formLabelRequired"><input type="button" name="searchBtn" onclick="searchPotential()" id="searchBtn" value="<fmt:message key='statistics.potential.search' />" /></td></tr>
					<tr  id="noResult" style="display: none">
						<td colspan="4"  class="formLabelRequired" align="right"><span style="color: red"><fmt:message key="statistic.information.noresult"/></span></td>
					</tr>
					<tr>
						<td colspan="2">
							<div id="resultList" style="position:relative;width:auto; height:160px;overflow-x:auto;overflow-y:auto; display: none;" class="borderDiv marR marB"  >
								<table  style="width: auto;">
									<thead>
										<tr class="rowHeader" id="resultHeader">
										<td></td>
										<td><fmt:message key="factory.name"></fmt:message></td>
										<td><fmt:message key="zone.address"></fmt:message></td>
										<td><fmt:message key="zone.lon"></fmt:message></td>
										<td><fmt:message key="zone.lat"></fmt:message></td>
										</tr>
									</thead>
									<tbody id="factoryList">
									</tbody>
								</table>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</td>
	</tr>
</table>
</tag:page>