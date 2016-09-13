/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataImage;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import com.serotonin.mango.rt.dataImage.types.MangoValue;
import com.serotonin.web.i18n.I18NUtils;

/**
 * This class provides a way of arbitrarily annotating a PointValue. Point value annotations should not be confused with
 * Java annotations. A point value annotation will typically explain the source of the value when it did not simply come
 * from data source.
 * 
 * @see SetPointSource
 *  
 */
public class AnnotatedPointValueTime extends PointValueTime {
    private static final long serialVersionUID = -1;

    /**
     * The type of source that created the annotation.
     * 
     * @see SetPointSource
     */
    private final int sourceType;

    /**
     * The id of the source that created the annotation.
     * 
     * @see SetPointSource
     */
    private final int sourceId;

    /**
     * An arbitrary description of the source, human readable. This depends on the source type, but will typically be
     * the source's name. For example, for a user source it would be the username.
     * 
     * @see SetPointSource
     */
    private String sourceDescriptionArgument;

    public AnnotatedPointValueTime(MangoValue value, long time, int sourceType, int sourceId) {
        super(value, time);
        this.sourceType = sourceType;
        this.sourceId = sourceId;
    }

    @Override
    public boolean isAnnotated() {
        return true;
    }

    public int getSourceId() {
        return sourceId;
    }

    public int getSourceType() {
        return sourceType;
    }

    public String getSourceDescriptionKey() {
        switch (sourceType) {
        case SetPointSource.Types.ANONYMOUS:
            return "annotation.anonymous";
        case SetPointSource.Types.EVENT_HANDLER:
            return "annotation.eventHandler";
        case SetPointSource.Types.USER:
            return "annotation.user";
        case SetPointSource.Types.POINT_LINK:
            return "annotation.pointLink";
        }
        return null;
    }

    public String getSourceDescriptionArgument() {
        return sourceDescriptionArgument;
    }

    public void setSourceDescriptionArgument(String sourceDescriptionArgument) {
        this.sourceDescriptionArgument = sourceDescriptionArgument;
    }

    public String getAnnotation(ResourceBundle bundle) {
        String pattern = I18NUtils.getMessage(bundle, getSourceDescriptionKey());
        if (sourceDescriptionArgument == null)
            return MessageFormat.format(pattern, I18NUtils.getMessage(bundle, "common.deleted"));
        return MessageFormat.format(pattern, sourceDescriptionArgument);
    }
}
