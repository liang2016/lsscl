/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.util;

import java.util.ArrayList;
import java.util.List;

/**
 *  
 */
public class DocumentationItem {
    private final String id;
    private final List<String> related = new ArrayList<String>();

    public DocumentationItem(String id) {
        this.id = id;
    }

    public void addRelated(String id) {
        related.add(id);
    }

    public String getId() {
        return id;
    }

    public List<String> getRelated() {
        return related;
    }
}
