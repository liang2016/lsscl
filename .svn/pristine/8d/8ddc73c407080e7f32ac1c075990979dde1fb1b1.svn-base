/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.filter;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.lang.NumberFormatException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.impl.DefaultWebContextBuilder;

import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.power.ActionDao;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.vo.power.ActionVO;
import com.serotonin.mango.vo.power.RoleVO;
import com.serotonin.mango.vo.scope.ScopeVO;
import com.serotonin.mango.db.dao.scope.ScopeDao;
import org.springframework.validation.BindException;
import com.serotonin.util.ValidationUtils;
import com.serotonin.mango.vo.permission.PermissionException;

/**
 *  
 */
public class RoleFilter implements Filter {
	private FilterConfig filterConfig;

    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    @Override
    public void destroy() {
        // no op
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
    	HttpServletRequest httpRequest = (HttpServletRequest)request;
    	HttpServletResponse httpResponse = (HttpServletResponse)response;
    	httpResponse.setHeader("Cache-Control","no-store");
    	httpResponse.setHeader("Pragrma","no-cache");
    	httpResponse.setDateHeader("Expires",0);
    	//当前请求的URL
    	String uri = httpRequest.getRequestURI().toString();
//    	//登陆页面不过滤
//    	if(filterConfig.getInitParameter("login").equals(uri)){
//    		chain.doFilter(httpRequest, httpResponse);	
//    	}
    	//是否有权限继续访问
    	boolean permit = false;
    	//获取Session中的User对象
    	User user = Common.getUser(httpRequest);
    	ScopeDao scopeDao = new ScopeDao();
    	ScopeVO currentScope = null;
    	int currentScopeID = -1;
    	if(user.getCurrentScope()==null){
    		throw new PermissionException("you have not a role ",user);
    	}else{
    		//当前要进入的范围
    		currentScope = user.getCurrentScope();
    		if(user.getCurrentScope().getId()==null){
    			throw new PermissionException("you have not a role ",user);
    		}else{
    			//当前要进入的范围ID
    			currentScopeID = user.getCurrentScope().getId();
    		}
    	}
    	
    	//获取当前进入页面的范围ID
    	if(ActionDao.isHqAdminAction(uri)){//如果是总部管理员角色拥有的权限下的URI，则当要进入的范围ID为总部ID
    		currentScope = scopeDao.findHQ();
    	}else{
//    		//获取web.xml的参数--此处配置的变量为不需要过滤器过滤的url
//        	String specialUriStr = filterConfig.getInitParameter("special");
//        	String[] specialUris = specialUriStr.split("/t/e/m/p");
//    		boolean isSpecial = false;//判断是否为特殊页面
//    		for(int i=0;i<specialUris.length;i++){
//    			if(specialUris[i].equals(uri)){
//    				isSpecial = true;
//    				if(user.getCurrentScope().getScopetype()!=ScopeVO.ScopeTypes.FACTORY){
//    					httpResponse.sendRedirect("/login.htm");
//    				}
//    				break;
//    			}
//    		}//不是特殊页面就需要进行验证
//    		if(isSpecial==false){
    			try{
    	    		String currentScopeIDStr = "";
    	    		currentScopeIDStr = request.getParameter("zoneId");
    	    		if(currentScopeIDStr==null||currentScopeIDStr=="null"){
    	    			currentScopeIDStr = request.getParameter("subzoneId");
    	    			if(currentScopeIDStr==null||currentScopeIDStr=="null"){
    	    				currentScopeIDStr = request.getParameter("factoryId");
    	    				if(currentScopeIDStr==null||currentScopeIDStr=="null"){
    	    					throw new PermissionException("please input a param like 'zoneId' or 'subzoneId' or 'factoryId' ",user);
    	    				}else{
    	    					currentScopeID = Integer.parseInt(currentScopeIDStr);
    	    					currentScope = scopeDao.findFactoryById(currentScopeID); 
    	    				}
    	    			}else{
    	    				currentScopeID = Integer.parseInt(currentScopeIDStr);
    	    				currentScope = scopeDao.findZoneOrSubZoneById(currentScopeID); 
    	    			}
    	    		}else{
    	    			currentScopeID = Integer.parseInt(currentScopeIDStr);
    	    			currentScope = scopeDao.findZoneOrSubZoneById(currentScopeID); 
    	    		}
        		}catch(NumberFormatException e){
        			System.out.println(e.getMessage());
        		}
        		if(currentScope==null){
        			throw new PermissionException("the scope is not found!",user);
        		}else{
	        		/**************************当前范围ID是否属于自己注册ID的子范围******************************/
	        		if(!user.getHomeScope().getId().equals(currentScope.getId())&&scopeDao.isMyChild(user.getHomeScope().getId(),currentScope.getId())==false){
	        			//不是则抛出异常
	        			throw new PermissionException("you only see yourself scope,can't looking scopes of other people !",user);
	        		}   	
        		}
    		}
//    	}
    	
    	//判断当前角色下权限集合内是否有当前请求URi，
    	List<ActionVO> actionList = user.getCurrentRoleActionList();
    	for (ActionVO actionVO : actionList) {
			if(actionVO.getUrl().equals(uri.trim())){
				permit = true;
				break;
			}
		}
    	//判断当前范围是否是用户临时可设置的范围,如果是设置为临时admin
    	//1.判断当前范围是否就是用户的临时设置范围,如果是设置为临时admin
    	//2.判断当前范围是不是用户注册范围如果是,直接跳过
    	//3.判断当前范围是不是用户可设置范围内的子范围,如果是设置为admin
    //	int scopeIdTemp=currentScope.getId();
    	if(!user.isAdmin()){//取出用户可以设置的范围
    		List<ScopeDao.UserSetClass> listSet=scopeDao.isUserSetScope(user.getId());
    		List<ScopeVO> userScope=user.getChildScopeList();
    		if(user.getHomeScope().getScopetype().equals(currentScope.getScopetype())){//用户当前范围类型就是用户所在的范围
				user.setTempAdmin(false);
				//permit=false;
			}else{
	    	for (ScopeDao.UserSetClass usc : listSet) {
				if(usc.getScopeId()==currentScopeID){//如果当前访问的范围就是用户可设置的范围
					user.setTempAdmin(true);
					break;
				}
				else if(usc.getScopeType() == currentScope.getScopetype()){
					for (ScopeVO sVo : userScope) {
						if(sVo.getId()==currentScope.getId()){
							user.setTempAdmin(false);
							break;
						}
					}
				}
				else{
//					//user.setTempAdmin(false);
//					for (ScopeVO scope : userScope) {
//						if(scope.getId()==currentScopeID){
//							user.setTempAdmin(true);
//							//user.setAdmin(true);//设置为admin
//							break;
//						}
//						else{
							if(!user.isTempAdmin())
								user.setTempAdmin(false);
							List<ScopeVO> temp=new ArrayList<ScopeVO>();
							switch (usc.getScopeType()) {//根据当前范围类型,找出他的子范围
							case 1://区域
								temp=scopeDao.getsubZoneList(usc.getScopeId());//查询的是子区域
								for (ScopeVO s : temp) {
									if(s.getId()==currentScopeID){
										user.setTempAdmin(true);
										break;
									}
									else{
										List<ScopeVO> tempChild=new ArrayList<ScopeVO>();//查询的是工厂
										tempChild=scopeDao.getFactoryBySubZone(s.getId());
										for (ScopeVO s1 : tempChild) {
											if(s1.getId()==currentScopeID){
												user.setTempAdmin(true);
												break;
											}
										}
									}
								}
								break;
							case 2://子区域
								temp=scopeDao.getFactoryBySubZone(usc.getScopeId());//查询的是工厂
								for (ScopeVO s : temp) {
									if(s.getId()==currentScopeID){
										user.setTempAdmin(true);
										break;
									}
								}
								break;
							case 3://工厂没有子集
								user.setTempAdmin(false);
								break;
							default:
								break;
							}
						}
						
					}
				}		
	    	//}
    	}
    	
    	
    	
    	/**
    	 * 如果当前角色下的权限集合中没有找到其进入权限；
    	 * 则进入其他角色中查看此用户是否有权限进入；
    	 * 有则转换到该角色，无则拒绝进入，进入错误页面；
    	 */
    	if(permit==false){
    		//获取当前用户的角色集合，查找所有角色的权限 
    		for (int i =0;i<user.getRoleList().size();i++) {
    			RoleVO role = user.getRoleList().get(i);
				if(role.getId()==user.getCurrentRole().getId()){
					continue;
				}
				//某个角色下的权限集合
				List<ActionVO> tempActionList = new ActionDao().findByRole(role.getId());
				for (ActionVO actionVO : tempActionList) {
					if(actionVO.getUrl().equals(uri.trim())){//一旦匹配
						permit = true;//允许通行
						user.setCurrentRole(role);//转换为当前角色
						user.setCurrentScope(currentScope);
						if(user.isTempAdmin())
							user.setCurrentRoleActionList(new ActionDao().findByRole(user
									.getCurrentRole().getId()-1));
						else{
						     user.setCurrentRoleActionList(new ActionDao().findByRole(user
								.getCurrentRole().getId()));
						}
						break;
					}
				}
				if(permit==true){//如果已经匹配，则不在继续查找，直接放行
					break;
				} 
			} 
    	}
    	else if(!permit&&user.isTempAdmin()){
    		user.setCurrentRoleActionList(new ActionDao().findByRole(user
					.getCurrentRole().getId()-1));
    	}
    	if(permit){//有权限访问
    		chain.doFilter(httpRequest, httpResponse);
    	}else{//无权限访问
    		throw new PermissionException("you have not a role ",user);
    	}
    }
} 