package com.serotonin.mango.web.servlet;

import java.io.IOException;
import com.serotonin.mango.util.LoginImg;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException; 
import javax.servlet.ServletOutputStream;  
import javax.imageio.ImageIO;  
import java.awt.image.BufferedImage;  
public class LoginCheckServlet extends BaseInfoServlet {
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException,IOException {
		 doPost(request,response) ;  
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		LoginImg bean = new LoginImg();
		BufferedImage buffImg = bean.getBuffImg(request);
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		// 将图像输出到Servlet输出流中。
		ServletOutputStream sos = response.getOutputStream();
		ImageIO.write(buffImg, "jpg", sos);
		sos.flush();
		sos.close();
	}

}
