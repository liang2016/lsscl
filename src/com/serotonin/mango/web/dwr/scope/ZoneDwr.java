package com.serotonin.mango.web.dwr.scope;

import com.serotonin.mango.web.dwr.BaseDwr;
import com.serotonin.mango.Common;
import com.serotonin.mango.vo.scope.ScopeVO;
import com.serotonin.mango.db.dao.scope.ScopeDao;
import com.serotonin.mango.db.dao.DataSourceDao;
import com.serotonin.mango.vo.dataSource.DataSourceVO;
import com.serotonin.mango.rt.RuntimeManager;
import com.serotonin.mango.vo.permission.Permissions;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.serotonin.web.dwr.DwrResponseI18n;
import com.serotonin.mango.vo.scope.TradeVO;
import com.serotonin.mango.db.dao.scope.TradeDao;
import com.serotonin.mango.db.dao.scope.ScopeDao;
import com.serotonin.mango.db.dao.power.UserEventLimtDao;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.db.dao.UserDao;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import java.util.ArrayList;
import com.serotonin.web.i18n.LocalizableMessage;
import com.serotonin.mango.vo.link.PointLinkVO;
import com.serotonin.mango.vo.event.CompoundEventDetectorVO;
import com.serotonin.mango.vo.report.ReportVO;
import com.serotonin.mango.vo.mailingList.MailingList;
import com.serotonin.mango.vo.event.ScheduledEventVO;
import com.serotonin.mango.vo.acp.ACPSystemVO;
import com.serotonin.mango.vo.report.ReportJob;
import com.serotonin.mango.db.dao.ViewDao;
import com.serotonin.mango.db.dao.ScheduledEventDao;
import com.serotonin.mango.db.dao.CompoundEventDetectorDao;
import com.serotonin.mango.db.dao.scope.ScopeSettingDao;
import com.serotonin.mango.db.dao.MailingListDao;
import com.serotonin.mango.db.dao.WatchListDao;
import com.serotonin.mango.db.dao.EventDao;
import com.serotonin.mango.db.dao.DataSourceDao;
import com.serotonin.mango.db.dao.acp.ACPDao;
import com.serotonin.mango.db.dao.PointLinkDao;
import com.serotonin.mango.db.dao.ReportDao;
import com.serotonin.mango.db.dao.DataPointDao;
import com.serotonin.mango.db.dao.acp.CompressedAirSystemDao;
public class ZoneDwr extends BaseDwr {
	/**
	 * 获得所有区域
	 */
	public List<ScopeVO> getZones() {
		ScopeDao zoneDao = new ScopeDao();
		List<ScopeVO> list = zoneDao.getZoneList();
		User user = Common.getUser();
		UserDao.validateScopes(list,user);
		return list;
	}
	
	/**
	 * 获得所有区域单页
	 */
	public List<ScopeVO> getZonesPage(int pageNo,int pageSize) {
		ScopeDao zoneDao = new ScopeDao();
		List<ScopeVO> list = zoneDao.getZoneListByPage(pageNo, pageSize);
		User user = Common.getUser();
		UserDao.validateScopes(list,user);
		return list;
	}
	
	/**
	 * 根据区域获得子区域
	 */
	public List<ScopeVO> getSubZonesByZId(int zId) {
		ScopeDao zoneDao = new ScopeDao();
		List<ScopeVO> list= zoneDao.getsubZoneList(zId);
		User user = Common.getUser();
		UserDao.validateScopes(list,user);
		return list;
	}

	/**
	 * 获得所有区域单页
	 */
	public List<ScopeVO> getSubZonesPageByZId(int zId,int pageNo,int pageSize) {
		ScopeDao zoneDao = new ScopeDao();
		List<ScopeVO> list = zoneDao.getsubZonePageList(zId,pageNo, pageSize);
		User user = Common.getUser();
		UserDao.validateScopes(list,user);
		return list;
	}
	
	/**
	 * 获得所有子区域
	 */
	public List<ScopeVO> getAllSubZones() {
		ScopeDao zoneDao = new ScopeDao();
		List<ScopeVO> list = zoneDao.getsubZoneList();
		User user = Common.getUser();
		UserDao.validateScopes(list,user);
		return list;
	}

	/**
	 * 根据子区域编号获得子区域
	 */
	public ScopeVO selectSubZoneById(int sId) {
		ScopeDao zoneDao = new ScopeDao();
		ScopeVO zone = zoneDao.findZoneOrSubZoneById(sId);
		return zone;
	}

	/**
	 * 根据子区域获得所有工厂
	 */
	public List<ScopeVO> getFactoryBySubZone(int sid) {
		ScopeDao zoneDao = new ScopeDao();
		List<ScopeVO> list = zoneDao.getFactoryBySubZone(sid);
		User user = Common.getUser();
		UserDao.validateScopes(list,user);
		return list;
	}

	/**
	 * 根据子区域获得所有工厂
	 */
	public List<ScopeVO> getFactoryPageBySubZone(int sid,int pageNo,int pageSize) {
		ScopeDao zoneDao = new ScopeDao();
		List<ScopeVO> list = zoneDao.getFactoryPageByZone(sid, pageNo, pageSize);
		User user = Common.getUser();
		UserDao.validateScopes(list,user);
		return list;
	}
	
	/**
	 * 区域的上级一定是总部
	 */
	private static final Integer CURRENT_SCOPE_PARENTID = 1;
	/**
	 * 当前为区域保存操作
	 */
	private static final Integer CURRENT_SCOPE_TYPE = ScopeVO.ScopeTypes.ZONE;

	/**
	 * 修改区域
	 */
	public int updateZone(ScopeVO scopeVO) {
		ScopeDao zoneDao = new ScopeDao();
		// 区域类型
		scopeVO.setScopetype(CURRENT_SCOPE_TYPE);
		// 区域上级
		ScopeVO parentScope = new ScopeVO();
		parentScope.setId(CURRENT_SCOPE_PARENTID);
		scopeVO.setParentScope(parentScope);
		return zoneDao.update(scopeVO);
	}

	/**
	 * 根据工厂编号获得工厂
	 * 
	 * @param factoryId
	 * @return 工厂
	 */
	public ScopeVO findFactoryById(int factoryId) {
		ScopeDao factoryDao = new ScopeDao();
		ScopeVO factory = factoryDao.findFactoryById(factoryId);
		return factory;
	}

	/**
	 * 删除一个scope
	 * 
	 * @param scopeIds
	 *            scope下级的编号
	 * @param newParentIds
	 *            新的父级编号
	 * @param zoneId
	 *            scpoe编号
	 */
	public boolean deleteScope(final int[] scopeIds, final int[] newParentIds,
			final int zoneId) {
		ScopeDao scopeDao = new ScopeDao();
		try {
			scopeDao.changeParentScope(scopeIds, newParentIds, zoneId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}
	/**
	 * 删除一个工厂
	 * @return
	 */
	public boolean deleteFactory(final int[] scopeIds, final int[] newParentIds,
			final int zoneId){
		User user = Common.getUser();
		if(user.getHomeScope().getScopetype()!=0)
			return false;
		ScopeDao scopeDao = new ScopeDao();
		try {
			//删除事件处理器
			EventDao eventDao=new EventDao();
			List<Integer> eventHandlers=eventDao.getEventHandlerIds(zoneId);
			for (int k = 0; k < eventHandlers.size(); k++) {
				int handlerId=eventHandlers.get(k);
		        eventDao.deleteEventHandler(handlerId);
			}
			//删除观察列表,关系
			WatchListDao wlDao=new WatchListDao();
			List<Integer> wlIds=wlDao.getWatchListIds(zoneId);
			for (int m = 0; m < wlIds.size(); m++) {
				wlDao.deleteWatchList(wlIds.get(m));
			}
			//删除图形化视图
			ViewDao viewDao=new ViewDao();
			List<Integer> viewIds=viewDao.getViewIds(zoneId);
			for (int n = 0; n < viewIds.size(); n++) {
				viewDao.removeView(viewIds.get(n));
			}
			//删除事件
			eventDao.deleteByScope(zoneId);
			//删除报告
			ReportDao reportDao=new ReportDao();
			List<ReportVO> reports=reportDao.getReportsByScope(zoneId);
			for (int i = 0; i < reports.size(); i++) {
			 ReportVO report = reports.get(i);
		        if (report != null) {
		            ReportJob.unscheduleReportJob(report);
		            reportDao.deleteReport(report.getId());
		        }
			}
			//删除定时事件
			ScheduledEventDao seDao=new ScheduledEventDao();
			List<ScheduledEventVO> listSE=seDao.getScheduledEvents(zoneId);
			for (int i = 0; i < listSE.size(); i++) {
				ScheduledEventVO seVo=listSE.get(i);
				seDao.deleteScheduledEvent(seVo.getId());
				Common.ctx.getRuntimeManager().stopSimpleEventDetector(ScheduledEventVO.getEventDetectorKey(seVo.getId()));
			}
			
		     //删除组合事件
			CompoundEventDetectorDao sdDao=new CompoundEventDetectorDao(); 
			List<CompoundEventDetectorVO> listCED=sdDao.getCompoundEventDetectors(zoneId);
			for (int i = 0; i < listCED.size(); i++) {
				CompoundEventDetectorVO cedVO=listCED.get(i);
				sdDao.deleteCompoundEventDetector(cedVO.getId());
			    Common.ctx.getRuntimeManager().stopCompoundEventDetector(cedVO.getId());
			}
			//删除点链接
			PointLinkDao plDao=new PointLinkDao();
			List<PointLinkVO> listPL=plDao.getPointLinks(zoneId);
			for (int i = 0; i <listPL.size(); i++) {
				PointLinkVO plVO=listPL.get(i);
				Common.ctx.getRuntimeManager().deletePointLink(plVO.getId());
			}
			//删除点继承关系
			new DataPointDao().deleteScopeHierarchy(zoneId);
			//删除邮件列表
			MailingListDao mListDao=new MailingListDao();
			List<MailingList> listMail=mListDao.getMailingLists(zoneId);
			for (int i = 0; i <listMail.size(); i++) {
				MailingList mList=listMail.get(i);
				mListDao.deleteMailingList(mList.getId());
			}

			//删除空压机,系统
			ACPDao acpDao=new ACPDao(); 
			List<Integer> acpIds=acpDao.getAcpIds(zoneId);
			for (int j = 0; j < acpIds.size(); j++) {
				acpDao.delete(acpIds.get(j));
			}
			CompressedAirSystemDao CASDao=new CompressedAirSystemDao();
			List<ACPSystemVO> listACPS=CASDao.getACPSystemVOByfactoryId(zoneId);
			for (int i = 0; i < listACPS.size(); i++) {
				ACPSystemVO acpsVO=listACPS.get(i);
				CASDao.deleteCompressedById(acpsVO.getId());
			}
			//删除数据源
			List<Integer> dataSourcesIds=new DataSourceDao().getDataSourceIds(zoneId);
			for (int i = 0; i < dataSourcesIds.size(); i++) {
		        Common.ctx.getRuntimeManager().deleteDataSource(dataSourcesIds.get(i));
			}
			//删除工厂系统设置
			new ScopeSettingDao().deleteSetting(zoneId);
			scopeDao.changeParentScope(scopeIds, newParentIds, zoneId);//删除工厂,用户关系
			new UserDao().deleteUserLimit(zoneId);
			new UserEventLimtDao().deleteUserLimit(zoneId);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 查询工厂
	 * @param zoneId
	 * @param subZoneId
	 * @param tradeId
	 * @param factoryName
	 * @return 工厂列表
	 */
	public List<ScopeVO> searchFactory(int zoneId, int subZoneId, int tradeId,String code,String factoryName) {
		User user = Common.getUser();
		ScopeDao scopeDao = new ScopeDao();
		List<ScopeVO>  zoneList;
		if(zoneId==-1&&!user.isAdmin()){
			zoneList=scopeDao.getZoneByNormalUser(user.getId(),1);//1表示查询区域
		}
		else{
			zoneList=scopeDao.getZoneList();
		}
		List<ScopeVO> factoryList1 = new ScopeDao().searchFactory(user,zoneId,subZoneId,tradeId,code,factoryName);
		List<ScopeVO> factoryList=new ArrayList<ScopeVO>();
		for (int i = 0; i < factoryList1.size(); i++) {
			Map<String, Integer> map = new HashMap<String, Integer>();
			ScopeVO scope = factoryList1.get(i);
			for (int j = 0; j < zoneList.size(); j++) {
				if(zoneList.get(j).getId()==scope.getGrandParent().getId()||zoneList.get(j).getId().equals(scope.getGrandParent().getId())){
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
		return factoryList;
	}

	// 编辑scope
	public DwrResponseI18n editScope(int id, String scopename, String address,
			String description,String code, double lon, double lat, int enlargenum,int scopeType,int parentId,int tradeId,boolean disabled) {
		DwrResponseI18n response = new DwrResponseI18n();
		User currentUser = Common.getUser();

		int newId = 0;
		// 封装数据
		ScopeVO zoneVO = new ScopeVO();
		zoneVO.setId(id);
		zoneVO.setScopename(scopename);
		zoneVO.setAddress(address);
		zoneVO.setDescription(description);
		zoneVO.setCode(code);
		zoneVO.setLon(lon);
		zoneVO.setLat(lat);
		zoneVO.setEnlargenum(enlargenum);
		zoneVO.setDisabled(disabled);
		if(tradeId!=0){
			TradeVO trade=new TradeVO();
			trade.setId(tradeId);
			zoneVO.setTradeVO(trade);
		}
		ScopeDao scopeDao = new ScopeDao();
		zoneVO.setScopetype(scopeType);
		if(null!=code&&code.length()>20){
			response.addMessage(new LocalizableMessage( "scope.code.length.error"));
		}
		else if(!scopeDao.isCodeUnique(zoneVO.getCode(),id,"scope")&&zoneVO.getCode()!=null){
			response.addMessage(new LocalizableMessage( "scope.code.used"));
		}
		else if(!scopeDao.isNameUnique(zoneVO.getScopename(),id,"scope")){
			response.addMessage(new LocalizableMessage( "scope.name.used"));
		}
		else if (zoneVO.getId() >0) {
			newId = scopeDao.update(zoneVO);
	        //这里还要修改工厂禁用;1.修改改工厂的用户权限2;如果是禁用就全部禁用数据源
			if(zoneVO.getScopetype()==3){//如果是工厂
				//1.禁用所有用户
				UserDao userDao=new UserDao();
				List<User> users=userDao.getUsers(zoneVO.getId());
				for (User user : users) {
					if(zoneVO.isDisabled())
						user.setDisabled(true);
					else
						user.setDisabled(false);
					userDao.saveUser(user);
				}
				if(zoneVO.isDisabled()){
					//2.禁用所有数据源
					DataSourceDao dsDao=new DataSourceDao();
					List<DataSourceVO<?>> dss=dsDao.getDataSources();
					for (DataSourceVO<?> ds : dss) {
						  Permissions.ensureDataSourcePermission(Common.getUser(), ds.getId());
					      RuntimeManager runtimeManager = Common.ctx.getRuntimeManager();
					      DataSourceVO<?> dataSource = runtimeManager.getDataSource(ds.getId());
					      dataSource.setEnabled(false);
					      runtimeManager.saveDataSource(dataSource);
					}
				}
			}
		}
		else {
			ScopeVO parentScope = new ScopeVO();
			parentScope.setId(parentId);
			zoneVO.setParentScope(parentScope);
			newId = scopeDao.save(zoneVO);
			zoneVO.setId(newId);
			// 添加管理员
			// New database. Create a default user.
			User user = new User();
			user.setId(Common.NEW_ID);
			user.setUsername(zoneVO.getScopename() + "admin");
			user.setPassword(Common.encrypt("admin"));
			//user.setEmail("admin@" + zoneVO.getScopename() + ".com");
			//user.setPhone("123456");
			user.setAdmin(true);
			if(zoneVO.isDisabled()){
				user.setDisabled(true);
			}
			user.setDisabled(false);

			// 用户注册范围
			ScopeVO scope = new ScopeVO();
			//scopetype = currentUser.getCurrentScope().getScopetype();
			scope.setScopetype(scopeType);
			scope.setId(newId);
			user.setHomeScope(scope);
			new UserDao().saveUser(user);
			
			if(scope.getScopetype()==3){
				//给用户添加事件处理器个数限制初始化数据
		          UserEventLimtDao UELDao=new UserEventLimtDao();
		          UELDao.insertUserEventHandler(scope.getId(),10);
		          new UserDao().insertUserAddLimit(scope.getId(),10);
			}
		}
			// 1表示成功
		response.addData("scope",zoneVO);
		
		return response;
	}

	public Map<String, Object> getInitData() {
		Map<String, Object> model = new HashMap<String, Object>();
		ScopeDao scopeDao=new ScopeDao();
		List<ScopeVO> zoneList = scopeDao.getZoneList();
		User user = Common.getUser();
		UserDao.validateScopes(zoneList,user);
		model.put("zoneList", zoneList);
		// 查询所有行业
		TradeDao tradeDao = new TradeDao();
		List<TradeVO> tradeList = tradeDao.findAll();
		model.put("tradeList", tradeList);
		return model;
	}
	/**
	 * 获得所有行业
	 * @return
	 */
	public List<TradeVO> getAlltrades(){
		TradeDao tradeDao = new TradeDao();
		List<TradeVO> tradeList = tradeDao.findAll();
		return tradeList;
		
	}
}
