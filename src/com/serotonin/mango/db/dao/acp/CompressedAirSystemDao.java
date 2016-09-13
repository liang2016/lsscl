package com.serotonin.mango.db.dao.acp;

import com.serotonin.mango.db.dao.BaseDao;
import com.serotonin.mango.Common;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.serotonin.db.spring.GenericRowMapper;
import com.serotonin.mango.vo.acp.ACPSystemVO;
import com.serotonin.mango.vo.acp.ACPVO;
import com.serotonin.mango.vo.DataPointVO;

import com.serotonin.ShouldNeverHappenException;
import com.serotonin.util.SerializationHelper;
import com.serotonin.db.spring.ExtendedJdbcTemplate;
import org.springframework.transaction.TransactionStatus;
import com.serotonin.mango.db.dao.statistics.StatisticsDao;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

public class CompressedAirSystemDao extends BaseDao {
	/**
	 * 查询压缩空气系统基本sql
	 */
	private static final String CAS_SELECT = "select id,xid,systemname,factoryid from aircompressor_system";
	/**
	 * 查询压缩空气系统成员基本sql
	 */
	private static final String CAS_MEMBERS = "select acm.acsid ,acs.systemname  ,acm.membertype ,acm.memberid  from aircompressor_system_members acm left join aircompressor_system acs on acm.acsid=acs.id ";
	/**
	 * 根据压缩空气系统编号查询空压机
	 */
	private static final String FIND_AIR_COMPRESSOR = "select ar.id ,ar.acname,acsm.memberid  from aircompressor_system_members acsm left join aircompressor ar on ar.id=acsm.memberid  where acsid=? and membertype=0 and ar.type=0";
	/**
	 * 根据压缩系统编号查询数据点
	 */
	private static final String FIND_AIR_SYSTEM_POINTS = "select dp.id, dp.xid, dp.dataSourceId, dp.data ,asm.acsid from dataPoints dp left join aircompressor_system_members asm  on dp.id=asm.memberid where asm.acsid=? and membertype=1";

	/**
	 * 根据工厂编号查询空压机(无<系统编号>字段)
	 */
	private static final String FIND_FACTORY_AIR_COMPRESSOR = "select ac.id,ac.acname,ac.actid from aircompressor ac where factoryid=? and ac.type=0 and ac.id not in (select memberid from dbo.aircompressor_system_members where membertype=0)";
	/**
	 * 根据工厂编号查询空压机(有<系统编号>字段)
	 */
	private static final String FIND_FACTORY_AIR_COMPRESSOR2 = "select ar.id ,ar.acname ,acsm.acsid  from aircompressor_system_members acsm left join aircompressor ar on ar.id=acsm.memberid  where acsid in(select id  from dbo.aircompressor_system where factoryid=?) and membertype=0 and ar.type=0";
	/**
	 * 查询工厂-压缩空气系统-数据点
	 */
	private static final String FIND_POINT_AIR = "select dp.id, dp.xid, dp.dataSourceId, dp.data,acs.acsid  from dataPoints dp left join  aircompressor_system_members  acs on acs.memberid=dp.id where acs.membertype=1 and acs.acsid in(select id from aircompressor_system where factoryid=?)";
	/**
	 * 查询工厂-压缩系统-空压机下的数据点
	 */
	private static final String FIND_POINT_COMPRESS_FACTORY = " select dp.id, dp.xid, dp.dataSourceId, dp.data ,acm.acid from dataPoints dp left join aircompressor_members acm on  acm.dpid=dp.id "
			+ "where acm.acid in(select  ac.id from aircompressor ac left join aircompressor_system_members  acs on acs.memberid=ac.id "
			+ " where  ac.type=0 and acs.membertype=0 and acs.acsid  "
			+ "in(select id from aircompressor_system where factoryid=?))";
	/**
	 * 根据空压机编号获得数据点
	 */
	private static final String FIND_POINT_COMPRESSOR = "select dp.id, dp.xid, dp.dataSourceId, dp.data ,acm.acid from dataPoints dp left join aircompressor_members acm on  acm.dpid=dp.id where acm.acid=?";
	/**
	 * 查询没有存在于空压机的数据点
	 */
	private static final String FINT_POINT_NOT_IN_ACP = "select dp.id, dp.xid, dp.dataSourceId, dp.data,dp.id from dataPoints dp where  dp.id not in (select dpid from aircompressor_members union all select memberid from aircompressor_system_members where membertype=1) and dp.dataSourceId in(select id from dataSources where factoryId =?) ";
	/**
	 * 添加压缩空气系统成员
	 */
	private static final String COMPRESSED_SYSTEM_INSET = "insert into aircompressor_system_members (acsid,membertype,memberid,spid) ";

	/**
	 * 压缩空气系统添加空压机
	 */
	private static final String COMPRESSED_SYSTEM_INSET_VALUE = "select ?,?,?,?";
	/**
	 * 查询空压机成员表中spid的个数
	 */
	private static final String ACP_MEMBER_SPID = "select count(spid) from aircompressor_members where acid=?";

	class CompressedAirSystemRowMapper implements GenericRowMapper<ACPSystemVO> {
		public ACPSystemVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ACPSystemVO acpSystem = new ACPSystemVO();
			acpSystem.setId(rs.getInt(1));
			acpSystem.setXid(rs.getString(2));
			acpSystem.setSystemname(rs.getString(3));
			acpSystem.setFactoryId(rs.getInt(4));
			return acpSystem;
		}
	}
	
	
    public String generateUniqueXid() {
        return generateUniqueXid("ACP_S", "aircompressor_system");
    }

    public boolean isXidUnique(String xid, int excludeId) {
        return isXidUnique(xid, excludeId, "aircompressor_system");
    }

	/**
	 * 根据压缩空气系统编号获得压缩空气系统
	 * 
	 * @param id
	 *            压缩空气系统编号
	 * @return
	 */
	public ACPSystemVO getACPSystemVOById(int id) {
		String sql = CAS_SELECT + " where id= ? ";
		List<ACPSystemVO> list = query(sql, new Object[] { id },
				new CompressedAirSystemRowMapper());
		if (list.size() > 0)
			return list.get(0);
		else
			return null;
	}

	/**
	 * 根据工厂编号获得空压机系统
	 * 
	 * @param id
	 * @return 压缩空气系统集合
	 */
	public List<ACPSystemVO> getACPSystemVOByfactoryId(int factoryid) {
		String sql = CAS_SELECT + " where factoryid= ? ";
		List<ACPSystemVO> acpSystem = query(sql, new Object[] { factoryid },
				new CompressedAirSystemRowMapper());
		return acpSystem;
	}

	/**
	 * 根据压缩空气系统编号获得成员中的空压机
	 */
	public List<ACPVO> getACPsByACSId(int compressorId) {
		List<ACPVO> acpList = query(FIND_AIR_COMPRESSOR,
				new Object[] { compressorId }, new ACPVORowMapper());
		return acpList;
	}

	/**
	 * 根据工厂编号获得工厂的空压机(查询出来的没有<空压机所属的系统编号>) 此方法仅是用于编辑系统是选择的空压机下拉列表
	 * 
	 * @param factoryId
	 *            工厂编号
	 * @return 空压机集合
	 */
	public List<ACPVO> getACPsByFactoryId(int factoryId) {
		List<ACPVO> acpList = query(FIND_FACTORY_AIR_COMPRESSOR,
				new Object[] { factoryId }, new ACPVORowMapper());
		return acpList;
	}

	/***************************************************************************
	 * 根据工厂编号获得工厂的空压机(查询出来的有<空压机所属的系统编号>) 此方法用于初始化系统树形菜单
	 * 
	 * @param factoryId
	 *            工厂编号
	 * @return 空压机集合
	 */
	public List<ACPVO> searchACPsByFactoryId(int factoryId) {
		List<ACPVO> acpList = query(FIND_FACTORY_AIR_COMPRESSOR2,
				new Object[] { factoryId }, new ACPVORowMapper());
		return acpList;
	}

	/**
	 * 查询空压机编号和名称的mappper
	 * 
	 * @author Administrator
	 * 
	 */
	class ACPVORowMapper implements GenericRowMapper<ACPVO> {
		public ACPVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ACPVO acp = new ACPVO();
			acp.setId(rs.getInt(1));
			acp.setAcpname(rs.getString(2));
			acp.setCompressorId(rs.getInt(3));
			return acp;
		}
	}

	/**
	 * 数据点
	 */
	class DataPointRowMapper implements GenericRowMapper<DataPointVO> {
		public DataPointVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			DataPointVO dp;
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
			return dp;
		}
	}

	/**
	 * 根据工厂编号获得压缩系统中数据点
	 * 
	 * @param casId
	 *            压缩空气系统
	 * @return
	 */
	public List<DataPointVO> getDataPointsByFactoryId(int compressorId) {
		String sql = FIND_POINT_AIR;
		List<DataPointVO> list = query(sql, new Object[] { compressorId },
				new DataPointRowMapper());
		return list;
	}

	/**
	 * 获得压缩空气系统中的数据点
	 * 
	 * @param ACSId
	 *            压缩空气系统编号
	 * @return
	 */
	public List<DataPointVO> getDataPointsByACSId(int ACSId) {
		String sql = FIND_AIR_SYSTEM_POINTS;
		List<DataPointVO> list = query(sql, new Object[] { ACSId },
				new DataPointRowMapper());
		return list;
	}

	/**
	 * 获得指定工厂-压缩系统-空压机下的数据点
	 * 
	 * @param compressorId
	 * @return
	 */
	public List<DataPointVO> getDataPointsByFactoryCompress(int factoryId) {
		String sql = FIND_POINT_COMPRESS_FACTORY;
		List<DataPointVO> list = query(sql, new Object[] { factoryId },
				new DataPointRowMapper());
		return list;
	}

	/**
	 * 根据空压机编号获得 数据点<此数据的为空压机成员>
	 * 
	 * @param compressorId
	 *            空压机编号
	 * @return
	 */
	public List<DataPointVO> getDataPointsByCompressorId(int compressorId) {
		String sql = FIND_POINT_COMPRESSOR;
		List<DataPointVO> list = query(sql, new Object[] { compressorId },
				new DataPointRowMapper());
		return list;
	}

	/**
	 * 根据空压机编号获得 数据点<此数据点查询方式为空压机-数据点-数据源-数据点>
	 * 
	 * @param acpId
	 *            空压机编号
	 * @return
	 */
	public List<DataPointVO> getDpByDsByAcpId(int factoryid) {
		String sql = "select dp.id, dp.xid, dp.dataSourceId, dp.data,dp.id from dataPoints dp where dataSourceId in(select id from  datasources where factoryId=?) and id not in (select dpid from aircompressor_members where acid in(select id from aircompressor where type=0 and factoryid=? ))";
		List<DataPointVO> list = query(sql, new Object[] { factoryid,factoryid },
				new DataPointRowMapper());
		return list;
	}

	/**
	 * 根据空压机编号获得空压机属性
	 * 
	 * @param Id
	 * @return
	 */
	public ACPVO getACPsById(int Id) {
		String sql = "select id,acname,actid,xid from aircompressor where id=?";
		List<ACPVO> acpList = query(sql, new Object[] { Id },
				new ACPVORowMapper());
		if (acpList.size() > 0) {
			return acpList.get(0);
		} else
			return null;
	}

	/**
	 * 查询一个工厂中不存在空压机系统中的数据点
	 * 
	 * @param factoryId
	 * @return
	 */
	public List<DataPointVO> getDataPointsNotUse(int factoryId) {
		String sql = FINT_POINT_NOT_IN_ACP;
		List<DataPointVO> list = query(sql, new Object[] { factoryId },
				new DataPointRowMapper());
		return list;
	}

	/**
	 * 根据压缩空气系统编号删除系统成员
	 * 
	 * @param id
	 *            系统编号
	 */
	void deleteCompressedMember(int CompressedSystemId) {
		ejt.update("delete aircompressor_system_members where acsid=?",
				new Object[] { CompressedSystemId });
	}
	public ACPSystemVO saveSystemBanse(final int compressedSystemId,final ACPSystemVO acpSystem) {
		if (compressedSystemId != Common.NEW_ID) {
			ejt.update(
					"update aircompressor_system set xid=?,systemname=? where id=?",
					new Object[] { acpSystem.getXid(),
							acpSystem.getSystemname(),
							compressedSystemId });
							acpSystem.setId(compressedSystemId);
		}
		else{
			acpSystem.setId(doInsert(
					"insert into aircompressor_system (xid,systemname,factoryid) values(?,?,?)",
					new Object[] { acpSystem.getXid(),
							acpSystem.getSystemname(),
							acpSystem.getFactoryId() }));
		}
		return acpSystem;
  	}
	/**
	 * 修改压缩空气系统基本数据,成员(这里更新前,要删除原有压缩系统中的成员,重新保存)
	 * 
	 * @param compressedSystemId
	 *            压缩空气系统编号
	 * @param acpSystem
	 *            压缩空气系统
	 * @param statistics
	 *            统计参数集合
	 * @param points
	 *            点设备集合
	 * @param compressors
	 *            空压机集合
	 */
	public void updateCompressedSystem(final int compressedSystemId,
			 final int[] statistics,final int[] points, final int[] compressors) {
		final ExtendedJdbcTemplate ejt2 = ejt;
		getTransactionTemplate().execute(
				new TransactionCallbackWithoutResult() {
					@SuppressWarnings("synthetic-access")
					@Override
					protected void doInTransactionWithoutResult(
							TransactionStatus status) {
						int num = -1;
						
						// 执行删除成员操作
						ejt2.update("delete aircompressor_system_members where acsid=?",
										new Object[] { compressedSystemId });
						// 给压缩空气系统添加新的数据点成员
						if (points.length == statistics.length) {
							StringBuilder insertSql = new StringBuilder();
							insertSql.append(COMPRESSED_SYSTEM_INSET);
							for (int i = 0; i < statistics.length; i++) {
								if (i > 0) {
									insertSql.append(" union all ");
								}
								insertSql.append(COMPRESSED_SYSTEM_INSET_VALUE);
							}
							int index = 0;
							Object[] params = new Object[(statistics.length + compressors.length) * 4];
							for (int i = 0; i < statistics.length; i++) {
								if (num != -1) {
									params[index++] = num;
								} else {
									params[index++] =compressedSystemId;
								}
								params[index++] = 1;
								params[index++] = points[i];
								params[index++] = statistics[i];

							}

							for (int i = 0; i < compressors.length; i++) {
								insertSql.append(" union all ");
								insertSql.append(COMPRESSED_SYSTEM_INSET_VALUE);
								if (num != -1) {
									params[index++] = num;
								} else {
									params[index++] =compressedSystemId;
								}
								params[index++] = 0;
								params[index++] = compressors[i];
								params[index++] = null;
							}
							ejt2.update(insertSql.toString(), params);
							// ejt2.update(insertSql, new Object[] { params });
						}
						// 给压缩空气系统添加空压机成员
					}
				});
	}

	/**
	 * 更新空压机成员
	 * 
	 * @param acpId
	 * @param statistics
	 * @param points
	 */
	public void updateACPMember(final int acpId, final int[][] update,
			final int[][] add) {
		final ExtendedJdbcTemplate ejt2 = ejt;

		getTransactionTemplate().execute(
				new TransactionCallbackWithoutResult() {
					@SuppressWarnings("synthetic-access")
					@Override
					protected void doInTransactionWithoutResult(
							TransactionStatus status) {
						StatisticsDao statisticsDao = new StatisticsDao();

						for (int i = 0; i < update.length; i++) {
							String sql = "update aircompressor_members set spid=? where dpid=?  and acid=?";
							ejt2.update(sql, new Object[] { update[i][0],
									update[i][1], acpId });
						}
						// 添加sql语句
						// 1.删除原有配置
						if (add.length > 0) {
							ejt2
									.update(
											"delete aircompressor_members where acaid is null and acid=?",
											new Object[] { acpId });
							// 重新添加成员
							StringBuilder insertSql = new StringBuilder();
							Object[] params = new Object[(add.length) * 3];
							insertSql
									.append("insert into aircompressor_members (acid,spid,dpid)");
							int insertIndex = 0;
							for (int i = 0; i < add.length; i++) {
								if (i > 0) {
									insertSql.append(" union all ");
								}
								insertSql.append(" select ?,?,? ");
								params[insertIndex++] = acpId;
								params[insertIndex++] = add[i][0];
								params[insertIndex++] = add[i][1];
							}
							ejt2.update(insertSql.toString(), params);
						}
					}
				});
	}

	/**
	 * 根绝压缩空气系统删除压缩空气系统和成员
	 * 
	 * @param compressedSystemId
	 *            压缩空气系统编号
	 */
	public void deleteCompressedById(final int compressedSystemId) {
		final ExtendedJdbcTemplate ejt2 = ejt;

		getTransactionTemplate().execute(
				new TransactionCallbackWithoutResult() {
					@SuppressWarnings("synthetic-access")
					@Override
					protected void doInTransactionWithoutResult(
							TransactionStatus status) {
						// 执行删除成员操作
						ejt2
								.update(
										"delete aircompressor_system_members where acsid=?",
										new Object[] { compressedSystemId });

						ejt2.update("delete aircompressor_system where id=?",
								new Object[] { compressedSystemId });

					}
				});
	}

	/**
	 * 验证空压机是否配置统计参数
	 * 
	 * @param acpId
	 *            空压机id
	 * @return true验证过,false没验证过
	 */
	public int checkSpid(int[] compressors) {
		// ACP_MEMBER_SPID
		for (int acpId : compressors) {
			int count = ejt.queryForInt(ACP_MEMBER_SPID,
					new Object[] { acpId }, -1);
			if (count == 0)
				return acpId;
		}
		return -1;
	}
	/**
	 * 清空空压机成员
	 * @param acpId 空压机id
	 */
	public void clearAcpMember(int acpId) {
		ejt.update(
				"delete aircompressor_members where spid is not null and acid=?",
				new Object[] { acpId });
	}
	/**
	 * 获取所有空压机系统VO
	 * 
	 * @return 所有空压机系统VO
	 */
	public List<ACPSystemVO> getAllACPSystem() {
		List<ACPSystemVO> list = query(CAS_SELECT, new Object[0],
				new CompressedAirSystemRowMapper());
		return list;
	}
/**
 * 查询自动生成系统点的实体
 * @param id 工厂id
 * @return
 */
	public List<ACPVO> getSystemconfig(int id) {
		List<ACPVO> list = query(
				"select ac.id ,ac.acname,ac.actid from aircompressor ac where ac.type=1 and ac.factoryId=?",
				new Object[] { id }, new ACPVORowMapper());
		return list;
	}
}
