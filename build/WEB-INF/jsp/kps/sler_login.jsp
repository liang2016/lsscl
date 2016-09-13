<%--
    Mango - Open Source M2M - http://mango.serotoninsoftware.com
    Copyright (C) 2006-2011 Serotonin Software Technologies Inc.
    @author Matthew Lohbihler
    
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>
<%@page import="com.serotonin.mango.web.dwr.MiscDwr"%>
<tag:page onload="setFocus">
<c:if test='${mango:envBoolean("ssl.on", false)}'>
  <c:if test='${pageContext.request.scheme == "http"}'>
    <c:redirect url='https://${pageContext.request.serverName}:${mango:envString("ssl.port", "8443")}${requestScope["javax.servlet.forward.request_uri"]}'/>
  </c:if>
</c:if>

  <script type="text/javascript">
    function setFocus() {
        $("username").focus();
    }
    
    // check this user is logined or not
    function isLogined(){
    	var flag = true;
    	dwr.engine.setAsync(false);
    	MiscDwr.isLogined($get("username"),function(logined){
    		if(logined){
    			if(!confirm("<fmt:message key='login.validation.islogged' />")){
    				flag = false;
    			}
    		}
    	});
    	return flag;
    	dwr.engine.setAsync(true);
    }
	function clearInfo(){
		$set("username","");
		$set("password","");
		$set("yazhengma","");
	}
 </script>
<style>
.inputcss{
	border:1px solid #458B00;
    color: #333333;
    padding: 5px;
    font-family: Verdana,Arial,Helvetica,sans-serif;
    font-size: 18px;
    width: 200px;
    height: 30px;
}
.yanzhengma{
  	padding: 5px 0px 5px 5px;
	border: 1px solid #458B00;
    color: #333333;
    font-family: Verdana,Arial,Helvetica,sans-serif;
    font-size: 18px;
    width: 100px;
    height: 30px;
}
.textLogin{
    color: #006400;
    padding-right: 10px;
    font-weight: bold;
    padding-top: 5px;
    text-align: right;
    vertical-align: top;
    white-space: nowrap;
     font-size: 16px;
    font-weight: bold;
}
.buttoncss{
 background-color: #458B00;
    border: 2px outset #458B00;
    color: white;
    cursor: pointer;
    margin: 1px;
    overflow: visible;
    padding: 1px 0px;
    width: 80px;
    height: 36px;
}
</style>
<body style="background-color: #FFFFF">
<table width="100%"  cellspacing="0" cellpadding="0" border="0">
	<tr  style="background-color: #6ba53a">
		<td colspan="3" align="left"><img alt="kpsLogo" width="272px;" height="100px;" src="logos/sler.png"></img></td>
	</tr>
	<tr>
		<td align="right" width="350px;" style="background-color: #6ba53a" height="84px"></td>
		<td align="center" rowspan="2" style="width: 231px;height: 120px">
			<img style="padding:0px 0px 1px 0px;" alt="loginLogo" src="images/kps_login_logo.png">
		</td>
		<td align="left" style="background-color: #6ba53a"  height="84px"><b><span style="color:#ffffff;font-size: 30px;">&nbsp;&nbsp;&nbsp;&nbsp;<fmt:message key="login.sler.word"/></span></b></td>
	</tr>
	<tr width="100%" height="36px">
		<td></td>
		<td></td>
	</tr>
	<tr>
		<td colspan="3" height="36px"></td>
	</tr>
</table>
<center>
        <form  action="sler_login.htm" id="loginForm" method="post"  onSubmit="return true" >
          <table align="center">
             <spring:bind path="login">
              <c:if test="${status.error}">	
            	  <tr>
	                <td align="center" colspan="2" class="formError">
	                  <c:forEach items="${status.errorMessages}" var="error">
	                    <c:out value="${error}"/>
	                  </c:forEach>
	                </td>
                </tr>
              </c:if>
            </spring:bind>
            <spring:bind path="login.username">
            <tr>   
            	<td align="center" class="formError" colspan="2">${status.errorMessage}</td>
            </tr>
              <tr>
                <td align="left" class="textLogin"><fmt:message key="login.userId"/></td>
                <td align="left" class="formField">
                  <input id="username" class="inputcss" type="text" name="username" size="40" value="${status.value}" maxlength="40"/>
                </td>
              </tr>
            </spring:bind>
            
            <spring:bind path="login.password">
              <tr>
                <td class="textLogin"><fmt:message key="login.password"/></td>
                <td align="left" class="formField">
                  <input id="password" class="inputcss" type="password" name="password" value="${status.value}" maxlength="20"/>
                </td>
              </tr>
            </spring:bind>
             <!-- 
             <spring:bind path="login.yazhengma">
              <tr>
                <td class="textLogin"><fmt:message key="login.yazhengma"/></td>
                <td align="left" class="formField">
                  <input id="yazhengma" type="text" class="yanzhengma"  name="yazhengma"  maxlength="20"/>
              		<img id="code" align="right" style="padding:5px 5px 5px 0px;" onclick="document.getElementById('code').src='/loginCheckServlet.do?'+(new Date()).getTime()" src="/loginCheckServlet.do"/></td>
               	</tr> 
            </spring:bind>    
                 -->
            <tr>
            	<td colspan="2"></td>
            </tr>
            <tr>
            	<td></td>
              <td algin="left">
                <input type="submit"  class="buttoncss" value="<fmt:message key="login.loginButton"/>" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                <input type="button" algin="right" class="buttoncss" onclick="clearInfo()" value="<fmt:message key="login.reset"/>" />
              </td>
            </tr>
          </table>
        </form>
   </center>
<table align="center">
   <tr>
   	<td align="center"><span style="color: #006400;font-size: 12px;" ><fmt:message key="login.browser.notice"/></span></td>
   </tr>
   <tr>
   	<td align="center" height="60px"><span style="color: #006400;font-size: 18px; padding: 10px;" ><fmt:message key="login.sler.tel"/></span></td>
   </tr>
</table>
</body>  
</tag:page>