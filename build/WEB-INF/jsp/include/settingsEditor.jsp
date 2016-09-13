<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>
<div id="settingsEditorPopup" style="display:none;left:0px;top:0px;" class="windowDiv">
  <table cellpadding="0" cellspacing="0"><tr><td>
    <table width="100%">
      <tr>
        <td>
          <tag:img png="plugin_edit" title="viewEdit.settings.editor" style="display:inline;"/>
          <span class="smallTitle" id="settingsComponentName"></span>
        </td>
        <td align="right">
          <tag:img png="save" onclick="settingsEditor.save()" title="common.save" style="display:inline;"/>&nbsp;
          <tag:img png="cross" onclick="settingsEditor.close()" title="common.close" style="display:inline;"/>
        </td>
      </tr>
    </table>
    <table>
      <tr>
        <td class="formLabelRequired"><fmt:message key="viewEdit.settings.point"/></td>
        <td class="formField"><select id="settingsPointList" onchange="settingsEditor.pointSelectChanged()"></select></td>
      </tr>
      <tr>
        <td class="formLabel"><fmt:message key="viewEdit.settings.nameOverride"/></td>
        <td class="formField"><input id="settingsPointName" type="text"/></td>
      </tr>
      <tr>
        <td class="formLabel"><fmt:message key="viewEdit.settings.settableOverride"/></td>
        <td class="formField"><input id="settingsSettable" type="checkbox"/></td>
      </tr>
      <tr>
		<td></td>
      	<td>
      	<div id="color" style="display:none;"></div>
      	</td>
      </tr>
      <tr>
        <td class="formLabel"><fmt:message key="viewEdit.settings.displayControls"/></td>
        <td class="formField"><input id="settingsControls" type="checkbox"/></td>
      </tr>
      <tr>
      	<td class="formLabel">
      	<fmt:message key="viewEdit.settings.fontSize"/>
      	</td>
      	<td class="formField">
	      	<select id="fontSize">
	      	  <c:forEach varStatus="index" begin="8" end="40">
	      	  <option value="${index.index }">
	      	  ${ index.index}
	      	  </option>
	      	  </c:forEach>
      		</select>
      	</td>
      </tr>
      <tr>
      <td class="formLabel">
      	<fmt:message key="viewEdit.settings.fontWeight"/>
      </td>
      <td class="formField">
      	<select id="fontWeight">
		      	<option value="normal">
		      		<fmt:message key="viewEdit.settings.normal"/>
				</option>
				<option value="bold">
		     		<fmt:message key="viewEdit.settings.bold"/>
				</option>
      		</select>
      </td>
      </tr>
       <tr>
        <td class="formLabel"><fmt:message key="viewEdit.settings.background"/></td>
        <td class="formField"><input type="text"  id="settingsBkgdColor" class="color" value="#FFFF00"></td>
      </tr>
    </table>
  </td></tr></table>
  <script type="text/javascript">
    // Script requires
    //  - Drag and Drop library for locating objects and positioning the window.
    //  - DWR utils for using $() prototype.
    //  - common.js
    function SettingsEditor() {
        this.componentId = null;
        this.pointList = [];
        
        this.open = function(compId) {
            settingsEditor.componentId = compId;
            ViewDwr.getViewComponent(compId, function(comp) {
                $set("settingsComponentName", comp.displayName);
                
                // Update the point list
                settingsEditor.updatePointList(comp.supportedDataTypes);
                
                // Update the data in the form.
                $set("settingsPointList", comp.dataPointId);
                $set("settingsPointName", comp.nameOverride);
                $set("settingsSettable", comp.settableOverride);
                $set("settingsBkgdColor", comp.bkgdColorOverride);
                $("settingsBkgdColor").style.backgroundColor=comp.bkgdColorOverride;
                $set("settingsControls", comp.displayControls);
                $set("fontSize", comp.fontSize);
                $set("fontWeight", comp.fontWeight);
                settingsEditor.pointSelectChanged();
                show("settingsEditorPopup");
            });
            
            positionEditor(compId, "settingsEditorPopup");
        };
        
        this.close = function() {
            hide("settingsEditorPopup");
            hideContextualMessages("settingsEditorPopup");
        };
        
        this.save = function() {
            hideContextualMessages("settingsEditorPopup");
            ViewDwr.setPointComponentSettings(settingsEditor.componentId, $get("settingsPointList"),
                    $get("settingsPointName"), $get("settingsSettable"), $get("settingsBkgdColor"),
                    $get("settingsControls"),$get("fontSize"),$get("fontWeight"), function(response) {
                if (response.hasMessages) {
                    showDwrMessages(response.messages);
                }
                else {
                    settingsEditor.close();
                    MiscDwr.notifyLongPoll(mango.longPoll.pollSessionId);
                }
            });
        };
        
        this.setPointList = function(pointList) {
            settingsEditor.pointList = pointList;
        };
        
        this.pointSelectChanged = function() {
            var point = getElement(settingsEditor.pointList, $get("settingsPointList"));
            if (!point || !point.settable) {
                $set("settingsSettable", false);
                $("settingsSettable").disabled = true;
            }
            else
                $("settingsSettable").disabled = false;
        };
        
        this.updatePointList = function(dataTypes) {
            dwr.util.removeAllOptions("settingsPointList");
            var sel = $("settingsPointList");
            sel.options[0] = new Option("", 0);
            
            for (var i=0; i<settingsEditor.pointList.length; i++) {
                if (contains(dataTypes, settingsEditor.pointList[i].dataType))
                    sel.options[sel.options.length] = new Option(settingsEditor.pointList[i].name,
                            settingsEditor.pointList[i].id);
            }
        };
    }
    var settingsEditor = new SettingsEditor();
  </script>
  <style>
		body{ margin:0; padding:20px 0 0 50px; font-size:14px; background:#ccc; color:#000; font-family:simsun, arial;}
		div,ul,ol,li,dl,dt,dd,form,img,p{ margin:0; padding:0; border:0;}
		li { list-style-type:none;}
		h1,h2,h3,h4,h5,input{ margin:0; padding:0; letter-spacing:1px;}
		table,tr,td,th{ font-size:12px;}
		textarea{ overflow:auto;}
		
		#color{ width:235px; padding:0 0 1px 0; background:#fff; overflow:hidden; margin-bottom:30px;}
		#color ul{ width:78px; float:left; display:inline; background:#fff; overflow:hidden;}
		#color li{ float:left; display:inline; width:12px; height:12px; margin:1px 0 0 1px; background:#808080;}
		#color li a{ display:block; margin:1px 0 0 1px; width:11px; height:11px; overflow:hidden;}
</style>
 <script type="text/javascript" src="../js/script.js"></script>
</div>