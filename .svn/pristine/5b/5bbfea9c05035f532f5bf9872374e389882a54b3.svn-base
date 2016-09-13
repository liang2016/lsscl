/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.taglib;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.JspException;

import com.serotonin.mango.Common;
import com.serotonin.mango.view.custom.CustomView;
import com.serotonin.mango.view.custom.CustomViewChartPoint;
import com.serotonin.mango.vo.DataPointVO;

/**
 *  
 */
public class ChartTag extends ViewTagSupport {
    private static final long serialVersionUID = -1;

    private int duration;
    private String durationType;
    private int width;
    private int height;
    private List<CustomViewChartPoint> points;
    private CustomView view;

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setDurationType(String durationType) {
        this.durationType = durationType;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public int doStartTag() throws JspException {
        points = new ArrayList<CustomViewChartPoint>();

        // Find the custom view.
        view = getCustomView();

        return EVAL_BODY_INCLUDE;
    }

    void addChartPoint(String xid, String color) throws JspException {
        DataPointVO dataPointVO = getDataPointVO(view, xid);
        points.add(new CustomViewChartPoint(dataPointVO, color));
    }

    @Override
    public int doEndTag() throws JspException {
        int periodType = Common.TIME_PERIOD_CODES.getId(durationType.toUpperCase());
        if (periodType == -1)
            throw new JspException("Invalid durationType. Must be one of " + Common.TIME_PERIOD_CODES.getCodeList());
        long millis = Common.getMillis(periodType, duration);

        // Add the chart to the view
        int id = view.addChart(millis, width, height, points);

        // Add the id for the point to the page context.
        pageContext.setAttribute("componentId", id);

        return EVAL_PAGE;
    }

    @Override
    public void release() {
        super.release();
        duration = 0;
        durationType = null;
        width = 0;
        height = 0;
        view = null;
        points = null;
    }
}
