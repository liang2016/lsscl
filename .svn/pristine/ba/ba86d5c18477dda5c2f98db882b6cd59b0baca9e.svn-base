/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.db;

import java.sql.SQLException;

import javax.servlet.ServletContext;

import org.springframework.dao.DataAccessException;

import com.serotonin.db.spring.ExtendedJdbcTemplate;

public class MSSQLAccess extends BasePooledAccess {
    public MSSQLAccess(ServletContext ctx) {
        super(ctx);
    }

    @Override
    public DatabaseType getType() {
        return DatabaseType.MSSQL;
    }

    @Override
    protected String getDriverClassName() {
        return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    }

    @Override
    protected boolean newDatabaseCheck(ExtendedJdbcTemplate ejt) {
        try {
            ejt.execute("select count(*) from users");
        }
        catch (DataAccessException e) {
            if (e.getCause() instanceof SQLException) {
                SQLException se = (SQLException) e.getCause();
                if ("S0002".equals(se.getSQLState())) {
                    // This state means a missing table. Assume that the schema needs to be created.
                    createSchema("/WEB-INF/db/createTables-mssql.sql");
                    return true;
                }
            }
            throw e;
        }
        return false;
    }

    @Override
    public double applyBounds(double value) {
        if (Double.isNaN(value))
            return 0;
        if (value == Double.POSITIVE_INFINITY)
            return Double.MAX_VALUE;
        if (value == Double.NEGATIVE_INFINITY)
            return -Double.MAX_VALUE;

        return value;
    }

    @Override
    public void executeCompress(ExtendedJdbcTemplate ejt) {
        // no op
    }
}
