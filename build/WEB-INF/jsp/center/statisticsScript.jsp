<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>
<%@page import="com.serotonin.mango.Common"%>
<%@page import="com.serotonin.mango.vo.statistics.StatisticsScriptVO"%>
<%@page import="org.joda.time.DateTimeConstants"%>
<c:set var="NEW_ID"><%= Common.NEW_ID %></c:set>

<tag:page dwr="StatisticsScriptDwr" onload="init">
<script type="text/javascript" src="../js/jquery.js"></script>
<script>var $j = jQuery.noConflict();</script>
<script type="text/javascript">
    var TreeClickHandler = function() {
        this.handle = function(message) {
            var widget = message.source;
            var wid = widget.widgetId;
            if (wid.startsWith("acpParam") || wid.startsWith("acpSystemParam"))
                insertText(widget.object);
        }
    }
    
    function init() {
    	StatisticsScriptDwr.init(initCB);
    	var timer=setInterval("refreshStatisticProgress()", 1000);
    }
    
    function initCB(response){
    	refreshList(response.data.ssptlist);
    	var tempNode;
    	var acpParams = response.data.acpParams;
    	// init acp params tree
    	var acpParamsTreeRoot = dojo.widget.manager.getWidgetById('acpparams');
        for (var i=0; i<acpParams.length; i++) {
        	acpParam = acpParams[i];
        	tempNode = dojo.widget.createWidget("TreeNode", {
        		title: "<img id='acpImg"+acpParam.id+"' src='images/icon_comp.png' /> "+ acpParam.statisticsName+"  ("+"<c:out value='<%= StatisticsScriptVO.ACP_PARAM_PREFIX %>'/>"+acpParam.id+")",
        		widgetId:"acpParam"+acpParam.id,
        		object:'<c:out value="<%= StatisticsScriptVO.ACP_PARAM_PREFIX %>"/>'+acpParam.id
        	});
        	acpParamsTreeRoot.addChild(tempNode);
        }
        acpParamsTreeRoot.expand();
        // init acpsystem params tree
        var acpSystemParams = response.data.acpSystemParams;
        var acpSystemParamsTreeRoot = dojo.widget.manager.getWidgetById('acpsystemparams');
        for (var i=0; i<acpSystemParams.length; i++) {
        	acpSystemParam = acpSystemParams[i];
        	tempNode = dojo.widget.createWidget("TreeNode", {
        		title: "<img id='acpSystemImg"+acpSystemParam.id+"' src='images/icon_comp.png' /> "+ acpSystemParam.statisticsName+"  (S"+acpSystemParam.id+")",
        		widgetId:"acpSystemParam"+acpSystemParam.id,
        		object:'<c:out value="<%= StatisticsScriptVO.ACPSYSTEM_PARAM_PREFIX %>"/>'+acpSystemParam.id        		
        	});
        	acpSystemParamsTreeRoot.addChild(tempNode);
        }
        acpSystemParamsTreeRoot.expand();
        dojo.event.topic.subscribe("statisticParams/titleClick", new TreeClickHandler(), 'handle');
        
        if(acpParams.length==0&&acpSystemParams.length==0){
        	$set("noStatisticParams","<fmt:message key='statistic.information.params.isnull' />");
        }
    }
    
    // refresh the list of statisticScripts
    function refreshList(ssptlist){
    	var content = "";
    	for(var i=0;i<ssptlist.length;i++){
    		content += "<tr>";
			content += "<td class='ptr' class='link' onclick='showDetails("+ssptlist[i].id+")'><img src='images/multi_bell_disabled.png' onclick=''/>&nbsp;<a>"+ssptlist[i].name+"</a></td>";
    		content += "</tr>";
    	}
		$j("#ssptlist").html(content);			
    }
    
    // show the statisticScript details 
    function showDetails(id){
    	$set("id",id);
    	if(id==${NEW_ID}){
    		showDetailsCB(null);
    	}else{
	    	StatisticsScriptDwr.getStatisticsScriptById(id,showDetailsCB);
    	}
    }
    
    // the callback function of 'showDetails'
    function showDetailsCB(response){
    	setUserMessage();
    	hide($("scriptDetails"));
    	$j("#scriptMessage").html("");
    	show($("scriptDetails"));
    	show($("statisticParams"));
    	if(response==null){
	    	StatisticsScriptDwr.getUniqueXid(function(uniqueXid){
	    		$set("xid",uniqueXid);
	    	});
    		$set("name","");
    		$set("conditionText","");
    		$j("#conditionText").attr("readonly","");
    		
    		var startTimeStr = "<select id='startTime' onchange='showSpecifiedDate(this.value)'>"
              					+"<option value='-1'><fmt:message key='stattistic.script.startTime.earliest'/></option>"
              					+"<option value='0'><fmt:message key='stattistic.script.startTime.now'/></option>"
              					+"<option value='1'><fmt:message key='stattistic.script.startTime.specified'/></option>"
              				  +"</select>&nbsp;"
              				  +"<input id='specifiedDate' type='text' name='specifiedDate' style='display:none' />";
    		
    		$j("#startTimeTD").html(startTimeStr);
    		$set("startTime","");
    		$j("#specifiedDate").hide();
    		$set("disabled","");
    	}else{
    		var vo = response.data.scriptVO;
    		$set("xid",vo.xid);
    		$set("name",vo.name);
    		$set("conditionText",vo.conditionText);
    		$j("#conditionText").attr("readonly","readonly");
    		var startTime = new Date(vo.startTime);
    		$j("#startTimeTD").html(""+startTime.getFullYear()+"-"+(startTime.getMonth()+1)+"-"+startTime.getDate()+" "+startTime.getHours()+":00");
    		$set("disabled",vo.disabled);
   		} 
    }
    //save the statisticScript
    function saveStatisticScript(){
    	setUserMessage();
    	hideContextualMessages("scriptDetails");
    	var id = $get("id");
    	var xid = $get("xid");
    	var name = $get("name");
    	var disabled = $get("disabled");
    	var conditionText = $get("conditionText");
    	var startTime;
    	if(id==${NEW_ID}){
    		startTime = $j("#startTime").val();
    	}else{
			startTime = "2";
    	}
    	if(startTime=="1"){
    		startTime = $j("#specifiedDate").val(); 
    	}
    	StatisticsScriptDwr.saveStatisticsScript(id,xid,name,disabled,conditionText,startTime,function(response){
    		//call back function of saveStatisticScript
    		setUserMessage();
    		$set("scriptMessage","")
	    	refreshList(response.data.ssptlist);
	    	if (response.hasMessages){
	    		showDwrMessages(response.messages);
	    	}else{
	    		var newId = response.data.newId;
	    		if(id==newId){//update
	    			setUserMessage("<fmt:message key='statistic.statisticScript.updateSuccess' />");
	    		}else{//save
	    			$j("#conditionText").attr("readonly","readonly");
	    			$set("id",newId);
		    		var startTime = new Date(response.data.startTime);
	    			$j("#startTimeTD").html(""+startTime.getFullYear()+"-"+(startTime.getMonth()+1)+"-"+startTime.getDate()+" "+startTime.getHours()+":00");
	    			setUserMessage("<fmt:message key='statistic.statisticScript.saveSuccess' />");
	    		}
	    	} 
    	});
    }
    
    //delete the statisticScript
	function deleteStatisticScript(){
		StatisticsScriptDwr.deleteStatisticsScriptById($get("id"),function(response){
			refreshList(response.data.ssptlist);
		});
		hide("scriptDetails");
		hide("statisticParams");
    }
    //write the content to textarea
    function insertText(text) {
		if($j("#conditionText").attr("readonly")==false){
	        insertIntoTextArea($("conditionText"), text);
		}
    }
    function setUserMessage(message) {
    	if (message) {
        	show("userMessage");
        	$set("userMessage", message);
        }
        else
            hide("userMessage");
    }
    
    function validateCondition(){
    	$set("scriptMessage","")
    	hideContextualMessages("scriptDetails");
    	var conditionText = $get("conditionText");
    	StatisticsScriptDwr.validateCondition(conditionText,function(response){
    		if(response.data.value){
    			$set("scriptMessage",response.data.value);
    		}else if(response.data.scriptError){
    			$set("scriptMessage",response.data.scriptError);
    		}else{
    			showDwrMessages(response.messages, "scriptMessage");
    		}
    	});
    }
    
    function showSpecifiedDate(value){
		if(value=="1") $j("#specifiedDate").show();
		else $j("#specifiedDate").hide();
    }
    
    function refreshStatisticProgress(){
    	StatisticsScriptDwr.getAllStatisticProgress(function(data){
    		var content = "";
    		if(data.length==0){
    			content = "<tr><td colspan='3'>init...</td></tr>";
    			$j("#processList").html(content);
    		}else{
	    		for(var i=0;i<data.length;i++){
	    			var vo = data[i];
	    			if(vo.running)content+="<tr style='background:yellow'>";
	    			else content+="<tr class='row'>";
	    			if(vo.scriptVO==null){
	    				content+="<td colspan='3'>init...</td>";
	    			}else{
	    				content+="<td>";
	    				if(vo.scriptVO.name==""){
	    					content+="init...";
	    				}else{
	    					content+=vo.scriptVO.name;
	    				}	    				
		    			content+="</td>";
		    			content+="<td>";
		    			if(vo.unit==""){
			 				content+="init...";
		    			}else{
		    				content+=vo.unit;
		    			}
		    			content+="</td>";
		    			content+="<td>";
		    			if(vo.statisticTime==0||vo.statisticTime==null){
		    				content+="init...";
		    			}else{
		    				if(vo.statisticTime==-1){
								content+="stop by user ";
		    				}else{
			    				var endTime = new Date(vo.statisticTime);
			 					content+=endTime.getFullYear()+"-"+(endTime.getMonth()+1)+"-"+endTime.getDate()+" "+endTime.getHours()+":00";
		    				}
		    			}
		    			content+="</td>";
		    			content+="</tr>";
	    			}
	    		}
	    	}
	  	    $j("#processList").html(content);
    	});
    }
    
  </script>
  <table> 
    <tr>
      <td rowspan="2" valign="top">
        <div class="borderDiv">
          <table width="100%">
            <tr>
              <td>
                <span class="smallTitle"><fmt:message key="statistic.statisticScript.list"/></span>
                <tag:help id="statisticScriptHelp"/>
              </td>
              <td align="right"><tag:img png="multi_bell_add" title="common.add" id="sspt${NEW_ID}Img"
                      onclick="showDetails(${NEW_ID})"/></td>
            </tr>
          </table>
          <table id="statisticScriptsTable">
            <tbody id="ssptlist"></tbody>
          </table>
        </div>
      </td>
      <td valign="top" style="display:none;" id="scriptDetails">
        <div class="borderDiv">
          <table width="100%">
            <tr>
              <td><span class="smallTitle"><fmt:message key="statistic.statisticScript.details"/></span></td>
              <td align="right">
                <tag:img png="save" onclick="saveStatisticScript();" title="common.save"/>
                <tag:img id="deleteStatisticScriptImg" png="delete" onclick="deleteStatisticScript();" title="common.delete"/>
              </td>
            </tr>
          </table>
  
          <table>
            <tr>
              <td class="formLabelRequired"><fmt:message key="common.xid"/></td>
              <td class="formField"><input type="text" id="xid"/><input type="hidden" id="id" value="${NEW_ID}"> </td>
            </tr>
            <tr>
              <td class="formLabelRequired"><fmt:message key="statistic.script.name"/></td>
              <td class="formField"><input type="text" id="name"/></td>
            </tr>
            <tr>
              <td class="formLabelRequired">
                <fmt:message key="compoundDetectors.condition"/>
                <tag:img png="accept" onclick="validateCondition();" title="compoundDetectors.validate"/><br/>
                <br/>
                <a style="TEXT-DECORATION:none" onclick="insertText(' + ');return false" href="#" > +</a><br/>
                <a style="TEXT-DECORATION:none" onclick="insertText(' - ');return false" href="#" >-</a><br/>
                <a style="TEXT-DECORATION:none" onclick="insertText(' * ');return false" href="#" >&times;</a><br/>
                <a style="TEXT-DECORATION:none" onclick="insertText(' / ');return false" href="#" >&divide;</a><br/>
              </td>
              <td class="formField"><textarea rows="10" cols="60" id="conditionText"></textarea></td>
            </tr>
            <tr>
            	<td></td>
            	<td id="scriptMessage" style="font-weight: bold;color: red;" ></td>
            </tr>
            <tr>
            	<td class="formLabelRequired"><fmt:message key="stattistic.script.startTime"/></td>
              	<td class="formField" id="startTimeTD">
              		<select id="startTime" onchange="showSpecifiedDate(this.value)">
              			<option value="-1"><fmt:message key="stattistic.script.startTime.earliest"/></option>
              			<option value="0"><fmt:message key="stattistic.script.startTime.now"/></option>
              			<option value="1"><fmt:message key="stattistic.script.startTime.specified"/></option>
              		</select>&nbsp;
              		<input id="specifiedDate" type="text" name="specifiedDate" style="display:none" />
              	</td>
            </tr>
            <tr>
              <td class="formLabelRequired"><fmt:message key="common.disabled"/></td>
              <td class="formField"><input type="checkbox" id="disabled"/></td>
            </tr>
          </table>
          <table>
            <tr>
              <td colspan="2" id="userMessage" class="formError" style="display:none;"></td>
            </tr>
          </table>
        </div>
      </td>
    </tr>
    
    <tr>
      <td valign="top" style="display:none;" id="statisticParams">
        <div class="borderDivPadded">
          <span class="smallTitle"><fmt:message key="statistic.params.list"/></span>
          <div dojoType="TreeBasicController" widgetId="controller"></div>
          <div id="tree">
            <div dojoType="Tree" widgetId="statisticParams" listeners="controller" toggle="wipe">
              <div dojoType="TreeNode" title="<fmt:message key="statistic.acp.params.list"/>" widgetId="acpparams"></div>
              <div dojoType="TreeNode" title="<fmt:message key="statistic.acpsystem.params.list"/>" widgetId="acpsystemparams"></div>
              <span id="noStatisticParams" style="font-style: oblique;color: red;" ></span>
            </div>
          </div>
        </div>
      </td>
    </tr>
    <tr>
    	<td colspan="2" valign="top" id="statisticProcessInformation">
    		<div class="borderDivPadded">
    		<table>
    			<thead>
			  		<tr>
			  			<td colspan="4" class="smallTitle" ><fmt:message key="statistic.process.information" /></td>
			  		</tr>
			  		<tr class="rowHeader">
			  			<td><fmt:message key="statistic.script.scriptName" /></td>
			  			<td><fmt:message key="statistic.script.machine" />/<fmt:message key="statistic.script.system" /><fmt:message key="statistic.script.name" /></td>
			  			<td><fmt:message key="statistic.script.currenttime" /></td>
			  		</tr>
			  	</thead>
			  	<tbody id="processList">
			  	</tbody>  
    		</table>
    		</div>
    	</td>
    </tr>
  </table>
</tag:page>