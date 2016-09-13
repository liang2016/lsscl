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

public class ZoneDeleteController extends SimpleFormController {
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
		ScopeDao scopeDao = new ScopeDao();
		int zoneId=Integer.parseInt(request.getParameter("zoneId"));
        
        model.put("zoneid",zoneId);
        //根据删除区域的编号查询所有子区域
        List<ScopeVO> subZoneList = scopeDao.getsubZoneList(zoneId);
        List<ScopeVO> zoneList = scopeDao.getZoneList();
        model.put("subZoneList",subZoneList);
        model.put("zoneList",zoneList);
		return new ModelAndView(deletePage, model);
	}
}
