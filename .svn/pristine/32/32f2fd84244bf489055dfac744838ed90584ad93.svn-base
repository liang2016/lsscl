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

import com.serotonin.mango.vo.permission.PermissionException;
import com.serotonin.mango.vo.permission.Permissions;
import com.serotonin.web.dwr.MethodFilter;

/**
 *  
 */
public class LoggedInAjaxMethodFilter implements AjaxFilter {
    private static final Log LOG = LogFactory.getLog(LoggedInAjaxFilter.class);

    public Object doFilter(Object obj, Method method, Object[] params, AjaxFilterChain chain) throws Exception {
        LOG.debug("Running LoggedInAjaxFilter, hash=" + hashCode());

        if (method.isAnnotationPresent(MethodFilter.class)) {
            LOG.debug("Method filter found. We should check if the user is logged in");
            try {
                Permissions.ensureValidUser();
            }
            catch (PermissionException e) {
                LOG.error("Permission exception while checking method " + method);
                throw e;
            }
        }

        return chain.doFilter(obj, method, params);
    }
}
