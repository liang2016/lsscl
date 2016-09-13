/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.view.graphic;

import java.io.Serializable;

import com.serotonin.mango.view.ImplDefinition;

@Deprecated
// Use ViewComponent instead
public interface GraphicRenderer extends Serializable {
    public static final int TYPE_BASIC = 1;
    public static final int TYPE_BINARY_IMAGE_SET = 2;
    public static final int TYPE_ANALOG_IMAGE_SET = 3;

    public String getTypeName();

    public ImplDefinition getDef();
}
