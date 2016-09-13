package com.serotonin.mango.web.mvc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.UserDao;
import com.serotonin.mango.db.dao.scope.ScopeDao;
import com.serotonin.mango.db.dao.power.ActionDao;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.vo.scope.ScopeVO;
import com.serotonin.mango.vo.power.ActionVO;
import com.serotonin.mango.vo.power.RoleVO;


public class FactoryListController extends ParameterizableViewController {
	
	private static final int CURRENT_SCOPETYPE = ScopeVO.ScopeTypes.SUBZONE;
	
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		//这里获取参数(区域编号)
		int subzoneId=Integer.parseInt(request.getParameter("subzoneId"));
		Map<String, Object> model = new HashMap<String, Object>();
		User user = Common.getUser(request);
		ScopeDao scopeDao = new ScopeDao();
		/************************* 转换角色权限信息 *****************************/
		UserDao.changeRole(CURRENT_SCOPETYPE,subzoneId,user);
		List<ScopeVO> factoryList1= scopeDao.getFactoryBySubZone(subzoneId);
		List<ScopeVO> factoryList2= scopeDao.getFactoryPageByZone(subzoneId, 1, 10);

		UserDao.validateScopes(factoryList1, user);
		int rowCount =factoryList1.size();

		model.put("count", ControllerUtils.getPageCount(rowCount));
		/******************** 验证工厂列表是否都是有权限访问的 ********************/
		UserDao.validateScopes(factoryList2,user);
		ScopeVO zone=scopeDao.findZoneOrSubZoneById(subzoneId);
		model.put("subzoneId",subzoneId);
		model.put("zoneId",zone.getParentScope().getId());
		model.put("user", user);
		model.put("factoryList", factoryList2);
		return new ModelAndView(getViewName(), model);
	}

}
