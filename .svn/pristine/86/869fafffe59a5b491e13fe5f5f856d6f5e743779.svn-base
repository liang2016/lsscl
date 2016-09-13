<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
 
     
--%>
<%@ include file="/WEB-INF/jsp/include/tech.jsp" %>
<tag:page dwr="ViewDwr" js="view">
  <script type="text/javascript" src="resources/wz_jsgraphics.js"></script>
  <script type="text/javascript">
    <c:if test="${!empty currentView}">
      mango.view.initNormalView();
    </c:if>
        
    function unshare() {
        ViewDwr.deleteViewShare(function() { window.location = 'views.shtm?factoryId=${sessionUser.currentScope.id}'; });
    }
  </script>
  
  <table class="borderDiv">
    <tr>
      <td class="smallTitle"><fmt:message key="views.title"/> <tag:help id="graphicalViews"/></td>
      <td width="50"></td>
      <td align="right">
        <sst:select value="${currentView.id}" onchange="window.location='?viewId='+this.value+'&factoryId=${sessionUser.currentScope.id}'">
          <c:forEach items="${views}" var="aView">
            <sst:option value="${aView.key}">${sst:escapeLessThan(aView.value)}</sst:option>
          </c:forEach>
        </sst:select>
        <c:if test="${!empty currentView}">
          <c:choose>
            <c:when test="${owner||sessionUser.admin||sessionUser.tempAdmin}">
              <a href="view_edit.shtm?viewId=${currentView.id}&factoryId=${sessionUser.currentScope.id}"><tag:img png="icon_view_edit" title="viewEdit.editView"/></a>
            </c:when>
            <c:otherwise>
             <!--  <tag:img png="icon_view_delete" title="viewEdit.deleteView" onclick="unshare()"/>--> 
            </c:otherwise>
          </c:choose>
        </c:if>
        <a href="view_edit.shtm?factoryId=${sessionUser.currentScope.id}"><tag:img png="icon_view_new" title="views.newView"/></a>
      </td>
    </tr>
  </table>
  
  <tag:displayView view="${currentView}" emptyMessageKey="views.noViews"/>
</tag:page>