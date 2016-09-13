package com.serotonin.mango.db.dao.acp;

import com.serotonin.mango.db.dao.BaseDao;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.vo.acp.AcpMetaVo;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.TransactionStatus;
import com.serotonin.util.SerializationHelper;
import com.serotonin.db.spring.GenericRowMapper;
import java.util.List;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class AcpMetaDao extends BaseDao {
	private static final String INSERT_BASE = "insert into aircompressorMetaAttr (acpType,metaName,data) VALUES(?,?,?) ";
	private static final String UPDATE_BASE = "update aircompressorMetaAttr set metaName=?, data=? where id=? ";
	private static final String DELETE_BASE = "delete aircompressorMetaAttr where id=? ";
	private static final String SELECT_BASE = "select id,acpType,metaName,data from aircompressorMetaAttr where acpType=?";
	private static final String SELECT_BASE_ID = "select id,acpType,metaName,data from aircompressorMetaAttr where id=?";
	int id;

	/**
	 * 保存一个模版
	 * 
	 * @param acpMetaVo
	 *            元数据模版
	 * @return 影响行数
	 */
	public int save(final AcpMetaVo acpMetaVo) {
		getTransactionTemplate().execute(
				new TransactionCallbackWithoutResult() {
					@SuppressWarnings("synthetic-access")
					@Override
					protected void doInTransactionWithoutResult(
							TransactionStatus status) {
						// 添加关系以及配置
						id = doInsert(
								INSERT_BASE,
								new Object[] {
										acpMetaVo.getAcpTypeId(),
										acpMetaVo.getMetaName(),
										SerializationHelper
												.writeObject(acpMetaVo.getDp()) },
								new int[] { Types.INTEGER, Types.VARCHAR,
										Types.BLOB });

					}
				});
		return id;
	}

	/**
	 * 修改一个模版
	 * 
	 * @param acpMetaVo
	 */
	public void update(final AcpMetaVo acpMetaVo) {
		getTransactionTemplate().execute(
				new TransactionCallbackWithoutResult() {
					@SuppressWarnings("synthetic-access")
					@Override
					protected void doInTransactionWithoutResult(
							TransactionStatus status) {
						// 添加关系以及配置
						ejt.update(
								UPDATE_BASE,
								new Object[] {
										acpMetaVo.getMetaName(),
										SerializationHelper
												.writeObject(acpMetaVo.getDp()),
										acpMetaVo.getId() }, new int[] {
										Types.VARCHAR, Types.BLOB,
										Types.INTEGER });

					}
				});
	}

	/**
	 * 根据id删除一个模版
	 * 
	 * @param id
	 */
	public void delete(final int id) {
		getTransactionTemplate().execute(
				new TransactionCallbackWithoutResult() {
					@SuppressWarnings("synthetic-access")
					@Override
					protected void doInTransactionWithoutResult(
							TransactionStatus status) {
						ejt.update(DELETE_BASE, new Object[] { id });

					}
				});
	}

	class AcpMetaRowMapper implements GenericRowMapper<AcpMetaVo> {
		public AcpMetaVo mapRow(ResultSet rs, int rowNum) throws SQLException {
			AcpMetaVo metaVo = new AcpMetaVo();
			int i = 1;
			metaVo.setId(rs.getInt(i++));
			metaVo.setAcpTypeId(rs.getInt(i++));
			metaVo.setMetaName(rs.getString(i++));
			DataPointVO dp = (DataPointVO) SerializationHelper.readObject(rs
					.getBlob(i++).getBinaryStream());
			metaVo.setDp(dp);
			return metaVo;
		}
	}

	public List<AcpMetaVo> findMetaPoints(int typeId) {
		List<AcpMetaVo> points = query(SELECT_BASE,new Object[]{typeId},new AcpMetaRowMapper());
		return points;
	}
	public AcpMetaVo findMetaPoint(int id) {
		AcpMetaVo acpMeta = query(SELECT_BASE_ID,new Object[]{id},new AcpMetaRowMapper()).get(0);
		return acpMeta;
	}
}
