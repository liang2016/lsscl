package com.serotonin.mango.db.dao.power;

import java.util.List;

import com.serotonin.db.spring.GenericRowMapper;
import com.serotonin.mango.db.dao.BaseDao;
import com.serotonin.mango.vo.power.ActionVO;
import com.serotonin.mango.vo.power.RoleVO;
import java.sql.SQLException;
import java.sql.ResultSet;

public class ActionDao extends BaseDao{
	
	/**
	 * 根据角色查找权限的sql语句
	 */
	public static final String SELECT_ACTION_BY_ROLE = " SELECT id,actionname,description,url FROM ACTION ,role_action  WHERE role_action.aid = action.id AND role_action.rid=? ";
	
	
	class ActionVORowMapper implements GenericRowMapper<ActionVO> {
		public ActionVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ActionVO actionVO = new ActionVO();
			int i = 0;
			actionVO.setId(rs.getInt(++i));
			actionVO.setActionName(rs.getString(++i));
			actionVO.setDescription(rs.getString(++i));
			actionVO.setUrl(rs.getString(++i));
			return actionVO;
		}
	}
	
	/**
	 * 根据角色ID查找权限集合
	 * @param roleId 角色ID
	 * @return 权限集合
	 */
	public List<ActionVO> findByRole(int roleId){
		List<ActionVO> actionList = query(SELECT_ACTION_BY_ROLE, new Object[]{roleId}, new ActionVORowMapper());
		return  actionList;
	}
	
	 public static boolean isHqAdminAction(String url){
	    	boolean hqAdminAction = false;
	    	List<ActionVO> actionList = new ActionDao().findByRole(1);//admin角色拥有的角色
	    	for(ActionVO action:actionList){
	    		if(url.equals(action.getUrl())){
	    			hqAdminAction = true;
	    			break;
	    		}
	    	}
	    	return hqAdminAction;
	    }
	
	

}
