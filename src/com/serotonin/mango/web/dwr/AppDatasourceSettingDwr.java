/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     

     auther : thl
     20140314
 */
package com.serotonin.mango.web.dwr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lsscl.app.dao.AppsettingDao;
import com.serotonin.db.IntValuePair;
import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.DataPointDao;
import com.serotonin.mango.db.dao.DataSourceDao;
import com.serotonin.mango.db.dao.SystemSettingsDao;
import com.serotonin.mango.db.dao.acp.ACPDao;
import com.serotonin.mango.vo.AcpNameComparator;
import com.serotonin.mango.vo.AppPoints;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.vo.acp.ACPVO;
import com.serotonin.mango.vo.dataSource.DataSourceVO;
import com.serotonin.web.dwr.DwrResponseI18n;
import com.serotonin.web.dwr.MethodFilter;

public class AppDatasourceSettingDwr extends BaseDwr {
	
    private static final Log LOG = LogFactory.getLog(AppDatasourceSettingDwr.class);
    private AppsettingDao dao;
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

    @MethodFilter
    public DwrResponseI18n getPointsByAcpId(String acpid) {
    	dao = new AppsettingDao();
        DwrResponseI18n response = new DwrResponseI18n();
        List<AppPoints<?>>points = dao.getPointsByAcpId(acpid);
        response.addData("points",points);
        return response;
    }
    /**
     * 根据数据源获取空压机
     * @param dsid
     * @return
     */
    public DwrResponseI18n getAcps(String dsid) {
    	DwrResponseI18n response = new DwrResponseI18n();
    	if(dsid==null||"".equals(dsid))return response;
    	int id = Integer.parseInt(dsid);
        List<ACPVO> acps = new ACPDao().findAcpsByDataSourceId(id);
        if(acps!=null){
        	Collections.sort(acps, AcpNameComparator.instance);
        }
        response.addData("acps", acps);
        return response;
    }
    
    /**
     * 单个空压机或数据源下的点
     * @param aid
     * @param dsid
     * @return
     */
    public DwrResponseI18n getPoints(String aid,String dsid){
     	DwrResponseI18n response = new DwrResponseI18n();
    	List<DataPointVO>points = new ArrayList<DataPointVO>();
    	if(aid!=null&&!"-1".equals(aid)){
    		if(dsid==null)return response;
    		int acpId = Integer.parseInt(aid);
    		int dataSourceId = Integer.parseInt(dsid);
    		points = new DataPointDao().getDataPointByAcpId(acpId, dataSourceId);
    	}else{//获取数据源的的元
    		if(dsid==null)return response;
    		int dataSourceId = Integer.parseInt(dsid);
    		points = getDataPointWhitoutAcp(dataSourceId);
    	}
    	response.addData("points", points);
        return response;
    }
    /**
     * 获取当前数据源下不属于任何空压机的点
     * @param dsid 数据源ID
     * @return 点的集合
     */
    private List<DataPointVO> getDataPointWhitoutAcp(int dsid){
    	DataPointDao dpDao =  new DataPointDao();
        //空压机集合
        List<ACPVO> acpList = new ACPDao().findAcpsAndSystemsByDataSourceId(dsid);
        //所有空压机的Id的数组
        int[] acpIds = new int[acpList.size()];
        for(int i =0;i<acpList.size();i++){
        	acpIds[i]=acpList.get(i).getId();
        }
        //不属于空压机的点/直接属于数据源的点
        List<DataPointVO> otherPointList = dpDao.getDataPointWhitoutAcp(dsid,acpIds); 
        return otherPointList;
    }

    public DwrResponseI18n saveAcp(String aid,String scopeId,String name,String power,String type){
    	DwrResponseI18n response = new DwrResponseI18n();
    	int acpid = 0;
    	if("".equals(aid)){//新增
    		acpid = dao.addAcp(scopeId,name,power,type);
    	}else{//修改
    		if(aid!=null)acpid = Integer.parseInt(aid);
    		dao.updateAcp(aid,name,power,type);
    	}
    	response.addData("aid", acpid);
    	return response;
    }
    
    public DwrResponseI18n deleteAcp(String aid){
    	dao = new AppsettingDao();
    	dao.deleteAcp(aid);
    	return new DwrResponseI18n();
    }
    /**
     * 保存点信息
     * @param pid app点id（自增）
     * @param name 点名称
     * @param pointId 属性点id
     * @param aid app空压机id
     * @return
     */
    public DwrResponseI18n savePoint(String pid,String name,String pointId,String aid){
    	dao = new AppsettingDao();
    	boolean ret = true;
    	DwrResponseI18n response = new DwrResponseI18n();
        if(!"-1".equals(pid)){//修改
        	ret = dao.updatePoint(pid,name,pointId,aid);
        }else{//新增
        	ret = dao.addPoint(name,pointId,aid);
        }
        if(!ret)response.addData("error","app.pointSaveError");
        return response;
    }
    
    public DwrResponseI18n saveAcpPoints(String dsid,String acpid,String appAcpId){
    	DwrResponseI18n response = new DwrResponseI18n();
    	if("".equals(appAcpId)||appAcpId==null){
    		return response;
    	}
    	DataPointDao dpDao = new DataPointDao();
    	List<DataPointVO> points = new ArrayList<DataPointVO>();
    	int dataSourceId = Integer.parseInt(dsid);
    	if("-1".equals(acpid)){
    		 //空压机集合
            List<ACPVO> acpList = new ACPDao().findAcpsAndSystemsByDataSourceId(dataSourceId);
            //所有空压机的Id的数组
            int[] acpIds = new int[acpList.size()];
    		points = dpDao.getDataPointWhitoutAcp(dataSourceId, acpIds);
    	}else{
    		points = dpDao.getDataPointByAcpId(Integer.parseInt(acpid), dataSourceId);
    	}
    	for(DataPointVO p:points){
    		dao.addPoint(p.getName(), p.getId()+"",appAcpId);
    	}
    	return response;
    }
    public DwrResponseI18n deletePoint(String pid){
    	dao = new AppsettingDao();
    	dao.deletePoint(pid);
    	return new DwrResponseI18n();
    }
    /**
     * 获取数据源
     * @return
     */
    @MethodFilter
    public DwrResponseI18n getDataSources() {
        DwrResponseI18n response = new DwrResponseI18n();
        List<DataSourceVO<?>> data = Common.ctx.getRuntimeManager().getDataSources();
        response.addData("dataSources", data);
        return response;
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
