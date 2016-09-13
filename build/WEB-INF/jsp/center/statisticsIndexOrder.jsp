<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp"%>
<%@page import="com.serotonin.mango.Common"%>
<%@page import="com.serotonin.mango.rt.statistic.common.StatisticsUtil"%>
<%@page import="com.serotonin.mango.web.dwr.statistics.StatisticsInformationDwr"%>
<tag:page dwr="StatisticsInformationDwr" onload="init">
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>  
<script type="text/javascript">
	var geocoder;
	function init(){
		if(typeof(google)!='undefined'){  
			geocoder = new google.maps.Geocoder();
		}
		else{
			alert("<fmt:message key="google.failed"></fmt:message>");
				return;
		}	
		$j("#defaultCycle").attr("checked",true);
		$j("#order2").attr("checked",true);
		writePage($get("cycle"),<c:out value='<%=StatisticsUtil.ENERGY_SAVING_INDEX%>' />);
	}
	
	// on the basis of select condition to order the index
	function executeOrder(){
		var cycle  = $get("cycle");
		script = $get("order");
		writePage(cycle,script);
	}
	
	function writePage(cycle,script){
		$j("#indexList").html("");
		StatisticsInformationDwr.indexOrderInit(cycle,script,function(response){
			var indexList = response.data.indexList;
			var content = "";
			for(var i=0;i<indexList.length;i++){
				var indexValue = indexList[i];
				content +="<tr class='row' id='scope"+indexValue.id+"'>";
				content +="<td align='center' width='20%'>"+(i+1)+"</td>";
				content +="<td width='50%'>"+indexValue.scopeName+"</td>";
				var value = parseFloat(indexValue.value);
				if(value==-1){
					content +="<td width='30%'><fmt:message key='statistic.information.nodata'/></td>";
				}else{					
					content +="<td width='30%'>"+((value*100).toFixed(2))+"%</td>";
				}
				content +="</tr>";
			}
			if(content!=""){
				//document.getElementById("indexList").innerHTML = content;
				$j("#indexList").append(content);
			}
			var childScopeList = response.data.childScopeList;
			//write google map code
			initMap(childScopeList);
		});
	}
	
	function initMap(scope) {     
    	/**if (GBrowserIsCompatible()) {*/    
    	 var myLatlng = new google.maps.LatLng(32.4419,112.1419);  
                var myOptions = {  
                    zoom: 4,  
                    center:myLatlng,  
                    mapTypeId: google.maps.MapTypeId.ROADMAP  
                };     
           map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);  
          //  map= new GMap2(document.getElementById("map_canvas"));        
		
			getAllMarkers(scope); 
		/**}else{
		     alert('Google Map Error!');    
		}*/
	}
    //load scope
   	function getAllMarkers(scope){
		//map.setCenter(new GLatLng(32.4419, 112.1419), 4);
        map.setCenter( new google.maps.LatLng(32.4419,112.1419));
		map.setZoom(4);
        if(scope.length<1){
           return;
        }else{
        	var ico;
        	var shadIco;
        	for(var i=0;i<scope.length;i++){
	        	var scopeTemp = scope[i];
				//create marker
				ico= "images/google_ico/pyramid-southam_blue.png";
				shadIco= "images/google_ico/shadow-pyramid-southam_blue.png";
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
	      // Shapes define the clickable region of the icon.
	      // The type defines an HTML <area> element 'poly' which
	      // traces out a polygon as a series of X,Y points. The final
	      // coordinate closes the poly by connecting to the first
	      // coordinate.
      /**
        var shape = {
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
			var tr=$("scope"+id).childNodes;
			if($("order1").checked){
				tempContent+="<br><font class='smallTitle'><fmt:message key='statistic.information.healthIndex'/></font>";
			}
			if($("order2").checked){
				tempContent+="<br><font class='smallTitle'><fmt:message key='statistic.information.energySavingIndex'/></font>";
			}
			if($("order3").checked){
				tempContent+="<br><font class='smallTitle'><fmt:message key='statistic.information.solveTroubleRate'/></font>";
			}
			tempContent+="<font class='formLabelRequired'>"+tr[2].innerHTML+"</font>";
			//parent.map.openInfoWindow(latlng,tempContent);
			var infowindow = new google.maps.InfoWindow(
			      { content: tempContent,
			        size: new google.maps.Size(50,50)
			      });
			infowindow.open(map,pointMarker);
			//DSInfo = "";
		});
	} 
</script>
<table align="center">
	<tr>
		<td valign="top">
			<div id="map_canvas" class="borderDiv marR marB">
			</div>
		</td>
		<td valign="top">
		<div class="borderDiv marR marB" style="width: 100%;">
			<table >
					<tr><td class="smallTitle" ><fmt:message key="statistic.information.indexOrder" /></td></tr>
					<tr>
						<td>
							<table width="97%" >
								<tr class="rowHeader">
									<td width="20%"><fmt:message key="statistic.information.index.id" /></td>
									<td width="50%"><fmt:message key="statistic.information.index.name" /></td>
									<td width="30%"><fmt:message key="statistic.information.index.value" /></td>
								</tr>							
							</table>
						</td>
					</tr>
					<tr>
						<td>
							<div style="position:relative;width:97%;height:250px;overflow-x:hidden;overflow-y:auto;"  >
								<table width="100%"  >
									<tbody id="indexList"></tbody>
								</table>
							</div>
						</td> 
					</tr>
					<tr><td  class="horzSeparator"></td></tr>					
					<tr>
						<td class="smallTitle" ><fmt:message key="statistic.information.changeCycle" /></td>
					</tr>
					<tr>
						<td>
							<table >
								<tr><td class="formField"><input onclick="executeOrder()" type="radio" name="cycle" value="<c:out value='<%=StatisticsInformationDwr.CYCLE_YESTERDAY%>' />" checked="checked" id="defaultCycle" /><fmt:message key="statistic.information.date.yesterday" /></td></tr>
								<tr><td class="formField"><input onclick="executeOrder()" type="radio" name="cycle" value="<c:out value='<%=StatisticsInformationDwr.CYCLE_WEEK%>' />"  /><fmt:message key="statistic.information.date.lastweek" /></td></tr>
								<tr><td class="formField"><input onclick="executeOrder()" type="radio" name="cycle" value="<c:out value='<%=StatisticsInformationDwr.CYCLE_MONTH%>' />" /><fmt:message key="statistic.information.date.lastmonth" /></td></tr>
								<tr><td class="formField"><input onclick="executeOrder()" type="radio" name="cycle" value="<c:out value='<%=StatisticsInformationDwr.CYCLE_QUARTER%>' />" /><fmt:message key="statistic.information.date.lastquarter" /></td></tr>
								<tr><td class="formField"><input onclick="executeOrder()" type="radio" name="cycle" value="<c:out value='<%=StatisticsInformationDwr.CYCLE_YEAR%>' />" /><fmt:message key="statistic.information.date.lastyear" /></td></tr>
							</table>
						</td>
					</tr>
					<tr><td  class="horzSeparator"></td></tr>
					<tr>
						<td class="smallTitle" ><fmt:message key="statistic.information.changeOrder" /></td>
					</tr>
					<tr>
						<td>
							<table >
								<tr><td class="formField"><input id="order1" onclick="executeOrder()"  type="radio" name="order" value="<c:out value='<%=StatisticsUtil.INDEX_OF_HEALTH%>' />" /><fmt:message key="statistic.information.healthIndex" /></td></tr>
								<tr><td class="formField"><input id="order2" onclick="executeOrder()"  type="radio" name="order" value="<c:out value='<%=StatisticsUtil.ENERGY_SAVING_INDEX%>' />"  checked="checked" /><fmt:message key="statistic.information.energySavingIndex" /></td></tr>
								<tr><td class="formField"><input id="order3" onclick="executeOrder()"  type="radio" name="order" value="<c:out value='<%=StatisticsUtil.RATE_OF_TROUBLE_HANDLE%>' />" /><fmt:message key="statistic.information.solveTroubleRate" /></td></tr>
							</table>
						</td>
					</tr>
			</table>
			</div>
		</td>
		</tr>
	</table>
</tag:page>