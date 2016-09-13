package com.serotonin.mango.vo.power;

import java.util.List;

/**
 * 角色 实体类
 * @author 王金阳
 * 
 */
public class RoleVO {
	
	/**
	 * 目前系统中角色权限是固定的，不可编辑的，然而，为了方便现在代码的编写，将数据库中八条数据作为常量来使用
	 * @author 王金阳
	 *
	 */
	public interface RoleTypes{
		int HQ_MANAGER = 1;//总部管理员
		int HQ_USER = 2;//总部普通用户
		int ZONE_MANAGER = 3;//区域管理员
		int ZONE_USER = 4;//区域普通用户
		int SUBZONE_MANAGER = 5;//子区域管理员
		int SUBZONE_USER = 6;//子区域普通用户
		int FACTORY_MANAGER = 7;//工厂管理员
		int FACTORY_USER = 8;//工厂普通用户
	}
	 
	/**
	 * 角色编号
	 */
	private Integer id;
	
	/**
	 * 角色名称
	 */
	private String rolename;
	
	/**
	 * 角色描述
	 */
	private String description;
	
	/**
	 * 权限对应的范围类型
	 */
	private int scopeType;
	
	/**
	 * 此角色的所有权限
	 */
	private List<ActionVO> actionList;
	/**
	 * 是否是默认角色
	 */
	private boolean defaultRole;
	
	public int getScopeType() {
		return scopeType;
	}

	public void setScopeType(int scopeType) {
		this.scopeType = scopeType;
	}
	
	public boolean isDefaultRole() {
		return defaultRole;
	}

	public void setDefaultRole(boolean defaultRole) {
		this.defaultRole = defaultRole;
	}

	public RoleVO() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ActionVO> getActionList() {
		return actionList;
	}

	public void setActionList(List<ActionVO> actionList) {
		this.actionList = actionList;
	}

	public RoleVO(Integer id, String rolename, String description,
			List<ActionVO> actionList, boolean defaultRole) {
		this.id = id;
		this.rolename = rolename;
		this.description = description;
		this.actionList = actionList;
		this.defaultRole = defaultRole;
	}
}
