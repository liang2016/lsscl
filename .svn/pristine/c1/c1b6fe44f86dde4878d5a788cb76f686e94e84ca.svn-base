<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>
<script type="text/javascript">
  var dataTypeId = ${form.pointLocator.dataTypeId};
</script>
<script type="text/javascript">
  function doSave(taskName) {
      $("taskName").name = taskName;
      textRendererEditor.save(doSaveChartRenderer);
      return false;
  }
  function doSaveChartRenderer() {
      chartRendererEditor.save(doSavePointEventDetectors);
  }
  function doSavePointEventDetectors() {
      pointEventDetectorEditor.save(doSaveForm);
  }
  function doSaveForm() {
      document.forms[0].submit();
  }
</script>

<table width="100%">
  <tr>
    <td valign="top">
      <table width="100%" cellspacing="0" cellpadding="0" border="0">
        <spring:bind path="form">
          <c:if test="${status.error}">
            <tr><td colspan="2" class="formError">${status.errorMessage}</td></tr>
          </c:if>
        </spring:bind>
      </table>
    </td>
    <td valign="top" align="right">
      <fmt:message key="pointEdit.name.goto"/>:&nbsp;
      <sst:select value="${form.id}" onchange="window.location='data_point_edit.shtm?dpid='+ this.value+'&factoryId=${sessionUser.currentScope.id}';">
        <c:forEach items="${userPoints}" var="point">
          <sst:option value="${point.id}">${point.extendedName}</sst:option>
        </c:forEach>
      </sst:select>
      
      <c:if test="${!empty prevId}">
        <tag:img png="bullet_go_left" title="pagination.previous"
                onclick="window.location='data_point_edit.shtm?dpid=${prevId}&factoryId=${sessionUser.currentScope.id}'"/>
      </c:if>
      
      <c:if test="${!empty nextId}">
        <tag:img png="bullet_go" title="pagination.next"
                onclick="window.location='data_point_edit.shtm?dpid=${nextId}&factoryId=${sessionUser.currentScope.id}'"/>
      </c:if>
    </td>
  </tr>
</table>
