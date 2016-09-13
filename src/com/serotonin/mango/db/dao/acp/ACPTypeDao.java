package com.serotonin.mango.db.dao.acp;

import java.util.List;
import java.sql.SQLException;
import java.sql.ResultSet;
import com.serotonin.db.spring.GenericRowMapper;
import com.serotonin.mango.db.dao.BaseDao;
import com.serotonin.mango.vo.acp.ACPTypeVO;

/**
 * 机器型号信息数据库操作类
 * 
 * @author 王金阳
 * 
 */
public class ACPTypeDao extends BaseDao {

	/**
	 * 查询所有
	 */
	public static final String SELECT_ALL = " SELECT ID,TYPENAME,DESCRIPTION,TYPE FROM AIRCOMPRESSOR_TYPE ";
	/**
	 * 保存行
	 */
	public static final String INSERT_BASE = " insert into aircompressor_type(typename,type,warnCount,alarmCount) values(?,?,?,?) ";

	/**
	 * 更新行
	 */
	public static final String UPDATE_BASE = " update aircompressor_type set typename=? , warnCount=?,alarmCount=? where id =? ";

	/**
	 * 根据ID查找
	 */
	public static final String FIND_BY_ID = " select id,typename,description,type,warnCount,alarmCount from aircompressor_type where id =? ";

	/**
	 * 当前保存到行中，名字是否已经存在
	 */
	public static final String NAME_IS_EXIST = " SELECT COUNT(*) FROM AIRCOMPRESSOR_TYPE WHERE TYPENAME=? AND ID !=? ";

	public static final String HAS_ATTR = " select count(*)  from aircompressor_type_attr where actid  = ? ";

	/**
	 * 查询所有型号集合
	 * 
	 * @return 型号集合
	 */
	public List<ACPTypeVO> findAll() {
		List<ACPTypeVO> result = query(SELECT_ALL, new Object[0],
				new AcpRowMapper());
		return result;
	}

	/**
	 * 根据类型查询
	 * 
	 * @param type
	 *            类型 机器/系统
	 * @return
	 */
	public List<ACPTypeVO> findByType(int type) {
		List<ACPTypeVO> result = query(SELECT_ALL + " where type = ? ",
				new Object[] { type }, new AcpRowMapper());
		return result;
	}

	/**
	 * 根据ID查找行
	 * 
	 * @param id
	 *            ID
	 * @return 结果行
	 */
	public ACPTypeVO findById(int id) {
		List<ACPTypeVO> result = query(FIND_BY_ID, new Object[] { id },
				new AcpBaseRowMapper());
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	class AcpBaseRowMapper implements GenericRowMapper<ACPTypeVO> {
		public ACPTypeVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ACPTypeVO acpTypeVO = new ACPTypeVO();
			int i = 1;
			acpTypeVO.setId(rs.getInt(i++));
			acpTypeVO.setTypename(rs.getString(i++));
			acpTypeVO.setDescription(rs.getString(i++));
			acpTypeVO.setType(rs.getInt(i++));
			acpTypeVO.setWarnCount(rs.getString(i++));
			acpTypeVO.setAlarmCount(rs.getString(i++));
			return acpTypeVO;
		}
	}

	/**
	 * 保存型号信息
	 * 
	 * @return 被保存行的ID
	 */
	public int save(ACPTypeVO vo) {
		return doInsert(
				INSERT_BASE,
				new Object[] { vo.getTypename(), vo.getType(),
						vo.getWarnCount(), vo.getAlarmCount() });
	}

	/**
	 * 更新一个型号信息
	 */
	public void update(ACPTypeVO vo) {
		ejt.update(
				UPDATE_BASE,
				new Object[] { vo.getTypename(), vo.getWarnCount(),
						vo.getAlarmCount(), vo.getId() });
	}

	/**
	 * 型号名称是否已经存在
	 * 
	 * @param acpTypeVO
	 *            将要被保存的数据
	 * @return 是否存在
	 */
	public int nameisExist(ACPTypeVO acpTypeVO) {
		return ejt.queryForInt(NAME_IS_EXIST,
				new Object[] { acpTypeVO.getTypename(), acpTypeVO.getId() }, 0);

	}

	/**
	 * 当前型号是否可以被删除
	 * 
	 * @param typeId
	 *            型号ID
	 * @return 是否可以被删除
	 */
	public boolean canDelete(int typeId) {
		// 空压机成员表中属性和该型号相关的行
		int aircompressor_members_count = ejt
				.queryForInt(
						" select count(*) from aircompressor_members where acaid in ( select acaid from aircompressor_type_attr where actid = ? ) ",
						new Object[] { typeId }, 0);
		// 空压机表中为此型号的空压机
		int aircompressor_count = ejt.queryForInt(
				"select count(*) from aircompressor where actid = ?",
				new Object[] { typeId }, 0);
		if (aircompressor_members_count == 0 && aircompressor_count == 0)
			return true;
		else
			return false;
	}

	/**
	 * 此型号下是否有属性配置
	 * 
	 * @param typeId
	 *            型号ID
	 * @return 是否已经配置了属性
	 */
	public boolean hasAttr(int typeId) {
		int count = ejt.queryForInt(HAS_ATTR, new Object[] { typeId }, 0);
		if (count == 0)
			return false;
		else
			return true;
	}

	class AcpRowMapper implements GenericRowMapper<ACPTypeVO> {
		public ACPTypeVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ACPTypeVO acpTypeVO = new ACPTypeVO();
			int i = 1;
			acpTypeVO.setId(rs.getInt(i++));
			acpTypeVO.setTypename(rs.getString(i++));
			acpTypeVO.setDescription(rs.getString(i++));
			acpTypeVO.setType(rs.getInt(i++));
			return acpTypeVO;
		}
	}

	public String getAcpWarnOrAlarmCount(int acpId, int type, String alarmOrWarn) {
		String sql = "select "+alarmOrWarn+"  from aircompressor_type where id=(select actid from aircompressor where id=? and type=?)";
		return (String)ejt.queryForObject( sql,
				new Object[] { acpId, type },String.class,"0");

	}
}
