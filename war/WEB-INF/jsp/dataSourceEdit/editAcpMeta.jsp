<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>
<%@page import="com.serotonin.mango.vo.dataSource.meta.MetaPointLocatorVO"%>
<%@page import="com.serotonin.mango.Common"%>

<script type="text/javascript">
  var pointsArray = new Array();
  var contextArray = new Array();
  
  function initImpl() {
      <c:forEach items="${userPoints}" var="dp">
        pointsArray[pointsArray.length] = {
            id : ${dp.id}, 
            name : '${sst:quotEncode(dp.extendedName)}',
            type : '<sst:i18n message="${dp.dataTypeMessage}"/>'
        };
      </c:forEach>
      
      createContextualMessageNode("contextContainer", "context");
  }
  
  function appendPointListColumnFunctions(pointListColumnHeaders, pointListColumnFunctions) {
      pointListColumnHeaders.push("");
      pointListColumnFunctions.push(function(p) {
          var id = "generateImg"+ p.id;
          var onclick = "generateHistory("+ p.id +")";
          return writeImage(id, null, "clock-history", "<fmt:message key="dsEdit.meta.generate"/>", onclick);
      });
  }
  
  function generateHistory(pointId) {
      startImageFader("generateImg"+ pointId, true);
	  DataSourceEditDwr.generateMetaPointHistory(pointId, function(result) {
	      stopImageFader("generateImg"+ pointId);
	      alert(result);
	  });
  }
  
  function saveDataSourceImpl() {
      DataSourceEditDwr.saveMetaDataSource($get("dataSourceName"), $get("dataSourceXid"), saveDataSourceCB);
  }
  
  function editPointCBImpl(locator) {
      contextArray.length = 0;
      for (var i=0; i<locator.context.length; i++)
          addToContextArray(locator.context[i].key, locator.context[i].value);
      writeContextArray();
      
      $set("script", locator.script);
      $set("dataTypeId", locator.dataTypeId);
      $set("updateEvent", locator.updateEvent);
      $set("executionDelaySeconds", locator.executionDelaySeconds);
      
      updateEventChanged();
  }
  function savePointImpl(locator) {

      locator.context = createContextArray();
      locator.script = $get("script");
      locator.dataTypeId = $get("dataTypeId");
      locator.updateEvent = $get("updateEvent");
      locator.executionDelaySeconds = $get("executionDelaySeconds");
      var acpTypeId=parseInt($j("#typeId").val());
      ACPTypeAttrDwr.editMetaModel(currentPoint.id, acpTypeId,$get("acpMetaName"),locator,saveMetaPointCB);
  }
  
  function addPointToContext() {
      var pointId = $get("allPointsList");
      addToContextArray(pointId, "p"+ pointId);
      writeContextArray();
  }
  
  function addToContextArray(pointId, scriptVarName) {
      var data = getElement(pointsArray, pointId);
      if (data) {
          // Missing names imply that the point was deleted, so ignore.
          contextArray[contextArray.length] = {
              pointId : pointId,
              pointName : data.name,
              pointType : data.type,
              scriptVarName : scriptVarName
          };
      }
  }
  
  function removeFromContextArray(pointId) {
      for (var i=contextArray.length-1; i>=0; i--) {
          if (contextArray[i].pointId == pointId)
              contextArray.splice(i, 1);
      }
      writeContextArray();
  }
  
  function writeContextArray() {
      dwr.util.removeAllRows("contextTable");
      if (contextArray.length == 0) {
          show($("contextTableEmpty"));
          hide($("contextTableHeaders"));
      }
      else {
          hide($("contextTableEmpty"));
          show($("contextTableHeaders"));
          dwr.util.addRows("contextTable", contextArray,
              [
                  function(data) { return data.pointName; },
                  function(data) { return data.pointType; },
                  function(data) {
                          return "<input type='text' value='"+ data.scriptVarName +"' class='formShort' "+
                                  "onblur='updateScriptVarName("+ data.pointId +", this.value)'/>";
                  },
                  function(data) { 
                          return "<img src='images/bullet_delete.png' class='ptr' "+
                                  "onclick='removeFromContextArray("+ data.pointId +")'/>";
                  }
              ],
              {
                  rowCreator:function(options) {
                      var tr = document.createElement("tr");
                      tr.className = "smRow"+ (options.rowIndex % 2 == 0 ? "" : "Alt");
                      return tr;
                  }
              });
      }
      updatePointsList();
  }
  
  function updatePointsList() {
      dwr.util.removeAllOptions("allPointsList");
      var availPoints = new Array();
      for (var i=0; i<pointsArray.length; i++) {
          var found = false;
          for (var j=0; j<contextArray.length; j++) {
              if (contextArray[j].pointId == pointsArray[i].id) {
                  found = true;
                  break;
              }
          }
          
          if (!found)
              availPoints[availPoints.length] = pointsArray[i];
      }
      dwr.util.addOptions("allPointsList", availPoints, "id", "name");
  }
  
  function updateScriptVarName(pointId, scriptVarName) {
      for (var i=contextArray.length-1; i>=0; i--) {
          if (contextArray[i].pointId == pointId)
              contextArray[i].scriptVarName = scriptVarName;
      }
  }
  
  function validateScript() {
      hideContextualMessages("pointProperties");
      DataSourceEditDwr.validateScript($get("script"), createContextArray(), $get("dataTypeId"), validateScriptCB);
  }
  
  function createContextArray() {
      var context = new Array();
      for (var i=0; i<contextArray.length; i++) {
          context[context.length] = {
              key : contextArray[i].pointId,
              value : contextArray[i].scriptVarName
          };
      }
      return context;
  }
  
  function validateScriptCB(response) {
      showDwrMessages(response.messages);
  }
  
  function updateEventChanged() {
     // display("updateCronPatternRow", $get("updateEvent") == <%= MetaPointLocatorVO.UPDATE_EVENT_CRON %>);
  }
</script>

<c:set var="dsDesc"><fmt:message key="dsEdit.meta.desc"/></c:set>
<c:set var="dsHelpId" value="metaDS"/>

  <tr>
    <td class="formLabelRequired"><fmt:message key="dsEdit.pointDataType"/></td>
    <td class="formField">
      <select id="dataTypeId">
        <tag:dataTypeOptions excludeImage="true"/>
      </select>
    </td>
  </tr>
  
  <tr>
    <td class="formLabelRequired"><fmt:message key="dsEdit.meta.scriptContext"/></td>
    <td class="formField">
      <select id="allPointsList"></select>
      <tag:img png="add" onclick="addPointToContext();" title="common.add"/>
      
      <table cellspacing="1" id="contextContainer">
        <tbody id="contextTableEmpty" style="display:none;">
          <tr><th colspan="4"><fmt:message key="dsEdit.meta.noPoints"/></th></tr>
        </tbody>
        <tbody id="contextTableHeaders" style="display:none;">
          <tr class="smRowHeader">
            <td><fmt:message key="dsEdit.meta.pointName"/></td>
            <td><fmt:message key="dsEdit.pointDataType"/></td>
            <td><fmt:message key="dsEdit.meta.var"/></td>
            <td></td>
          </tr>
        </tbody>
        <tbody id="contextTable"></tbody>
      </table>
    </td>
  </tr>
  
  <tr>
    <td class="formLabelRequired">
      <fmt:message key="dsEdit.meta.script"/> <tag:img png="accept" onclick="validateScript();" title="dsEdit.meta.validate"/>
    </td>
    <td class="formField"><textarea id="script" rows="10" cols="50"/></textarea></td>
  </tr>
  
  <tr>
    <td class="formLabelRequired"><fmt:message key="dsEdit.meta.event"/></td>
    <td class="formField">
      <select id="updateEvent" onchange="updateEventChanged()">
        <option value="<c:out value="<%= MetaPointLocatorVO.UPDATE_EVENT_CONTEXT_UPDATE %>"/>"><fmt:message key="dsEdit.meta.event.context"/></option>
        <option value="<c:out value="<%= Common.TimePeriods.MINUTES %>"/>"><fmt:message key="dsEdit.meta.event.minute"/></option>
        <option value="<c:out value="<%= Common.TimePeriods.HOURS %>"/>"><fmt:message key="dsEdit.meta.event.hour"/></option>
        <option value="<c:out value="<%= Common.TimePeriods.DAYS %>"/>"><fmt:message key="dsEdit.meta.event.day"/></option>
        <option value="<c:out value="<%= Common.TimePeriods.WEEKS %>"/>"><fmt:message key="dsEdit.meta.event.week"/></option>
        <option value="<c:out value="<%= Common.TimePeriods.MONTHS %>"/>"><fmt:message key="dsEdit.meta.event.month"/></option>
        <option value="<c:out value="<%= Common.TimePeriods.YEARS %>"/>"><fmt:message key="dsEdit.meta.event.year"/></option>
        <!-- <option value="<c:out value="<%= MetaPointLocatorVO.UPDATE_EVENT_CRON %>"/>"><fmt:message key="dsEdit.meta.event.cron"/></option>  -->
      </select>
    </td>
  </tr>
  
  
  <tr>
    <td class="formLabelRequired"><fmt:message key="dsEdit.meta.delay"/></td>
    <td class="formField"><input id="executionDelaySeconds" type="text" class="formShort"/></td>
  </tr>
