<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>
<div id="linkEditorPopup" style="display:none;left:0px;top:0px;" class="windowDiv">
  <table cellpadding="0" cellspacing="0"><tr><td>
    <table width="100%">
      <tr>
        <td>
          <tag:img png="html" title="viewEdit.link.editor" style="display:inline;"/>
        </td>
        <td align="right">
          <tag:img png="save" onclick="linkEditor.save()" title="common.save" style="display:inline;"/>&nbsp;
          <tag:img png="cross" onclick="linkEditor.close()" title="common.close" style="display:inline;"/>
        </td>
      </tr>
    </table>
    <table>
      <tr>
        <td class="formField">
         <select id="views">
         </select>
        </td>
      </tr>
    </table>
  </td></tr></table>
  
  <script type="text/javascript">
    function LinkEditor() {
        this.componentId = null;
        
        this.open = function(compId) {
            linkEditor.componentId = compId;
            
            ViewDwr.getViewComponent(compId, function(comp) {
				$set("views",comp.viewId);
                show("linkEditorPopup");
            });
            
            positionEditor(compId, "linkEditorPopup");
        };
        
        this.close = function() {
            hide("linkEditorPopup");
        };
        this.addOptions = function(views){
        	var viewSelect=$("views");
        	for(var i=0;i<views.length;i++){
        		viewSelect.options[viewSelect.length] = new Option(views[i].value, views[i].key);
        	}
        };
        this.save = function() {
        var view=$("views");
        var factoryId=${sessionUser.currentScope.id};
        var content=view.options[view.selectedIndex].text;
            ViewDwr.saveLinkComponent(linkEditor.componentId, content,view.value,factoryId, function() {
                linkEditor.close();
                updateLinkComponentContent("c"+ linkEditor.componentId,content,view.value,factoryId);
            });
        };
    }
    var linkEditor = new LinkEditor();
  </script>
</div>