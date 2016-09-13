/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.taglib;

import javax.servlet.jsp.JspException;

import com.serotonin.mango.view.custom.CustomView;
import com.serotonin.mango.vo.DataPointVO;

/**
 *  
 */
public class SimplePointTag extends ViewTagSupport {
    private static final long serialVersionUID = -1;

    private String xid;
    private boolean raw;
    private String disabledValue;
    private boolean time;

    public void setXid(String xid) {
        this.xid = xid;
    }

    public void setRaw(boolean raw) {
        this.raw = raw;
    }

    public void setDisabledValue(String disabledValue) {
        this.disabledValue = disabledValue;
    }

    public void setTime(boolean time) {
        this.time = time;
    }

    @Override
    public int doStartTag() throws JspException {
        // Find the custom view.
        CustomView view = getCustomView();

        // Find the point.
        DataPointVO dataPointVO = getDataPointVO(view, xid);

        // Add the point to the view
        int id = view.addPoint(dataPointVO, raw, disabledValue, time);

        // Add the id for the point to the page context.
        pageContext.setAttribute("componentId", id);

        return EVAL_BODY_INCLUDE;
    }

    @Override
    public void release() {
        super.release();
        xid = null;
        raw = false;
        disabledValue = null;
        time = false;
    }
}
