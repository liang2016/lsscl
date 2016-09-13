<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp"%>
<%@page import="com.serotonin.mango.Common"%>
<%@page import="com.serotonin.mango.rt.statistic.common.StatisticsUtil"%>
<%@page import="com.serotonin.mango.web.dwr.statistics.StatisticsInformationDwr"%>
<tag:page dwr="StatisticsInformationDwr" onload="init">
<link rel="stylesheet" type="text/css" href="../css/jquery.jqChart.css" />
<script src="../js/jquery.jqChart.min.js" type="text/javascript"></script>
<script type="javascript" type="text/javascript" src="../js/excanvas.js"></script>
<!--[if IE]><script lang="javascript" type="text/javascript" src="js/excanvas.js"></script><![endif]-->
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>  
<script type="text/javascript">
 	var map = null;
	var geocoder = null;
	function init(){
		if(BrowserDetect.browser=="Safari"){
			var screenWidth=document.body.clientWidth;
			var screenHeight=document.body.clientHeight;
			if(screenHeight>screenWidth){
				screenWidth=screenHeight;
			}
			document.getElementById("jqChart").style.width=(screenWidth*0.8)+"px"; 
			document.getElementById("map_canvas").style.width=(screenWidth*0.40)+"px";
			document.getElementById("map_canvas").style.height=(screenWidth*0.4)+"px";
		}
	
		
	
		StatisticsInformationDwr.informationInit(function(response){
			// init the statistics information
			var countOfMachine = response.data.countOfMachine;
			var countOfOnLoadMachine = response.data.countOfOnLoadMachine;
			var countOfUnLoadMachine = response.data.countOfUnLoadMachine;
			var countOfShutdownMachine = response.data.countOfShutdownMachine;
			var countOfShutdownInTroubleMachine = response.data.countOfShutdownInTroubleMachine;
			var healthIndex = response.data.healthIndex;
			var energySavingIndex = response.data.energySavingIndex;
			var solveTroubleRate = response.data.solveTroubleRate;
			$set("count",countOfMachine);
			$set("countOfOnLoad",countOfOnLoadMachine);
			$set("countOfUnload",countOfUnLoadMachine);
			$set("countOfShutdown",countOfShutdownMachine);
			$set("countOfShutdownInTrouble",countOfShutdownInTroubleMachine);
			if(parseInt(healthIndex)==-1)$set("healthIndex","<fmt:message key='statistic.information.nodata'/>");
			else $set("healthIndex",healthIndex.toFixed(2)*100+"%");
			if(parseInt(energySavingIndex)==-1)$set("energySavingIndex","<fmt:message key='statistic.information.nodata'/>");
			else $set("energySavingIndex",energySavingIndex.toFixed(2)*100+"%");
			if(parseInt(solveTroubleRate)==-1)$set("solveTroubleRate","<fmt:message key='statistic.information.nodata'/>");
			else $set("solveTroubleRate",(solveTroubleRate.toFixed(2)*100)+"%");
			// write the table information
			var childScopeList = response.data.childScopeList;
			var childDataList = response.data.childDataList;
			var tableContent = "";
			for(var i=0;i<childScopeList.length;i++){
				var childScope = childScopeList[i];
				tableContent+="<tr class='row' id='scope"+childScope.id+"'>";
				tableContent+="<td  style='white-space:nowrap;' width='20%'>"+childScope.scopename+"</td>";
				var childData = childDataList[i];
				for(var j=0;j<childData.length;j++){
					tableContent+="<td  style='white-space:nowrap;' width='16%'>"+childData[j]+"</td>";
				}				
				tableContent+="</tr>";
			}
			$j("#childList").append(tableContent);
			
			// write the google map
			if(typeof(google)!='undefined'){  
				geocoder = new google.maps.Geocoder();
			}
			else{
				alert("<fmt:message key="google.failed"></fmt:message>");
				return;
			}	
			initMap(childScopeList);
		});
	}	
	 function initMap(scope) {     
	      /** if (GBrowserIsCompatible()) {*/     
	      var myLatlng = new google.maps.LatLng(32.4419,112.1419);  
                var myOptions = {  
                    zoom: 4,  
                    center:myLatlng,  
                    mapTypeId: google.maps.MapTypeId.ROADMAP  
                };  
                map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);    
	           /** map= new GMap2(document.getElementById("map_canvas"));        
	            map.setCenter(new GLatLng(39.9493, 116.3975), 13);  
	           	map.setCenter(new GLatLng(32.4419, 112.1419), 4);
				map.addControl(new GLargeMapControl());
				map.addControl(new GScaleControl());
				map.addControl(new GMapTypeControl());
				map.addControl(new GOverviewMapControl());
				geocoder = new google.maps.ClientGeocoder();*/
				getAllMarkers(scope); 
				/**}
			  	else{
			    	alert('Google Map Error!');    
				}
				*/
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
				ico = "images/google_ico/pyramid-southam_blue.png";
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
		    //map.addOverlay(marker);
	}
	
	// click the mark in google map
	function addListener(pointMarker, latlng, content, id,type) {
		google.maps.event.addListener(pointMarker, "click", function() {
			var tempContent=content;
			var tr=$("scope"+id).childNodes;
			tempContent+="<br><font class='smallTitle'><fmt:message key='statistic.information.countOfMachine'/></font><font class='formLabelRequired'>"+tr[1].innerHTML+"</font>";
			tempContent+="<br><font class='smallTitle'><fmt:message key='statistic.information.countOfOnLoadMachine'/></font><font class='formLabelRequired'>"+tr[2].innerHTML+"</font>";
			tempContent+="<br><font class='smallTitle'><fmt:message key='statistic.information.countOfUnloadMachine' /></font><font class='formLabelRequired'>"+tr[3].innerHTML+"</font>";
			tempContent+="<br><font class='smallTitle'><fmt:message key='statistic.information.countOfShutdownMachine'/></font><font class='formLabelRequired'>"+tr[4].innerHTML+"</font>";
			tempContent+="<br><font class='smallTitle'><fmt:message key='statistic.information.countOfShutdownInTroubleMachine'/></font><font class='formLabelRequired'>"+tr[5].innerHTML+"</font>";
			var infowindow = new google.maps.InfoWindow(
			      { content: tempContent,
			        size: new google.maps.Size(50,50)
			      });
			infowindow.open(map,pointMarker);
			//parent.map.openInfoWindow(latlng,tempContent);
			//DSInfo = "";
		});
	} 

	//produce ImageChart for history data
	function getImageChartByScript(scriptId,scriptName){
		$set("scriptName",scriptName);
		$set("script",scriptId);
		getImageChart(scriptId,$get("cycle"),scriptName);
	}
	
	function getImageByCycle(cycle){
		if($get("script")!=null){
			getImageChart($get("script"),cycle,$j("#scriptName").val());
		}else{
			alert("unknow error!");
		}
	}
	
	function getImageChart(scriptId,cycle,scriptName){
		StatisticsInformationDwr.getHistoryData(scriptId,cycle,function(data){
			if(data==null){
				alert("<fmt:message key='statistic.information.nodata'/>");
				return;
			}
			$j("#chart").show();
			var points = new Array();
			for(var i =0;i<data.length;i++){
				var obj = data[i];
				var date = new Date(obj[0]);
				var point = new Array();
				point.push(date);
				point.push(obj[1]*100);
				points.push(point);
			} 
			$j('#jqChart').jqChart({
                axes: [{
                         name: 'y2',
                         location: 'right',
                         strokeStyle: '#FCB441',
                         majorGridLines: { strokeStyle: '#FCB441' },
                         majorTickMarks: { strokeStyle: '#FCB441' }
                      }],
                series: [{
                         type: 'line',
                         axisY: 'y2',
                         title: scriptName+"(%)",
                         data: points
                        }]
            }); 
	  	});
	}
</script>
<table align="center">
	<tr>
		<td valign="top" >
			<div id="map_canvas" class="borderDiv marR marB">
			</div>
		</td>
		<td valign="top">
			<div class="borderDiv marR marB">
				<table id="machineStatus" >
					<thead>
						<tr><td colspan="3" class="smallTitle" ><fmt:message key="statistic.information.machine.information" /><tag:help id="machineStatusInfo"/></td></tr>
					</thead>
					<tbody>
						<tr>
							<td class="formLabelRequired"><fmt:message key="statistic.information.countOfMachine" />:</td>
							<td class="formField"><input id="count" name="count" readonly="readonly"  />&nbsp;<fmt:message key="common.quantifier.tai" /></td>
						</tr>
						<tr>
							<td class="formLabelRequired"><fmt:message key="statistic.information.countOfOnLoadMachine" />:</td>
							<td class="formField"><input id="countOfOnLoad" name="countOfOnLoad" readonly="readonly"  />&nbsp;<fmt:message key="common.quantifier.tai" /></td>
						</tr>
						<tr>
							<td class="formLabelRequired"><fmt:message key="statistic.information.countOfUnloadMachine" />:</td>
							<td class="formField"><input id="countOfUnload" name="countOfUnload" readonly="readonly"  />&nbsp;<fmt:message key="common.quantifier.tai" /></td>
						</tr>
						<tr>
							<td class="formLabelRequired"><fmt:message key="statistic.information.countOfShutdownMachine" />:</td>
							<td class="formField"><input id="countOfShutdown" name="countOfShutdown" readonly="readonly"  />&nbsp;<fmt:message key="common.quantifier.tai" /></td>
						</tr>
						<tr>
							<td class="formLabelRequired"><fmt:message key="statistic.information.countOfShutdownInTroubleMachine" />:</td>
							<td class="formField"><input id="countOfShutdownInTrouble" name="countOfShutdownInTrouble" readonly="readonly"  />&nbsp;<fmt:message key="common.quantifier.tai" /></td>
						</tr>
						<tr><td colspan="3" class="horzSeparator"></td></tr>
						<tr>
<!--						<td colspan="3" class="smallTitle" ><fmt:message key="statistic.information.machine.lastWeek.index" /></td>-->
							<td colspan="3" class="smallTitle" ><fmt:message key="statistic.information.machine.yesterday.index" /></td>
						</tr>
						<tr>
							<td class="formLabelRequired"><fmt:message key="statistic.information.healthIndex" />:</td>
							<td class="formField"><input id="healthIndex" name="healthIndex" readonly="readonly"  />&nbsp;<input type="button" onclick="getImageChartByScript(<c:out value='<%=StatisticsUtil.INDEX_OF_HEALTH%>' />,'<fmt:message key='statistic.information.healthIndex' />')" value="<fmt:message key='statistic.information.historyCurve' />" ></td>
						</tr>
						<tr>
							<td class="formLabelRequired"><fmt:message key="statistic.information.energySavingIndex" />:</td>
							<td class="formField"><input id="energySavingIndex" name="energySavingIndex" readonly="readonly"  />&nbsp;<input type="button" onclick="getImageChartByScript(<c:out value='<%=StatisticsUtil.ENERGY_SAVING_INDEX%>' />,'<fmt:message key='statistic.information.energySavingIndex' />')" value="<fmt:message key='statistic.information.historyCurve' />" ></td>
						</tr>
						<tr>
							<td class="formLabelRequired"><fmt:message key="statistic.information.solveTroubleRate" />:</td>
							<td class="formField"><input id="solveTroubleRate" name="solveTroubleRate" readonly="readonly"  />&nbsp;<input type="button" onclick="getImageChartByScript(<c:out value='<%=StatisticsUtil.RATE_OF_TROUBLE_HANDLE%>' />,'<fmt:message key='statistic.information.solveTroubleRate' />')" value="<fmt:message key='statistic.information.historyCurve' />" ></td>
						</tr>
						<tr><td colspan="3" class="horzSeparator"></td></tr>
						<tr class="smallTitle">
							<td>
								<c:if test="${sessionUser.currentScope.scopetype==2}">
								<fmt:message key="common.scope.type.factory" />
								</c:if>
								<c:if test="${sessionUser.currentScope.scopetype==1}">
								<fmt:message key="common.scope.type.subzone" />
								</c:if>
								<c:if test="${sessionUser.currentScope.scopetype==0}">
								<fmt:message key="common.scope.type.zone" />
								</c:if>
								<fmt:message key="common.list" />
							</td>					
						</tr>
						<tr>
							<td  colspan="3" >
								<table width="100%">
									<tr class="rowHeader">
											<td style="white-space:nowrap;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<fmt:message key="common.name" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
											<td style="white-space:nowrap;">&nbsp;<fmt:message key="statistic.information.count" />&nbsp;</td>
											<td style="white-space:nowrap;"><fmt:message key="statistic.information.countOfOnLoad" /></td>
											<td style="white-space:nowrap;"><fmt:message key="statistic.information.countOfUnload" /></td>
											<td style="white-space:nowrap;"><fmt:message key="statistic.information.countOfShutdown" /></td>
											<td style="white-space:nowrap;"><fmt:message key="statistic.information.countOfShutdownInTrouble" /></td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td colspan="3" >
								<div style="position:relative;width:auto; height:150px;overflow-x:hidden;overflow-y:auto;"  >
									<table width="100%">
										<tbody id="childList"></tbody>
									</table>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</td>
	</tr>
	<tr>
		<td colspan="2" id="chart" align="center" style="display: none;" >
			<table style="position: relative;width: 100%">
				<tr>
					<td align="center"  class="formField">
						<input type="hidden" id="scriptName" />
						<span class="smallTitle" ><fmt:message key="statistic.information.changeCycle" /><input type="hidden" id="script" value="-1" /></span>
						<input type="radio" name="cycle" value="<c:out value='<%=StatisticsInformationDwr.CYCLE_YESTERDAY%>' />" onclick="getImageByCycle(this.value)"  checked="checked" /><fmt:message key="statistic.information.date.yesterday" />
						<input type="radio" name="cycle" value="<c:out value='<%=StatisticsInformationDwr.CYCLE_WEEK%>' />" onclick="getImageByCycle(this.value)" /><fmt:message key="statistic.information.date.lastweek" />
						<input type="radio" name="cycle" value="<c:out value='<%=StatisticsInformationDwr.CYCLE_MONTH%>' />" onclick="getImageByCycle(this.value)"  /><fmt:message key="statistic.information.date.lastmonth" />
						<input type="radio" name="cycle" value="<c:out value='<%=StatisticsInformationDwr.CYCLE_QUARTER%>'/>" onclick="getImageByCycle(this.value)"  /><fmt:message key="statistic.information.date.lastquarter" />
					</td>
				</tr>
				<tr>
					<td>
						<div>
							<div id="jqChart"></div>
						</div>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</tag:page>