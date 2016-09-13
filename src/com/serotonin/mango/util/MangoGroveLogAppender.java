/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.util;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Level;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.SystemSettingsDao;
import com.serotonin.mango.rt.maint.VersionCheck;

/**
 *  
 */
public class MangoGroveLogAppender extends AppenderSkeleton {
    @Override
    protected void append(LoggingEvent event) {
        // In spite of what the configuration file says, we don't care about anything less than an error.
        if (!event.getLevel().isGreaterOrEqual(Level.ERROR))
            return;

        // Check the logging property setting.
        try {
            if (!SystemSettingsDao.getBooleanValue(SystemSettingsDao.GROVE_LOGGING, false))
                return;
        }
        catch (Throwable t) {
            // If anything bad happens while trying to figure out if we should log, just fuggetabowit.
            return;
        }

        HttpClient client = Common.getHttpClient();
        PostMethod method = new PostMethod(Common.getGroveUrl(Common.GroveServlets.MANGO_LOG));
        method.addParameter("productId", "LssclM2M");
        method.addParameter("productVersion", Common.getVersion());
        method.addParameter("instanceId", VersionCheck.getInstanceId());
        method.addParameter("instanceName", SystemSettingsDao.getValue(SystemSettingsDao.INSTANCE_DESCRIPTION));
        method.addParameter("ts", Long.toString(event.timeStamp));
        method.addParameter("level", event.getLevel().toString());
        method.addParameter("message", event.getRenderedMessage());

        String[] throwableStrRep = event.getThrowableStrRep();
        if (throwableStrRep != null) {
            StringBuilder throwable = new StringBuilder();
            for (String s : throwableStrRep)
                throwable.append(s).append("\r\n");
            method.addParameter("throwable", throwable.toString());
        }

        method.addParameter("clazz", event.getLocationInformation().getClassName());
        method.addParameter("method", event.getLocationInformation().getMethodName());
        method.addParameter("file", event.getLocationInformation().getFileName());
        method.addParameter("line", event.getLocationInformation().getLineNumber());

        try {
            int responseCode = client.executeMethod(method);
            if (responseCode != HttpStatus.SC_OK)
                LogLog.error("Invalid response code: " + responseCode);
        }
        catch (HttpException e) {
            LogLog.error("Error sending log event to grove", e);
        }
        catch (IOException e) {
            LogLog.error("Error sending log event to grove", e);
        }
    }

    @Override
    public void close() {
        // no op
    }

    @Override
    public boolean requiresLayout() {
        return false;
    }
}
