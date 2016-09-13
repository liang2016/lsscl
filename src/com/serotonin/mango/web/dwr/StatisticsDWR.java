package com.serotonin.mango.web.dwr;

import java.util.List;
import java.util.ArrayList;
import com.serotonin.mango.db.dao.statistics.StatisticsDao;
import com.serotonin.mango.vo.statistics.StatisticsVO;
import com.serotonin.web.dwr.DwrResponseI18n;
import com.serotonin.web.i18n.LocalizableMessage;
import com.serotonin.mango.vo.statistics.ACPAttrStatisticsVO;
import com.serotonin.mango.db.dao.statistics.ACPStatisticsDao;
import com.serotonin.mango.vo.acp.ACPAttrVO;
import com.serotonin.web.dwr.DwrResponseI18n;
import com.serotonin.mango.vo.dataSource.modbus.ModbusPointLocatorVO;

/**
 * 压缩空气系统,空压机配置
 * 
 * @author 刘建坤
 * 
 */
public class StatisticsDWR extends BaseDwr {
	/**
	 * 获得压缩空气系统统计配置
	 * 
	 * @return
	 */
	public List<StatisticsVO> getACPSystemConfig() {
		StatisticsDao statisticsDao = new StatisticsDao();
		List<StatisticsVO> acpSystemConfigList = statisticsDao
				.getStatisticsConfig(0);
		return acpSystemConfigList;
	}

	/**
	 * 获得空压机统计配置
	 * 
	 * @return
	 */
	public List<StatisticsVO> getACPConfig() {
		StatisticsDao statisticsDao = new StatisticsDao();
		List<StatisticsVO> acpListConfig = statisticsDao.getStatisticsConfig(1);
		return acpListConfig;
	}

	/**
	 * 根据id获得统计参数配置
	 * 
	 * @param id
	 * @return
	 */
	public StatisticsVO getStatisticsConfigById(int id) {
		StatisticsVO statistics = new StatisticsVO();
		StatisticsDao statisticsDao = new StatisticsDao();
		statistics = statisticsDao.getStatisticsConfigById(id);
		return statistics;
	}

	/**
	 * 初始化统计配置数据
	 * 
	 * @return
	 */
	public DwrResponseI18n initConfig() {
		DwrResponseI18n response = new DwrResponseI18n();
		StatisticsDao statisticsDao = new StatisticsDao();
		List<StatisticsVO> acpListConfig = getACPSystemConfig();
		List<StatisticsVO> acpSystemConfigList = getACPConfig();
		response.addData("acpSystemConfigList", acpListConfig);
		response.addData("acpListConfig", acpSystemConfigList);
		return response;
	}

	/**
	 * 保存统计参数配置
	 * 
	 * @param id
	 * @param useType
	 * @param dataType
	 * @param paramname
	 */
	public DwrResponseI18n saveStatisticsConfig(int id, int useType,
			int dataType, String paramname) {
		StatisticsVO statisticsVo=new StatisticsVO(id,paramname,dataType,useType);
		DwrResponseI18n response = new DwrResponseI18n();
		StatisticsDao statisticsDao = new StatisticsDao();
		statisticsVo=statisticsDao.saveStatisticsConfig(statisticsVo);
		response.addData("statisticsVo",statisticsVo);
		return response;
	}

	/**
	 * 根据编号删除一个统计参数配置
	 * 
	 * @param id
	 *            编号
	 * @return
	 */
	public DwrResponseI18n deleteStatisticsConfig(int id) {
		DwrResponseI18n response = new DwrResponseI18n();
		StatisticsDao statisticsDao = new StatisticsDao();
		if(id<9)
			response.addMessage(new LocalizableMessage("config.hasUsed"));
		else{
			if (statisticsDao.cheeckConfigIsUsed(id)) {
				statisticsDao.deleteStatisticsConfig(id);
			} else {
				response.addMessage(new LocalizableMessage("config.hasUsed"));
			}
		}	
		return response;
	}
}
