/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.dwr.beans;

import com.serotonin.ShouldNeverHappenException;
import com.serotonin.util.StringUtils;

/**
 * This class is used by DWR to package up information needed at the browser for the display of the current state of
 * point information.
 * 
 *  
 */
public class ViewComponentState extends BasePointState {
    private String content;
    private String info;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        if (content != null)
            this.content = content.trim();
        else
            this.content = content;
    }

    @Override
    public ViewComponentState clone() {
        try {
            return (ViewComponentState) super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new ShouldNeverHappenException(e);
        }
    }

    public void removeEqualValue(ViewComponentState that) {
        super.removeEqualValue(that);
        if (StringUtils.isEqual(content, that.content))
            content = null;
        if (StringUtils.isEqual(info, that.info))
            info = null;
    }

    @Override
    public boolean isEmpty() {
        return content == null && info == null && super.isEmpty();
    }
}
