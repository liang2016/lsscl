<%@ include file="/WEB-INF/jsp/include/tech.jsp"%>
<%@page import="com.serotonin.mango.vo.UserComment"%>
<%@page import="com.serotonin.mango.Common"%>
<tag:page dwr="HealthDwr" onload="init">
<%@ include file="/WEB-INF/jsp/include/userComment.jsp" %>
	<script type="text/javascript">
	function init(){
		HealthDwr.initTree($get("scopeId"),initTreeCB);
		tree = dojo.widget.manager.getWidgetById('tree');
	    dojo.event.topic.subscribe("tree/titleClick", new TreeClickHandler(), 'handle');
	    HealthDwr.getDateRangeDefaults(<c:out value="<%= Common.TimePeriods.DAYS %>"/>, 1, function(data) { setDateRange(data); setDateRangeS(data);setDateRangeC(data);});
		
	
	}
	function initTreeCB(data){
     var ACPSystem=data.casList;
     var acpList=data.acpList;
     for(var i=0;i<ACPSystem.length;i++){
     var typeNode = dojo.widget.createWidget("TreeNode", {
	                title: "<img src='images/folder_brick.png'/> <span onclick='' >"+ ACPSystem[i].systemname+"</span>",
	                isFolder: "true",
	                object: ACPSystem[i]
		        });
		        tree.addChild(typeNode);//
		        
		        for(var j=0;j<acpList.length;j++){
		        if(acpList[j].compressorId==ACPSystem[i].id){
		           var attrNode1 = dojo.widget.createWidget("TreeNode", {
		                title: "<img src='images/folder_brick.png' onclick='getAll("+acpList[j].id+")' > "+ acpList[j].acpname+"</span>",
		                isFolder: "false",
		                object: acpList[j]
		        	});
		        	typeNode.addChild(attrNode1); 
		        	
		          }
		        }
     }
    var options=data.users;
    	var creator=$("creator");
    	var selectSolver=$("selectSolver");
    	var selectChecker=$("selectChecker");
    	 creator.length=0;
    	 selectSolver.length=0;
    	 selectChecker.length=0;
    	 creator[creator.length]=new Option("<fmt:message key="common.all"/>",-1);
    	 selectSolver[selectSolver.length]=new Option("<fmt:message key="common.all"/>",-1);
    	 selectChecker[selectChecker.length]=new Option("<fmt:message key="common.all"/>",-1);
	    	 for(var i=0;i<options.length;i++){
				creator[creator.length]=new Option(options[i].username,options[i].id);
				selectSolver[selectSolver.length]=new Option(options[i].username,options[i].id);
				selectChecker[selectChecker.length]=new Option(options[i].username,options[i].id); 
	    	 }	 
   hide("loadingImg");
   }
	function getAll(acpid){
		show("cardInfo");
		HealthDwr.getAll(acpid,10,writeInfo);
		$set("acpId",acpid);
	}
	function getLimitData(){
		var status=document.getElementsByName("showType");
		var statusNum;
		for(var i=0;i<status.length;i++){
			if(status[i].checked){
				statusNum=status[i].value;
				break;
			}
		}
		HealthDwr.getLimitData($get("acpId"),statusNum,$get("historyLimit"),writeInfo);
	}
	function writeInfo(results){
		 $set("card", results.data.content);
	}	
	var TreeClickHandler = function() {
        this.handle = function(message) {
        	//setErrorMessage();
            var widget = message.source;
            if (widget.isFolder) {
                selectedFolderNode = widget;
            }
            else
                hide("folderEditDiv");
        }
        show("treeDiv");
    }
    //save
    function save(){
	    HealthDwr.save($get("id"),$get("type"),$get("description"),$get("solver"),$get("acpId"),$get("solve"),$get("message"),$get("check"),saveCB);
    }
    function saveCB(response){
    	 if (response.hasMessages)
    	 	 showDwrMessages(response.messages, "genericMessages");
    	 else{
    	 	$set("id",response.data.cardId);
    	 	closeHealthCard();
    	 	hideGenericMessages("genericMessages");
    	 	getLimitData();
    	 }	
    }
    function showHealthCard(){
    	 hide("details");
    	 $set("id",<c:out value="<%= Common.NEW_ID %>"/>);
    	 HealthDwr.initAdd($get("scopeId"), $get("id"),initCD);
    	 dojo.widget.byId("add").show();
    }
    function initCD(response){
    	 var options=response.users;
    	 $("solver").length=0;
	    	 for(var i=0;i<options.length;i++){
				$("solver")[$("solver").length]=new Option(options[i].username,options[i].id);;    	 
	    	 }
    	 }
    
    function closeHealthCard() {
     	 dojo.widget.byId("add").hide();
 	 }
 	function deleteCard(){
 	if (confirm("<sst:i18n key="health.deleteConfirm" escapeDQuotes="true"/>")) {
 	  	 var cardId=$get("id");
 	  	 HealthDwr.deleteCard(cardId,deleteDB);
 	   }
 	 }
 	 function deleteDB(response){
 	  if (response.hasMessages)
    	 	 showDwrMessages(response.messages, "genericMessages");
    	 else{
    	 	$set("id",<c:out value="<%= Common.NEW_ID %>"/>);
    	 	closeHealthCard();
    	 	hideGenericMessages("genericMessages");
    	 	getLimitData();
    	 }	
 	 }
 	 function search(){
	 	 var creator=$get("creator");
	 	 var selectType=$get("selectType");
	 	 var selectChecker=$get("selectChecker");
	 	 var selectStatus=$get("selectStatus");
	 	 var fromNone=$get("fromNone");
	 	 var toNone=$get("toNone");
	 	 var startTimeF=new Date($get("fromYear"),$get("fromMonth"),$get("fromDay"),$get("fromHour"),$get("fromMinute"),$get("fromSecond"));
	 	 var startTimeT=new Date($get("toYear"),$get("toMonth"),$get("toDay"),$get("toHour"),$get("toMinute"),$get("toSecond"));
	 	 var fromNoneS=$get("fromNoneS");
	 	 var toNoneS=$get("toNoneS");
	 	 var selectSolver=$get("selectSolver");
	 	 var solveTimeF=new Date($get("fromYearS"),$get("fromMonthS"),$get("fromDayS"),$get("fromHourS"),$get("fromMinuteS"),$get("fromSecondS"));
	 	 var solveTimeT=new Date($get("toYearS"),$get("toMonthS"),$get("toDayS"),$get("toHourS"),$get("toMinuteS"),$get("toSecondS"));
	 	 var fromNoneC=$get("fromNoneC");
	 	 var toNoneC=$get("toNoneC");
	 	 var checkTimeF=new Date($get("fromYearC"),$get("fromMonthC"),$get("fromDayC"),$get("fromHourC"),$get("fromMinuteC"),$get("fromSecondC"));
	 	 var checkTimeT=new Date($get("toYearC"),$get("toMonthC"),$get("toDayC"),$get("toHourC"),$get("toMinuteC"),$get("toSecondC"));
 	 	 var acpid=$get("acpId"); 
 	 	 HealthDwr.search(acpid,creator,selectType,selectSolver,selectChecker,selectStatus,fromNone,
			toNone,startTimeF,startTimeT,fromNoneS, toNoneS,solveTimeF,solveTimeT,fromNoneC,
			toNoneC,checkTimeF,checkTimeT,writeInfo);
 	 }
</script>
<style type="text/css">
  .dojoDialog {
      background : #eee;
      border : 1px solid #999;
      -moz-border-radius : 5px;
      padding : 4px;
  }
</style>
<div class="borderDivPadded" style="float:left;">
	<table>
		<tr>
			<td>
				<span class="smallTitle"><fmt:message key="health.card.title"/></span>
				<input type="hidden" id="scopeId" value="${sessionUser.currentScope.id}">
			</td>
		</tr>
		<tr>
			<td>
			<tag:img png="hourglass" id="loadingImg"/>
			<div id="treeDiv" style="display: none;">
			<div dojoType="Tree" id="tree"  DNDAcceptTypes="tree" widgetId="tree">
			</div>
			</td>
		</tr>
	</table>
</div>
<div dojoType="dialog" id="add" bgColor="white" bgOpacity="0.5" toggle="fade" toggleDuration="250">
	<table>
		<tr>
			<td>
			<span class="smallTitle"><fmt:message key="health.card"></fmt:message></span>
			</td>
			<td align="right">
				<tag:img png="delete" onclick="deleteCard()" title="common.delete"/>
				<tag:img png="save" onclick="save()" title="common.save"/>
				<tag:img png="cancel" onclick="closeHealthCard()" title="common.cancel"/>
			</td>
		</tr>
		<tr style="display: none;">
			<td>
				<input type="hidden" id="acpId" value="">
			</td>
			<td>
				<input type="hidden" id="id" value="<c:out value="<%= Common.NEW_ID %>"/>">
			</td>
		</tr>
		<tr>
				<td><fmt:message key="health.startTime"/></td>
				<td>
					<label id="startTime"></label>
				</td>
			</tr>
		<tr>
			<td>
				<fmt:message key="health.type"></fmt:message>
			</td>
			<td>
			<select id="type" >
				<option value="1"><fmt:message key="health.normal"/></option>
				<option value="2"><fmt:message key="health.abnormal"/></option>
				<option value="3"><fmt:message key="health.error"/></option>
			</select>
			</td>
		</tr>
		<tr>
			<td>
				<fmt:message key="health.description"></fmt:message>
			</td>
			<td>
				<textarea rows="5" cols="20" id="description"></textarea>
			</td>
		</tr>
		<tr>
			<td><fmt:message key="health.solver"/></td>
			<td>
				<select id="solver">
					<option></option>
				</select>
			</td>
		</tr>
		<tbody id="details" style="display: none;">
			<tr>
				<td><fmt:message key="health.solve"/></td>
				<td>
					<input type="checkbox" id="solve"/>
				</td>
			</tr>
			<tr>
				<td><fmt:message key="health.replace"/></td>
				<td>
					<textarea rows="5" cols="20" id="message"></textarea>
				</td>
			</tr>
			<tr>
				<td><fmt:message key="health.endTime"/></td>
				<td>
					<label id="endTime"></label>
				</td>
			</tr>
			<tr>
				<td><fmt:message key="health.check"/></td>
				<td>
				<select id="check">
				<!--  	<option value="1"><fmt:message key="health.new"/></option>
				-->	<option value="2"><fmt:message key="health.checking"/></option>
					<option value="3"><fmt:message key="health.checkBack"/></option>
					<option value="4"><fmt:message key="health.checked"/></option>
				</select>
				</td>
			</tr>
		</tbody>
	</table>
	<table><tbody id="genericMessages"></tbody></table>
</div>
<div style="display: none;float:left;" id="cardInfo">
	<div  class="borderDiv" id="card">
	</div>
	<div id="selectInfo" class="borderDiv">
		<div class="smallTitle titlePadding"><fmt:message key="health.search"/></div>
		 	<table>
		 		<tr>
		 			<td class="formLabel">
		 				<fmt:message key="health.creator"/>
		 			</td>
		 			<td class="formField">
		 				<select id="creator">
		 			</select>
		 			</td>
		 		</tr>
		 			<td class="formLabel">
		 				<fmt:message key="health.startTime"/>
		 			</td>
		 			<td class="formField">
		 				<tag:dateRange/>
		 			</td>
		 		<tr>
		 		</tr>
		 		<tr>
		 			<td class="formLabel">
		 				<fmt:message key="health.type"/>
		 			</td>
		 			<td class="formField">
		 				<select id="selectType" >
		 				<option value="-1"><fmt:message key="common.all"/></option>
						<option value="1"><fmt:message key="health.normal"/></option>
						<option value="2"><fmt:message key="health.abnormal"/></option>
						<option value="3"><fmt:message key="health.error"/></option>
					</select>
		 			</td>
		 		</tr>
		 		<tr>
		 			<td class="formLabel">
		 				<fmt:message key="health.solver"/>
		 			</td>
		 			<td class="formField">
		 				<select id="selectSolver">
		 				</select>
		 			</td>
		 		</tr>
		 		<tr>
		 			<td class="formLabel">
		 				<fmt:message key="health.solve.time"/>
		 			</td>
		 			<td class="formField">
		 				<tag:dateRangeSolve/>
		 			</td>
		 		</tr>
		 		<tr>
		 			<td class="formLabel">
		 				<fmt:message key="health.checker"/>
		 				</td>
		 			<td class="formField">
		 				<select id="selectChecker">
		 				</select>
		 			</td>
		 		</tr>
		 		<tr>
		 			<td class="formLabel">
		 				<fmt:message key="health.check.time"/>
		 			</td>
		 			<td class="formField">
		 				<tag:dateRangeCheck/>
		 			</td>
		 		</tr>
		 		<tr>
		 			<td class="formLabel">
		 				<fmt:message key="health.status"/>
		 			</td>
		 			<td class="formField">
		 				<select id="selectStatus">
		 					<option value="-1"><fmt:message key="common.all"/></option>
		 					<option value="1"><fmt:message key="health.new"/></option>
							<option value="2"><fmt:message key="health.checking"/></option>
							<option value="3"><fmt:message key="health.checkBack"/></option>
							<option value="4"><fmt:message key="health.checked"/></option>
		 				</select>
		 			</td>
		 		</tr>
		 		<tr>
		          <td colspan="2" align="center">
		            <input id="searchBtn" type="button" value="<fmt:message key="events.search.search"/>" onclick="search()"/>
		            <span id="searchMessage" class="formError"></span>
		          </td>
		        </tr>
		 	</table>
	</div>
</div>
</tag:page>