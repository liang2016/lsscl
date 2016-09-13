package com.serotonin.mango.web.mvc.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.serotonin.mango.db.dao.acp.CompressedAirSystemDao;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.serotonin.mango.vo.acp.ACPSystemVO;
import com.serotonin.mango.vo.acp.ACPVO;
import com.serotonin.mango.vo.DataPointVO;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import com.serotonin.mango.Common;

public class CASConntroller extends SimpleFormController {
	private String viewName;

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		CompressedAirSystemDao casDao = new CompressedAirSystemDao();
		List<ACPSystemVO> casList = casDao.getACPSystemVOByfactoryId(14);
		/**
		 * 查询所有系统(根据工厂id)
		 */
		model.put("casList", casList);
		/**
		 * 查询工厂中所有空压机
		 */
		List<ACPVO> acpList = casDao.searchACPsByFactoryId(14);
		model.put("acpList", acpList);

		/**
		 * 查询所有工厂系统-中的数据点
		 */
		List<DataPointVO> dpList = casDao.getDataPointsByFactoryId(14);
		model.put("dpList", dpList);
		/**
		 * 查询工厂压缩系统 -空压机- 数据点
		 */
		List<DataPointVO> compressorDpList = casDao
				.getDataPointsByFactoryCompress(14);

		model.put("compressorDpList", compressorDpList);

		return new ModelAndView(getViewName(), model);
	}
}
