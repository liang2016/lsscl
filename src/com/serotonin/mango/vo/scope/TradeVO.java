package com.serotonin.mango.vo.scope;

/**
 * 工厂行业信息 实体
 * @author 王金阳
 *
 */
public class TradeVO {
	
	/**
	 * 行业编号
	 */
	private Integer id;
	
	/**
	 * 行业名称
	 */
	private String tradename;
	
	/**
	 * 行业描述
	 */
	private String description;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTradename() {
		return tradename;
	}

	public void setTradename(String tradename) {
		this.tradename = tradename;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TradeVO() {
	}

	public TradeVO(Integer id, String tradename, String description) {
		this.id = id;
		this.tradename = tradename;
		this.description = description;
	}
	
	
 
}
