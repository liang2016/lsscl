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
public class Upgrade1_3_0 extends DBUpgrade {
    private final Log log = LogFactory.getLog(getClass());

    @Override
    public void upgrade() throws Exception {
        OutputStream out = createUpdateLogOutputStream("1_3_0");

        // Run the script.
        log.info("Running script");
        runScript(script, out);

        out.flush();
        out.close();
    }

    @Override
    protected String getNewSchemaVersion() {
        return "1.4.0";
    }

    private static String[] script = {
            "create table mangoViewUsers (",
            "  mangoViewId int not null,",
            "  userId int not null,",
            "  accessType int not null",
            ");",
            "alter table mangoViewUsers add constraint mangoViewUsersPk primary key (mangoViewId, userId);",
            "alter table mangoViewUsers add constraint mangoViewUsersFk1 foreign key (mangoViewId) references mangoViews(id);",
            "alter table mangoViewUsers add constraint mangoViewUsersFk1 foreign key (userId) references users(id);",

            "alter table events add messageOld varchar(1024);", "update events set messageOld=message;",
            "alter table events drop message;", "alter table events add message clob;",
            "update events set message=messageOld;", "alter table events drop messageOld;", };
}
