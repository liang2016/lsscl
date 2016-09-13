/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.serotonin.mango.Common;
import com.serotonin.util.XmlUtils;

/**
 *  
 */
public class DocumentationManifest {
    private final List<DocumentationItem> items = new ArrayList<DocumentationItem>();

    public DocumentationManifest() throws Exception {
        // Read the documentation manifest file.
        XmlUtils utils = new XmlUtils();

        Document document = utils.parse(new File(Common.getDocPath() + "manifest.xml"));

        Element root = document.getDocumentElement();
        for (Element item : utils.getElementsByTagName(root, "item")) {
            DocumentationItem di = new DocumentationItem(item.getAttribute("id"));

            for (Element relation : utils.getElementsByTagName(item, "relation"))
                di.addRelated(relation.getAttribute("id"));

            items.add(di);
        }
    }

    public DocumentationItem getItem(String id) {
        for (DocumentationItem di : items) {
            if (id.equals(di.getId()))
                return di;
        }
        return null;
    }
}
