<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@page import="com.serotonin.mango.view.ShareUser"%>
<%@page import="com.serotonin.mango.db.dao.power.RoleDao"%>
		<hr /> 
		current user is : <b>${sessionUser.username}</b> ;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		current role of user is :<b> ${sessionUser.currentRole.rolename};</b><br>
		the role list of user : <b>
		<c:forEach items="${sessionUser.roleList}" var="role">
                   ${role.rolename }.&nbsp;&nbsp;
		</c:forEach></b><br>  
		the current scope of user is :<b> ${sessionUser.currentScope.scopename }</b>;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		the native  socpe of user is :<b>${sessionUser.homeScope.scopename };</b><br> 
		<hr />
