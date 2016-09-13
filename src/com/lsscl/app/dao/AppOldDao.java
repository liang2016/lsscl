package com.lsscl.app.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import com.lsscl.app.bean.AcpAlarmInfo;
import com.lsscl.app.bean.AlarmInfo;
import com.lsscl.app.bean.AttrNamesMsgBody;
import com.lsscl.app.bean.CompressorDetailsMsgBody;
import com.lsscl.app.bean.CompressorInfo;
import com.lsscl.app.bean.CompressorListMsgBody;
import com.lsscl.app.bean.IndexMsgBody;
import com.lsscl.app.bean.LoginMsgBody;
import com.lsscl.app.bean.MailSenderInfo;
import com.lsscl.app.bean.MsgBody;
import com.lsscl.app.bean.QC;
import com.lsscl.app.bean.RSP;
import com.lsscl.app.dao.impl.MapResultData;
import com.lsscl.app.service.AppService;
import com.lsscl.app.util.AcpConfig;
import com.lsscl.app.util.SimpleMailSender;
import com.lsscl.app.util.StringUtil;
import com.serotonin.mango.Common;
import com.serotonin.mango.DataTypes;
import com.serotonin.mango.db.dao.BaseDao;
import com.serotonin.mango.db.dao.DataPointDao;
import com.serotonin.mango.db.dao.acp.ACPDao;
import com.serotonin.mango.vo.DataPointVO;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class AppOldDao extends BaseDao {
	private static final String MobileLogin = "select count(*) from users where phone = ? and password = ?";
	/**
	 * 空压机总数
	 */
	private static final String acp_total = "select a.id from aircompressor a "
			+ "left join scope s on a.factoryid = s.id "
			+ "left join user_scope us on s.id = us.scopeid "
			+ "left join users u on u.id = us.uid " + "where u.phone = ?";
	/**
	 * 空压机列表信息
	 * 
	 */
	private static final String acp_list = "select a.id,a.acname from aircompressor a "
			+ "left join scope s on a.factoryid = s.id "
			+ "left join user_scope us on s.id = us.scopeid "
			+ "left join users u on u.id = us.uid "
			+ "where u.phone = ? order by a.acname";
	/**
	 * 空压机信息
	 */
	private static final String acp_info = "select a.id,a.acname from aircompressor a "
			+ "left join scope s on a.factoryid = s.id "
			+ "left join user_scope us on s.id = us.scopeid "
			+ "left join users u on u.id = us.uid "
			+ "where a.id = ? and u.phone = ?";
	private static final String user_info = "select u.username,s.scopename from scope s "
			+ "left join user_scope us on s.id = us.scopeid "
			+ "left join users u on u.id = us.uid "
			+ "where u.phone = ? and u.password = ?";
	/**
	 * 首页基本属性点
	 */
	private static final String acp_attr = "select am.dpid "
			+ "from aircompressor a "
			+ "left join aircompressor_type at on a.actid = at.id "
			+ "left join aircompressor_type_attr ata on ata.actid = at.id "
			+ "left join aircompressor_attr aa on ata.acaid = aa.id "
			+ "left join aircompressor_members am on am.acaid = aa.id and am.acid = a.id "
			+ "where a.id = ? ";
	/**
	 * 点值查询
	 */
	private static final String pointValue = "DECLARE @t VARCHAR(200),@aid int ,@attr VARCHAR(20),@pid varchar(20),@acname VARCHAR(200) "
			+ "set @aid = ? "
			+ "set @attr = ? "
			+ "EXEC acpData @aid,@attr,@acname out,@pid out "
			+ "set @t = 'pointValues_'+@pid "
			+ "if @t is not null "
			+ "begin "
			+ "EXECUTE('select top 1 * from '+@t +' order by id desc') "
			+ "end else begin select @t end";
	/**
	 * 反馈信息
	 */
	private static final String contactInfo_insert = "insert into contactInfo (simno,contactInfo,createtime) values"
			+ "                                                                 (?,?,?)";

	/**
	 * 查询单个空压机的报警事件
	 */
	private static final String select_alarmByAcpId = "select m.id,m.cTime,aa.attrname from mobileEvents m "
			+ "left join aircompressor_members am on am.dpid = m.id "
			+ "left join aircompressor a on a.id = am.acid "
			+ "left join aircompressor_attr aa on am.acaid = aa.id "
			+ "where a.id = ? ";

	/**
	 * 上次报警时间
	 */
	private static final String lastAlarmTime = "select top 1 m.cTime from mobileEvents m "
			+ "left join aircompressor_members am on am.dpid = m.id "
			+ "left join aircompressor a on a.id = am.acid "
			+ "left join scope s on a.factoryid = s.id "
			+ "left join user_scope us on s.id = us.scopeid "
			+ "left join users u on u.id = us.uid "
			+ "where u.phone = ? and u.password = ? and m.cTime is not null order by m.cTime desc";

	private static final String alarm_acps = "select DISTINCT a.id from mobileEvents m "
			+ "left join aircompressor_members am on am.dpid = m.id "
			+ "left join aircompressor a on a.id = am.acid "
			+ "left join scope s on a.factoryid = s.id "
			+ "left join user_scope us on s.id = us.scopeid "
			+ "left join users u on u.id = us.uid "
			+ "where u.phone = ? and u.password = ? and m.cTime is not null";
	/**
	 * 单个空压机属性点
	 */
	private static final String acp_attr2 = "select a.acname,aa.id, "
			+ "aa.attrname,am.dpid "
			+ "from aircompressor a "
			+ "left join aircompressor_type at on a.actid = at.id "
			+ "left join aircompressor_type_attr ata on ata.actid = at.id "
			+ "left join aircompressor_attr aa on ata.acaid = aa.id "
			+ "left join aircompressor_members am on am.acaid = aa.id and am.acid = a.id "
			+ "left join statisticsConfiguration ac on ac.acpaid = aa.id "
			+ "left join statisticsParam sp on sp.id = ac.spid "
			+ "where sp.paramname is null " + "and aa.attrname not like '报警%' ";
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
	 * 单个用户下空压机属性名称列表
	 */
	private static final String acp_attr_names = "select DISTINCT aa.id, aa.attrname,am.dpid from aircompressor a "
			+ "left join aircompressor_type at on a.actid = at.id "
			+ "left join aircompressor_type_attr ata on ata.actid = at.id "
			+ "left join aircompressor_attr aa on ata.acaid = aa.id "
			+ "left join aircompressor_members am on am.acaid = aa.id and am.acid = a.id "
			+ "left join statisticsConfiguration ac on ac.acpaid = aa.id "
			+ "left join statisticsParam sp on sp.id = ac.spid "
			+ "left join scope s on a.factoryid = s.id "
			+ "left join user_scope us on s.id = us.scopeid "
			+ "left join users u on u.id = us.uid " + "where u.phone = ?";
	
	/**
	 * 属性点对应的手机号
	 */
	private static final String phonesByPid 
					= "select u.phone from aircompressor a "+ 
						"left join aircompressor_members am on  am.acid = a.id "+
						"left join scope s on a.factoryid = s.id "+ 
						"left join user_scope us on s.id = us.scopeid "+ 	
						"left join users u on u.id = us.uid "+ 	
						"where am.dpid = ?";	

	private static final String update_mobileEvents = "update mobileEvents set cTime=? where id = ?";

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

	public AppOldDao() {
		super();
	}

	public AppOldDao(DataSource dataSource) {
		super(dataSource);
	}

	/**
	 * 登录验证
	 * 
	 * @param qc
	 * @return
	 */
	public RSP mobleLogin(QC qc) {
		String phone = qc.getMsgBody().get("PHONENO");
		String password = qc.getMsgBody().get("PASSWORD");
		String enPwd = Common.encrypt(password);

		RSP rsp = null;
		int count = queryForObject(MobileLogin, new Object[] { phone, enPwd },
				Integer.class, 0);
		if (count == 1) {//
			String sql = "select s.scopeType,u.username from scope s "
					+ "left join user_scope us on s.id = us.scopeId "
					+ "left join users u on us.uid = u.id "
					+ "where u.phone = ? and u.password = ?";
			List<Map<String, String>> maps = ejt.query(sql, new Object[] {
					phone, enPwd }, new MapResultData());
			if (maps.size() > 0) {
				String scopeType = maps.get(0).get("scopeType");
				String username = maps.get(0).get("username");
				rsp = new RSP(qc.getMsgId());
				rsp.setResult(0);
				LoginMsgBody msgBody = new LoginMsgBody();
				if ("3".equals(scopeType)) {
					msgBody.setUserflag(1);// 工厂用户
				} else {// 非工厂用户
					msgBody.setUserflag(2);// 渠道用户
				}
				rsp.setMsgBody(msgBody);
			} else {
				rsp.setResult(1);
				rsp.setError("帐号异常，请联系管理员");
				rsp.setMsgBody(new MsgBody());
			}

		} else if (count == 0) {
			rsp = new RSP(qc.getMsgId());
			rsp.setResult(1);
			rsp.setError("用户名或密码不正确");
			rsp.setMsgBody(new MsgBody());
		} else if (count > 1) {
			rsp = new RSP(qc.getMsgId());
			rsp.setResult(1);
			rsp.setError("帐号异常，请联系管理员");
			rsp.setMsgBody(new MsgBody());
		}
		return rsp;
	}

	/**
	 * 登录首页响应
	 * 
	 * @param qc
	 * @return
	 */
	public RSP mobileIndex(QC qc) {
		String phone = qc.getMsgBody().get("PHONENO");
		String password = qc.getMsgBody().get("PASSWORD");
		String enPwd = Common.encrypt(password);

		RSP rsp = null;
		rsp = new RSP(qc.getMsgId());

		// 查询数据库
		IndexMsgBody msgBody = new IndexMsgBody();
		List<Integer> ids = queryForList(acp_total, new Object[] { phone },
				Integer.class);
		System.out.println("空压机id：" + ids);
		// IndexMsgBody msgBody = queryForObject(sql, new Object[]{phone},
		// rowMapper);
		// 运行停止 id 61[aircompressor_attr]
		// 0:停止 1：运行
		int r = 0, alarmCount = 0;
		double p = 0;
		List<Integer> acpids = ejt.query(alarm_acps, new Object[] { phone,
				enPwd }, Integer.class);
		for (int id : ids) {
			// 运行停止
			
			if (isRun(id)) {// 运行
				
					r++;
					/**
					 * 计算功率=电流*电压（380）*三相系数（1.732）*功率因数（0.9）
					 */
					// 功率

					Map<String,String>data = queryForObject(pointValue, new Object[] { id,
							AcpConfig.CURRENT }, new MapResultData(),
							new HashMap<String, String>());

					if (id == 351) {
						data = queryForObject(
								"select top 1 * from pointValues_6 order by id desc",
								null, new MapResultData(),
								new HashMap<String, String>());
					}
					if (id == 350) {
						data = queryForObject(
								"select top 1 * from pointValues_7 order by id desc",
								null, new MapResultData(),
								new HashMap<String, String>());
					}
					String current = (data.get("pointValue") == null ? "0"
							: data.get("pointValue"));
					double d = Double.valueOf(current);
					if (d != 0) {
						p += Double.parseDouble(current) * 380 * 1.732 * 0.9;
					} else {// 无电流计算额定功率
							p += (new ACPDao().findById(id).getVolume()) * 1000;
					}

				}
			for (Integer aid : acpids) {
				if (id == aid) {
					alarmCount++;
				}
			}
		}

		List<Map<String, String>> maps = ejt.query(user_info, new Object[] {
				phone, enPwd }, new MapResultData());
		if (maps.size() == 1) {
			String username = maps.get(0).get("username");
			String factoryName = maps.get(0).get("scopename");
			msgBody.setUsername(username);
			msgBody.setFactoryName(factoryName);
			msgBody.setTime(StringUtil.formatDate(new Date(),
					"yyyy-MM-dd HH:mm:ss"));
			msgBody.setPower(StringUtil.formatNumber(p / 1000, "0.00"));
			msgBody.setTotal(ids.size());
			msgBody.setOpen(r);
			msgBody.setClose(ids.size() - r);
			msgBody.setAlarm(alarmCount);
			Long time = queryForObject(lastAlarmTime, new Object[] { phone,
					enPwd }, Long.class, 0L);
			if (time != 0) {
				String datetime = StringUtil.formatDate(new Date(time),
						"yyyy-MM-dd HH:mm:ss");
				msgBody.setLastAlarmTime(datetime);
			} else {
				msgBody.setLastAlarmTime(" N/A ");
			}

			rsp.setMsgBody(msgBody);
		} else {
			rsp.setError("帐号异常");
			rsp.setResult(2);
		}
		return rsp;
	}

	/**
	 * 空压机列表
	 * 
	 * @param qc
	 * @return
	 */
	public RSP compressorList(QC qc) {
		long l = System.currentTimeMillis();
		String phone = qc.getMsgBody().get("PHONENO");
		String password = qc.getMsgBody().get("PASSWORD");
		String enPwd = Common.encrypt(password);
		RSP rsp = null;
		rsp = new RSP(qc.getMsgId());
		CompressorListMsgBody msgBody = new CompressorListMsgBody();
		List<Map<String, String>> list = ejt.query(acp_list,
				new Object[] { phone }, new MapResultData());
		List<CompressorInfo> infos = new ArrayList<CompressorInfo>();
		List<Integer> acpids = ejt.query(alarm_acps, new Object[] { phone,
				enPwd }, Integer.class);
		for (Map<String, String> m : list) {
			CompressorInfo info = new CompressorInfo();
			info.setCompressorId(m.get("id"));
			info.setCompressorName(m.get("acname"));
			// 空压机id
			String id = m.get("id");
			// 排气压力
			Map<String, String> data = queryForObject(pointValue, new Object[] {
					id, AcpConfig.EXHAUSPRESSURE }, new MapResultData(),
					new HashMap<String, String>());
			String data1 = StringUtil.formatNumber(data.get("pointValue"), "0");
			info.setExhausPressure(data1 == null ? "-320" : data1);
			// 排气温度
			data = queryForObject(pointValue, new Object[] { id,
					AcpConfig.EXHAUSTEMPERATURE }, new MapResultData(),
					new HashMap<String, String>());
			data1 = StringUtil.formatNumber(data.get("pointValue"), "0");
			info.setExhausTemperature(data1 == null ? "-100" : data1);
			
			info.setAlarmFlag((isRun(Integer.valueOf(id))?"0":"1"));
            
			/**
			 * 遍历所有报警
			 */
			for (Integer aid : acpids) {
				if (("" + aid).equals(id)) {
					info.setAlarmFlag("2");
				}
			}
			infos.add(info);
		}
		msgBody.setCompressorInfos(infos);
		rsp.setMsgBody(msgBody);
		System.out.println("time:" + (System.currentTimeMillis() - l));
		return rsp;
	}



	/**
	 * 空压机属性名称
	 * 
	 * @param qc
	 * @return
	 */
	public RSP compressorAtrrNames(QC qc) {
		String phone = qc.getMsgBody().get("PHONENO");
		String password = qc.getMsgBody().get("PASSWORD");
		String enPwd = Common.encrypt(password);
		List<Map<String, String>> attrs = ejt.query(acp_attr_names,
				new Object[] { phone }, new MapResultData());
		RSP rsp = null;
		rsp = new RSP(qc.getMsgId());
		AttrNamesMsgBody msg = new AttrNamesMsgBody();
		Map<String, String> map = new HashMap<String, String>();
		for (Map<String, String> m : attrs) {
			map.put("T" + m.get("dpid"), m.get("attrname"));
		}
		msg.setAttrs(map);
		rsp.setMsgBody(msg);
		return rsp;
	}
	
	/**
	 * 判断空压机运行状态
	 * @param id
	 * @return
	 */
	private boolean isRun(int id){
		Map<String, String> data = queryForObject(pointValue, new Object[] {
				id, AcpConfig.RUN_STOP }, new MapResultData(),
				new HashMap<String, String>());
		String pv = data.get("pointValue");
		if(pv!=null){
			double d = Double.valueOf(pv);
			if(d==1||d==108||d==76)return true;
		}else{
			if(id!=350&&id!=351){//排除中策两台空压机
				 data = queryForObject(pointValue, new Object[] {
						id, AcpConfig.CURRENT }, new MapResultData(),
						new HashMap<String, String>());
				String current = data.get("pointValue");
				if(current==null)return false;
				double d = Double.valueOf(current);
				if(d>10){//电流大于10
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * 空压机详细(根据手机号码和空压机id查询)
	 * 
	 * @param qc
	 * @return
	 */
	public RSP compressorDetails(QC qc) {
		String phone = qc.getMsgBody().get("PHONENO");
		String compressorId = qc.getMsgBody().get("COMPRESSORID");
		RSP rsp = null;
		rsp = new RSP(qc.getMsgId());
		
		Map<String, String> acpInfo = queryForObject(acp_info, new Object[] {
				compressorId, phone }, new MapResultData(),
				new HashMap<String, String>());
		List<Map<String, Object>> list = ejt.query(acp_attr2 + " and a.id = ?",
				new Object[] { compressorId },
				new com.serotonin.mango.vo.ResultData());
		Map<String, String> dataPoints = new HashMap<String, String>();
		CompressorDetailsMsgBody msg = new CompressorDetailsMsgBody();
		System.out.println(acpInfo);
		msg.setCompressorId(compressorId);
		msg.setCompressorName(acpInfo.get("acname"));
		msg.setTime(StringUtil.getCurrentDate());
		//判断运行停止状态
		boolean isNotRun = !isRun(Integer.valueOf(compressorId));
		/**
		 * 读取点值
		 */
		for (Map<String, Object> m : list) {
			Integer dpid = (Integer) m.get("dpid");
			if (dpid != null) {
				DataPointVO pv = new DataPointDao().getDataPoint(dpid);
				String sql = "select top 1 * from pointValues_" + dpid
						+ " order by ts desc";
				Map<String, Object> point = queryForObject(sql, null,
						new com.serotonin.mango.vo.ResultData(),
						new HashMap<String, Object>());
				Integer type = (Integer) point.get("dataType");
				if (type != null) {// 不为空且不为二进制数据
					String tValue;
					Object obj = point.get("pointValue");
					if (type != DataTypes.BINARY) {
						if (obj instanceof Double) {
							Double d = (Double) obj;
							tValue = pv.getTextRenderer().getText(d, 2);
						} else {
							tValue = pv.getTextRenderer().getText(
									point.get("pointValue") + "", 2);
						}
					} else {
						Double d = (Double) obj;
						Boolean b = (d > 0);
						tValue = pv.getTextRenderer().getText(b, 2);
					}

					String aid = "T" + dpid;
//					if(isNotRun){
//						dataPoints.put(aid, " N/A ");
//					}else{
//						dataPoints.put(aid, tValue);
//					}
					dataPoints.put(aid, tValue);
				}
			}
		}

		// 基本属性点：电流、压力、温度
		Map<String, String> data = queryForObject(pointValue, new Object[] {
				compressorId, AcpConfig.EXHAUSTEMPERATURE }, new MapResultData(),
				new HashMap<String, String>());
		String data1 = StringUtil.formatNumber(data.get("pointValue"), "0");
		msg.setExhausTemperature(data1 == null ? "0" : data1);
		// 排气压力
		data = queryForObject(pointValue, new Object[] { compressorId,
				AcpConfig.EXHAUSPRESSURE }, new MapResultData(),
				new HashMap<String, String>());
		data1 = StringUtil.formatNumber(data.get("pointValue"), "0.00");
		msg.setExhausPressure(data1 == null ? "0" : data1);

		// 电流

		data = queryForObject(pointValue, new Object[] { compressorId,
				AcpConfig.CURRENT }, new MapResultData(),
				new HashMap<String, String>());
		// FIXME 中策
		if ("351".equals(compressorId)) {
			data = queryForObject(
					"select top 1 * from pointValues_6 order by id desc", null,
					new MapResultData(), new HashMap<String, String>());
		} else if ("350".equals(compressorId)) {
			data = queryForObject(
					"select top 1 * from pointValues_7 order by id desc", null,
					new MapResultData(), new HashMap<String, String>());
		}
		data1 = StringUtil.formatNumber(data.get("pointValue"), "0");
		msg.setCurrent(data1 == null ? " N/A " : data1);

		if(isNotRun){//停机
			msg.setExhausTemperature(" N/A ");
			msg.setExhausPressure(" N/A ");
			msg.setCurrent(" N/A ");
		}
		
//		msg.setDataPoints(dataPoints);
		rsp.setMsgBody(msg);
		return rsp;
	}

	/**
	 * 报警推送历史
	 * 
	 * @param qc
	 * @return
	 */
	public RSP alarmHistory(QC qc) {
		RSP rsp = null;
		rsp = new RSP(qc.getMsgId());
		rsp.setRspTime(StringUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		String phone = qc.getMsgBody().get("PHONENO");
		String password = qc.getMsgBody().get("PASSWORD");
		String enPwd = Common.encrypt(password);
		List<Integer> acpIds = queryForList(alarm_acps, new Object[] { phone,
				enPwd }, Integer.class);
		Map<String, AcpAlarmInfo> map = new HashMap<String, AcpAlarmInfo>();
		for (Integer aid : acpIds) {
			AcpAlarmInfo info = new AcpAlarmInfo();
			info.setAid(aid);
			List<AlarmInfo> infos = new ArrayList<AlarmInfo>();
			info.setInfos(infos);
			List<Map<String, Object>> list = ejt.query(select_alarmByAcpId,
					new Object[] { aid },
					new com.serotonin.mango.vo.ResultData());
			for (Map<String, Object> m : list) {
				Integer id = (Integer) m.get("id");
				Long cTime = (Long) m.get("cTime");
				String content = (String) m.get("attrname");
				if (cTime != null) {// 有报警
					// 查询当前报警值
					String sql = "select top 1 * from pointValues_" + id
							+ " order by id desc";
					Map<String, Object> point = queryForObject(sql, null,
							new com.serotonin.mango.vo.ResultData(),
							new HashMap<String, Object>());
					double data = (Double) point.get("pointValue");
					long ts = (Long) point.get("ts");
					if (data == 1) {// 有报警
						AlarmInfo ai = new AlarmInfo();
						ai.setPid(id + "");
						ai.setcTime(cTime + "");
						ai.seteTime(ts + "");
						ai.setContent(content);
						infos.add(ai);
					} else if (data == 0) {// 无报警
						ejt.update(update_mobileEvents,
								new Object[] { null, id });
					}
				}
			}
			map.put(aid + "", info);
		}
		MsgBody msg = new MsgBody();
		msg.setAlarms(map);
		rsp.setMsgBody(msg);
		return rsp;
	}

	/**
	 * 我的反馈
	 * 
	 * @param qc
	 * @return
	 */
	public RSP contactUs(QC qc) {
		String phone = qc.getMsgBody().get("PHONENO");
		String contactText = qc.getMsgBody().get("CONTACTTEXT");
		RSP rsp = new RSP(qc.getMsgId());
		// 服务器
		/**
		 * 将反馈信息存入数据库
		 */
		int i = doInsert(contactInfo_insert, new Object[] { phone, contactText,
				new Date() });
		System.out.println("i:" + i);
		if (i > 0) {
			sendMail(phone, contactText);
			rsp.setError("");
			rsp.setResult(0);
		} else {
			rsp.setError("反馈失败");
			rsp.setResult(1);
		}
		return rsp;
	}

	public void sendMail(String phone, String contactText) {
		// 这个类主要是设置邮件
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost("www.lsscl.com");
		mailInfo.setMailServerPort("25");
		mailInfo.setValidate(true);
		mailInfo.setUserName("lsscl@lsscl.com");
		mailInfo.setPassword("window");// 您的邮箱密码
		mailInfo.setFromAddress("lsscl@lsscl.com");
		mailInfo.setToAddress("service@1kw.com.cn");
		mailInfo.setSubject("App反馈(" + phone + ")");
		mailInfo.setContent("反馈内容：<br>" + contactText);
		// 这个类主要来发送邮件
		SimpleMailSender sms = new SimpleMailSender();
		sms.sendHtmlMail(mailInfo);// 发送html格式
	}

	/**
	 * 退出登录
	 * 
	 * @param qc
	 * @return
	 */
	public RSP mobleLogout(QC qc) {
		/**
		 * 服务器注销帐号
		 */
		RSP rsp = new RSP(qc.getMsgId());
		rsp.setError("");
		rsp.setResult(0);
		return rsp;
	}

	/**
	 * 下载报警历史
	 * 
	 * @param qc
	 * @param req
	 * @return
	 */
	public RSP alarmHistoryDownload(QC qc, HttpServletRequest req) {
		String phone = qc.getMsgBody().get("PHONENO");
		RSP rsp = alarmHistory(qc);
		String fileName = null;
		try {
			String realPath = req.getServletContext().getRealPath("/");
			File file = new File(realPath + alarm_files);
			if (!file.exists())
				file.mkdirs();
			Configuration cfg = new Configuration();
			cfg.setServletContextForTemplateLoading(req.getServletContext(),
					"/WEB-INF/templates");
			Template temp = cfg.getTemplate("RSP.xml");
			Map root = new HashMap();
			rsp.setMsgId(AppService.ALARM_HISTORY);
			root.put("rsp", rsp);
			fileName = phone + StringUtil.getRandomString() + ".xml";
			PrintWriter out = new PrintWriter(new OutputStreamWriter(
					new FileOutputStream(new File(file, fileName)), "utf-8"));
			temp.process(root, out);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		rsp.setMsgBody(null);
		if (fileName != null) {
			String url = host_url + alarm_files + "/" + fileName;
			rsp.setMsgBody(new MsgBody(url));
		} else {
			rsp.setResult(1);
			rsp.setError("下载出错");
		}
		rsp.setMsgId(AppService.ALARM_HISTORY_DOWNLOAD);
		return rsp;
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
	 * 获取所有mobileEvents信息
	 * @return
	 */
	public List<Map<String, Object>> getMobileEvents() {
	    String sql = "select id,cTime from mobileEvents";
	    List<Map<String, Object>> list = ejt.query(sql,new Object[]{},new com.serotonin.mango.vo.ResultData());
		return list;
	}

	/**
	 * 获取点对应的手机号码
	 * @param pid
	 * @return
	 */
	public List<String> getPhonesByPid(Integer pid) {
		List<String> phones = queryForList(phonesByPid,new Object[]{pid},String.class);
		return phones;
	}

	/**
	 * 更新报警点
	 * @param pid
	 * @param time 报警创建时间
	 */
	public void updateMobileEvent(Integer pid, Long time) {
		
		if(time!=null){//报警时间不为空
			//表不存在
			if(!isPointExist(pid))return;
			String sql = "select top 1 * from pointValues_" + pid
					+ " order by id desc";
			Map<String, Object> point = queryForObject(sql, null,
					new com.serotonin.mango.vo.ResultData(),
					new HashMap<String, Object>());
			//查询点信息
			// 查询当前报警值
			double data = (Double) point.get("pointValue");
			if (data == 0) {// 无报警(将时间置为空)
				ejt.update(update_mobileEvents,
						new Object[] { null, pid });
			}
		}
	}

	private boolean isPointExist(Integer pid) {
		String sql = "select count(*) from sys.tables where name='pointValues_"+pid+"' and type = 'u'";
		int count = queryForObject(sql, null,Integer.class, 0);
		if(count==1)return true;
		return false;
	}

}


