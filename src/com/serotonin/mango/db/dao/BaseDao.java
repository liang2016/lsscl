/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.db.dao;

import java.util.List;

import javax.sql.DataSource;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.serotonin.db.DaoUtils;
import com.serotonin.mango.Common;

public class BaseDao extends DaoUtils {
	public static SQLServerDataSource ds = new SQLServerDataSource();
	static{
		String host = "114.55.31.174";
		ds.setURL("jdbc:sqlserver://"+host+":1433; DatabaseName=LssclDB");
		ds.setUser("sa");
		ds.setPassword("Asus_admin1");
	}
    /**
     * Public constructor for code that needs to get stuff from the database.
     */
    public BaseDao() {
		super(ds);
		//super(Common.ctx.getDatabaseAccess().getDataSource());
    }

    protected BaseDao(DataSource dataSource) {
        super(dataSource);
    }

    //
    // Convenience methods for storage of booleans.
    //
    protected static String boolToChar(boolean b) {
        return b ? "Y" : "N";
    }

    protected static boolean charToBool(String s) {
        return "Y".equals(s);
    }

    protected void deleteInChunks(String sql, List<Integer> ids) {
        int chunk = 1000;
        for (int i = 0; i < ids.size(); i += chunk) {
            String idStr = createDelimitedList(ids, i, i + chunk, ",", null);
            ejt.update(sql + " (" + idStr + ")");
        }
    }

    //
    // XID convenience methods
    //
    protected String generateUniqueXid(String prefix, String tableName) {
        String xid = Common.generateXid(prefix);
        while (!isXidUnique(xid, -1, tableName))
            xid = Common.generateXid(prefix);
        return xid;
    }

    protected boolean isXidUnique(String xid, int excludeId, String tableName) {
        return ejt.queryForInt("select count(*) from " + tableName + " where xid=? and id<>?", new Object[] { xid,
                excludeId }) == 0;
    }
}
