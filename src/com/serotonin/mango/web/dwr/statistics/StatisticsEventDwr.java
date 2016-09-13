package com.serotonin.mango.web.dwr.statistics;

import com.serotonin.mango.vo.User;
import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.scope.ScopeDao;
import com.serotonin.mango.web.dwr.BaseDwr;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.serotonin.mango.vo.scope.ScopeVO;
import com.serotonin.mango.db.dao.UserDao;
public class StatisticsEventDwr extends BaseDwr {
	public Map<String, Integer> getEventCount(int scopeId, int scopeType,
			boolean nowDay, boolean selectDay, String startTsStr,
			boolean fromNone, String endTsStr, boolean toNone, int emailHandler) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		long startTs = 0;
		long endTs = 0;
		Date dt = new Date();
		if (nowDay) {
			startTs = dt.getTime() - 1000 * 60 * 60 * 24;
			endTs = dt.getTime();
			fromNone=false;
			toNone=false;
		}
		if (selectDay) {
			startTs = getTime(startTsStr);
			endTs = getTime(endTsStr);
		}
		ScopeDao scopeDao = new ScopeDao();
		map = scopeDao.getEventCountByScope(scopeType, scopeId, startTs,
				fromNone, endTs, toNone, emailHandler);
		return map;
	}

	public long getDateTime(int Year, int Month, int Day, int Hour, int Minute,
			int Second) {
		String dateTime = Year + "/" + Month + "/" + Day + "/" + Hour + ":"
				+ Minute + ":" + Second;
		return getTime(dateTime) * 1000;
	}

	/**
	 * 首先将日期转换为时间戳long
	 * 
	 * @param user_time
	 * @return
	 */
	public static long getTime(String user_time) {
		String re_time = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss");
		Date d;
		long l=0l;
		try {
			d = sdf.parse(user_time);
			l = d.getTime();
			//String str = String.valueOf(l);
			//re_time = str.substring(0, 10);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return l;//Long.parseLong(re_time);
	}

	/**
	 * 加载区域
	 * 
	 * @return
	 */
	public List<ScopeVO> getZones() {
		User user = Common.getUser();
		ScopeDao scopeDao = new ScopeDao();
		List<ScopeVO> list;
		if (user.isAdmin()) {
			list = scopeDao.getZoneList();
		} else {
			list = scopeDao.getZoneByNormalUser(user.getId(), 1);
		}
		return list;
	}

	/**
	 * 查询一个区域下的子区域
	 * 
	 * @param zId
	 *            区域编号
	 * @return
	 */
	public List<ScopeVO> getSubZonesByZId(int zId) {
		ScopeDao zoneDao = new ScopeDao();
		List<ScopeVO> list = zoneDao.getsubZoneList(zId);
		return list;
	}

	/**
	 * 加载子区域
	 * @return
	 */
	public List<ScopeVO> getAllSubZones() {
		User user = Common.getUser();
		int zId = user.getCurrentScope().getId();
		ScopeDao zoneDao = new ScopeDao();
		List<ScopeVO> list=new ArrayList<ScopeVO>();
		if (user.isAdmin()) {// 是管理员查询所有
			if(user.getCurrentScope().getScopetype()==0){//总部
				list=zoneDao.getsubZoneList();
			}
			else{
				list = zoneDao.getsubZoneList(zId);
			}
		} else if (!user.isAdmin() && user.getHomeScope().getScopetype()==0) {// 是总部的普通用户也查询所有
			if(user.getCurrentScope().getScopetype()==0){//在总部进行的搜索//这里要查询总部普通用户对那几个区域有权限
				List<ScopeVO> zoneScope=zoneDao.getScopesByUser(user.getId());//查询出用户管理的区域
				for (int i = 0; i < zoneScope.size(); i++) {
					list.addAll(zoneDao.getsubZoneList(zoneScope.get(i).getId()));					
				}
			}
			else{//区域进行的搜索
				list = zoneDao.getsubZoneList(zId);//
			}
		}
		else {
			list = zoneDao.getScopesByUser(user.getId());
		}
		return list;
	}
	/**
	 * 查询工厂
	 * @return
	 */
	public List<ScopeVO> getFactories() {
		User user = Common.getUser();
		ScopeDao dao = new ScopeDao();
		int sId = user.getCurrentScope().getId();
		List<ScopeVO> list =new ArrayList<ScopeVO>();
		if (user.isAdmin()) {// 是管理员查询所有
			if(user.getCurrentScope().getScopetype()==0){//总部
				list=dao.getFactoryByHq();
			}
			else if(user.getCurrentScope().getScopetype()==1){//区域
				list=dao.getFactoryByZone(user.getCurrentScope().getId());
			}
			else{
				list = dao.getFactoryBySubZone(sId);
			}
		} else if (!user.isAdmin()) {// 是总部的普通用户也查询所有
			if(user.getCurrentScope().getScopetype() == 0){//总部的普通用户→查询管理的区域
				List<ScopeVO> zoneScope=dao.getScopesByUser(user.getId());//查询出用户管理的区域
				for (int i = 0; i < zoneScope.size(); i++) {
					ScopeVO zone=zoneScope.get(i);
					List<ScopeVO> factory=dao.getFactoryByZone(zone.getId());	
					list.addAll(factory);
				}
			}
			else if(user.getCurrentScope().getScopetype()==1){//当前范围是区域.查询管理的子区域
				list=dao.getFactoryByZone(sId);
			}
			else{
				list = dao.getFactoryBySubZone(sId);	
			}
		} else {// 
			list = dao.getScopesByUser(user.getId());
		}
		return list;
	}
}
