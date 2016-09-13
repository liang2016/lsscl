/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import com.serotonin.ShouldNeverHappenException;
import com.serotonin.json.JsonArray;
import com.serotonin.json.JsonException;
import com.serotonin.json.JsonObject;
import com.serotonin.json.JsonReader;
import com.serotonin.json.JsonRemoteEntity;
import com.serotonin.json.JsonRemoteProperty;
import com.serotonin.json.JsonSerializable;
import com.serotonin.json.JsonValue;
import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.DataPointDao;
import com.serotonin.mango.db.dao.DataSourceDao;
import com.serotonin.mango.rt.dataImage.SetPointSource;
import com.serotonin.mango.rt.event.type.SystemEventType;
import com.serotonin.mango.util.LocalizableJsonException;
import com.serotonin.mango.view.View;
import com.serotonin.mango.vo.dataSource.DataSourceVO;
import com.serotonin.mango.vo.permission.DataPointAccess;
import com.serotonin.mango.vo.permission.Permissions;
import com.serotonin.mango.vo.publish.PublishedPointVO;
import com.serotonin.mango.vo.publish.PublisherVO;
import com.serotonin.mango.web.dwr.beans.DataExportDefinition;
import com.serotonin.mango.web.dwr.beans.ImportTask;
import com.serotonin.mango.web.dwr.beans.TestingUtility;
import com.serotonin.util.StringUtils;
import com.serotonin.web.dwr.DwrResponseI18n;
import com.serotonin.web.i18n.LocalizableMessage;
import com.serotonin.mango.vo.power.RoleVO;
import com.serotonin.mango.vo.power.ActionVO;
import com.serotonin.mango.vo.scope.ScopeVO;


@JsonRemoteEntity
public class User implements SetPointSource, HttpSessionBindingListener, JsonSerializable {
    private int id = Common.NEW_ID;
    @JsonRemoteProperty
    private String username;
    @JsonRemoteProperty
    private String password;
    @JsonRemoteProperty 
    private String email;
    @JsonRemoteProperty
    private String phone;
    @JsonRemoteProperty
    private boolean admin;
    @JsonRemoteProperty
    private boolean disabled;
    private List<Integer> dataSourcePermissions;
    private List<DataPointAccess> dataPointPermissions;
    private int selectedWatchList;
    @JsonRemoteProperty
    private String homeUrl;
    private long lastLogin;
    private int receiveAlarmEmails;
    @JsonRemoteProperty
    private boolean receiveOwnAuditEvents;
    private boolean tempAdmin;
    private String loginUrl;
    public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	//
    // Session data. The user object is stored in session, and some other session-based information is cached here
    // for convenience.
    //
    private transient View view;
    private transient WatchList watchList;
    private transient DataPointVO editPoint;
    private transient DataSourceVO<?> editDataSource;
    private transient TestingUtility testingUtility;
    private transient Map<String, byte[]> reportImageData;
    private transient PublisherVO<? extends PublishedPointVO> editPublisher;
    private transient ImportTask importTask;
    private transient boolean muted = false;
    private transient DataExportDefinition dataExportDefinition;
    private transient Map<String, Object> attributes = new HashMap<String, Object>();

    //下面的五个属性也是需要加入到Session中的，用于权限验证
    /**
     * 当前用户拥有的角色集合
     */
    private transient List<RoleVO> roleList;
    
    /**
     * 当前用户的角色
     */
    private transient RoleVO currentRole;
	/**
	 * 区域范围--此用户注册于那个范围的
	 */
	private transient ScopeVO homeScope;
	
	/**
	 * 当前进入的范围--高级别的用户有权限进入下级范围
	 */
	private transient ScopeVO currentScope;
	
	/**
	 * 当前角色拥有的权限--放入Session(用户每次更换角色则设置不同的值)
	 */
	private transient List<ActionVO> currentRoleActionList;
	/**
	 * 有权限的子范围的集合
	 */
	private transient List<ScopeVO> childScopeList;
	
	/**
	 * 默认角色
	 */
	private transient RoleVO defaultRole;
	/**
	 * 用户目前添加事件处理器的个数
	 */
	private int eventHandlerCount;
	/**
	 * 用户最高添加个数限制
	 */
	private  int limit;
    public RoleVO getDefaultRole() {
		return defaultRole;
	}

	public void setDefaultRole(RoleVO defaultRole) {
		this.defaultRole = defaultRole;
	}

	public List<RoleVO> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<RoleVO> roleList) {
		this.roleList = roleList;
	}

	public RoleVO getCurrentRole() {
		return currentRole;
	}

	public void setCurrentRole(RoleVO currentRole) {
		this.currentRole = currentRole;
	}

	public ScopeVO getHomeScope() {
		return homeScope;
	}

	public void setHomeScope(ScopeVO homeScope) {
		this.homeScope = homeScope;
	}

	public ScopeVO getCurrentScope() {
		return currentScope;
	}

	public void setCurrentScope(ScopeVO currentScope) {
		this.currentScope = currentScope;
	}

	public List<ActionVO> getCurrentRoleActionList() {
		return currentRoleActionList;
	}

	public void setCurrentRoleActionList(List<ActionVO> currentRoleActionList) {
		if(this.tempAdmin&&this.currentScope.getScopetype()==3){//这里要根据数据库action表对应的id
			currentRoleActionList.add(new ActionVO(5,"data_sources","data_sources","/data_sources.shtm"));
			currentRoleActionList.add(new ActionVO(3,"data_point_edit","data_point_edit","/data_point_edit.shtm"));
			currentRoleActionList.add(new ActionVO(4,"data_source_edit","data_source_edit","/data_source_edit.shtm"));
			currentRoleActionList.add(new ActionVO(16,"point_links","point_links","/point_links.shtm"));
			currentRoleActionList.add(new ActionVO(14,"point_hierarchy","point_hierarchy","/point_hierarchy.shtm"));
			currentRoleActionList.add(new ActionVO(62,"compress_air_system","compress_air_system","/compress_air_system.shtm"));
		}
		this.currentRoleActionList = currentRoleActionList;
	}
	
	public List<ScopeVO> getChildScopeList() {
		return childScopeList;
	}

	public void setChildScopeList(List<ScopeVO> childScopeList) {
		this.childScopeList = childScopeList;
	}

	/**
     * Used for various display purposes.
     */
    public String getDescription() {
        return username + " (" + id + ")";
    }

    public boolean isFirstLogin() {
        return lastLogin == 0;
    }

    //
    // /
    // / SetPointSource implementation
    // /
    //
    public int getSetPointSourceId() {
        return id;
    }

    public int getSetPointSourceType() {
        return SetPointSource.Types.USER;
    }

    @Override
    public void raiseRecursionFailureEvent() {
        throw new ShouldNeverHappenException("");
    }

    //
    // /
    // / HttpSessionBindingListener implementation
    // /
    //
    public void valueBound(HttpSessionBindingEvent evt) {
        // User is bound to a session when logged in. Notify the event manager.
        SystemEventType.raiseEvent(new SystemEventType(SystemEventType.TYPE_USER_LOGIN, id),
                System.currentTimeMillis(), true, new LocalizableMessage("event.login", username));
    }

    public void valueUnbound(HttpSessionBindingEvent evt) {
        // User is unbound from a session when logged out or the session expires.
        SystemEventType.returnToNormal(new SystemEventType(SystemEventType.TYPE_USER_LOGIN, id),
                System.currentTimeMillis());

        // Terminate any testing utility
        if (testingUtility != null)
            testingUtility.cancel();
    }

    // Convenience method for JSPs
    public boolean isDataSourcePermission() {
        return Permissions.hasDataSourcePermission(this);
    }

    //
    // Testing utility management
    public <T extends TestingUtility> T getTestingUtility(Class<T> requiredClass) {
        TestingUtility tu = testingUtility;

        if (tu != null) {
            try {
                return requiredClass.cast(tu);
            }
            catch (ClassCastException e) {
                tu.cancel();
                testingUtility = null;
            }
        }
        return null;
    }

    public void setTestingUtility(TestingUtility testingUtility) {
        TestingUtility tu = this.testingUtility;
        if (tu != null)
            tu.cancel();
        this.testingUtility = testingUtility;
    }

    public void cancelTestingUtility() {
        setTestingUtility(null);
    }

    // Properties
    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public WatchList getWatchList() {
        return watchList;
    }

    public void setWatchList(WatchList watchList) {
        this.watchList = watchList;
    }

    public DataPointVO getEditPoint() {
        return editPoint;
    }

    public void setEditPoint(DataPointVO editPoint) {
        this.editPoint = editPoint;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public List<Integer> getDataSourcePermissions() {
        return dataSourcePermissions;
    }

    public void setDataSourcePermissions(List<Integer> dataSourcePermissions) {
        this.dataSourcePermissions = dataSourcePermissions;
    }

    public List<DataPointAccess> getDataPointPermissions() {
        return dataPointPermissions;
    }

    public void setDataPointPermissions(List<DataPointAccess> dataPointPermissions) {
        this.dataPointPermissions = dataPointPermissions;
    }

    public DataSourceVO<?> getEditDataSource() {
        return editDataSource;
    }

    public void setEditDataSource(DataSourceVO<?> editDataSource) {
        this.editDataSource = editDataSource;
    }

    public int getSelectedWatchList() {
        return selectedWatchList;
    }

    public void setSelectedWatchList(int selectedWatchList) {
        this.selectedWatchList = selectedWatchList;
    }

    public String getHomeUrl() {
        return homeUrl;
    }

    public void setHomeUrl(String homeUrl) {
        this.homeUrl = homeUrl;
    }

    public long getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(long lastLogin) {
        this.lastLogin = lastLogin;
    }

    public Map<String, byte[]> getReportImageData() {
        return reportImageData;
    }

    public void setReportImageData(Map<String, byte[]> reportImageData) {
        this.reportImageData = reportImageData;
    }

    public PublisherVO<? extends PublishedPointVO> getEditPublisher() {
        return editPublisher;
    }

    public void setEditPublisher(PublisherVO<? extends PublishedPointVO> editPublisher) {
        this.editPublisher = editPublisher;
    }

    public ImportTask getImportTask() {
        return importTask;
    }

    public void setImportTask(ImportTask importTask) {
        this.importTask = importTask;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public int getReceiveAlarmEmails() {
        return receiveAlarmEmails;
    }

    public void setReceiveAlarmEmails(int receiveAlarmEmails) {
        this.receiveAlarmEmails = receiveAlarmEmails;
    }

    public boolean isReceiveOwnAuditEvents() {
        return receiveOwnAuditEvents;
    }

    public void setReceiveOwnAuditEvents(boolean receiveOwnAuditEvents) {
        this.receiveOwnAuditEvents = receiveOwnAuditEvents;
    }

    public DataExportDefinition getDataExportDefinition() {
        return dataExportDefinition;
    }

    public void setDataExportDefinition(DataExportDefinition dataExportDefinition) {
        this.dataExportDefinition = dataExportDefinition;
    }

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public Object removeAttribute(String key) {
        return attributes.remove(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String key) {
        return (T) attributes.get(key);
    }

    public void validate(DwrResponseI18n response) {
        if (StringUtils.isEmpty(username))
            response.addMessage("username", new LocalizableMessage("validate.required"));
        if (StringUtils.isEmpty(email))
            response.addMessage("email", new LocalizableMessage("validate.required"));
    	Pattern pattern = Pattern.compile("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+",
    	Pattern.CASE_INSENSITIVE);
    	Matcher matcher = pattern.matcher(email);
        if(!matcher.matches())
        	response.addMessage("email", new LocalizableMessage("validate.email.error"));
        if (id == Common.NEW_ID && StringUtils.isEmpty(password))
            response.addMessage("password", new LocalizableMessage("validate.required"));
        pattern = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9])|(17[0-9]))\\d{8}$",
    	Pattern.CASE_INSENSITIVE);
    	matcher = pattern.matcher(phone);
    	if(!matcher.matches())
    		response.addMessage("phone", new LocalizableMessage("validate.phone.error"));
        // Check field lengths
        if (StringUtils.isLengthGreaterThan(username, 40))
            response.addMessage("username", new LocalizableMessage("validate.notLongerThan", 40));
        if (StringUtils.isLengthGreaterThan(email, 255))
            response.addMessage("email", new LocalizableMessage("validate.notLongerThan", 255));
        if (StringUtils.isLengthGreaterThan(phone, 40))
            response.addMessage("phone", new LocalizableMessage("validate.notLongerThan", 40));
        
    }

    //
    // /
    // / Serialization
    // /
    //
    @Override
    public void jsonDeserialize(JsonReader reader, JsonObject json) {
        // Note: data source permissions are explicitly deserialized by the import/export because the data sources and
        // points need to be certain to exist before we can resolve the xids.
    }

    public void jsonDeserializePermissions(JsonReader reader, JsonObject json) throws JsonException {
        if (admin) {
            dataSourcePermissions.clear();
            dataPointPermissions.clear();
        }
        else {
            JsonArray jsonDataSources = json.getJsonArray("dataSourcePermissions");
            if (jsonDataSources != null) {
                dataSourcePermissions.clear();
                DataSourceDao dataSourceDao = new DataSourceDao();

                for (JsonValue jv : jsonDataSources.getElements()) {
                    String xid = jv.toJsonString().getValue();
                    DataSourceVO<?> ds = dataSourceDao.getDataSource(xid);
                    if (ds == null)
                        throw new LocalizableJsonException("emport.error.missingSource", xid);
                    dataSourcePermissions.add(ds.getId());
                }
            }

            JsonArray jsonPoints = json.getJsonArray("dataPointPermissions");
            if (jsonPoints != null) {
                // Get a list of points to which permission already exists due to data source access.
                DataPointDao dataPointDao = new DataPointDao();
                List<Integer> permittedPoints = new ArrayList<Integer>();
                for (Integer dsId : dataSourcePermissions) {
                    for (DataPointVO dp : dataPointDao.getDataPoints(dsId, null))
                        permittedPoints.add(dp.getId());
                }

                dataPointPermissions.clear();

                for (JsonValue jv : jsonPoints.getElements()) {
                    DataPointAccess access = reader.readPropertyValue(jv, DataPointAccess.class, null);
                    if (!permittedPoints.contains(access.getDataPointId()))
                        // The user doesn't already have access to the point.
                        dataPointPermissions.add(access);
                }
            }
        }
    }

    @Override
    public void jsonSerialize(Map<String, Object> map) {
        if (!admin) {
            List<String> dsXids = new ArrayList<String>();
            DataSourceDao dataSourceDao = new DataSourceDao();
            for (Integer dsId : dataSourcePermissions)
                dsXids.add(dataSourceDao.getDataSource(dsId).getXid());
            map.put("dataSourcePermissions", dsXids);

            map.put("dataPointPermissions", dataPointPermissions);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final User other = (User) obj;
        if (id != other.id)
            return false;
        return true;
    }

	public int getEventHandlerCount() {
		return eventHandlerCount;
	}

	public void setEventHandlerCount(int eventHandlerCount) {
		this.eventHandlerCount = eventHandlerCount;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public boolean isTempAdmin() {
		return tempAdmin;
	}

	public void setTempAdmin(boolean tempAdmin) {
		this.tempAdmin = tempAdmin;
	}
}
