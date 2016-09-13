/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.event.handlers;

import com.serotonin.mango.rt.EventManager;
import com.serotonin.mango.rt.event.EventInstance;
import com.serotonin.mango.vo.event.EventHandlerVO;

abstract public class EventHandlerRT {
    protected EventHandlerVO vo;

    /**
     * Not all events that are raised are made active. It depends on the event's alarm level and duplicate handling.
     * 
     * @see EventManager.raiseEvent for details.
     * @param evt
     */
    abstract public void eventRaised(EventInstance evt);

    /**
     * Called when the event is considered inactive.
     * 
     * @see EventManager.raiseEvent for details.
     * @param evt
     */
    abstract public void eventInactive(EventInstance evt);
}
