/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.vo.permission;

import javax.servlet.http.HttpServletRequest;

import com.serotonin.ShouldNeverHappenException;
import com.serotonin.mango.Common;
import com.serotonin.mango.rt.event.type.EventType;
import com.serotonin.mango.view.ShareUser;
import com.serotonin.mango.view.View;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.vo.WatchList;
import com.serotonin.mango.vo.event.EventTypeVO;
import com.serotonin.mango.vo.report.ReportInstance;
import com.serotonin.mango.vo.report.ReportVO;
import com.serotonin.mango.db.dao.scope.ScopeDao;
import com.serotonin.mango.vo.scope.ScopeVO;
import java.util.List;
import com.serotonin.mango.db.dao.EventDao;
import com.serotonin.mango.db.dao.power.RoleDao;
import com.serotonin.mango.vo.power.RoleVO;

/**
 *  
 * 
 */
public class Permissions {
    public interface DataPointAccessTypes {
        int NONE = 0;
        int READ = 1;
        int SET = 2;
        int DATA_SOURCE = 3;
        int ADMIN = 4;
    }

    private Permissions() {
        // no op
    }

    //
    // / Valid user
    //
    public static void ensureValidUser() throws PermissionException {
        ensureValidUser(Common.getUser());
    }

    public static void ensureValidUser(HttpServletRequest request) throws PermissionException {
        ensureValidUser(Common.getUser(request));
    }

    public static void ensureValidUser(User user) throws PermissionException {
        if (user == null)
            throw new PermissionException("Not logged in", null);
        if (user.isDisabled())
            throw new PermissionException("User is disabled", user);
    }

    //
    // / Administrator
    //
    public static boolean hasAdmin() throws PermissionException {
        return hasAdmin(Common.getUser());
    }

    public static boolean hasAdmin(HttpServletRequest request) throws PermissionException {
        return hasAdmin(Common.getUser(request));
    }

    public static boolean hasAdmin(User user) throws PermissionException {
        ensureValidUser(user);
        if(user.isTempAdmin()||user.isAdmin())
        	return true;
        else
        	return false;
    }

    public static void ensureAdmin() throws PermissionException {
        ensureAdmin(Common.getUser());
    }

    public static void ensureAdmin(HttpServletRequest request) throws PermissionException {
        ensureAdmin(Common.getUser(request));
    }

    public static void ensureAdmin(User user) throws PermissionException {
        if (!hasAdmin(user))
            throw new PermissionException("User is not an administrator", user);
    }

    //
    // / Data source admin
    //
    public static void ensureDataSourcePermission(User user, int dataSourceId) throws PermissionException {
        if (!hasDataSourcePermission(user, dataSourceId))
            throw new PermissionException("User does not have permission to data source", user);
    }

    public static void ensureDataSourcePermission(User user) throws PermissionException {
        if (!hasDataSourcePermission(user))
            throw new PermissionException("User does not have permission to any data sources", user);
    }

    public static boolean hasDataSourcePermission(User user, int dataSourceId) throws PermissionException {
        ensureValidUser(user);
        if (user.isAdmin()||user.isTempAdmin())
            return true;
        return user.getDataSourcePermissions().contains(dataSourceId);
    }

    public static boolean hasDataSourcePermission(User user) throws PermissionException {
        ensureValidUser(user);
        if (user.isAdmin()||user.isTempAdmin())
            return true;
        return user.getDataSourcePermissions().size() > 0;
    }

    //
    // / Data point access
    //
    public static void ensureDataPointReadPermission(User user, DataPointVO point) throws PermissionException {
        if (!hasDataPointReadPermission(user, point))
            throw new PermissionException("User does not have read permission to point", user);
    }

    public static boolean hasDataPointReadPermission(User user, DataPointVO point) throws PermissionException {
        return hasDataPointReadPermission(user, point.getDataSourceId(), point.getId(),point.getFactoryId());
    }

    private static boolean hasDataPointReadPermission(User user, int dataSourceId, int dataPointId,int factoryId)
            throws PermissionException {
        if (hasDataSourcePermission(user, dataSourceId))
            return true;
        if(!user.isAdmin()){
        	if(user.getCurrentScope() != null && user.getCurrentScope().getId().equals(factoryId))
        		return true;
        }
        DataPointAccess a = getDataPointAccess(user, dataPointId);
        if (a == null)
            return false;
        return a.getPermission()== DataPointAccess.READ || a.getPermission() == DataPointAccess.SET;
    }

    private static boolean hasDataPointReadPermission(User user, int dataSourceId, int dataPointId)
		    throws PermissionException {
			if (hasDataSourcePermission(user, dataSourceId))
			    return true;
			DataPointAccess a = getDataPointAccess(user, dataPointId);
			if (a == null)
			    return false;
			return a.getPermission() == DataPointAccess.READ || a.getPermission() == DataPointAccess.SET;
		}
    
    public static void ensureDataPointSetPermission(User user, DataPointVO point) throws PermissionException {
        if (!point.getPointLocator().isSettable())
            throw new ShouldNeverHappenException("Point is not settable");
        if (!hasDataPointSetPermission(user, point))
            throw new PermissionException("User does not have set permission to point", user);
    }

    public static boolean hasDataPointSetPermission(User user, DataPointVO point) throws PermissionException {
        if (hasDataSourcePermission(user, point.getDataSourceId()))
            return true;
        DataPointAccess a = getDataPointAccess(user, point.getId());
        if (a == null)
            return false;
        return a.getPermission() == DataPointAccess.SET;
    }

    private static DataPointAccess getDataPointAccess(User user, int dataPointId) {
        for (DataPointAccess a : user.getDataPointPermissions()) {
            if (a.getDataPointId() == dataPointId)
                return a;
        }
        return null;
    }

    public static int getDataPointAccessType(User user, DataPointVO point) {
        if (user == null || user.isDisabled())
            return DataPointAccessTypes.NONE;
        if (user.isAdmin())
            return DataPointAccessTypes.ADMIN;
        if (user.getDataSourcePermissions().contains(point.getDataSourceId()))
            return DataPointAccessTypes.DATA_SOURCE;
        DataPointAccess a = getDataPointAccess(user, point.getId());
        if (a == null)
            return DataPointAccessTypes.NONE;
        if (a.getPermission() == DataPointAccess.SET)
            return DataPointAccessTypes.SET;
        if (a.getPermission() == DataPointAccess.READ)
            return DataPointAccessTypes.READ;
        return DataPointAccessTypes.NONE;
    }

    //
    // / View access
    //
    public static void ensureViewPermission(User user, View view) throws PermissionException {
        if (view.getUserAccess(user) == ShareUser.ACCESS_NONE)
            throw new PermissionException("User does not have permission to the view", user);
    }

    public static void ensureViewEditPermission(User user, View view) throws PermissionException {
        if (view.getUserAccess(user) != ShareUser.ACCESS_OWNER)
            throw new PermissionException("User does not have permission to edit the view", user);
    }

    //
    // / Watch list access
    //
    public static void ensureWatchListPermission(User user, WatchList watchList) throws PermissionException {
        if (watchList.getUserAccess(user) == ShareUser.ACCESS_NONE)
            throw new PermissionException("User does not have permission to the watch list", user);
    }

    public static void ensureWatchListEditPermission(User user, WatchList watchList) throws PermissionException {
        if (watchList.getUserAccess(user) != ShareUser.ACCESS_OWNER)
            throw new PermissionException("User does not have permission to edit the watch list", user);
    }

    //
    // / Report access
    //
    public static void ensureReportPermission(User user, ReportVO report) throws PermissionException {
        if (user == null)
            throw new PermissionException("User is null", user);
        if (report == null)
            throw new PermissionException("Report is null", user);
        if (report.getUserId() != user.getId())
            throw new PermissionException("User does not have permission to access the report", user);
    }

    public static void ensureReportInstancePermission(User user, ReportInstance instance) throws PermissionException {
        if (user == null)
            throw new PermissionException("User is null", user);
        if (instance == null)
            throw new PermissionException("Report instance is null", user);
        if (instance.getUserId() != user.getId())
            throw new PermissionException("User does not have permission to access the report instance", user);
    }

    //
    // / Event access
    //
    public static boolean hasEventTypePermission(User user, EventType eventType) {
        switch (eventType.getEventSourceId()) {
        case EventType.EventSources.DATA_POINT://
            return hasDataPointReadPermission(user, eventType.getDataSourceId(), eventType.getDataPointId());
        case EventType.EventSources.DATA_SOURCE://
            return hasDataSourcePermission(user, eventType.getDataSourceId());
        case EventType.EventSources.SYSTEM:
        case EventType.EventSources.COMPOUND://
        case EventType.EventSources.SCHEDULED://
        case EventType.EventSources.PUBLISHER:
        case EventType.EventSources.AUDIT:
        case EventType.EventSources.MAINTENANCE:
            return hasAdmin(user);
        }
        return false;
    }

    public static void ensureEventTypePermission(User user, EventType eventType) throws PermissionException {
        if (!hasEventTypePermission(user, eventType))
            throw new PermissionException("User does not have permission to the view", user);
    }

    public static void ensureEventTypePermission(User user, EventTypeVO eventType) throws PermissionException {
        ensureEventTypePermission(user, eventType.createEventType());
    }
 // 用户权限范围是否和事件范围相等或者包含
	public static boolean hasScopePerssion(User user, EventType type,int evtId) {
		if(evtId==-1)
			return false;
		ScopeDao scopeDao = new ScopeDao();
		ScopeVO scopevo = scopeDao.getScopeByUser(user.getId());
		EventDao eventDao = new EventDao();
		if(scopevo==null||null==scopevo.getId()||scopevo.getId()==null){
			return false;
		}
		if(user.isAdmin()&&scopevo.getScopetype()==ScopeVO.ScopeTypes.HQ){//总部admin
			return true;
		}
		List<Integer> scopeIds = scopeDao.getChildScopeIds2(scopevo
				.getScopetype(), scopevo.getId());
		int eventScopeId = eventDao.getEventScope(evtId);
		for (int i = 0; i < scopeIds.size(); i++) {
			if (scopeIds.get(i) ==eventScopeId) {
				return true;
			}
		}
		// 如果不是admin,判断该用户是否对这个数据源有权限
		if (!user.isAdmin()) {
			switch (type.getEventSourceId()) {
			case EventType.EventSources.DATA_POINT://
				return hasDataPointReadPermission(user, type.getDataSourceId(), type.getDataPointId());
			case EventType.EventSources.DATA_SOURCE://
				return hasDataSourcePermission(user, type.getDataSourceId());
			}
		}
		return false;
	}
	
	
	/**
	 * 是否有进入统计脚本页面的验证(是否为总部admin)
	 * @param user 用户
	 * @return 是否
	 */
	public static void hasStatisticsScriptPermission(User user){
		boolean flag = false;
		if(user!=null){
			if(user.getRoleList()!=null){
				if(new RoleDao().getDefaultRoleId(user.getRoleList())==RoleVO.RoleTypes.HQ_MANAGER){
					flag = true;
				}
			}
		}
		if(flag==false){
			throw new PermissionException("User does not have permission to the view", user);
		}
		
	}
	
	/**
	 * 是否有权限进入统计信息页面
	 * @param user 用户信息
	 */
	public static void hasStatisticsInformationPermission(User user){
		boolean flag = true;
		//待实现
		if(flag==false){
			throw new PermissionException("User does not have permission to the view", user);
		}
	}
}
