/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.util;

import java.util.List;

import com.serotonin.web.i18n.LocalizableMessage;

/**
 *  
 */
public interface ChangeComparable<T> {
    int getId();

    String getTypeKey();

    void addProperties(List<LocalizableMessage> list);

    void addPropertyChanges(List<LocalizableMessage> list, T from);
}
