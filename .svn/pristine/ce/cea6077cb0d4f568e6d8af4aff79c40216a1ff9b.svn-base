/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.event;

import java.util.List;
import java.util.Map;

import com.serotonin.mango.Common;
import com.serotonin.mango.rt.event.handlers.EventHandlerRT;
import com.serotonin.mango.rt.event.type.EventType;
import com.serotonin.mango.vo.UserComment;
import com.serotonin.mango.vo.scope.ScopeVO;
import com.serotonin.web.i18n.LocalizableMessage;
import com.serotonin.web.taglib.DateFunctions;

public class EventInstance {
    public interface RtnCauses {
        int RETURN_TO_NORMAL = 1;
        int SOURCE_DISABLED = 4;
    }

    public interface AlternateAcknowledgementSources {
        int DELETED_USER = 1;
        int MAINTENANCE_MODE = 2;
    }

    /**
     * Configuration field. Assigned by the database.
     */
    private int id = Common.NEW_ID;

    /**
     * Configuration field. Provided by the event producer. Identifies where the event came from and what it means.
     */
    private final EventType eventType;

    /**
     * State field. The time that the event became active (i.e. was raised).
     */
    private final long activeTimestamp;

    /**
     * Configuration field. Is this type of event capable of returning to normal (true), or is it stateless (false).
     */
    private final boolean rtnApplicable;

    /**
     * State field. The time that the event returned to normal.
     */
    private long rtnTimestamp;

    /**
     * State field. The action that caused the event to RTN. One of {@link RtnCauses}
     */
    private int rtnCause;

    /**
     * Configuration field. The alarm level assigned to the event.
     * 
     * @see AlarmLevels
     */
    private final int alarmLevel;

    /**
     * Configuration field. The message associated with the event.
     */
    private final LocalizableMessage message;

    /**
     * User comments on the event. Added in the events interface after the event has been raised.
     */
    private List<UserComment> eventComments;

    private List<EventHandlerRT> handlers;

    private long acknowledgedTimestamp;
    private int acknowledgedByUserId;
    private String acknowledgedByUsername;
    private int alternateAckSource;

    //
    //
    // These fields are used only in the context of access by a particular user, providing state filled in from
    // the userEvents table.
    private boolean userNotified;
    private boolean silenced;
    
    //  是否发送短信
    private boolean isSMS;
    public boolean isSMS() {
		return isSMS;
	}

	public void setSMS(boolean isSMS) {
		this.isSMS = isSMS;
	}


	//
    // Contextual data from the source that raised the event.
    private final Map<String, Object> context;

    public EventInstance(EventType eventType, long activeTimestamp, boolean rtnApplicable, int alarmLevel,
            LocalizableMessage message, Map<String, Object> context) {
        this.eventType = eventType;
        this.activeTimestamp = activeTimestamp;
        this.rtnApplicable = rtnApplicable;
        this.alarmLevel = alarmLevel;
        if (message == null)
            this.message = new LocalizableMessage("common.noMessage");
        else
            this.message = message;
        this.context = context;
    }

    public LocalizableMessage getRtnMessage() {
        LocalizableMessage rtnKey = null;

        if (!isActive()) {
            if (rtnCause == RtnCauses.RETURN_TO_NORMAL)
                rtnKey = new LocalizableMessage("event.rtn.rtn");
            else if (rtnCause == RtnCauses.SOURCE_DISABLED) {
                if (eventType.getEventSourceId() == EventType.EventSources.DATA_POINT)
                    rtnKey = new LocalizableMessage("event.rtn.pointDisabled");
                else if (eventType.getEventSourceId() == EventType.EventSources.DATA_SOURCE)
                    rtnKey = new LocalizableMessage("event.rtn.dsDisabled");
                else if (eventType.getEventSourceId() == EventType.EventSources.PUBLISHER)
                    rtnKey = new LocalizableMessage("event.rtn.pubDisabled");
                else if (eventType.getEventSourceId() == EventType.EventSources.MAINTENANCE)
                    rtnKey = new LocalizableMessage("event.rtn.maintDisabled");
                else
                    rtnKey = new LocalizableMessage("event.rtn.shutdown");
            }
            else
                rtnKey = new LocalizableMessage("event.rtn.unknown");
        }

        return rtnKey;
    }

    public LocalizableMessage getAckMessage() {
        if (isAcknowledged()) {
            if (acknowledgedByUserId != 0)
                return new LocalizableMessage("events.ackedByUser", acknowledgedByUsername);
            if (alternateAckSource == AlternateAcknowledgementSources.DELETED_USER)
                return new LocalizableMessage("events.ackedByDeletedUser");
            if (alternateAckSource == AlternateAcknowledgementSources.MAINTENANCE_MODE)
                return new LocalizableMessage("events.ackedByMaintenance");
        }

        return null;
    }

    public String getPrettyActiveTimestamp() {
        return DateFunctions.getTime(activeTimestamp);
    }

    public String getFullPrettyActiveTimestamp() {
        return DateFunctions.getFullSecondTime(activeTimestamp);
    }

    public String getPrettyRtnTimestamp() {
        return DateFunctions.getTime(rtnTimestamp);
    }

    public String getFullPrettyRtnTimestamp() {
        return DateFunctions.getFullSecondTime(rtnTimestamp);
    }

    public String getFullPrettyAcknowledgedTimestamp() {
        return DateFunctions.getFullSecondTime(acknowledgedTimestamp);
    }

    public boolean isAlarm() {
        return alarmLevel != AlarmLevels.NONE;
    }

    /**
     * This method should only be used by the EventDao for creating and updating.
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    public boolean isActive() {
        return rtnApplicable && rtnTimestamp == 0;
    }

    public void returnToNormal(long time, int rtnCause) {
        if (isActive()) {
            rtnTimestamp = time;
            this.rtnCause = rtnCause;
        }
    }

    public boolean isAcknowledged() {
        return acknowledgedTimestamp > 0;
    }

    public long getActiveTimestamp() {
        return activeTimestamp;
    }

    public int getAlarmLevel() {
        return alarmLevel;
    }

    public EventType getEventType() {
        return eventType;
    }

    public int getId() {
        return id;
    }

    public long getRtnTimestamp() {
        return rtnTimestamp;
    }

    public LocalizableMessage getMessage() {
        return message;
    }

    public boolean isRtnApplicable() {
        return rtnApplicable;
    }

    public void addEventComment(UserComment comment) {
        eventComments.add(comment);
    }

    public void setEventComments(List<UserComment> eventComments) {
        this.eventComments = eventComments;
    }

    public List<UserComment> getEventComments() {
        return eventComments;
    }

    public int getRtnCause() {
        return rtnCause;
    }

    public List<EventHandlerRT> getHandlers() {
        return handlers;
    }

    public void setHandlers(List<EventHandlerRT> handlers) {
        this.handlers = handlers;
    }

    public boolean isUserNotified() {
        return userNotified;
    }

    public void setUserNotified(boolean userNotified) {
        this.userNotified = userNotified;
    }

    public boolean isSilenced() {
        return silenced;
    }

    public void setSilenced(boolean silenced) {
        this.silenced = silenced;
    }

    public long getAcknowledgedTimestamp() {
        return acknowledgedTimestamp;
    }

    public void setAcknowledgedTimestamp(long acknowledgedTimestamp) {
        this.acknowledgedTimestamp = acknowledgedTimestamp;
    }

    public int getAcknowledgedByUserId() {
        return acknowledgedByUserId;
    }

    public void setAcknowledgedByUserId(int acknowledgedByUserId) {
        this.acknowledgedByUserId = acknowledgedByUserId;
    }

    public String getAcknowledgedByUsername() {
        return acknowledgedByUsername;
    }

    public void setAcknowledgedByUsername(String acknowledgedByUsername) {
        this.acknowledgedByUsername = acknowledgedByUsername;
    }

    public int getAlternateAckSource() {
        return alternateAckSource;
    }

    public void setAlternateAckSource(int alternateAckSource) {
        this.alternateAckSource = alternateAckSource;
    }

    public Map<String, Object> getContext() {
        return context;
    }
    
    
    /**
     * 事件所属范围
     */
    private ScopeVO scope;
    private Integer warin;
	public Integer getWarin() {
		return warin;
	}

	public void setWarin(Integer warin) {
		this.warin = warin;
	}

	public ScopeVO getScope() {
		return scope;
	}
	public void setScope(ScopeVO scope) {
		this.scope = scope;
	}
}
