package com.serotonin.mango.vo.acp;

/**
 * 压缩空气系统成员 实体类
 * 
 * @author 王金阳
 * 
 */
public class ACPSystemMembersVO {

	// 1代表为数据点
	// 0代表是空压机
	public interface MemberTypes {
		int POINT = 1;
		int ACP = 0;
	}

	/**
	 * 压缩空气系统
	 */
	private ACPSystemVO acpSystem;
	/**
	 * 成员类型[0为压缩空气系统，1为数据点]
	 */
	private Integer membertype;
	/**
	 * 对应membertype的压缩空气系统编号或者数据点编号
	 */
	private Integer memberid;
	/**
	 * 统计参数
	 */
	private StatisticsParamVO sp;

}
