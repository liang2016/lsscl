/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.mvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.view.RedirectView;
import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.WatchListDao;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.web.integration.CrowdUtils;
import com.serotonin.mango.vo.WatchList;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.vo.WatchList;
import com.serotonin.db.IntValuePair;
import java.util.Date;
import java.text.SimpleDateFormat;
public class DynamicDataController extends AbstractController {
	private String hasLogin;
	private String noLogin;

	public String getNoLogin() {
		return noLogin;
	}

	public void setNoLogin(String noLogin) {
		this.noLogin = noLogin;
	}

	public void setRedirectUrl(String hasLogin) {
		this.hasLogin = hasLogin;
	}

	public String getHasLogin() {
		return hasLogin;
	}

	public void setHasLogin(String hasLogin) {
		this.hasLogin = hasLogin;
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) {
		// Check if the user is logged in.
		User user = Common.getUser(request);
		if (user != null) {
			return new ModelAndView(hasLogin, createModel(request, user));
			// return new ModelAndView(new RedirectView(redirectUrl));
		}

		// Regardless of what happened above, forward to the configured view.
		return new ModelAndView(noLogin);
	}

	// 创建一个model(点设备集合,观察列表)
	protected Map<String, Object> createModel(HttpServletRequest request,
			User user) {
		Map<String, Object> model = new HashMap<String, Object>();
		WatchList watchList = user.getWatchList();
		List<DataPointVO> pointList = watchList.getPointList();
		model.put("watchListId",watchList.getId());
		model.put("pointList", pointList);
		// 获得用户权限下的观察列表,并将id,和name放入model
		WatchListDao watchListDao = new WatchListDao();
		List<WatchList> watchLists = watchListDao.getWatchLists(user.getId(),user.getCurrentScope().getId());
		List<IntValuePair> watchListNames = new ArrayList<IntValuePair>(
				watchLists.size());
		for (int i = 0; i < watchLists.size(); i++) {
			watchListNames.add(new IntValuePair(watchLists.get(i).getId(),
					watchLists.get(i).getName()));
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String serverDate=sdf.format(new Date());
		model.put("watchListNames", watchListNames);
		model.put("serverDate",serverDate);
		return model;
	}
}
