/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.mvc.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.SystemSettingsDao;
import com.serotonin.mango.web.mvc.controller.ControllerUtils;

/**
 *  
 */
public class CommonDataInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute("availableLanguages", Common.getLanguages());
        request.setAttribute("lang", ControllerUtils.getLocale(request).getLanguage());
        request.setAttribute("instanceDescription", SystemSettingsDao.getValue(SystemSettingsDao.INSTANCE_DESCRIPTION));
        request.setAttribute("NEW_ID", Common.NEW_ID);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) {
        // no op
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // no op
    }
}
