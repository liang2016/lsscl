package com.serotonin.mango.vo.power;

/**
 * 权限 实体类
 * @author Administrator
 *
 */
public class ActionVO {
	/**
	 * 权限编号
	 */
	private Integer id;
	
	/**
	 * 权限名称
	 */
	private String actionName;
	/**
	 * 权限描述
	 */
	private String description;
	
	/**
	 * 此描述对应的url
	 */
	private String url;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public ActionVO() {
	}

	public ActionVO(Integer id, String actionName, String description,
			String url) {
		this.id = id;
		this.actionName = actionName;
		this.description = description;
		this.url = url;
	}

 
	

}
