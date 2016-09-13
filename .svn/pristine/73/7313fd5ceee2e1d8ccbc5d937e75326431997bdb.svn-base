/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.dwr.util;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.directwebremoting.AjaxFilter;
import org.directwebremoting.AjaxFilterChain;

import com.serotonin.mango.vo.permission.Permissions;

/**
 *  
 */
public class LoggedInAjaxFilter implements AjaxFilter {
    private static final Log LOG = LogFactory.getLog(LoggedInAjaxFilter.class);

    public Object doFilter(Object obj, Method method, Object[] params, AjaxFilterChain chain) throws Exception {
        LOG.debug("Running LoggedInAjaxFilter, hash=" + hashCode());
        Permissions.ensureValidUser();
        return chain.doFilter(obj, method, params);
    }
}
