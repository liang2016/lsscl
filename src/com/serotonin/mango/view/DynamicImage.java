/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.view;

public class DynamicImage extends ViewGraphic {
    private final String imageFilename;

    DynamicImage(String id, String name, String imageFilename, int width, int height, int textX, int textY) {
        super(id, name, width, height, textX, textY);
        this.imageFilename = imageFilename;
    }

    @Override
    public boolean isDynamicImage() {
        return true;
    }

    public String getImageFilename() {
        return imageFilename;
    }
}
