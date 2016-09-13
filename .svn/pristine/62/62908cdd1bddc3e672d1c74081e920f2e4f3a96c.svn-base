/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.mvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.view.RedirectView;

import com.serotonin.mango.Common;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.web.integration.CrowdUtils;
import javax.servlet.ServletContext;
public class LogoutController extends AbstractController {
    private String redirectUrl;

	public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) {
        // Check if the user is logged in.
        User user = Common.getUser(request);
        if (user != null) {
        	this.redirectUrl=user.getLoginUrl();
        	ServletContext context = request.getSession().getServletContext();
        	context.removeAttribute(user.getUsername());
            // The user is in fact logged in. Invalidate the session.
            request.getSession().invalidate();

            if (CrowdUtils.isCrowdEnabled())
                CrowdUtils.logout(request, response);
        }

        // Regardless of what happened above, forward to the configured view.
        return new ModelAndView(new RedirectView(redirectUrl));
    }
}
