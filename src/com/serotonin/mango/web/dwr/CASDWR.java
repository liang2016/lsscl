package com.serotonin.mango.web.dwr;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import com.serotonin.util.StringUtils;
import com.serotonin.mango.vo.acp.ACPSystemVO;
import com.serotonin.mango.vo.acp.ACPVO;
import com.serotonin.mango.vo.statistics.StatisticsVO;
import com.serotonin.mango.vo.statistics.PointStatistics;
import com.serotonin.mango.vo.DataPointVO;
import java.util.HashMap;
import com.serotonin.mango.db.dao.statistics.StatisticsDao;
import com.serotonin.mango.db.dao.acp.ACPDao;
import com.serotonin.mango.db.dao.acp.CompressedAirSystemDao;
import com.serotonin.web.dwr.DwrResponseI18n;
import com.serotonin.web.i18n.I18NUtils;
import com.serotonin.web.i18n.LocalizableMessage;
/**
 * 压缩空气系统Dwr
 * 
 * @author 刘建坤
 * 
 */
public class CASDWR extends BaseDwr{
	public Map<String, Object> initTree(int scopeId) {
		Map<String, Object> model = new HashMap<String, Object>();
		CompressedAirSystemDao casDao = new CompressedAirSystemDao();
		List<ACPSystemVO> casList = casDao.getACPSystemVOByfactoryId(scopeId);
		/**
		 * 查询所有系统(根据工厂id)
		 */
		model.put("casList", casList);
		/**
		 * 查询工厂中所有空压机
		 */
		List<ACPVO> acpList = casDao.searchACPsByFactoryId(scopeId);
		model.put("acpList", acpList);

		/**
		 * 查询所有工厂系统-中的数据点
		 */
		List<DataPointVO> dpList = casDao.getDataPointsByFactoryId(scopeId);
		model.put("dpList", dpList);
		/**
		 * 查询工厂压缩系统 -空压机- 数据点
		 */
		List<DataPointVO> compressorDpList = casDao
				.getDataPointsByFactoryCompress(scopeId);

		model.put("compressorDpList", compressorDpList);
		return model;
	}

	/**
	 * 根据工厂获得压缩空气系统
	 * 
	 * @param factoryId
	 *            工厂编号
	 * @return
	 */
	public List<ACPSystemVO> getAcmByFactoory(int factoryId) {
		CompressedAirSystemDao casDao = new CompressedAirSystemDao();
		List<ACPSystemVO> casList = casDao.getACPSystemVOByfactoryId(factoryId);
		return casList;
	}

	/**
	 * 根据压缩系统编号获得空压机集合
	 * 
	 * @param casId
	 *            压缩系统编号
	 * @return
	 */
	public List<ACPVO> getACPsByACSId(int compressorId) {
		CompressedAirSystemDao casDao = new CompressedAirSystemDao();
		List<ACPVO> acpList = casDao.getACPsByACSId(compressorId);
		return acpList;
	}

	/**
	 * 根据压缩空气系统获得系统的属性,空压机成员,数据点成员
	 * 
	 * @param systemId
	 *            压缩空气系统
	 * @return
	 */
	public Map<String, Object> getACPSystemAttrById(int scopeId, int systemId) {
		CompressedAirSystemDao casDao = new CompressedAirSystemDao();
		StatisticsDao statisticsDao = new StatisticsDao();
		Map<String, Object> map = new HashMap<String, Object>();
		// 系统中的空压机集合
		List<ACPVO> acpList = casDao.getACPsByACSId(systemId);
		// 系统基本属性
		ACPSystemVO ACPS = casDao.getACPSystemVOById(systemId);
		List<PointStatistics> ps = statisticsDao.getDataPointsByACSId(systemId);
		// 下拉列表准备数据点初始化
		List<DataPointVO> otherPoint = casDao.getDataPointsNotUse(scopeId);
		// 查询系统统计参数
		List<StatisticsVO> statistics = statisticsDao.getSystemStatistics(0);
		List<ACPVO> sc = casDao.getSystemconfig(scopeId);
		map.put("sc", sc);
		map.put("statistics", statistics);
		map.put("acpList", acpList);
		map.put("ps", ps);
		map.put("ACPS", ACPS);
		map.put("otherPoint", otherPoint);
		return map;
	}

	/**
	 * 根据空压机编号获得空压机属性,数据点成员
	 * 
	 * @param acpId
	 * @return
	 */
	public Map<String, Object> getACPById(int scopeId, int acpId) {
		Map<String, Object> map = new HashMap<String, Object>();
		CompressedAirSystemDao casDao = new CompressedAirSystemDao();
		StatisticsDao statisticsDao = new StatisticsDao();

		statisticsDao.checkACPMemberStatistics(acpId);

		ACPDao acpDao = new ACPDao();
		// 查询空压机基本属性
		ACPVO acp = acpDao.findById(acpId);
		// 查询其他数据点
		List<DataPointVO> otherPoint = casDao.getDpByDsByAcpId(scopeId);

		List<PointStatistics> ps = statisticsDao.getDataPointsByACPId(acpId);
		List<PointStatistics> ps2 = statisticsDao.getDataPointsByACPId2(acpId);
		map.put("otherPoint", otherPoint);
		map.put("acp", acp);
		map.put("ps", ps);
		map.put("ps2", ps2);
		// 空压机统计参数
		List<StatisticsVO> statistics = statisticsDao.getSystemStatistics(1);
		map.put("statistics", statistics);
		return map;
	}

	// 添加压缩空气系统初始化
	public Map<String, Object> initAdd(int scopeId) {
		CompressedAirSystemDao casDao = new CompressedAirSystemDao();
		StatisticsDao statisticsDao = new StatisticsDao();
		// 下拉列表准备数据点初始化
		List<DataPointVO> otherPoint = casDao.getDataPointsNotUse(scopeId);
		// 查询系统统计参数
		List<StatisticsVO> statistics = statisticsDao.getSystemStatistics(0);
		Map<String, Object> map = new HashMap<String, Object>();
		/**
		 * 自动生成点的系统
		 */
		List<ACPVO> sc = casDao.getSystemconfig(scopeId);
		map.put("statistics", statistics);
		map.put("otherPoint", otherPoint);
		map.put("sc", sc);
		map.put("xid", casDao.generateUniqueXid());
		return map;
	}

	// /**
	// * 根据空压机编号获得空压机 属性,成员
	// *
	// * @param acpID
	// * @return
	// */
	// public Map<String, Object> getACPAttrById(int acpID) {
	// CompressedAirSystemDao casDao = new CompressedAirSystemDao();
	// Map<String, Object> map = new HashMap<String, Object>();
	// // 下拉列表准备数据点初始化
	// List<DataPointVO> otherPoint = casDao.getDataPointsNotUse(14);
	// // 查询空压机基本属性
	// ACPVO acp = casDao.getACPsById(acpID);
	//
	// map.put("acp", acp);
	// map.put("otherPoint", otherPoint);
	// return map;
	// }

	/**
	 * 根据工厂编号获得所有空压机
	 * 
	 * @param factoryId
	 *            工厂编号
	 * @return
	 */
	public List<ACPVO> getACPsByFactoryId(int factoryId) {
		CompressedAirSystemDao casDao = new CompressedAirSystemDao();
		List<ACPVO> acpList = casDao.getACPsByFactoryId(factoryId);
		return acpList;
	}

	/**
	 * 查询所有工厂系统-中的数据点
	 */
	public List<DataPointVO> getDataPointsByFactoryId(int compressorId) {
		CompressedAirSystemDao casDao = new CompressedAirSystemDao();
		List<DataPointVO> list = casDao.getDataPointsByFactoryId(compressorId);
		return list;
	}

	/**
	 * 根据空压机编号获得 数据点
	 * 
	 * @param compressorId
	 *            空压机编号
	 * @return
	 */
	public List<DataPointVO> getDataPointsByCompressorId(int compressorId) {
		CompressedAirSystemDao casDao = new CompressedAirSystemDao();
		List<DataPointVO> list = casDao
				.getDataPointsByCompressorId(compressorId);
		return list;
	}

	/**
	 * 保存压缩空气系统成员
	 * @param compressedSystemId
	 * @param statistics
	 * @param points
	 * @param compressors
	 * @return
	 */
	public DwrResponseI18n save(int compressedSystemId,  
			int[] statistics, int[] points, int[] compressors) {
		DwrResponseI18n response = new DwrResponseI18n();

		CompressedAirSystemDao casDao = new CompressedAirSystemDao();
		if(statistics.length<1||points.length<1){
			response.addMessage(new LocalizableMessage("acp.point.null"));
		}
		int errorAcpId = casDao.checkSpid(compressors);
		if (errorAcpId > 0) {
			response.addMessage(new LocalizableMessage("acp.statistics.null"));
		} 
		if (!response.getHasMessages()) {
			casDao.updateCompressedSystem(compressedSystemId, statistics, points,
					compressors);
		}
		return response;
	}
	/**
	 * 添加空压机基本数据
	 * @param compressedSystemId
	 * @param acpSystem
	 * @return
	 */
	public DwrResponseI18n saveSystemBase(int compressedSystemId, ACPSystemVO acpSystem) {
		DwrResponseI18n response = new DwrResponseI18n();
		CompressedAirSystemDao casDao = new CompressedAirSystemDao();
		if(StringUtils.isEmpty(acpSystem.getXid()))
			response.addMessage(new LocalizableMessage("validate.required"));
		else if (!casDao.isXidUnique(acpSystem.getXid(), compressedSystemId))
			response.addMessage(new LocalizableMessage("validate.xidUsed"));
		response.addData("acpSystemVo", casDao.saveSystemBanse(compressedSystemId,acpSystem));
		return response;
	}
	/**
	 * 保存空压机成员
	 * 
	 * @param acpId
	 *            空压机编号
	 * @param statistics
	 *            统计参数编号
	 * @param points
	 *            数据点编号
	 */
	public void updateACPMember(int acpId, int[][] update, int[][] add) {
		CompressedAirSystemDao casDao = new CompressedAirSystemDao();
		casDao.updateACPMember(acpId, update, add);
	}
	/**
	 * 清空一个空压机成员
	 * @param acpId
	 */
	public void clearAcpMember(int acpId){
		CompressedAirSystemDao casDao = new CompressedAirSystemDao();	
		casDao.clearAcpMember(acpId);
	}
	/**
	 * 删除压缩空气系统系统,极其成员
	 * 
	 * @param compressedSystemId
	 *            压缩空气系统编号
	 */
	public void deleteCompressedSystem(int compressedSystemId) {
		CompressedAirSystemDao casDao = new CompressedAirSystemDao();
		casDao.deleteCompressedById(compressedSystemId);
	}

	public List<PointStatistics> getSystemDefaultConfig(int acpId) {
		StatisticsDao statisticsDao = new StatisticsDao();
		List<PointStatistics> ps = statisticsDao.getDataPointsByACPId(acpId);
		return ps;
	}
}
