package com.serotonin.mango.web.mvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import com.serotonin.mango.vo.FactoryList;
import com.serotonin.mango.db.dao.FactoryDao;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.serotonin.mango.Common;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.db.dao.EventDao;
public class GooleMapController extends AbstractController {
	/**
	 * 转跳的页面
	 */
	private String newUrl;

	public String getNewUrl() {
		return newUrl;
	}

	public void setNewUrl(String newUrl) {
		this.newUrl = newUrl;
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) {
		User user = Common.getUser(request);
		return new ModelAndView(newUrl,createModel(request,user));
	}

	// 创建一个model(点设备集合,观察列表)
	protected Map<String, Object> createModel(HttpServletRequest request,User user) {
		Map<String, Object> model = new HashMap<String, Object>();
		FactoryDao factoryDao = new FactoryDao();
	    List<FactoryList> factoryList=factoryDao.getFactoryList();
		model.put("factoryList",factoryList);
		EventDao eventDao=new EventDao();
		int eventCount=eventDao.getEventCount();
		model.put("eventCount",eventCount);
		return model;
	}
	
}
