/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.mvc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import com.serotonin.db.IntValuePair;
import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.ViewDao;
import com.serotonin.mango.view.ShareUser;
import com.serotonin.mango.view.View;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.vo.permission.Permissions;

public class ViewsController extends ParameterizableViewController {
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        ViewDao viewDao = new ViewDao();
        User user = Common.getUser(request);

        List<IntValuePair> views = viewDao.getViewNamesByFactoryId(user.getCurrentScope().getId());
        model.put("views", views);

        // Set the current view.
        View currentView = null;
        String vid = request.getParameter("viewId");
        try {
            currentView = viewDao.getView(Integer.parseInt(vid));
        }
        catch (NumberFormatException e) {
            // no op
        }

        if (currentView == null && views.size() > 0)
            currentView = viewDao.getView(views.get(0).getKey());

        if (currentView != null) {
            //Permissions.ensureViewPermission(user, currentView);

            // Make sure the owner still has permission to all of the points in the view, and that components are
            // otherwise valid.
            currentView.validateViewComponents(false);

            // Add the view to the session for the dwr access stuff.
            model.put("currentView", currentView);
            model.put("owner", currentView.getUserAccess(user) == ShareUser.ACCESS_OWNER);
            user.setView(currentView);
        }

        return new ModelAndView(getViewName(), model);
    }
}
