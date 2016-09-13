<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@include file="/WEB-INF/tags/decl.tagf"%>
<%@attribute name="styles" fragment="true" %>
<%@attribute name="dwr" %>
<%@attribute name="js" %>
<%@attribute name="onload" %>

<html>
<head>
  <title><c:choose>
    <c:when test="${!empty instanceDescription}">${instanceDescription}</c:when>
    <c:otherwise><fmt:message key="header.title"/></c:otherwise>
  </c:choose></title>
  
  <meta http-equiv="pragma" content="no-cache"/>
  <meta http-equiv="cache-control" content="no-cache"/>
  <meta http-equiv="expires" content="0"/>
  
  <!-- Meta -->
  <meta http-equiv="content-type" content="application/xhtml+xml;charset=utf-8"/>
  <meta http-equiv="Content-Style-Type" content="text/css" />
  <meta name="Copyright" content="&copy;2010-2013 lsscl Technologies Inc."/>
  <meta name="DESCRIPTION" content="<fmt:message key='common.description'/>"/>
  <meta name="KEYWORDS" content="<fmt:message key='common.keyWords'/>"/>
  
  <!-- Style -->
  <link rel="icon" href="images/favicon.ico"/>
  <link rel="shortcut icon" href="images/favicon.ico"/>
  <link href="resources/common.css" type="text/css" rel="stylesheet"/>
  <link href="css/dd.css" type="text/css" rel="stylesheet"/>
  <jsp:invoke fragment="styles"/>
  
  <!-- Scripts -->
  <script type="text/javascript">var djConfig = { isDebug: false, extraLocale: ['en-us', 'nl', 'nl-nl', 'ja-jp', 'fi-fi', 'sv-se', 'zh-cn', 'zh-tw','xx'] };</script>
  <!-- script type="text/javascript" src="http://o.aolcdn.com/dojo/0.4.2/dojo.js"></script -->
  <script type="text/javascript" src="resources/dojo/dojo.js"></script>
  <script type="text/javascript" src="dwr/engine.js"></script>
  <script type="text/javascript" src="dwr/util.js"></script>
  <script type="text/javascript" src="dwr/interface/MiscDwr.js"></script>
  <script type="text/javascript" src="resources/soundmanager2-nodebug-jsmin.js"></script>
  <script type="text/javascript" src="resources/common.js"></script>
  <c:forEach items="${dwr}" var="dwrname">
    <script type="text/javascript" src="dwr/interface/${dwrname}.js"></script></c:forEach>
  <c:forEach items="${js}" var="jsname">
    <script type="text/javascript" src="resources/${jsname}.js"></script></c:forEach>
  <script type="text/javascript">
    mango.i18n = <sst:convert obj="${clientSideMessages}"/>;
  </script>
  <script type="text/javascript" src="/js/jquery-1.5.1.min.js"></script>
  <script type="text/javascript" src="/msdropdown/uncompressed.jquery.dd.js"></script>
  <script>var $j = jQuery.noConflict(); </script>
  <c:if test="${!simple}">
    <script type="text/javascript" src="resources/header.js"></script>
    <script type="text/javascript">
      dwr.util.setEscapeHtml(false);
    
      <c:if test="${!empty sessionUser}">
        dojo.addOnLoad(mango.header.onLoad);
        dojo.addOnLoad(function() { setUserMuted('${sessionUser.muted}');});
		dojo.addOnLoad(initZonePage);
		
		var zoneid = "";
		var subzoneid = "";
		var factoryid= "";
		
		function setvalue(){
			var scopetype = '${sessionUser.currentScope.scopetype}';
			switch (scopetype) {
				case '0' :
					break;
				case '1' :
					zoneid = '${sessionUser.currentScope.id}';
					break;
				case '2' : 
					subzoneid = '${sessionUser.currentScope.id}';
					zoneid = '${sessionUser.currentScope.parentScope.id}';
					break;
				case '3' :
					factoryid = '${sessionUser.currentScope.id}';
					subzoneid = '${sessionUser.currentScope.parentScope.id}';
					zoneid = '${sessionUser.currentScope.grandParent.id}';
					break;
			};
		}
			
		
		function initZonePage(){			
			var homescope = '${sessionUser.homeScope.scopetype}';	
			switch (homescope) {
				case '0':
					$j("#zoneManagerHome").show();
					$j("#subzoneManagerHome").show();
					$j("#factoryManagerHome").show();
					setvalue();
					getList(-1,1,"zoneManagerHome");
				break;
				case '1':
					$j("#zoneManagerHome").hide();
					$j("#subzoneManagerHome").show();
					$j("#factoryManagerHome").show();
					setvalue();
					getList(zoneid,2,"subzoneManagerHome");
				break;
				case '2':
					$j("#zoneManagerHome").hide();
					$j("#subzoneManagerHome").hide();
					$j("#factoryManagerHome").show();
					setvalue();
					getList(subzoneid,3,"factoryManagerHome");
				break;
				case '3':
					$j("#zoneManagerHome").hide();
					$j("#subzoneManagerHome").hide();
					$j("#factoryManagerHome").hide();
					setvalue();
				break;		
			};
		
			$j("#zoneManagerHome").change(function(){
				var parentId = $j("#zoneManagerHome").val();
				makeMsDropDown($j("#zoneManagerHome"));
				getList(parentId,2,"subzoneManagerHome");
			});
			
			$j("#subzoneManagerHome").change(function(){
				makeMsDropDown($j("#subzoneManagerHome"));
				var parentId = $j("#subzoneManagerHome").val();
				getList(parentId,3,"factoryManagerHome");
			});
		};		
		
		function clearSelect(id){
			var option = "<option title='images/google_ico/arrow_right.png'></option>";
			var textvalue = "";
			var node = null;
			switch (id) {
				case "zoneManagerHome" :
					node = $j("#zoneManagerHome");
					textvalue = '<fmt:message key="header.scope.zonemanagerhome"/>';
				break;
				case "subzoneManagerHome" :
					node = $j("#subzoneManagerHome");
					textvalue = '<fmt:message key="header.scope.subzonemanagerhome"/>';
				break;
				case "factoryManagerHome" :
					node = $j("#factoryManagerHome");
					textvalue = '<fmt:message key="herder.scope.factorymanagerhome"/>';
				break;
			};
			node.empty();
			$j(""+ option)
                .val("")
                .text(textvalue)
                .appendTo(node);
		}
		
		function makeMsDropDown(node){
			try {
				node.msDropDown().data("dd");
			} catch(e) {
					alert("Error: "+e.message);
			};
		}
		
		function getList(parentId,scope,id){
			var option = "<option title='images/google_ico/arrow_right.png'></option>";
			//var homescope = '${sessionUser.homeScope.scopetype}';
			var scopecur = scope ;
			var parentIdcur = parentId ;
			
			var node = null;
			switch (id) {
				case "zoneManagerHome" :
					node = $j("#zoneManagerHome");
				break;
				case "subzoneManagerHome" :
					node = $j("#subzoneManagerHome");
				break;
				case "factoryManagerHome" :
					node = $j("#factoryManagerHome");
				break;
			};			
			//var selectid = id;
			MiscDwr.getScopeListByType(parentIdcur,scopecur,function(response){
					data=response.data.scopeList;
					//var scopetype = '${sessionUser.currentScope.scopetype}';
					switch (id) {
						case "zoneManagerHome" :
							clearSelect("zoneManagerHome");
							clearSelect("subzoneManagerHome");
							clearSelect("factoryManagerHome");
						break;
						case "subzoneManagerHome" :
							clearSelect("subzoneManagerHome");
							clearSelect("factoryManagerHome");
						break;
						case "factoryManagerHome" :
							clearSelect("factoryManagerHome");
						break;
					};
					if(data==null||data.length==0||$j("#" && id) == null){
					}else{
						for(var i=0;i<data.length;i++){
							var scope = data[i];
		                	$j(""+ option)
		                   	 .val(scope.id)
		                   	 .text(scope.scopename)
		                  	  .appendTo(node);
						};
					};
					
					switch (id) {
						case "zoneManagerHome" :
							$j("#zoneManagerHome").val(zoneid);
							$j("#zoneManagerHome").change();
							makeMsDropDown($j("#zoneManagerHome"));
							makeMsDropDown($j("#subzoneManagerHome"));
							makeMsDropDown($j("#factoryManagerHome"));
						break;
						case "subzoneManagerHome" :
							$j("#subzoneManagerHome").val(subzoneid);
							$j("#subzoneManagerHome").change();
							makeMsDropDown($j("#subzoneManagerHome"));
							makeMsDropDown($j("#factoryManagerHome"));
						break;
						case "factoryManagerHome" :
							$j("#factoryManagerHome").val(factoryid);
							makeMsDropDown($j("#factoryManagerHome"));
						break;
					};
				});
		}
		
		function gotoPage(emid,val){
			var homescopeid = '${sessionUser.homeScope.id}';
			var homescope = '${sessionUser.homeScope.scopetype}';		
			switch(emid) {
				case "zoneManagerHome":
					if (val) {
						window.location.href="subzone_list.shtm?zoneId="+val;
					}else {
						window.location.href="zone_list.shtm";
					}
				break;
				case "subzoneManagerHome":
					if (val) {
						window.location.href="factory_list.shtm?subzoneId="+val;
					}else if ($j("#zoneManagerHome").val()) {
						window.location.href="subzone_list.shtm?zoneId=" + $j("#zoneManagerHome").val();
					}else {
						if (homescope == '0') {
							window.location.href="zone_list.shtm";
						}else {
							window.location.href="subzone_list.shtm?zoneId=" + homescopeid;
						}
					}
				break;
				case "factoryManagerHome":
					if (val) {
						window.location.href="watch_list.shtm?factoryId="+val;
					}else if ($j("#subzoneManagerHome").val()) {
						window.location.href="factory_list.shtm?subzoneId="+$j("#subzoneManagerHome").val();
					}else if ($j("#zoneManagerHome").val()) {
						window.location.href="subzone_list.shtm?zoneId=" + $j("#zoneManagerHome").val();
					} else {
						window.location.href="zone_list.shtm";
						
						if (homescope == '0') {
							window.location.href="zone_list.shtm";
						}else if (homescope == '1'){
							window.location.href="subzone_list.shtm?zoneId=" + homescopeid;
						}else if (homescope == '2'){
							window.location.href="factory_list.shtm?subzoneId="+homescopeid;
						}else {
							window.location.href="watch_list.shtm?factoryId="+homescopeid;
						}
					}
				break;
			};
			
		}
		
     	function changeCursorStyle(scope){
			$j(scope).css("cursor","pointer");
		}
		
		function mousePosition(ev){
			if(ev.pageX || ev.pageY){
		    	return {x:ev.pageX, y:ev.pageY};
		    }
		    return {
		       x:ev.clientX + document.body.scrollLeft - document.body.clientLeft,
		       y:ev.clientY + document.body.scrollTop  - document.body.clientTop
		    };
		 } 

		function mouseMove(ev){
		    ev = ev || window.event;
		    var mousePos = mousePosition(ev);
		    $j("#header_scopelist").css("position","absolute");
		    $j("#header_scopelist").css("background","#29cf4c");
		    $j("#header_scopelist").css("left",(mousePos.x-15)+"px");
		    $j("#header_scopelist").css("top",(mousePos.y+2)+"px");
		}

		function scopeSelector(event,scopetype){

			$j("#selector").mouseleave(function(){
				$j("#selector").hide();
			});
		
			mouseMove(event);
			$j("#header_scopelist").html("");
			if(scopetype==0){
				window.location.href="/login.htm";
			}
		}
			
      </c:if>
      
      function setLocale(locale) {
          MiscDwr.setLocale(locale, function() { window.location = window.location });
      }
      
      function setHomeUrl() {
          MiscDwr.setHomeUrl(window.location.href, function() { alert("<fmt:message key='comm.home.url.saved' />"); });
      }
      
      function goHomeUrl() {
          MiscDwr.getHomeUrl(function(loc) { window.location = loc; });
      }
      
      function setHeadWidth(){
		var screenHeight=document.body.clientWidth;
		var screenWidth=document.body.clientHeight;

		if(screenHeight>screenWidth){
			screenWidth=screenHeight;
		}
		
		if(typeof(map_canvas)!='undefined'){
		 	BrowserDetect.init();
			screenHeight=window.screen.height;
			screenWidth=window.screen.width;
			if(screenHeight>screenWidth){
				screenWidth=screenHeight;
			}
			if(BrowserDetect.browser=="Safari"&&BrowserDetect.OS=="iPhone/iPod")
				screenWidth+=screenWidth-120;
			document.getElementById("map_canvas").style.width=(screenWidth*0.4)+"px";
			document.getElementById("map_canvas").style.height=(screenWidth*0.4)+"px";
		}
	}
	$j(document).ready(function(){
	     setHeadWidth();
	});
    </script>
  </c:if>
  
</head>
<body>
 <c:if test="${!empty sessionUser}">
<table width="100%"  cellspacing="0" cellpadding="0" border="0" id="mainHeader">
  <tr style="height: 48px">
    <c:if test="${!empty sessionUser}">
  		<td rowspan="1" width="20%" style="padding: 0px 0px 0px 0px;background-color: #6ba53a;">
  		<img src="${sessionUser.currentScope.backgroundFilename}" id="imgLogo" style="top:0px;left:0px;position:absolute;height: 65px;padding: 0px 0px 10px 0px;" alt="Logo"/></td>
      <td align="center" width="60%"  style="background-color: #6ba53a;">
        <c:if test="${sessionUser.currentScope.scopetype==0}"><a href="center_events.shtm"></c:if>
        <c:if test="${sessionUser.currentScope.scopetype==1}"><a href="zone_events.shtm?zoneId=${sessionUser.currentScope.id}"></c:if>
        <c:if test="${sessionUser.currentScope.scopetype==2}"><a href="subzone_events.shtm?subzoneId=${sessionUser.currentScope.id}"></c:if>
        <c:if test="${sessionUser.currentScope.scopetype==3}"><a href="factory_events.shtm?factoryId=${sessionUser.currentScope.id}"></c:if>
          <span id="__header__alarmLevelDiv" style="display:none;">
            <img id="__header__alarmLevelImg" src="images/spacer.gif" alt="" border="0" title=""/>
            <span style="color:#FFFFFF;" id="__header__alarmLevelText"></span>
          </span>
        </a>
      </td>
    </c:if>
    <c:if test="${!empty sessionUser}">
    	<td align="right" width="20%"  class="copyTitle" style=" white-space: nowrap; background-color: #6ba53a;">
    	  <span><fmt:message key="header.user"/>: <b>${sessionUser.username}</b></span>
          <span ><fmt:message key="header.scope.currentScop"/>: <b>${sessionUser.currentScope.scopename}</b></span>
          <tag:img id="userMutedImg" onclick="MiscDwr.toggleUserMuted(setUserMuted)" onmouseover="hideLayer('localeEdit')"/>
          <tag:img png="house" title="header.goHomeUrl" onclick="goHomeUrl()" onmouseover="hideLayer('localeEdit')"/>
          <tag:img png="house_link" title="header.setHomeUrl" onclick="setHomeUrl()" onmouseover="hideLayer('localeEdit')"/>
    	     <div style="display:inline;" onmouseover="showMenu('localeEdit', -40, 10);">
          <tag:img png="world" title="header.changeLanguage"/>
          <div id="localeEdit" style="visibility:hidden;left:0px;top:15px;" class="labelDiv" onmouseout="hideLayer(this)">
            <c:forEach items="${availableLanguages}" var="lang">
              <a class="ptr" onclick="setLocale('${lang.key}')">${lang.value}</a><br/>
            </c:forEach>
          </div>
        </div>
    	</td>
    </c:if>
 <!--    <c:if test="${!empty instanceDescription}">
      <td align="right" valign="bottom" class="copyTitle" style="padding:5px; white-space: nowrap;">${instanceDescription}</td>
    </c:if>
    
    --> 
  </tr>
  <tr style="height: 25px;">
  	<td rowspan="1" width="20%" style="padding: 0px 0px 0px 0px;">
  		<img src="logos/4_hid.png" id="imgLogohid" style="padding: 0px 0px 10px 0px;"/></td>
  	<c:if test="${!simple}">
  	 <td style="cursor:default;" align="center">
        <c:if test="${!empty sessionUser}">
        <c:if test="${sessionUser.currentScope.scopetype==0}">
        <tag:menuItem href="zone_list.shtm" png="scope/zoneList" key="header.zoneList"/>
          <tag:menuItem href="center_factory_search.shtm" png="scope/factorySearch" key="header.factorySearch"/>
       <!--    <tag:menuItem href="center_statistics_event.shtm" png="alert" key="header.alertSearch"/>
          <tag:menuItem href="center_statisticsPotentialSearch.shtm" png="potentialSearch" key="header.potentialSearch"/>
          <tag:menuItem href="center_statisticsInformation.shtm" png="statisticsInformation" key="header.statisticsInformation"/>
          <tag:menuItem href="center_statisticsIndexOrder.shtm" png="statisticsIndexOrder" key="header.statisticsIndexOrder"/>
           -->
          <tag:menuItem href="center_users.shtm" png="userpng" key="header.centerUser"/>
          
        </c:if>
        <c:if test="${sessionUser.currentScope.scopetype==1}">
          <tag:menuItem href="subzone_list.shtm?zoneId=${sessionUser.currentScope.id}" png="scope/subZone" key="header.subZoneList"/>
		  <tag:menuItem href="zone_factory_search.shtm?zoneId=${sessionUser.currentScope.id}" png="scope/factorySearch" key="header.factorySearch"/>
		 <!--  <tag:menuItem href="zone_statistics_event.shtm?zoneId=${sessionUser.currentScope.id}" png="alert" key="header.alertSearch"/>
          <tag:menuItem href="zone_statisticsPotentialSearch.shtm?zoneId=${sessionUser.currentScope.id}" png="potentialSearch" key="header.potentialSearch"/>
          <tag:menuItem href="zone_statisticsInformation.shtm?zoneId=${sessionUser.currentScope.id}" png="statisticsInformation" key="header.statisticsInformation"/>
          <tag:menuItem href="zone_statisticsIndexOrder.shtm?zoneId=${sessionUser.currentScope.id}" png="statisticsIndexOrder" key="header.statisticsIndexOrder"/>
         --> <c:if test="${sessionUser.admin||sessionUser.tempAdmin||sessionUser.homeScope.scopetype==1}">
          	<tag:menuItem href="zone_users.shtm?zoneId=${sessionUser.currentScope.id}" png="userpng" key="header.zoneUser"/>
          </c:if>
        </c:if>
        <c:if test="${sessionUser.currentScope.scopetype==2}">
        <tag:menuItem href="factory_list.shtm?subzoneId=${sessionUser.currentScope.id}" png="scope/factorySearchnew" key="header.factoryList"/>
         <tag:menuItem href="subzone_factory_search.shtm?subzoneId=${sessionUser.currentScope.id}" png="scope/factorySearch" key="header.factorySearch"/>
 		  <!-- <tag:menuItem href="subzone_statistics_event.shtm?subzoneId=${sessionUser.currentScope.id}" png="alert" key="header.alertSearch"/>
          <tag:menuItem href="subzone_statisticsPotentialSearch.shtm?subzoneId=${sessionUser.currentScope.id}" png="potentialSearch" key="header.potentialSearch"/>
          <tag:menuItem href="subzone_statisticsInformation.shtm?subzoneId=${sessionUser.currentScope.id}" png="statisticsInformation" key="header.statisticsInformation"/>
          <tag:menuItem href="subzone_statisticsIndexOrder.shtm?subzoneId=${sessionUser.currentScope.id}" png="statisticsIndexOrder" key="header.statisticsIndexOrder"/>
       	 	 --> <c:if test="${sessionUser.admin||sessionUser.tempAdmin||sessionUser.homeScope.scopetype==2}">
         	 	<tag:menuItem href="subzone_users.shtm?subzoneId=${sessionUser.currentScope.id}" png="userpng" key="header.users"/>
        	 </c:if>
        </c:if>
         <c:if test="${sessionUser.currentScope.scopetype==3}">
          <tag:menuItem href="watch_list.shtm?factoryId=${sessionUser.currentScope.id}" png="eye" key="header.watchlist"/>
          <!--<tag:menuItem href="dynamicData.shtm?factoryId=${sessionUser.currentScope.id}" png="action_log" key="header.realtimeData" />-->
          <tag:menuItem href="views.shtm?factoryId=${sessionUser.currentScope.id}" png="icon_viewnew" key="header.views"/>
         <!--<tag:menuItem href="lssclMap.shtm?factoryId=${sessionUser.currentScope.id}" png="google_ico/earth" key="header.factoryList" />--> 
           <c:if test="${sessionUser.admin||sessionUser.tempAdmin||sessionUser.homeScope.scopetype==3}">
           		 <tag:menuItem href="factory_users.shtm?factoryId=${sessionUser.currentScope.id}" png="userpng" key="header.users"/>
           </c:if>		      
          <tag:menuItem href="factory_events.shtm?factoryId=${sessionUser.currentScope.id}" png="flag_whitenew" key="header.alarms"/>
          	<c:if test="${sessionUser.defaultRole.id==1}">
          		<tag:menuItem href="reports.shtm?factoryId=${sessionUser.currentScope.id}" png="reportnew" key="header.reports"/>
          	</c:if>
          </c:if> 
          <c:if test="${sessionUser.dataSourcePermission||sessionUser.tempAdmin}">
           <c:if test="${sessionUser.currentScope.scopetype==3}">
         	 <c:if test="${sessionUser.defaultRole.id==1||sessionUser.defaultRole.id==3||sessionUser.defaultRole.id==5||sessionUser.tempAdmin}">
            <tag:menuItem href="data_sources.shtm?factoryId=${sessionUser.currentScope.id}" png="icon_dsnew" key="header.dataSources"/>
        	<tag:menuItem href="scheduled_events.shtm?factoryId=${sessionUser.currentScope.id}" png="clocknew" key="header.scheduledEvents"/>
            <tag:menuItem href="compound_events.shtm?factoryId=${sessionUser.currentScope.id}" png="multi_bellnew" key="header.compoundEvents"/>
            <tag:menuItem href="point_links.shtm?factoryId=${sessionUser.currentScope.id}" png="linknew" key="header.pointLinks"/>
        	  </c:if>
          </c:if>
          </c:if>
          
        
          
          <c:if test="${sessionUser.admin||sessionUser.tempAdmin}">
            <c:if test="${sessionUser.currentScope.scopetype==0}">
            	<tag:menuItem href="statistics_config.shtm" png="scope/statistics" key="header.statistics"/>
           		<tag:menuItem href="center_system_settings.shtm" png="scope/systemnew" key="header.centerSystemSetting"/>
           		<tag:menuItem href="zone_edit.shtm" png="scope/edit_diff" key="header.zoneEdit"/>
           		<tag:menuItem href="acp_type_attr.shtm" png="scope/acpAttrnew" key="header.acpAttr"/>
            </c:if>
           <c:if test="${sessionUser.currentScope.scopetype==1}">
          		<tag:menuItem href="zone_system_settings.shtm?zoneId=${sessionUser.currentScope.id}" png="scope/system" key="header.zoneSystemSetting"/>
          		<tag:menuItem href="subzone_edit.shtm?zoneId=${sessionUser.currentScope.id}" png="scope/edit_diff" key="header.subZoneEdit"/>
           </c:if>
           <c:if test="${sessionUser.currentScope.scopetype==2}">
          <tag:menuItem href="subzone_system_settings.shtm?subzoneId=${sessionUser.currentScope.id}" png="scope/system" key="header.zoneSystemSetting"/>
          <tag:menuItem href="factory_edit.shtm?subzoneId=${sessionUser.currentScope.id}" png="scope/factoryedit" key="header.factoryEdit"/>
         	</c:if>
       	    <c:if test="${sessionUser.currentScope.scopetype==3}">
       	    <tag:menuItem href="event_handlers.shtm?factoryId=${sessionUser.currentScope.id}" png="cognew" key="header.eventHandlers"/>
		<c:if test="${sessionUser.defaultRole.id==1||sessionUser.defaultRole.id==3||sessionUser.defaultRole.id==5}">
			<tag:menuItem href="compress_air_system.shtm?factoryId=${sessionUser.currentScope.id}" png="acpSettingnew" key="header.acpSetting"/>
		        <tag:menuItem href="point_hierarchy.shtm?factoryId=${sessionUser.currentScope.id}" png="folder_bricknew" key="header.pointHierarchy"/>
		</c:if>
            <tag:menuItem href="mailing_lists.shtm?factoryId=${sessionUser.currentScope.id}" png="booknew" key="header.mailingLists"/>
            <c:if test="false" >
	            <tag:menuItem href="publishers.shtm" png="transmit" key="header.publishers"/>
            </c:if>
          <!-- <tag:menuItem href="maintenance_events.shtm" png="hammer" key="header.maintenanceEvents"/> -->  
            <tag:menuItem href="factory_system_settings.shtm?factoryId=${sessionUser.currentScope.id}" png="scope/system" key="header.factory_setting"/>
            	<c:if test="${sessionUser.defaultRole.id==1||sessionUser.defaultRole.id==2||sessionUser.defaultRole.id==3||sessionUser.defaultRole.id==5}">
            		<tag:menuItem href="appsetting.shtm?factoryId=${sessionUser.currentScope.id}" png="scope/systemnew" key="header.factory_appSetting"/>
            	</c:if>
            </c:if>
          </c:if>
          
          <tag:menuItem href="logout.htm" png="control_stop_blue" key="header.logout"/>
        </c:if>
      <!--  <c:if test="${empty sessionUser}">
          <tag:menuItem href="login.htm" png="control_play_blue" key="header.login"/>
        </c:if>
         --> 
        <div id="headerMenuDescription" class="labelDiv" style="position:absolute;display:none;"></div>
      </td>
       <td align="right" style="padding:5px; white-space: nowrap;">
	    <c:if test="${!empty sessionUser}">
			<input type="hidden" id="header_zoneId"  value="<c:if test="${sessionUser.homeScope.scopetype==1}">${sessionUser.homeScope.id}</c:if><c:if test="${sessionUser.homeScope.scopetype!=1}" >-1</c:if>" />
			<input type="hidden" id="header_subzoneId"  value="<c:if test="${sessionUser.homeScope.scopetype==2}">${sessionUser.homeScope.id}</c:if><c:if test="${sessionUser.homeScope.scopetype!=2}" >-1</c:if>" />	    	
	    	<table>
	    		<tr>
	    			<td>
					    <span style="font-size: 11px;color: green;" id="myManagerHome"  onmouseover="changeCursorStyle(this)" onclick="scopeSelector(event,0)" ><fmt:message key="header.scope.mymanagerhome"/></span>
	    			</td>
	    			<td>
				    	<!-- <c:if test="${sessionUser.homeScope.scopetype==0}"> -->
				    		<select id="zoneManagerHome" name="zoneManagerHome" tabindex="2" style="display:none"></select>
				      	<!-- <span style="font-size: 11px;color: green;"  id="zoneManagerHome"  onmouseover="changeCursorStyle(this)" onclick="scopeSelector(event,1)" ><fmt:message key="header.scope.zonemanagerhome"/></span>&nbsp;&nbsp;&nbsp;&nbsp; -->
				      	<!-- </c:if> -->
	    			</td>
	    			<td>
				      	<!-- <c:if test="${sessionUser.homeScope.scopetype<=1}"> -->
				      		<select id="subzoneManagerHome" name="subzoneManagerHome" tabindex="3" style="display:none"></select>
				      	<!-- <span style="font-size: 11px;color: green;"  id="subzoneManagerHome"  onmouseover="changeCursorStyle(this)" onclick="scopeSelector(event,2)" ><fmt:message key="header.scope.subzonemanagerhome"/></span>&nbsp;&nbsp;&nbsp;&nbsp; -->
				      	<!-- </c:if> -->
	    			</td>
	    			<td>
				      	<!-- <c:if test="${sessionUser.homeScope.scopetype<=2}"> -->
				      		<select id="factoryManagerHome" name="factoryManagerHome" tabindex="4" style="display:none"></select> 
				      	<!--<span style="font-size: 11px;color: green;"  id="factoryManagerHome"  onmouseover="changeCursorStyle(this)" onclick="scopeSelector(event,3)" ><fmt:message key="herder.scope.factorymanagerhome"/></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -->
				      	<!-- </c:if> -->
	    			</td>	    		
	    		</tr>
	      	</table>
        </c:if>
   
      </td>
      </c:if>
  </tr>
</table> 
</c:if>
<div>
  <jsp:doBody/>
</div>

<c:if test="${!empty onload}">
  <script type="text/javascript">dojo.addOnLoad(${onload});</script>
</c:if>
<div id="selector"  style="font-size: 11px;color:#FFFFFF;width:150px;height:10px;overflow-x:auto;overflow-y:auto;cursor:pointer;z-index=10;" >
	<table>
		<tbody style="font-size: 11px;color:#FFFFFF;" id="header_scopelist">
		</tbody>
	</table>
</div>
</body>
</html> 