/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.event;

import java.util.concurrent.CopyOnWriteArraySet;

import com.serotonin.util.ILifecycle;

/**
 *  
 */
abstract public class SimpleEventDetector implements EventDetector, ILifecycle {
    private final CopyOnWriteArraySet<EventDetectorListener> listeners = new CopyOnWriteArraySet<EventDetectorListener>();

    public void addListener(EventDetectorListener l) {
        listeners.add(l);
    }

    public void removeListener(EventDetectorListener l) {
        listeners.remove(l);
    }

    protected void fireEventDetectorStateChanged(long time) {
        for (EventDetectorListener l : listeners)
            l.eventDetectorStateChanged(time);
    }

    protected void fireEventDetectorTerminated() {
        for (EventDetectorListener l : listeners)
            l.eventDetectorTerminated(this);
    }

    public boolean hasListeners() {
        return listeners.size() > 0;
    }

    public void terminate() {
        // no op
    }
}
