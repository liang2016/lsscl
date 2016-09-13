package com.serotonin.mango.web.mvc.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.validation.BindException;

import com.lsscl.app.dao.AppsettingDao;
import com.serotonin.mango.Common;
import com.serotonin.mango.vo.Appacpinfo;
import com.serotonin.mango.vo.User;
import com.serotonin.web.util.PaginatedData;
import com.serotonin.web.util.PaginatedListController;
import com.serotonin.web.util.PagingDataForm;

public class AppDatasourceSettingController extends PaginatedListController {
	protected PaginatedData getData(HttpServletRequest request,
			PagingDataForm paging, BindException errors) throws Exception {
		User user = Common.getUser(request);
		AppsettingDao appsettingDao = new AppsettingDao();
		int scopeid = Integer.parseInt(request.getParameter("factoryId"));
		List<Appacpinfo> data = appsettingDao.getScopeAppacpinfoList(scopeid);

		return new PaginatedData<Appacpinfo>(data, data.size());
	}

}
