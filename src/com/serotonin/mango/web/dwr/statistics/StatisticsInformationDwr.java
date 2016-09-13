package com.serotonin.mango.web.dwr.statistics;

import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.Date;
import java.util.List;
import java.util.Collections;
import java.util.HashMap;
import java.util.Calendar;
import java.util.ArrayList;
import com.serotonin.mango.Common;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.web.dwr.BaseDwr;
import com.serotonin.mango.vo.scope.ScopeVO;
import com.serotonin.web.dwr.DwrResponseI18n;
import com.serotonin.mango.vo.permission.Permissions;
import com.serotonin.mango.vo.statistics.IndexOrderVO;
import com.serotonin.mango.vo.statistics.StatisticsScriptVO;
import com.serotonin.mango.db.dao.scope.ScopeDao;
import com.serotonin.mango.db.dao.statistics.StatisticsInformationDao;
import com.serotonin.mango.rt.statistic.common.StatisticsUtil;
import com.serotonin.mango.vo.statistics.StatisticsScriptVO;
import com.serotonin.mango.db.dao.statistics.StatisticsScriptDao;
import com.serotonin.mango.rt.statistic.common.StatisticsRT;
import com.serotonin.mango.db.dao.UserDao;
import com.serotonin.mango.vo.statistics.ComparatorIndexOrder;
public class StatisticsInformationDwr extends BaseDwr{
	
	/**
	 * 循环周期
	 * 前一天，上一周，上一个月，上一个季度，前一年
	 */
	public static final int CYCLE_YESTERDAY = 1;//7
	public static final int CYCLE_WEEK = 2;//8
	public static final int CYCLE_MONTH = 3;//9
	public static final int CYCLE_QUARTER = 4;//10
	public static final int CYCLE_YEAR = 5;
	
	//数据采集周期的毫秒数
	public static final long MS_IN_COLLECT_CYCLE = StatisticsUtil.getCollectCycle();

	//一个周期的毫秒数
	public static final long MS_IN_STATISTIC_CYCLE = StatisticsRT.getMsInCycle();
	
	
	
	/***********************************************指数排序页面 *************************************************/
	
	
	/**
	 * 指数排序显示页面初始化方法
	 * @param scriptId 脚本ID
	 * @param from     开始时间
	 * @param to 	   结束时间 
	 * @return
	 */
	public DwrResponseI18n indexOrderInit(int cycle,int scriptId){
		long from = -1,to = -1;
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		switch(cycle){
		case CYCLE_YESTERDAY:
			to = cal.getTimeInMillis();
			cal.set(Calendar.DATE,cal.get(Calendar.DATE)-1);
			from = cal.getTimeInMillis();
			break;
		case CYCLE_WEEK:
			cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
			to = cal.getTimeInMillis();
			cal.set(Calendar.WEEK_OF_YEAR,cal.get(Calendar.WEEK_OF_YEAR)-1);
			from = cal.getTimeInMillis();
			break;
		case CYCLE_MONTH:
			cal.set(Calendar.DATE, 1);
			to = cal.getTimeInMillis();
			cal.set(Calendar.MONDAY,cal.get(Calendar.MONDAY)-1);
			from = cal.getTimeInMillis();
			break;
		case CYCLE_QUARTER:
			cal.set(Calendar.DATE, 1);
			int month = cal.get(Calendar.MONTH);
			if(month>=0&&month<3){
				cal.set(Calendar.YEAR,cal.get(Calendar.YEAR)-1);
				cal.set(Calendar.MONDAY,9);
			}else if(month>=4&&month<7){
				cal.set(Calendar.MONDAY,0);
			}else if(month>=7&&month<10){
				cal.set(Calendar.MONDAY,3);
			}else if(month>=10&&month<12){
				cal.set(Calendar.MONDAY,6);
			}
			from = cal.getTimeInMillis();
			cal.set(Calendar.MONDAY,cal.get(Calendar.MONDAY)+3);
			to = cal.getTimeInMillis();
			break;
		case CYCLE_YEAR:
			cal.set(Calendar.DATE, 1);
			cal.set(Calendar.MONDAY, 0);
			to = cal.getTimeInMillis();
			cal.set(Calendar.YEAR,cal.get(Calendar.YEAR)-1);
			from = cal.getTimeInMillis();
			break;
		}
//		System.out.println("from:"+new Date(from).toLocaleString()+"      "+new Date(to).toLocaleString());
		if(from==-1l||to==-1l) return null;
		
		//获取当前用户信息 并且 权限验证
		User user = Common.getUser();
		Permissions.hasStatisticsInformationPermission(user);
		DwrResponseI18n response = new DwrResponseI18n();
		ScopeDao scopeDao = new ScopeDao();
		StatisticsInformationDao informationDao = new StatisticsInformationDao();
		List<ScopeVO> childScopeList = new ArrayList<ScopeVO>();
		if(user.isAdmin()){
			//管理员则显示全部子范围
			childScopeList = scopeDao.getChildScope(user.getCurrentScope().getId(),user.getCurrentScope().getScopetype());
		}else{
			//普通用户则只显示它有权限访问的子范围
			if(user.getHomeScope().getScopetype()<user.getCurrentScope().getScopetype()){
				childScopeList = scopeDao.getChildScope(user.getCurrentScope().getId(),user.getCurrentScope().getScopetype());
			}else{
				childScopeList = scopeDao.getScopesByUser(user.getId());
			}
		}
		response.addData("childScopeList",childScopeList);
		StatisticsScriptVO scriptVO;
		if(scriptId==StatisticsUtil.INDEX_OF_HEALTH||scriptId==StatisticsUtil.RATE_OF_TROUBLE_HANDLE){
			scriptVO = StatisticsUtil.getIndexScript(scriptId,cycle);
		}
		else{
			scriptVO = StatisticsUtil.getIndexScript(scriptId);
		}
		//和childScopeList中每个范围对应的from到to时间之间的scriptId 脚本的值
		List<Double> values = new ArrayList<Double>();
//		if(scriptId==StatisticsUtil.ENERGY_SAVING_INDEX){
//			values = informationDao.getResultByScriptBetweenTimeIncludeSomeScope(scriptVO,childScopeList,user,from,to);
//		}else{
			values = informationDao.getResultByScriptBetweenTimeIncludeSomeScope(scriptVO,childScopeList,user,from,to);
//		}
		List<IndexOrderVO> indexList = new ArrayList<IndexOrderVO>();
		for(int i=0;i<childScopeList.size();i++){
			ScopeVO scopeVO = childScopeList.get(i);
			indexList.add(new IndexOrderVO(scopeVO.getId(),scopeVO.getScopename(),values.get(i)));
		}
		ComparatorIndexOrder ci=new ComparatorIndexOrder();
		Collections.sort(indexList,ci);
		response.addData("indexList",indexList);
		return response;
	}
	
	
	
	
	/********************************************宏观数据页面**************************************************/
	
	
	
	/**
	 * 根据范围查询统计信息页面要显示的几个统计信息
	 * @param scope 统计范围
	 * @return 统计结果集
	 */
	private Map<String,Object> getInformation(ScopeVO scope,User user){
		StatisticsInformationDao informationDao = new StatisticsInformationDao();
		StatisticsScriptDao scriptDao = new StatisticsScriptDao();
		long to = new Date().getTime();
		long from = to - MS_IN_COLLECT_CYCLE;
//		System.out.println("from:"+new Date(from).toLocaleString()+"      "+new Date(to).toLocaleString());
		Map<String,Object> data = new HashMap<String,Object>();
		//总机器数量
		data.put("countOfMachine",informationDao.getCountOfMachine(scope,user));
		//机器加载数量
		data.put("countOfOnLoadMachine",informationDao.getCountOfOnloadMachine(scope,user));
		//机器卸载数量
		data.put("countOfUnLoadMachine",informationDao.getCountOfUnloadMachine(scope,user));
		//机器停机数量
		data.put("countOfShutdownMachine",informationDao.getCountOfShutdownMachine(scope,user));
		//故障停机数量
		data.put("countOfShutdownInTroubleMachine",informationDao.getCountOfShutdownInTroubleMachine(scope,user));
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);
		//cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
		to = cal.getTimeInMillis();
		//cal.set(Calendar.WEEK_OF_YEAR,cal.get(Calendar.WEEK_OF_YEAR)-1);
		cal.set(Calendar.DAY_OF_YEAR,cal.get(Calendar.DAY_OF_YEAR)-1);
		from = cal.getTimeInMillis();
//		System.out.println("from:"+new Date(from).toLocaleString()+"      "+new Date(to).toLocaleString());
		//健康指数
		data.put("healthIndex",informationDao.getResultByScriptBetweenTime(StatisticsUtil.getIndexScript(StatisticsUtil.INDEX_OF_HEALTH,CYCLE_YESTERDAY),scope,user,from,to));
		//节能指数
		data.put("energySavingIndex",informationDao.getResultByScriptBetweenTime(StatisticsUtil.getIndexScript(StatisticsUtil.ENERGY_SAVING_INDEX),scope,user,from,to));
		//故障处理率
		data.put("solveTroubleRate",informationDao.getResultByScriptBetweenTime(StatisticsUtil.getIndexScript(StatisticsUtil.RATE_OF_TROUBLE_HANDLE,CYCLE_YESTERDAY),scope,user,from,to));
		return data;
	}
	
	/**
	 * 统计信息显示页面初始化方法
	 * @return 结果集
	 */
	public DwrResponseI18n informationInit(){
		//获取当前用户信息 并且 权限验证
		User user = Common.getUser();
		Permissions.hasStatisticsInformationPermission(user);
		DwrResponseI18n response = new DwrResponseI18n();
		ScopeDao scopeDao = new ScopeDao();
		List<ScopeVO> childScopeList = new ArrayList<ScopeVO>();
		if(user.isAdmin()){
			//管理员则显示全部子范围
			childScopeList = scopeDao.getChildScope(user.getCurrentScope().getId(),user.getCurrentScope().getScopetype());
		}else{
			//普通用户则只显示它有权限访问的子范围
			if(user.getHomeScope().getScopetype()<user.getCurrentScope().getScopetype()){
				childScopeList = scopeDao.getChildScope(user.getCurrentScope().getId(),user.getCurrentScope().getScopetype());
			}else{
				childScopeList = scopeDao.getScopesByUser(user.getId());
			}
		}
		response.setData(getInformation(user.getCurrentScope(),user));
		List<Object[]> childDataList = new ArrayList<Object[]>();
		for(ScopeVO scopeVO:childScopeList){
			Map<String,Object> childMapData= getInformation(scopeVO,user);
			Object[] object = new Object[5];
			object[0] = childMapData.get("countOfMachine");
			object[1] = childMapData.get("countOfOnLoadMachine");
			object[2] = childMapData.get("countOfUnLoadMachine");
			object[3] = childMapData.get("countOfShutdownMachine");
			object[4] = childMapData.get("countOfShutdownInTroubleMachine");
			childDataList.add(object);
		}
		response.addData("childScopeList",childScopeList);
		response.addData("childDataList",childDataList);
		return response;
	}
	
	
	
	/**
	 * 获取历史数据，为画图准备数据
	 * @param scriptId 脚本ID
	 * @param from     开始时间
	 * @param to       结束时间
	 * @return  历史数据
	 */
	public List<Object[]> getHistoryData(int scriptId,int cycle){
		long from = -1,to = -1;
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		switch(cycle){
		case CYCLE_YESTERDAY:
			to = cal.getTimeInMillis();
			cal.set(Calendar.DATE,cal.get(Calendar.DATE)-1);
			from = cal.getTimeInMillis();
			break;
		case CYCLE_WEEK:
			if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
				cal.set(Calendar.WEEK_OF_YEAR,cal.get(Calendar.WEEK_OF_YEAR)-1);
			}
			cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
			to = cal.getTimeInMillis();
			cal.set(Calendar.WEEK_OF_YEAR,cal.get(Calendar.WEEK_OF_YEAR)-1);
			from = cal.getTimeInMillis();
			break;
		case CYCLE_MONTH:
			cal.set(Calendar.DATE, 1);
			to = cal.getTimeInMillis();
			cal.set(Calendar.MONDAY,cal.get(Calendar.MONDAY)-1);
			from = cal.getTimeInMillis();
			break;
		case CYCLE_QUARTER:
			cal.set(Calendar.DATE, 1);
			int month = cal.get(Calendar.MONTH);
			if(month>=0&&month<3){
				cal.set(Calendar.YEAR,cal.get(Calendar.YEAR)-1);
				cal.set(Calendar.MONDAY,9);
			}else if(month>=4&&month<7){
				cal.set(Calendar.MONDAY,0);
			}else if(month>=7&&month<10){
				cal.set(Calendar.MONDAY,3);
			}else if(month>=10&&month<12){
				cal.set(Calendar.MONDAY,6);
			}
			from = cal.getTimeInMillis();
			cal.set(Calendar.MONDAY,cal.get(Calendar.MONDAY)+3);
			to = cal.getTimeInMillis();
			break;
		case CYCLE_YEAR:
			cal.set(Calendar.DATE, 1);
			cal.set(Calendar.MONDAY, 0);
			to = cal.getTimeInMillis();
			cal.set(Calendar.YEAR,cal.get(Calendar.YEAR)-1);
			from = cal.getTimeInMillis();
			break;
		}
//		System.out.println("from:"+new Date(from).toLocaleString()+"      "+new Date(to).toLocaleString());
		if(from==-1l||to==-1l) return null;
		
		List<Object[]> result = new ArrayList<Object[]>();
		User user = Common.getUser();
		Permissions.hasStatisticsInformationPermission(user);
		StatisticsInformationDao informationDao = new StatisticsInformationDao();
		StatisticsScriptDao scriptDao = new StatisticsScriptDao();
		StatisticsScriptVO scriptVO;
		if(scriptId==StatisticsUtil.INDEX_OF_HEALTH||scriptId==StatisticsUtil.RATE_OF_TROUBLE_HANDLE){
			scriptVO = StatisticsUtil.getIndexScript(scriptId,cycle);
		}
		else{
			scriptVO = StatisticsUtil.getIndexScript(scriptId);
		}
		boolean canStart = false;
		while(from<=to){
			double value = informationDao.getResultByScriptBetweenTime(scriptVO,user.getCurrentScope(),user,from,to);
			if(canStart==false&&value!=-1d){
				canStart = true;
			}
			if(canStart){
				Object[] object = new Object[2];
				object[0]=from;
				object[1]=value;
				result.add(object);	
			}
			from+=MS_IN_STATISTIC_CYCLE;
		}
		if(result.size()>0){
			//去除尾部没有数据的部分
			boolean lastIsNull = true;
			while(lastIsNull){
				Object[] last = result.get(result.size()-1);
				if((Double)last[1]==-1D){
					lastIsNull = true;
					result.remove(last);
				}else{
					lastIsNull = false;
				}
			}
			if(result.size()>0){
				return result;
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
	
	
	
	
	
	
	/*************************************************节能潜力查询***************************************************/
	
	/**
	 * 节能潜力查询页面初始化
	 * @return 页面加载需要的数据
	 */
	public DwrResponseI18n potentialSearchInit(){
		User user = Common.getUser();
		Permissions.hasStatisticsInformationPermission(user);
		DwrResponseI18n response = new DwrResponseI18n();
		List<ScopeVO> childScope = null;
		List<ScopeVO> grandchildScope = null;
		List<ScopeVO> factoryList = null;
		response.addData("childScope",childScope);
		response.addData("grandchildScope",grandchildScope);
		//显示当前范围下所有工厂
		ScopeDao scopeDao = new ScopeDao();
		if(user.getCurrentScope().getScopetype()==ScopeVO.ScopeTypes.HQ){
			childScope = getChildScope();
			if(childScope!=null&&childScope.size()>0)
			grandchildScope = getGrandchildScope(childScope.get(0).getId());
			factoryList = scopeDao.searchFactory(user,-1, -1, -1, "","");
		}else if(user.getCurrentScope().getScopetype()==ScopeVO.ScopeTypes.ZONE){
			grandchildScope = getGrandchildScope(user.getCurrentScope().getId());
			factoryList = scopeDao.searchFactory(user,user.getCurrentScope().getId(),-1, -1,"",""); 
		}else if(user.getCurrentScope().getScopetype()==ScopeVO.ScopeTypes.SUBZONE){
			factoryList = scopeDao.searchFactory(user,user.getCurrentScope().getParentScope().getId(),user.getCurrentScope().getId(), -1,"",""); 
		}
		response.addData("childScope",childScope);
		response.addData("grandchildScope",grandchildScope);
		response.addData("factoryList",factoryList);
		return response;
	}
	
	/**
	 * 获取子范围集合
	 * @return 子范围集合
	 */
	public List<ScopeVO> getChildScope(){
		User user = Common.getUser();
		ScopeDao scopeDao = new ScopeDao();
		List<ScopeVO> childScopeList = null;
		if(user.getCurrentScope().getScopetype()==ScopeVO.ScopeTypes.HQ){
			childScopeList = scopeDao.getZoneList();
		}else if(user.getCurrentScope().getScopetype()==ScopeVO.ScopeTypes.ZONE){
			childScopeList = scopeDao.getsubZoneList(user.getCurrentScope().getId());
		}else if(user.getCurrentScope().getScopetype()==ScopeVO.ScopeTypes.SUBZONE){
			childScopeList = scopeDao.getFactoryBySubZone(user.getCurrentScope().getId());
		}
		UserDao.validateScopes(childScopeList,user);
		return childScopeList;
	}
	
	/**
	 * 获取子范围的子范围
	 * @param childScopeId 子范围编号
	 * @return 子范围的子范围的集合
	 */
	public List<ScopeVO> getGrandchildScope(int childScopeId){
		User user = Common.getUser();
		ScopeDao scopeDao = new ScopeDao();
		List<ScopeVO> childScopeList = scopeDao.getsubZoneList(childScopeId);
		UserDao.validateScopes(childScopeList,user);
		return childScopeList;
	}
	
	
	
	/**
	 * 节能潜力查询(查询工厂)
	 * @param childId 区域条件
	 * @param grandchildId 子区域条件
	 * @param countOfMachine 工厂空压机台数超过
	 * @param volumetricOfMachine 工厂总装机容量
	 * @param unloadRate 卸载率大于
	 * @param pressureWave 压力波动大于
	 * @param systemPressureDrop 系统压降大于
	 * @param cycle 周期选择
	 * @param hasRunningInSameTime 工厂内所有机器曾经同时开启过
	 * @return 筛选出来的工厂
	 */
	public DwrResponseI18n potentialSearch(int childId,int grandchildId,int countOfMachine,double volumetricOfMachine,int unloadRate,int pressureWave,int systemPressureDrop,int cycle,boolean hasRunningInSameTime){
		User user = Common.getUser();
		Permissions.hasStatisticsInformationPermission(user);
		DwrResponseI18n response = new DwrResponseI18n();
		ScopeDao scopeDao = new ScopeDao();
		StatisticsScriptDao scriptDao = new StatisticsScriptDao();
		StatisticsInformationDao informationDao = new StatisticsInformationDao();
		long from = -1;
		long to = -1;
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);
		switch(cycle){
		case CYCLE_YESTERDAY:
			to = cal.getTimeInMillis();
			cal.set(Calendar.DATE,cal.get(Calendar.DATE)-1);
			from = cal.getTimeInMillis();
			break;
		case CYCLE_WEEK:
			cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
			to = cal.getTimeInMillis();
			cal.set(Calendar.WEEK_OF_YEAR,cal.get(Calendar.WEEK_OF_YEAR)-1);
			from = cal.getTimeInMillis();
			break;
		case CYCLE_MONTH:
			cal.set(Calendar.DATE, 1);
			to = cal.getTimeInMillis();
			cal.set(Calendar.MONDAY,cal.get(Calendar.MONDAY)-1);
			from = cal.getTimeInMillis();
			break;
		case CYCLE_QUARTER:
			cal.set(Calendar.DATE, 1);
			int month = cal.get(Calendar.MONTH);
			if(month>=0&&month<3){
				cal.set(Calendar.YEAR,cal.get(Calendar.YEAR)-1);
				cal.set(Calendar.MONDAY,9);
			}else if(month>=4&&month<7){
				cal.set(Calendar.MONDAY,0);
			}else if(month>=7&&month<10){
				cal.set(Calendar.MONDAY,3);
			}else if(month>=10&&month<12){
				cal.set(Calendar.MONDAY,6);
			}
			from = cal.getTimeInMillis();
			cal.set(Calendar.MONDAY,cal.get(Calendar.MONDAY)+3);
			to = cal.getTimeInMillis();
			break;
		case CYCLE_YEAR:
			cal.set(Calendar.DATE, 1);
			cal.set(Calendar.MONDAY, 0);
			to = cal.getTimeInMillis();
			cal.set(Calendar.YEAR,cal.get(Calendar.YEAR)-1);
			from = cal.getTimeInMillis();
			break;
		}
//		System.out.println("from:"+new Date(from).toLocaleString()+"      "+new Date(to).toLocaleString());
		
		List<ScopeVO> factoryList = new ArrayList<ScopeVO>();
		//根据范围筛选工厂
		if(user.getCurrentScope().getScopetype()==ScopeVO.ScopeTypes.HQ){
			factoryList = scopeDao.searchFactory(user,childId,grandchildId,-1,"","");
		}else if(user.getCurrentScope().getScopetype()==ScopeVO.ScopeTypes.ZONE){
			if(grandchildId==-1)//查询一个当前区域下的
				factoryList = scopeDao.searchFactory(user,user.getCurrentScope().getId(),childId,-1,"","");
			else{
				factoryList = scopeDao.searchFactory(user,user.getCurrentScope().getId(),grandchildId,-1,"","");
			}
		}else if(user.getCurrentScope().getScopetype()==ScopeVO.ScopeTypes.SUBZONE){
			factoryList = scopeDao.searchFactory(user,user.getCurrentScope().getParentScope().getId(),user.getCurrentScope().getId(),-1,"","");
		}
		if(factoryList==null||factoryList.size()==0){
			response.addData("factoryList",null);
			return response;
		}
		
		List<Integer> result = new ArrayList<Integer>();
		for(ScopeVO scope:factoryList){
			if(scope.isDisabled())
				continue;
			result.add(scope.getId());
		}
		if(result==null||result.size()==0){
			response.addData("factoryList",null);
			return response;
		}
		//根据   空压机台数大于 countOfMachine	筛选工厂
		/****交集*********/
		if(countOfMachine!=-1)
	 	result = unionUtil(result,informationDao.findCountOfMachineThan(countOfMachine));
		if(result==null||result.size()==0){
			response.addData("factoryList",null);
			return response;
		}
		/***
		 * 表结构发生变化，装机容量作为字段存放在空压机表中
		 * 
		 */
		if(volumetricOfMachine!=-1)
	 	result = informationDao.findVolumetricOfMachineThan(result,volumetricOfMachine);
		if(result==null||result.size()==0){
			response.addData("factoryList",null);
			return response;
		}
		//卸载率筛选	
		if(unloadRate!=0){//这里改为0，1
		 	result = unionUtil(result,informationDao.findUnloadRateThan(result,StatisticsUtil.ENERGY_SAVING_TARGET_NO2,from,to,unloadRate ));
			if(result==null||result.size()==0){
				response.addData("factoryList",null);
				return response;
			}
		}
	 	//压力波动筛选
		if(pressureWave!=0){
		 	result = informationDao.findPressureWaveThan(result,StatisticsUtil.ENERGY_SAVING_TARGET_NO1,from,to,pressureWave );
			if(result==null||result.size()==0){
				response.addData("factoryList",null);
				return response;
			}
		}
	 	//系统压降筛选
	 	if(systemPressureDrop!=0){
		 	result = informationDao.findPressureWaveThan2(result,StatisticsUtil.ENERGY_SAVING_TARGET_NO3,from,to,systemPressureDrop );
		 	if(result==null||result.size()==0){
				response.addData("factoryList",null);
				return response;
			}
	 	}
	 	//曾经所有机器同时开过的厂家
	 	if(hasRunningInSameTime==true){
	 		//过滤掉的工厂ID
		 	List<Integer> passFactory = new ArrayList<Integer>(); 
		 	for(int i=0;i<result.size();i++){
		 		int acpCount = informationDao.getAcpCountByFactory(result.get(i));
		 		if(acpCount<1){
		 			passFactory.add(result.get(i));
		 			continue;
		 		}
		 		int maxCount = informationDao.runInSameTimeMaxCountInFactory(result.get(i),StatisticsUtil.STATISTICS_PARAM_ACP_STATUS_RUN_STOP,from,to);
		 		if(maxCount==-1||acpCount!=maxCount){
		 			passFactory.add(result.get(i));
		 		}
		 	}
		 	result = union2Util(result,passFactory);
	 	}
	 	/**
	 	 * 根据查出来的ID查询详情
	 	 */
	 	if(result.size()>0){
	 		response.addData("factoryList",scopeDao.findByIds(result));
	 	}else{
	 		response.addData("factoryList",null);
	 	}
		return response;
	}
	
	
	/**
	 * 获取两个工厂结合的交集
	 * @param list1 集合1
	 * @param list2 集合2
	 * @return 集合1和集合2的交集
	 */
	private List<Integer> unionUtil(List<Integer> list1,List<Integer> list2 ){
		List<Integer> unionScopeList = new ArrayList<Integer>();
		for(int i=0;i<list2.size();i++){
			if(list1.contains(list2.get(i))){
				unionScopeList.add(list2.get(i));
			}
		}
		return unionScopeList;
	}
	
	private List<Integer> union2Util(List<Integer> list1,List<Integer> list2 ){
		for(int i=0;i<list2.size();i++){
			if(list1.contains(list2.get(i))){
				list1.remove(list2.get(i));
			}
		}
		return list1;
	}
	
}
