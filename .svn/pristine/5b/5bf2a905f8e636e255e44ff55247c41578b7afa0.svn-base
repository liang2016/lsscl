/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.view.custom;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.serotonin.mango.rt.RuntimeManager;
import com.serotonin.mango.rt.dataImage.DataPointRT;
import com.serotonin.mango.rt.dataImage.PointValueTime;
import com.serotonin.mango.web.dwr.beans.CustomComponentState;
import com.serotonin.util.StringUtils;

/**
 *  
 */
public class CustomViewChart extends CustomViewComponent {
    private final long duration;
    private final int width;
    private final int height;
    private final List<CustomViewChartPoint> points;

    public CustomViewChart(long duration, int id, int width, int height, List<CustomViewChartPoint> points) {
        super(id);
        this.duration = duration;
        this.width = width;
        this.height = height;
        this.points = points;
    }

    @Override
    protected void createStateImpl(RuntimeManager rtm, HttpServletRequest request, CustomComponentState state) {
        long maxTs = 0;
        for (CustomViewChartPoint point : points) {
            DataPointRT dataPointRT = rtm.getDataPoint(point.getDataPointVO().getId());
            if (dataPointRT != null) {
                PointValueTime pvt = dataPointRT.getPointValue();
                if (pvt != null && maxTs < pvt.getTime())
                    maxTs = pvt.getTime();
            }
        }

        StringBuilder htmlData = new StringBuilder();
        htmlData.append("chart/");
        htmlData.append(maxTs);
        htmlData.append('_');
        htmlData.append(duration);

        for (CustomViewChartPoint point : points) {
            htmlData.append('_');
            htmlData.append(point.getDataPointVO().getId());
            if (!StringUtils.isEmpty(point.getColor()))
                htmlData.append('|').append(point.getColor().replaceAll("#", "0x"));
        }

        htmlData.append(".png");

        htmlData.append("?w=");
        htmlData.append(width);
        htmlData.append("&h=");
        htmlData.append(height);

        state.setValue(htmlData.toString());
    }
}
