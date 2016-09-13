/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.db.upgrade;

import java.io.OutputStream;

/**
 *  
 */
public class Upgrade1_8_0 extends DBUpgrade {
    @Override
    public void upgrade() throws Exception {
        OutputStream out = createUpdateLogOutputStream("1_8_0");

        out.flush();
        out.close();
    }

    @Override
    protected String getNewSchemaVersion() {
        return "1.8.1";
    }
}
