<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp"%>
<%@page import="com.serotonin.mango.Common"%>
<tag:page dwr="ZoneDwr" onload="getpage">
<link rel="stylesheet" type="text/css" href="../css/jquery.jqChart.css" />
<link rel="stylesheet" type="text/css" href="../css/style.css" />
<script src="../js/jquery.jqChart.min.js" type="text/javascript"></script>
<script src="../jPaginate/jquery.paginate.js" type="text/javascript"></script>
<!--[if IE]><script lang="javascript" type="text/javascript" src="js/excanvas.js"></script><![endif]-->
<script>var $j = jQuery.noConflict();</script>
<script type="text/javascript">
	function getpage() {
		var count = '${count}';
		var szid = '${subzoneId}';
		var zid = '${zoneId}';
		$j("#szid").val(szid);
		$j("#zid").val(zid);
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
			onChange				: changePage
		});
		check();
	}
	
	function changePage(){
		var currtval = parseInt($j(".jPag-current").html());
		ZoneDwr.getFactoryPageBySubZone(parseInt($j("#szid").val()),currtval,10,setpage);
	}
	
	function setpage(data){
		var zonelist = data;
		$j("#listTbody").empty();
		for (var i in zonelist){
			var htmlstr = "";
			if (i%2==0) {
				htmlstr += "<tr class=\"row\">";
			}else {
				htmlstr += "<tr class=\"rowAlt\">";
			}
			htmlstr += "<td>"+zonelist[i].scopename+"</td>";
			htmlstr += "<td>"+zonelist[i].scopeUser.username+"</td>";
			htmlstr += "<td>"+zonelist[i].warnCount+"</td>";
			htmlstr += "<td>"+zonelist[i].warnUnderThreeDays+"</td>";
			htmlstr += "<td>"+zonelist[i].warnUnderSevenDays+"</td>";
			htmlstr += "<td>";
			var homescope = '${sessionUser.homeScope.scopetype}';
			if (homescope < 2) {
			htmlstr += "<img class=\"ptr\" alt=\"" + $j("#hidname1").html() + "\" src=\"images/google_ico/arrow_left.png\"";
			htmlstr += " onclick=\"window.location='subzone_list.shtm?zoneId="+$j("#zid").val()+"'\">&nbsp;";
			}
			htmlstr += "<img class=\"ptr\" alt=\"" + $j("#hidname2").html() + "\" src=\"images/google_ico/arrow_right.png\"";
			htmlstr += " onclick=\"window.location ='watch_list.shtm?factoryId="+zonelist[i].id+"'\">";
			htmlstr += "</td>";
			htmlstr += "</tr>";
			$j("#listTbody").append(htmlstr);
		}
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
<!-- <body onload="check()"> -->
<body>

	<div id="demo1"></div>
	<div>
		<table  class="borderDiv marB marR">
		<thead>
			<tr id="listTrHead" class="rowHeader">
				<td>
					<fmt:message key="factory.name"></fmt:message>
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
		<tbody id="listTbody">
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
					<td>${factory.scopename }</td>
					<td>${factory.scopeUser.username }</td>
					<td>${factory.warnCount }</td>
					<td>${factory.warnUnderThreeDays }</td>
					<td>${factory.warnUnderSevenDays }</td>
					<td>
						<c:choose>
							<c:when test="${sessionUser.homeScope.scopetype<2}">
								<img class="ptr"
									alt="<fmt:message key="header.zoneList"></fmt:message>"
									src="images/google_ico/arrow_left.png"
									onclick="window.location='subzone_list.shtm?zoneId=${zoneId}'">
							</c:when>
						</c:choose>
						<img class="ptr"
							alt="<fmt:message key="FactoryList.factory.login"></fmt:message>"
							src="images/google_ico/arrow_right.png"
							onclick="window.location ='watch_list.shtm?factoryId=${factory.id}'">
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
        <input type="hidden" id="szid" value='${subzoneId}'>
        <input type="hidden" id="zid" value='${zoneId}'>
   	 	<div style="display:none" id="hidname1"><fmt:message key="header.zoneList"></fmt:message></div>
    	<div style="display:none" id="hidname2"><fmt:message key="FactoryList.factory.login"></fmt:message></div>
	</body>
</tag:page>