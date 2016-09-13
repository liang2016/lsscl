/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.db.upgrade;

/**
 *  
 */
public class Upgrade1_4_1 extends DBUpgrade {
    @Override
    public void upgrade() {
        // no op
    }

    @Override
    protected String getNewSchemaVersion() {
        return "1.4.2";
    }
}
