/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.publish.pachube;

import java.util.Iterator;

import com.serotonin.mango.rt.dataImage.PointValueTime;
import com.serotonin.mango.rt.publish.PublishQueue;
import com.serotonin.mango.rt.publish.PublishQueueEntry;
import com.serotonin.mango.rt.publish.PublisherRT;
import com.serotonin.mango.vo.publish.pachube.PachubePointVO;

public class PachubePublishQueue extends PublishQueue<PachubePointVO> {
    public PachubePublishQueue(PublisherRT<PachubePointVO> owner, int warningSize) {
        super(owner, warningSize);
    }

    @Override
    public synchronized void add(PachubePointVO vo, PointValueTime pvt) {
        // Remove duplicate points.
        Iterator<PublishQueueEntry<PachubePointVO>> iter = queue.iterator();
        while (iter.hasNext()) {
            PachubePointVO entry = iter.next().getVo();
            if (entry.getFeedId() == vo.getFeedId() && entry.getDataStreamId() == vo.getDataStreamId())
                iter.remove();
        }

        super.add(vo, pvt);
    }
}
