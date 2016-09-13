/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.dwr;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.WebContextFactory;
import org.joda.time.DateTime;

import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.EventDao;
import com.serotonin.mango.rt.event.EventInstance;
import com.serotonin.mango.util.DateUtils;
import com.serotonin.mango.vo.User;
import com.serotonin.util.StringUtils;
import com.serotonin.web.dwr.DwrResponseI18n;
import com.serotonin.web.i18n.LocalizableMessage;
import com.serotonin.mango.vo.scope.ScopeVO;

public class EventsDwr extends BaseDwr {
    private static final int PAGE_SIZE = 50;
    private static final int PAGINATION_RADIUS = 3;

    public static final String STATUS_ALL = "*";
    public static final String STATUS_ACTIVE = "A";
    public static final String STATUS_RTN = "R";
    public static final String STATUS_NORTN = "N";
    public DwrResponseI18n init(){
    	DwrResponseI18n response = new DwrResponseI18n();
    	Map<String, Object> model = new HashMap<String, Object>();
    	HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
    	User user = Common.getUser(request);
    	EventDao eventDao = new EventDao();
    	model.put("pendingEvents", false);
    	if(user.getCurrentScope().getScopetype()==ScopeVO.ScopeTypes.FACTORY){//工厂
    	 	model.put("yellowAlerts",eventDao.getPendingAlarms(user,1));
        	model.put("orangeAlerts",eventDao.getPendingAlarms(user,2));
        	model.put("redAlerts",eventDao.getPendingAlarms(user,3));
    		response.addData("content", generateContent(request, "redAlertList.jsp", model));
    	}
    	else if(user.getCurrentScope().getScopetype()==ScopeVO.ScopeTypes.SUBZONE){//子区域
    		model.put("yellowAlerts",eventDao.getPendingAlarms(user,1));
        	model.put("orangeAlerts",eventDao.getPendingAlarms(user,2));
        	model.put("redAlerts",eventDao.getPendingAlarms(user,3));
    		response.addData("content", generateContent(request, "redAlertList.jsp", model));
    	}
    	else if(user.getCurrentScope().getScopetype()==ScopeVO.ScopeTypes.ZONE){//区域
        	model.put("orangeAlerts",eventDao.getPendingAlarms(user,2));
    		response.addData("content", generateContent(request, "zoneOrangeAlertList.jsp", model));
    	}
    	else{//(user.getCurrentScope().getScopetype()==ScopeVO.ScopeTypes.HQ){//总部
        	model.put("redAlerts",eventDao.getPendingAlarms(user,3));
    		response.addData("content", generateContent(request, "centerRedAlertList.jsp", model));
    	}
    	return response;
    }
    public DwrResponseI18n search(int eventId, int eventSourceType, String status, int alarmLevel, String keywordStr,
            int page, Date date) {
        DwrResponseI18n response = new DwrResponseI18n();
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        User user = Common.getUser(request);

        String[] keywordArr = keywordStr.split("\\s+");
        List<String> keywords = new ArrayList<String>();
        for (String s : keywordArr) {
            if (!StringUtils.isEmpty(s))
                keywords.add(s);
        }

        if (keywords.isEmpty())
            keywordArr = null;
        else {
            keywordArr = new String[keywords.size()];
            keywords.toArray(keywordArr);
        }

        int from = PAGE_SIZE * page;
        int to = from + PAGE_SIZE;

        // The date is set for the top of the day, which will end up excluding all of the events for that day. So,
        // we need to add 1 day to it.
        if (date != null)
            date = DateUtils.minus(new DateTime(date.getTime()), Common.TimePeriods.DAYS, 0).toDate();

        EventDao eventDao = new EventDao();
        List<EventInstance> results = eventDao.search(eventId, eventSourceType, status, alarmLevel, keywordArr, user
                .getId(), getResourceBundle(), from, to, date,user.getCurrentScope().getId());

        Map<String, Object> model = new HashMap<String, Object>();
        int searchRowCount = eventDao.getSearchRowCount();
        int pages = (int) Math.ceil(((double) searchRowCount) / PAGE_SIZE);

        if (date != null) {
            int startRow = eventDao.getStartRow();
            if (startRow == -1)
                page = pages - 1;
            else
                page = eventDao.getStartRow() / PAGE_SIZE;
        }

        if (pages > 1) {
            model.put("displayPagination", true);

            if (page - PAGINATION_RADIUS > 1)
                model.put("leftEllipsis", true);
            else
                model.put("leftEllipsis", false);

            int linkFrom = page + 1 - PAGINATION_RADIUS;
            if (linkFrom < 2)
                linkFrom = 2;
            model.put("linkFrom", linkFrom);
            int linkTo = page + 1 + PAGINATION_RADIUS;
            if (linkTo >= pages)
                linkTo = pages - 1;
            model.put("linkTo", linkTo);

            if (page + PAGINATION_RADIUS < pages - 2)
                model.put("rightEllipsis", true);
            else
                model.put("rightEllipsis", false);

            model.put("numberOfPages", pages);
        }
        else
            model.put("displayPagination", false);

        model.put("events", results);
        model.put("page", page);
        model.put("pendingEvents", false);

        response.addData("content", generateContent(request, "eventList.jsp", model));
        response.addData("resultCount", new LocalizableMessage("events.search.resultCount", searchRowCount));

        return response;
    }
}
