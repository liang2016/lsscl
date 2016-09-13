/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.mvc;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;

public class SimpleFormRedirectController extends SimpleFormController {
    private String successUrl;

    public void setSuccessUrl(String successUrl) {
        this.successUrl = successUrl;
    }

    public ModelAndView getSuccessRedirectView() {
        return getSuccessRedirectView(null);
    }

    public ModelAndView getSuccessRedirectView(String queryString) {
        String url = successUrl;
        if (queryString != null && queryString.trim().length() > 0) {
            if (queryString.charAt(0) != '?')
                url += '?' + queryString;
            else
                url += queryString;
        }
        RedirectView redirectView = new RedirectView(url, true);
        return new ModelAndView(redirectView);
    }

    public boolean hasSubmitParameter(HttpServletRequest request, String name) {
        return WebUtils.hasSubmitParameter(request, name);
    }
}
