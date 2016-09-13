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

public class FactoryEditInitController extends ParameterizableViewController {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 这里获取参数(区域编号)
		int subzoneId = Integer.parseInt(request.getParameter("subzoneId"));
		Map<String, Object> model = new HashMap<String, Object>();
		User user = Common.getUser(request);
		ScopeDao scopeDao = new ScopeDao();
		// 根据区域查询子区域
		List<ScopeVO> factoryList = scopeDao.getFactoryBySubZone(subzoneId);
		
		//查询所有行业
		TradeDao tradeDao=new TradeDao();
		List<TradeVO>  tradeList=tradeDao.findAll();
		model.put("tradeList", tradeList);
		
		model.put("user", user);
		model.put("subzoneId", subzoneId);
		model.put("factoryList", factoryList);
		return new ModelAndView(getViewName(), model);
	}
}
