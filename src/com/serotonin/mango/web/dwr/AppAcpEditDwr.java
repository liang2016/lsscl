package com.serotonin.mango.web.dwr;

import java.util.List;

import com.lsscl.app.dao.AppsettingDao;
import com.serotonin.mango.Common;
import com.serotonin.mango.vo.dataSource.DataSourceVO;
import com.serotonin.web.dwr.DwrResponseI18n;
import com.serotonin.web.dwr.MethodFilter;

public class AppAcpEditDwr extends BaseDwr {
	private AppsettingDao dao = new AppsettingDao();
	
	    @MethodFilter
	    public DwrResponseI18n getPointsByAcpId(String acpid) {
	        DwrResponseI18n response = new DwrResponseI18n();
	        response.addData("points", dao.getPointsByAcpId(acpid));
	        return response;
	    }
	 
	    /**
	     * 获取数据源
	     * @return
	     */
	    @MethodFilter
	    public DwrResponseI18n getDataSources() {
	        DwrResponseI18n response = new DwrResponseI18n();
	        List<DataSourceVO<?>> data = Common.ctx.getRuntimeManager().getDataSources();
	        response.addData("dataSources", data);
	        return response;
	    }
}
