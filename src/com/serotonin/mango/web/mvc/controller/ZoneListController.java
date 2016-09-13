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
import com.serotonin.mango.vo.User;

import com.serotonin.mango.vo.scope.ScopeVO;
import com.serotonin.mango.db.dao.scope.ScopeDao;

;

public class ZoneListController extends ParameterizableViewController {

	private static final int CURRENT_SCOPETYPE = ScopeVO.ScopeTypes.HQ;

	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		ScopeDao scopeDao = new ScopeDao();
		ScopeVO hq = scopeDao.findHQ();
		User user = Common.getUser(request);
		/** *********************** 转换角色权限信息 **************************** */
		UserDao.changeRole(CURRENT_SCOPETYPE, hq.getId(), user);
		List<ScopeVO> zoneList1 = scopeDao.getZoneList();
		List<ScopeVO> zoneList2 = scopeDao.getZoneListByPage(1, 10);
		UserDao.validateScopes(zoneList1, user);
		int rowCount =zoneList1.size();
		
		model.put("count", ControllerUtils.getPageCount(rowCount));
		List<ScopeVO> zoneList=new ArrayList<ScopeVO>();
		/** ****************** 验证区域列表是否都是有权限访问的 ******************* */
		UserDao.validateScopes(zoneList2, user);
		model.put("user", user);
		for (int i = 0; i < zoneList2.size(); i++) {
			Map<String, Integer> map = new HashMap<String, Integer>();
			ScopeVO scope = zoneList2.get(i);
			map = scopeDao.getEventCountByScope(1,scope.getId());//1表示区域
			for (int j = 0; j < 3; j++) {
				scope.setWarnCount(map.get("yellow"));
				scope.setWarnUnderThreeDays(map.get("orange"));
				scope.setWarnUnderSevenDays(map.get("red"));
			}
			zoneList.add(scope);
		}
		model.put("zoneList", zoneList);
		return new ModelAndView(getViewName(), model);
	}
	

}
