/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.mvc.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import com.serotonin.mango.db.dao.DataPointDao;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.vo.dataSource.http.HttpImagePointLocatorVO;

/**
 *  
 */
public class WebcamLiveFeedController extends ParameterizableViewController {
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        int pointId = Integer.parseInt(request.getParameter("pointId"));
        DataPointDao dataPointDao = new DataPointDao();
        DataPointVO dp = dataPointDao.getDataPoint(pointId);

        if (!(dp.getPointLocator() instanceof HttpImagePointLocatorVO))
            throw new Exception("Point is not an HTTP Image point");

        // User user = Common.getUser(request);
        // Permissions.ensureDataPointReadPermission(user, dp);

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("code", ((HttpImagePointLocatorVO) dp.getPointLocator()).getWebcamLiveFeedCode());

        return new ModelAndView(getViewName(), model);
    }
}
