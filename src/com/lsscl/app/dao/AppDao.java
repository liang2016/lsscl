package com.lsscl.app.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.sql.DataSource;

import com.lsscl.app.bean.QC;
import com.lsscl.app.bean.RSP;
import com.lsscl.app.bean.Scope;
import com.lsscl.app.dao.impl.ScopeRowMapper;
import com.lsscl.app.util.AcpConfig;
import com.lsscl.app.util.StringUtil;
import com.serotonin.mango.db.dao.BaseDao;
import com.lsscl.app.dao.impl.*;

public abstract class AppDao extends BaseDao {
	/**
	 * 更新点报警
	 */
	protected static final String update_mobileEvents = "update mobileEvents set cTime=? where id = ?";

	protected static final String alarm_acps = "select DISTINCT a.id from mobileEvents m "
			+ "left join appPoints p on p.pointId = m.id "
			+ "left join appAcps a on a.id = p.acpId "
			+ "where a.scopeId = ? and m.cTime is not null";
	/**
	 * 空压机列表信息
	 * 
	 */
	protected static final String acp_list = "select a.id,a.name,a.type from appAcps a where a.scopeId = ? order by a.name";
	/**
	 * 空压机信息
	 */
	protected static final String acp_info = "select a.id,a.name,a.power,a.type from appAcps a "
			+ "where a.id = ?";

	/**
	 * 单个空压机属性点
	 */
	protected static final String acp_attr2 = "select p.pointId,p.name from appAcps a "
			+ "left join appPoints p on a.id = p.acpId "
			+ "where a.id = ? order by p.name";
	/**
	 * 单个空压机属性点
	 */
	private static final String acp_attr3 = "select a.id,aa.attrname "
			+ "from aircompressor a "
			+ "left join aircompressor_type at on a.actid = at.id "
			+ "left join aircompressor_type_attr ata on ata.actid = at.id "
			+ "left join aircompressor_attr aa on ata.acaid = aa.id "
			+ "left join aircompressor_members am on am.acaid = aa.id and am.acid = a.id "
			+ "left join statisticsConfiguration ac on ac.acpaid = aa.id "
			+ "left join statisticsParam sp on sp.id = ac.spid "
			+ "where sp.paramname is null " + "and aa.attrname like '报警%' "
			+ "and am.dpid = ?";

	/**
	 * 属性点对应的手机号
	 */
	private static final String phonesByPid = "select u.phone from aircompressor a "
			+ "left join aircompressor_members am on  am.acid = a.id "
			+ "left join scope s on a.factoryid = s.id "
			+ "left join user_scope us on s.id = us.scopeid "
			+ "left join users u on u.id = us.uid " + "where am.dpid = ?";

	/**
	 * 下载域名地址
	 */
	private static final String host_url = AcpConfig.cfg
			.getProperty("host.url");

	/**
	 * 报警历史文件夹相对目录
	 */
	private static final String alarm_files = AcpConfig.cfg
			.getProperty("alarm.files");

	public AppDao() {
		super();
	}

	public AppDao(DataSource dataSource) {
		super(dataSource);
	}

	/**
	 * 根据请求获取响应
	 * 
	 * @param qc
	 * @return
	 */
	public abstract RSP getRSP(QC qc);

	/**
	 * 获取默认app上显示的工厂
	 * 
	 * @param scopeId
	 * @return
	 */
	protected int getDefaultScopeId(String scopeId) {
		List<Scope> scopes = ejt.query(ScopeRowMapper.selectByParentId2,
				new Object[] { scopeId }, new ScopeRowMapper());
		while (scopes.size() != 0) {
			Scope s = scopes.get(0);
			if (s.getType() == 3) {
				return s.getId();
			}
			scopes = ejt.query(ScopeRowMapper.selectByParentId2,
					new Object[] { s.getId() }, new ScopeRowMapper());
		}
		return 0;
	}

	/**
	 * 根据appAcp的id获取功率
	 * 
	 * @param aid
	 * @return
	 */
	protected double getPowerByAid(String aid) {
		double power = 0;
		/**
		 * 计算功率=电流*电压（380）*三相系数（1.732）*功率因数（0.9）
		 */
		Map<String, String> acpInfo = getAcpInfoById(aid + "");
		// 功率
		String current = getPointValueByAcpIdAndName(aid + "",
				AcpConfig.CURRENT);
		current = current != null ? current : "0";
		double d = Double.valueOf(current);
		if (d != 0) {
			power = Double.parseDouble(current) * 380 * 1.732 * 0.9;
		} else {// 无电流计算额定功率
			if(isRun(Integer.parseInt(aid))){
				String ratedPower = acpInfo.get("power");// 额定功率
				power = ratedPower != null ? Double.parseDouble(ratedPower) * 1000
						: 0;
			}
		}
		return power;
	}

	/**
	 * 获取空压机信息
	 * 
	 * @param id
	 * @return
	 */
	protected Map<String, String> getAcpInfoById(String aid) {
		Map<String, String> acpInfo = queryForObject(acp_info,
				new Object[] { aid }, new ResultDataMapper(),
				new HashMap<String, String>());
		return acpInfo;
	}

	/**
	 * 获取空压机名称
	 * 
	 * @param m
	 * @return
	 */
	protected String getAcpName(Map<String, String> m) {
		String acpname = m.get("name");
		String acptype = m.get("type");
		if (acptype != null && acptype.trim().equals("")) {// 型号
			acpname = acpname + " - " + acptype;
		}
		return acpname;
	}

	/**
	 * 查询空压机对应属性名称的值
	 * 
	 * @param aid
	 * @param attrName
	 * @return
	 */
	protected String getPointValueByAcpIdAndName(String aid, String attrName) {
		String getPid = "select pointId from appPoints where acpId = ? and name = ?";
		int pid = queryForObject(getPid, new Object[] { aid, attrName },
				Integer.class, 0);
		if (pid == 0)
			return null;
		String tableName = "pointValues_" + pid;
		if (!isTableExist(tableName))
			return null;
		String getPoint = "select top 1 * from " + tableName
				+ " order by id desc";
		Map<String, String> data = queryForObject(getPoint, new Object[] {},
				new ResultDataMapper(), new HashMap<String, String>());
		// 排气压力保留一位小数
		String pattern = "0.0";
		return StringUtil.formatNumber(data.get("pointValue"), pattern);
	}

	/**
	 * 查询表是否存在
	 * 
	 * @param tableName
	 * @return
	 */
	private boolean isTableExist(String tableName) {
		String sql = "select count(*) from sys.tables where name='" + tableName
				+ "' and type = 'u'";
		int count = queryForObject(sql, new Object[] {}, Integer.class, 0);
		if (count == 1) {
			return true;
		}
		return false;
	}

	/**
	 * 判断空压机运行状态
	 * 
	 * @param id
	 * @return
	 */
	protected boolean isRun(int id) {
		String pv = getPointValueByAcpIdAndName(id + "", AcpConfig.RUN_STOP);
		if (pv != null) {
			double d = Double.valueOf(pv);
			if (d == 1 || d == 108 || d == 76)
				return true;
		} else {
			String current = getPointValueByAcpIdAndName(id + "",
					AcpConfig.CURRENT);
			if (current == null)
				return false;
			double d = Double.valueOf(current);
			if (d > 10) {// 电流大于10
				return true;
			}
		}
		return false;
	}

	/**
	 * 查询是否为基本属性名称
	 * 
	 * @param name
	 * @return
	 */
	protected boolean isBasicPointName(String name) {
		if (AcpConfig.CURRENT.equals(name)
				|| AcpConfig.EXHAUSPRESSURE.equals(name)
				|| AcpConfig.EXHAUSTEMPERATURE.equals(name))
			return true;
		return false;
	}

	/**
	 * 查询并保存报警点
	 * 
	 * @param id
	 */
	public void saveMobileEvent(Integer id) {
		// 查询点为报警点
		String sql = "select top 1 * from pointValues_" + id
				+ " order by id desc";
		Map<String, Object> map = queryForObject(acp_attr3,
				new Object[] { id }, new com.serotonin.mango.vo.ResultData(),
				new HashMap<String, Object>());
		Integer aid = (Integer) map.get("id");
		String attrname = (String) map.get("attrname");
		if (aid != null) {
			//
			Random rd = new Random();
			int i = rd.nextInt(10);
			if (i > 5) {
				ejt.update("insert into pointValues_" + id
						+ "(dataType,pointValue,ts)values(?,?,?)",
						new Object[] { 1, 1, new Date().getTime() });
			} else {
				ejt.update("insert into pointValues_" + id
						+ "(dataType,pointValue,ts)values(?,?,?)",
						new Object[] { 1, 0, new Date().getTime() });
			}

			System.out.println(id + "," + attrname);
		}
	}

	/**
	 * 获取所有在after之后的mobileEvents信息
	 * 
	 * @param after
	 * @return
	 */
	public List<Map<String, Object>> getMobileEvents(long after) {
		String sql = "select id,cTime from mobileEvents";
		List<Map<String, Object>> list = ejt.query(sql, new Object[] {},
				new com.serotonin.mango.vo.ResultData());
		return list;
	}

	/**
	 * 获取点对应的手机号码
	 * 
	 * @param pid
	 * @return
	 */
	public List<String> getPhonesByPid(Integer pid) {
		List<String> phones = queryForList(phonesByPid, new Object[] { pid },
				String.class);
		return phones;
	}

	/*	*//**
	 * 更新报警点
	 * 
	 * @param pid
	 * @param time
	 *            报警创建时间
	 */
	public void updateMobileEvent(Integer pid, Long time) {

		if (time != null) {// 报警时间不为空
			// 表不存在
			if (!isPointExist(pid))
				return;
			String sql = "select top 1 * from pointValues_" + pid
					+ " order by id desc";
			Map<String, Object> point = queryForObject(sql, null,
					new com.serotonin.mango.vo.ResultData(),
					new HashMap<String, Object>());
			// 查询点信息
			// 查询当前报警值
			double data = (Double) point.get("pointValue");
			if (data == 0) {// 无报警(将时间置为空)
				ejt.update(update_mobileEvents, new Object[] { null, pid });
			}
		}
	}

	private boolean isPointExist(Integer pid) {
		String sql = "select count(*) from sys.tables where name='pointValues_"
				+ pid + "' and type = 'u'";
		int count = queryForObject(sql, null, Integer.class, 0);
		if (count == 1)
			return true;
		return false;
	}

	public List<Map<String, String>> getAllScope() {
		String sql = "select * from scope";
		return ejt.query(sql, new Object[] {}, new ResultDataMapper());
	}

	private static final String getScopeAndLevelByActiveTs = "select DISTINCT e.scopeId,e.emailHandler,s.scopename from events e "
			+ "left join scope s on e.scopeId = s.id "
			+ "where s.scopetype=3 "
			+ "and  ( e.ackTs is null or (e.rtnApplicable='Y' and e.rtnTs is null))and e.typeId!=3 and e.typeId!=4 and e.emailHandler is not null "
			+ "and e.activeTs > ?";
	private static final String getScopeParent = "select parentId from scope where id = ?";

	/**
	 * 遍历一段事件后工厂的事件报警，并且查询父区域
	 * 
	 * @param after
	 * @return
	 */
	public Map<String, Set<String>> getScopeEvents(long after) {
		// 存放工厂名称
		Map<String, Set<String>> ret = new HashMap<String, Set<String>>();
		List<Map<String, String>> results = ejt.query(
				getScopeAndLevelByActiveTs, new Object[] { after },
				new ResultDataMapper());
		for (Map<String, String> map : results) {
			ret.put(map.get("scopeId"), new HashSet<String>());
			setScopeEvents(map, ret);
		}
		return ret;
	}

	public void setScopeEvents(Map<String, String> map,
			Map<String, Set<String>> eventScopes) {
		String slevel = map.get("emailHandler");
		String scopeId = map.get("scopeId");
		String scopename = map.get("scopename");
		addScopeNames(eventScopes, scopeId, scopename);
		if (slevel == null)
			return;
		int level = Integer.parseInt(slevel);
		int deep = getDeepByLevel(level);
		for (int i = 0; i < deep; i++) {
			scopeId = queryForObject(getScopeParent, new Object[] { scopeId },
					String.class, null);
			addScopeNames(eventScopes, scopeId, scopename);
		}
	}

	/**
	 * 添加区域下报警工厂名称
	 * 
	 * @param eventScopes
	 * @param scopeId
	 * @param scopename
	 */
	private void addScopeNames(Map<String, Set<String>> eventScopes,
			String scopeId, String scopename) {
		Set<String> set;
		set = eventScopes.get(scopeId);
		set = (set != null) ? set : new HashSet<String>();
		set.add(scopename);
		eventScopes.put(scopeId, set);
	}

	/**
	 * 根据报警级别获取需要向上多少级区域的深度进行推送
	 * 
	 * @param level
	 * @return
	 */
	private int getDeepByLevel(Integer level) {
		if (level == 1) {
			return 3;// 即scopetype为3、2、1、0
		}
		if (level == 2) {
			return 2;// scopetype为3、2、1
		}
		if (level == 3) {
			return 1;// scopetype为3、2
		}
		return 0;
	}

	/**
	 * 保存统计信息
	 * 
	 * @param map
	 */
	private static final String insert_eventStatistics = "insert into eventStatistics (scopeId,L1,L2,L3)values(?,?,?,?)";
	private static final String update_eventStatistics = "update eventStatistics set L1 = ?,L2=?,L3 = ? where scopeId = ?";
	private static final String select_eventStatistics = "select count(*) from eventStatistics where scopeId = ?";

	public void saveOrUpdateEventStatistics(Map<String, String> map) {
		String scopeId = map.get("id");
		String L1 = map.get("L1");
		String L2 = map.get("L2");
		String L3 = map.get("L3");
		L1 = L1 != null ? L1 : "0";
		L2 = L2 != null ? L2 : "0";
		L3 = L3 != null ? L3 : "0";
		int count = queryForObject(select_eventStatistics,
				new Object[] { scopeId }, Integer.class, 0);
		if (count == 1) {
			ejt.update(update_eventStatistics, new Object[] { L1, L2, L3,
					scopeId });
		} else {
			ejt.update(insert_eventStatistics, new Object[] { scopeId, L1, L2,
					L3 });
		}
	}

	public void saveOrUpdateEventStatistics(List<Map<String, String>> maps) {
		for (Map<String, String> m : maps) {
			saveOrUpdateEventStatistics(m);
		}
	}

	/**
	 * 根据scopeId更新报警统计表
	 * 
	 * @param scopeId
	 */
	private static final String select_scopeType_by_id = "select scopetype from scope where id = ?";

	public void saveOrUpdateEventStatistics(String scopeId) {
		int scopeType = queryForObject(select_scopeType_by_id,
				new Object[] { scopeId }, Integer.class, 0);
		if (scopeType != 0) {
			String sql = "select id,L1,L2,L3 from scopeEventCount" + scopeType
					+ " where id = ?";
			Map<String, String> map = queryForObject(sql, new Object[] {},
					new ResultDataMapper());
			saveOrUpdateEventStatistics(map);
		}
	}

	/**
	 * 查询所有报警统计，并且保存到数据库表中
	 */
	private static final String select_eventCountView1 = "select id,L1,L2,L3 from scopeEventCount1";
	private static final String select_eventCountView2 = "select id,L1,L2,L3 from scopeEventCount2";
	private static final String select_eventCountView3 = "select id,L1,L2,L3 from scopeEventCount3";

	public void saveAllEventStatistics() {
//		List<Map<String, String>> map1 = ejt.query(select_eventCountView1,
//				new Object[] {}, new ResultDataMapper());
//		List<Map<String, String>> map2 = ejt.query(select_eventCountView2,
//				new Object[] {}, new ResultDataMapper());
//		List<Map<String, String>> map3 = ejt.query(select_eventCountView3,
//				new Object[] {}, new ResultDataMapper());
//		clearEventStatistics();
//		saveOrUpdateEventStatistics(map1);
//		saveOrUpdateEventStatistics(map2);
//		saveOrUpdateEventStatistics(map3);
	}

	/**
	 * 清空数据
	 */
	private void clearEventStatistics() {
		ejt.update("delete from eventStatistics");
	}

	/**
	 * 模拟一条报警事件
	 * 
	 * @param scopeId
	 * @param level
	 */
	public void makeEvent(int scopeId, int level) {
		String sql = "insert into events (typeId,typeRef1,typeRef2,activeTs,rtnApplicable,emailHandler,scopeId,message,alarmLevel)values(?,?,?,?,?,?,?,?,?)";
		ejt.update(sql, new Object[] { 1, 10000, 100, new Date().getTime(),
				"Y", level, scopeId, "common.default|模拟报警|",level});
	}

	
}
