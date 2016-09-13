<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%><%@include file="/WEB-INF/tags/decl.tagf"%><%--
--%><%@tag body-content="empty"%>
<table>
  <tr>
    <td><fmt:message key="common.dateRangeFrom"/></td>
    <td><input type="text" id="fromYearS" class="formVeryShort" value="${fromYear}"/></td>
    <td><sst:select id="fromMonthS" value="${fromMonth}"><tag:monthOptions sst="true"/></sst:select></td>
    <td><sst:select id="fromDayS" value="${fromDay}"><tag:dayOptions sst="true"/></sst:select></td>
    <td>,</td>
    <td><sst:select id="fromHourS" value="${fromHour}"><tag:hourOptions sst="true"/></sst:select></td>
    <td>:</td>
    <td><sst:select id="fromMinuteS" value="${fromMinute}"><tag:minuteOptions sst="true"/></sst:select></td>
    <td>:</td>
    <td><sst:select id="fromSecondS" value="${fromSecond}"><tag:secondOptions sst="true"/></sst:select></td>
    <td><input type="checkbox" name="fromNoneS" id="fromNoneS" onclick="updateDateRangeS()"/><label
            for="fromNoneS"><fmt:message key="common.inception"/></label></td>
  </tr>
  <tr>
    <td><fmt:message key="common.dateRangeTo"/></td>
    <td><input type="text" id="toYearS" class="formVeryShort" value="${toYear}"/></td>
    <td><sst:select id="toMonthS" value="${toMonth}"><tag:monthOptions sst="true"/></sst:select></td>
    <td><sst:select id="toDayS" value="${toDay}"><tag:dayOptions sst="true"/></sst:select></td>
    <td>,</td>
    <td><sst:select id="toHourS" value="${toHour}"><tag:hourOptions sst="true"/></sst:select></td>
    <td>:</td>
    <td><sst:select id="toMinuteS" value="${toMinute}"><tag:minuteOptions sst="true"/></sst:select></td>
    <td>:</td>
    <td><sst:select id="toSecondS" value="${toSecond}"><tag:secondOptions sst="true"/></sst:select></td>
    <td><input type="checkbox" name="toNoneS" id="toNoneS" checked="checked" onclick="updateDateRangeS()"/><label
            for="toNoneS"><fmt:message key="common.latest"/></label></td>
  </tr>
</table>
