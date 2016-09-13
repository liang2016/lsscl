package com.serotonin.mango.vo.statistics;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import com.serotonin.mango.db.dao.statistics.StatisticsDao;

/**
 * 数据库StatisticsScript表对应的实体
 * 
 * @author 王金阳
 * 
 */
public class StatisticsScriptVO {

	// 检测脚本以什么为单位统计的
	public interface UnitTypes {
		// 脚本统计不以机器或者系统为单位
		int STATISTIC_UNIT_NONE = 0;
		// 脚本统计以机器为单位
		int STATISTIC_UNIT_MACHINE = 1;
		// 统计机器时需要不同周期不同的计算(用于健康指数和故障处理率)
		int STATISTIC_UNIT_MACHINE_DAY = 7;
		int STATISTIC_UNIT_MACHINE_WEEK = 8;
		int STATISTIC_UNIT_MACHINE_MONTH = 9;
		int STATISTIC_UNIT_MACHINE_QUARTER = 10;

		// 脚本统计以机器和机器为单位
		int STATISTIC_UNIT_DOUBLE = 2;
		// 脚本统计以系统为单位
		int STATISTIC_UNIT_SYSTEM = 3;
		// 同一个脚本需要有俩种类型的结果
		int STATISTIC_UNIT_SYSTEM_SYS = 4;
		int STATISTIC_UNIT_SYSTEM_ACP = 5;
		// 以工厂为单位
		int STATISTIC_UNIT_FACTORY = 6;
	}

	// 机器统计参数变量前缀
	public static final String ACP_PARAM_PREFIX = "A";
	// 系统统计参数变量前缀
	public static final String ACPSYSTEM_PARAM_PREFIX = "S";
	// 统计时分钟数为0
	public static final int STATISTIC_MINUTE = 0;
	// 统计时秒钟数为0
	public static final int STATISTIC_SECOND = 0;
	// 统计时毫秒数为0
	public static final int STATISTIC_MSECOND = 0;
	// 机器统计参数可能使用的全部名称的前缀
	public static final String[] ACP_PARAM_BASE = new String[] { "A1", "A2",
			"A3", "A4", "A5", "A6", "A7", "A8", "A9", "A0", };
	// 系统统计参数可能使用的全部名称的前缀
	public static final String[] ACPSYSTEM_PARAM_BASE = new String[] { "S1",
			"S2", "S3", "S4", "S5", "S6", "S7", "S8", "S9", "S0", };

	/**
	 * ID
	 */
	private int id;
	/**
	 * 别名(输出编号)
	 */
	private String xid;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 是否禁用
	 */
	private boolean disabled;
	/**
	 * 脚本内容
	 */
	private String conditionText;
	/**
	 * 统计开始时间
	 */
	private long startTime;

	/**
	 * 统计单位
	 */
	private int unitType;
	private int cycleType;

	public int getCycleType() {
		return cycleType;
	}

	public void setCycleType(int cycleType) {
		this.cycleType = cycleType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getXid() {
		return xid;
	}

	public void setXid(String xid) {
		this.xid = xid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public String getConditionText() {
		return conditionText;
	}

	public void setConditionText(String conditionText) {
		this.conditionText = conditionText;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public StatisticsScriptVO() {
	}

	public StatisticsScriptVO(int id, String xid, String name, boolean disabled) {
		this.id = id;
		this.xid = xid;
		this.name = name;
		this.disabled = disabled;
	}

	public StatisticsScriptVO(String xid, String name, boolean disabled,
			String conditionText, long startTime) {
		super();
		this.xid = xid;
		this.name = name;
		this.disabled = disabled;
		this.conditionText = conditionText;
		this.startTime = startTime;
	}

	public StatisticsScriptVO(int id, String xid, String name,
			boolean disabled, String conditionText, long startTime) {
		this.id = id;
		this.xid = xid;
		this.name = name;
		this.disabled = disabled;
		this.conditionText = conditionText;
		this.startTime = startTime;
	}

	/**
	 * 获取统计单位类型，系统或者机器
	 * 
	 * @return
	 */
	public int getUnitType() {
		// boolean isAcp = false;
		// boolean isSystem = false;
		// for(int i=0;i<ACP_PARAM_BASE.length;i++){
		// if(this.conditionText.indexOf(ACP_PARAM_BASE[i])!=-1){
		// isAcp = true;
		// break;
		// }
		// }
		// for(int i=0;i<ACPSYSTEM_PARAM_BASE.length;i++){
		// if(this.conditionText.indexOf(ACPSYSTEM_PARAM_BASE[i])!=-1){
		// isSystem = true;
		// break;
		// }
		// }
		// if(isAcp==true&&isSystem==true){
		// return UnitTypes.STATISTIC_UNIT_DOUBLE;
		// }else if(isAcp==true&&isSystem==false){
		// return UnitTypes.STATISTIC_UNIT_MACHINE;
		// }else if(isSystem==true&&isAcp==false){
		// return UnitTypes.STATISTIC_UNIT_SYSTEM;
		// }else{
		// return UnitTypes.STATISTIC_UNIT_NONE;
		// }
		return unitType;
	}

	public void setUnitType(int unitType) {
		this.unitType = unitType;
	}

	/**
	 * 获取脚本中使用到的统计参数的ID的集合
	 * 
	 * @return ID的集合
	 */
	public List<Integer> getStatisticParamIds() {
		List<Integer> ids = new ArrayList<Integer>();
		String[] names = new StatisticsDao()
				.getStatisticParamNames(getUnitType());
		for (int i = 0; i < names.length; i++) {
			if (this.conditionText.indexOf(names[i]) != -1) {
				ids.add(Integer.parseInt(names[i].substring(1)));
			}
		}
		return ids;
	}

	/**
	 * 获取上一个整点时间
	 * 
	 * @return 上一个整点时间
	 */
	public static Date getPrevExcuteTime() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MINUTE, STATISTIC_MINUTE);
		cal.set(Calendar.SECOND, STATISTIC_SECOND);
		cal.set(Calendar.MILLISECOND, STATISTIC_MSECOND);
		return cal.getTime();
	}

	/**
	 * 获取上一个整点时间
	 * 
	 * @return 上一个整点时间
	 */
	public static Date getPrevExcuteTime(long time) {
		Calendar cal = Calendar.getInstance();
		if (time != -1L)
			cal.setTimeInMillis(time);
		cal.set(Calendar.MINUTE, STATISTIC_MINUTE);
		cal.set(Calendar.SECOND, STATISTIC_SECOND);
		cal.set(Calendar.MILLISECOND, STATISTIC_MSECOND);
		return cal.getTime();
	}

	/**
	 * 获取下一个整点时间(当前若为整点时刻算为上一个整点)
	 * 
	 * @return 下一个整点时间
	 */
	public static Date getNextExcuteTime() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + 1);
		cal.set(Calendar.MINUTE, STATISTIC_MINUTE);
		cal.set(Calendar.SECOND, STATISTIC_SECOND);
		cal.set(Calendar.MILLISECOND, STATISTIC_MSECOND);
		return cal.getTime();
	}

	/**
	 * 获取下一个整点时间(当前若为整点时刻算为上一个整点)
	 * 
	 * @return 下一个整点时间
	 */
	public static Date getNextExcuteTime(long time) {
		Calendar cal = Calendar.getInstance();
		if (time != -1L)
			cal.setTimeInMillis(time);
		cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + 1);
		cal.set(Calendar.MINUTE, STATISTIC_MINUTE);
		cal.set(Calendar.SECOND, STATISTIC_SECOND);
		cal.set(Calendar.MILLISECOND, STATISTIC_MSECOND);
		return cal.getTime();
	}

}
