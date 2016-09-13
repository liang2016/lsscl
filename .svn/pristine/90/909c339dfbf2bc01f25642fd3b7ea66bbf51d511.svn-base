package com.serotonin.mango.vo.statistics;

/**
 * 系统统计参数
 * 
 * @author 刘建坤
 * 
 */
public class StatisticsVO {
		
	public interface UseTypes{
		int USE_ACP = 1;
		int USE_ACPSYSTEM = 0;
	}
	
	public interface DataType {
		/**
		 * 二进制
		 */
		int BINARY = 1;
		int BINARY_UNSIGNED_INTEGER = 2;
		int BINARY_SIGNED_INTEGER = 3;
		int BINARY_BCD = 16;
		/**
		 * 四进制
		 */
		int QUATERNARY_UNSIGNED_INTEGER = 4;
		int QUATERNARY_SIGNED_INTEGER = 5;
		int QUATERNARY_UNSIGNED_INTEGER_SWAPPED = 6;
		int QUATERNARY_SIGNED_INTEGER_SWAPPED = 7;
		int QUATERNARY_FLOAT = 8;
		int QUATERNARY_FLOAT_SWAPPED = 9;
		int QUATERNARY_BCD = 17;
		/**
		 * 八进制
		 */
		int OCTAL_UNSIGNED_INTEGER = 10;
		int OCTAL_SIGNED_INTEGER = 11;
		int OCTAL_UNSIGNED_INTEGER_SWAPPED = 12;
		int OCTAL_SIGNED_INTEGER_SWAPPED = 13;
		int OCTAL_FLOAT = 14;
		int OCTAL_FLOAT_SWAPPED = 15;
		/**
		 * 字符类型
		 */
		int FIXED_LENGTH_STRING = 18;
		int VARIABLE_LENGTH_STRING = 19;
	}

	/**
	 * 统计参数id
	 */
	private int id;
	/**
	 * 统计名称
	 */
	private String statisticsName;
	/**
	 * 统计的数据类型
	 */
	private int dataType;
	/**
	 * 使用的范围(压缩空气系统统计/空压机统计)
	 */
	private int useType;

	public StatisticsVO() {
		super();
	}

	public StatisticsVO(int id, String statisticsName, int dataType, int useType) {
		super();
		this.id = id;
		this.statisticsName = statisticsName;
		this.dataType = dataType;
		this.useType = useType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStatisticsName() {
		return statisticsName;
	}

	public void setStatisticsName(String statisticsName) {
		this.statisticsName = statisticsName;
	}

	public int getDataType() {
		return dataType;
	}

	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	public int getUseType() {
		return useType;
	}

	public void setUseType(int useType) {
		this.useType = useType;
	}
}
