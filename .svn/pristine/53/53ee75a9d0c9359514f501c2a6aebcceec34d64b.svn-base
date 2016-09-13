/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.dwr;

import com.serotonin.web.dwr.DwrResponseI18n;
import com.serotonin.mango.rt.statistic.common.StatisticsUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import com.serotonin.mango.vo.HistoryPointInfo;
import org.directwebremoting.WebContextFactory;
import org.joda.time.DateTime;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import com.serotonin.db.IntValuePair;
import com.serotonin.mango.Common;
import com.serotonin.mango.DataTypes;
import com.serotonin.mango.db.dao.DataPointDao;
import com.serotonin.mango.db.dao.UserDao;
import com.serotonin.mango.db.dao.WatchListDao;
import com.serotonin.mango.rt.RuntimeManager;
import com.serotonin.mango.rt.dataImage.DataPointRT;
import com.serotonin.mango.rt.dataImage.PointValueTime;
import com.serotonin.mango.rt.dataImage.types.ImageValue;
import com.serotonin.mango.view.ShareUser;
import com.serotonin.mango.vo.DataPointExtendedNameComparator;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.vo.WatchList;
import com.serotonin.mango.vo.hierarchy.PointHierarchy;
import com.serotonin.mango.vo.permission.Permissions;
import com.serotonin.mango.web.dwr.beans.DataExportDefinition;
import com.serotonin.mango.web.dwr.beans.WatchListState;
import com.serotonin.mango.web.taglib.Functions;
import com.serotonin.util.ArrayUtils;
import com.serotonin.util.ObjectUtils;
import com.serotonin.web.dwr.MethodFilter;
import com.serotonin.web.i18n.LocalizableMessage;
import com.serotonin.mango.db.dao.PointValueDao;
public class WatchListDwr extends BaseDwr {
    public Map<String, Object> init() {
        DataPointDao dataPointDao = new DataPointDao();
        Map<String, Object> data = new HashMap<String, Object>();
        User user = Common.getUser();
        PointHierarchy ph = dataPointDao.getPointHierarchy(user.getCurrentScope().getId()).copyFoldersOnly();
        List<DataPointVO> points = dataPointDao.getDataPoints(user.getCurrentScope().getId(),DataPointExtendedNameComparator.instance, false);
        for (DataPointVO point : points) {
            if (Permissions.hasDataPointReadPermission(user, point))
                ph.addDataPoint(point.getId(), point.getPointFolderId(), point.getExtendedName());
        }

        ph.parseEmptyFolders();

        WatchList watchList = new WatchListDao().getWatchList(user.getSelectedWatchList());
        prepareWatchList(watchList, user);
        user.setWatchList(watchList);

        data.put("pointFolder", ph.getRoot());
        data.put("shareUsers", getShareUsersByScope(user));
        data.put("selectedWatchList", getWatchListData(user, watchList));

        return data;
    }

    /**
     * Retrieves point state for all points on the current watch list.
     * 
     * @param pointIds
     * @return
     */
    public List<WatchListState> getPointData() {
        // Get the watch list from the user's session. It should have been set by the controller.
        return getPointDataImpl(Common.getUser().getWatchList());
    }

    private List<WatchListState> getPointDataImpl(WatchList watchList) {
        if (watchList == null)
            return new ArrayList<WatchListState>();

        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        User user = Common.getUser(request);

        RuntimeManager rtm = Common.ctx.getRuntimeManager();

        WatchListState state;
        List<WatchListState> states = new ArrayList<WatchListState>(watchList.getPointList().size());
        Map<String, Object> model = new HashMap<String, Object>();
        for (DataPointVO point : watchList.getPointList()) {
            // Create the watch list state.
            state = createWatchListState(request, point, rtm, model, user);
            states.add(state);
        }

        return states;
    }

    public void updateWatchListName(String name) {
        User user = Common.getUser();
        WatchList watchList = user.getWatchList();
        Permissions.ensureWatchListEditPermission(user, watchList);
        watchList.setName(name);
        new WatchListDao().saveWatchList(watchList);
    }

    public IntValuePair addNewWatchList(int copyId) {
        User user = Common.getUser();

        WatchListDao watchListDao = new WatchListDao();
        WatchList watchList;

        if (copyId == Common.NEW_ID) {
            watchList = new WatchList();
            watchList.setName(getMessage("common.newName"));
        }
        else {
            watchList = new WatchListDao().getWatchList(user.getSelectedWatchList());
            watchList.setId(Common.NEW_ID);
            watchList.setName(getMessage(new LocalizableMessage("common.copyPrefix", watchList.getName())));
        }
        watchList.setUserId(user.getId());
        watchList.setXid(watchListDao.generateUniqueXid());
        watchList.setFactoryId(user.getCurrentScope().getId());
        watchListDao.saveWatchList(watchList);

        user.setSelectedWatchList(watchList.getId());
        user.setWatchList(watchList);

        watchListDao.saveSelectedWatchList(user.getId(), watchList.getId());

        return new IntValuePair(watchList.getId(), watchList.getName());
    }

    public void deleteWatchList(int watchListId) {
        User user = Common.getUser();

        WatchListDao watchListDao = new WatchListDao();
        WatchList watchList = user.getWatchList();
        if (watchList == null || watchListId != watchList.getId())
            watchList = watchListDao.getWatchList(watchListId);

        if (watchList == null || watchListDao.getWatchLists(user.getId()).size() == 1)
            // Only one watch list left. Leave it.
            return;

        // Allow the delete.
        if (watchList.getUserAccess(user) == ShareUser.ACCESS_OWNER)
            watchListDao.deleteWatchList(watchListId);
        else
            watchListDao.removeUserFromWatchList(watchListId, user.getId());
    }

    public Map<String, Object> setSelectedWatchList(int watchListId) {
        User user = Common.getUser();

        WatchListDao watchListDao = new WatchListDao();
        WatchList watchList = watchListDao.getWatchList(watchListId);
        Permissions.ensureWatchListPermission(user, watchList);
        prepareWatchList(watchList, user);

        watchListDao.saveSelectedWatchList(user.getId(), watchList.getId());
        user.setSelectedWatchList(watchListId);

        Map<String, Object> data = getWatchListData(user, watchList);
        // Set the watchlist in the user object after getting the data since it may take a while, and the long poll
        // updates will all be missed in the meantime.
        user.setWatchList(watchList);

        return data;
    }

    public WatchListState addToWatchList(int pointId) {
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        User user = Common.getUser();
        DataPointVO point = new DataPointDao().getDataPoint(pointId);
        if (point == null)
            return null;
        WatchList watchList = user.getWatchList();

        // Check permissions.
        Permissions.ensureDataPointReadPermission(user, point);
        Permissions.ensureWatchListEditPermission(user, watchList);

        // Add it to the watch list.
        watchList.getPointList().add(point);
        new WatchListDao().saveWatchList(watchList);
        updateSetPermission(point, watchList.getUserAccess(user), new UserDao().getUser(watchList.getUserId()));

        // Return the watch list state for it.
        return createWatchListState(request, point, Common.ctx.getRuntimeManager(), new HashMap<String, Object>(), user);
    }

    public void removeFromWatchList(int pointId) {
        // Remove the point from the user's list.
        User user = Common.getUser();
        WatchList watchList = user.getWatchList();
        Permissions.ensureWatchListEditPermission(user, watchList);
        for (DataPointVO point : watchList.getPointList()) {
            if (point.getId() == pointId) {
                watchList.getPointList().remove(point);
                break;
            }
        }
        new WatchListDao().saveWatchList(watchList);
    }

    public void moveUp(int pointId) {
        User user = Common.getUser();
        WatchList watchList = user.getWatchList();
        Permissions.ensureWatchListEditPermission(user, watchList);
        List<DataPointVO> points = watchList.getPointList();

        DataPointVO point;
        for (int i = 0; i < points.size(); i++) {
            point = points.get(i);
            if (point.getId() == pointId) {
                points.set(i, points.get(i - 1));
                points.set(i - 1, point);
                break;
            }
        }

        new WatchListDao().saveWatchList(watchList);
    }

    public void moveDown(int pointId) {
        User user = Common.getUser();
        WatchList watchList = user.getWatchList();
        Permissions.ensureWatchListEditPermission(user, watchList);
        List<DataPointVO> points = watchList.getPointList();

        DataPointVO point;
        for (int i = 0; i < points.size(); i++) {
            point = points.get(i);
            if (point.getId() == pointId) {
                points.set(i, points.get(i + 1));
                points.set(i + 1, point);
                break;
            }
        }

        new WatchListDao().saveWatchList(watchList);
    }

    /**
     * Convenience method for creating a populated view state.
     */
    private WatchListState createWatchListState(HttpServletRequest request, DataPointVO pointVO, RuntimeManager rtm,
            Map<String, Object> model, User user) {
        // Get the data point status from the data image.
        DataPointRT point = rtm.getDataPoint(pointVO.getId());

        WatchListState state = new WatchListState();
        state.setId(Integer.toString(pointVO.getId()));

        PointValueTime pointValue = prepareBasePointState(Integer.toString(pointVO.getId()), state, pointVO, point,
                model);
        setEvents(pointVO, user, model);
        if (pointValue != null && pointValue.getValue() instanceof ImageValue) {
            // Text renderers don't help here. Create a thumbnail.
            setImageText(request, state, pointVO, model, pointValue);
        }
        else
            setPrettyText(state, pointVO, model, pointValue);

        if (pointVO.isSettable())
            setChange(pointVO, state, point, request, model, user);

        if (state.getValue() != null)
            setChart(pointVO, state, request, model);
        setMessages(state, request, "watchListMessages", model);

        return state;
    }

    private void setImageText(HttpServletRequest request, WatchListState state, DataPointVO pointVO,
            Map<String, Object> model, PointValueTime pointValue) {
        if (!ObjectUtils.isEqual(pointVO.lastValue(), pointValue)) {
            state.setValue(generateContent(request, "imageValueThumbnail.jsp", model));
            if (pointValue != null)
                state.setTime(Functions.getTime(pointValue));
            pointVO.updateLastValue(pointValue);
        }
    }

    /**
     * Method for creating image charts of the points on the watch list.
     */
    public String getImageChartData(int[] pointIds, int fromYear, int fromMonth, int fromDay, int fromHour,
            int fromMinute, int fromSecond, boolean fromNone, int toYear, int toMonth, int toDay, int toHour,
            int toMinute, int toSecond, boolean toNone, int width, int height) {
        DateTime from = createDateTime(fromYear, fromMonth, fromDay, fromHour, fromMinute, fromSecond, fromNone);
        DateTime to = createDateTime(toYear, toMonth, toDay, toHour, toMinute, toSecond, toNone);

        StringBuilder htmlData = new StringBuilder();
        htmlData.append("<img src=\"achart/ft_");
        htmlData.append(System.currentTimeMillis());
        htmlData.append('_');
        htmlData.append(fromNone ? -1 : from.getMillis());
        htmlData.append('_');
        htmlData.append(toNone ? -1 : to.getMillis());

        boolean pointsFound = false;
        // Add the list of points that are numeric.
        List<DataPointVO> watchList = Common.getUser().getWatchList().getPointList();
        for (DataPointVO dp : watchList) {
            int dtid = dp.getPointLocator().getDataTypeId();
            if ((dtid == DataTypes.NUMERIC || dtid == DataTypes.BINARY || dtid == DataTypes.MULTISTATE)
                    && ArrayUtils.contains(pointIds, dp.getId())) {
                pointsFound = true;
                htmlData.append('_');
                htmlData.append(dp.getId());
            }
        }

        if (!pointsFound)
            // There are no chartable points, so abort the image creation.
            return getMessage("watchlist.noChartables");

        htmlData.append(".png?w=");
        htmlData.append(width);
        htmlData.append("&h=");
        htmlData.append(height);
        htmlData.append("\" alt=\"" + getMessage("common.imageChart") + "\"/>");

        return htmlData.toString();
    }

    private Map<String, Object> getWatchListData(User user, WatchList watchList) {
        Map<String, Object> data = new HashMap<String, Object>();
        if (watchList == null)
            return data;

        List<DataPointVO> points = watchList.getPointList();
        List<Integer> pointIds = new ArrayList<Integer>(points.size());
        for (DataPointVO point : points) {
            if (Permissions.hasDataPointReadPermission(user, point))
                pointIds.add(point.getId());
        }

        data.put("points", pointIds);
        data.put("users", watchList.getWatchListUsers());
        data.put("access", watchList.getUserAccess(user));

        return data;
    }

    private void prepareWatchList(WatchList watchList, User user) {
        int access = watchList.getUserAccess(user);
        User owner = new UserDao().getUser(watchList.getUserId());
        for (DataPointVO point : watchList.getPointList())
            updateSetPermission(point, access, owner);
    }

    private void updateSetPermission(DataPointVO point, int access, User owner) {
        // Point isn't settable
        if (!point.getPointLocator().isSettable())
            return;

        // Read-only access
        if (access != ShareUser.ACCESS_OWNER && access != ShareUser.ACCESS_SET)
            return;

        // Watch list owner doesn't have set permission
        if (!Permissions.hasDataPointSetPermission(owner, point))
            return;

        // All good.
        point.setSettable(true);
    }

    //
    // Share users
    //
    @MethodFilter
    public List<ShareUser> addUpdateSharedUser(int userId, int accessType) {
        WatchList watchList = Common.getUser().getWatchList();
        boolean found = false;
        for (ShareUser su : watchList.getWatchListUsers()) {
            if (su.getUserId() == userId) {
                found = true;
                su.setAccessType(accessType);
                break;
            }
        }

        if (!found) {
            ShareUser su = new ShareUser();
            su.setUserId(userId);
            su.setAccessType(accessType);
            watchList.getWatchListUsers().add(su);
        }

        new WatchListDao().saveWatchList(watchList);

        return watchList.getWatchListUsers();
    }

    @MethodFilter
    public List<ShareUser> removeSharedUser(int userId) {
        WatchList watchList = Common.getUser().getWatchList();

        for (ShareUser su : watchList.getWatchListUsers()) {
            if (su.getUserId() == userId) {
                watchList.getWatchListUsers().remove(su);
                break;
            }
        }

        new WatchListDao().saveWatchList(watchList);

        return watchList.getWatchListUsers();
    }

    @MethodFilter
    public void getChartData(int[] pointIds, int fromYear, int fromMonth, int fromDay, int fromHour, int fromMinute,
            int fromSecond, boolean fromNone, int toYear, int toMonth, int toDay, int toHour, int toMinute,
            int toSecond, boolean toNone) {
        DateTime from = createDateTime(fromYear, fromMonth, fromDay, fromHour, fromMinute, fromSecond, fromNone);
        DateTime to = createDateTime(toYear, toMonth, toDay, toHour, toMinute, toSecond, toNone);
        DataExportDefinition def = new DataExportDefinition(pointIds, from, to);
        Common.getUser().setDataExportDefinition(def);
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


	// 时间转化
	public long getDateTime(int Year, int Month, int Day, int Hour, int Minute,
			int Second) {
		String dateTime = Year + "/" + Month + "/" + Day + "/" + Hour + ":"
				+ Minute + ":" + Second;
		return getTime(dateTime) * 1000;
	}
	/**
	 * 获取点设备的历史数据
	 */
	public List<HistoryPointInfo>  getPointHistoryDataA(int[] pointIds) {
		// 实例化pointValueDao ,根据参数查询历史数据
		PointValueDao pointValueDao = new PointValueDao();
		// 定义历史数据集合,用于返回到jsp
		List<HistoryPointInfo> hpi = new ArrayList<HistoryPointInfo>();
		List<Integer> pointids = new ArrayList<Integer>();
		// 循环一个点数组
		for (int i = 0; i < pointIds.length; i++) {
			pointids.add(pointIds[i]);
		}
		hpi=pointValueDao.getHistoryPointValues(pointids,40000);
		return hpi;
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
				0, 10000);
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
	

}
