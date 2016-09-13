package com.serotonin.mango.web.mvc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.UserDao;
import com.serotonin.mango.vo.User;

import com.serotonin.mango.vo.scope.ScopeVO;
import com.serotonin.mango.db.dao.scope.ScopeDao;
import com.serotonin.mango.vo.scope.TradeVO;
import com.serotonin.mango.db.dao.scope.TradeDao;

public class ZoneEditInitController extends ParameterizableViewController {
	

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		User user = Common.getUser(request);
		ScopeDao scopeDao = new ScopeDao();
		List<ScopeVO> zoneList = scopeDao.getZoneList();
		model.put("user", user);
		model.put("zoneList", zoneList);
		//查询所有行业
		TradeDao tradeDao=new TradeDao();
		List<TradeVO>  tradeList=tradeDao.findAll();
		model.put("tradeList", tradeList);
		return new ModelAndView(getViewName(), model);
	}
}
