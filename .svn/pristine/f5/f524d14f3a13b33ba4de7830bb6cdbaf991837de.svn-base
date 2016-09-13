/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.vo.report;

import java.io.PrintWriter;
import java.util.List;
import java.util.ResourceBundle;

import com.serotonin.mango.rt.event.AlarmLevels;
import com.serotonin.mango.rt.event.EventInstance;
import com.serotonin.mango.view.export.CsvWriter;
import com.serotonin.web.i18n.I18NUtils;
import com.serotonin.web.i18n.LocalizableMessage;

/**
 *  
 */
public class EventCsvStreamer {
    public EventCsvStreamer(PrintWriter out, List<EventInstance> events, ResourceBundle bundle) {
        CsvWriter csvWriter = new CsvWriter();
        String[] data = new String[7];

        // Write the headers.
        data[0] = I18NUtils.getMessage(bundle, "reports.eventList.id");
        data[1] = I18NUtils.getMessage(bundle, "common.alarmLevel");
        data[2] = I18NUtils.getMessage(bundle, "common.activeTime");
        data[3] = I18NUtils.getMessage(bundle, "reports.eventList.message");
        data[4] = I18NUtils.getMessage(bundle, "reports.eventList.status");
        data[5] = I18NUtils.getMessage(bundle, "reports.eventList.ackTime");
        data[6] = I18NUtils.getMessage(bundle, "reports.eventList.ackUser");

        out.write(csvWriter.encodeRow(data));

        for (EventInstance event : events) {
            data[0] = Integer.toString(event.getId());
            data[1] = AlarmLevels.getAlarmLevelMessage(event.getAlarmLevel()).getLocalizedMessage(bundle);
            data[2] = event.getPrettyActiveTimestamp();
            data[3] = event.getMessage().getLocalizedMessage(bundle);

            if (event.isActive())
                data[4] = I18NUtils.getMessage(bundle, "common.active");
            else if (!event.isRtnApplicable())
                data[4] = "";
            else
                data[4] = event.getFullPrettyRtnTimestamp() + " - " + event.getRtnMessage().getLocalizedMessage(bundle);

            if (event.isAcknowledged()) {
                data[5] = event.getFullPrettyAcknowledgedTimestamp();

                LocalizableMessage ack = event.getAckMessage();
                if (ack == null)
                    data[6] = "";
                else
                    data[6] = ack.getLocalizedMessage(bundle);
            }
            else {
                data[5] = "";
                data[6] = "";
            }

            out.write(csvWriter.encodeRow(data));
        }

        out.flush();
        out.close();
    }
}
