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
import com.serotonin.mango.vo.event.CompoundEventDetectorVO;

/**
 *  
 */
public class CompoundEventDetectorDao extends BaseDao {
    private static final String COMPOUND_EVENT_DETECTOR_SELECT = "select id, xid, name, alarmLevel, returnToNormal, disabled, conditionText from compoundEventDetectors ";

    public String generateUniqueXid() {
        return generateUniqueXid(CompoundEventDetectorVO.XID_PREFIX, "compoundEventDetectors");
    }

    public boolean isXidUnique(String xid, int excludeId) {
        return isXidUnique(xid, excludeId, "compoundEventDetectors");
    }

    public List<CompoundEventDetectorVO> getCompoundEventDetectors() {
        return query(COMPOUND_EVENT_DETECTOR_SELECT + "order by name", new CompoundEventDetectorRowMapper());
    }
    /**
     * get CompoundEvent by scope
     * @return
     */
    public List<CompoundEventDetectorVO> getCompoundEventDetectors(int scopeId) {
        return query(COMPOUND_EVENT_DETECTOR_SELECT + "where scopeId=? order by name",new Object[]{scopeId}, new CompoundEventDetectorRowMapper());
    }
    public CompoundEventDetectorVO getCompoundEventDetector(int id) {
        return queryForObject(COMPOUND_EVENT_DETECTOR_SELECT + "where id=?", new Object[] { id },
                new CompoundEventDetectorRowMapper());
    }

    public CompoundEventDetectorVO getCompoundEventDetector(String xid) {
        return queryForObject(COMPOUND_EVENT_DETECTOR_SELECT + "where xid=?", new Object[] { xid },
                new CompoundEventDetectorRowMapper(), null);
    }

    class CompoundEventDetectorRowMapper implements GenericRowMapper<CompoundEventDetectorVO> {
        public CompoundEventDetectorVO mapRow(ResultSet rs, int rowNum) throws SQLException {
            CompoundEventDetectorVO ced = new CompoundEventDetectorVO();
            int i = 0;
            ced.setId(rs.getInt(++i));
            ced.setXid(rs.getString(++i));
            ced.setName(rs.getString(++i));
            ced.setAlarmLevel(rs.getInt(++i));
            ced.setReturnToNormal(charToBool(rs.getString(++i)));
            ced.setDisabled(charToBool(rs.getString(++i)));
            ced.setCondition(rs.getString(++i));
            return ced;
        }
    }

    public void saveCompoundEventDetector(final CompoundEventDetectorVO ced) {
        if (ced.getId() == Common.NEW_ID)
            insertCompoundEventDetector(ced);
        else
            updateCompoundEventDetector(ced);
    }

    private static final String COMPOUND_EVENT_DETECTOR_INSERT = "insert into compoundEventDetectors (xid, name, alarmLevel, returnToNormal, disabled, conditionText,scopeId) "
            + "values (?,?,?,?,?,?,?)";

    private void insertCompoundEventDetector(CompoundEventDetectorVO ced) {
        int id = doInsert(COMPOUND_EVENT_DETECTOR_INSERT, new Object[] { ced.getXid(), ced.getName(),
                ced.getAlarmLevel(), boolToChar(ced.isReturnToNormal()), boolToChar(ced.isDisabled()),
                ced.getCondition(),ced.getScopeId() });
        ced.setId(id);
        AuditEventType.raiseAddedEvent(AuditEventType.TYPE_COMPOUND_EVENT_DETECTOR, ced);
    }

    private static final String COMPOUND_EVENT_DETECTOR_UPDATE = "update compoundEventDetectors set xid=?, name=?, alarmLevel=?, returnToNormal=?, disabled=?, conditionText=? "
            + "where id=?";

    private void updateCompoundEventDetector(CompoundEventDetectorVO ced) {
        CompoundEventDetectorVO old = getCompoundEventDetector(ced.getId());

        ejt.update(COMPOUND_EVENT_DETECTOR_UPDATE, new Object[] { ced.getXid(), ced.getName(), ced.getAlarmLevel(),
                boolToChar(ced.isReturnToNormal()), boolToChar(ced.isDisabled()), ced.getCondition(), ced.getId() });

        AuditEventType.raiseChangedEvent(AuditEventType.TYPE_COMPOUND_EVENT_DETECTOR, old, ced);

    }

    public void deleteCompoundEventDetector(final int compoundEventDetectorId) {
        final ExtendedJdbcTemplate ejt2 = ejt;
        CompoundEventDetectorVO ced = getCompoundEventDetector(compoundEventDetectorId);
        if (ced != null) {
            getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    ejt2.update("delete from eventHandlers where eventTypeId=" + EventType.EventSources.COMPOUND
                            + " and eventTypeRef1=?", new Object[] { compoundEventDetectorId });
                    ejt2.update("delete from compoundEventDetectors where id=?",
                            new Object[] { compoundEventDetectorId });
                }
            });

            AuditEventType.raiseDeletedEvent(AuditEventType.TYPE_COMPOUND_EVENT_DETECTOR, ced);
        }
    }
}
