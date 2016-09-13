/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.view.custom;

import java.util.ArrayList;
import java.util.List;

import com.serotonin.mango.db.dao.DataPointDao;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.vo.permission.Permissions;

/**
 *  
 */
public class CustomView {
    private final User authorityUser;
    private final List<CustomViewComponent> components = new ArrayList<CustomViewComponent>();
    private final List<DataPointVO> pointCache = new ArrayList<DataPointVO>();

    public CustomView(User authorityUser) {
        this.authorityUser = authorityUser;
    }

    public User getAuthorityUser() {
        return authorityUser;
    }

    public int addPoint(DataPointVO dataPointVO, boolean raw, String disabledValue, boolean time) {
        CustomViewPoint point = new CustomViewPoint(components.size(), dataPointVO, raw, disabledValue, time);
        components.add(point);
        return point.getId();
    }

    public int addChart(long duration, int width, int height, List<CustomViewChartPoint> points) {
        CustomViewChart chart = new CustomViewChart(duration, components.size(), width, height, points);
        components.add(chart);
        return chart.getId();
    }

    public List<CustomViewComponent> getComponents() {
        return components;
    }

    synchronized public DataPointVO getPoint(String xid) {
        for (DataPointVO dp : pointCache) {
            if (dp.getXid().equals(xid))
                return dp;
        }

        DataPointVO dp = new DataPointDao().getDataPoint(xid);
        if (dp != null) {
            // Check permissions.
            Permissions.ensureDataPointSetPermission(authorityUser, dp);

            pointCache.add(dp);
        }
        return dp;
    }
}
