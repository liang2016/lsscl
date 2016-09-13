/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.publish;

import com.serotonin.mango.rt.dataImage.PointValueTime;
import com.serotonin.mango.vo.publish.PublishedPointVO;

/**
 *  
 */
public class PublishQueueEntry<T extends PublishedPointVO> {
    private final T vo;
    private final PointValueTime pvt;

    public PublishQueueEntry(T vo, PointValueTime pvt) {
        this.vo = vo;
        this.pvt = pvt;
    }

    public T getVo() {
        return vo;
    }

    public PointValueTime getPvt() {
        return pvt;
    }
}
