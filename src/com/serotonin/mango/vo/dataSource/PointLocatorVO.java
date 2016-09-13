/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.vo.dataSource;

import java.io.Serializable;

import com.serotonin.mango.rt.dataSource.PointLocatorRT;
import com.serotonin.mango.util.ChangeComparableObject;
import com.serotonin.web.dwr.DwrResponseI18n;
import com.serotonin.web.i18n.LocalizableMessage;

public interface PointLocatorVO extends Serializable, ChangeComparableObject {
    /**
     * One of the com.serotonin.mango.DataTypes
     */
    public int getDataTypeId();

    /**
     * The text representation of the data type
     */
    public LocalizableMessage getDataTypeMessage();

    /**
     * An arbitrary description of the point location configuration for human consumption.
     */
    public LocalizableMessage getConfigurationDescription();

    /**
     * Can the value be set in the data source?
     */
    public boolean isSettable();

    /**
     * Supplemental to being settable, can the set value be relinquished?
     */
    public boolean isRelinquishable();

    /**
     * Create a runtime version of the locator
     */
    public PointLocatorRT createRuntime();

    /**
     * Validate. What else?
     */
    public void validate(DwrResponseI18n response);

    public DataPointSaveHandler getDataPointSaveHandler();
}
