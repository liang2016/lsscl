package com.serotonin.mango.db.dao.power;

import java.util.List;

import com.serotonin.db.spring.GenericRowMapper;
import com.serotonin.mango.db.dao.BaseDao;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.vo.power.*; 
import com.serotonin.mango.db.dao.scope.*;

import java.sql.SQLException;
import java.sql.ResultSet;
public class RoleDao extends BaseDao {
	
	/**
	 * 初始化当前用户是没有角色的
	 */
	public static final int NO_ROLE = -1;
	
	/**
	 * 根据用户查找角色列表的SQL语句
	 */
	private static final String SEARCH_ROLE_BY_USER = " SELECT R.ID,R.ROLENAME,R.DESCRIPTION,R.SCOPETYPE,UR.DEFAULTROLE FROM ROLE R LEFT JOIN USER_ROLE UR ON R.ID = UR.RID WHERE UR.UID = ? ";
	
	/**
	 * 制定规则：
	 * 判断是否为默认角色 
	 * @param param defaultRole 字段为1则为默认角色，0则不是默认角色
	 * @return 是否
	 */
	public static boolean isDefaultRoleId(int param){
		if(param==1)return true;
		else return false;
	}
	
	/**
	 * 根据用户编号查找角色列表  
	 * @param userId 用户编号
	 * @return List<Role> 角色列表
	 */
	public List<RoleVO> findByUser(Integer userId) {
		List<RoleVO> roleList = query(SEARCH_ROLE_BY_USER, new Object[]{userId}, new RoleVORowMapper());
		return roleList;
	}
	
	/**
	 * 从角色列表中取出当前用户的默认角色
	 * @param roleList 角色列表
	 * @return 默认角色编号
	 */
	public int getDefaultRoleId(List<RoleVO> roleList){
		int currentRoleId = NO_ROLE;
		for(RoleVO roleVO:roleList){
			if(roleVO.isDefaultRole()==true){
				currentRoleId =  roleVO.getId();
				break;
			}				
		}
		return currentRoleId;
	}
	
	/**
	 * 获取默认角色
	 * @param roleList 角色集合
	 * @return 默认角色
	 */
	public RoleVO getDefaultRole(List<RoleVO> roleList){
		RoleVO currentRole = null;
		for(RoleVO roleVO:roleList){
			if(roleVO.isDefaultRole()==true){
				currentRole = roleVO;
				break;
			}				
		}
		return currentRole;
	}
	  
	/**
	 * 角色查询结果
	 * @author 王金阳
	 *
	 */
	class RoleVORowMapper implements GenericRowMapper<RoleVO> {
		public RoleVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			RoleVO roleVO = new RoleVO();
			int i = 0;
			roleVO.setId(rs.getInt(++i));
			roleVO.setRolename(rs.getString(++i));
			roleVO.setDescription(rs.getString(++i));
			roleVO.setScopeType(rs.getInt(++i));
			roleVO.setDefaultRole(rs.getInt(++i)==1?true:false);
			return roleVO;
		}
	}
	 
}
