package com.lsscl.app.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class AcpConfig {
	public static Properties cfg; 
	static {
		cfg = new Properties();
		InputStream in = AcpConfig.class.getResourceAsStream("/acpConfig.properties");
		try {
			cfg.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 运行/停止
	 */
	public static final String RUN_STOP = cfg.getProperty("RUN_STOP");
	/**
	 * 排气温度
	 */
	public static final String EXHAUSTEMPERATURE = cfg.getProperty("EXHAUSTEMPERATURE");
	/**
	 * 排气压力
	 */
	public static final String EXHAUSPRESSURE = cfg.getProperty("EXHAUSPRESSURE");
	
    /**
     * 0=正常运行；1=停机；2=有警报
     */
	public static final String ALARMFLAG = cfg.getProperty("ALARMFLAG");
	
	/**
	 * 功率
	 */
	public static final String POWER = cfg.getProperty("POWER");
	
	/**
	 * 电流
	 */
	public static final String CURRENT = cfg.getProperty("CURRENT");
	
	/**
	 * 设定温度
	 */
	public static final String SETTEMPERATURE = cfg.getProperty("SETTEMPERATURE");
	
	/**
	 * 压力上限
	 */
	public static final String PRESSURECUP = cfg.getProperty("PRESSURECUP");
	
	/**
	 * 压力下限
	 */
	public static final String PRESSURELIMIT = cfg.getProperty("PRESSURELIMIT");
	
	/**
	 * 运行时间
	 */
	public static final String TIME_RUN = cfg.getProperty("TIME_RUN");
	
	/**
	 * 加载时间
	 */
	public static final String TIME_LOAD = cfg.getProperty("TIME_LOAD");
	
	/**
	 * 下次保养时间
	 */
	public static final String TIME_NEXTMAINTENANCETIME = cfg.getProperty("TIME_NEXTMAINTENANCETIME");
	
	/**
	 * 油分
	 */
	public static final String PRESSUER_OIL = cfg.getProperty("PRESSUER_OIL");
	
	/**
	 * 油滤
	 */
	public static final String PRESSUER_OILFILTER = cfg.getProperty("PRESSUER_OILFILTER");
	
	/**
	 * 空滤
	 */
	public static final String PRESSUER_AIRFILTER = cfg.getProperty("PRESSUER_AIRFILTER");
	/**
	 * 加载/卸载
	 */
	public static final String Load_UnLoad = cfg.getProperty("Load_UnLoad");;
}
