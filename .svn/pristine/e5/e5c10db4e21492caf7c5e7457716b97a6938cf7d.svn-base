package com.serotonin.mango.db.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.serotonin.mango.vo.google.BaseZone;
import com.serotonin.mango.vo.google.SubZone;
import com.serotonin.db.spring.GenericRowMapper;
import com.serotonin.mango.db.dao.BaseDao;

/**
 * 区域信息查询
 * 
 * @author 刘建坤
 * 
 */
public class ZoneDao extends BaseDao {
	private static final String ZONE_SELECT = "select z_id,z_name,z_comment,lon,lat,z_range from zone";
	private static final String SUB_ZONE_SELECT = "select s_id,s_z_name,s_z_comment,lon,lat,s_range,z_id from sub_zone";

	class ZoneRowMapper implements GenericRowMapper<BaseZone> {
		public BaseZone mapRow(ResultSet rs, int rowNum) throws SQLException {
			BaseZone zone = new BaseZone();
			zone.setId(rs.getInt(1));
			zone.setName(rs.getString(2));
			zone.setComment(rs.getString(3));
			zone.setLon(rs.getFloat(4));
			zone.setLat(rs.getFloat(5));
			zone.setRange(rs.getFloat(6));
			return zone;
		}

	}

	class SubZoneRowMapper implements GenericRowMapper<SubZone> {
		public SubZone mapRow(ResultSet rs, int rowNum) throws SQLException {
			SubZone subZone = new SubZone();
			subZone.setId(rs.getInt(1));
			subZone.setName(rs.getString(2));
			subZone.setComment(rs.getString(3));
			subZone.setLon(rs.getFloat(4));
			subZone.setLat(rs.getFloat(5));
			subZone.setRange(rs.getFloat(6));
			subZone.setZId(rs.getInt(7));
			return subZone;
		}
	}

	/**
	 * 查询所有区域
	 * 
	 * @return
	 */
	public List<BaseZone> getZones() {
		return query(ZONE_SELECT, new ZoneRowMapper());
	}
	/**
	 * 查询所有子区域
	 * 
	 * @return
	 */
	public List<SubZone> getAllSubZones() {
		return query(SUB_ZONE_SELECT, new SubZoneRowMapper());
	}

	/**
	 * 根据区域编号查询子区域
	 */
	public List<SubZone> getSubZonesByZId(int zId) {
		String sql = SUB_ZONE_SELECT + " where z_id=?";
		return subZoneQuery(sql, new Object[] { zId }, 0);
	}

	/**
	 * 根据子区域编号查询子区域
	 * 
	 */
	public SubZone selectSubZoneById(int sId) {
		String sql = SUB_ZONE_SELECT + " where s_id=?";
		List<SubZone> list = subZoneQuery(sql, new Object[] { sId }, 0);
		return list.get(0);
	}

	/**
	 * 查询子集合区域
	 * 
	 * @param sql
	 *            sql语句
	 * @param params
	 *            参数
	 * @param limit
	 *            数量
	 * @return 子区域集合
	 */
	public List<SubZone> subZoneQuery(String sql, Object[] params, int limit) {
		List<SubZone> list = query(sql, params, new SubZoneRowMapper(), limit);
		return list;
	}

}
