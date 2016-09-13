/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.mvc.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.LocaleResolver;

import com.serotonin.mango.db.dao.DataPointDao;
import com.serotonin.mango.vo.DataPointExtendedNameComparator;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.vo.permission.Permissions;
import com.serotonin.web.i18n.Utf8ResourceBundle;

/**
 *  
 */
public class ControllerUtils {
    public static ResourceBundle getResourceBundle(HttpServletRequest request) {
        return Utf8ResourceBundle.getBundle("messages", getLocale(request));
    }

    public static Locale getLocale(HttpServletRequest request) {
        WebApplicationContext webApplicationContext = WebApplicationContextUtils
                .getRequiredWebApplicationContext(request.getSession().getServletContext());
        LocaleResolver localeResolver = (LocaleResolver) webApplicationContext.getBean("localeResolver");
        return localeResolver.resolveLocale(request);
    }

    public static void addPointListDataToModel(User user, int pointId, Map<String, Object> model) {
        List<DataPointVO> allPoints = new DataPointDao().getDataPoints(user.getCurrentScope().getId(),DataPointExtendedNameComparator.instance, false);
        List<DataPointVO> userPoints = new LinkedList<DataPointVO>();
        int pointIndex = -1;
        for (DataPointVO dp : allPoints) {
            if (Permissions.hasDataPointReadPermission(user, dp)) {
                userPoints.add(dp);
                if (dp.getId() == pointId)
                    pointIndex = userPoints.size() - 1;
            }
        }
        model.put("userPoints", userPoints);

        // Determine next and previous ids
        if (pointIndex > 0)
            model.put("prevId", userPoints.get(pointIndex - 1).getId());
        if (pointIndex < userPoints.size() - 1)
            model.put("nextId", userPoints.get(pointIndex + 1).getId());
    }
    
    public static int getPageCount(int totalRecord){
		int pageSize  = 10;
		int pagecount =0;
		if (totalRecord  >0) {
			pagecount = (totalRecord + pageSize -1)/pageSize  ;
		}
		return pagecount;
	}
}
