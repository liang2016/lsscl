/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.db.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.serotonin.ShouldNeverHappenException;
import com.serotonin.db.spring.ExtendedJdbcTemplate;
import com.serotonin.db.spring.GenericRowMapper;
import com.serotonin.db.spring.GenericTransactionCallback;
import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.scope.ScopeDao;
import com.serotonin.mango.rt.event.EventInstance;
import com.serotonin.mango.rt.event.type.AuditEventType;
import com.serotonin.mango.rt.event.type.CompoundDetectorEventType;
import com.serotonin.mango.rt.event.type.DataPointEventType;
import com.serotonin.mango.rt.event.type.DataSourceEventType;
import com.serotonin.mango.rt.event.type.EventType;
import com.serotonin.mango.rt.event.type.MaintenanceEventType;
import com.serotonin.mango.rt.event.type.PublisherEventType;
import com.serotonin.mango.rt.event.type.ScheduledEventType;
import com.serotonin.mango.rt.event.type.SystemEventType;
import com.serotonin.mango.vo.EmailTempVO;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.vo.UserComment;
import com.serotonin.mango.vo.event.EventHandlerVO;
import com.serotonin.mango.vo.event.EventTypeVO;
import com.serotonin.mango.vo.scope.ScopeVO;
import com.serotonin.mango.web.dwr.EventsDwr;
import com.serotonin.util.SerializationHelper;
import com.serotonin.util.StringUtils;
import com.serotonin.web.i18n.LocalizableMessage;
import com.serotonin.web.i18n.LocalizableMessageParseException;


public class EventDao extends BaseDao {
    private static final int MAX_PENDING_EVENTS = 100;

    public void saveEvent(EventInstance event) {
        if (event.getId() == Common.NEW_ID)
            insertEvent(event);
        else
            updateEvent(event);
    }

    public EventDao() {
		super();
	}

    public EventDao(DataSource dataSource) {
		super(dataSource);
	}    
	//修改insert语句，将所在范围ID插入
    private static final String EVENT_INSERT = "insert into events (typeId, typeRef1, typeRef2, activeTs, rtnApplicable, rtnTs, rtnCause, "
            + "  alarmLevel, message, ackTs,scopeid) " + "values (?,?,?,?,?,?,?,?,?,?,?)";
    private static final int[] EVENT_INSERT_TYPES = { Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.BIGINT,
            Types.CHAR, Types.BIGINT, Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.BIGINT,Types.INTEGER };

    private void insertEvent(EventInstance event) {
        EventType type = event.getEventType();
		Object[] args = new Object[11];//参数由原来的10个变成11个
        args[0] = type.getEventSourceId();
        args[1] = type.getReferenceId1();
        args[2] = type.getReferenceId2();
        args[3] = event.getActiveTimestamp();
        args[4] = boolToChar(event.isRtnApplicable());
        if (!event.isActive()) {
            args[5] = event.getRtnTimestamp();
            args[6] = event.getRtnCause();
        }
        args[7] = event.getAlarmLevel();
        args[8] = event.getMessage().serialize();
        if (!event.isAlarm()) {
            event.setAcknowledgedTimestamp(event.getActiveTimestamp());
            args[9] = event.getAcknowledgedTimestamp();
        }
        /**********************以下代码为根据事件类型(前3个参数)判断当前事件所属范围ID******************/
        /**********************既然可以通过判断来得到范围ID，为何还要放入数据库中？*********************/
        /**********************为了方便事件的查询(假设将来事件有很多，根据判断则会消耗大量资源) **********/
        ScopeDao scopeDao = new ScopeDao();
        int args10 = -1;//-1表示未知事件
        Integer args1 = (Integer)args[1];
        Integer args0 = (Integer)args[0];
        Integer args2 = (Integer)args[2];
        switch(args0){
        	case EventType.EventSources.DATA_POINT://数据点事件
        		args10=scopeDao.findScopeIdByDataPoint(args1);
        		break;
        	case EventType.EventSources.DATA_SOURCE://数据源事件
        		args10=scopeDao.findScopeIdByDataSource(args1);
        		break;
        	case EventType.EventSources.SYSTEM://系统事件
	            switch(args1){
	            	case SystemEventType.TYPE_SYSTEM_STARTUP://系统开启
	            		args10=scopeDao.findHQ().getId();//范围ID为总部ID
	            		break;
	            	case SystemEventType.TYPE_SYSTEM_SHUTDOWN://系统关闭
	            		args10=scopeDao.findHQ().getId();//范围ID为总部ID
	            		break;
	            	case SystemEventType.TYPE_MAX_ALARM_LEVEL_CHANGED://最大警告级别发生变化
	            		args10=scopeDao.findHQ().getId();//范围ID为总部ID
	            		break;
	            	case SystemEventType.TYPE_USER_LOGIN://用户登陆
	            		args10=scopeDao.findScopeIdByUser(args2);
	            		break;
	            	case SystemEventType.TYPE_VERSION_CHECK://版本检测
	            		args10=scopeDao.findHQ().getId();//范围ID为总部ID
	            		break;
	            	case SystemEventType.TYPE_COMPOUND_DETECTOR_FAILURE://组合事件失败
	            		args10=scopeDao.findScopeIdByCompound(args1);
	            		break;
	            	case SystemEventType.TYPE_SET_POINT_HANDLER_FAILURE://点赋予负责人失败?????????????
	            		break;
	            	case SystemEventType.TYPE_EMAIL_SEND_FAILURE://邮件发送失败 ???????????????????????
	            		break;
	            	case SystemEventType.TYPE_POINT_LINK_FAILURE://点连接失败
	            		args10=scopeDao.findScopeIdByPointLink(args1);
	            		break;
//	            	case SystemEventType.TYPE_PROCESS_FAILURE://process 失败??????????????????????????
//	            		break;
	            }
	            break;
        	case EventType.EventSources.COMPOUND://组合事件
        		args10=scopeDao.findScopeIdByCompound(args1);
            	break;
        	case EventType.EventSources.SCHEDULED://定时事件
        		args10=scopeDao.findScopeIdByScheduled(args1);
        		break;
        	case EventType.EventSources.PUBLISHER://发布事件
        		return;//发布事件不在数据库中记录
        	case EventType.EventSources.AUDIT://审计事件
        		return;//审计事件不在数据库中记录
        	case EventType.EventSources.MAINTENANCE://维护事件
        		return;//维护事件不在数据库中记录
        }
        args[10] = args10;
        /**********************************范围判断完毕*************************************************/
        int id = doInsert(EVENT_INSERT, args, EVENT_INSERT_TYPES);
        event.setId(id);
        /**
         * 手机报警历史
         */
        //doInsert("insert into mobileEvents(eid,cTime)values(?,?)", new Object[]{id,new Date()});
        event.setEventComments(new LinkedList<UserComment>());
    }

    private static final String EVENT_UPDATE = "update events set rtnTs=?, rtnCause=? where id=?";
    private static final String UPDATE_EVENT_EMAIL="update events set emailHandler=? where id=?";
    public void updateEventEmail(int isSendEmail,int id) {
		ejt.update(UPDATE_EVENT_EMAIL,new Object[]{isSendEmail,id});
	}
    public void updateEventEmailReturnNormal(int id) {
		ejt.update("update events set emailHandler=null where id=?",new Object[]{id});
	}
    private void updateEvent(EventInstance event) {
        ejt.update(EVENT_UPDATE, new Object[] { event.getRtnTimestamp(), event.getRtnCause(), event.getId() });
        updateCache(event);
    }
    public void deleteByScope(int scopeid) {
    	ejt.update("delete from events where scopeId=?", new Object[] { scopeid });
	}
    
    private static final String EVENT_ACK = "update events set ackTs=?, ackUserId=?, alternateAckSource=? where id=? and ackTs is null";
    private static final String USER_EVENT_ACK = "update userEvents set silenced=? where eventId=?";

    public void ackEvent(int eventId, long time, int userId, int alternateAckSource) {
        // Ack the event 
        ejt.update(EVENT_ACK, new Object[] { time, userId == 0 ? null : userId, alternateAckSource, eventId });
        // Silence the user events
        ejt.update(USER_EVENT_ACK, new Object[] { boolToChar(true), eventId });
        // Clear the cache
        clearCache();
    }

    private static final String USER_EVENTS_INSERT = "insert into userEvents (eventId, userId, silenced) values (?,?,?)";

    public void insertUserEvents(final int eventId, final List<Integer> userIds, final boolean alarm) {
        ejt.batchUpdate(USER_EVENTS_INSERT, new BatchPreparedStatementSetter() {
            @Override
            public int getBatchSize() {
                return userIds.size();
            }

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, eventId);
                ps.setInt(2, userIds.get(i));
                ps.setString(3, boolToChar(!alarm));
            }
        });

        if (alarm) {
            for (int userId : userIds)
                removeUserIdFromCache(userId);
        }
    }

    private static final String BASIC_EVENT_SELECT = "select e.id, e.typeId, e.typeRef1, e.typeRef2, e.activeTs, e.rtnApplicable, e.rtnTs, e.rtnCause, "
            + "  e.alarmLevel, e.message, e.ackTs, e.ackUserId, u.username, e.alternateAckSource ,e.emailHandler "
            + "from events e "
            + "  left join users u on e.ackUserId=u.id ";

    public List<EventInstance> getActiveEvents() {
        List<EventInstance> results = query(BASIC_EVENT_SELECT + "where e.rtnApplicable=? and e.rtnTs is null",
                new Object[] { boolToChar(true) }, new EventInstanceRowMapper());
        attachRelationalInfo(results);
        return results;
    }

    private static final String EVENT_SELECT_WITH_USER_DATA = "select e.id, e.typeId, e.typeRef1, e.typeRef2, e.activeTs, e.rtnApplicable, e.rtnTs, e.rtnCause, "
            + "  e.alarmLevel, e.message, e.ackTs, e.ackUserId, u.username, e.alternateAckSource,e.emailHandler, ue.silenced "
            + "from events e "
            + "  left join users u on e.ackUserId=u.id "
            + "  left join userEvents ue on e.id=ue.eventId ";
    
    /**
     * 查询事件附加用户(静音)信息和范围信息
     */
    private static final String EVENT_SELECT_WITH_USERSCOPE_DATA = "select e.id, e.typeId, e.typeRef1, e.typeRef2, e.activeTs, e.rtnApplicable, e.rtnTs, e.rtnCause, "
        + "  e.alarmLevel, e.message, e.ackTs, e.ackUserId, u.username, e.alternateAckSource,e.emailHandler, ue.silenced,e.scopeId,s.scopename "
        + "from events e "
        + "  left join users u on e.ackUserId=u.id "
        + "  left join userEvents ue on e.id=ue.eventId "
        + "  left join scope s on e.scopeId = s.id ";
    

    public List<EventInstance> getEventsForDataPoint(int dataPointId, int userId) {
        List<EventInstance> results = query(EVENT_SELECT_WITH_USER_DATA + "where e.typeId="
                + EventType.EventSources.DATA_POINT + "  and e.typeRef1=? " + "  and ue.userId=? "
                + "order by e.activeTs desc", new Object[] { dataPointId, userId }, new UserEventInstanceRowMapper());
        attachRelationalInfo(results);
        return results;
    }

    public List<EventInstance> getPendingEventsForDataPoint(int dataPointId, int userId) {
        // Check the cache
        List<EventInstance> userEvents = getFromCache(userId);
        if (userEvents == null) {
            // This is a potentially long running query, so run it offline.
            userEvents = Collections.emptyList();
            addToCache(userId, userEvents);
            Common.timer.execute(new UserPendingEventRetriever(userId));
        }

        List<EventInstance> list = null;
        for (EventInstance e : userEvents) {
            if (e.getEventType().getDataPointId() == dataPointId) {
                if (list == null)
                    list = new ArrayList<EventInstance>();
                list.add(e);
            }
        }

        if (list == null)
            return Collections.emptyList();
        return list;
    }

    class UserPendingEventRetriever implements Runnable {
        private final int userId;

        UserPendingEventRetriever(int userId) {
            this.userId = userId;
        }

        @Override
        public void run() {
            addToCache(userId, getPendingEvents(EventType.EventSources.DATA_POINT, -1, userId));
        }
    }

    public List<EventInstance> getPendingEventsForDataSource(int dataSourceId, int userId) {
        return getPendingEvents(EventType.EventSources.DATA_SOURCE, dataSourceId, userId);
    }

    public List<EventInstance> getPendingEventsForPublisher(int publisherId, int userId) {
        return getPendingEvents(EventType.EventSources.PUBLISHER, publisherId, userId);
    }

    List<EventInstance> getPendingEvents(int typeId, int typeRef1, int userId) {
        Object[] params;
        StringBuilder sb = new StringBuilder();
        sb.append(EVENT_SELECT_WITH_USER_DATA);
        sb.append("where e.typeId=?");

        if (typeRef1 == -1) {
            params = new Object[] { typeId, userId, boolToChar(true) };
        }
        else {
            sb.append("  and e.typeRef1=?");
            params = new Object[] { typeId, typeRef1, userId, boolToChar(true) };
        }
        sb.append("  and ue.userId=? ");
        sb.append("  and (e.ackTs is null or (e.rtnApplicable=? and e.rtnTs is null and e.alarmLevel > 0)) ");
        sb.append("order by e.activeTs desc");

        List<EventInstance> results = query(sb.toString(), params, new UserEventInstanceRowMapper());
        attachRelationalInfo(results);
        return results;
    }
    
    /*********************************************暂时请勿删除************************
    List<EventInstance> results = query(EVENT_SELECT_WITH_USERSCOPE_DATA
    		//2//+ "where ue.userId=? and e.ackTs is null and s.id = ? order by e.activeTs desc", new Object[] { user.getId() ,user.getCurrentScope().getId()},
			+ "where (ue.userId=? and e.ackTs is null and s.id = ?) or (e.typeId = ? and typeRef1 = ? and e.scopeId = ?) order by e.activeTs desc", new Object[] { user.getId() ,user.getCurrentScope().getId(),EventType.EventSources.SYSTEM,SystemEventType.TYPE_EMAIL_SEND_FAILURE,-1},
    		// 修改目的：原来查询条件只是查询没有确认事件的行，现在查询条件是没有确认时间  或者  可激活状态(rtnApplicable='Y' and rtnTs is null)+只限于 点事件,数据源事件,组合事件e.typeid in(1,3,5)
    		//1//+ " where ue.userId=? and (e.ackTs is null or (rtnApplicable='Y' and rtnTs is null and e.typeid in(1,3,5) ) ) order by e.activeTs desc ", new Object[] { userId },
            new UserScopeEventInstanceRowMapper(), MAX_PENDING_EVENTS);
    **************************************************************************************/
    
    
    /**
     * 进入事件页面加载时调用的方法 //具体查询条件还需要修改。用下面一个where语句来修改
     * @param userId 用户ID
     * @return 事件列表 
     *//******************************错误处在这里范围‘邮件发送错误scopeID都是1，所以其他用户在这里查不到’******************************/
    public List<EventInstance> getPendingEvents(User user) {
    	String sql  = EVENT_SELECT_WITH_USERSCOPE_DATA;
    	EmailTempDao tempDao = new EmailTempDao();
    	List<EventInstance> results = new ArrayList<EventInstance>();
    	if(tempDao.hasData(user.getId())){
    		if(user.getCurrentScope().getScopetype()==0){//HQ
    			sql +="where (ue.userId=? and e.ackTs is null and (s.id = ? or e.emailhandler is not null) ) or (e.typeId = ? and typeRef1 = ? and e.scopeId = ?) order by e.activeTs desc";
    			results = query(sql,new Object[] { user.getId() ,user.getCurrentScope().getId(),EventType.EventSources.SYSTEM,SystemEventType.TYPE_EMAIL_SEND_FAILURE,-1},
                        new UserScopeEventInstanceRowMapper(), MAX_PENDING_EVENTS);		
    		}
    		else{
    			sql +="where ue.userId=? and e.ackTs is null and (s.id = ? or e.emailhandler is not null)  order by e.activeTs desc";
    			results = query(sql,new Object[] { user.getId() ,user.getCurrentScope().getId()},new UserScopeEventInstanceRowMapper(), MAX_PENDING_EVENTS);
    		}
    		relationEmailErrorUser(results);
    		/*****************此次查询把所有没有被表示所属范围的‘邮件发送失败事件’全部查找出来，讲属于自己的标识为自己的，不是删除列表**********************/
    		for(int i = 0;i<results.size();i++){
    			if(results.get(i).getScope().getId()==-1){
    				results.remove(i);
    			}
    		}
    	}else{
    		sql +=" where ue.userId=? and e.ackTs is null and s.id = ? order by e.activeTs desc";
    		results = query(sql,new Object[] { user.getId() ,user.getCurrentScope().getId()},new UserScopeEventInstanceRowMapper(), MAX_PENDING_EVENTS);
    	}
    	attachRelationalInfo(results);
        return results;
    }

    public List<EventInstance> getPendingAlarms(User user,int alarmLevel) {
    	String sql  = EVENT_SELECT_WITH_USERSCOPE_DATA;
    	EmailTempDao tempDao = new EmailTempDao();
    	List<EventInstance> results = new ArrayList<EventInstance>();
    	if(tempDao.hasData(user.getId())){
    		sql +="where e.emailhandler=? and  (ue.userId=? and e.ackTs is null) order by e.activeTs desc";
    		results = query(sql,new Object[] {alarmLevel, user.getId() },
                    new UserScopeEventInstanceRowMapper(), MAX_PENDING_EVENTS);
    		relationEmailErrorUser(results);
    		/*****************此次查询把所有没有被表示所属范围的‘邮件发送失败事件’全部查找出来，讲属于自己的标识为自己的，不是删除列表**********************/
    		for(int i = 0;i<results.size();i++){
    			if(results.get(i).getScope().getId()==-1){
    				results.remove(i);
    			}
    		}
    	}else{
    		sql +=" where e.emailhandler=? and ue.userId=? and e.ackTs is null  order by e.activeTs desc";
    		results = query(sql,new Object[] {alarmLevel, user.getId()},new UserScopeEventInstanceRowMapper(), MAX_PENDING_EVENTS);
    	}
    	attachRelationalInfo(results);
        return results;
    }
    
    
    private EventInstance getEventInstance(int eventId) {
        return queryForObject(BASIC_EVENT_SELECT + "where e.id=?", new Object[] { eventId },
                new EventInstanceRowMapper());
    }

    public static class EventInstanceRowMapper implements GenericRowMapper<EventInstance> {
        public EventInstance mapRow(ResultSet rs, int rowNum) throws SQLException {
            EventType type = createEventType(rs, 2);

            LocalizableMessage message;
            try {
                message = LocalizableMessage.deserialize(rs.getString(10));
            }
            catch (LocalizableMessageParseException e) {
                message = new LocalizableMessage("common.default", rs.getString(10));
            }

            EventInstance event = new EventInstance(type, rs.getLong(5), charToBool(rs.getString(6)), rs.getInt(9),
                    message, null);
            event.setId(rs.getInt(1));
            long rtnTs = rs.getLong(7);
            if (!rs.wasNull())
                event.returnToNormal(rtnTs, rs.getInt(8));
            long ackTs = rs.getLong(11);
            if (!rs.wasNull()) {
                event.setAcknowledgedTimestamp(ackTs);
                event.setAcknowledgedByUserId(rs.getInt(12));
                if (!rs.wasNull())
                    event.setAcknowledgedByUsername(rs.getString(13));
                event.setAlternateAckSource(rs.getInt(14));
            }
            event.setWarin(rs.getInt(15));
            return event;
        }
    }

    class UserEventInstanceRowMapper extends EventInstanceRowMapper {
        @Override
        public EventInstance mapRow(ResultSet rs, int rowNum) throws SQLException {
            EventInstance event = super.mapRow(rs, rowNum);
            event.setSilenced(charToBool(rs.getString(16)));
            if (!rs.wasNull())
                event.setUserNotified(true);
            return event;
        }
    }
    
    class UserScopeEventInstanceRowMapper extends UserEventInstanceRowMapper {
        @Override
        public EventInstance mapRow(ResultSet rs, int rowNum) throws SQLException {
            EventInstance event = super.mapRow(rs, rowNum);
            ScopeVO scope = new ScopeVO();
            scope.setId(rs.getInt(17));
            scope.setScopename(rs.getString(18));
            event.setScope(scope);
            if (!rs.wasNull())
                event.setUserNotified(true);
            return event;
        }
    }

    static EventType createEventType(ResultSet rs, int offset) throws SQLException {
        int typeId = rs.getInt(offset);
        EventType type;
        if (typeId == EventType.EventSources.DATA_POINT)
            type = new DataPointEventType(rs.getInt(offset + 1), rs.getInt(offset + 2));
        else if (typeId == EventType.EventSources.DATA_SOURCE)
            type = new DataSourceEventType(rs.getInt(offset + 1), rs.getInt(offset + 2));
        else if (typeId == EventType.EventSources.SYSTEM)
            type = new SystemEventType(rs.getInt(offset + 1), rs.getInt(offset + 2));
        else if (typeId == EventType.EventSources.COMPOUND)
            type = new CompoundDetectorEventType(rs.getInt(offset + 1));
        else if (typeId == EventType.EventSources.SCHEDULED)
            type = new ScheduledEventType(rs.getInt(offset + 1));
        else if (typeId == EventType.EventSources.PUBLISHER)
            type = new PublisherEventType(rs.getInt(offset + 1), rs.getInt(offset + 2));
        else if (typeId == EventType.EventSources.AUDIT)
            type = new AuditEventType(rs.getInt(offset + 1), rs.getInt(offset + 2));
        else if (typeId == EventType.EventSources.MAINTENANCE)
            type = new MaintenanceEventType(rs.getInt(offset + 1));
        else
            throw new ShouldNeverHappenException("Unknown event type: " + typeId);
        return type;
    }

    private void attachRelationalInfo(List<EventInstance> list) {
        for (EventInstance e : list)
            attachRelationalInfo(e);
    }

    private static final String EVENT_COMMENT_SELECT = UserCommentRowMapper.USER_COMMENT_SELECT
            + "where uc.commentType= " + UserComment.TYPE_EVENT + " and uc.typeKey=? " + "order by uc.ts";

    void attachRelationalInfo(EventInstance event) {
        event.setEventComments(query(EVENT_COMMENT_SELECT, new Object[] { event.getId() }, new UserCommentRowMapper()));
    }
    
    /**
     * 循环执行下面方法
     * @param list
     */
    private void relationEmailErrorUser(List<EventInstance> list) {
        for (EventInstance e : list)
        	relationEmailErrorUser(e);
    }
    /**
     * 给邮件发送失败的事件指定用户
     * @param event 当前事件行
     */
    void relationEmailErrorUser(final EventInstance eventVO){
    	getTransactionTemplate().execute(
				new TransactionCallbackWithoutResult() {
					@SuppressWarnings("synthetic-access")
					@Override
					protected void doInTransactionWithoutResult(
							TransactionStatus status) { 
						EventInstance event = eventVO;
						if(event.getEventType().getEventSourceId()==EventType.EventSources.SYSTEM&&event.getEventType().getReferenceId1()==SystemEventType.TYPE_EMAIL_SEND_FAILURE){
				    		EmailTempDao emailTempDao = new EmailTempDao();
				    		EmailTempVO tempVO = emailTempDao.getUidByEmailAndTs(event.getMessage(),event.getActiveTimestamp());
				    		if(tempVO!=null){
				    			//获取用户ID
				    			int userId = tempVO.getUid();
				    			//根据用户ID查找其注册地
				    			ScopeVO scope = new ScopeDao().getScopeByUser(userId); 
				    			//此次显示就改变
				    			event.setScope(scope);
				    			//更新这条数据在数据库中events表中的内容
				    			ejt.update(" update events set typeRef2 = ? ,scopeid = ? where id = ? ",new Object[]{userId,scope.getId(),event.getId()});
				    			//在userevents表中添加一行数据
				    			if(ejt.queryForInt(" select count(*) from userEvents where eventId = ? and userId=?",new Object[]{event.getId(),userId},0)<1)
				    				ejt.update("insert into userEvents (eventId, userId, silenced) values (?,?,'N')",new Object[]{event.getId(),userId});
				    			//删除临时数据
				    			ejt.update(" delete from eventtemp where id = ? ",new Object[]{tempVO.getId()});
				    		}
				    	}
					}

				});
    	
    	
    	
    }

    public EventInstance insertEventComment(int eventId, UserComment comment) {
        new UserDao().insertUserComment(UserComment.TYPE_EVENT, eventId, comment);
        return getEventInstance(eventId);
    }

    public int purgeEventsBefore(final long time) {
        // Find a list of event ids with no remaining acknowledgements pending.
        final ExtendedJdbcTemplate ejt2 = ejt;
        int count = getTransactionTemplate().execute(new GenericTransactionCallback<Integer>() {
            @Override
            public Integer doInTransaction(TransactionStatus status) {
                int count = ejt2.update("delete from events " + "where activeTs<? " + "  and ackTs is not null "
                        + "  and (rtnApplicable=? or (rtnApplicable=? and rtnTs is not null))", new Object[] { time,
                        boolToChar(false), boolToChar(true) });

                // Delete orphaned user comments.
                ejt2.update("delete from userComments where commentType=" + UserComment.TYPE_EVENT
                        + "  and typeKey not in (select id from events)");

                return count;
            }
        });

        clearCache();

        return count;
    }

    public int getEventCount() {
        return ejt.queryForInt("select count(*) from events");
    }

    public List<EventInstance> search(int eventId, int eventSourceType, String status, int alarmLevel,
            final String[] keywords, int userId, final ResourceBundle bundle, final int from, final int to,
            final Date date,final int scopeId) {
        List<String> where = new ArrayList<String>();
        List<Object> params = new ArrayList<Object>();

        StringBuilder sql = new StringBuilder();
        sql.append(EVENT_SELECT_WITH_USER_DATA);
        sql.append("where ue.userId=? and e.scopeId=?");
        params.add(userId);
        params.add(scopeId);

        if (eventId != 0) {
            where.add("e.id=?");
            params.add(eventId);
        }

        if (eventSourceType != -1) {
            where.add("e.typeId=?");
            params.add(eventSourceType);
        }

        if (EventsDwr.STATUS_ACTIVE.equals(status)) {
            where.add("e.rtnApplicable=? and e.rtnTs is null");
            params.add(boolToChar(true));
        }
        else if (EventsDwr.STATUS_RTN.equals(status)) {
            where.add("e.rtnApplicable=? and e.rtnTs is not null");
            params.add(boolToChar(true));
        }
        else if (EventsDwr.STATUS_NORTN.equals(status)) {
            where.add("e.rtnApplicable=?");
            params.add(boolToChar(false));
        }

        if (alarmLevel != -1) {
            where.add("e.alarmLevel=?");
            params.add(alarmLevel);
        }
        // add the date check
        if(date!=null){
        	where.add(" e.activeTs >= ? ");
        	params.add(date.getTime());
        } 

        if (!where.isEmpty()) {
            for (String s : where) {
                sql.append(" and ");
                sql.append(s);
            }
        }
        sql.append(" order by e.activeTs desc");

        final List<EventInstance> results = new ArrayList<EventInstance>(to - from);
        final UserEventInstanceRowMapper rowMapper = new UserEventInstanceRowMapper();

        final int[] data = new int[2];

        ejt.query(sql.toString(), params.toArray(), new ResultSetExtractor() {
            @Override
            public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
                int row = 0;
                long dateTs = date == null ? -1 : date.getTime();
                int startRow = from;//开始行为from
                //int startRow = -1;

                while (rs.next()) {
                    EventInstance e = rowMapper.mapRow(rs, 0);
                    attachRelationalInfo(e);
                    boolean add = true;

                    if (keywords != null) {
                        // Do the text search. If the instance has a match, put it in the result. Otherwise ignore.
                        StringBuilder text = new StringBuilder();
                        text.append(e.getMessage().getLocalizedMessage(bundle));
                        for (UserComment comment : e.getEventComments())
                            text.append(' ').append(comment.getComment());

                        String[] values = text.toString().split("\\s+");

                        for (String keyword : keywords) {
                            if (keyword.startsWith("-")) {
                                if (StringUtils.globWhiteListMatchIgnoreCase(values, keyword.substring(1))) {
                                    add = false;
                                    break;
                                }
                            }
                            else {
                                if (!StringUtils.globWhiteListMatchIgnoreCase(values, keyword)) {
                                    add = false;
                                    break;
                                }
                            }
                        }
                    }
                    //时间判断已经在SQL语句中判断了，此处可直接添加	
                    if (add) {
                        if (date != null) {
                            //if (e.getActiveTimestamp() <= dateTs && results.size() < to - from) {
                              if (results.size() < to - from&&row >= from && row < to) {
                                //if (startRow == -1)
                                //    startRow = row;
                                results.add(e);
                            }
                        }
                        //else if (row >= from && row < to)
                        //    results.add(e);

                        row++;
                    }
                }

                data[0] = row;
                data[1] = startRow;

                return null;
            }
        });

        searchRowCount = data[0];
        startRow = data[1];

        return results;
    }

    private int searchRowCount;
    private int startRow;

    public int getSearchRowCount() {
        return searchRowCount;
    }

    public int getStartRow() {
        return startRow;
    }

    //
    // /
    // / Event handlers
    // /
    //
    public String generateUniqueXid() {
        return generateUniqueXid(EventHandlerVO.XID_PREFIX, "eventHandlers");
    }

    public boolean isXidUnique(String xid, int excludeId) {
        return isXidUnique(xid, excludeId, "eventHandlers");
    }

    public EventType getEventHandlerType(int handlerId) {
        return queryForObject("select eventTypeId, eventTypeRef1, eventTypeRef2 from eventHandlers where id=?",
                new Object[] { handlerId }, new GenericRowMapper<EventType>() {
                    public EventType mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return createEventType(rs, 1);
                    }
                });
    }

    public List<EventHandlerVO> getEventHandlers(EventType type) {
        return getEventHandlers(type.getEventSourceId(), type.getReferenceId1(), type.getReferenceId2());
    }

    public List<EventHandlerVO> getEventHandlers(EventTypeVO type) {
        return getEventHandlers(type.getTypeId(), type.getTypeRef1(), type.getTypeRef2());
    }

    public List<EventHandlerVO> getEventHandlers() {
        return query(EVENT_HANDLER_SELECT, new EventHandlerRowMapper());
    }

    /**
     * Note: eventHandlers.eventTypeRef2 matches on both the given ref2 and 0. This is to allow a single set of event
     * handlers to be defined for user login events, rather than have to individually define them for each user.
     */
    private List<EventHandlerVO> getEventHandlers(int typeId, int ref1, int ref2) {
        return query(EVENT_HANDLER_SELECT + "where eventTypeId=? and eventTypeRef1=? "
                + "  and (eventTypeRef2=? or eventTypeRef2=0)", new Object[] { typeId, ref1, ref2 },
                new EventHandlerRowMapper());
    }

    public EventHandlerVO getEventHandler(int eventHandlerId) {
        return queryForObject(EVENT_HANDLER_SELECT + "where id=?", new Object[] { eventHandlerId },
                new EventHandlerRowMapper());
    }

    public EventHandlerVO getEventHandler(String xid) {
        return queryForObject(EVENT_HANDLER_SELECT + "where xid=?", new Object[] { xid }, new EventHandlerRowMapper(),
                null);
    }
    public EventHandlerVO getEventHandlerByAlias(String alias) {
        return queryForObject(EVENT_HANDLER_SELECT + "where alias=?", new Object[] { alias }, new EventHandlerRowMapper(),
                null);
    }
    //getEventHandlerIds
    public List<Integer> getEventHandlerIds(int factory) {
        return queryForList("select id from eventHandlers where scopeId=?", new Object[] { factory },Integer.class);
    }
    private static final String EVENT_HANDLER_SELECT = "select id, xid, alias, data ,scopeId,disableChange from eventHandlers ";

    class EventHandlerRowMapper implements GenericRowMapper<EventHandlerVO> {
        public EventHandlerVO mapRow(ResultSet rs, int rowNum) throws SQLException {
            EventHandlerVO h = (EventHandlerVO) SerializationHelper.readObject(rs.getBlob(4).getBinaryStream());
            h.setId(rs.getInt(1));
            h.setXid(rs.getString(2));
            h.setAlias(rs.getString(3));
            h.setScopeId(rs.getInt(5));
            h.setDisableChange("1".equals(rs.getString(6)));
            return h;
        }
    }

    public EventHandlerVO saveEventHandler(final int scopeId,final EventType type, final EventHandlerVO handler) {
        if (type == null)
            return saveEventHandler(scopeId,0, 0, 0, handler);
        return saveEventHandler(scopeId,type.getEventSourceId(), type.getReferenceId1(), type.getReferenceId2(), handler);
    }

    public EventHandlerVO saveEventHandler(final int scopeId,final EventTypeVO type, final EventHandlerVO handler) {
        if (type == null)
            return saveEventHandler(scopeId,0, 0, 0, handler);
        return saveEventHandler(scopeId,type.getTypeId(), type.getTypeRef1(), type.getTypeRef2(), handler);
    }

    private EventHandlerVO saveEventHandler(final int scopeId,final int typeId, final int typeRef1, final int typeRef2,
            final EventHandlerVO handler) {
        getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                if (handler.getId() == Common.NEW_ID)
                    insertEventHandler(scopeId,typeId, typeRef1, typeRef2, handler);
                else
                    updateEventHandler(handler);
            }
        });
        return getEventHandler(handler.getId());
    }

    void insertEventHandler(int scopeId,int typeId, int typeRef1, int typeRef2, EventHandlerVO handler) {
        handler.setId(doInsert(
                "insert into eventHandlers (xid, alias, eventTypeId, eventTypeRef1, eventTypeRef2,data,scopeId,disableChange) values (?,?,?,?,?,?,?,?)",
                new Object[] { handler.getXid(), handler.getAlias(), typeId, typeRef1, typeRef2,
                        SerializationHelper.writeObject(handler),scopeId,handler.isDisableChange()}, new int[] { Types.VARCHAR, Types.VARCHAR,
                        Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.BLOB,Types.INTEGER,Types.BOOLEAN }));
        //AuditEventType.raiseAddedEvent(AuditEventType.TYPE_EVENT_HANDLER, handler);
    }

    void updateEventHandler(EventHandlerVO handler) {
        EventHandlerVO old = getEventHandler(handler.getId());
        ejt.update("update eventHandlers set xid=?, alias=?, data=?,disableChange=? where id=?", new Object[] { handler.getXid(),
                handler.getAlias(), SerializationHelper.writeObject(handler), handler.isDisableChange(),handler.getId() }, new int[] {
                Types.VARCHAR, Types.VARCHAR, Types.BLOB, Types.BOOLEAN,Types.INTEGER });
        //AuditEventType.raiseChangedEvent(AuditEventType.TYPE_EVENT_HANDLER, old, handler);
    }

    public void deleteEventHandler(final int handlerId) {
        EventHandlerVO handler = getEventHandler(handlerId);
        ejt.update("delete from eventHandlers where id=?", new Object[] { handlerId });
        //AuditEventType.raiseDeletedEvent(AuditEventType.TYPE_EVENT_HANDLER, handler);
    }

    //
    // /
    // / User alarms
    // /
    //
    private static final String SILENCED_SELECT = "select ue.silenced " + "from events e "
            + "  join userEvents ue on e.id=ue.eventId " + "where e.id=? " + "  and ue.userId=? "
            + "  and e.ackTs is null";

    public boolean toggleSilence(int eventId, int userId) {
        String result = ejt.queryForObject(SILENCED_SELECT, new Object[] { eventId, userId }, String.class, null);
        if (result == null)
            return true;

        boolean silenced = !charToBool(result);
        ejt.update("update userEvents set silenced=? where eventId=? and userId=?", new Object[] {
                boolToChar(silenced), eventId, userId });
        return silenced;
    }

    /**
     * 获取最高的未被确认的事件等级
     * @param user 当前用户 
     * @return 精到最高等级ID
     */
    public int getHighestUnsilencedAlarmLevel(User user) {
        return ejt.queryForInt("select max(e.alarmLevel) from userEvents u " + "  join events e on u.eventId=e.id "
                + "where u.silenced=? and u.userId=? and (e.scopeid = ? or e.emailhandler is not null )", new Object[] { boolToChar(false), user.getId(),user.getCurrentScope().getId() });
    }

    //
    // /
    // / Pending event caching
    // /
    //
    static class PendingEventCacheEntry {
        private final List<EventInstance> list;
        private final long createTime;

        public PendingEventCacheEntry(List<EventInstance> list) {
            this.list = list;
            createTime = System.currentTimeMillis();
        }

        public List<EventInstance> getList() {
            return list;
        }

        public boolean hasExpired() {
            return System.currentTimeMillis() - createTime > CACHE_TTL;
        }
    }

    private static Map<Integer, PendingEventCacheEntry> pendingEventCache = new ConcurrentHashMap<Integer, PendingEventCacheEntry>();

    private static final long CACHE_TTL = 300000; // 5 minutes

    public static List<EventInstance> getFromCache(int userId) {
        PendingEventCacheEntry entry = pendingEventCache.get(userId);
        if (entry == null)
            return null;
        if (entry.hasExpired()) {
            pendingEventCache.remove(userId);
            return null;
        }
        return entry.getList();
    }

    public static void addToCache(int userId, List<EventInstance> list) {
        pendingEventCache.put(userId, new PendingEventCacheEntry(list));
    }

    public static void updateCache(EventInstance event) {
        if (event.isAlarm() && event.getEventType().getEventSourceId() == EventType.EventSources.DATA_POINT)
            pendingEventCache.clear();
    }

    public static void removeUserIdFromCache(int userId) {
        pendingEventCache.remove(userId);
    }

    public static void clearCache() {
        pendingEventCache.clear();
    }
    //根据事件编号获得事件范围(如果没有值就返回-2)
    public Integer getEventScope(int eventId){
    	return ejt.queryForInt("select scopeId from events where id=?",new Object[]{eventId},-2);
    }
}
