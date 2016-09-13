/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.vo.mailingList;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.joda.time.DateTime;

import com.serotonin.json.JsonObject;
import com.serotonin.json.JsonReader;
import com.serotonin.json.JsonRemoteEntity;
import com.serotonin.json.JsonRemoteProperty;
import com.serotonin.mango.Common;
import com.serotonin.util.StringUtils;
import com.serotonin.web.dwr.DwrResponseI18n;

@JsonRemoteEntity
public class MailingList extends EmailRecipient {
    public static final String XID_PREFIX = "ML_";

    private int id = Common.NEW_ID;
    private String xid;
    @JsonRemoteProperty
    private String name;
    @JsonRemoteProperty(innerType = EmailRecipient.class)
    private List<EmailRecipient> entries;
    private int scopeId;
    /**
     * Integers that are present in the inactive intervals set are times at which the mailing list schedule is not to be
     * sent to. Intervals are split into 15 minutes, starting at [00:00 to 00:15) on Monday. Thus, there are 4 * 24 * 7
     * = 672 individual periods.
     */
    @JsonRemoteProperty(innerType = Integer.class)
    private Set<Integer> inactiveIntervals = new TreeSet<Integer>();

    @Override
    public int getRecipientType() {
        return EmailRecipient.TYPE_MAILING_LIST;
    }

    @Override
    public String getReferenceAddress() {
        return null;
    }

    @Override
    public int getReferenceId() {
        return id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getXid() {
        return xid;
    }

    public void setXid(String xid) {
        this.xid = xid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EmailRecipient> getEntries() {
        return entries;
    }

    public void setEntries(List<EmailRecipient> entries) {
        this.entries = entries;
    }

    public Set<Integer> getInactiveIntervals() {
        return inactiveIntervals;
    }

    public void setInactiveIntervals(Set<Integer> inactiveIntervals) {
        this.inactiveIntervals = inactiveIntervals;
    }

    @Override
    public void appendAddresses(Set<String> addresses, DateTime sendTime) {
        if (sendTime != null && inactiveIntervals.contains(getIntervalIdAt(sendTime)))
            return;
        appendAllAddresses(addresses);
    }

    @Override
    public void appendAllAddresses(Set<String> addresses) {
        for (EmailRecipient e : entries)
            e.appendAddresses(addresses, null);
    }

    private static int getIntervalIdAt(DateTime dt) {
        int interval = 0;
        interval += dt.getMinuteOfHour() / 15;
        interval += dt.getHourOfDay() * 4;
        interval += (dt.getDayOfWeek() - 1) * 96;
        return interval;
    }

    public void validate(DwrResponseI18n response) {
        // Check that required fields are present.
        if (StringUtils.isEmpty(name))
            response.addContextualMessage("name", "mailingLists.validate.nameRequired");

        // Check field lengths
        if (StringUtils.isLengthGreaterThan(name, 40))
            response.addContextualMessage("name", "mailingLists.validate.nameGreaterThan40");

        // Check for entries.
        if (entries.size() == 0)
            response.addGenericMessage("mailingLists.validate.entries");
    }

    @Override
    public String toString() {
        return "MailingList(" + entries + ")";
    }

    @Override
    public void jsonSerialize(Map<String, Object> map) {
        // Don't call the super method, because a mailing list can't be a member of a mailing list.
        map.put("xid", xid);
    }

    @Override
    public void jsonDeserialize(JsonReader reader, JsonObject json) {
        // no op
    }

	public int getScopeId() {
		return scopeId;
	}

	public void setScopeId(int scopeId) {
		this.scopeId = scopeId;
	}
}
