/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.db.upgrade;

import java.io.OutputStream;

/**
 *  
 */
public class Upgrade1_12_1 extends DBUpgrade {
    @Override
    public void upgrade() throws Exception {
        OutputStream out = createUpdateLogOutputStream("1_12_1");

        runScript(script, out);

        out.flush();
        out.close();
    }

    @Override
    protected String getNewSchemaVersion() {
        return "1.12.2";
    }

    private static String[] script = { //
    "alter table reportInstancePoints add column consolidatedChart char(1);", //
            "update reportInstancePoints set consolidatedChart='Y';", //
    };
}