<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp"%>
<%@page import="com.serotonin.mango.Common"%>

<tag:page dwr="UsersDwr" onload="init">
	<script type="text/javascript">
    var userId = ${sessionUser.id};
    var editingUserId;
    var zoneList;
    var adminUser;
    function init() {
        UsersDwr.getInitData(${sessionUser.currentScope.id},${sessionUser.currentScope.scopetype},function(data) {
             zoneList=data.zoneList;
             setUserZone(zoneList);
            if (data.admin) {
                adminUser = true;
                show("userList");
                show("usernameRow");
                hide("administrationRow");
                show("disabledRow");
                show("deleteImg");
                show("zoneRow");
                show("sendTestEmailImg");
                
                var i, j;
                for (i=0; i<data.users.length; i++) {
                    appendUser(data.users[i].id);
                    updateUser(data.users[i]);
                }
            }
            else {
                // Not an admin user.
                adminUser = false;
                editingUserId = data.user.id;
                showUserCB(data.user);
                UsersDwr.getUserZoneList(data.user.id, showScopeDB);
                hide("zoneRow");
            }
        });
    }
    function setUserZone(zoneList){
         var zonehtml = "";
         var id="";
          for (i=0; i<zoneList.length; i++) {
              id = "zone"+ zoneList[i].id;
              zonehtml  += "<input type='checkbox' onclick='checkSetzoneSync("+zoneList[i].id+")' id='"+ id +"'>";
              zonehtml  += "<label for='"+ id +"'> "+ zoneList[i].scopename +"</label><br/>";
              zonehtml  += "<div style='margin-left:25px;' id='zone"+ zoneList[i].id +"'>";
              zonehtml  += "<input type='checkbox' onclick='checkSync("+zoneList[i].id+")' id='set"+ id +"'>";
              zonehtml  += "<label for='set"+ id +"'> "+ "<fmt:message key='users.set'/>" +"</label><br/>";
              zonehtml  += "</div>";
          }
         $("zone").innerHTML = zonehtml ;
    }
   //let scope checkbox and set checkbox sync
    function checkSync(zoneId){
		zoneId="zone"+zoneId;
    	var isSetId="set"+zoneId;
    	if($(isSetId).checked){
    		$(zoneId).checked=true;
    	}
    	else{
    		$(zoneId).checked=false;
    	}
    }
    //if scope not selected,set checkbox not selected 
    function checkSetzoneSync(zoneId){
		zoneId="zone"+zoneId;
   		var isSetId="set"+zoneId;
    	if(!$(zoneId).checked){
    		$(isSetId).checked=false;
    	}
    }
    function showUser(userId) {
        if (editingUserId)
            stopImageFader($("u"+ editingUserId +"Img"));
        editingUserId = userId;
        UsersDwr.getUser(userId, showUserCB);
        startImageFader($("u"+ editingUserId +"Img"));
        UsersDwr.getUserZoneList(userId, showScopeDB);
    }
 	function showScopeDB(data){
 		var i,j;
 	 	for (i=0; i<zoneList.length; i++) {
               var dscb = $("zone"+ zoneList[i].id);
                dscb.checked = false;
 	  		for (j=0; j<data.length; j++) {
               var scope= $("zone"+ data[j].id);
                scope.checked = true;
                var isSet=$("setzone"+ data[j].id);
                if(data[j].userIsSet){
                	isSet.checked = true;
                }
                else{
                	isSet.checked = false;
                }
           	 }
 	 	}    
 	}
    function showUserCB(user) {
        show($("userDetails"));
        $set("username", user.username);
        $set("password", user.password);
        $set("email", user.email);
        $set("phone", user.phone);
        $set("administrator", user.admin);
        $set("disabled", user.disabled);
        /*$set("receiveAlarmEmails", user.receiveAlarmEmails);
        $set("receiveOwnAuditEvents", user.receiveOwnAuditEvents);*/
        
        if (!user.admin){
       	   	show("zoneRow");
       	   	}
        else
        	hide("zoneRow");
        setUserMessage();
        updateUserImg();
    }
    
    function saveUser() {
        setUserMessage();
        if (adminUser) {
            // Create the list of allowed data sources and data point permissions.
            var i;
            var zonePermis = new Array();
            var zoneSets = new Array();
            for(i=0;i<zoneList.length;i++){
             	 if ($("zone"+ zoneList[i].id).checked){
                    zonePermis[zonePermis .length] = zoneList[i].id;
                    zoneSets[zoneSets.length]=$("setzone"+ zoneList[i].id).checked;
                 }
            }
 
    		UsersDwr.saveScopeUserAdmin(editingUserId, $get("username"), $get("password"), $get("email"), $get("phone"), 
                    $get("administrator"), $get("disabled"), 0, false,
                    zonePermis,zoneSets,${sessionUser.currentScope.scopetype}, saveUserCB);
           
       	}else{
            UsersDwr.saveUser(editingUserId, $get("password"), $get("email"), $get("phone"),
                    0, false, saveUserCB);
    	}
    }
    function saveUserCB(response) {
        if (response.hasMessages)
            showDwrMessages(response.messages, "genericMessages");
        else if (!adminUser)
            setUserMessage("<fmt:message key="users.dataSaved"/>");
        else {
            if (editingUserId == <c:out value="<%=Common.NEW_ID%>"/>) {
                stopImageFader($("u"+ editingUserId +"Img"));
                editingUserId = response.data.userId;
                appendUser(editingUserId);
                startImageFader($("u"+ editingUserId +"Img"));
                setUserMessage("<fmt:message key="users.added"/>");
            }
            else
                setUserMessage("<fmt:message key="users.saved"/>");
            UsersDwr.getUser(editingUserId, updateUser)
        }
    }
    
    function sendTestEmail() {
        UsersDwr.sendTestEmail($get("email"), $get("username"), function(result) {
            stopImageFader($("sendTestEmailImg"));
            if (result.exception)
                setUserMessage(result.exception);
            else
                setUserMessage(result.message);
        });
        startImageFader($("sendTestEmailImg"));
    }
    
    function setUserMessage(message) {
        if (message)
            $set("userMessage", message);
        else {
            $set("userMessage");
            hideContextualMessages("userDetails");
            hideGenericMessages("genericMessages");
        }
    }
    
    function appendUser(userId) {
        createFromTemplate("u_TEMPLATE_", userId, "usersTable");
    }
    
    function updateUser(user) {
        $("u"+ user.id +"Username").innerHTML = user.username;
        setUserImg(user.admin, user.disabled, $("u"+ user.id +"Img"));
    }
    
    function updateUserImg() {
        var admin = $get("administrator");
        setUserImg(admin, $get("disabled"), $("userImg"));
    }
    
    function deleteUser() {
        if (confirm("<sst:i18n key="users.deleteConfirm" escapeDQuotes="true"/>")) {
        	var userId = editingUserId;
            startImageFader("deleteImg");
            UsersDwr.deleteUser(userId, function(response) {
                stopImageFader("deleteImg");
                
                if (response.hasMessages)
                	setUserMessage(response.messages[0].genericMessage);
                else {
                    stopImageFader("u"+ userId +"Img");
                    $("usersTable").removeChild($("u"+ userId));
                    hide("userDetails");
                    editingUserId = null;
                }
            });
        }
    }
  </script>
 <table>
    <tr>
      <td valign="top" id="userList" style="display:none;">
        <div class="borderDiv">
          <table width="100%">
            <tr>
              <td>
                <span class="smallTitle"><fmt:message key="users.title"/></span>
                <tag:help id="userAdministration"/>
              </td>
              <td align="right"><tag:img png="user_add" onclick="showUser(${applicationScope['constants.Common.NEW_ID']})"
                      title="users.add" id="u${applicationScope['constants.Common.NEW_ID']}Img"/></td>
            </tr>
          </table>
          <table id="usersTable">
            <tbody id="u_TEMPLATE_" onclick="showUser(getMangoId(this))" class="ptr" style="display:none;"><tr>
              <td><tag:img id="u_TEMPLATE_Img" png="user_green" title="users.user"/></td>
              <td class="link" id="u_TEMPLATE_Username"></td>
            </tr></tbody>
          </table>
        </div>
      </td>
      
      <td valign="top" style="display:none;" id="userDetails">
        <div class="borderDiv">
          <table width="100%">
            <tr>
              <td>
                <span class="smallTitle"><tag:img id="userImg" png="user_green" title="users.user"/>
                <fmt:message key="users.details"/></span>
              </td>
              <td align="right">
                <tag:img png="save" onclick="saveUser();" title="common.save"/>
                <tag:img id="deleteImg" png="delete" onclick="deleteUser();" title="common.delete" style="display:none;"/>
                <tag:img id="sendTestEmailImg" png="email_go" onclick="sendTestEmail();" title="common.sendTestEmail"
                        style="display:none;"/>
              </td>
            </tr>
          </table>
          
          <table><tbody id="genericMessages"></tbody></table>
          
          <table>
            <tr>
              <td colspan="2" id="userMessage" class="formError"></td>
            </tr>
            <tr id="usernameRow" style="display:none;">
              <td class="formLabelRequired"><fmt:message key="users.username"/></td>
              <td class="formField"><input id="username" type="text"/></td>
            </tr>
            <tr>
              <td class="formLabelRequired"><fmt:message key="users.newPassword"/></td>
              <td class="formField"><input id="password" type="text"/></td>
            </tr>
            <tr>
              <td class="formLabelRequired"><fmt:message key="users.email"/></td>
              <td class="formField"><input id="email" type="text" class="formLong"/></td>
            </tr>
            <tr>
              <td class="formLabelRequired"><fmt:message key="users.phone"/></td>
              <td class="formField"><input id="phone" type="text"/></td>
            </tr>
            <tr id="administrationRow" style="display:none;">
              <td class="formLabelRequired"><fmt:message key="common.administrator"/></td>
              <td class="formField"><input id="administrator" type="checkbox" onclick="updateUserImg();"/></td>
            </tr>
            <tr id="disabledRow" style="display:none;">
              <td class="formLabelRequired"><fmt:message key="common.disabled"/></td>
              <td class="formField"><input id="disabled" type="checkbox" onclick="updateUserImg();"/></td>
            </tr>
<!--        <tr>
              <td class="formLabelRequired"><fmt:message key="users.receiveAlarmEmails"/></td>
              <td class="formField"><select id="receiveAlarmEmails"><tag:alarmLevelOptions/></select></td>
            </tr>
	 		<tr>
              <td class="formLabelRequired"><fmt:message key="users.receiveOwnAuditEvents"/></td>
              <td class="formField"><input id="receiveOwnAuditEvents" type="checkbox"/></td>
            </tr>
 -->   
            <tbody id="zoneRow" style="display:none;">
              <tr><td class="horzSeparator" colspan="2"></td></tr>
              <tr id="zoneTd">
                <td class="formLabelRequired"><fmt:message key="scope.name"></fmt:message> </td>
                <td class="formField" id="zone"></td>
              </tr>
            </tbody>
          </table>
        </div>
      </td>
    </tr>
  </table>
</tag:page>