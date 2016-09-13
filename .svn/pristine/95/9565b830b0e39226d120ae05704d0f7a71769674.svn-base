/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.dwr;

import java.util.List;

import org.joda.time.DateTime;

import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.ScheduledEventDao;
import com.serotonin.mango.vo.event.ScheduledEventVO;
import com.serotonin.mango.vo.permission.Permissions;
import com.serotonin.util.StringUtils;
import com.serotonin.web.dwr.DwrResponseI18n;

/**
 *  
 * 
 */
public class ScheduledEventsDwr extends BaseDwr {
    //
    // /
    // / Public methods
    // /
    //
    public List<ScheduledEventVO> getScheduledEvents(int scopeId) {
        Permissions.ensureDataSourcePermission(Common.getUser());
        return new ScheduledEventDao().getScheduledEvents(scopeId);
    }

    public ScheduledEventVO getScheduledEvent(int id) {
        Permissions.ensureDataSourcePermission(Common.getUser());

        if (id == Common.NEW_ID) {
            DateTime dt = new DateTime();
            ScheduledEventVO se = new ScheduledEventVO();
            se.setXid(new ScheduledEventDao().generateUniqueXid());
            se.setActiveYear(dt.getYear());
            se.setInactiveYear(dt.getYear());
            se.setActiveMonth(dt.getMonthOfYear());
            se.setInactiveMonth(dt.getMonthOfYear());
            return se;
        }
        return new ScheduledEventDao().getScheduledEvent(id);
    }
    //修改时间
    public ScheduledEventVO setStartTime(int hours) {
    	DateTime dt=new DateTime(System.currentTimeMillis());
    	dt=dt.plusHours(hours);
    	ScheduledEventVO se = new ScheduledEventVO();
    	se.setActiveYear(dt.getYear());
        se.setInactiveYear(dt.getYear());
        
        se.setActiveMonth(dt.getMonthOfYear());
        se.setInactiveMonth(dt.getMonthOfYear());
        
        se.setActiveDay(dt.getDayOfMonth());
        se.setInactiveDay(dt.getDayOfMonth());
        
        se.setActiveHour(dt.getHourOfDay());
        se.setInactiveHour(dt.getHourOfDay()+1);
        
        se.setActiveMinute(dt.getMinuteOfHour());
        se.setInactiveMinute(dt.getMinuteOfHour());
        
        se.setActiveSecond(0);
        se.setInactiveSecond(0);
        return se;
    }

    public DwrResponseI18n saveScheduledEvent(int id, String xid, String alias, int alarmLevel, int scheduleType,
            boolean returnToNormal, boolean disabled, int activeYear, int activeMonth, int activeDay, int activeHour,
            int activeMinute, int activeSecond, String activeCron, int inactiveYear, int inactiveMonth,
            int inactiveDay, int inactiveHour, int inactiveMinute, int inactiveSecond, String inactiveCron,int scopeId) {
        Permissions.ensureDataSourcePermission(Common.getUser());

        // Validate the given information. If there is a problem, return an appropriate error message.
        ScheduledEventVO se = new ScheduledEventVO();
        se.setId(id);
        se.setXid(xid);
        se.setAlias(alias);
        se.setAlarmLevel(alarmLevel);
        se.setScheduleType(scheduleType);
        se.setReturnToNormal(returnToNormal);
        se.setDisabled(disabled);
        se.setActiveYear(activeYear);
        se.setActiveMonth(activeMonth);
        se.setActiveDay(activeDay);
        se.setActiveHour(activeHour);
        se.setActiveMinute(activeMinute);
        se.setActiveSecond(activeSecond);
        se.setActiveCron(activeCron);
        se.setInactiveYear(inactiveYear);
        se.setInactiveMonth(inactiveMonth);
        se.setInactiveDay(inactiveDay);
        se.setInactiveHour(inactiveHour);
        se.setInactiveMinute(inactiveMinute);
        se.setInactiveSecond(inactiveSecond);
        se.setInactiveCron(inactiveCron);
        se.setScopeId(scopeId);
        DwrResponseI18n response = new DwrResponseI18n();
        ScheduledEventDao scheduledEventDao = new ScheduledEventDao();

        if (StringUtils.isEmpty(xid))
            response.addContextualMessage("xid", "validate.required");
        else if (!scheduledEventDao.isXidUnique(xid, id))
            response.addContextualMessage("xid", "validate.xidUsed");

        se.validate(response);

        // Save the scheduled event
        if (!response.getHasMessages())
            Common.ctx.getRuntimeManager().saveScheduledEvent(se);

        response.addData("seId", se.getId());
        return response;
    }

    public void deleteScheduledEvent(int seId) {
        Permissions.ensureDataSourcePermission(Common.getUser());
        new ScheduledEventDao().deleteScheduledEvent(seId);
        Common.ctx.getRuntimeManager().stopSimpleEventDetector(ScheduledEventVO.getEventDetectorKey(seId));
    }
}
