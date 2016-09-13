/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.view.component;

import com.serotonin.web.i18n.LocalizableMessage;

/**
 *  
 */
public class CompoundChild {
    private final String id;
    private final LocalizableMessage description;
    private final ViewComponent viewComponent;
    private final int[] dataTypesOverride;

    public CompoundChild(String id, LocalizableMessage description, ViewComponent viewComponent, int[] dataTypesOverride) {
        this.id = id;
        this.description = description;
        this.viewComponent = viewComponent;
        this.dataTypesOverride = dataTypesOverride;
    }

    public String getId() {
        return id;
    }

    public LocalizableMessage getDescription() {
        return description;
    }

    public ViewComponent getViewComponent() {
        return viewComponent;
    }

    public int[] getDataTypes() {
        if (dataTypesOverride != null)
            return dataTypesOverride;
        if (viewComponent.isPointComponent())
            return ((PointComponent) viewComponent).getSupportedDataTypes();
        return null;
    }
}
