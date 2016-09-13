package com.serotonin.mango.rt.statistic.common;

import java.util.List;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.serotonin.db.spring.GenericRowMapper;
import com.serotonin.mango.db.dao.BaseDao;
import com.serotonin.mango.vo.acp.ACPSystemMembersVO.MemberTypes;
import com.serotonin.mango.vo.acp.ACPSystemMembersVO;
import com.serotonin.mango.vo.statistics.RunInSameTimeVO;
import com.serotonin.mango.vo.statistics.ScheduledStatisticVO;
import com.serotonin.mango.vo.statistics.ScopeResultVO;
import com.serotonin.mango.vo.statistics.SumInSameTimeVO;


/**
 * 为了将来将其统计功能分离出本项目，现在编写代码暂时把数据访问层代码单独写在一个类中
 * @author 王金阳
 *
 */
public class GrabForStatisticsDao extends BaseDao{
	
	/**
	 * 通过统计参数编号在空压机中找到对应的点的编号
	 * @param acpid 空压机编号
	 * @param spid  统计参数编号
	 * @return 数据点的编号
	 */
	public int findDataPointIdFromAcpBySpid(int acpId,int spid){
		String sql = "  select dpid from aircompressor_members where acid = ? and spid = ? ";
		return ejt.queryForInt(sql,new Object[]{acpId,spid},-1);
	}
	
	/**
	 * 通过统计参数编号在压缩空气系统中找到对应的点的编号
	 * @param systemId 压缩空气系统编号
	 * @param spid 统计参数编号
	 * @return 数据点的编号
	 */
	public int findDataPointIdFromSystemBySpid(int systemId,int spid){
		String sql = "  select memberid from aircompressor_system_members where acsid = ? and membertype = ? and spid = ? ";
		return ejt.queryForInt(sql,new Object[]{systemId,ACPSystemMembersVO.MemberTypes.POINT,spid},-1);
	}
	
	/**
	 * 获取一个点在from和to两个时间点之间的所有值
	 * @param dpid 点编号
	 * @param from 开始时间
	 * @param to   结束时间
	 * @return     值的集合
	 */
	public List<Double> getValuesByDataPointIdBewteenTimes(int dpid,long from,long to){
		String sql = " select pointValue from pointValues_"+dpid+" where  ts > ? and  ts <= ? order by pointValue ";
		return queryForList(sql, new Object[]{from,to}, Double.class);
	}
	
	/**
	 * 获取一个点在from和to两个时间点之间的最后一个值
	 * @param dpid 点编号
	 * @param from 开始时间
	 * @param to   结束时间
	 * @return     值的集合
	 */
	public long getValueByDataPointIdBewteenTimes(int dpid,long from,long to){
		String sql = " select top 1 pointValue from pointValues_"+dpid+" where  ts > ? and  ts <= ? order by ts desc ";
		return ejt.queryForLong(sql, new Object[]{from,to},-1 );
	}
	
	/**
	 * 获得空压机的功率
	 * @param acpId
	 * @return 功率
	 */
	public int findAcpPowerByid(int acpId){
		String sql = "  select volume  from aircompressor where id=? ";
		return ejt.queryForInt(sql,new Object[]{acpId},-1);
	}
	
	
	/**
	 * 获取一个点在from和to两个时间点之间的所有值,根据时间排序
	 * @param dpid 点编号
	 * @param from 开始时间
	 * @param to   结束时间
	 * @return     值的集合
	 */
	public List<Double> getValuesByDataPointIdBewteenTimesOrderByTs(int dpid,long from,long to){
		String sql = " select pointValue from pointValues_"+dpid+" where ts > ? and  ts <= ? order by ts ";
		return queryForList(sql, new Object[]{from,to}, Double.class);
	}
	
	/**
	 * 查询某个统计最后一次次统计结束的时间
	 * @param statistic 哪个统计
	 * @param unitId 机器/系统的ID
	 * @return 最后一次统计的结束时间
	 */
	public long getLastExecuteTime(int statistic,int unitId){
		String sql = "  select max(ts) from scheduledStatistic where scriptId = ? and unitId = ? ";
		return ejt.queryForLong(sql,new Object[]{statistic,unitId},-1L);
	}
	
	/**
	 * 查询某个统计最后一次次统计结束的时间/主要用户分期的统计,例如健康指数和故障处理率.
	 * @param statistic 哪个统计
	 * @param unitId 机器/系统的ID
	 * @return 最后一次统计的结束时间
	 */
	public long getLastExecuteTime(int statistic,int unitId,int unitType){
		String sql = "  select max(ts) from scheduledStatistic where scriptId = ? and unitId = ? and unitType=?";
		return ejt.queryForLong(sql,new Object[]{statistic,unitId,unitType},-1L);
	}
	
	/**
	 * 获取pointvalues表中最早的数据行的采集时间，即最早被采集到的点的时间
	 * @return 最早的时间
	 */
	public long getFirstCollectTime(int dpid){
		String sql = " select min(ts) from pointValues_"+dpid;
		return ejt.queryForLong(sql, new Object[]{},-1L);
	}
	
	/**
	 * 获取某些脚本什么时候开始统计的
	 * @param scriptIds 脚本ID集合
	 * @return 最早统计的时间
	 */
	public long getFirstStatisticTime(List<Integer> scriptIds){
		String sql = " select min(ts) from scheduledStatistic where scriptId in( ";
		List<Integer> params = new ArrayList<Integer>();
		for(int i=0;i<scriptIds.size();i++){
			sql += " ? ";
			if(i<scriptIds.size()-1){
				sql += ",";
			}
			params.add(scriptIds.get(i));
		}
		sql += " ) ";
		return ejt.queryForLong(sql,params.toArray(),-1L);
	}
	
	/**
	 * 获取pointvalues表中最早的数据行的采集时间，即最早被采集到的点的时间
	 * @return 最早的时间
	 */
	public long getFirstCollectTime(List<Integer> dpids){
		//String sql = " select min(ts) from pointValues_"+dpid+" where dataPointId in ( ";
		String sql="";
		for(int i=0;i<dpids.size();i++){
			int dpid=dpids.get(i);
			sql += "select min(ts) from pointValues_"+dpid;
			if(i<dpids.size()-1){
				sql+=" union all ";	
			}
		}
		return ejt.queryForLong(sql,new Object[]{},-1L);
	}
	
	
	/**
	 * 将统计一个小时的值插入数据库中
	 * @param vo 统计信息
	 */
	public int scheduleStatistic(ScheduledStatisticVO vo){
		String sql = " insert into scheduledStatistic(scriptId,value,ts,unitType,unitId,date) values(?,?,?,?,?,?) ";
		return doInsert(sql, new Object[] {
				vo.getScriptId(), 
				vo.getValue(),
				vo.getTimestamp(),
				vo.getUnitType(), 
				vo.getUnitId(), 
				new java.util.Date(vo.getTimestamp()).toLocaleString()
		});
	}
	
	/**
	 * 获取N个点中最早被采集到数据的时间
	 * @param dpids 点的ID的集合
	 * @return 
	 */
	public long getEarliestCollectTimeFromDpids(List<Integer> dpids){
		//String sql  = " select min(ts) from pointvalues where dataPointId in ( " ;
		if(dpids.size()<=0)
			return -1l;
		String sql="select min(ts) from (";
		for(int i=0;i<dpids.size();i++){
			int dpid=dpids.get(i);
			sql += "select min(ts) as ts from pointValues_"+dpid;
			if(i<dpids.size()-1){
				sql+=" union all ";	
			}
		}
		sql += ")as pointvalues";
		return ejt.queryForLong(sql, new Object[]{},-1L);
	}
	
	/**
	 * 查询同时开机数量差时候临时用上的实体
	 * @author 王金阳
	 *
	 */
	class RunInSameTimeVORowMapper implements GenericRowMapper<RunInSameTimeVO> {
		public RunInSameTimeVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			RunInSameTimeVO vo = new RunInSameTimeVO();
			int i = 1;
			vo.setTs(rs.getLong(i++)); 
			vo.setCount(rs.getInt(i++)); 
			return vo;
		}
	}
	
	
	/**
	 * 获取一个系统下所有机器同时开机情况
	 * @param systemId 系统编号
	 * @return 同时开机情况
	 */
	//???????????????
	public List<RunInSameTimeVO> getRunInSameTimeCountOfAcpSystemList(int systemId,int spid,long from,long to,List<Integer> values){
		List<Integer> points=getDataPointIds(systemId,spid);
		String sql="";
		List<Object> params = new ArrayList<Object>();
		for (int i = 0; i < points.size(); i++) {
			int dpid=points.get(i);
			sql += "select pv.ts, count(*) from pointValues_"+dpid+
			 " pv where pv.ts >  "+from+
			 " and pv.ts <=  "+to+
			 " and pv.pointvalue in ( ";
			for(int j=0;j<values.size();j++){
				sql += " ? ";
				if(j<values.size()-1) 
					sql+=" , ";
			}
			params.addAll(values);
			sql+=")  group by pv.ts ";
			if(i<points.size()-1){
				sql+=" union all ";	
			}
		}
//		String sql = " select pv.ts, count(*) from pointvalues pv "+
//					 " left join dataPoints ds on pv.dataPointId = ds.id "+
//					 " left join aircompressor_members acpm on acpm.dpid = ds.id "+
//					 " left join aircompressor_system_members acpsm on acpsm.memberid = acpm.acid "+
//					 " where acpsm.acsid = ? "+ 
//					 " and acpsm.membertype = ? "+
//					 " and acpm.spid = ? "+
//					 " and pv.ts > ? "+
//					 " and pv.ts <= ? "+
//					 " and pv.pointvalue in ( ";
//		List<Object> params = new ArrayList<Object>();
//		params.add(systemId);
//		params.add(ACPSystemMembersVO.MemberTypes.ACP);
//		params.add(spid);
//		params.add(from);
//		params.add(to);
//		for(int i=0;i<values.size();i++){
//			sql += " ? ";
//			if(i+1<values.size()) sql+=" , ";
//		}
//		params.addAll(values);
//		sql+=")  group by pv.ts order by pv.ts asc ";
		
		return ejt.query(sql,params.toArray(),new RunInSameTimeVORowMapper());
		
	}
	//获取datapointid
	private  List<Integer> getDataPointIds(int systemId,int spid){
		String sql = " select ds.id from dataPoints ds "+
					 " left join aircompressor_members acpm on acpm.dpid = ds.id "+
					 " left join aircompressor_system_members acpsm on acpsm.memberid = acpm.acid "+
					 " where acpsm.acsid = ? "+ 
					 " and acpsm.membertype = ? "+
					 " and acpm.spid = ? ";
					
		List<Object> params = new ArrayList<Object>();
		params.add(systemId);
		params.add(ACPSystemMembersVO.MemberTypes.ACP);
		params.add(spid);
		return queryForList(sql,params.toArray(),Integer.class);
	}
	
	/**
	 * 查询节能指标3时候临时用上的实体
	 * @author 王金阳
	 *
	 */
	class SumInSameTimeVORowMapper implements GenericRowMapper<SumInSameTimeVO> {
		public SumInSameTimeVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			SumInSameTimeVO vo = new SumInSameTimeVO();
			int i = 1;
			vo.setTs(rs.getLong(i++)); 
			vo.setValue(rs.getDouble(i++));
			vo.setCount(rs.getInt(i++)); 
			return vo;
		}
	}
	
	class SumInSameTimeVORowMapper2 implements GenericRowMapper<SumInSameTimeVO> {
		public SumInSameTimeVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			SumInSameTimeVO vo = new SumInSameTimeVO();
			int i = 1;
			vo.setTs(rs.getLong(i++)); 
			vo.setValue(rs.getDouble(i++));
			return vo;
		}
	}
	
	/**
	 * 获取一个系统下所有空压机《某个属性的在同一时间的和》 的集合
	 * @param systemId 系统编号
	 * @param spid 统计参数ID
	 * @param from 开始时间
	 * @param to 结束时间 
	 * @return 
	 */
	//????????
	public List<SumInSameTimeVO> getAcpsValuesInSystem(int systemId,int spid,long from,long to){
		List<Integer> points=getDataPointIds(systemId,spid);
		String sql="";
		for (int i = 0; i < points.size(); i++) {
			int dpid=points.get(i);
			sql += "select pv.ts,sum(pv.pointvalue),count(*) from pointValues_"+dpid+
			 " pv where pv.ts >  "+from+
			 " and pv.ts <= "+to;
			sql+="  group by pv.ts order by pv.ts asc ";
			if(i<points.size()-1){
				sql+=" union all ";	
			}
		}
//		String sql = " select pv.ts,sum(pv.pointvalue),count(*) from pointvalues pv "+
//					 " left join dataPoints ds on pv.dataPointId = ds.id "+
//					 " left join aircompressor_members acpm on acpm.dpid = ds.id "+
//					 " left join aircompressor_system_members acpsm on acpsm.memberid = acpm.acid "+
//					 " where acpsm.acsid = ? "+ 
//					 " and acpsm.membertype = ? "+
//					 " and acpm.spid = ? "+
//					 " and pv.ts > ? "+
//					 " and pv.ts <= ? "+
//					 " group by pv.ts " +
//					 " order by pv.ts asc ";
		return ejt.query(sql,new Object[]{},new SumInSameTimeVORowMapper());
	}
	
	/**
	 * 查询节能指标3时候临时用上的实体
	 * @author 王金阳
	 *
	 */
	class SystemValueVORowMapper implements GenericRowMapper<SumInSameTimeVO> {
		public SumInSameTimeVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			SumInSameTimeVO vo = new SumInSameTimeVO();
			int i = 1;
			vo.setTs(rs.getLong(i++)); 
			vo.setValue(rs.getDouble(i++));
			return vo;
		}
	}
	
	/**
	 * 获取某个系统在周期内每个时间周期内的spid参数对应点的值
	 * @param systemId 系统ID
	 * @param spid 统计参数ID
	 * @param from 开始时间
	 * @param to   结束时间
 	 * @return  <时间,值>的集合
	 */
	public List<SumInSameTimeVO> getSystemValues(int systemId,int spid,long from,long to){
		List<Integer> points=getDataPointIdsForSystem(systemId,spid);
		String sql="";
		for (int i = 0; i < points.size(); i++) {
			int dpid=points.get(i);
			sql += "select pv.ts,pv.pointvalue  from pointValues_"+dpid+
			 " pv where pv.ts >  "+from+
			 " and pv.ts <= "+to;
			sql+=" order by pv.ts asc ";
			if(i<points.size()-1){
				sql+=" union all ";	
			}
		}
//		String sql = " select pv.ts,pv.pointvalue from pointvalues pv "+
//					 " left join dataPoints ds on pv.dataPointId = ds.id "+
//					 " left join aircompressor_system_members acpsm on acpsm.memberid = ds.id "+
//					 " where acpsm.acsid = ? "+ 
//					 " and acpsm.membertype = ? "+
//					 " and acpsm.spid = ? "+
//					 " and pv.ts > ? "+
//					 " and pv.ts <= ? "+ 
//					 " order by pv.ts asc ";
		return ejt.query(sql,new Object[]{},new SumInSameTimeVORowMapper2());
	}
		
		

		//获取datapointid For System
	private  List<Integer> getDataPointIdsForSystem(int systemId,int spid){
			String sql = " select ds.id from dataPoints ds "+
						 " left join aircompressor_system_members acpsm on acpsm.memberid = ds.id "+
						 " where acpsm.acsid = ? "+ 
						 " and acpsm.membertype = ? "+
						 " and acpm.spid = ? ";
						
			List<Object> params = new ArrayList<Object>();
			params.add(systemId);
			params.add(ACPSystemMembersVO.MemberTypes.POINT);
			params.add(spid);
			return queryForList(sql,params.toArray(),Integer.class);
	}

/**
 * 查询一个点,在一个时间段内出现过的值
 * @param pointId
 * @param from
 * @param to
 * @return
 */
	public int getCountOfAllByPointId(int pointId,long from,long to,String code,boolean hasZero){
		String sql="select COUNT(distinct(pointvalue)) from pointValues_"+pointId;
		sql=sql+" where ts>? and ts<?";
		if(!hasZero)
			sql=sql+" and pointvalue in("+code+")  having COUNT(pointvalue)!=0";
		return ejt.queryForInt(sql,new Object[]{from,to},-1);
	}
	
	/**
	 * 获取一个系统下每台空压机参数总个数|健康参数(没有故障报警的，即故障报警参数的值为0)的个数的总和
	 * @param systemId 系统编号
	 * @param spid 统计参数ID
	 * @param from 开始时间
	 * @param to 结束时间
	 * @param healthParam 是否为 健康参数个数，true为健康指数，false为总参数个数
	 * @return 个数
	 */
	public int getCountOfAllAcpParamsInSystem(int systemId,int spid,long from,long to,boolean healthParam){
		List<Integer> points=getDataPointIds(systemId,spid);
		if(points.size()<=0)
			return 0;
		String sql="select sum(c) from (";
		List<Object> params = new ArrayList<Object>();
		for (int i = 0; i < points.size(); i++) {
			int dpid=points.get(i);
			sql += "select count(*) as c from pointValues_"+dpid+
			 " pv where pv.ts >  "+from+
			 " and pv.ts <=  "+to;
			 if(healthParam)
				sql+= " and pv.pointvalue < 1"; 
			 else
				 sql+= " and pv.pointvalue < 2"; 
			if(i<points.size()-1){
				sql+=" union all ";	
			}
		}
		sql+=") as pointvales";
//		String sql = " select count(*) from pointvalues pv "+
//					 " left join dataPoints dp on dp.id = pv.dataPointId "+
//					 " left join aircompressor_members acpm on acpm.dpid = dp.id "+
//					 " left join aircompressor_system_members acpsm on acpsm.memberid = acpm.acid "+
//					 " where acpsm.acsid = ? "+
//					 " and acpsm.membertype = ? "+
//					 " and acpm.spid = ? "+
//					 " and pv.ts > ? "+
//					 " and pv.ts <= ? "+
//					 " and pv.pointvalue < ? "; 
		
		return ejt.queryForInt(sql,new Object[]{},0);
	}
	
	/**
	 * 获取空压机在周期内某个统计参数对应的点的所有值
	 * @param acpId 空压机ID
	 * @param spid 统计参数ID
	 * @param from 开始时间
	 * @param to   结束时间
	 * @return
	 */
	public List<SumInSameTimeVO> getAcpValues(int acpId,int spid,long from,long to){
		List<Integer> points=getDataPointIdsForAcp(acpId,spid);
		String sql="";
		List<Object> params = new ArrayList<Object>();
		for (int i = 0; i < points.size(); i++) {
			int dpid=points.get(i);
			sql += "select pv.ts,pv.pointvalue from pointValues_"+dpid+
			 " pv where pv.ts >  "+from+
			 " and pv.ts <=  "+to;
			if(i<points.size()-1){
				sql+=" union all ";	
			}
		}
//		String sql = " select pv.ts,pv.pointvalue from pointvalues pv "+
//					 " left join dataPoints dp on dp.id = pv.dataPointId "+
//					 " left join aircompressor_members acpm on acpm.dpid = dp.id "+
//					 " where acpm.acid = ? "+
//					 " and acpm.spid = ? "+
//					 " and pv.ts > ? "+
//					 " and pv.ts <= ? "+
//					 " order by pv.ts  ";
		return ejt.query(sql,new Object[]{},new SumInSameTimeVORowMapper2());
	}
	
	//获取datapointid For System
	private  List<Integer> getDataPointIdsForAcp(int acpId,int spid){
		String sql = " select ds.id from dataPoints ds "+
					 " left join aircompressor_members acpm on acpm.dpid = ds.id "+
					 " where acpm.acid = ? "+
					 " and acpm.spid = ? ";
					
		List<Object> params = new ArrayList<Object>();
		params.add(acpId);
		params.add(spid);
		return queryForList(sql,params.toArray(),Integer.class);
	}
	
	/**
	 * 获取某个脚本在某段时间内的统计结果
	 * @param scriptId 脚本ID
	 * @param from 开始时间
	 * @param to   结束时间
	 * @return     统计结果
	 */
	public Double getStatisticValueByScript(int scriptId,int unitId,long from,long to){
		String sql = " select top 1 value from scheduledStatistic where scriptId = ? and unitId =? and ts > ? and ts <= ? ";
		List<Double> values = ejt.queryForList(sql,new Object[]{scriptId,unitId,from,to},Double.class);
		if(values==null||values.size()==0){
			return null;
		}else{
			return values.get(0);
		}
	}
//	
//	/**
//	 * 获取某个脚本在某段时间内的统计结果集合
//	 * @param scriptId 脚本ID
//	 * @param from 开始时间
//	 * @param to   结束时间
//	 * @return     统计结果 集合
//	 */
//	public List<Double> getStatisticValueByScript(int scriptId,int unitId,long from,long to){
//		String sql = " select  value from scheduledStatistic where scriptId = ? and unitId =? and ts > ? and ts <= ? ";
//		List<Double> values = ejt.queryForList(sql,new Object[]{scriptId,unitId,from,to},Double.class);
//		return values;
//	}
	
	/**
	 * 获取某个脚本在某段时间内的统计结果的最大值
	 * @param scriptId 脚本ID
	 * @param from 开始时间
	 * @param to   结束时间
	 * @return     统计结果
	 */
	public Double getStatisticValueByScriptMax(int scriptId,int unitId,long from,long to){
		String sql = " select max(value) from scheduledStatistic where scriptId = ? and unitId =? and ts > ? and ts <= ? ";
		List<Double> values = ejt.queryForList(sql,new Object[]{scriptId,unitId,from,to},Double.class);
		if(values==null||values.size()==0){
			return null;
		}else{
			return values.get(0);
		}
	}
	
	/**
	 * 获取某个脚本在某段时间内的统计结果的平均值
	 * @param scriptId 脚本ID
	 * @param from 开始时间
	 * @param to   结束时间
	 * @return     统计结果
	 */
	public Double getStatisticValueByScriptAvg(int scriptId,int unitType,int unitId,long from,long to){
		String sql = " select avg(value) from scheduledStatistic where scriptId = ? and unitType=? and unitId =? and ts > ? and ts <= ? ";
		List<Double> values = ejt.queryForList(sql,new Object[]{scriptId,unitType,unitId,from,to},Double.class);
		if(values==null||values.size()==0){
			return null;
		}else{
			return values.get(0);
		}
	}
	
	/**
	 * 获取某个脚本在某段时间内的统计结果的平均值
	 * @param scriptId 脚本ID
	 * @param from 开始时间
	 * @param to   结束时间
	 * @return     统计结果
	 */
	public Double getStatisticValueByScriptAvg(int scriptId,int unitType,List<Integer>  unitIds,long from,long to){
		String sql ="select avg(value) from scheduledStatistic where scriptId = ? and unitType=? and unitId in( ";
		for (int i = 0; i < unitIds.size(); i++) {
			sql+=unitIds.get(i);
			if(i<unitIds.size()-1){
				sql+=",";
			}
		}
		sql+=") and ts > ? and ts <= ?";
		
		List<Double> values = ejt.queryForList(sql,new Object[]{scriptId,unitType,from,to},Double.class);
		if(values==null||values.size()==0){
			return null;
		}else{
			return values.get(0);
		}
	}
	
	
	
	/**
	 * 获取某个脚本在某段时间内的统计结果的最小值
	 * @param scriptId 脚本ID
	 * @param from 开始时间
	 * @param to   结束时间
	 * @return     统计结果
	 */
	public Double getStatisticValueByScriptMin(int scriptId,int unitId,long from,long to){
		String sql = " select min(value) from scheduledStatistic where scriptId = ? and unitId =? and ts > ? and ts <= ? ";
		List<Double> values = ejt.queryForList(sql,new Object[]{scriptId,unitId,from,to},Double.class);
		if(values==null||values.size()==0){
			return null;
		}else{
			return values.get(0);
		}
	}
	
	/**
	 * 获取某个点在某段时间内的平均值
	 * @param dpid 点的ID
	 * @param from 开始时间
	 * @param to   结束时间
	 * @return 平均值
	 */
	public Double getAvgValueByDataPointBewteenTimes(int dpid,long from,long to){
		String sql = "select avg(pointValue) from pointValues_"+dpid+ "  where ts > ? and ts <= ? ";
		return (Double) ejt.queryForObject(sql, new Object[]{from,to},Double.class,-1d);
	}

	/**
	 * 获取几个点在某段时间内的平均值
	 * @param dpid 点的ID
	 * @param from 开始时间
	 * @param to   结束时间
	 * @return 平均值
	 */
	public Double getAvgValueByDataPointBewteenTimes(List<Integer> dpids,long from,long to){
		String sql="select AVG(temp) from (";
		for (int i = 0; i < dpids.size(); i++) {
			sql+="select AVG(pointValue) as temp from pointValues_"+dpids.get(i)+"  where ts between "+from+" and "+to;
			if(i<dpids.size()-1){
				sql+=" union all ";	
			}
		}
		sql+=" ) as pointvalues";
		return (Double) ejt.queryForObject(sql, new Object[]{},Double.class,-1d);
	}
	
	/**
	 * 获取一个系统下所有机器的某个点的和为最大值的时刻
	 */
	public long getTimeOfValueIsMax(int systemId,int spid,long from,long to){
		List<Integer> points=getDataPointIds(systemId,spid);
		String sql="select top 1 ts from  (";
		List<Object> params = new ArrayList<Object>();
		for (int i = 0; i < points.size(); i++) {
			int dpid=points.get(i);
			sql += "select ts,pointValue from pointValues_"+dpid+
			 " pv where pv.ts >  "+from+
			 " and pv.ts <=  "+to;
			if(i<points.size()-1){
				sql+=" union all ";	
			}
		}
		sql+=" )as topT group by ts order by sum(pointValue) desc";
//		
//		String sql = " select top 1 ts from pointvalues pv "+
//					" left join dataPoints dp on pv.dataPointId =  dp.id "+
//					" left join aircompressor_members acpm on acpm.dpid = dp.id "+
//					" left join aircompressor_system_members acpsm on acpsm.memberid = acpm.acid "+
//					" where acpsm.acsid = ? "+
//					" and acpsm.membertype = ? "+
//					" and acpm.spid = ? "+
//					" and pv.ts > ? and pv.ts <= ? "+
//					" group by ts "+
//					" order by sum(pointValue) desc ";
		return ejt.queryForLong(sql,new Object[]{},-1L);
		
	}
	/**
	 * 获取一个系统下所有空压机某个点在某个时刻的值的和 
	 */
	public SumInSameTimeVO getTotalDischargePressure(int systemId,int spid,long from,long to){
		List<Integer> points=getDataPointIds(systemId,spid);
		String sql="";
		List<Object> params = new ArrayList<Object>();
		for (int i = 0; i < points.size(); i++) {
			int dpid=points.get(i);
			sql += "select top 1 pv.ts,sum(pv.pointValue),count(*)  from pointValues_"+dpid+
			 " pv where pv.ts >  "+from+
			 " and pv.ts <=  "+to;
			sql+=" group by ts  ";
			if(i<points.size()-1){
				sql+=" union all ";	
			}
		}		
//		String sql = " select top 1 pv.ts,sum(pv.pointValue),count(*) from pointValues pv "+
//					" left join dataPoints dp on pv.dataPointId =  dp.id  "+
//					" left join aircompressor_members acpm on acpm.dpid = dp.id "+
//					" left join aircompressor_system_members acpsm on acpsm.memberid = acpm.acid "+
//					" where acpsm.acsid = ? "+
//					" and acpsm.membertype = ? "+
//					" and acpm.spid = ? "+
//					" and pv.ts > ? and pv.ts <= ? "+
//					" group by pv.ts "; 
		List<SumInSameTimeVO> list = ejt.query(sql,new Object[]{},new SumInSameTimeVORowMapper());
		if(list==null||list.size()==0){
			return null;
		}else{
			return list.get(0);
		}
		
	}
		
}
