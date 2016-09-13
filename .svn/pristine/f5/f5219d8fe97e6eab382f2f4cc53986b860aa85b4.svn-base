/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.email;

import java.io.IOException;
import java.util.Map;
import java.util.ResourceBundle;

import com.serotonin.web.i18n.I18NUtils;
import com.serotonin.web.i18n.LocalizableMessage;

import freemarker.core.Environment;
import freemarker.ext.beans.BeanModel;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.TemplateScalarModel;

/**
 *  
 */
public class MessageFormatDirective implements TemplateDirectiveModel {
    private final ResourceBundle bundle;

    public MessageFormatDirective(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public void execute(Environment env, @SuppressWarnings("rawtypes") Map params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) throws TemplateException, IOException {
        TemplateModel key = (TemplateModel) params.get("key");

        String out;
        if (key == null) {
            // No key. Look for a message.
            BeanModel model = (BeanModel) params.get("message");
            if (model == null) {
                if (params.containsKey("message"))
                    // The parameter is there, but the value is null.
                    out = "";
                else
                    // The parameter wasn't given
                    throw new TemplateModelException("One of key or message must be provided");
            }
            else {
                LocalizableMessage message = (LocalizableMessage) model.getWrappedObject();
                if (message == null)
                    out = "";
                else
                    out = message.getLocalizedMessage(bundle);
            }
        }
        else {
            if (key instanceof TemplateScalarModel)
                out = I18NUtils.getMessage(bundle, ((TemplateScalarModel) key).getAsString());
            else
                throw new TemplateModelException("key must be a string");
        }

        env.getOut().write(out);
    }
}
