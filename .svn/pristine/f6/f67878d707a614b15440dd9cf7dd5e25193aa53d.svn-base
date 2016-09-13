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
public class Upgrade1_1_0 extends DBUpgrade {
    private final Log log = LogFactory.getLog(getClass());

    @Override
    public void upgrade() throws Exception {
        OutputStream out = createUpdateLogOutputStream("1_1_0");

        // Run the script.
        log.info("Running script");
        runScript(script, out);

        out.flush();
        out.close();
    }

    @Override
    protected String getNewSchemaVersion() {
        return "1.1.1";
    }

    private static String[] script = {
            "alter table pointValueAnnotations add column textPointValueShort varchar(128);",
            "alter table pointValueAnnotations add column textPointValueLong clob;",
            "update pointValueAnnotations set textPointValueShort=textPointValue where textPointValue is not null and length(textPointValue) <= 128;",
            "update pointValueAnnotations set textPointValueLong=textPointValue where textPointValue is not null and length(textPointValue) > 128;",
            "alter table pointValueAnnotations drop textPointValue;",

            "alter table reportInstanceDataAnnotations add column textPointValueShort varchar(128);",
            "alter table reportInstanceDataAnnotations add column textPointValueLong clob;",
            "update reportInstanceDataAnnotations set textPointValueShort=textPointValue where textPointValue is not null and length(textPointValue) <= 128;",
            "update reportInstanceDataAnnotations set textPointValueLong=textPointValue where textPointValue is not null and length(textPointValue) > 128;",
            "alter table reportInstanceDataAnnotations drop textPointValue;", };
}
