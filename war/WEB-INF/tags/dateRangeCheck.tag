<%--
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
     
--%><%@include file="/WEB-INF/tags/decl.tagf"%><%--
--%><%@tag body-content="empty"%>
<table>
  <tr>
    <td><fmt:message key="common.dateRangeFrom"/></td>
    <td><input type="text" id="fromYearC" class="formVeryShort" value="${fromYear}"/></td>
    <td><sst:select id="fromMonthC" value="${fromMonth}"><tag:monthOptions sst="true"/></sst:select></td>
    <td><sst:select id="fromDayC" value="${fromDay}"><tag:dayOptions sst="true"/></sst:select></td>
    <td>,</td>
    <td><sst:select id="fromHourC" value="${fromHour}"><tag:hourOptions sst="true"/></sst:select></td>
    <td>:</td>
    <td><sst:select id="fromMinuteC" value="${fromMinute}"><tag:minuteOptions sst="true"/></sst:select></td>
    <td>:</td>
    <td><sst:select id="fromSecondC" value="${fromSecond}"><tag:secondOptions sst="true"/></sst:select></td>
    <td><input type="checkbox" name="fromNoneC" id="fromNoneC" onclick="updateDateRangeC()"/><label
            for="fromNoneC"><fmt:message key="common.inception"/></label></td>
  </tr>
  <tr>
    <td><fmt:message key="common.dateRangeTo"/></td>
    <td><input type="text" id="toYearC" class="formVeryShort" value="${toYear}"/></td>
    <td><sst:select id="toMonthC" value="${toMonth}"><tag:monthOptions sst="true"/></sst:select></td>
    <td><sst:select id="toDayC" value="${toDay}"><tag:dayOptions sst="true"/></sst:select></td>
    <td>,</td>
    <td><sst:select id="toHourC" value="${toHour}"><tag:hourOptions sst="true"/></sst:select></td>
    <td>:</td>
    <td><sst:select id="toMinuteC" value="${toMinute}"><tag:minuteOptions sst="true"/></sst:select></td>
    <td>:</td>
    <td><sst:select id="toSecondC" value="${toSecond}"><tag:secondOptions sst="true"/></sst:select></td>
    <td><input type="checkbox" name="toNoneC" id="toNoneC" checked="checked" onclick="updateDateRangeC()"/><label
            for="toNoneC"><fmt:message key="common.latest"/></label></td>
  </tr>
</table>
