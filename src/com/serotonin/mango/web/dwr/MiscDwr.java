/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.dwr;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import com.serotonin.mango.vo.scope.ScopeVO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.serotonin.mango.db.dao.scope.ScopeDao;

import microsoft.exchange.webservices.data.EmailAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.springframework.beans.propertyeditors.LocaleEditor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.serotonin.io.StreamUtils;
import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.EventDao;
import com.serotonin.mango.db.dao.MailingListDao;
import com.serotonin.mango.db.dao.SystemSettingsDao;
import com.serotonin.mango.db.dao.UserDao;
import com.serotonin.mango.rt.EventManager;
import com.serotonin.mango.rt.event.EventInstance;
import com.serotonin.mango.rt.exchange.ExchangeContent;
import com.serotonin.mango.rt.maint.work.EmailWorkItem;
import com.serotonin.mango.util.DocumentationItem;
import com.serotonin.mango.util.DocumentationManifest;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.vo.WatchList;
import com.serotonin.mango.web.dwr.beans.CustomComponentState;
import com.serotonin.mango.web.dwr.beans.RecipientListEntryBean;
import com.serotonin.mango.web.dwr.beans.ViewComponentState;
import com.serotonin.mango.web.dwr.beans.WatchListState;
import com.serotonin.mango.web.dwr.longPoll.LongPollData;
import com.serotonin.mango.web.dwr.longPoll.LongPollRequest;
import com.serotonin.mango.web.dwr.longPoll.LongPollState;
import com.serotonin.mango.web.email.MangoEmailContent;
import com.serotonin.util.StringUtils;
import com.serotonin.web.dwr.DwrResponseI18n;
import com.serotonin.web.dwr.MethodFilter;
import com.serotonin.web.i18n.I18NUtils;
import com.serotonin.web.i18n.LocalizableMessage;


import com.serotonin.mango.rt.exchange.ExchangeSend;
import com.serotonin.mango.rt.exchange.Content;
public class MiscDwr extends BaseDwr {
    public static final Log LOG = LogFactory.getLog(MiscDwr.class);
    private static final String LONG_POLL_DATA_KEY = "LONG_POLL_DATA";
    private static final String LONG_POLL_DATA_TIMEOUT_KEY = "LONG_POLL_DATA_TIMEOUT";

    private final WatchListDwr watchListDwr = new WatchListDwr();
    private final DataPointDetailsDwr dataPointDetailsDwr = new DataPointDetailsDwr();
    private final ViewDwr viewDwr = new ViewDwr();
    private final CustomViewDwr customViewDwr = new CustomViewDwr();

    public DwrResponseI18n toggleSilence(int eventId) {
        DwrResponseI18n response = new DwrResponseI18n();
        response.addData("eventId", eventId);

        User user = Common.getUser();
        if (user != null) {
            boolean result = new EventDao().toggleSilence(eventId, user.getId());
            resetLastAlarmLevelChange();
            response.addData("silenced", result);
        }
        else
            response.addData("silenced", false);

        return response;
    }

    @MethodFilter
    public DwrResponseI18n silenceAll() {
        List<Integer> silenced = new ArrayList<Integer>();
        User user = Common.getUser();
        EventDao eventDao = new EventDao();
        for (EventInstance evt : eventDao.getPendingEvents(user)) {
            if (!evt.isSilenced()) {
                eventDao.toggleSilence(evt.getId(), user.getId());
                silenced.add(evt.getId());
            }
        }

        resetLastAlarmLevelChange();

        DwrResponseI18n response = new DwrResponseI18n();
        response.addData("silenced", silenced);
        return response;
    }

    public int acknowledgeEvent(int eventId) {
        User user = Common.getUser();
        if (user != null) {
            new EventDao().ackEvent(eventId, System.currentTimeMillis(), user.getId(), 0);
            resetLastAlarmLevelChange();
        }
        return eventId;
    }

    public void acknowledgeAllPendingEvents() {
        User user = Common.getUser();
        if (user != null) {
            EventDao eventDao = new EventDao();
            long now = System.currentTimeMillis();
            for (EventInstance evt : eventDao.getPendingEvents(user))
                eventDao.ackEvent(evt.getId(), now, user.getId(), 0);
            resetLastAlarmLevelChange();
        }
    }

    public boolean toggleUserMuted() {
        User user = Common.getUser();
        if (user != null) {
            user.setMuted(!user.isMuted());
            return user.isMuted();
        }
        return false;
    }

    public Map<String, Object> getDocumentationItem(String documentId) {
        Map<String, Object> result = new HashMap<String, Object>();

        DocumentationManifest manifest = Common.ctx.getDocumentationManifest();
        DocumentationItem item = manifest.getItem(documentId);
        if (item == null)
            result.put("error", getMessage("dox.notFound"));
        else {
            // Read the content.
            String filename = Common.getDocPath() + "/" + getMessage("dox.dir") + "/" + documentId + ".htm";
            try {
                Reader in = new FileReader(filename);
                StringWriter out = new StringWriter();
                StreamUtils.transfer(in, out);
                in.close();

                addDocumentationItem(result, item);
                result.put("content", out.toString());

                List<Map<String, Object>> related = new ArrayList<Map<String, Object>>();
                for (String relatedId : item.getRelated()) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    related.add(map);
                    addDocumentationItem(map, manifest.getItem(relatedId));
                }

                result.put("relatedList", related);
            }
            catch (FileNotFoundException e) {
                result.put("error", getMessage("dox.notFound") + " " + filename);
            }
            catch (IOException e) {
                result.put("error", getMessage("dox.readError") + " " + e.getClass().getName() + ": " + e.getMessage());
            }
        }

        return result;
    }

    private void addDocumentationItem(Map<String, Object> map, DocumentationItem di) {
        map.put("id", di.getId());
        map.put("title", getMessage("dox." + di.getId()));
    }

    public void jsError(String desc, String page, String line, String browserName, String browserVersion,
            String osName, String location) {
        LOG.warn("Javascript error\r\n" + "   Description: " + desc + "\r\n" + "   Page: " + page + "\r\n"
                + "   Line: " + line + "\r\n" + "   Browser name: " + browserName + "\r\n" + "   Browser version: "
                + browserVersion + "\r\n" + "   osName: " + osName + "\r\n" + "   location: " + location);
    }

    @MethodFilter
    public DwrResponseI18n sendTestEmail(List<RecipientListEntryBean> recipientList, String prefix, String message) {
        DwrResponseI18n response = new DwrResponseI18n();

        String[] toAddrs = new MailingListDao().getRecipientAddresses(recipientList, null).toArray(new String[0]);
        if (toAddrs.length == 0)
            response.addGenericMessage("js.email.noRecipForEmail");
        else {
            try {
                ResourceBundle bundle = Common.getBundle();
                Map<String, Object> model = new HashMap<String, Object>();
                model.put("user", Common.getUser());
                model.put("message", new LocalizableMessage("common.default", message));
                if(SystemSettingsDao.isExchange()){
                	List<EmailAddress> address=new ArrayList<EmailAddress>();
                		for (String email : toAddrs) {
                			EmailAddress add=new EmailAddress();
                    		add.setAddress(email);
                    		address.add(add);
					}
	                 Content exchangeContent = new Content("testEmail", model, bundle, I18NUtils.getMessage(bundle,
	                "ftl.testEmail"), Common.UTF8);
	                ExchangeSend exchangeSend=new ExchangeSend(address,exchangeContent);
	                exchangeSend.sendMail();
                }
                else{
                	MangoEmailContent cnt = new MangoEmailContent("testEmail", model, bundle, I18NUtils.getMessage(bundle,
                		"ftl.testEmail"), Common.UTF8);
                	EmailWorkItem.queueEmail(toAddrs, cnt);
                }
            }
            catch (Exception e) {
                response.addGenericMessage("common.default", e.getMessage());
            }
        }

        response.addData("prefix", prefix);

        return response;
    }

    public void setLocale(String locale) {
        WebContext webContext = WebContextFactory.get();

        LocaleResolver localeResolver = new SessionLocaleResolver();

        LocaleEditor localeEditor = new LocaleEditor();
        localeEditor.setAsText(locale);

        localeResolver.setLocale(webContext.getHttpServletRequest(), webContext.getHttpServletResponse(),
                (Locale) localeEditor.getValue());
    }

    @MethodFilter
    public void setHomeUrl(String url) {
        // Remove the scheme, domain, and context if there.
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();

        // Remove the scheme.
        url = url.substring(request.getScheme().length() + 3);

        // Remove the domain.
        url = url.substring(request.getServerName().length());

        // Remove the port
        if (url.charAt(0) == ':')
            url = url.substring(Integer.toString(request.getServerPort()).length() + 1);

        // Remove the context
        url = url.substring(request.getContextPath().length());

        // Remove any leading /
        if (url.charAt(0) == '/')
            url = url.substring(1);

        // Save the result
        new UserDao().saveHomeUrl(Common.getUser().getId(), url);
        Common.getUser().setHomeUrl(url);
        
    }

    @MethodFilter
    public String getHomeUrl() {
        String url = Common.getUser().getHomeUrl();
        if (StringUtils.isEmpty(url))
            url = "watch_list.shtm";
        return url;
    }

    //
    // /
    // / Long poll
    // /
    //
    public Map<String, Object> initializeLongPoll(int pollSessionId, LongPollRequest request) {
        LongPollData data = getLongPollData(pollSessionId, true);
        data.setRequest(request);
        return doLongPoll(pollSessionId);
    }

    public Map<String, Object> doLongPoll(int pollSessionId) {
        Map<String, Object> response = new HashMap<String, Object>();
        HttpServletRequest httpRequest = WebContextFactory.get().getHttpServletRequest();
        User user = Common.getUser(httpRequest);
        EventManager eventManager = Common.ctx.getEventManager();
        EventDao eventDao = new EventDao();

        LongPollData data = getLongPollData(pollSessionId, false);
        data.updateTimestamp();

        LongPollRequest pollRequest = data.getRequest();

        long expireTime = System.currentTimeMillis() + 60000; // One minute
        LongPollState state = data.getState();
        int waitTime = SystemSettingsDao.getIntValue(SystemSettingsDao.UI_PERFORAMANCE);

        // For users that log in on multiple machines (or browsers), reset the last alarm timestamp so that it always
        // gets reset with at least each new poll. For now this beats writing user-specific event change tracking code.
        state.setLastAlarmLevelChange(0);

        while (!pollRequest.isTerminated() && System.currentTimeMillis() < expireTime) {
            if (pollRequest.isMaxAlarm() && user != null) {
                // Check the max alarm. First check if the events have changed since the last time this request checked.
                long lastEMUpdate = eventManager.getLastAlarmTimestamp();
                if (state.getLastAlarmLevelChange() < lastEMUpdate) {
                    state.setLastAlarmLevelChange(lastEMUpdate);

                    // The events have changed. See if the user's particular max alarm level has changed.
                    int maxAlarmLevel = eventDao.getHighestUnsilencedAlarmLevel(user);
                    if (maxAlarmLevel != state.getMaxAlarmLevel()) {
                        response.put("highestUnsilencedAlarmLevel", maxAlarmLevel);
                        state.setMaxAlarmLevel(maxAlarmLevel);
                    }
                }
            }

            if (pollRequest.isWatchList() && user != null) {
                synchronized (state) {
                    List<WatchListState> newStates = watchListDwr.getPointData();
                    List<WatchListState> differentStates = new ArrayList<WatchListState>();

                    for (WatchListState newState : newStates) {
                        WatchListState oldState = state.getWatchListState(newState.getId());
                        if (oldState == null)
                            differentStates.add(newState);
                        else {
                            WatchListState copy = newState.clone();
                            copy.removeEqualValue(oldState);
                            if (!copy.isEmpty())
                                differentStates.add(copy);
                        }
                    }

                    if (!differentStates.isEmpty()) {
                        response.put("watchListStates", differentStates);
                        state.setWatchListStates(newStates);
                    }
                }
            }
//           if(pollRequest.dynamicData() ){
//        	   
//           }
            if (pollRequest.isPointDetails() && user != null) {
                WatchListState newState = dataPointDetailsDwr.getPointData();
                WatchListState responseState;
                WatchListState oldState = state.getPointDetailsState();

                if (oldState == null)
                    responseState = newState;
                else {
                    responseState = newState.clone();
                    responseState.removeEqualValue(oldState);
                }

                if (!responseState.isEmpty()) {
                    response.put("pointDetailsState", responseState);
                    state.setPointDetailsState(newState);
                }
            }

            if ((pollRequest.isView() && user != null) || (pollRequest.isViewEdit() && user != null)
                    || pollRequest.getAnonViewId() > 0) {
                List<ViewComponentState> newStates;
                if (pollRequest.getAnonViewId() > 0)
                    newStates = viewDwr.getViewPointDataAnon(pollRequest.getAnonViewId());
                else
                    newStates = viewDwr.getViewPointData(pollRequest.isViewEdit());
                List<ViewComponentState> differentStates = new ArrayList<ViewComponentState>();

                for (ViewComponentState newState : newStates) {
                    ViewComponentState oldState = state.getViewComponentState(newState.getId());
                    if (oldState == null)
                        differentStates.add(newState);
                    else {
                        ViewComponentState copy = newState.clone();
                        copy.removeEqualValue(oldState);
                        if (!copy.isEmpty())
                            differentStates.add(copy);
                    }
                }

                if (!differentStates.isEmpty()) {
                    response.put("viewStates", differentStates);
                    state.setViewComponentStates(newStates);
                }
            }

            if (pollRequest.isCustomView()) {
                List<CustomComponentState> newStates = customViewDwr.getViewPointData();
                List<CustomComponentState> differentStates = new ArrayList<CustomComponentState>();

                for (CustomComponentState newState : newStates) {
                    CustomComponentState oldState = state.getCustomViewState(newState.getId());
                    if (oldState == null)
                        differentStates.add(newState);
                    else {
                        CustomComponentState copy = newState.clone();
                        copy.removeEqualValue(oldState);
                        if (!copy.isEmpty())
                            differentStates.add(copy);
                    }
                }

                if (!differentStates.isEmpty()) {
                    response.put("customViewStates", differentStates);
                    state.setCustomViewStates(newStates);
                }
            }

            if (pollRequest.isPendingAlarms() && user != null) {
                // Create the list of most current pending alarm content.
                Map<String, Object> model = new HashMap<String, Object>();
                model.put("events", eventDao.getPendingEvents(user));
                model.put("pendingEvents", true);
                model.put("noContentWhenEmpty", true);
                String currentContent = generateContent(httpRequest, "eventList.jsp", model);
                currentContent = StringUtils.trimWhitespace(currentContent);

                if (!StringUtils.isEqual(currentContent, state.getPendingAlarmsContent())) {
                    response.put("pendingAlarmsContent", currentContent);
                    state.setPendingAlarmsContent(currentContent);
                }
            }

            if (!response.isEmpty())
                break;

            synchronized (pollRequest) {
                try {
                    pollRequest.wait(waitTime);
                }
                catch (InterruptedException e) {
                    // no op
                }
            }

        }

        if (pollRequest.isTerminated())
            response.put("terminated", true);

        return response;
    }

    public void terminateLongPoll(int pollSessionId) {
        terminateLongPollImpl(getLongPollData(pollSessionId, false));
    }

    public static void terminateLongPollImpl(LongPollData longPollData) {
        LongPollRequest request = longPollData.getRequest();
        if (request == null)
            return;

        request.setTerminated(true);
        notifyLongPollImpl(request);
    }

    public void resetWatchlistState(int pollSessionId) {
        LongPollData data = getLongPollData(pollSessionId, false);

        synchronized (data.getState()) {
            data.getState().getWatchListStates().clear();
            WatchList wl = Common.getUser().getWatchList();
            for (DataPointVO dp : wl.getPointList())
                dp.resetLastValue();
        }
        notifyLongPollImpl(data.getRequest());
    }

    public void notifyLongPoll(int pollSessionId) {
        notifyLongPollImpl(getLongPollData(pollSessionId, false).getRequest());
    }

    private static void notifyLongPollImpl(LongPollRequest request) {
        synchronized (request) {
            request.notifyAll();
        }
    }

    private LongPollData getLongPollData(int pollSessionId, boolean refreshState) {
        List<LongPollData> dataList = getLongPollData();

        LongPollData data = getDataFromList(dataList, pollSessionId);
        if (data == null) {
            synchronized (dataList) {
                data = getDataFromList(dataList, pollSessionId);
                if (data == null) {
                    data = new LongPollData(pollSessionId);
                    refreshState = true;
                    dataList.add(data);
                }
            }
        }

        if (refreshState)
            data.setState(new LongPollState());

        return data;
    }

    private LongPollData getDataFromList(List<LongPollData> dataList, int pollSessionId) {
        for (LongPollData data : dataList) {
            if (data.getPollSessionId() == pollSessionId)
                return data;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private List<LongPollData> getLongPollData() {
        HttpSession session = WebContextFactory.get().getSession();

        List<LongPollData> data = (List<LongPollData>) session.getAttribute(LONG_POLL_DATA_KEY);
        if (data == null) {
            synchronized (session) {
                data = (List<LongPollData>) session.getAttribute(LONG_POLL_DATA_KEY);
                if (data == null) {
                    data = new ArrayList<LongPollData>();
                    session.setAttribute(LONG_POLL_DATA_KEY, data);
                }
            }
        }

        // Check for old data objects.
        Long lastTimeoutCheck = (Long) session.getAttribute(LONG_POLL_DATA_TIMEOUT_KEY);
        if (lastTimeoutCheck == null)
            lastTimeoutCheck = 0L;
        long cutoff = System.currentTimeMillis() - (1000 * 60 * 5); // Five minutes.
        if (lastTimeoutCheck < cutoff) {
            synchronized (data) {
                Iterator<LongPollData> iter = data.iterator();
                while (iter.hasNext()) {
                    LongPollData lpd = iter.next();
                    if (lpd.getTimestamp() < cutoff)
                        iter.remove();
                }
            }

            session.setAttribute(LONG_POLL_DATA_TIMEOUT_KEY, System.currentTimeMillis());
        }

        return data;
    }

    private void resetLastAlarmLevelChange() {
        List<LongPollData> data = getLongPollData();

        synchronized (data) {
            // Check if this user has a current long poll request (very likely)
            for (LongPollData lpd : data) {
                LongPollState state = lpd.getState();
                // Reset the last alarm level change time so that the alarm level gets rechecked.
                state.setLastAlarmLevelChange(0);
                // Notify the long poll thread so that any change
                notifyLongPollImpl(lpd.getRequest());
            }
        }
    }
    
//    /**
//     * 销毁session
//     * @param username 用户名
//     */
//    public void destroySession(){
//    	HttpSession session = WebContextFactory.get().getSession();
//    	//User user = (User)session.getAttribute("sessionUser");
//    	//ServletContext context = session.getServletContext();
//    	//if(user!=null) context.removeAttribute(user.getUsername());
//    	session.invalidate();
//    }
    /**
     * 检查是否已经登陆
     * @param username 用户名称
     * @return 是否登陆
     */
    public boolean isLogined(String username){
    	HttpSession session = WebContextFactory.get().getSession();
    	ServletContext context = session.getServletContext();
    	HttpSession userSession = (HttpSession)context.getAttribute(username);
    	//同一个用户在同一个浏览器再次登陆时候，删除之前的session从新创建一个session
    	if(session==userSession) return false;
    	if(userSession==null){
    		return false;
    	}else{
	    	User user = (User)userSession.getAttribute("sessionUser");
	    	if(user!=null){
	    		return true;
	    	}else{
	    		return false;
	    	}
    	}
    }
//    /**
//     * 每个客户端每秒钟调用一次该方法，用这种方法保持用户在线，一旦10秒钟没有调用，则说明该用户退出
//     * 10秒钟允许一定的延迟
//     */
//    public void uesrConnection(){}
//    
//    
    /**
     * 获取范围列表
     * @param parentId 父范围ID
     * @param scopeType 范围类型
     * @return 范围列表
     */
    public DwrResponseI18n getScopeListByType(int parentId,int scopeType){
    	DwrResponseI18n response = new DwrResponseI18n();
    	User user = Common.getUser();
    	if(user==null){
    		response.addData("isLog",false);
    	}
    	else{
    		response.addData("isLog",true);
	    	ScopeDao scopeDao = new ScopeDao();
	    	List<ScopeVO> scopeList = null;
	    	switch(scopeType){
	    	case ScopeVO.ScopeTypes.ZONE:
	    		scopeList = scopeDao.getZoneList();
	    		UserDao.validateScopes(scopeList, user);
	    		break;
	    	case ScopeVO.ScopeTypes.SUBZONE:
	    		if(parentId!=-1){
	    			scopeList = scopeDao.getsubZoneList(parentId);
	    			UserDao.validateScopes(scopeList, user);
	    		}else{
	    			scopeList = scopeDao.getsubZoneList();
	    			UserDao.validateScopes(scopeList, user);
	    		}
	    		break;
	    	case ScopeVO.ScopeTypes.FACTORY:
	    		if(parentId!=-1){
	    			scopeList = scopeDao.getFactoryBySubZone(parentId);
	    			UserDao.validateScopes(scopeList, user);
	    		}else{
	    			if(user.getHomeScope().getScopetype()==ScopeVO.ScopeTypes.ZONE){
	    				scopeList = scopeDao.getFactoryByZone(user.getHomeScope().getId());
	        			UserDao.validateScopes(scopeList, user);
	    			}else
	    			scopeList = scopeDao.getFactoryByHq();
	    			UserDao.validateScopes(scopeList, user);
	    		}
	    		break;
	    	}
	    	response.addData("scopeList",scopeList);
    	}
    	return response;
    }
    
}
