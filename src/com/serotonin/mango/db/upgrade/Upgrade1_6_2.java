/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.db.upgrade;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.serotonin.mango.db.DatabaseAccess;

/**
 *  
 */
public class Upgrade1_6_2 extends DBUpgrade {
    private final Log log = LogFactory.getLog(getClass());

    @Override
    public void upgrade() throws Exception {
        OutputStream out = createUpdateLogOutputStream("1_6_2");

        log.info("Running script 1");
        runScript(script1, out);

        Map<String, String[]> script2 = new HashMap<String, String[]>();
        script2.put(DatabaseAccess.DatabaseType.DERBY.name(), derbyScript2);
        script2.put(DatabaseAccess.DatabaseType.MYSQL.name(), mysqlScript2);
        runScript(script2, out);

        out.flush();
        out.close();
    }

    @Override
    protected String getNewSchemaVersion() {
        return "1.6.3";
    }

    private static String[] script1 = { "alter table pointEventDetectors add column weight double;", };

    private static String[] derbyScript2 = {};

    private static String[] mysqlScript2 = { "alter table dataSources modify data longblob not null;",
            "alter table dataPoints modify data longblob not null;",
            "alter table mangoViews modify data longblob not null;",
            "alter table eventHandlers modify data longblob not null;",
            "alter table reports modify data longblob not null;",
            "alter table reportInstancePoints modify textRenderer longblob;",
            "alter table publishers modify data longblob not null;",

            "alter table systemSettings modify settingValue longtext;",
            "alter table pointValueAnnotations modify textPointValueLong longtext;",
            "alter table events modify message longtext;",
            "alter table reportInstanceDataAnnotations modify textPointValueLong longtext;",
            "alter table reportInstanceEvents modify message longtext;", };
}
