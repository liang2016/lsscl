/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.serotonin.InvalidArgumentException;
import com.serotonin.util.ColorUtils;
import com.serotonin.util.StringUtils;

/**
 *  
 */
public class ChartPointTag extends TagSupport {
    private static final long serialVersionUID = -1;

    private String xid;
    private String color;

    public void setXid(String xid) {
        this.xid = xid;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public int doStartTag() throws JspException {
        ChartTag chartTag = (ChartTag) findAncestorWithClass(this, ChartTag.class);
        if (chartTag == null)
            throw new JspException("chartPoint tags must be used within a chart tag");

        // Validate the colour.
        try {
            if (!StringUtils.isEmpty(color))
                ColorUtils.toColor(color);
        }
        catch (InvalidArgumentException e) {
            throw new JspException("Invalid color '" + color + "'");
        }

        chartTag.addChartPoint(xid, color);

        return EVAL_BODY_INCLUDE;
    }

    @Override
    public void release() {
        super.release();
        xid = null;
        color = null;
    }
}
