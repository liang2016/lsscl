/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.db.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.serotonin.db.spring.ExtendedJdbcTemplate;
import com.serotonin.db.spring.GenericRowMapper;
import com.serotonin.mango.Common;
import com.serotonin.mango.rt.event.type.AuditEventType;
import com.serotonin.mango.rt.event.type.EventType;
import com.serotonin.mango.vo.event.ScheduledEventVO;

/**
 *  
 * 
 */
public class ScheduledEventDao extends BaseDao {
    private static final String SCHEDULED_EVENT_SELECT = "select id, xid, alias, alarmLevel, scheduleType, "
            + "  returnToNormal, disabled, activeYear, activeMonth, activeDay, activeHour, activeMinute, activeSecond, "
            + "  activeCron, inactiveYear, inactiveMonth, inactiveDay, inactiveHour, inactiveMinute, inactiveSecond, "
            + "inactiveCron from scheduledEvents ";

    public String generateUniqueXid() {
        return generateUniqueXid(ScheduledEventVO.XID_PREFIX, "scheduledEvents");
    }

    public boolean isXidUnique(String xid, int excludeId) {
        return isXidUnique(xid, excludeId, "scheduledEvents");
    }

    public List<ScheduledEventVO> getScheduledEvents() {
        return query(SCHEDULED_EVENT_SELECT + " order by scheduleType", new ScheduledEventRowMapper());
    }
    //get ScheduledEventVO by scopeId
    public List<ScheduledEventVO> getScheduledEvents(int scopeId) {
        return query(SCHEDULED_EVENT_SELECT + " where scopeId=? order by scheduleType", new Object[]{scopeId}, new ScheduledEventRowMapper());
    }
    public ScheduledEventVO getScheduledEvent(int id) {
        ScheduledEventVO se = queryForObject(SCHEDULED_EVENT_SELECT + "where id=?", new Object[] { id },
                new ScheduledEventRowMapper());
        return se;
    }

    public ScheduledEventVO getScheduledEvent(String xid) {
        return queryForObject(SCHEDULED_EVENT_SELECT + "where xid=?", new Object[] { xid },
                new ScheduledEventRowMapper(), null);
    }

    class ScheduledEventRowMapper implements GenericRowMapper<ScheduledEventVO> {
        public ScheduledEventVO mapRow(ResultSet rs, int rowNum) throws SQLException {
            ScheduledEventVO se = new ScheduledEventVO();
            int i = 0;
            se.setId(rs.getInt(++i));
            se.setXid(rs.getString(++i));
            se.setAlias(rs.getString(++i));
            se.setAlarmLevel(rs.getInt(++i));
            se.setScheduleType(rs.getInt(++i));
            se.setReturnToNormal(charToBool(rs.getString(++i)));
            se.setDisabled(charToBool(rs.getString(++i)));
            se.setActiveYear(rs.getInt(++i));
            se.setActiveMonth(rs.getInt(++i));
            se.setActiveDay(rs.getInt(++i));
            se.setActiveHour(rs.getInt(++i));
            se.setActiveMinute(rs.getInt(++i));
            se.setActiveSecond(rs.getInt(++i));
            se.setActiveCron(rs.getString(++i));
            se.setInactiveYear(rs.getInt(++i));
            se.setInactiveMonth(rs.getInt(++i));
            se.setInactiveDay(rs.getInt(++i));
            se.setInactiveHour(rs.getInt(++i));
            se.setInactiveMinute(rs.getInt(++i));
            se.setInactiveSecond(rs.getInt(++i));
            se.setInactiveCron(rs.getString(++i));
            return se;
        }
    }

    public void saveScheduledEvent(final ScheduledEventVO se) {
        if (se.getId() == Common.NEW_ID)
            insertScheduledEvent(se);
        else
            updateScheduledEvent(se);
    }

    private void insertScheduledEvent(ScheduledEventVO se) {
        se
                .setId(doInsert(
                        "insert into scheduledEvents ("
                                + "  xid, alarmLevel, alias, scheduleType, returnToNormal, disabled, "
                                + "  activeYear, activeMonth, activeDay, activeHour, activeMinute, activeSecond, activeCron, "
                                + "  inactiveYear, inactiveMonth, inactiveDay, inactiveHour, inactiveMinute, inactiveSecond, inactiveCron,scopeid "
                                + ") values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new Object[] { se.getXid(),
                                se.getAlarmLevel(), se.getAlias(), se.getScheduleType(),
                                boolToChar(se.isReturnToNormal()), boolToChar(se.isDisabled()), se.getActiveYear(),
                                se.getActiveMonth(), se.getActiveDay(), se.getActiveHour(), se.getActiveMinute(),
                                se.getActiveSecond(), se.getActiveCron(), se.getInactiveYear(), se.getInactiveMonth(),
                                se.getInactiveDay(), se.getInactiveHour(), se.getInactiveMinute(),
                                se.getInactiveSecond(), se.getInactiveCron() ,se.getScopeId()}));
        AuditEventType.raiseAddedEvent(AuditEventType.TYPE_SCHEDULED_EVENT, se);
    }

    private void updateScheduledEvent(ScheduledEventVO se) {
        ScheduledEventVO old = getScheduledEvent(se.getId());
        ejt
                .update(
                        "update scheduledEvents set "
                                + "  xid=?, alarmLevel=?, alias=?, scheduleType=?, returnToNormal=?, disabled=?, "
                                + "  activeYear=?, activeMonth=?, activeDay=?, activeHour=?, activeMinute=?, activeSecond=?, activeCron=?, "
                                + "  inactiveYear=?, inactiveMonth=?, inactiveDay=?, inactiveHour=?, inactiveMinute=?, inactiveSecond=?, "
                                + "  inactiveCron=? " + "where id=?", new Object[] { se.getXid(), se.getAlarmLevel(),
                                se.getAlias(), se.getScheduleType(), boolToChar(se.isReturnToNormal()),
                                boolToChar(se.isDisabled()), se.getActiveYear(), se.getActiveMonth(),
                                se.getActiveDay(), se.getActiveHour(), se.getActiveMinute(), se.getActiveSecond(),
                                se.getActiveCron(), se.getInactiveYear(), se.getInactiveMonth(), se.getInactiveDay(),
                                se.getInactiveHour(), se.getInactiveMinute(), se.getInactiveSecond(),
                                se.getInactiveCron(), se.getId() });
        AuditEventType.raiseChangedEvent(AuditEventType.TYPE_SCHEDULED_EVENT, old, se);
    }

    public void deleteScheduledEvent(final int scheduledEventId) {
        ScheduledEventVO se = getScheduledEvent(scheduledEventId);
        final ExtendedJdbcTemplate ejt2 = ejt;
        if (se != null) {
            getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    ejt2.update("delete from eventHandlers where eventTypeId=" + EventType.EventSources.SCHEDULED
                            + " and eventTypeRef1=?", new Object[] { scheduledEventId });
                    ejt2.update("delete from scheduledEvents where id=?", new Object[] { scheduledEventId });
                }
            });

            AuditEventType.raiseDeletedEvent(AuditEventType.TYPE_SCHEDULED_EVENT, se);
        }
    }
}
