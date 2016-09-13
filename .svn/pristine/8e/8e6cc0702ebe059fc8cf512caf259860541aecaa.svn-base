/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.event.handlers;

import com.serotonin.mango.rt.event.EventInstance;
import com.serotonin.mango.rt.maint.work.ProcessWorkItem;
import com.serotonin.mango.vo.event.EventHandlerVO;
import com.serotonin.util.StringUtils;

/**
 *  
 */
public class ProcessHandlerRT extends EventHandlerRT {
    public ProcessHandlerRT(EventHandlerVO vo) {
        this.vo = vo;
    }

    @Override
    public void eventRaised(EventInstance evt) {
        executeCommand(vo.getActiveProcessCommand());
    }

    @Override
    public void eventInactive(EventInstance evt) {
        executeCommand(vo.getInactiveProcessCommand());
    }

    private void executeCommand(String command) {
        if (StringUtils.isEmpty(command))
            return;
        ProcessWorkItem.queueProcess(command);
    }
}
