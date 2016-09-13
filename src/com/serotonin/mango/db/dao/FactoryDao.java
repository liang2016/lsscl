package com.serotonin.mango.db.dao;

import com.serotonin.db.spring.GenericRowMapper;
import java.sql.SQLException;

import java.sql.ResultSet;
import com.serotonin.mango.vo.Factory;
import com.serotonin.mango.db.dao.BaseDao;

import java.util.List;
import com.serotonin.mango.vo.FactoryList;
import com.serotonin.db.spring.ExtendedJdbcTemplate;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

/**
 * 工厂数据交互Dao
 * 
 * @author Administrator
 * 
 */
public class FactoryDao extends BaseDao {
	/**
	 * 工厂查询语句
	 */
	private static final String FACTORY_SELECT = "select id,factory_name ,factory_comment ,lon,lat,z_id ,s_z_id from factory";

	class FactoryRowMapper implements GenericRowMapper<Factory> {
		public Factory mapRow(ResultSet rs, int rowNum) throws SQLException {
			Factory factory = new Factory();
			factory.setId(rs.getInt(1));
			factory.setName(rs.getString(2));
			factory.setComment(rs.getString(3));
			factory.setLon(rs.getFloat(4));
			factory.setLat(rs.getFloat(5));
			factory.setZoneId(rs.getInt(6));
			factory.setSZId(rs.getInt(7));
			return factory;
		}

	}

	/**
	 * 查询所有工厂
	 * 
	 * @return
	 */
	public List<Factory> getFactories() {
		return query(FACTORY_SELECT, new FactoryRowMapper());
	}

	/**
	 * 根据编号查询工厂
	 * 
	 * @param id
	 *            编号
	 * @return
	 */
	public Factory getFactoriesById(int id) {
		String sql = FACTORY_SELECT + " where id=?";
		List<Factory> list = factoryQuery(sql, new Object[] { id }, 0);
		return list.get(0);
	}

	/**
	 * 根据子区域编号查询工厂
	 * 
	 * @param SZId
	 *            子区域编号
	 * @return
	 */
	public List<Factory> getFactoriesBySZId(int SZId) {
		String sql = FACTORY_SELECT + " where s_z_id=?";
		List<Factory> list = factoryQuery(sql, new Object[] { SZId }, 0);
		return list;
	}

	/**
	 * 根据区域编号查询工厂
	 * 
	 * @param ZId
	 *            区域编号
	 * @return
	 */
	public List<Factory> getFactoriesByZId(int ZId) {
		String sql = FACTORY_SELECT + " where z_id=?";
		List<Factory> list = factoryQuery(sql, new Object[] { ZId }, 0);
		return list;
	}

	/**
	 * 工厂查询query
	 * 
	 * @param sql
	 *            sql语句
	 * @param params
	 *            参数
	 * @param limit
	 * @return 工厂集合
	 */
	public List<Factory> factoryQuery(String sql, Object[] params, int limit) {
		List<Factory> list = query(sql, params, new FactoryRowMapper(), limit);
		return list;
	}

	/**
	 * 查询工厂列表
	 */
	public List<FactoryList> getFactoryList() {
		String sql = "SELECT f.id ,f.factory_name,f.factory_comment,z.z_name,s.s_z_name FROM factory f LEFT JOIN zone z ON f.z_id=z.z_id  LEFT JOIN sub_zone s ON s.s_id=f.s_z_id";
		List<FactoryList> list = query(sql, new FactoryListRowMapper());
		return list;
	}

	class FactoryListRowMapper implements GenericRowMapper<FactoryList> {
		public FactoryList mapRow(ResultSet rs, int rowNum) throws SQLException {
			FactoryList factoryList = new FactoryList();
			factoryList.setId(rs.getInt(1));
			factoryList.setName(rs.getString(2));
			factoryList.setComment(rs.getString(2));
			factoryList.setZone(rs.getString(4));
			factoryList.setSubZone(rs.getString(5));
			// factoryList.setEvents(rs.getInt(6));
			return factoryList;
		}

	}
//删除工厂使用了事务
	public void deletefactoryById(final int factoryId) {
		final ExtendedJdbcTemplate ejt2 = ejt;
		getTransactionTemplate().execute(
				new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(
							TransactionStatus status) {
						ejt2.update("delete from factory where id=?",new Object[]{factoryId});
					}
				});
	        }
	/**
	 * 保存一个工厂
	 * @param factory 工厂
	 */
   public void savefactory(final Factory factory){
	 doInsert("insert into factory (factory_name,factory_comment,lon,lat,z_id,s_z_id)  values(?,?,?,?,?,?)",new Object[] {factory.getName(),factory.getComment(),factory.getLon(),factory.getLat(),factory.getZoneId(),factory.getSZId()});
   }
}
