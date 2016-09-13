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
public class Upgrade1_6_3 extends DBUpgrade {
    private final Log log = LogFactory.getLog(getClass());

    @Override
    public void upgrade() throws Exception {
        OutputStream out = createUpdateLogOutputStream("1_6_3");

        log.info("Running script 1");
        runScript(script1, out);

        log.info("Running script 2");
        Map<String, String[]> script2 = new HashMap<String, String[]>();
        script2.put(DatabaseAccess.DatabaseType.DERBY.name(), derbyScript2);
        script2.put(DatabaseAccess.DatabaseType.MYSQL.name(), mysqlScript2);
        runScript(script2, out);

        out.flush();
        out.close();
    }

    @Override
    protected String getNewSchemaVersion() {
        return "1.6.4";
    }

    private static String[] script1 = { "alter table users add column receiveAlarmEmails int;",
            "update users set receiveAlarmEmails=0;", };

    private static String[] derbyScript2 = { "alter table users alter receiveAlarmEmails not null;", };

    private static String[] mysqlScript2 = { "alter table users modify receiveAlarmEmails int not null;", };
}
