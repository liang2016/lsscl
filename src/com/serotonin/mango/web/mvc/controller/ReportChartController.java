/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.mvc.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import com.serotonin.mango.vo.DataPointVO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.AbstractController;

import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.ReportDao;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.vo.permission.Permissions;
import com.serotonin.mango.vo.report.ReportChartCreator;
import com.serotonin.mango.vo.report.ReportChartCreator.PointStatistics;
import com.serotonin.mango.vo.report.ReportInstance;

/**
 *  
 */
public class ReportChartController extends AbstractController {
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        int instanceId = Integer.parseInt(request.getParameter("instanceId"));
        ReportDao reportDao = new ReportDao();
        ReportInstance instance = reportDao.getReportInstance(instanceId);

        User user = Common.getUser(request);
        Permissions.ensureReportInstancePermission(user, instance);

        ReportChartCreator creator = new ReportChartCreator(ControllerUtils.getResourceBundle(request));
        creator.createContent(instance, reportDao, null, false);

        Map<String, byte[]> imageData = new HashMap<String, byte[]>();
        imageData.put(creator.getChartName(), creator.getImageData());
        for (PointStatistics pointStatistics : creator.getPointStatistics())
            imageData.put(pointStatistics.getChartName(), pointStatistics.getImageData());
        user.setReportImageData(imageData);

        return new ModelAndView(new ReportChartView(creator.getHtml()));
    }

    static class ReportChartView implements View {
        private final String content;

        public ReportChartView(String content) {
            this.content = content;
        }

        public String getContentType() {
            return null;
        }

        public void render(Map model, HttpServletRequest request, HttpServletResponse response) throws Exception {
            response.getWriter().write(content);
        }
    }
}
