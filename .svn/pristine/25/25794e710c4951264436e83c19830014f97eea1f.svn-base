package com.serotonin.mango.web.mvc.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import com.lsscl.app.dao.AppsettingDao;
import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.DataSourceDao;
import com.serotonin.mango.vo.AppPoints;
import com.serotonin.mango.vo.Appacpinfo;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.vo.dataSource.DataSourceVO;

public class AppAcpEditController extends ParameterizableViewController {

	private AppsettingDao dao = new AppsettingDao();
	private DataSourceDao dataSourceDao = new DataSourceDao();
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		User user = Common.getUser(request);
		String acpid = request.getParameter("acpid");
		Map<String, Object> model = new HashMap<String, Object>();
		String factroyId = request.getParameter("factoryId");
		
		if(acpid!=null){//编辑
			List<AppPoints<?>>points = dao.getPointsByAcpId(acpid);
			Appacpinfo acp = dao.getAcpById(acpid);
			model.put("points", points);
			model.put("acp", acp);
		}else{//添加
			
		}
		
		if(factroyId!=null){
			List<Integer>ids = dataSourceDao.getDataSourceIds(Integer.parseInt(factroyId));
			List<DataSourceVO<?>>dataSources = new ArrayList<DataSourceVO<?>>();
			for(int id:ids){
				DataSourceVO<?> dataSource = dataSourceDao.getDataSource(id);
				dataSources.add(dataSource);
			}
			model.put("dataSources", dataSources);
		}
		model.put("scopeId", factroyId);
        return new ModelAndView(getViewName(), model);
	}
}
