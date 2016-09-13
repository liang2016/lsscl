package com.serotonin.mango.vo.factory;

public class FactorySetting {
	/**
	 * 工厂编号
	 */
	private int factoryId;
	/**
	 * 属性名
	 */
	private String SettingName;
	/**
	 * 属性值
	 */
	private String SettingValue;

	public FactorySetting() {
		super();
	}

	public FactorySetting(int factoryId, String settingName, String settingValue) {
		super();
		this.factoryId = factoryId;
		SettingName = settingName;
		SettingValue = settingValue;
	}

	public int getFactoryId() {
		return factoryId;
	}

	public void setFactoryId(int factoryId) {
		this.factoryId = factoryId;
	}

	public String getSettingName() {
		return SettingName;
	}

	public void setSettingName(String settingName) {
		SettingName = settingName;
	}

	public String getSettingValue() {
		return SettingValue;
	}

	public void setSettingValue(String settingValue) {
		SettingValue = settingValue;
	}
}
