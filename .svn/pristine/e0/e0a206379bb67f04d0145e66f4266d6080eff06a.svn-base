/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.db.upgrade;

import java.io.OutputStream;

/**
 *  
 */
public class Upgrade1_11_1 extends DBUpgrade {
    @Override
    public void upgrade() throws Exception {
        OutputStream out = createUpdateLogOutputStream("1_11_1");

        // Run the script.
        runScript(script1, out);

        out.flush();
        out.close();
    }

    @Override
    protected String getNewSchemaVersion() {
        return "1.12.0";
    }

    private static String[] script1 = { //
            "alter table reportInstanceEvents add column alternateAckSource int;", //

            "alter table mangoViewUsers drop foreign key mangoViewUsersFk1;", //
            "alter table mangoViewUsers add constraint mangoViewUsersFk1 foreign key (mangoViewId) references mangoViews(id);", //

            "alter table watchListUsers drop foreign key watchListUsersFk1;", //
            "alter table watchListUsers add constraint watchListUsersFk1 foreign key (watchListId) references watchLists(id);", //
    };
}