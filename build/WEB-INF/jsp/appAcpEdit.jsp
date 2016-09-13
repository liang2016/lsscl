<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp"%>
<%@page import="com.serotonin.mango.Common"%>

<tag:page dwr="AppDatasourceSettingDwr" onload="init">
	<script type="text/javascript">
    var currentPoint;
    var pointListColumnFunctions = new Array();
    var pointListOptions;
 
    function init() {
        show("pointProperties");
        var pointListColumnHeaders = new Array();
        var acpListColumnHeaders = new Array();//copy to acp
        pointListColumnHeaders.push("<fmt:message key="dsEdit.name"/>");
        pointListColumnFunctions.push(function(p) { return "<b>"+ p.name +"</b>"; });
        pointListColumnHeaders.push("<fmt:message key="app.pointname"/>");
        pointListColumnFunctions.push(function(p) { return p.dataTypeMessage; });
        if (typeof appendPointListColumnFunctions == 'function')
            appendPointListColumnFunctions(pointListColumnHeaders, pointListColumnFunctions);
        pointListColumnHeaders.push("");
        pointListColumnFunctions.push(function(p) {
                return writeImage("editImg"+ p.id, null, "icon_comp_edit", "<fmt:message key="common.edit"/>", "editPoint("+ p.id +",this)");
        });
        var headers = $("pointListHeaders");
        var td;
        for (var i=0; i<pointListColumnHeaders.length; i++) {
            td = document.createElement("td");
            if (typeof(pointListColumnHeaders[i]) == "string")
                td.innerHTML = pointListColumnHeaders[i];
            else
                pointListColumnHeaders[i](td);
            headers.appendChild(td);
        }
        getPoints();
        getAcps();
        getAcpPoints();
    }
    function getPoints(){
    	AppDatasourceSettingDwr.getPointsByAcpId($("hiddenAcpId").value,function(response){
    	var pointsList = $("pointsList");
    	pointsList.innerHTML = "";
    	var points = response.data.points;
    	    for(var i=0;i<points.length;i++){
    	    var point = points[i];
    	    console.log(point);
    	    	var tr = document.createElement("tr"),
    	    	    tdName = document.createElement("td"),
    	    	    tdPointName = document.createElement("td"),
    	    	    tdEdit = document.createElement("td");
    	    	    tdName.innerHTML = point.name;
    	    	    tdPointName.innerHTML = point.dataPointVo.extendedName;
    	    	    var name = "'"+point.name+"'";
    	    	    tdEdit.innerHTML = '<a href="#" onclick="editPoint('+point.id+','+name+','+point.dataPointVo.dataSourceId+','+point.aid+','+point.pointId+')" class="editImg"><tag:img png="icon_ds_edit"
                              title="common.edit" id="editImg'+point.id+'"/></a>';
    	    	    tr.appendChild(tdName);
    	    	    tr.appendChild(tdPointName);
    	    	    tr.appendChild(tdEdit);
    	    	    pointsList.appendChild(tr);
    	    }
    	    
    	});
    }
    
    function getAcps(){
    	var dsid = jQuery("#dataSourceId").val();
    	AppDatasourceSettingDwr.getAcps(dsid,function(response){
    		var acps = response.data.acps;
    		var html = '<option value="-1">---<fmt:message key="reports.commentList.type.point"/>---</option>';
    		if(acps){
    		    jQuery.each(acps,function(i,acp){
    		    	 html += '<option value="'+acp.id+'">'+acp.acpname+'</option>';
    		    });
    		    jQuery("#acpId").html(html);
    		    getAcpPoints();
    		}
    	});
    }
   
    function getAcpPoints(){
    	var acpid = jQuery("#acpId").val(),
    	    dsid = jQuery("#dataSourceId").val();
    	AppDatasourceSettingDwr.getPoints(acpid,dsid,function(response){
    		var points = response.data.points;
    		var html = '';
    		if(points){
    			jQuery.each(points,function(i,p){
    				html+='<option value="'+p.id+'">'+p.name+'</option>';
    			});
    			jQuery("#pointId").html(html);
    			jQuery("#pointId").val(jQuery("#hPointId").val());
    		}
    	});
    }
    function editPoint(pointId,name,dsid,aid,pid) { 
       jQuery("#pointMessage").html("");
       show("pointDetails");
       var imgId = "editImg"+pointId;
       showAllImg();
       jQuery("#pid").val(pointId);
       jQuery("#"+imgId).fadeOut();
       jQuery("#name").val(name);
       jQuery("#dataSourceId").val(dsid);
       jQuery("#acpId").val(aid);
       jQuery("#hPointId").val(pid);
       getAcpPoints();
       
    }
    
    function showAllImg(){
    	//show("editImag-1");
    	jQuery("#editImg-1").show();
    	jQuery(".editImg img").show();
    }
    
    function savePoint(){
    	var pid = jQuery("#pid").val(),
    	    name = jQuery("#name").val(),
    	    pointId = jQuery("#pointId").val(),
    	    aid = jQuery("#hiddenAcpId").val();
        AppDatasourceSettingDwr.savePoint(pid,name,pointId,aid,function(response){
            var error = response.data.error;
            if(error){
            	jQuery("#pointMessage").html("<fmt:message key="app.pointSaveError"/>");
            }else{
           	 	jQuery("#pointMessage").html("");
	        	showAllImg();
	        	hide("pointDetails");
	        	getPoints();
            }
        });
    }
    function saveAcpPoints(){
    	var acpId = jQuery("#acpId").val(),
    	    aid = jQuery("#hiddenAcpId").val(),
    	    dsid = jQuery("#dataSourceId").val();
    	 AppDatasourceSettingDwr.saveAcpPoints(dsid,acpId,aid,function(response){
        	showAllImg();
        	hide("pointDetails");
        	getPoints();
        });    
    }
    function deletePoint(){
    	var pid = jQuery("#pid").val();
    	if(pid!=""){
    		AppDatasourceSettingDwr.deletePoint(pid,function(response){
    			getPoints();
    			hide("pointDetails");
    		});
    	}
    }
    function saveAcp(){
    	var scopeId = jQuery("#scopeId").val(),
    	    aid = jQuery("#hiddenAcpId").val(),
    	    acppower = jQuery("#acppower").val(),
    	    name = jQuery("#acpname").val(),
    	    type = jQuery("#acptype").val();
    	    
    	 AppDatasourceSettingDwr.saveAcp(aid,scopeId,name,acppower,type,function(response){
    	     jQuery("#hiddenAcpId").val(response.data.aid);
    	     jQuery("#pointsTable").fadeIn();
    	     alert("<fmt:message key="FactoryList.factory.save.succeed"/>");
    	 });   
    }
  </script>
  <input type="hidden" id="pid"/>
  <input type="hidden" id="hPointId"/>
  <input type="hidden" id="scopeId" value="${scopeId}">
  <input type="hidden" value="${acp.id}" id="hiddenAcpId"/>
   <%@ include file="/WEB-INF/jsp/app/editAppPoints.jsp" %>
</tag:page>