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
import com.serotonin.mango.db.dao.power.RoleDao;
import com.serotonin.mango.db.dao.power.ActionDao;
import com.serotonin.mango.db.dao.scope.ScopeDao;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.vo.power.RoleVO;
import com.serotonin.mango.vo.scope.ScopeVO;


public class SubZoneListController extends ParameterizableViewController {
	
	//当前范围类型为  区域类型
	private static final int CURRENT_SCOPETYPE = ScopeVO.ScopeTypes.ZONE;
	
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 这里获取参数(区域编号)
		int zoneId = Integer.parseInt(request.getParameter("zoneId"));
		Map<String, Object> model = new HashMap<String, Object>();
		User user = Common.getUser(request);
		ScopeDao scopeDao = new ScopeDao();
		/************************* 转换角色权限信息 *****************************/
		UserDao.changeRole(CURRENT_SCOPETYPE,zoneId,user);
		// 根据区域查询子区域
		List<ScopeVO> subZoneList1 = scopeDao.getsubZoneList(zoneId);
		List<ScopeVO> subzoneList2 = scopeDao.getsubZonePageList(zoneId,1, 10);

		UserDao.validateScopes(subZoneList1, user);
		int rowCount =subZoneList1.size();
		
		model.put("count", ControllerUtils.getPageCount(rowCount));
			/******************** 验证子区域列表是否都是有权限访问的 ********************/
		UserDao.validateScopes(subzoneList2,user);
		model.put("zoneId", zoneId);
		model.put("user", user);
		model.put("subZoneList", subzoneList2);
		return new ModelAndView(getViewName(), model);
	}

}
