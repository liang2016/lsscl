package com.lsscl.app.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.serotonin.db.spring.ExtendedJdbcTemplate;
import com.serotonin.db.spring.GenericRowMapper;
import com.serotonin.mango.db.dao.BaseDao;
import com.serotonin.mango.db.dao.DataPointDao;
import com.serotonin.mango.vo.AppPoints;
import com.serotonin.mango.vo.Appacpinfo;
import com.serotonin.mango.vo.DataPointVO;

public class AppsettingDao extends BaseDao {

	private static final String APP_LIST_SELECT = "SELECT id,scopeId,name,power,type FROM appAcps";
	private static final String Point_List_Select = "select p.id,p.pointId,p.name,a.id from appPoints p " +
													"left join aircompressor_members am on am.dpid = p.pointId " +
													"left join aircompressor a on a.id = am.acid ";
    private DataPointDao dataPointDao = new DataPointDao();
	class AppsettingRowMapper implements GenericRowMapper<Appacpinfo> {
		public Appacpinfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			Appacpinfo appacpinfo = new Appacpinfo();
			appacpinfo.setId(rs.getInt("id"));
			appacpinfo.setScopeId(rs.getInt("scopeId"));
			appacpinfo.setName(rs.getString("name"));
			appacpinfo.setPower(rs.getFloat("power"));
			appacpinfo.setType(rs.getString("type"));
			return appacpinfo;
		}
	}
	
	class AppPointsRowMapper implements GenericRowMapper<AppPoints<?>> {
		public AppPoints<?> mapRow(ResultSet rs, int rowNum) throws SQLException {
			AppPoints<?> point = new AppPoints();
			point.setId(rs.getInt(1));
			point.setPointId(rs.getInt(2));
			point.setName(rs.getString(3));
			point.setAid(rs.getInt(4));
			return point;
		}
	}
	/**
	 * 根据工厂id查询所有手机设定空压机信息
	 * 
	 * @param scopeid
	 *            编号
	 * @return
	 */
	public List<Appacpinfo> getScopeAppacpinfoList(int scopeid) {
		String sql = APP_LIST_SELECT + " where scopeid=?";
		List<Appacpinfo> list = appacpinfoQuery(sql, new Object[] { scopeid },
				0);
		return list;
	}

	/**
	 * 手机空压机查询query
	 * 
	 * @param sql
	 *            sql语句
	 * @param params
	 *            参数
	 * @param limit
	 * @return 工厂集合
	 */
	public List<Appacpinfo> appacpinfoQuery(String sql, Object[] params,
			int limit) {
		List<Appacpinfo> list = query(sql, params, new AppsettingRowMapper(),
				limit);
		return list;
	}

	// 删除空压机使用了事务
	public void deleteAppacpinfoById(final int appacpid) {
		final ExtendedJdbcTemplate ejt2 = ejt;
		getTransactionTemplate().execute(
				new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(
							TransactionStatus status) {
						ejt2.update("delete from appacpinfo where appacpid=?",
								new Object[] { appacpid });
					}
				});
	}

	/**
	 * 根据空压机id查询属性点
	 * @param acpid
	 * @return
	 */
	public List<AppPoints<?>> getPointsByAcpId(String acpid) {
		String sql = Point_List_Select+" where p.acpId = ? order by p.name";
		List<AppPoints<?>>points = query(sql,new Object[]{acpid},new AppPointsRowMapper());
		for(AppPoints<?> p:points){
			DataPointVO pv = dataPointDao.getDataPoint(p.getPointId());
			p.setDataPointVo(pv);
		}
		return points;
	}

	/**
	 * 查询app空压机
	 * @param acpid
	 * @return
	 */
	public Appacpinfo getAcpById(String acpid) {
		return queryForObject(APP_LIST_SELECT + " where id = ?", new Object[]{acpid}, new AppsettingRowMapper());
	}

	public boolean updatePoint(String pid, String name, String pointId, String aid) {
		//查询是否与其他点同名
		String isExist = "select count(*) from appPoints where acpId = ? and id !=? and(name = ? or pointId = ?)";
		int count = queryForObject(isExist, new Object[]{aid,pid,name,pointId}, Integer.class, 0);
		if(count!=0)return false;
		String sql = "update appPoints set name = ?,pointId = ?,acpId = ? where id = ?";
		this.ejt.update(sql, new Object[]{name,pointId,aid,pid});
		return true;
	}

	public boolean addPoint(String name, String pointId, String aid) {
		String isExist = "select count(*) from appPoints where acpId = ? and (name = ? or pointId = ?)";
		int count = queryForObject(isExist, new Object[]{aid,name,pointId}, Integer.class, 0);
		if(count!=0)return false;
		String sql = "insert into appPoints(name,pointId,acpId) values(?,?,?)";
		this.ejt.update(sql, new Object[]{name,pointId,aid});
		return true;
	}

	/**
	 * 新增app空压机
	 * @param scopeId
	 * @param name
	 * @return
	 */
	public int addAcp(String scopeId,String name,String power,String type) {
		String sql = "insert into appAcps(scopeId,name,power,type)values(?,?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		this.ejt.update(sql, new Object[]{scopeId,name,power,type}, keyHolder);
		return keyHolder.getKey().intValue();
	}

	/**
	 * 修改app空压机名称
	 * @param aid
	 * @param name
	 */
	public void updateAcp(String aid, String name,String power,String type) {
		String sql = "update appAcps set name = ?,power=?,type = ? where id = ?";
		this.ejt.update(sql, new Object[]{name,power,type,aid});
	}

	public void deleteAcp(String aid) {
		String sql = "delete from appAcps where id = ?";
		String delPoints = "delete from appPoints where acpId = ?";
		this.ejt.update(delPoints,new Object[]{aid});
		this.ejt.update(sql,new Object[]{aid});
	}
	public void deletePoint(String pid){
		String sql = "delete from appPoints where id = ?";
		this.ejt.update(sql,new Object[]{pid});
	}
}
