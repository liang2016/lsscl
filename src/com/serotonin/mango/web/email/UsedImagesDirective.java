/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.email;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

/**
 *  
 */
public class UsedImagesDirective implements TemplateDirectiveModel {
    private final List<String> imageList = new ArrayList<String>();

    public List<String> getImageList() {
        return imageList;
    }

    @Override
    public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) throws TemplateException, IOException {
        TemplateModel src = (TemplateModel) params.get("src");

        if (src instanceof TemplateScalarModel) {
            String s = "images/" + ((TemplateScalarModel) src).getAsString();
            if (!imageList.contains(s))
                imageList.add(s);
            env.getOut().write(s);
        }
        else
            throw new TemplateModelException("key must be a string");
    }
}
