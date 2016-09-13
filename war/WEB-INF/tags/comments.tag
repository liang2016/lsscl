<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%><%@include file="/WEB-INF/tags/decl.tagf" %><%--
--%><%@tag body-content="empty" %><%--
--%><%@attribute name="comments" type="java.util.List" required="true" %><%--
--%><c:forEach items="${comments}" var="comment">
  <tr>
    <td valign="top" width="16"><tag:img png="comment" title="notes.note"/></td>
    <td valign="top">
      <span class="copyTitle">
        ${comment.prettyTime} <fmt:message key="notes.by"/>
        <c:choose>
          <c:when test="${empty comment.username}"><fmt:message key="common.deleted"/></c:when>
          <c:otherwise>${comment.username}</c:otherwise>
        </c:choose>
      </span><br/>
      ${comment.comment}
    </td>
  </tr>
</c:forEach>