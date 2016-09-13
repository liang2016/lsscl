package com.serotonin.mango.db.dao.statistics;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import com.serotonin.db.spring.GenericRowMapper;
import com.serotonin.mango.db.dao.BaseDao;
import com.serotonin.mango.db.dao.scope.ScopeDao;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.vo.acp.ACPVO;
import com.serotonin.mango.vo.scope.ScopeVO;
import com.serotonin.mango.vo.statistics.ScheduledStatisticVO;
import com.serotonin.mango.vo.statistics.ScopeResultVO;
import com.serotonin.mango.vo.statistics.StatisticsScriptVO;
import com.serotonin.mango.vo.statistics.StatisticsVO;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import com.serotonin.mango.rt.statistic.common.StatisticsUtil;

/**
 * 统计信息查询
 * @author 王金阳
 *
 */
public class StatisticsInformationDao extends BaseDao{
	
	/** 机器状态     运行停机一个点      加载卸载一个点    有没有报警一个点 
	 *****/
	public static final int ACP_STATUS_RUN = 1;
	public static final int ACP_STATUS_STOP = 0;
	public static final int ACP_STATUS_RUN_ONLOAD = 1;
	public static final int ACP_STATUS_RUN_UNLOAD = 0;
	public static final int ACP_STATUS_STOP_NOALARM = 0;
	public static final int ACP_STATUS_STOP_ALARM = 1;
	
	/**
	 * 查询不同状态的机器的个数(运行/停机)
	 */
	public static final String SELECT_ACP_COUNT_BY_STATUS_HEADER = " select acp.id,dps.id from aircompressor acp "
			+" left join aircompressor_members acpm on acp.id = acpm.acid "
			+" left join statisticsparam stp on acpm.spid = stp.id "
			+" left join dataPoints dps on dps.id = acpm.dpid "
		//	+" left join pointvalues pvs on dps.id = pvs.dataPointId "
			+" where acp.type=? and stp.useType = ? and stp.id = ? "
		//	+" and pvs.ts = (select max(ts) from pointvalues pvs2 where pvs2.dataPointId = pvs.dataPointId) and pvs.pointValue = ? "
			+" and  "; 
	
	//总部普通用户能访问的工厂
	public static final String SQL_USER_FACTORY_IN_HQ =  " " +
		   " in "
	      +" ( "
		  +" select id from scope where parentid in " 
		  +"      ("
		  +"       select id from scope where parentid in " 
		  +"	  		( "
		  +"       		 select s.id from user_scope us  left join users u on us.uid = u.id left "
		  +" 			 join scope s on s.id = us.scopeid where s.parentid = ? and u.id = ? " 
		  +"	   		) "
		  +"   	  ) "
		  +" ) ";
	//总部admin用户能访问的工厂
	public static final String SQL_ADMIN_FACTORY_IN_HQ =  " " +
		   " in "
	      +" ( "
		  +" select id from scope where parentid in " 
		  +"      ("
		  +"       select id from scope where parentid in " 
		  +"	  		( "
		  +"			 select id from scope where parentid = ? "
		  +"	   		) "
		  +"   	  ) "
		  +" ) ";
	//某个区域普通用户能访问的的工厂
	public static final String SQL_USER_FACTORY_IN_ZONE = " " +
			" in "
		   +" ( "
		   +"  select id from scope where parentid in " 
	       +"      ( "
	       +"       	select s.id from user_scope us  left join users u on us.uid = u.id left "
		   +" 			join scope s on s.id = us.scopeid where s.parentid = ? and u.id = ? " 
		   +"      ) "
		   +" ) ";
	//某个区域admin用户能访问的的工厂
	public static final String SQL_ADMIN_FACTORY_IN_ZONE = " " +
			" in "
		   +" ( "
		   +"  select id from scope where parentid in " 
	       +"      ( "
	       +"       	select id from scope where parentid = ? "
		   +"      ) "
		   +" ) ";
	//某个子区域普通用户能访问的工厂
	public static final String SQL_USER_FACTORY_IN_SUBZONE = " " +
		   " in "
	      +" ( "
	      +"       select s.id from user_scope us  left join users u on us.uid = u.id left "
		  +" 	   join scope s on s.id = us.scopeid where s.parentid = ? and u.id = ? " 
		  +" ) ";
	//某个子区域admin用户能访问的工厂
	public static final String SQL_ADMIN_FACTORY_IN_SUBZONE = " " +
		   " in "
	      +" ( "
	      +"       	select id from scope where parentid = ? "
		  +" ) ";
	//没有记录则返回-1;
	public static final int NO_RECORD = -1;
	
	/**
	 * 根据统计范围生成不同的SQL条件
	 * @param scope 统计范围
	 * @return SQL条件
	 */
	private String getSQLByScope(ScopeVO scope,User user){
		ScopeDao scopeDao = new ScopeDao();
		String sql = " ";
		switch(scope.getScopetype()){
		case ScopeVO.ScopeTypes.FACTORY:
			sql += " = ? ";
			break;
		case ScopeVO.ScopeTypes.SUBZONE:
			if(user.isAdmin()) {
				sql += SQL_ADMIN_FACTORY_IN_SUBZONE;
			}else{
				if(scopeDao.isMyChild(user.getHomeScope().getId(),scope.getId())){
					sql += SQL_ADMIN_FACTORY_IN_SUBZONE;
				}else{
					sql += SQL_USER_FACTORY_IN_SUBZONE;
				}
			}
			break;
		case ScopeVO.ScopeTypes.ZONE:
			if(user.isAdmin()){
				sql += SQL_ADMIN_FACTORY_IN_ZONE;
			}else{
				if(scopeDao.isMyChild(user.getHomeScope().getId(),scope.getId())){
					sql += SQL_ADMIN_FACTORY_IN_ZONE;
				}else{
					sql += SQL_USER_FACTORY_IN_HQ;
				}
			}
			break;
		case ScopeVO.ScopeTypes.HQ:
			if(user.isAdmin()){
				sql += SQL_ADMIN_FACTORY_IN_HQ;
			}else{
				if(scopeDao.isMyChild(user.getHomeScope().getId(),scope.getId())){
					sql += SQL_ADMIN_FACTORY_IN_HQ;
				}else{
					sql += SQL_USER_FACTORY_IN_HQ;
				}
			}
			break;
		}
		return sql;
	}
	
	/**
	 * 对应getSQLByScope()方法中的参数
	 * @param scope 统计范围
	 * @return SQL条件
	 */
	private List<Object> getSQLParamsByScope(ScopeVO scope,User user){
		List<Object> params = new ArrayList<Object>();
		params.add(scope.getId());
		if(!user.isAdmin()&&!new ScopeDao().isMyChild(user.getHomeScope().getId(),scope.getId())) {
			params.add(user.getId());
		}
		return params;
	}
	
	/**
	 * 获取总机器数量
	 * @param scope 统计范围
	 * @return 总计期数量
	 */
	public Integer getCountOfMachine(ScopeVO scope,User user){
		String sql = " select count(*) from aircompressor where type = ? and factoryid  "+getSQLByScope(scope,user);
		List<Object> params = new ArrayList<Object>();
		params.add(ACPVO.ACP_TYPE);
		params.addAll(getSQLParamsByScope(scope,user));
		return ejt.queryForInt(sql,params.toArray(), 0);
	}
//	
//	/**
//	 * 获取装机容量
//	 * @param scope 统计范围
//	 * @param user 用户信息
//	 * @return	装机容量
//	 */
//	public Double getVolumetricOfMachine(ScopeVO scope,User user,int spid,long from,long to){
//		String sql = " select sum(pointvalue) from pointvalues pv "+
//					 " left join dataPoints ds on ds.id = pv.dataPointId "+
//					 " left join aircompressor_members acpm on acpm.dpid = ds.id "+
//					 " left join statisticsparam sstp on sstp.id = acpm.spid "+
//					 " left join aircompressor acp on acp.id = acpm.acid "+
//					 " where sstp.id = ? "+
//					 " acp.type = ? "+
//					 " and pv.ts ? and ts ? "+
//					 " and acp.factoryid "+
//					 getSQLByScope(scope,user);
//		List<Object> params = new ArrayList<Object>();
//		params.add(spid);
//		params.add(ACPVO.ACP_TYPE);
//		params.add(from);
//		params.add(to);
//		params.addAll(getSQLParamsByScope(scope,user));
//		List<Double> volumetrics = ejt.queryForList(sql,params.toArray(),Double.class);
//		if(volumetrics.size()>0)return volumetrics.get(0);
//		else return -1d;
//	}
	
	/**
	 * 查询不同状态的机器(运行/停机)
	 */
	private List<Integer> getCountOfMachineByStatus(ScopeVO scope,User user,int spid,int status){
		String sql = SELECT_ACP_COUNT_BY_STATUS_HEADER+" factoryid "+getSQLByScope(scope,user);
		List<Object> params = new ArrayList<Object>();
		params.add(ACPVO.ACP_TYPE);
		params.add(StatisticsVO.UseTypes.USE_ACP);
		params.add(spid);
		//params.add(status);
		params.addAll(getSQLParamsByScope(scope, user));
		List<AcpDpIdResultVO> list=query(sql, params.toArray(),new acpDpIdResultVORowMapper());
		sql="";
		if(list.size()<=0)
			return  new ArrayList<Integer>();;
		for(int i=0;i<list.size();i++){
			AcpDpIdResultVO  vo=list.get(i);
			sql+="select "+vo.getDpId()+" as pointId from  pointvalues_"+vo.getDpId()+"  where  ts=(select max(ts) from pointValues_"+vo.getDpId()+"  ) and pointValue =0";
			if(i<list.size()-1)
				sql+=" union all ";
		}
		System.out.println("getCountOfMachineByStatus:"+sql);
		List<Integer> result = ejt.queryForList(sql, new Object[]{},Integer.class);
		List<Integer> returnData=new ArrayList<Integer>();
		for(int i=0;i<result.size();i++){
			for(int j=0;j<list.size();j++){
				if(result.get(i)==list.get(j).getDpId()){
					returnData.add(result.get(i));
					continue;
				}
				list.remove(j);
			}
		}
		return result;
	}
	
	/**
	 * 查询不同状态的机器(加载/卸载/停机/故障停机)
	 */
	private int getCountOfMachineByStatus2(List<Integer> acpIds ,int spid,int status){
		if(acpIds==null||acpIds.size()==0) return 0;
		String sql = SELECT_ACP_COUNT_BY_STATUS_HEADER+" acp.id in ( ";
		List<Object> params = new ArrayList<Object>();
		params.add(ACPVO.ACP_TYPE);
		params.add(StatisticsVO.UseTypes.USE_ACP);
		params.add(spid);
		//params.add(status);
		for(int i=0;i<acpIds.size();i++){
			sql += " ? ";
			if(i+1<acpIds.size()){
				sql+=" , ";
			}
			params.add(acpIds.get(i));
		}
		sql+=" ) ";
		List<AcpDpIdResultVO> list =query(sql, params.toArray(),new acpDpIdResultVORowMapper());
		if(list.size()<=0){
			return 0;
		}
		sql="";
		for(int i=0;i<list.size();i++){
			AcpDpIdResultVO  vo=list.get(i);
			sql+="select "+vo.getDpId()+" as pointId from  pointvalues_"+vo.getDpId()+"  where  ts=(select max(ts) from pointValues_"+vo.getDpId()+"  ) and pointValue =0";
			if(i<list.size()-1)
				sql+=" union all ";
		}
		List<Integer> result = ejt.queryForList(sql, new Object[]{},Integer.class);
		List<Integer> returnData=new ArrayList<Integer>();
		for(int i=0;i<result.size();i++){
			for(int j=0;j<list.size();j++){
				if(result.get(i)==list.get(j).getDpId()){
					returnData.add(result.get(i));
					continue;
				}
				list.remove(j);
			}
		}
		return returnData.size();
	}
	
	/**
	 * 取出acp和dpid键值对
	 * @author 刘建坤
	 *
	 */
	class acpDpIdResultVORowMapper implements GenericRowMapper<AcpDpIdResultVO> {
		public AcpDpIdResultVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			AcpDpIdResultVO resultVO=new AcpDpIdResultVO();
			resultVO.setAcpId(rs.getInt(1));
			resultVO.setDpId(rs.getInt(2));
			return resultVO;
		}
	}
	class AcpDpIdResultVO{
		private int acpId;
		private int dpId;
		public int getAcpId() {
			return acpId;
		}
		public void setAcpId(int acpId) {
			this.acpId = acpId;
		}
		public int getDpId() {
			return dpId;
		}
		public void setDpId(int dpId) {
			this.dpId = dpId;
		}
	}
	
	/**
	 * 获取机器加载数量
	 * @param scope 统计范围
	 * @return 机器加载数量
	 */
	public Integer getCountOfOnloadMachine(ScopeVO scope,User user){
		List<Integer> result1 = getCountOfMachineByStatus(scope,user,StatisticsUtil.STATISTICS_PARAM_ACP_STATUS_RUN_STOP,ACP_STATUS_RUN);
		return getCountOfMachineByStatus2(result1,StatisticsUtil.STATISTICS_PARAM_ACP_STATUS_RUN_LOAD,ACP_STATUS_RUN_ONLOAD);
	}
	
	/**
	 * 获取机器卸载数量
	 * @param scope 统计范围
	 * @return 机器卸载数量
	 */
	public Integer getCountOfUnloadMachine(ScopeVO scope,User user){
		List<Integer> result1 = getCountOfMachineByStatus(scope,user,StatisticsUtil.STATISTICS_PARAM_ACP_STATUS_RUN_STOP,ACP_STATUS_RUN);
		return getCountOfMachineByStatus2(result1,StatisticsUtil.STATISTICS_PARAM_ACP_STATUS_RUN_LOAD,ACP_STATUS_RUN_UNLOAD);
	}
	
	/**
	 * 获取机器停机数量
	 * @param scope 统计范围
	 * @return 机器停机数量
	 */
	public Integer getCountOfShutdownMachine(ScopeVO scope,User user){
		List<Integer> result1 = getCountOfMachineByStatus(scope,user,StatisticsUtil.STATISTICS_PARAM_ACP_STATUS_RUN_STOP,ACP_STATUS_STOP);
		return result1.size();
	}
	
	/**
	 * 获取故障停机数量
	 * @param scope 统计范围
	 * @return 故障停机数量
	 */
	public Integer getCountOfShutdownInTroubleMachine(ScopeVO scope,User user){
		List<Integer> result1 = getCountOfMachineByStatus(scope,user,StatisticsUtil.STATISTICS_PARAM_ACP_STATUS_RUN_STOP,ACP_STATUS_STOP);
		return getCountOfMachineByStatus2(result1,StatisticsUtil.STATISTICS_PARAM_ACP_STATUS_RUN_ALARM,ACP_STATUS_STOP_ALARM);
	}
	
	/**
	 * 获取多个脚本在某个范围内两个时刻之间的统计结果的集合
	 * @param scriptVO  脚本信息
	 * @param scopeList 脚本集合
	 * @param user      用户信息
	 * @param from 		开始时间
	 * @param to        结束时间
	 * @return
	 */
	public List<Double> getResultByScriptBetweenTimeIncludeSomeScope(StatisticsScriptVO scriptVO,List<ScopeVO> scopeList,User user,long from,long to){
		List<Double> results = new ArrayList<Double>();
		for(ScopeVO scopeVO:scopeList){
			results.add(getResultByScriptBetweenTime(scriptVO,scopeVO,user,from,to));
		}
		return results;
	}
	
	
	/**
	 * 获取某个脚本在某个范围内两个时刻之间的统计结果的集合
	 * @param scriptVO 脚本信息
	 * @param scope    统计范围
	 * @user  user	   用户信息
	 * @param from 	   开始事件
	 * @param to	   结束事件
	 * @return 统计结果的集合
	 */
	public Double getResultByScriptBetweenTime(StatisticsScriptVO scriptVO,ScopeVO scope,User user,long from,long to){
		List<Object> params = new ArrayList<Object>();
		String sql = " select avg(value) from scheduledstatistic where scriptid = ? "
//					+" and ts = (select min(ts) from scheduledstatistic where scriptid = ?)  "
					+" and ts <= ? and ts > ? "
					+" and unitType = ? "
					+" and unitid in ( ";
//		params.add(scriptVO.getId());
		params.add(scriptVO.getId());
		params.add(to);
		params.add(from);
		if(scriptVO.getUnitType()==StatisticsScriptVO.UnitTypes.STATISTIC_UNIT_MACHINE&&scriptVO.getCycleType()!=0){
			params.add(scriptVO.getCycleType());
		}
		else{
			params.add(scriptVO.getUnitType());
		}
		boolean selectFactory=false;
		boolean isFactory=false;
		if(scriptVO.getUnitType()==StatisticsScriptVO.UnitTypes.STATISTIC_UNIT_MACHINE){
			sql += " 				  	  select id from aircompressor where type = ? and factoryid ";
			params.add(ACPVO.ACP_TYPE);
		}
		else if((scriptVO.getUnitType()==StatisticsScriptVO.UnitTypes.STATISTIC_UNIT_SYSTEM)&&scriptVO.getId()!=0){
			sql += " 				  	  select id from aircompressor_system where factoryid ";	
		}
		else if((scriptVO.getUnitType()==StatisticsScriptVO.UnitTypes.STATISTIC_UNIT_FACTORY)&&scriptVO.getId()==0&&scope.getScopetype()<3){
			sql += " 				  	  select id  from ";
			selectFactory=true;
		}
		else if((scriptVO.getUnitType()==StatisticsScriptVO.UnitTypes.STATISTIC_UNIT_FACTORY)&&scriptVO.getId()==0&&scope.getScopetype()==3){
			sql += " ?";
			isFactory=true;
		}
		String temp=getSQLByScope(scope,user);
		if(selectFactory){
			temp=temp.substring(temp.indexOf("in")+2);
		}
		if(!isFactory)
			sql += temp;
		if(selectFactory)
			sql+=" as ids";
		params.addAll(getSQLParamsByScope(scope,user));
		sql += " 					 ) "; 
		return (Double)ejt.queryForObject(sql,params.toArray(),Double.class,-1D);
	}
	
	
	/**
	 * 获取某个范围scope在两个时间点from，to之间的故障处理率 
	 * @param scope 范围编号
	 * @param user  范围信息
	 * @param from  开始时间
	 * @param to    结束时间
	 * @return 故障处理率
	 */
	public Double getSolveTroubleRateBewteenTime(ScopeVO scope,User user,long from,long to){
		String countOfEventsSql  = " select count(*) from events where scopeid " + getSQLByScope(scope,user) + " and activeTs > ? and activeTs < ? ";
		List<Object> countOfEventsParams = new ArrayList<Object>();
		countOfEventsParams.addAll(getSQLParamsByScope(scope,user));
		countOfEventsParams.add(from);
		countOfEventsParams.add(to);
		int countOfEvents = ejt.queryForInt(countOfEventsSql,countOfEventsParams.toArray(),0);
		if(countOfEvents==0) return 1.0D;
		String countOfSolveEventsSql = " select count(*) from events where scopeid " + getSQLByScope(scope,user) + " and activeTs > ? and activeTs <= ?  and ackTs is not null  ";
		int countOfSolveEvents = ejt.queryForInt(countOfSolveEventsSql,countOfEventsParams.toArray(),0);
		return (double)countOfSolveEvents/(double)countOfEvents;
	}
	
//	/**
//	 * 获取多个范围在同一个时间段内的故障处理率
//	 * @param scopeList 范围集合
//	 * @param user      当前用户
//	 * @param from      开始时间
//	 * @param to        结束时间
//	 * @return   故障处理率(多个范围的)集合
//	 */
//	public List<Double> getSolveTroubleRateBewteenTimeIncludeSomeScope(List<ScopeVO> scopeList,User user,long from,long to){
//		List<Double> results = new ArrayList<Double>();
//		for(ScopeVO scopeVO:scopeList){
//			results.add(getSolveTroubleRateBewteenTime(scopeVO,user,from,to));
//		}
//		return results;
//	}
	
	
	/**
	 * 获取某个范围一段时间内故障处理率的历史记录
	 * @param scope 范围信息
	 * @param user  用户信息
	 * @param from  开始时间
	 * @param to    结束时间
	 * @param timestamp  时间间隔
	 * @return 历史记录
	 */
	public List<Double> getSolveTroubleRateHistory(ScopeVO scope,User user,long from,long to,long timestamp){
		List<Double> historyList = new ArrayList<Double>();
		while(from <=to){
			historyList.add(getSolveTroubleRateBewteenTime(scope,user,from,to));
			from+=timestamp;
		}
		return historyList;
	}
	
	/**
	 * 获取空压机台数超过num的工厂
	 * @param num 空压机数量
	 * @return 工厂列表
	 */
	public List<Integer> findCountOfMachineThan(int num){
		String sql = "  select s.id from scope s where scopetype = ? and id in (" +
				"		   select factoryid from aircompressor where type = ?  group by factoryid having count(*) >  ?" +
				" 		) ";
		return queryForList(sql,new Object[]{ScopeVO.ScopeTypes.FACTORY,ACPVO.ACP_TYPE,num},Integer.class );
	}
	
	/**
	 * 获取总装机容量超过num的工厂
	 * @param num 总装机容量
	 * @return 工厂列表
	 */
	public List<Integer> findVolumetricOfMachineThan(List<Integer> factoryIds,double num){
//		String sql = " select s.id  from scope s "+
//						" left join aircompressor acp on s.id = acp.factoryid "+
//						" left join aircompressor_members acpm on acpm.acid = acp.id "+
//						" left join pointvalues pv on pv.dataPointId = acpm.dpid "+
//						" where s.scopetype = ?  "+ //过滤工厂
//						" and acpm.spid = ? "+ //装机容量统计参数的ID
//						" and pv.ts > ? and ts < = ? "+//-- 时间为上一个小时的最后一分钟的最后20秒
//						" group by s.id having sum(pointvalue) > ?  ";
//		return queryForList(sql,new Object[]{ScopeVO.ScopeTypes.FACTORY,spid,from,to,num},Integer.class);
		
		/**
		 * 表机构发生变化，装机容量作为字段存放在空压机表中
		 */
		List<Object> params = new ArrayList<Object>();
		String sql = " select s.id from scope s "+
					 " left join aircompressor acp on s.id = acp.factoryId "+
					 " where acp.type = ? "+
					 " and s.scopetype = ?  "+
					 " and s.id in ( ";	
		params.add(ACPVO.ACP_TYPE);
		params.add(ScopeVO.ScopeTypes.FACTORY);
		for(int i=0;i<factoryIds.size();i++){
			sql += " ? ";
			if(i+1<factoryIds.size()){
				sql += " , ";
			}
			params.add(factoryIds.get(i));
		}
		sql += " ) group by s.id having sum(acp.volume) > ? ";
		params.add(num);			 
		return queryForList(sql,params.toArray(),Integer.class);
		
	}
	
	
	/**
	 * 查询卸载率时候临时用上的实体
	 * @author 王金阳
	 *
	 */
	class ScopeResultVORowMapper implements GenericRowMapper<ScopeResultVO> {
		public ScopeResultVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ScopeResultVO resultVO = new ScopeResultVO();
			int i = 1;
			resultVO.setScopeId(rs.getInt(i++)); 
			resultVO.setValue(rs.getDouble(i++)); 
			return resultVO;
		}
	}
	
	/**
	 * 查询卸载率超过num的工厂
	 * @param onload 加载时间统计参数ID
	 * @param unload 卸载时间统计参数ID
	 * @param from   开始时间
	 * @param to     结束时间
	 * @param num    比较值
	 * @return 符合条件的工厂列表
	 */
	public List<Integer> findUnloadRateThan(List<Integer> factoryIds,int target,long from,long to,int num ){
		/**
		 * 卸载率大于=全载率小于
		 */
		String sql = " select airs.factoryId from scheduledStatistic ss "+
					 " left join aircompressor_system airs on airs.id = ss.unitId "+
					 " where airs.factoryId in ( ";
		List<Object> params = new ArrayList<Object>();
		for(int i=0;i<factoryIds.size();i++){
			sql += " ? ";
			if(i < factoryIds.size()-1){
				sql += " , ";
			}
			params.add(factoryIds.get(i));
		}
		sql += " ) ";
		sql+=" and ss.scriptId = ? "+
			 " and ss.ts > ? and ts <=? "+
			 " group by airs.factoryId ";
			 if(num==1)
				 sql+= " having avg(ss.value) <0.8 ";
		params.add(target);
		params.add(from);
		params.add(to);
	//	params.add(0.8);//
		return queryForList(sql,params.toArray(),Integer.class);
		
	}
	
	/**
	 * 筛选压力波动超过num的工厂
	 * @param factoryIds 之前已经筛选出来的工厂
	 * @param target 脚本ID 
	 * @param from 开始时间
	 * @param to 结束时间
	 * @param num 超过多少
	 * @return 工厂ID
	 */
	public List<Integer> findPressureWaveThan(List<Integer> factoryIds,int target,long from,long to,int num ){
		String sql = " select airs.factoryId from scheduledStatistic ss "+
		 " left join aircompressor_system airs on airs.id = ss.unitId "+
		 " where airs.factoryId in ( ";
		List<Object> params = new ArrayList<Object>();
		for(int i=0;i<factoryIds.size();i++){
		sql += " ? ";
		if(i < factoryIds.size()-1){
			sql += " , ";
		}
		params.add(factoryIds.get(i));
		}
		sql += " ) ";
		sql+=" and ss.scriptId = ? "+
		" and ss.ts > ? and ss.ts<= ?  "+
		" group by airs.factoryId ";
		if(num==1)
			sql+="  having (max(ss.value)-min(ss.value)/max(ss.value)) >0.1 ";
		params.add(target);
		params.add(from);
		params.add(to);
		//params.add(num);
		return queryForList(sql,params.toArray(),Integer.class);
	
	}
	
	
	/**
	 * 筛选压力波动超过num的工厂
	 * @param factoryIds 之前已经筛选出来的工厂
	 * @param target 脚本ID 
	 * @param from 开始时间
	 * @param to 结束时间
	 * @param num 超过多少
	 * @return 工厂ID
	 */
	public List<Integer> findPressureWaveThan2(List<Integer> factoryIds,int target,long from,long to,int num ){
		List<FactoryStaticsVo> type4;
		String sql4="select airs.factoryId ,avg(ss.value)as avgValue from" +
				" scheduledStatistic ss  left join aircompressor_system airs on airs.id = ss.unitId " +
				 " where airs.factoryId in ( ";
				List<Object> params = new ArrayList<Object>();
				for(int i=0;i<factoryIds.size();i++){
				sql4 += " ? ";
				if(i < factoryIds.size()-1){
					sql4 += " , ";
				}
				params.add(factoryIds.get(i));
				}
				sql4 += " ) ";
				sql4+=" and ss.scriptId =  "+target
						+" and ss.ts > "+ from +" and ss.ts<=   "+to
						+"  and ss.unitType=4"
						+ " group by airs.factoryId,ss.unitId ";
				
		type4 = query(sql4,params.toArray(),new FatoryMapper() );

		List<FactoryStaticsVo> type5;
		String sql5="select airs.factoryId ,avg(ss.value)as avgValue from" +
				" scheduledStatistic ss  left join aircompressor airs on airs.id = ss.unitId " 
				+ " where airs.factoryId in ( ";
				List<Object> params2 = new ArrayList<Object>();
				for(int i=0;i<factoryIds.size();i++){
				sql5 += " ? ";
				if(i < factoryIds.size()-1){
					sql5 += " , ";
				}
				params2.add(factoryIds.get(i));
				}
				sql5 += " ) ";
				sql5+=" and ss.scriptId =  "+target
						+" and ss.ts > "+ from +" and ss.ts<=   "+to
						+"  and ss.unitType=5"
						+ " group by airs.factoryId,ss.unitId ";
				
		type5 = query(sql5,params2.toArray(),new FatoryMapper() );
		
		List<Integer> listFactory=new ArrayList<Integer>();
		for(FactoryStaticsVo p2:type5){
			for(FactoryStaticsVo p3:type4){
				if(p2.getFactoryId()==p3.getFactoryId()){
					if( (p2.getAvg()-p3.getAvg())/p3.getAvg()>0.1){
						listFactory.add(p2.getFactoryId());
					}
				}
				
			}
		}
		return listFactory;
	
	}
	
	class FactoryStaticsVo{
		private int factoryId;
		private long avg;
		public int getFactoryId() {
			return factoryId;
		}
		public void setFactoryId(int factoryId) {
			this.factoryId = factoryId;
		}
		public long getAvg() {
			return avg;
		}
		public void setAvg(long avg) {
			this.avg = avg;
		}
	}
	
	class FatoryMapper implements GenericRowMapper<FactoryStaticsVo> {
		public FactoryStaticsVo mapRow(ResultSet rs, int rowNum) throws SQLException {
			FactoryStaticsVo FSVo = new FactoryStaticsVo();
			int i = 1;
			FSVo.setFactoryId(rs.getInt(i++));
			FSVo.setAvg(rs.getLong(i++));
			return FSVo;
		}
	}
	
	
	/**
	 * 筛选系统压降超过num的工厂
	 * @param factoryIds 之前已经筛选出来的工厂
	 * @param target 脚本ID 
	 * @param from 开始时间
	 * @param to 结束时间
	 * @param num 超过多少
	 * @return 工厂ID
	 */
	public List<Integer> findSystemYajiangThan(List<Integer> factoryIds,int target,long from,long to,double num ){
		String sql = " select airs.factoryId from scheduledStatistic ss "+
					 " left join aircompressor_system airs on airs.id = ss.unitId "+
					 " where airs.factoryId in ";
		List<Object> params = new ArrayList<Object>();
		for(int i=0;i<factoryIds.size();i++){
		sql += " ? ";
		if(i < factoryIds.size()-1){
			sql += " , ";
		}
		params.add(factoryIds.get(i));
		}
		sql += " ) ";
		sql+=" and ss.scriptId = ? "+
		" group by airs.factoryId "+
		" having avg(ss.value) > ? ";
		params.add(target);
		params.add(num);
		return queryForList(sql,params.toArray(),Integer.class);
	
	}
	
	/**
	 * 所有机器同时开过的厂家
	 * @return 
	 */
	public int runInSameTimeMaxCountInFactory(int factoryId,int spid,long from,long to){
		String sql = " select top 1 count(*) from pointvalues pv "+
					 " left join dataPoints ds on ds.id = pv.dataPointId "+
					 " left join aircompressor_members acpm on acpm.dpid = ds.id "+ 
					 " left join aircompressor acp on acp.id = acpm.acid "+
					 " where acpm.spid = ? "+
					 " and acp.factoryId = ? "+
					 " and pv.pointvalue = ? "+ 
					 " and pv.ts < ? "+
					 " and pv.ts >= ? "+
					 " group by pv.ts "+
					 " order by count(*) desc ";
		
		return ejt.queryForInt(sql,new Object[]{spid,factoryId,ACP_STATUS_RUN,from,to},-1 );
	}
	/**
	 * 查询某个工厂空压机的个数
	 * @param factoryId
	 * @return
	 */
	public int getAcpCountByFactory(int factoryId){
		return ejt.queryForInt(" select count(*) from aircompressor where factoryId = ? and type = ? ",new Object[]{factoryId,ACPVO.ACP_TYPE},-1);
	}

}
