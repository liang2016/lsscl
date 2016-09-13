<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%><%@ include file="/WEB-INF/jsp/include/tech.jsp" %>
<c:if test="${!empty events || !noContentWhenEmpty}">
  <c:if test="${displayPagination}">
    <div style="padding:3px; float:left;">
      <c:choose>
        <c:when test="${page == 0}">1</c:when>
        <c:otherwise><a href="#" onclick="jumpToPage(0);return false;">1</a></c:otherwise>
      </c:choose>
      <c:if test="${leftEllipsis}">...</c:if>
      <c:forEach begin="${linkFrom}" end="${linkTo}" var="i">
        <c:choose>
          <c:when test="${i-1 == page}">${i}</c:when>
          <c:otherwise><a href="#" onclick="jumpToPage(${i-1});return false;">${i}</a></c:otherwise>
        </c:choose>
      </c:forEach>
      <c:if test="${rightEllipsis}">...</c:if>
      <c:choose>
        <c:when test="${page == numberOfPages-1}">${numberOfPages}</c:when>
        <c:otherwise><a href="#" onclick="jumpToPage(${numberOfPages-1});return false;">${numberOfPages}</a></c:otherwise>
      </c:choose>
      
      <c:choose>
        <c:when test="${page <= 0}">&lt;</c:when>
        <c:otherwise><a href="#" onclick="jumpToPage(${page-1});return false;">&lt;</a></c:otherwise>
      </c:choose>
      <c:choose>
        <c:when test="${page + 1 >= numberOfPages}">&gt;</c:when>
        <c:otherwise><a href="#" onclick="jumpToPage(${page+1});return false;">&gt;</a></c:otherwise>
      </c:choose>
    </div>
  </c:if>
  <c:if test="${!empty events && !pendingEvents}">
    <div style="padding:3px; float:left;">
      <c:if test="${displayPagination}">|</c:if>
		<fmt:message key="events.listed"><fmt:param value="${fn:length(events)}"/></fmt:message>
    </div>
  </c:if>
  <div style="clear:both;"></div>
  
  <table cellspacing="1" cellpadding="0" border="0">
    <tr class="rowHeader">
      <td><fmt:message key="events.id"/></td>
      <td><fmt:message key="common.alarmLevel"/></td>
      <td><fmt:message key="common.time"/></td>
      <td><fmt:message key="events.msg"/></td>
      <td><fmt:message key="common.inactiveTime"/></td>
      <c:if test="${!pendingEvents}"><td><fmt:message key="events.acknowledged"/></td></c:if>
      <td></td>
    </tr>
    <c:if test="${empty events}"><tr><td colspan="6"><b><fmt:message key="events.emptyList"/></b></td></tr></c:if>
    <c:forEach items="${events}" var="event" varStatus="status">
      <tr class="row<c:if test="${status.index % 2 == 1}">Alt</c:if>">
        <td align="center">${event.id}</td>
        <td align="center"><tag:eventIcon event="${event}"/></td>
        <td>${sst:time(event.activeTimestamp)}</td>
        <td>
          <table cellspacing="0" cellpadding="0" width="100%">
            <tr>
              <td colspan="2"><b><sst:i18n message="${event.message}"/></b></td>
              <td align="right">
                <tag:img png="comment_add" title="notes.addNote"
                        onclick="openCommentDialog(${applicationScope['constants.UserComment.TYPE_EVENT']}, ${event.id})"/>
              </td>
            </tr>
            <tbody id="eventComments${event.id}"><tag:comments comments="${event.eventComments}"/></tbody>
          </table>
        </td>
        <td>
          <c:choose>
            <c:when test="${event.active}">
              <fmt:message key="common.active"/>
                <c:if test="${sessionUser.currentScope.scopetype==0}"><a href="center_events.shtm"></c:if>
		        <c:if test="${sessionUser.currentScope.scopetype==1}"><a href="zone_events.shtm?zoneId=${sessionUser.currentScope.id}"></c:if>
		        <c:if test="${sessionUser.currentScope.scopetype==2}"><a href="subzone_events.shtm?subzoneId=${sessionUser.currentScope.id}"></c:if>
		        <c:if test="${sessionUser.currentScope.scopetype==3}"><a href="factory_events.shtm?factoryId=${sessionUser.currentScope.id}"></c:if>
                <tag:img png="flag_white" title="common.active"/></a>
            </c:when>
            <c:when test="${!event.rtnApplicable}"><fmt:message key="common.nortn"/></c:when>
            <c:otherwise>
              ${sst:time(event.rtnTimestamp)} - <sst:i18n message="${event.rtnMessage}"/>
            </c:otherwise>
          </c:choose>
        </td>
        <c:if test="${!pendingEvents}">
          <td>
            <c:if test="${event.acknowledged}">
              ${sst:time(event.acknowledgedTimestamp)}
              <sst:i18n message="${event.ackMessage}"/>
            </c:if>
          </td>
        </c:if>
        <td style="white-space:nowrap;">
          <c:if test="${pendingEvents}">
            <tag:alarmAck event="${event}"/>
          </c:if>
          <c:choose>
            <c:when test="${event.eventType.eventSourceId == applicationScope['constants.EventType.EventSources.DATA_POINT']}">
              <a href="data_point_details.shtm?dpid=${event.eventType.dataPointId}&factoryId=${event.scope.id}"><tag:img png="icon_comp" title="events.pointDetails"/></a>
            </c:when>
            <c:when test="${event.eventType.eventSourceId == applicationScope['constants.EventType.EventSources.DATA_SOURCE']}">
              <a href="data_source_edit.shtm?dsid=${event.eventType.dataSourceId}&factoryId=${event.scope.id}"><tag:img png="icon_ds_edit" title="events.editDataSource"/></a>
            </c:when>
            <c:when test="${event.eventType.eventSourceId == applicationScope['constants.EventType.EventSources.SYSTEM']}">
              <c:choose>
                <c:when test="${event.eventType.systemEventTypeId == applicationScope['constants.SystemEventType.TYPE_VERSION_CHECK']}">
                  <a href="http://www.lsscl.com/download.jsp" target="_blank"><tag:img png="bullet_down" title="events.downloadMango"/></a>
                </c:when>
                <c:when test="${event.eventType.systemEventTypeId == applicationScope['constants.SystemEventType.TYPE_COMPOUND_DETECTOR_FAILURE']}">
                  <a href="compound_events.shtm?cedid=${event.eventType.referenceId2}&factoryId=${event.scope.id}"><tag:img png="multi_bell" title="events.editCompound"/></a>
                </c:when>
                <c:when test="${event.eventType.systemEventTypeId == applicationScope['constants.SystemEventType.TYPE_SET_POINT_HANDLER_FAILURE']}">
                  <a href="event_handlers.shtm?ehid=${event.eventType.referenceId2}&factoryId=${event.scope.id}"><tag:img png="cog" title="events.editEventHandler"/></a>
                </c:when>
                <c:when test="${event.eventType.systemEventTypeId == applicationScope['constants.SystemEventType.TYPE_POINT_LINK_FAILURE']}">
                  <a href="point_links.shtm?plid=${event.eventType.referenceId2}&factoryId=${event.scope.id}"><tag:img png="link" title="events.editPointLink"/></a>
                </c:when>
              </c:choose>
            </c:when>
            <c:when test="${event.eventType.eventSourceId == applicationScope['constants.EventType.EventSources.COMPOUND']}">
              <a href="compound_events.shtm?cedid=${event.eventType.compoundEventDetectorId}&factoryId=${event.scope.id}"><tag:img png="multi_bell" title="events.editCompound"/></a>
            </c:when>
            <c:when test="${event.eventType.eventSourceId == applicationScope['constants.EventType.EventSources.SCHEDULED']}">
              <a href="scheduled_events.shtm?seid=${event.eventType.scheduleId}&factoryId=${event.scope.id}"><tag:img png="clock" title="events.editScheduledEvent"/></a>
            </c:when>
            <c:when test="${event.eventType.eventSourceId == applicationScope['constants.EventType.EventSources.PUBLISHER']}">
              <a href="publisher_edit.shtm?pid=${event.eventType.publisherId}&factoryId=${event.scope.id}"><tag:img png="transmit_edit" title="events.editPublisher"/></a>
            </c:when>
            <c:when test="${event.eventType.eventSourceId == applicationScope['constants.EventType.EventSources.AUDIT']}">
              <c:choose>
                <c:when test="${event.eventType.auditEventTypeId == applicationScope['constants.AuditEventType.TYPE_DATA_SOURCE']}">
                  <a href="data_source_edit.shtm?dsid=${event.eventType.referenceId2}&factoryId=${event.scope.id}"><tag:img png="icon_ds_edit" title="events.editDataSource"/></a>
                </c:when>
                <c:when test="${event.eventType.auditEventTypeId == applicationScope['constants.AuditEventType.TYPE_DATA_POINT']}">
                  <a href="data_point_edit.shtm?dpid=${event.eventType.referenceId2}&factoryId=${event.scope.id}"><tag:img png="icon_comp_edit" title="events.pointEdit"/></a>
                  <a href="data_source_edit.shtm?pid=${event.eventType.referenceId2}&factoryId=${event.scope.id}"><tag:img png="icon_ds_edit" title="events.editDataSource"/></a>
                </c:when>
                <c:when test="${event.eventType.auditEventTypeId == applicationScope['constants.AuditEventType.TYPE_POINT_EVENT_DETECTOR']}">
                  <a href="data_point_edit.shtm?pedid=${event.eventType.referenceId2}&factoryId=${event.scope.id}"><tag:img png="icon_comp_edit" title="events.pointEdit"/></a>
                </c:when>
                <c:when test="${event.eventType.auditEventTypeId == applicationScope['constants.AuditEventType.TYPE_COMPOUND_EVENT_DETECTOR']}">
                  <a href="compound_events.shtm?cedid=${event.eventType.referenceId2}&factoryId=${event.scope.id}"><tag:img png="multi_bell" title="events.editCompound"/></a>
                </c:when>
                <c:when test="${event.eventType.auditEventTypeId == applicationScope['constants.AuditEventType.TYPE_SCHEDULED_EVENT']}">
                  <a href="scheduled_events.shtm?seid=${event.eventType.referenceId2}&factoryId=${event.scope.id}"><tag:img png="clock" title="events.editScheduledEvent"/></a>
                </c:when>
                <c:when test="${event.eventType.auditEventTypeId == applicationScope['constants.AuditEventType.TYPE_EVENT_HANDLER']}">
                  <a href="event_handlers.shtm?ehid=${event.eventType.referenceId2}&factoryId=${event.scope.id}"><tag:img png="cog" title="events.editEventHandler"/></a>
                </c:when>
                <c:when test="${event.eventType.auditEventTypeId == applicationScope['constants.AuditEventType.TYPE_POINT_LINK']}">
                  <a href="point_links.shtm?plid=${event.eventType.referenceId2}&factoryId=${event.scope.id}"><tag:img png="link" title="events.editPointLink"/></a>
                </c:when>
              </c:choose>
            </c:when>
            <c:when test="${event.eventType.eventSourceId == applicationScope['constants.EventType.EventSources.MAINTENANCE']}">
              <a href="maintenance_events.shtm?meid=${event.eventType.maintenanceId}&factoryId=${event.scope.id}"><tag:img png="hammer" title="events.editMaintenanceEvent"/></a>
            </c:when>
            <c:otherwise>(unknown event source id ${event.eventType.eventSourceId})</c:otherwise>
          </c:choose>
        </td>
      </tr>
    </c:forEach>
  </table>
</c:if>