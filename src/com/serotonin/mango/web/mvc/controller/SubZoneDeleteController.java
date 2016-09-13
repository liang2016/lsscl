package com.serotonin.mango.web.mvc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.UserDao;
import com.serotonin.mango.vo.User;

import com.serotonin.mango.vo.scope.ScopeVO;
import com.serotonin.mango.db.dao.scope.ScopeDao;

public class SubZoneDeleteController extends SimpleFormController {
	private String deletePage;

	public String getDeletePage() {
		return deletePage;
	}

	public void setDeletePage(String deletePage) {
		this.deletePage = deletePage;
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		User currentUser = Common.getUser(request);
		int zoneId=currentUser.getCurrentScope().getId();
		
		ScopeDao scopeDao = new ScopeDao();
		int subZoneId=Integer.parseInt(request.getParameter("subzoneId"));
        
        model.put("subZoneId",subZoneId);
        //根据删除区域的编号查询所有子区域
        List<ScopeVO> factoryList = scopeDao.getFactoryBySubZone(subZoneId);
        List<ScopeVO> subZoneList = scopeDao.getsubZoneList(zoneId);
        model.put("factoryList",factoryList);
        model.put("subZoneList",subZoneList);
		return new ModelAndView(deletePage, model);
	}
}
