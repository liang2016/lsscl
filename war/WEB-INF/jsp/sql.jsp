<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>

<tag:page onload="setFocus">
  <script type="text/javascript">
    function setFocus() {
        $("sqlString").focus();
    }
  </script>
  
  <table width="100%" cellspacing="0" cellpadding="0" border="0">
    <tr>
      <td>
        <form action="sql.shtm" method="post">
          <table>
            <tr>
              <td colspan="2"><fmt:message key="sql.warning"/></td>
            </tr>
            <spring:bind path="form.sqlString">
              <tr>
                <td class="formLabelRequired">
                  <fmt:message key="sql.sql"/>
                  <tag:help id="directQuerying"/>
                </td>
                <td><textarea id="sqlString" name="sqlString" rows="8" cols="80">${status.value}</textarea></td>
              </tr>
              <tr>
                <td colspan="2" class="formError">${status.errorMessage}</td>
              </tr>
            </spring:bind>
            
            <tr>
              <td colspan="2" align="center">
                <input type="submit" value="<fmt:message key="sql.query"/>" name="query"/>
                <input type="submit" value="<fmt:message key="sql.update"/>" name="update"/>
              </td>
              <td></td>
            </tr>
          </table>
        </form>
        <br/>
        
        <c:if test="${form.data != null}">
          <table cellspacing="1">
            <tr class="rowHeader">
              <c:forEach items="${form.headers}" var="rowHeader">
                <td>${rowHeader}</td>
              </c:forEach>
            </tr>
            
            <c:forEach items="${form.data}" var="row">
              <tr class="row">
                <c:forEach items="${row}" var="col">
                  <td>${col}</td>
                </c:forEach>
              </tr>
            </c:forEach>
          </table>
        </c:if>
        
        <c:if test="${form.updateResult > -1}">
          ${form.updateResult} <fmt:message key="sql.rowsUpdated"/>
        </c:if>
        
        <br/>
      </td>
    </tr>
  </table>
</tag:page>