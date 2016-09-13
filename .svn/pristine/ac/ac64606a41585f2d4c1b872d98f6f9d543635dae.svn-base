/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.publish;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.serotonin.mango.rt.dataImage.PointValueTime;
import com.serotonin.mango.vo.publish.PublishedPointVO;

/**
 *  
 */
public class PublishQueue<T extends PublishedPointVO> {
    protected final LinkedList<PublishQueueEntry<T>> queue = new LinkedList<PublishQueueEntry<T>>();
    private final PublisherRT<T> owner;
    private final int warningSize;
    private boolean warningActive = false;

    public PublishQueue(PublisherRT<T> owner, int warningSize) {
        this.owner = owner;
        this.warningSize = warningSize;
    }

    public synchronized void add(T vo, PointValueTime pvt) {
        queue.add(new PublishQueueEntry<T>(vo, pvt));
        sizeCheck();
    }

    public synchronized void add(T vo, List<PointValueTime> pvts) {
        for (PointValueTime pvt : pvts)
            queue.add(new PublishQueueEntry<T>(vo, pvt));
        sizeCheck();
    }

    public synchronized PublishQueueEntry<T> next() {
        if (queue.size() == 0)
            return null;
        return queue.get(0);
    }

    public synchronized List<PublishQueueEntry<T>> get(int max) {
        if (queue.size() == 0)
            return null;

        int amt = max;
        if (amt > queue.size())
            amt = queue.size();

        return new ArrayList<PublishQueueEntry<T>>(queue.subList(0, amt));
    }

    public synchronized void remove(PublishQueueEntry<T> e) {
        queue.remove(e);
        sizeCheck();
    }

    private void sizeCheck() {
        if (warningActive) {
            if (queue.size() <= warningSize) {
                owner.deactivateQueueSizeWarningEvent();
                warningActive = false;
            }
        }
        else {
            if (queue.size() > warningSize) {
                owner.fireQueueSizeWarningEvent();
                warningActive = true;
            }
        }
    }
}
