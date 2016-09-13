<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp"%>
<%@page import="com.serotonin.mango.Common"%>
<tag:page dwr="ZoneDwr" onload="init">
<link rel="stylesheet" type="text/css" href="../css/jquery.jqChart.css" />
<script src="../js/jquery-1.5.1.min.js" type="text/javascript"></script>
<script src="../js/jquery.jqChart.min.js" type="text/javascript"></script>
 <script type="text/javascript" src="../msdropdown/uncompressed.jquery.dd.js"></script>
<!--[if IE]><script lang="javascript" type="text/javascript" src="js/excanvas.js"></script><![endif]-->
<script>var $j = jQuery.noConflict();</script>
<script>
	function init(){
		var scopeType='${sessionUser.currentScope.scopetype}';
		if(scopeType=='1'){
			var zone = $("zone"); 
			var scopeId=${sessionUser.currentScope.id};
			for(var i=0;i<zone.length;i++){
				if(scopeId==zone.options[i].value){
				//	getSubZones(zone[i].value);
				}else {
		 	   		zone.options.remove(i);   
				}
			}
			getSubZones(zone[0].value);
		}
		
	}
	function getSubZones(zoneId) {
		ZoneDwr.getSubZonesByZId(zoneId, addSubZone);
	}
	function addSubZone(data) {
		$("subzone").length = 0;
		addDefault($("subzone"));
		for ( var i = 0; i < data.length; i++) {
			addOption($("subzone"), data[i]);
		}
	}

	//
	function addOption(select, data) {
		select.options[select.length] = new Option(data.scopename, data.id);
	}
	function addDefault(select) {
		select.options[select.length] = new Option("<fmt:message key="common.all"/>", "-1");
	}
	function search() {
		$("searchBtn").disabled=true;
		ZoneDwr.searchFactory($get("zone"),$get("subzone"),$get("trade"),$get("code").trim(),$get("factoryName").trim(),searchCB);
	}
	
	function searchCB(result){
		$("searchBtn").disabled=false;
		var content = "";
		for(var i =0;i<result.length;i++){
			var factory = result[i];
			if(factory.disabled)
			    continue;
			if(i%2==0){
				content+="<tr class='row'>";
			}else{
				content+="<tr class='rowAlt'>";
			}
			if(factory.code==null||factory.code=="")
				content+="<td></td>";
			else
				content+="<td>"+factory.code+"</td>";
			content+="<td>"+factory.scopename+"</td>";
			content+="<td>"+factory.grandParent.scopename+"</td>";
			content+="<td>"+factory.parentScope.scopename+"</td>";
			content+="<td>"+factory.scopeUser.username+"</td>";
			if(factory.warnCount==null)
				content+="<td>"+0+"</td>";
			else
				content+="<td>"+factory.warnCount+"</td>";
			if(factory.warnUnderThreeDays==null)
				content+="<td>"+0+"</td>";
			else
				content+="<td>"+factory.warnUnderThreeDays+"</td>";
			if(factory.warnUnderSevenDays==null)
				content+="<td>"+0+"</td>";
			else	
				content+="<td>"+factory.warnUnderSevenDays+"</td>";
			content+="<td>";
			content+="<img class='ptr' src='images/google_ico/factory_go_in.png' onclick='goFactory("+factory.id+")'  >";
			content+="</td>";
			content+="</tr>";
		}
		$j("#searchResult").empty();
		$j("#searchResult").append(content);
		check();
	}
	
	function goFactory(id){
		window.location="watch_list.shtm?factoryId="+id;		
	}
</script>

<script type="text/javascript">
	function getpage() {
		var count = '${count}';
		$j("#demo1").paginate({
			count 		: count,
			start 		: 1,
			display     : 8,
			border					: true,
			border_color			: '#fff',
			text_color  			: '#fff',
			background_color    	: '#6ba53a',	
			border_hover_color		: '#ccc',
			text_hover_color  		: '#000',
			background_hover_color	: '#fff', 
			images					: false,
			mouse					: 'press',
			onChange				: search
		});
		check();
	}
	
</script>
<script type="text/javascript" >
	
	function popArray(list) {
		var len = list.length;
		var arr = new Array();
		if (len) {
			len--;
			for (var i=len;i>=0;i--) {
				arr.push(list[i]);
			};
		}
		return arr;
	}
	
	function check(){
	    $j("#jqChart").html("");
		var titles=new Array();
		var serie1=new Array();
		var serie2=new Array();
		var serie3=new Array(); 
		var names=new Array();
		titles[0]=$j("#listTrHead>td:eq(2)").html();
		titles[1]=$j("#listTrHead>td:eq(3)").html();
		titles[2]=$j("#listTrHead>td:eq(4)").html();
		$j("#listTbody>tr").each(function(){
    	serie1.push([$j(this).find("td:eq(0)").html(),parseFloat($j(this).find("td:eq(2)").html())]);
    	serie2.push([$j(this).find("td:eq(0)").html(),parseFloat($j(this).find("td:eq(3)").html())]);
    	serie3.push([$j(this).find("td:eq(0)").html(),parseFloat($j(this).find("td:eq(4)").html())]);
 		});
 		serie1 = popArray(serie1);
 		serie2 = popArray(serie2);
 		serie3 = popArray(serie3);
 		
 		var hight=(serie3.length)*60+100;
 		$j("#jqChart").height(hight);
 		if(serie3.length>0){
 		$j('#jqChart').jqChart({
              title: {
                    text: '<fmt:message key="event.statistics"></fmt:message>',
                    font: '20px Tahoma, Helvetica, Arial, "5b8b4f53", sans-serif',
                    lineWidth: 1
                    },
              border: {
       			 cornerRadius: 10,
        		 lineWidth: 1,
                 strokeStyle: '#458B00'
               },     
               legend: { location: 'bottom' },

                series: [
                            {
                                type: 'bar',
                                title: titles[0],
                                data: serie1,
                                fillStyle:"yellow"
                                
                            },
                            {
                                type: 'bar',
                                title: titles[1],
                                data: serie2,
                                fillStyle:"orange"
                            },
                            {
                                type: 'bar',
                                title: titles[2],
                                data: serie3,
                                fillStyle:"red"
                            }
                        ]   
        	});
        }
	}
</script>
<body>
<div>
		<table class="borderDiv marR marB">
			<tr>
				<td class="formLabel">
					<fmt:message key="factory.name"></fmt:message>
				</td>
				<td>
					<input id="factoryName" type="text">
				</td>
			</tr>
			<c:if test="${sessionUser.currentScope.scopetype==0}">
			<tr>
				<td class="formLabel">
					<fmt:message key="zone.name"></fmt:message>
				</td>
				<td>
					<select id="zone" onchange="getSubZones(this.value)">
						<option value="-1">
							<fmt:message key="common.all"/>
						</option>
						<c:forEach items="${zoneList}" var="zone">
							<option value="${zone.id }">
								${zone.scopename }
							</option>
						</c:forEach>
					</select>

				</td>
			</tr>
			<tr>
				<td class="formLabel">
					<fmt:message key="subZone.name"></fmt:message>
				</td>
				<td>
					<select id="subzone">
						<option value="-1">
							    <fmt:message key="common.all"/>
						</option>
					</select>

				</td>
			</tr>
			</c:if>
			<c:if test="${sessionUser.currentScope.scopetype==1}">
			<tr>
				<td class="formLabel">
					<fmt:message key="zone.name"></fmt:message>
				</td>
				<td>
					<select id="zone"  disabled="disabled">
						<option value="${sessionUser.currentScope.id }">
							${sessionUser.currentScope.scopename }
						</option>
					</select> 
				</td>
			</tr>
			<tr>
				<td class="formLabel">
					<fmt:message key="subZone.name"></fmt:message>
				</td>
				<td>
					<select id="subzone">
						<option value="-1">
							    <fmt:message key="common.all"/>
						</option>
					</select>
				</td>
			</tr>
			</c:if>
			<c:if test="${sessionUser.currentScope.scopetype==2}">
				<tr>
				<td class="formLabel">
					<fmt:message key="subZone.name"></fmt:message>
				</td>
				<td>
					<input type="hidden" id="zone" value="${sessionUser.currentScope.parentScope.id}"  />
					<select id="subzone" disabled="disabled">
						<option value="${sessionUser.currentScope.id }">
							${sessionUser.currentScope.scopename }
						</option>
					</select>
				</td>
			</tr>
			</c:if>
			<tr>
				<td class="formLabel">
					<fmt:message key="trade.name"></fmt:message>
				</td>
				<td>
					<select id="trade">
						<option value="-1">
							<fmt:message key="common.all"/>
						</option>
						<c:forEach items="${tradeList}" var="trade">
							<option value="${trade.id}">
								${trade.tradename}
							</option>
						</c:forEach>
					</select>

				</td>
			</tr>
			<tr>
				<td class="formLabel">
					<fmt:message key="zone.code"></fmt:message>
				</td>
				<td>
					<input id="code" type="text"></input>
				</td>
			</tr>
			<tr>
				<td colspan="2" align="right">
					<input id="searchBtn" type="button" value="<fmt:message key="events.search.search"/>" onclick="search()"/>
				</td>
			</tr>
		</table>

		<div id="demo1"></div>
		<table  class="borderDiv marB marR">
			<thead>
				<tr id="listTrHead" class="rowHeader">
					<td>
						<fmt:message key="zone.code"></fmt:message>
					</td>
					<td>
						<fmt:message key="factory.name"></fmt:message>
					</td>
					<td>
						<fmt:message key="zone.name"></fmt:message>
					</td>
					<td>
						<fmt:message key="subZone.name"></fmt:message>
					</td>
					<td>
						<fmt:message key="user.admin"></fmt:message>
					</td>
					<td>
						<fmt:message key="event.statistics.warn.yellow"></fmt:message>
					</td>
					<td>
						<fmt:message key="event.statistics.warn.orange"></fmt:message>
	
					</td>
					<td>
						<fmt:message key="event.statistics.warn.red"></fmt:message>
					</td>
					<td>
					  
					</td>
				</tr>
			</thead>
			<tbody id="searchResult"> 
			<c:forEach var="factory" items="${factoryList}" varStatus="index"> 
			<c:if test="${!factory.disabled}">
				<c:choose>
					<c:when test="${index.index%2==0}">
						<tr class="row">
					</c:when>
					<c:otherwise>
						<tr class="rowAlt">
					</c:otherwise>
				</c:choose>
				<td>${factory.code }</td>
				<td>${factory.scopename }</td>
				<td>${factory.grandParent.scopename }</td>
				<td>${factory.parentScope.scopename }</td>
				<td>${factory.scopeUser.username }</td>
				<td>${factory.warnCount }</td>
				<td>${factory.warnUnderThreeDays }</td>
				<td>${factory.warnUnderSevenDays }</td>
				<td>
					<img class="ptr"
						alt="<fmt:message key="FactoryList.factory.login"></fmt:message>"
						src="images/google_ico/factory_go_in.png"
						onclick="goFactory(${factory.id})">
				</td>
				</tr>
				</c:if>
			</c:forEach>
			</tbody>
		</table>
		</div>
		<div>
        <div id="jqChart" style="width: 800px; height: 300px;"></div>
    </div>
	</body>
</tag:page>