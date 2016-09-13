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
import com.serotonin.mango.vo.scope.TradeVO;
import com.serotonin.mango.db.dao.scope.TradeDao;

public class FactorySearchController extends ParameterizableViewController {
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		User user = Common.getUser(request);
		ScopeDao scopeDao = new ScopeDao();
		List<ScopeVO>  zoneList;
		if(user.isAdmin()){
			//查询所有区域
			zoneList=scopeDao.getZoneList();
		}else{
			zoneList=scopeDao.getZonesByUser(user.getId(),user.getHomeScope().getScopetype());
		}
		model.put("zoneList", zoneList);
		List<ScopeVO> factoryList1 = new ArrayList<ScopeVO>();
		List<ScopeVO> factoryList= new ArrayList<ScopeVO>();
		int currentScopeID = -1;
		try{
    		String currentScopeIDStr = "";
    		currentScopeIDStr = request.getParameter("zoneId");
    		if(currentScopeIDStr==null||currentScopeIDStr=="null"){
    			currentScopeIDStr = request.getParameter("subzoneId");
    			if(currentScopeIDStr==null||currentScopeIDStr=="null"){
    				factoryList1 = scopeDao.searchFactory(user,-1, -1, -1,"","");
    			}else{
    				currentScopeID = Integer.parseInt(currentScopeIDStr);
    				ScopeVO currentScope = scopeDao.findZoneOrSubZoneById(currentScopeID);
    				factoryList1 = scopeDao.searchFactory(user,currentScope.getParentScope().getId(), currentScopeID, -1,"","");
    			}
    		}else{
    			currentScopeID = Integer.parseInt(currentScopeIDStr);
    			factoryList1 = scopeDao.searchFactory(user,currentScopeID, -1, -1,"","");
    		}
		}catch(NumberFormatException e){
			System.out.println(e.getMessage());
		}
		model.put("user", user);
		
		for (int i = 0; i < factoryList1.size(); i++) {
			Map<String, Integer> map = new HashMap<String, Integer>();
			ScopeVO scope = factoryList1.get(i);
			if(scope.isDisabled())
				continue;
				for (int j = 0; j < zoneList.size(); j++) {
					if(zoneList.get(j).getId().equals(scope.getGrandParent().getId())||zoneList.get(j).getId().equals(scope.getGrandParent().getId())){
						map = scopeDao.getEventCountByScope(3,scope.getId());
						for (int k = 0; k < 3; k++) {
							scope.setWarnCount(map.get("yellow"));
							scope.setWarnUnderThreeDays(map.get("orange"));
							scope.setWarnUnderSevenDays(map.get("red"));
						}
						factoryList.add(scope);
					}
				}
		}
		model.put("factoryList", factoryList);
		//查询所有行业
		TradeDao tradeDao=new TradeDao();
		List<TradeVO>  tradeList=tradeDao.findAll();
		model.put("tradeList", tradeList);
		return new ModelAndView(getViewName(), model);
	}
}
