/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.db.upgrade;

import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  
 */
public class Upgrade0_9_0 extends DBUpgrade {
    private final Log log = LogFactory.getLog(getClass());

    @Override
    public void upgrade() throws Exception {
        OutputStream out = createUpdateLogOutputStream("0_9_0");

        // Run the script.
        log.info("Running script");
        runScript(script, out);

        out.flush();
        out.close();
    }

    @Override
    protected String getNewSchemaVersion() {
        return "0.9.1";
    }

    private static String[] script = { "create table compoundEventDetectors ( ",
            "  id int not null generated by default as identity (start with 1, increment by 1), ",
            "  name varchar(100), ", "  alarmLevel int not null, ", "  returnToNormal char(1) not null, ",
            "  disabled char(1) not null, ", "  condition varchar(256) not null ", "); ",
            "alter table compoundEventDetectors add constraint compoundEventDetectorsPk primary key (id); ", };
}
