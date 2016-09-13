/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.dwr;

import java.io.File;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import com.serotonin.mango.db.dao.UserDao;
import com.serotonin.InvalidArgumentException;
import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.DataPointDao;
import com.serotonin.mango.db.dao.EventDao;
import com.serotonin.mango.db.dao.SystemSettingsDao;
import com.serotonin.mango.rt.event.type.AuditEventType;
import com.serotonin.mango.rt.event.type.SystemEventType;
import com.serotonin.mango.rt.maint.DataPurge;
import com.serotonin.mango.rt.maint.VersionCheck;
import com.serotonin.mango.rt.maint.work.EmailWorkItem;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.vo.bean.PointHistoryCount;
import com.serotonin.mango.vo.permission.Permissions;
import com.serotonin.mango.web.dwr.beans.IntegerPair;
import com.serotonin.mango.web.email.MangoEmailContent;
import com.serotonin.util.ColorUtils;
import com.serotonin.util.DirectoryInfo;
import com.serotonin.util.DirectoryUtils;
import com.serotonin.web.dwr.DwrResponseI18n;
import com.serotonin.web.dwr.MethodFilter;
import com.serotonin.web.i18n.I18NUtils;
import com.serotonin.web.i18n.LocalizableMessage;

import java.util.ArrayList;
import com.serotonin.mango.rt.exchange.ExchangeSend;
import com.serotonin.mango.rt.exchange.Content;
import microsoft.exchange.webservices.data.EmailAddress;
public class SystemSettingsDwr extends BaseDwr {
    @MethodFilter
    public Map<String, Object> getSettings() {
        Permissions.ensureAdmin();
        Map<String, Object> settings = new HashMap<String, Object>();

        // Email
        settings.put(SystemSettingsDao.EMAIL_TYPE, SystemSettingsDao.getValue(SystemSettingsDao.EMAIL_TYPE));
        //smtp
        settings.put(SystemSettingsDao.EMAIL_SMTP_HOST, SystemSettingsDao.getValue(SystemSettingsDao.EMAIL_SMTP_HOST));
        settings.put(SystemSettingsDao.EMAIL_SMTP_PORT,
                SystemSettingsDao.getIntValue(SystemSettingsDao.EMAIL_SMTP_PORT));
        settings.put(SystemSettingsDao.EMAIL_FROM_ADDRESS,
                SystemSettingsDao.getValue(SystemSettingsDao.EMAIL_FROM_ADDRESS));
        settings.put(SystemSettingsDao.EMAIL_FROM_NAME, SystemSettingsDao.getValue(SystemSettingsDao.EMAIL_FROM_NAME));
        settings.put(SystemSettingsDao.EMAIL_AUTHORIZATION,
                SystemSettingsDao.getBooleanValue(SystemSettingsDao.EMAIL_AUTHORIZATION));
        settings.put(SystemSettingsDao.EMAIL_SMTP_USERNAME,
                SystemSettingsDao.getValue(SystemSettingsDao.EMAIL_SMTP_USERNAME));
        settings.put(SystemSettingsDao.EMAIL_SMTP_PASSWORD,
                SystemSettingsDao.getValue(SystemSettingsDao.EMAIL_SMTP_PASSWORD));
        settings.put(SystemSettingsDao.EMAIL_TLS, SystemSettingsDao.getBooleanValue(SystemSettingsDao.EMAIL_TLS));
        settings.put(SystemSettingsDao.EMAIL_CONTENT_TYPE,
                SystemSettingsDao.getIntValue(SystemSettingsDao.EMAIL_CONTENT_TYPE));
        //exchange
        settings.put(SystemSettingsDao.EXCHANGE_URL, SystemSettingsDao.getValue(SystemSettingsDao.EXCHANGE_URL));
        settings.put(SystemSettingsDao.EXCHANGE_USERNAME,
                SystemSettingsDao.getValue(SystemSettingsDao.EXCHANGE_USERNAME));
        settings.put(SystemSettingsDao.EXCHANGE_PASSWORD,
                SystemSettingsDao.getValue(SystemSettingsDao.EXCHANGE_PASSWORD));
        settings.put(SystemSettingsDao.EXCHANGE_DOMAIN, SystemSettingsDao.getValue(SystemSettingsDao.EXCHANGE_DOMAIN));
        // System event types
        settings.put("systemEventTypes", SystemEventType.getSystemEventTypes());

        // System event types
        settings.put("auditEventTypes", AuditEventType.getAuditEventTypes());

        // Http
        settings.put(SystemSettingsDao.HTTP_CLIENT_USE_PROXY,
                SystemSettingsDao.getBooleanValue(SystemSettingsDao.HTTP_CLIENT_USE_PROXY));
        settings.put(SystemSettingsDao.HTTP_CLIENT_PROXY_SERVER,
                SystemSettingsDao.getValue(SystemSettingsDao.HTTP_CLIENT_PROXY_SERVER));
        settings.put(SystemSettingsDao.HTTP_CLIENT_PROXY_PORT,
                SystemSettingsDao.getIntValue(SystemSettingsDao.HTTP_CLIENT_PROXY_PORT));
        settings.put(SystemSettingsDao.HTTP_CLIENT_PROXY_USERNAME,
                SystemSettingsDao.getValue(SystemSettingsDao.HTTP_CLIENT_PROXY_USERNAME));
        settings.put(SystemSettingsDao.HTTP_CLIENT_PROXY_PASSWORD,
                SystemSettingsDao.getValue(SystemSettingsDao.HTTP_CLIENT_PROXY_PASSWORD));

        // Misc
        settings.put(SystemSettingsDao.EVENT_PURGE_PERIOD_TYPE,
                SystemSettingsDao.getIntValue(SystemSettingsDao.EVENT_PURGE_PERIOD_TYPE));
        settings.put(SystemSettingsDao.EVENT_PURGE_PERIODS,
                SystemSettingsDao.getIntValue(SystemSettingsDao.EVENT_PURGE_PERIODS));
        settings.put(SystemSettingsDao.REPORT_PURGE_PERIOD_TYPE,
                SystemSettingsDao.getIntValue(SystemSettingsDao.REPORT_PURGE_PERIOD_TYPE));
        settings.put(SystemSettingsDao.REPORT_PURGE_PERIODS,
                SystemSettingsDao.getIntValue(SystemSettingsDao.REPORT_PURGE_PERIODS));
        settings.put(SystemSettingsDao.UI_PERFORAMANCE,
                SystemSettingsDao.getIntValue(SystemSettingsDao.UI_PERFORAMANCE));
        settings.put(SystemSettingsDao.GROVE_LOGGING,
                SystemSettingsDao.getBooleanValue(SystemSettingsDao.GROVE_LOGGING));
        settings.put(SystemSettingsDao.FUTURE_DATE_LIMIT_PERIOD_TYPE,
                SystemSettingsDao.getIntValue(SystemSettingsDao.FUTURE_DATE_LIMIT_PERIOD_TYPE));
        settings.put(SystemSettingsDao.FUTURE_DATE_LIMIT_PERIODS,
                SystemSettingsDao.getIntValue(SystemSettingsDao.FUTURE_DATE_LIMIT_PERIODS));

        // System
        settings.put(SystemSettingsDao.NEW_VERSION_NOTIFICATION_LEVEL,
                SystemSettingsDao.getValue(SystemSettingsDao.NEW_VERSION_NOTIFICATION_LEVEL));
        settings.put(SystemSettingsDao.INSTANCE_DESCRIPTION,
                SystemSettingsDao.getValue(SystemSettingsDao.INSTANCE_DESCRIPTION));

        // Language
        settings.put(SystemSettingsDao.LANGUAGE, SystemSettingsDao.getValue(SystemSettingsDao.LANGUAGE));

        // Colours
        settings.put(SystemSettingsDao.CHART_BACKGROUND_COLOUR,
                SystemSettingsDao.getValue(SystemSettingsDao.CHART_BACKGROUND_COLOUR));
        settings.put(SystemSettingsDao.PLOT_BACKGROUND_COLOUR,
                SystemSettingsDao.getValue(SystemSettingsDao.PLOT_BACKGROUND_COLOUR));
        settings.put(SystemSettingsDao.PLOT_GRIDLINE_COLOUR,
                SystemSettingsDao.getValue(SystemSettingsDao.PLOT_GRIDLINE_COLOUR));
        UserDao userDao = new UserDao();
        settings.put("adminUsers", userDao.getAdminUsers());//
        settings.put("adminAddUsers", userDao.getAdminAddUsers());
        return settings;
    }

    @MethodFilter
    public Map<String, Object> getDatabaseSize() {
        Permissions.ensureAdmin();
        Map<String, Object> data = new HashMap<String, Object>();

        // Database size
        File dataDirectory = Common.ctx.getDatabaseAccess().getDataDirectory();
        long dbSize = 0;
        if (dataDirectory != null) {
            DirectoryInfo dbInfo = DirectoryUtils.getDirectorySize(dataDirectory);
            dbSize = dbInfo.getSize();
            data.put("databaseSize", DirectoryUtils.bytesDescription(dbSize));
        }
        else
            data.put("databaseSize", "(" + getMessage("common.unknown") + ")");

        // Filedata data
        DirectoryInfo fileDatainfo = DirectoryUtils.getDirectorySize(new File(Common.getFiledataPath()));
        long filedataSize = fileDatainfo.getSize();
        data.put("filedataCount", fileDatainfo.getCount());
        data.put("filedataSize", DirectoryUtils.bytesDescription(filedataSize));

        data.put("totalSize", DirectoryUtils.bytesDescription(dbSize + filedataSize));

        // Point history counts.
        List<PointHistoryCount> counts = new DataPointDao().getTopPointHistoryCounts();
        int sum = 0;
        for (PointHistoryCount c : counts)
            sum += c.getCount();

        data.put("historyCount", sum);
        data.put("topPoints", counts);
        data.put("eventCount", new EventDao().getEventCount());

        return data;
    }

    @MethodFilter
    public void saveEmailSettings(String host, int port, String from, String name, boolean auth, String username,
            String password, boolean tls, int contentType,String emailType,String url,String exchangeUsrename,String exchangePassword,String domain) {
        Permissions.ensureAdmin();
        SystemSettingsDao systemSettingsDao = new SystemSettingsDao();
        systemSettingsDao.setValue(SystemSettingsDao.EMAIL_SMTP_HOST, host);
        systemSettingsDao.setIntValue(SystemSettingsDao.EMAIL_SMTP_PORT, port);
        systemSettingsDao.setValue(SystemSettingsDao.EMAIL_FROM_ADDRESS, from);
        systemSettingsDao.setValue(SystemSettingsDao.EMAIL_FROM_NAME, name);
        systemSettingsDao.setBooleanValue(SystemSettingsDao.EMAIL_AUTHORIZATION, auth);
        systemSettingsDao.setValue(SystemSettingsDao.EMAIL_SMTP_USERNAME, username);
        systemSettingsDao.setValue(SystemSettingsDao.EMAIL_SMTP_PASSWORD, password);
        systemSettingsDao.setBooleanValue(SystemSettingsDao.EMAIL_TLS, tls);
        systemSettingsDao.setIntValue(SystemSettingsDao.EMAIL_CONTENT_TYPE, contentType);
        systemSettingsDao.setValue(SystemSettingsDao.EMAIL_TYPE, emailType);
        systemSettingsDao.setValue(SystemSettingsDao.EXCHANGE_URL, url);
        systemSettingsDao.setValue(SystemSettingsDao.EXCHANGE_USERNAME, exchangeUsrename);
        systemSettingsDao.setValue(SystemSettingsDao.EXCHANGE_PASSWORD, exchangePassword);
        systemSettingsDao.setValue(SystemSettingsDao.EXCHANGE_DOMAIN, domain);
    }

    @MethodFilter
    public Map<String, Object> sendTestEmail(String host, int port, String from, String name, boolean auth,
            String username, String password, boolean tls, int contentType,String emailType,String url,String exchangeUsrename,String exchangePassword,String domain) {
        Permissions.ensureAdmin();

        // Save the settings
        saveEmailSettings(host, port, from, name, auth, username, password, tls, contentType,emailType,url,exchangeUsrename,exchangePassword,domain);

        // Get the web context information
        User user = Common.getUser();

        Map<String, Object> result = new HashMap<String, Object>();
        try {
            ResourceBundle bundle = getResourceBundle();
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("message", new LocalizableMessage("systemSettings.testEmail"));
            if(SystemSettingsDao.isExchange()){
            	List<EmailAddress> address=new ArrayList<EmailAddress>();
        		EmailAddress add=new EmailAddress();
            	add.setAddress(user.getEmail());
            	address.add(add);
            	 Content exchangeContent = new Content("testEmail", model, bundle, I18NUtils.getMessage(bundle,
	                "ftl.testEmail"), Common.UTF8);
            	 ExchangeSend exchangeSend=new ExchangeSend(address,exchangeContent);
	                exchangeSend.sendMail();
            }
            else{
	            MangoEmailContent cnt = new MangoEmailContent("testEmail", model, bundle, I18NUtils.getMessage(bundle,
	                    "ftl.testEmail"), Common.UTF8);
	            EmailWorkItem.queueEmail(user.getEmail(), cnt);
            }
            result.put("message", new LocalizableMessage("common.testEmailSent", user.getEmail()));
        }
        catch (Exception e) {
            result.put("exception", e.getMessage());
        }
        return result;
    }

    @MethodFilter
    public void saveSystemEventAlarmLevels(List<IntegerPair> eventAlarmLevels) {
        Permissions.ensureAdmin();
        for (IntegerPair eventAlarmLevel : eventAlarmLevels)
            SystemEventType.setEventTypeAlarmLevel(eventAlarmLevel.getI1(), eventAlarmLevel.getI2());
    }

    @MethodFilter
    public void saveAuditEventAlarmLevels(List<IntegerPair> eventAlarmLevels) {
        Permissions.ensureAdmin();
        for (IntegerPair eventAlarmLevel : eventAlarmLevels)
            AuditEventType.setEventTypeAlarmLevel(eventAlarmLevel.getI1(), eventAlarmLevel.getI2());
    }

    @MethodFilter
    public void saveHttpSettings(boolean useProxy, String host, int port, String username, String password) {
        Permissions.ensureAdmin();
        SystemSettingsDao systemSettingsDao = new SystemSettingsDao();
        systemSettingsDao.setBooleanValue(SystemSettingsDao.HTTP_CLIENT_USE_PROXY, useProxy);
        systemSettingsDao.setValue(SystemSettingsDao.HTTP_CLIENT_PROXY_SERVER, host);
        systemSettingsDao.setIntValue(SystemSettingsDao.HTTP_CLIENT_PROXY_PORT, port);
        systemSettingsDao.setValue(SystemSettingsDao.HTTP_CLIENT_PROXY_USERNAME, username);
        systemSettingsDao.setValue(SystemSettingsDao.HTTP_CLIENT_PROXY_PASSWORD, password);
    }

    @MethodFilter
    public void saveMiscSettings(int eventPurgePeriodType, int eventPurgePeriods, int reportPurgePeriodType,
            int reportPurgePeriods, int uiPerformance, boolean groveLogging, int futureDateLimitPeriodType,
            int futureDateLimitPeriods) {
        Permissions.ensureAdmin();
        SystemSettingsDao systemSettingsDao = new SystemSettingsDao();
        systemSettingsDao.setIntValue(SystemSettingsDao.EVENT_PURGE_PERIOD_TYPE, eventPurgePeriodType);
        systemSettingsDao.setIntValue(SystemSettingsDao.EVENT_PURGE_PERIODS, eventPurgePeriods);
        systemSettingsDao.setIntValue(SystemSettingsDao.REPORT_PURGE_PERIOD_TYPE, reportPurgePeriodType);
        systemSettingsDao.setIntValue(SystemSettingsDao.REPORT_PURGE_PERIODS, reportPurgePeriods);
        systemSettingsDao.setIntValue(SystemSettingsDao.UI_PERFORAMANCE, uiPerformance);
        systemSettingsDao.setBooleanValue(SystemSettingsDao.GROVE_LOGGING, groveLogging);
        systemSettingsDao.setIntValue(SystemSettingsDao.FUTURE_DATE_LIMIT_PERIOD_TYPE, futureDateLimitPeriodType);
        systemSettingsDao.setIntValue(SystemSettingsDao.FUTURE_DATE_LIMIT_PERIODS, futureDateLimitPeriods);
    }

    @MethodFilter
    public DwrResponseI18n saveColourSettings(String chartBackgroundColour, String plotBackgroundColour,
            String plotGridlineColour) {
        Permissions.ensureAdmin();

        DwrResponseI18n response = new DwrResponseI18n();

        try {
            ColorUtils.toColor(chartBackgroundColour);
        }
        catch (InvalidArgumentException e) {
            response.addContextualMessage(SystemSettingsDao.CHART_BACKGROUND_COLOUR,
                    "systemSettings.validation.invalidColour");
        }

        try {
            ColorUtils.toColor(plotBackgroundColour);
        }
        catch (InvalidArgumentException e) {
            response.addContextualMessage(SystemSettingsDao.PLOT_BACKGROUND_COLOUR,
                    "systemSettings.validation.invalidColour");
        }

        try {
            ColorUtils.toColor(plotGridlineColour);
        }
        catch (InvalidArgumentException e) {
            response.addContextualMessage(SystemSettingsDao.PLOT_GRIDLINE_COLOUR,
                    "systemSettings.validation.invalidColour");
        }

        if (!response.getHasMessages()) {
            SystemSettingsDao systemSettingsDao = new SystemSettingsDao();
            systemSettingsDao.setValue(SystemSettingsDao.CHART_BACKGROUND_COLOUR, chartBackgroundColour);
            systemSettingsDao.setValue(SystemSettingsDao.PLOT_BACKGROUND_COLOUR, plotBackgroundColour);
            systemSettingsDao.setValue(SystemSettingsDao.PLOT_GRIDLINE_COLOUR, plotGridlineColour);
        }

        return response;
    }

    @MethodFilter
    public void saveInfoSettings(String newVersionNotificationLevel, String instanceDescription) {
        Permissions.ensureAdmin();
        SystemSettingsDao systemSettingsDao = new SystemSettingsDao();
        systemSettingsDao.setValue(SystemSettingsDao.NEW_VERSION_NOTIFICATION_LEVEL, newVersionNotificationLevel);
        systemSettingsDao.setValue(SystemSettingsDao.INSTANCE_DESCRIPTION, instanceDescription);
    }

    @MethodFilter
    public String newVersionCheck(String newVersionNotificationLevel) {
        Permissions.ensureAdmin();
        try {
            return getMessage(VersionCheck.newVersionCheck(newVersionNotificationLevel));
        }
        catch (SocketTimeoutException e) {
            return getMessage("systemSettings.versionCheck1");
        }
        catch (Exception e) {
            return getMessage(new LocalizableMessage("systemSettings.versionCheck2", e.getClass().getName(),
                    e.getMessage()));
        }
    }

    @MethodFilter
    public void saveLanguageSettings(String language) {
        Permissions.ensureAdmin();
        SystemSettingsDao systemSettingsDao = new SystemSettingsDao();
        systemSettingsDao.setValue(SystemSettingsDao.LANGUAGE, language);
        Common.setSystemLanguage(language);
    }

    @MethodFilter
    public void purgeNow() {
        Permissions.ensureAdmin();
        DataPurge dataPurge = new DataPurge();
        dataPurge.execute(System.currentTimeMillis());
    }

    @MethodFilter
    public LocalizableMessage purgeAllData() {
        Permissions.ensureAdmin();
        long cnt = Common.ctx.getRuntimeManager().purgeDataPointValues();
        return new LocalizableMessage("systemSettings.purgeDataComplete", cnt);
    }
    @MethodFilter
	public DwrResponseI18n updateUserAddLimit(int factoryId, int limit) {
		DwrResponseI18n response = new DwrResponseI18n();
		UserDao userDao = new UserDao();
		if (limit < 0)
			response.addMessage(new LocalizableMessage("users.add.error"));
		if(limit >100)
			response.addMessage(new LocalizableMessage("users.add.overtake"));
		if(!response.getHasMessages())
			userDao.updateUserAddLimit(limit,factoryId);
		return response;
	}
    @MethodFilter
	public DwrResponseI18n updateAllUserAddLimit(int[][] userLimits) {
		DwrResponseI18n response = new DwrResponseI18n();
		UserDao userDao = new UserDao();
		for (int i = 0; i < userLimits.length; i++) {
			if(userLimits[i][1]>100){
				response.addMessage(new LocalizableMessage(
				"users.add.overtake"));
				break;
			}
			if(userLimits[i][1]<0){
				response.addMessage(new LocalizableMessage("users.add.error"));
				break;
			}
			if(!response.getHasMessages())
				userDao.updateUserAddLimit(userLimits[i][1], userLimits[i][0]);
		}
		return response;
	}
}
