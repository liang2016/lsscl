/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.dwr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.serotonin.db.IntValuePair;
import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.DataPointDao;
import com.serotonin.mango.db.dao.DataSourceDao;
import com.serotonin.mango.db.dao.SystemSettingsDao;
import com.serotonin.mango.db.dao.UserDao;
import com.serotonin.mango.rt.RuntimeManager;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.vo.dataSource.DataSourceVO;
import com.serotonin.mango.vo.permission.Permissions;
import com.serotonin.web.dwr.DwrResponseI18n;
import com.serotonin.mango.db.dao.acp.ACPDao;

/**
 *  
 */
public class DataSourceListDwr extends BaseDwr {
    public DwrResponseI18n init() {
        DwrResponseI18n response = new DwrResponseI18n();

        if (Common.getUser().isAdmin()||Common.getUser().isTempAdmin()) {
            List<IntValuePair> translatedTypes = new ArrayList<IntValuePair>();
            for (DataSourceVO.Type type : DataSourceVO.Type.values()) {
                // Allow customization settings to overwrite the default display value.
                boolean display = SystemSettingsDao.getBooleanValue(type.name()
                        + SystemSettingsDao.DATASOURCE_DISPLAY_SUFFIX, type.isDisplay());
                if (display){
                    if(type==DataSourceVO.Type.MODBUS_IP||type==DataSourceVO.Type.META)	
                    	translatedTypes.add(new IntValuePair(type.getId(), getMessage(type.getKey())));
                	}
                }
            response.addData("types", translatedTypes);
        }

        return response;
    }

    public Map<String, Object> toggleDataSource(int dataSourceId) {
        Permissions.ensureDataSourcePermission(Common.getUser(), dataSourceId);

        RuntimeManager runtimeManager = Common.ctx.getRuntimeManager();
        DataSourceVO<?> dataSource = runtimeManager.getDataSource(dataSourceId);
        Map<String, Object> result = new HashMap<String, Object>();

        dataSource.setEnabled(!dataSource.isEnabled());
        runtimeManager.saveDataSource(dataSource);

        result.put("enabled", dataSource.isEnabled());
        result.put("id", dataSourceId);
        return result;
    }

    public int deleteDataSource(int dataSourceId) {
        Permissions.ensureDataSourcePermission(Common.getUser(), dataSourceId);
        Common.ctx.getRuntimeManager().deleteDataSource(dataSourceId);
        return dataSourceId;
    }

    public DwrResponseI18n toggleDataPoint(int dataPointId) {
        DataPointVO dataPoint = new DataPointDao().getDataPoint(dataPointId);
        Permissions.ensureDataSourcePermission(Common.getUser(), dataPoint.getDataSourceId());

        RuntimeManager runtimeManager = Common.ctx.getRuntimeManager();
        dataPoint.setEnabled(!dataPoint.isEnabled());
        runtimeManager.saveDataPoint(dataPoint);

        DwrResponseI18n response = new DwrResponseI18n();
        response.addData("id", dataPointId);
        response.addData("enabled", dataPoint.isEnabled());
        return response;
    }

    public int copyDataSource(int dataSourceId) {
        Permissions.ensureDataSourcePermission(Common.getUser(), dataSourceId);
        int factoryId = Common.getUser().getCurrentScope().getId();
        int dsId = new DataSourceDao().copyDataSource(dataSourceId,factoryId, getResourceBundle());
        new UserDao().populateUserPermissions(Common.getUser());
        return dsId;
    }
    
    /**
     * 该数据源下是否有空压机
     * @param dataSourceId 数据源ID
     * @return 是否含有空压机
     */
    public boolean hasAcp(int dataSourceId){
    	boolean has = new ACPDao().hasAcp(dataSourceId);
    	return has;
    }
}
