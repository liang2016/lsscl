package com.serotonin.mango.rt.statistic.common;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Properties;
import com.serotonin.mango.vo.statistics.StatisticsScriptVO;

/**
 * 统计信息工具包
 * @author 王金阳
 *
 */
public class StatisticsUtil {
	
	/**
	 * 节能指数
	 */
	public static final int ENERGY_SAVING_INDEX = 0;
	
	/**
	 * 节能指标一
	 */
	public static final int ENERGY_SAVING_TARGET_NO1 = 1;
	
	/**
	 * 节能指标二
	 */
	public static final int ENERGY_SAVING_TARGET_NO2 = 2;
	
	/**
	 * 节能指标三
	 */
	public static final int ENERGY_SAVING_TARGET_NO3 = 3;
	
	/**
	 * 节能指标四
	 */
	public static final int ENERGY_SAVING_TARGET_NO4 = 4;
	
	/**
	 * 节能指标五
	 */
	public static final int ENERGY_SAVING_TARGET_NO5 = 5;
	
	/**
	 * 备件指数
	 */
	public static final int INDEX_OF_RESERVE_STORE = 6; 
	
	/**
	 * 故障处理率
	 */
	public static final int RATE_OF_TROUBLE_HANDLE = 7;
	
	/**
	 * 健康指数
	 */
	public static final int INDEX_OF_HEALTH = 8;
	
	/**
	 * 系统压降
	 */
	public static final int SYSTEM_YAJIANG = 9;
	
	
	
	
	
	
	
	/********************以上定义为 统计脚本ID(需要在后台定时统计的)**********************/
	
	
	/******************以下定义为 必须的统计参数ID(即统计需要用到的参数)*********************/
	
	/*****机器统计参数****/
	
//	/**
//	 * 机器额定功率
//	 */
//	public static final int STATISTICS_PARAM_ACP_VOLUMETRIC = 1;
	
//	/**
//	 * 机器加载时间
//	 */
//	public static final int STATISTICS_PARAM_ACP_ONLOAD_TIME = 2; 
//	
//	/**
//	 * 机器卸载时间
//	 */
//	public static final int STATISTICS_PARAM_ACP_UNLOAD_TIME = 3;
	
	/**
	 * 机器运行状态
	 */
	public static final int STATISTICS_PARAM_ACP_STATUS = 2;
	
	
	
	
	/**
	 * 故障报警
	 */
	public static final int STATISTICS_PARAM_ACP_WARNING_BY_STOPPAGE = 3;
	
	/**
	 * 机器全载时间
	 */
	public static final int STATISTICS_PARAM_ACP_LADEN_TIME = 4;
	
	/**
	 * 机器运行时间
	 */
	public static final int STATISTICS_PARAM_ACP_RUN_TIME = 5;
	
	/**
	 * P空压机=排气压力
	 */
	public static final int STATISTICS_PARAM_ACP_P = 6;
	
	/**
	 * 机器电流
	 */
	public static final int STATISTICS_PARAM_ACP_ELECTRICITY = 12;
	
	/**
	 * 运行/停止
	 */
	public static final int STATISTICS_PARAM_ACP_STATUS_RUN_STOP = 10;
	
	/**
	 * 加载/卸载
	 */
	public static final int STATISTICS_PARAM_ACP_STATUS_RUN_LOAD = 9;
	
	/**
	 * 是否有报警	
	 */
	public static final int STATISTICS_PARAM_ACP_STATUS_RUN_ALARM = 11;
	
	/**
	 * Alarm Code
	 */
	public static final int STATISTICS_PARAM_ACP_ALARM_CODE = 11;
	
	/**
	 * Warning Code
	 */
	public static final int STATISTICS_PARAM_ACP_WARNING_CODE = 13;
	
	
	/*****系统统计参数****/
	
	/**
	 * 系统P3
	 */
	public static final int STATISTICS_PARAM_SYSTEM_P3 = 7;
	
	/**
	 * 系统流量
	 */
	public static final int STATISTICS_PARAM_SYSTEM_FLOW = 8;
	
//	/**
//	 * P总管=P3
//	 */
//	public static final int STATISTICS_PARAM_SYSTEM_P = 7;
//	
	
	
	
	/**
	 * 获取已经加载配置文件的Properties对象
	 * @return
	 */
	public static Properties getProperties(){
		StatisticsUtil util = new StatisticsUtil();
		InputStream is = util.getClass().getResourceAsStream("/statisticsConfig.properties");
		Properties properties = new Properties();
		try {
			properties.load(is);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return properties;
	} 
	
	/**
	 * 从配置文件中获取统计周期
	 * @return 返回统计周期
	 */
	public static int getStatisticsCycle(){
		Properties properties = getProperties();
		String cycle = properties.getProperty("statistics.cycle");
		return Integer.parseInt(cycle);
	}
	
	/**
	 * 查找脚本的计算结果需要相比的值
	 * @return
	 */
	public static double getTarget(String key){
		Properties properties = getProperties();
		String target = properties.getProperty(key);
		return Double.parseDouble(target);
	}
	
	/**
	 * 查找脚本的计算结和需要相比的值是什么关系，大于还是小于
	 * @return 大于返回1，小于返回2
	 */
	public static int getRelation(String key){
		Properties properties = getProperties();
		String relation = properties.getProperty(key);
		return Integer.parseInt(relation);
	}
	
	/**
	 * 获取数据采集周期，多久采集一次数据
	 * @return 采集周期
	 */
	public static long getCollectCycle(){
		Properties properties = getProperties();
		String cycle = properties.getProperty("collect.cycle");
		return Long.parseLong(cycle);
	}
	
	/**
	 * 获取 ‘同时开机数量差’ 全场关闭和启动(有的工厂只有白天工作)的临界时间
	 * @return  临界时间
	 */
	public static long getShutdownUpInterval(){
		Properties properties = getProperties();
		String cycle = properties.getProperty("statistics.shutupdown.interval");
		return Long.parseLong(cycle);
	}
	
	
	
	
	//为不同的统计脚本返回不同的脚本实体(只包含ID和统计单位类型)
	public static StatisticsScriptVO getIndexScript(int scriptId){
		StatisticsScriptVO scriptVO = new StatisticsScriptVO(); 
		if(scriptId==ENERGY_SAVING_INDEX){
			scriptVO.setId(ENERGY_SAVING_INDEX);
			scriptVO.setUnitType(StatisticsScriptVO.UnitTypes.STATISTIC_UNIT_FACTORY);
			return scriptVO;
		}else if(scriptId== ENERGY_SAVING_TARGET_NO1){
			scriptVO.setId(ENERGY_SAVING_TARGET_NO1);
			scriptVO.setUnitType(StatisticsScriptVO.UnitTypes.STATISTIC_UNIT_SYSTEM);
			return scriptVO;
		}else if(scriptId== ENERGY_SAVING_TARGET_NO2){
			scriptVO.setId(ENERGY_SAVING_TARGET_NO2);
			scriptVO.setUnitType(StatisticsScriptVO.UnitTypes.STATISTIC_UNIT_SYSTEM);
			return scriptVO;
		}else if(scriptId== ENERGY_SAVING_TARGET_NO3){
			scriptVO.setId(ENERGY_SAVING_TARGET_NO3);
			scriptVO.setUnitType(StatisticsScriptVO.UnitTypes.STATISTIC_UNIT_SYSTEM);
			return scriptVO;
		}else if(scriptId== ENERGY_SAVING_TARGET_NO4){
			scriptVO.setId(ENERGY_SAVING_TARGET_NO4);
			scriptVO.setUnitType(StatisticsScriptVO.UnitTypes.STATISTIC_UNIT_SYSTEM);
			return scriptVO;
		}else if(scriptId== ENERGY_SAVING_TARGET_NO5){
			scriptVO.setId(ENERGY_SAVING_TARGET_NO5);
			scriptVO.setUnitType(StatisticsScriptVO.UnitTypes.STATISTIC_UNIT_SYSTEM);
			return scriptVO;
		}else if(scriptId== INDEX_OF_RESERVE_STORE){
			scriptVO.setId(INDEX_OF_RESERVE_STORE);
			scriptVO.setUnitType(StatisticsScriptVO.UnitTypes.STATISTIC_UNIT_SYSTEM);
			return scriptVO;
		}else if(scriptId== RATE_OF_TROUBLE_HANDLE){
			scriptVO.setId(RATE_OF_TROUBLE_HANDLE);
			scriptVO.setUnitType(StatisticsScriptVO.UnitTypes.STATISTIC_UNIT_SYSTEM);
			return scriptVO;
		}else if(scriptId== INDEX_OF_HEALTH){
			scriptVO.setId(INDEX_OF_HEALTH);
			scriptVO.setUnitType(StatisticsScriptVO.UnitTypes.STATISTIC_UNIT_MACHINE);
			return scriptVO;
		}else{
			return null;
		}
	}
	
	//为不同的统计脚本返回不同的脚本实体(只包含ID和统计单位类型)
		public static StatisticsScriptVO getIndexScript(int scriptId,int cycle){
			StatisticsScriptVO scriptVO = new StatisticsScriptVO(); 
			
			 if(scriptId== INDEX_OF_HEALTH){
				scriptVO.setId(INDEX_OF_HEALTH);
				scriptVO.setUnitType(StatisticsScriptVO.UnitTypes.STATISTIC_UNIT_MACHINE);
				scriptVO.setCycleType(cycle+6);//这里的cycle与Vo中的type差6
				return scriptVO;
			}else if(scriptId== RATE_OF_TROUBLE_HANDLE){
				scriptVO.setId(RATE_OF_TROUBLE_HANDLE);
				scriptVO.setUnitType(StatisticsScriptVO.UnitTypes.STATISTIC_UNIT_MACHINE);
				scriptVO.setCycleType(cycle+6);//这里的cycle与Vo中的type差6
				return scriptVO;
				}
			 else{
				return null;
			}
		}
	
}
