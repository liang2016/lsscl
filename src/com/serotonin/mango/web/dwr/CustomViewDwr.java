/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.dwr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.WebContextFactory;

import com.serotonin.mango.Common;
import com.serotonin.mango.rt.RuntimeManager;
import com.serotonin.mango.view.custom.CustomView;
import com.serotonin.mango.view.custom.CustomViewComponent;
import com.serotonin.mango.web.dwr.beans.CustomComponentState;

/**
 *  
 */
public class CustomViewDwr extends BaseDwr {
    public List<CustomComponentState> getViewPointData() {
        CustomView view = Common.getCustomView();
        if (view == null)
            return Collections.emptyList();

        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        List<CustomComponentState> states = new ArrayList<CustomComponentState>();
        RuntimeManager rtm = Common.ctx.getRuntimeManager();

        for (CustomViewComponent comp : view.getComponents())
            states.add(comp.createState(rtm, request));

        return states;
    }

    public void setCustomPoint(String xid, String valueStr) {
        CustomView view = Common.getCustomView();
        setPointImpl(view.getPoint(xid), valueStr, view.getAuthorityUser());
    }
}
