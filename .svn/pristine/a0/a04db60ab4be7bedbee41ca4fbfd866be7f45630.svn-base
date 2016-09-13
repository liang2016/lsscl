package com.serotonin.mango.vo.scope;

import java.util.List;

import com.serotonin.mango.vo.User;
import com.serotonin.mango.vo.scope.TradeVO;
import com.serotonin.util.StringUtils;
import com.serotonin.web.dwr.DwrResponseI18n;
import com.serotonin.web.i18n.LocalizableMessage;
/**
 * 范围实体类 基类
 * @author 王金阳
 *
 */
public class ScopeVO {

	/**
	 * 规定scopetype字段:总部为0,区域类型编号为1,子区域类型编号为2,工厂类型编号为3
	 * @author 王金阳
	 *
	 */
	public interface ScopeTypes{
		int HQ = 0;
		int ZONE =1;
		int SUBZONE =2;
		int FACTORY =3;
	} 
	
	/**
	 * 范围编号
	 */
	private Integer id;
	/**
	 * 范围名称
	 */
	private String  scopename;
	
	/**
	 * 地址
	 */
	private String address;
	
	/**
	 * 范围在地图上现实图标的经度
	 */
	private double lon;
	
	/**
	 * 范围在地图上现实图标的纬度
	 */
	private double lat;
	
	/**
	 * 放大倍数
	 */
	private int enlargenum;
	
	/**
	 * 描述
	 */
	private String description;
	 
	/**
	 * 上级范围编号(总部-->区域-->子区域-->工厂)
	 * 区域下有可能直接是工厂
	 */
	private ScopeVO parentScope;
	/**
	 * 范围类型(是区域，子区域，工厂)
	 */
	private Integer scopetype;
	
	/**
	 * 行业类型
	 */
	private TradeVO tradeVO;
	
	/**
	 * 负责人
	 */
	private User scopeUser;
	 
	/**
	 * 报警总数
	 */
	private Integer warnCount;
	
	/**
	 * 3天未处理
	 */
	private Integer warnUnderThreeDays;
	
	/**
	 * 7天未处理
	 */
	private Integer warnUnderSevenDays;
 
	/**
	 * 父节点的父节点
	 */
	private ScopeVO grandParent;
	
	/**
	 * 是否是注册范围
	 */
	private boolean isHomeScope;
	
	/**
	 * 拥有权限访问的下级集合
	 */
	private List<ScopeVO> permitScopeList;
	/**
	 * 是否被禁用
	 */
	private boolean disabled; 
	/**
	 * 普通用户是否有设置权限
	 */
	private boolean userIsSet;
	//客户代码
	private String code;
	private String backgroundFilename;
	public String getBackgroundFilename() {
		return backgroundFilename;
	}

	public void setBackgroundFilename(String backgroundFilename) {
		this.backgroundFilename = backgroundFilename;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean isUserIsSet() {
		return userIsSet;
	}

	public void setUserIsSet(boolean userIsSet) {
		this.userIsSet = userIsSet;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getScopename() {
		return scopename;
	}

	public void setScopename(String scopename) {
		this.scopename = scopename;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public int getEnlargenum() {
		return enlargenum;
	}

	public void setEnlargenum(int enlargenum) {
		this.enlargenum = enlargenum;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ScopeVO getParentScope() {
		return parentScope;
	}

	public void setParentScope(ScopeVO parentScope) {
		this.parentScope = parentScope;
	}

	public Integer getScopetype() {
		return scopetype;
	}

	public void setScopetype(Integer scopetype) {
		this.scopetype = scopetype;
	}

	public TradeVO getTradeVO() {
		return tradeVO;
	}

	public void setTradeVO(TradeVO tradeVO) {
		this.tradeVO = tradeVO;
	}

	public User getScopeUser() {
		return scopeUser;
	}

	public void setScopeUser(User scopeUser) {
		this.scopeUser = scopeUser;
	}

	public Integer getWarnCount() {
		return warnCount;
	}

	public void setWarnCount(Integer warnCount) {
		this.warnCount = warnCount;
	}

	public Integer getWarnUnderThreeDays() {
		return warnUnderThreeDays;
	}

	public void setWarnUnderThreeDays(Integer warnUnderThreeDays) {
		this.warnUnderThreeDays = warnUnderThreeDays;
	}

	public Integer getWarnUnderSevenDays() {
		return warnUnderSevenDays;
	}

	public void setWarnUnderSevenDays(Integer warnUnderSevenDays) {
		this.warnUnderSevenDays = warnUnderSevenDays;
	}
	 
	public ScopeVO getGrandParent() {
		return grandParent;
	}

	public void setGrandParent(ScopeVO grandParent) {
		this.grandParent = grandParent;
	}

	
	public boolean isHomeScope() {
		return isHomeScope;
	}

	public void setHomeScope(boolean isHomeScope) {
		this.isHomeScope = isHomeScope;
	}

	public List<ScopeVO> getPermitScopeList() {
		return permitScopeList;
	}

	public void setPermitScopeList(List<ScopeVO> permitScopeList) {
		this.permitScopeList = permitScopeList;
	}

	public ScopeVO() { 
	}

	public ScopeVO(Integer id, String scopename, String address, double lon,
			double lat, int enlargenum, String description,
			ScopeVO parentScope, Integer scopetype, TradeVO tradeVO,
			User scopeUser, Integer warnCount, Integer warnUnderThreeDays,
			Integer warnUnderSevenDays, ScopeVO grandParent,
			boolean isHomeScope, List<ScopeVO> permitScopeList) {
		this.id = id;
		this.scopename = scopename;
		this.address = address;
		this.lon = lon;
		this.lat = lat;
		this.enlargenum = enlargenum;
		this.description = description;
		this.parentScope = parentScope;
		this.scopetype = scopetype;
		this.tradeVO = tradeVO;
		this.scopeUser = scopeUser;
		this.warnCount = warnCount;
		this.warnUnderThreeDays = warnUnderThreeDays;
		this.warnUnderSevenDays = warnUnderSevenDays;
		this.grandParent = grandParent;
		this.isHomeScope = isHomeScope;
		this.permitScopeList = permitScopeList;
	}
	public void validate(DwrResponseI18n response) {
		if (id==null){
			response.addMessage("id", new LocalizableMessage(
					"scope.isNull"));

		}
	}
	//
}
