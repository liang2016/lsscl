package com.serotonin.mango.vo.acp;

import com.serotonin.mango.vo.DataPointVO;

/**
 * 空压机型号-属性 关系 实体类
 * @author 王金阳
 *
 */
public class ACPTypeAttrVO {
	
	/**
	 * 编号
	 */
	private Integer id;
	
	/**
	 * 空压机型号编号
	 */
	private ACPTypeVO acpTypeVO;
	/**
	 * 空压机属性标号
	 */
	private ACPAttrVO acpAttrVO;
//	/**
//	 * 偏移量
//	 */
//	private Integer offset;
//	
//	/**
//	 * 乘法器
//	 */
//	private float multiplier;
//	
//	/**
//	 * 附加的
//	 */
//	private float additive;
//	
//	/**
//	 * 数据类型
//	 */
//	private int dataType;
//	/**
//	 * 功能
//	 */
//	private int range;
//	/**
//	 * 文本渲染器类型
//	 */
//	private int renderertType;
//	/**
//	 * 格式
//	 */
//	private String layout;
//	/**
//	 * 后缀
//	 */
//	private String suffix;
	
	/**
	 * 将原来的乘法器，附加的，数据类型...全部放进DataPointVO对象中存存入数据库中
	 */
	private DataPointVO dataPointVO;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ACPTypeVO getAcpTypeVO() {
		return acpTypeVO;
	}

	public void setAcpTypeVO(ACPTypeVO acpTypeVO) {
		this.acpTypeVO = acpTypeVO;
	}

	public ACPAttrVO getAcpAttrVO() {
		return acpAttrVO;
	}

	public void setAcpAttrVO(ACPAttrVO acpAttrVO) {
		this.acpAttrVO = acpAttrVO;
	}

//	public Integer getOffset() {
//		return offset;
//	}
//	
//	public void setOffset(Integer offset) {
//		this.offset = offset;
//	}
//	
//	public float getMultiplier() {
//		return multiplier;
//	}
//
//	public void setMultiplier(float multiplier) {
//		this.multiplier = multiplier;
//	}
//
//	public float getAdditive() {
//		return additive;
//	}
//
//	public void setAdditive(float additive) {
//		this.additive = additive;
//	}
//
//	public ACPTypeAttrVO() {
//	}
//	
//	public int getDataType() {
//		return dataType;
//	}
//
//	public void setDataType(int dataType) {
//		this.dataType = dataType;
//	}
//
//	public int getRange() {
//		return range;
//	}
//
//	public void setRange(int range) {
//		this.range = range;
//	}
//
//	public int getRenderertType() {
//		return renderertType;
//	}
//
//	public void setRenderertType(int renderertType) {
//		this.renderertType = renderertType;
//	}
//
//	public String getLayout() {
//		return layout;
//	}
//
//	public void setLayout(String layout) {
//		this.layout = layout;
//	}
//
//	public String getSuffix() {
//		return suffix;
//	}
//
//	public void setSuffix(String suffix) {
//		this.suffix = suffix;
//	}
	
	public DataPointVO getDataPointVO() {
		return dataPointVO;
	}

	public void setDataPointVO(DataPointVO dataPointVO) {
		this.dataPointVO = dataPointVO;
	}

	public ACPTypeAttrVO(Integer id, ACPTypeVO acpTypeVO, ACPAttrVO acpAttrVO,
			DataPointVO dataPointVO) {
		super();
		this.id = id;
		this.acpTypeVO = acpTypeVO;
		this.acpAttrVO = acpAttrVO;
		this.dataPointVO = dataPointVO;
	}

	public ACPTypeAttrVO() {
	}

//	public ACPTypeAttrVO(Integer id, ACPTypeVO acpTypeVO, ACPAttrVO acpAttrVO,
//			Integer offset, float multiplier, float additive, int dataType,
//			int range, int renderertType, String layout, String suffix) {
//		super();
//		this.id = id;
//		this.acpTypeVO = acpTypeVO;
//		this.acpAttrVO = acpAttrVO;
//		this.offset = offset;
//		this.multiplier = multiplier;
//		this.additive = additive;
//		this.dataType = dataType;
//		this.range = range;
//		this.renderertType = renderertType;
//		this.layout = layout;
//		this.suffix = suffix;
//	}
	
	
	


}
