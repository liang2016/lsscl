package com.serotonin.mango.db.dao.statistics;

import com.serotonin.mango.db.dao.BaseDao;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.vo.statistics.StatisticsVO;
import com.serotonin.mango.vo.statistics.StatisticsScriptVO;
import com.serotonin.mango.vo.statistics.PointStatistics;
import java.sql.ResultSet;
import java.util.List;
import com.serotonin.util.SerializationHelper;
import java.sql.SQLException;
import com.serotonin.db.spring.GenericRowMapper;
import com.serotonin.ShouldNeverHappenException;

/**
 * 统计数据交互dao
 * 
 * @author 刘建坤
 * 
 */
public class StatisticsDao extends BaseDao {

	/**
	 * 查询压缩空气系统参数配置
	 */
	private static final String STATISTICS_PARAM = "select id,paramname,dataType,useType from statisticsParam ";

	/**
	 * 查询数据点-统计参数
	 */
	private static final String POINT_STATISTICS = "select dp.id, dp.xid, dp.dataSourceId, dp.data ,asm.acsid,sp.id,sp.paramname,sp.datatype,sp.useType from dataPoints dp left join aircompressor_system_members asm  on dp.id=asm.memberid  left join statisticsParam sp on sp.id=asm.spid  where asm.acsid=? and membertype=1";

	class StatisticsRowMapper implements GenericRowMapper<StatisticsVO> {
		public StatisticsVO mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			StatisticsVO statistics = new StatisticsVO();
			statistics.setId(rs.getInt(1));
			statistics.setStatisticsName(rs.getString(2));
			statistics.setDataType(rs.getInt(3));
			statistics.setUseType(rs.getInt(4));
			return statistics;
		}

	}
	
	/**
	 * 获取统计参数在统计脚本中的变量名
	 * @param useType 参数类型，系统/机器 统计参数
	 * @return 变量名集合
	 */
	public String[] getStatisticParamNames(int useType){
		String[] names = null;
		String prefix = (useType==StatisticsVO.UseTypes.USE_ACP?StatisticsScriptVO.ACP_PARAM_PREFIX:StatisticsScriptVO.ACPSYSTEM_PARAM_PREFIX);		
		List<Integer> result = queryForList(" select id from statisticsParam where useType = ? ",new Object[]{useType}, Integer.class);
		if(result!=null&&result.size()>0){
			names = new String[result.size()];
			for(int i = 0;i<names.length;i++){
				names[i] = prefix+result.get(i); 
			}
		}
		return names;
	}

	/**
	 * 查询统计参数
	 * 
	 * @param useType
	 *            (压缩空气系统/空压机)
	 * @return
	 */
	public List<StatisticsVO> getSystemStatistics(int useType) {
		String sql = STATISTICS_PARAM + " where useType=?";
		List<StatisticsVO> list = query(sql, new Object[] { useType },
				new StatisticsRowMapper());
		return list;
	}

	/**
	 * 统计-数据点
	 */
	class PointStatisticsRowMapper implements GenericRowMapper<PointStatistics> {
		public PointStatistics mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			DataPointVO dp;
			StatisticsVO s = new StatisticsVO();
			try {
				dp = (DataPointVO) SerializationHelper.readObject(rs.getBlob(4)
						.getBinaryStream());
			} catch (ShouldNeverHappenException e) {
				dp = new DataPointVO();
				dp.setName("Point configuration lost. Please recreate.");
				dp.defaultTextRenderer();
			}
			dp.setId(rs.getInt(1));
			dp.setXid(rs.getString(2));
			dp.setDataSourceId(rs.getInt(3));
			dp.setParentId(rs.getInt(5));
			s.setId(rs.getInt(6));
			s.setStatisticsName(rs.getString(7));
			s.setDataType(rs.getInt(8));
			s.setUseType(rs.getInt(9));
			// 统计参数-数据点
			PointStatistics pointStatistics = new PointStatistics();
			pointStatistics.setDataPointVO(dp);
			pointStatistics.setStatisticsVO(s);
			return pointStatistics;
		}
	}

	/**
	 * 获得压缩空气系统中的数据点-统计参数
	 * 
	 * @param ACSId
	 *            压缩空气系统编号
	 * @return
	 */
	public List<PointStatistics> getDataPointsByACSId(int ACSId) {
		String sql = POINT_STATISTICS;
		List<PointStatistics> list = query(sql, new Object[] { ACSId },
				new PointStatisticsRowMapper());
		return list;
	}

	/**
	 * 获得空压机中的数据点-统计参数<通过空压机配置获取>
	 * 
	 * @param ACPId
	 *            空压机编号
	 * @return
	 */
	public List<PointStatistics> getDataPointsByACPId(int ACPId) {
		String sql = "select dp.id , dp.xid , dp.dataSourceId , dp.data ,acpm.acid ,sc.spid ,sp.paramname ,sp.datatype ,sp.useType  from  statisticsConfiguration sc inner join statisticsParam sp on sp.id=sc.spid inner join aircompressor_attr acpa on acpa.id=sc.acpaid inner join  aircompressor_members acpm on acpm.acaid=acpa.id inner join dataPoints dp on dp.id=acpm.dpid where acpm.acid=?";
		List<PointStatistics> list = query(sql, new Object[] { ACPId },
				new PointStatisticsRowMapper());
		return list;
	}

	/**
	 * 获得空压机中的数据点-统计参数<通过空压机统计成员-其中属性字段为空>
	 * 
	 * @param ACPId
	 * @return
	 */
	public List<PointStatistics> getDataPointsByACPId2(int ACPId) {
		String sql = "select dp.id,dp.xid, dp.dataSourceId, dp.data ,acm.acid,acm.spid,sp.paramname,sp.datatype,sp.usetype from aircompressor_members acm inner join datapoints dp on dp.id=acm.dpid left join statisticsParam sp on sp.id=acm.spid where acm.acaid is NULL and  acm.acid=?";
		List<PointStatistics> list = query(sql, new Object[] { ACPId },
				new PointStatisticsRowMapper());
		return list;
	}

	/**
	 * 判断空压机成员执行的是修改还是添加
	 * 
	 * @param acpId
	 *            空压机编号
	 * @return
	 */
	public boolean checkACPMemberStatistics(int acpId) {
		String sql = "select spid,0,0,0,0 from dbo.aircompressor_members where acid=?";
		List<StatisticsVO> list = query(sql, new Object[] { acpId },
				new StatisticsRowMapper());
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getId() != 0) {
				// 如果存在统计编号不为0表示是更新,返回true
				return true;
			}
		}
		// 如果全部为0表示是修改
		return false;
	}

	class StatisticsVORowMapper implements GenericRowMapper<StatisticsVO> {
		public StatisticsVO mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			StatisticsVO s = new StatisticsVO();
			s.setId(rs.getInt(1));
			s.setStatisticsName(rs.getString(2));
			s.setDataType(rs.getInt(3));
			s.setUseType(rs.getInt(4));
			return s;
		}
	}

	/**
	 * 根据使用类型查询压缩空气系统或者空压机统计配置
	 * 
	 * @param useTpye
	 *            使用类型
	 * @return
	 */
	public List<StatisticsVO> getStatisticsConfig(int useTpye) {
		String sql = "select id,paramname,dataType,useType from statisticsParam where useType=?";
		List<StatisticsVO> list = query(sql, new Object[] { useTpye },
				new StatisticsVORowMapper());
		return list;
	}
	
	/**
	 * 根据ID集合来查询统计参数集合
	 * @param ids ID集合
	 * @return 统计参数集合
	 */
	public List<StatisticsVO> getStatisticsParamByScript(List<Integer> ids){
		String sql = "select id,paramname,dataType,useType from statisticsParam where id in ( ";
		for(int i=0;i<ids.size();i++){
			sql+= ids.get(i);
			if(i+1<ids.size()){
				sql+=",";
			}
		}
		sql+=" ) ";
		List<StatisticsVO> list = query(sql, new Object[0],
				new StatisticsVORowMapper());
		return list;
	}

	/**
	 * 根据编号获得统计参数
	 * 
	 * @param id
	 * @return
	 */
	public StatisticsVO getStatisticsConfigById(int id) {
		String sql = "select id,paramname,dataType,useType from statisticsParam where id=?";
		List<StatisticsVO> list = query(sql, new Object[] { id },
				new StatisticsVORowMapper());
		return list.get(0);
	}

	/**
	 * 保存统计参数配置
	 * 
	 * @param id
	 * @param useType
	 * @param dataType
	 * @param paramname
	 */
	public StatisticsVO saveStatisticsConfig(StatisticsVO statisticsVo) {
		// 执行更新
		if (statisticsVo.getId() > 0) {
			ejt
					.update(
							"update statisticsParam set useType=?,dataType=? ,paramname=? where id=? ",
							new Object[] { statisticsVo.getUseType(), statisticsVo.getDataType(), statisticsVo.getStatisticsName(), statisticsVo.getId() });
		} else {
			statisticsVo.setId(doInsert(
							"insert statisticsParam (paramname,dataType,useType)values(?,?,?)",
							new Object[] { statisticsVo.getStatisticsName(), statisticsVo.getDataType(), statisticsVo.getUseType() }));
		}
		return statisticsVo;
	}

	/**
	 * 删除一个统计参数配置
	 * 
	 * @param id
	 *            编号
	 */
	public void deleteStatisticsConfig(int id) {
		ejt.update("delete statisticsParam  where id=?", new Object[] { id });
	}

	/**
	 * 验证统计参数是否被其他表使用
	 * 
	 * @return
	 */
	public boolean cheeckConfigIsUsed(int id) {
		if (ejt
				.queryForInt(
						"select count(*) from dbo.statisticsConfiguration where spid=?",
						new Object[] { id }) > 0) {
			return false;

		} else if (ejt
				.queryForInt(
						"select count(*) from dbo.aircompressor_system_members where spid=?",
						new Object[] { id }) > 0) {
			return false;
		}else if(
		ejt.queryForInt("select count(*) from dbo.aircompressor_members where spid=?",
				new Object[] { id })>0){
			return false;
		}
		return true;
	}
}
