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
public class Upgrade1_6_1 extends DBUpgrade {
    private final Log log = LogFactory.getLog(getClass());

    @Override
    public void upgrade() throws Exception {
        OutputStream out = createUpdateLogOutputStream("1_6_1");

        log.info("Running script 1");
        runScript(script1, out);

        out.flush();
        out.close();
    }

    @Override
    protected String getNewSchemaVersion() {
        return "1.6.2";
    }

    private static String[] script1 = {
            "alter table pointValues drop foreign key pointValuesFk1;",
            "alter table pointValues add constraint pointValuesFk1 foreign key (dataPointId) references dataPoints(id) on delete cascade;",

            "alter table mangoViews drop foreign key mangoViewsFk1;",
            "alter table mangoViews add constraint mangoViewsFk1 foreign key (userId) references users(id) on delete cascade;",
            "alter table mangoViewUsers drop foreign key mangoViewUsersFk1;",
            "alter table mangoViewUsers add constraint mangoViewUsersFk1 foreign key (mangoViewId) references mangoViews(id) on delete cascade;" };
}
