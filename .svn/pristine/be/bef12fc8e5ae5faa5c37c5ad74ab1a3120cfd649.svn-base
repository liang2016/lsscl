package com.serotonin.mango.web.dwr;

import java.util.HashMap;
import com.serotonin.mango.db.dao.PointValueDao;
import com.serotonin.mango.rt.dataImage.PointValueTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;
import java.util.ArrayList;

import net.sf.json.JSONArray;

import com.serotonin.mango.vo.HistoryPointInfo;
import com.serotonin.mango.db.dao.WatchListDao;
import com.serotonin.mango.vo.WatchList;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.Common;
//import com.serotonin.json.JsonArray;
//import net.sf.mbus4j.json.JSONFactory;
public class DynamicAndHistoryPointDwr {
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
		try {
			d = sdf.parse(user_time);
			long l = d.getTime();
			String str = String.valueOf(l);
			re_time = str.substring(0, 10);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Long.parseLong(re_time);
	}

	/**
	 * 获取点设备的历史数据
	 */
	public List<HistoryPointInfo>  getPointHistoryData(int[] pointIds,
			int fromYear, int fromMonth, int fromDay, int fromHour,
			int fromMinute, int fromSecond, int toYear,
			int toMonth, int toDay, int toHour, int toMinute, int toSecond, int firstLimit, int secondLimit) {
		System.out.println(firstLimit+ "----"+secondLimit);
		Date now = new Date();
		System.out.println(now+ "开始");
		// 设置开始时间

		long from = getDateTime(fromYear, fromMonth, fromDay, fromHour,
				fromMinute, fromSecond);
		// 结束时间
		long to = getDateTime(toYear,toMonth, toDay,toHour, toMinute,toSecond);
		// 实例化pointValueDao ,根据参数查询历史数据
		PointValueDao pointValueDao = new PointValueDao();
		// 定义历史数据集合,用于返回到jsp
		List<HistoryPointInfo> hpi = new ArrayList<HistoryPointInfo>();
		List<Integer> pointids = new ArrayList<Integer>();
		// 循环一个点数组
		for (int i = 0; i < pointIds.length; i++) {
			pointids.add(pointIds[i]);
		}

		hpi = pointValueDao.getHistoryPointValuesBetween(pointids, from, to,
				0, secondLimit);
		System.out.println((new Date()) + "-结束");
		System.out.println(hpi.size());
		System.out.println((new Date().getTime() - now.getTime()) + "-耗时");
		System.out.println(hpi.size() / (new Date().getTime() - now.getTime())
				+ "-平均");

		return hpi;
	}

	// 根据观察列表获得表内的点设备数据
	public List<DataPointVO> getPoints(int watchListId) {
		User user = Common.getUser();
		user.setSelectedWatchList(watchListId);
		WatchListDao watchListDao = new WatchListDao();
		WatchList watchList = watchListDao.getWatchList(watchListId);
		user.setWatchList(watchList);
		List<DataPointVO> pointList = watchList.getPointList();
		return pointList;
	}

	// 时间转化
	public long getDateTime(int Year, int Month, int Day, int Hour, int Minute,
			int Second) {
		String dateTime = Year + "/" + Month + "/" + Day + "/" + Hour + ":"
				+ Minute + ":" + Second;
		return getTime(dateTime) * 1000;
	}
  
}
