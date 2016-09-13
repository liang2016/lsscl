<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%><%@ include file="/WEB-INF/jsp/include/tech.jsp"%>
<%@page import="com.serotonin.mango.vo.health.HealthVo"%>
<table cellspacing="1" cellpadding="0" border="0">
	<tr class="smallTitle titlePadding">
		<td colspan="7">
		<tag:img png="add" id="add" title="health.add" onclick="showHealthCard()"/>
		</td>
		
	</tr>
	<tr class="rowHeader">
		<td>
			<fmt:message key="health.startTime" />
		</td>
		<td>
			<fmt:message key="health.type" />
		</td>
		<td>
			<fmt:message key="health.description" />
		</td>
		<td>
			<fmt:message key="health.message" />
		</td>
		<td>
			<fmt:message key="health.solve.message" />
		</td>
		<td>
			<fmt:message key="health.check.message" />
		</td>
		<td>
			<fmt:message key="health.edit" />
		</td>
	</tr>
	<c:forEach items="${cards}" var="content" varStatus="status">
		<tr class="row<c:if test="${status.index % 2 == 1}">Alt</c:if>">
			<td>
				${sst:time(content.startTime)} by <span style="color:red">${content. recorder.username}</span>created
			</td>
			<td
				<c:if test="${content.type==0}"> title="0">
					<fmt:message key="health.all"></fmt:message>
				</c:if>
				<c:if test="${content.type==1}"> title="1">
					<fmt:message key="health.normal"></fmt:message>
				</c:if>
				<c:if test="${content.type==2}"> title="2">
					<fmt:message key="health.abnormal"></fmt:message>
				</c:if>
				<c:if test="${content.type==3}"> title="3">
					<fmt:message key="health.error"></fmt:message>
				</c:if>

			</td>
			<td>
				${content.description}
			</td>
			<td>
				<table cellspacing="0" cellpadding="0" width="100%">
					<tr>
						<td colspan="2">
							<b id="cardMessage${content.id}">${content.message}</b>
						</td>
						<td align="right">
							<tag:img png="comment_add" title="notes.addNote"
								onclick="openCommentDialog(3, ${content.id})" />
							<!-- ${applicationScope['constants.UserComment.TYPR_HEALTH']} -->
						</td>
					</tr>
					<tbody id="healthComments${content.id}">
						<tag:comments comments="${content.comment}" />
					</tbody>
				</table>
			</td>
			<c:choose>
				<c:when test="${content.solveTime==0}">
					<td></td>
				</c:when>
				<c:otherwise>
					<td>
						${sst:time(content.solveTime)}  by <span style="color:red">${content. solvers.username}</span>
						<c:if test="${content.status==1}">
							<fmt:message key="health.solving"/>
						</c:if>
						<c:if test="${content.status==2||content.status==4}">
							<fmt:message key="health.solved"/>
						</c:if>
						<c:if test="${content.status==3}">
							<fmt:message key="health.repeat.solving"/>
						</c:if>
						
					</td>
				</c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${content.endTime==0}">
					<td></td>
				</c:when>
				<c:otherwise>
					<td>
						${sst:time(content.endTime)} by <span style="color:red">${content. checkUser.username}</span>
						<c:if test="${content.status==2}">
							<fmt:message key="health.checking"/>
						</c:if>
						<c:if test="${content.status==3}">
							<fmt:message key="health.checkBack"/>
						</c:if>
						<c:if test="${content.status==4}">
							<fmt:message key="health.checked"/>
						</c:if>
					</td>
				</c:otherwise>
			</c:choose>
			<td>
				<tag:img png="edit" title="common.edit"
					onclick="editHealth(this.parentNode,${content.id})" />
			</td>
		</tr>
	</c:forEach>
</table>
