package com.lsscl.app.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lsscl.app.util.StringUtil;
import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.DataSourceDao;
import com.serotonin.mango.vo.User;

public class PortListServlet extends HttpServlet {
	private DataSourceDao dao = new DataSourceDao();

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		User user = Common.getUser(request);
		if(user==null){
			response.sendRedirect("/login.htm");
			return;
		}
		Map<String, Object> queryParam = new HashMap<String, Object>();
		List<Map<String, Object>> ports = dao.getDataSourceByMap(queryParam);
		request.setAttribute("ports", ports);
		String pageString = request.getParameter("page");
		String pageSizeString = request.getParameter("pageSizeString");
		int page = pageString == null ? 1 : Integer.valueOf(pageString);

		int pageSize = pageSizeString == null ? 10 : Integer
				.valueOf(pageSizeString);
		int count = dao.getCountByMap(queryParam);
		int totalPage = count % pageSize == 0 ? count / pageSize : count
				/ pageSize + 1;
		if (page > totalPage)
			page = totalPage;
		if (page < 1)
			page = 1;
		queryParam.put("page", page);
		queryParam.put("pageSize", pageSize);
		queryParam.put("totalPage", totalPage);
		request.setAttribute("query", queryParam);
		request.getRequestDispatcher("/WEB-INF/jsp/portList.jsp").forward(
				request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if(Common.getUser(request)==null){
			response.sendRedirect("/login.htm");
			return;
		}
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String port = request.getParameter("port");
		Map<String, Object> queryParam = new HashMap<String, Object>();
		String pageString = request.getParameter("page");
		String pageSizeString = request.getParameter("pageSizeString");
		String name = request.getParameter("name");
		int page = pageString == null ? 1 : Integer.valueOf(pageString);

		int pageSize = pageSizeString == null ? 10 : Integer
				.valueOf(pageSizeString);

		queryParam.put("pageSize", pageSize);
		queryParam.put("name", name);
		queryParam.put("port", port);
		int count = dao.getCountByMap(queryParam);
		int totalPage = count % pageSize == 0 ? count / pageSize : count
				/ pageSize + 1;
		if (!"".equals(port))
			totalPage = 1;
		if (page > totalPage)
			page = totalPage;
		if (page < 1)
			page = 1;
		queryParam.put("page", page);
		StringUtil.trimMap(queryParam);
		queryParam.put("totalPage", totalPage);
		List<Map<String, Object>> ports = dao.getDataSourceByMap(queryParam);
		request.setAttribute("ports", ports);
		request.setAttribute("query", queryParam);
		request.getRequestDispatcher("/WEB-INF/jsp/portList.jsp").forward(
				request, response);
	}

}
