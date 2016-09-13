package com.serotonin.mango.db.dao.statistics;

import com.serotonin.mango.db.dao.BaseDao;
import java.sql.SQLException;
import com.serotonin.db.spring.GenericRowMapper;
import com.serotonin.util.SerializationHelper;
import com.serotonin.mango.vo.DataPointVO;
import java.sql.ResultSet;
import com.serotonin.mango.vo.acp.ACPAttrVO;
import com.serotonin.mango.vo.statistics.StatisticsVO;
import com.serotonin.mango.vo.statistics.ACPAttrStatisticsVO;
import java.util.List;
import java.util.ArrayList;

public class ACPStatisticsDao extends BaseDao {
	/**
	 * 根据空压机型号编号查询空压机型号属性
	 */
	private static final String SELECT_ACP_ATTR = "select acpa.id ,acpa.attrname,acpt.typename ,acpta.data from aircompressor_type_attr acpta left join  aircompressor_type acpt on acpta.actid=acpt.id left join aircompressor_attr acpa on acpa.id=acaid where acpt.id=?";
	/**
	 * 根据机器型号查询 已经配置的属性
	 */
	private static final String SELECT_CONFIG_ACP = "select sp.id,sp.paramname,sp.dataType,acpa.id,acpa.attrname ,acpta.data from dbo.statisticsConfiguration sc left join aircompressor_attr acpa on acpa.id=sc.acpaid left join statisticsParam sp on sc.spid=sp.id  left join aircompressor_type_attr acpta on  sc.acpaid =acpta.acaid where acpta.actid=?";

	class ACPAttrStatisticsVORowMapper implements
			GenericRowMapper<ACPAttrStatisticsVO> {
		public ACPAttrStatisticsVO mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			ACPAttrStatisticsVO acpAttrStatisrics = new ACPAttrStatisticsVO();
			StatisticsVO statisticsVO = new StatisticsVO();
			ACPAttrVO acpAttrVO = new ACPAttrVO();
			statisticsVO.setId(rs.getInt(1));
			statisticsVO.setStatisticsName(rs.getString(2));
			statisticsVO.setDataType(rs.getInt(3));
			acpAttrVO.setId(rs.getInt(4));
			acpAttrVO.setAttrname(rs.getString(5));
			DataPointVO dp = (DataPointVO) SerializationHelper.readObject(rs
					.getBlob(6).getBinaryStream());
			acpAttrVO.setDp(dp);
			acpAttrStatisrics.setStatisticsVO(statisticsVO);
			acpAttrStatisrics.setAttrVO(acpAttrVO);
			return acpAttrStatisrics;
		}
	}

	class ACPAttrVORowMapper implements GenericRowMapper<ACPAttrVO> {
		public ACPAttrVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ACPAttrVO acpAttrVO = new ACPAttrVO();
			acpAttrVO.setId(rs.getInt(1));
			acpAttrVO.setAttrname(rs.getString(2));
			DataPointVO dp = (DataPointVO) SerializationHelper.readObject(rs
					.getBlob(4).getBinaryStream());
			acpAttrVO.setDp(dp);
			return acpAttrVO;
		}
	}

	/**
	 * 查询一个空压机型号的属性和对应的统计
	 * 
	 * @return
	 */
	public List<ACPAttrStatisticsVO> getACPAttrStatisticsVOByACPId(int acptId) {
		List<ACPAttrStatisticsVO> list = query(SELECT_CONFIG_ACP,
				new Object[] { acptId }, new ACPAttrStatisticsVORowMapper());
		return list;
	}

	/**
	 * 查询一个空压机型号的属性
	 * 
	 * @param acptId
	 *            空压机型号编号
	 * @return
	 */
	public List<ACPAttrVO> getACPAttrId(int acptId) {
		List<ACPAttrVO> list = query(SELECT_ACP_ATTR, new Object[] { acptId },
				new ACPAttrVORowMapper());
		return list;
	}

	/**
	 * 修改一个型号空压机的一个配置
	 * 
	 * @param statisticsId
	 *            统计参数编号
	 * @param attrId
	 *            属性参数编号
	 */
	public void updataACPTypeConfig(int statisticsId, int newAttrId,
			int oldAttrId) {
		// 添加
		if (oldAttrId == -1) {
			ejt
					.update(
							"insert statisticsConfiguration (spid,acpaid) values(?,?) ",
							new Object[] { statisticsId, newAttrId });
		}
		// 删除
		if (newAttrId == -1) {
			ejt
					.update(
							"delete statisticsConfiguration  where  spid=? and acpaid=?",
							new Object[] { statisticsId, oldAttrId });
		}
		// 修改
		else {
			ejt
					.update(
							"update statisticsConfiguration set acpaid=? where spid=? and acpaid=?",
							new Object[] { newAttrId, statisticsId, oldAttrId });
		}
	}
}
