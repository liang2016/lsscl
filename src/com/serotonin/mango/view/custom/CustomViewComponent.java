/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.view.custom;

import javax.servlet.http.HttpServletRequest;

import com.serotonin.mango.rt.RuntimeManager;
import com.serotonin.mango.web.dwr.beans.CustomComponentState;

/**
 *  
 */
abstract public class CustomViewComponent {
    private final int id;

    public CustomViewComponent(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public CustomComponentState createState(RuntimeManager rtm, HttpServletRequest request) {
        CustomComponentState state = new CustomComponentState();
        state.setId(id);
        createStateImpl(rtm, request, state);
        return state;
    }

    abstract protected void createStateImpl(RuntimeManager rtm, HttpServletRequest request, CustomComponentState state);
}
