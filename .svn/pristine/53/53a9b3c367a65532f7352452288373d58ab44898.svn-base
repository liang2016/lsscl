/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.dwr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

import org.joda.time.DateTime;

import com.serotonin.db.IntValuePair;
import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.DataSourceDao;
import com.serotonin.mango.db.dao.MaintenanceEventDao;
import com.serotonin.mango.rt.event.maintenance.MaintenanceEventRT;
import com.serotonin.mango.vo.dataSource.DataSourceVO;
import com.serotonin.mango.vo.event.MaintenanceEventVO;
import com.serotonin.mango.vo.permission.Permissions;
import com.serotonin.util.StringUtils;
import com.serotonin.web.dwr.DwrResponseI18n;
import com.serotonin.web.i18n.LocalizableMessage;

/**
 *  
 */
public class MaintenanceEventsDwr extends BaseDwr {
    public DwrResponseI18n getMaintenanceEvents() {
        Permissions.ensureAdmin();

        DwrResponseI18n response = new DwrResponseI18n();
        final ResourceBundle bundle = getResourceBundle();

        List<MaintenanceEventVO> events = new MaintenanceEventDao().getMaintenanceEvents();
        Collections.sort(events, new Comparator<MaintenanceEventVO>() {
            @Override
            public int compare(MaintenanceEventVO m1, MaintenanceEventVO m2) {
                return m1.getDescription().getLocalizedMessage(bundle)
                        .compareTo(m1.getDescription().getLocalizedMessage(bundle));
            }
        });
        response.addData("events", events);

        List<IntValuePair> dataSources = new ArrayList<IntValuePair>();
        for (DataSourceVO<?> ds : new DataSourceDao().getDataSources())
            dataSources.add(new IntValuePair(ds.getId(), ds.getName()));
        response.addData("dataSources", dataSources);

        return response;
    }

    public DwrResponseI18n getMaintenanceEvent(int id) {
        Permissions.ensureAdmin();

        DwrResponseI18n response = new DwrResponseI18n();

        MaintenanceEventVO me;
        boolean activated = false;
        if (id == Common.NEW_ID) {
            DateTime dt = new DateTime();
            me = new MaintenanceEventVO();
            me.setXid(new MaintenanceEventDao().generateUniqueXid());
            me.setActiveYear(dt.getYear());
            me.setInactiveYear(dt.getYear());
            me.setActiveMonth(dt.getMonthOfYear());
            me.setInactiveMonth(dt.getMonthOfYear());
        }
        else {
            me = new MaintenanceEventDao().getMaintenanceEvent(id);

            MaintenanceEventRT rt = Common.ctx.getRuntimeManager().getRunningMaintenanceEvent(me.getId());
            if (rt != null)
                activated = rt.isEventActive();
        }

        response.addData("me", me);
        response.addData("activated", activated);

        return response;
    }

    public DwrResponseI18n saveMaintenanceEvent(int id, String xid, int dataSourceId, String alias, int alarmLevel,
            int scheduleType, boolean disabled, int activeYear, int activeMonth, int activeDay, int activeHour,
            int activeMinute, int activeSecond, String activeCron, int inactiveYear, int inactiveMonth,
            int inactiveDay, int inactiveHour, int inactiveMinute, int inactiveSecond, String inactiveCron) {
        Permissions.ensureAdmin();

        MaintenanceEventVO e = new MaintenanceEventVO();
        e.setId(id);
        e.setXid(xid);
        e.setDataSourceId(dataSourceId);
        e.setAlias(alias);
        e.setAlarmLevel(alarmLevel);
        e.setScheduleType(scheduleType);
        e.setDisabled(disabled);
        e.setActiveYear(activeYear);
        e.setActiveMonth(activeMonth);
        e.setActiveDay(activeDay);
        e.setActiveHour(activeHour);
        e.setActiveMinute(activeMinute);
        e.setActiveSecond(activeSecond);
        e.setActiveCron(activeCron);
        e.setInactiveYear(inactiveYear);
        e.setInactiveMonth(inactiveMonth);
        e.setInactiveDay(inactiveDay);
        e.setInactiveHour(inactiveHour);
        e.setInactiveMinute(inactiveMinute);
        e.setInactiveSecond(inactiveSecond);
        e.setInactiveCron(inactiveCron);

        DwrResponseI18n response = new DwrResponseI18n();
        MaintenanceEventDao maintenanceEventDao = new MaintenanceEventDao();

        if (StringUtils.isEmpty(xid))
            response.addContextualMessage("xid", "validate.required");
        else if (!maintenanceEventDao.isXidUnique(xid, id))
            response.addContextualMessage("xid", "validate.xidUsed");

        e.validate(response);

        // Save the maintenance event
        if (!response.getHasMessages()) {
           // Common.ctx.getRuntimeManager().saveMaintenanceEvent(e);
            response.addData("meId", e.getId());
        }

        return response;
    }

    public void deleteMaintenanceEvent(int meId) {
        Permissions.ensureAdmin();
       // Common.ctx.getRuntimeManager().deleteMaintenanceEvent(meId);
    }

    public DwrResponseI18n toggleMaintenanceEvent(int id) {
        Permissions.ensureAdmin();
        DwrResponseI18n response = new DwrResponseI18n();

        MaintenanceEventRT rt = Common.ctx.getRuntimeManager().getRunningMaintenanceEvent(id);
        boolean activated = false;
        if (rt == null)
            response.addMessage(new LocalizableMessage("maintenanceEvents.toggle.disabled"));
        else
            activated = rt.toggle();

        response.addData("activated", activated);

        return response;
    }
}
