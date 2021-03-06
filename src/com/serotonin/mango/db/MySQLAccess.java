/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.db;

import java.sql.SQLException;

import javax.servlet.ServletContext;

import org.springframework.dao.DataAccessException;

import com.serotonin.db.spring.ExtendedJdbcTemplate;
import com.serotonin.mango.Common;

public class MySQLAccess extends BasePooledAccess {
    public MySQLAccess(ServletContext ctx) {
        super(ctx);
    }

    @Override
    protected void initializeImpl(String propertyPrefix) {
        super.initializeImpl(propertyPrefix);
        dataSource.setInitialSize(3);
        dataSource.setMaxWait(-1);
        dataSource.setTestWhileIdle(true);
        dataSource.setTimeBetweenEvictionRunsMillis(10000);
        dataSource.setMinEvictableIdleTimeMillis(60000);
    }

    @Override
    protected String getUrl(String propertyPrefix) {
        String url = super.getUrl(propertyPrefix);
        if (url.indexOf('?') > 0)
            url += "&";
        else
            url += "?";
        url += "useUnicode=yes&characterEncoding=" + Common.UTF8;
        return url;
    }

    @Override
    public DatabaseType getType() {
        return DatabaseType.MYSQL;
    }

    @Override
    protected String getDriverClassName() {
        return "com.mysql.jdbc.Driver";
    }

    @Override
    protected boolean newDatabaseCheck(ExtendedJdbcTemplate ejt) {
        try {
            ejt.execute("select count(*) from users");
        }
        catch (DataAccessException e) {
            if (e.getCause() instanceof SQLException) {
                SQLException se = (SQLException) e.getCause();
                if ("42S02".equals(se.getSQLState())) {
                    // This state means a missing table. Assume that the schema needs to be created.
                    createSchema("/WEB-INF/db/createTables-mysql.sql");
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
