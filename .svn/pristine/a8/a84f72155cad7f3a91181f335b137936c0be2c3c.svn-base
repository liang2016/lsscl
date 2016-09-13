<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>
<mobileTag:page>
  <form action="mobile_login.htm" method="post">
    <table>
      <spring:bind path="login.username">
        <tr>
          <td class="formLabelRequired"><fmt:message key="login.userId"/></td>
          <td class="formField">
            <input id="username" type="text" name="username" value="${status.value}" maxlength="40"/>
          </td>
        </tr>
        <tr><td colspan="2" class="formError">${status.errorMessage}</td></tr>
      </spring:bind>
      
      <spring:bind path="login.password">
        <tr>
          <td class="formLabelRequired"><fmt:message key="login.password"/></td>
          <td class="formField">
            <input id="password" type="password" name="password" value="${status.value}" maxlength="20"/>
          </td>
          
        </tr>
        <tr><td colspan="2" class="formError">${status.errorMessage}</td></tr>
      </spring:bind>
          
      <spring:bind path="login">
        <c:if test="${status.error}">
          <td colspan="2" class="formError">
            <c:forEach items="${status.errorMessages}" var="error">
              <c:out value="${error}"/><br/>
            </c:forEach>
          </td>
        </c:if>
      </spring:bind>
      
      <tr>
        <td colspan="2" align="center">
          <input type="submit" value="<fmt:message key="login.loginButton"/>"/>
        </td>
        <td></td>
      </tr>
    </table>
  </form>
  <br/>
  <br/>
</mobileTag:page>