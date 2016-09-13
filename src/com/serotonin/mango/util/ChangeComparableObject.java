/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.util;

import java.util.List;

import com.serotonin.web.i18n.LocalizableMessage;

/**
 * This interface is meant for comparable objects (for audit purposes) that are members of other comparable objects. It
 * does not use generics so to avoid having to spread generic definitions throughout the entire code base.
 * 
 *  
 */
public interface ChangeComparableObject {
    void addProperties(List<LocalizableMessage> list);

    void addPropertyChanges(List<LocalizableMessage> list, Object o);
}
