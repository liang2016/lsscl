/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.db.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.serotonin.db.spring.GenericRowMapper;
import com.serotonin.mango.Common;
import com.serotonin.mango.rt.dataImage.SetPointSource;
import com.serotonin.mango.rt.event.EventInstance;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.vo.UserComment;
import com.serotonin.mango.vo.permission.DataPointAccess;
import com.serotonin.web.taglib.Functions;
import com.serotonin.mango.vo.power.RoleVO;
import com.serotonin.mango.vo.power.ActionVO;
import com.serotonin.mango.vo.scope.ScopeVO;
import com.serotonin.mango.db.dao.power.RoleDao;
import com.serotonin.mango.db.dao.power.ActionDao;
import com.serotonin.mango.db.dao.scope.ScopeDao;
import com.serotonin.mango.db.dao.power.UserEventLimtDao;

public class UserDao extends BaseDao {

	private static final String USER_SELECT = "select id, username, password, email, phone, admin, disabled, selectedWatchList, homeUrl, lastLogin, "
			+ "  receiveAlarmEmails, receiveOwnAuditEvents " + "from users ";

	public User getUser(int id) {
		User user = queryForObject(USER_SELECT + "where id=?",
				new Object[] { id }, new UserRowMapper(), null);
		populateUserPermissions(user);
		return user;
	}

	public User getUser(String username) {
		User user = queryForObject(USER_SELECT + "where lower(username)=?",
				new Object[] { username.toLowerCase() }, new UserRowMapper(),
				null);
		populateUserPermissions(user);

		return user;
	}

	class UserRowMapper implements GenericRowMapper<User> {
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			int i = 0;
			user.setId(rs.getInt(++i));
			user.setUsername(rs.getString(++i));
			user.setPassword(rs.getString(++i));
			user.setEmail(rs.getString(++i));
			user.setPhone(rs.getString(++i));
			user.setAdmin(charToBool(rs.getString(++i)));
			user.setDisabled(charToBool(rs.getString(++i)));
			user.setSelectedWatchList(rs.getInt(++i));
			user.setHomeUrl(rs.getString(++i));
			user.setLastLogin(rs.getLong(++i));
			user.setReceiveAlarmEmails(rs.getInt(++i));
			user.setReceiveOwnAuditEvents(charToBool(rs.getString(++i)));
			return user;
		}
	}

	class UserRowMapper2 implements GenericRowMapper<User> {
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getInt(1));
			user.setUsername(rs.getString(2));
			user.setAdmin(charToBool(rs.getString(3)));
			ScopeVO homeScope = new ScopeVO();
			homeScope.setId(rs.getInt(4));
			user.setHomeScope(homeScope);
			user.setEventHandlerCount(rs.getInt(5));
			user.setLimit(rs.getInt(6));
			return user;
		}
	}

	public List<User> getUsers() {
		List<User> users = query(USER_SELECT + "order by username",
				new Object[0], new UserRowMapper());
		populateUserPermissions(users);
		return users;
	}

	// 查询用户事件处理器添加限制
	public List<User> getAdminUsers() {
		List<User> users = query(
				"select u.id,u.username,u.admin ,ua.factoryId,ua.eventHandlerCount,ua.limit from users u left join user_scope us on us.uid=u.id left join scope s on s.id=us.scopeid left join userEventhandlers ua on ua.factoryId=us.scopeid where s.scopetype=3 and u.admin='Y' order by u.username",
				new Object[] {}, new UserRowMapper2());
		return users;
	}

	// 根据用户查询admin添加用户的限制
	public List<User> getAdminAddUsers() {
		List<User> users = query(
				"select u.id,u.username,u.admin ,ua.factoryId,ua.insertCount,ua.limit from users u left join user_scope us on us.uid=u.id left join scope s on s.id=us.scopeid left join userAddLimit ua on ua.factoryId=us.scopeid where s.scopetype=3 and u.admin='Y' order by u.username",
				new Object[] {}, new UserRowMapper2());
		return users;
	}

	// 根据范围id查询用户
	public List<User> getUsers(int scopeId) {
		List<User> users = query(
				USER_SELECT
						+ "where id in( select uid from user_scope where scopeid=? and isHomeScope=1) order by username",
				new Object[] { scopeId }, new UserRowMapper());
		populateUserPermissions(users);
		return users;
	}

	/**
	 * 根据范围id查询用户和父用户
	 * 
	 * @param scopeId
	 * @return
	 */
	public static final String getUsersAndParentUsers = "with ScopeTree AS " + "( "
			+ "SELECT id,scopetype,parentid FROM scope " + "WHERE id = ? "
			+ "UNION ALL " + "SELECT s.id,s.scopetype,s.parentid FROM "
			+ "ScopeTree t,Scope s " + "WHERE t.parentId = s.id "
			+ "and s.scopetype in (1,2,3)) "
			+ "select u.id, u.username, u.password, u.email, u.phone, u.admin, u.disabled, u.selectedWatchList, u.homeUrl, u.lastLogin, "
			+ "u.receiveAlarmEmails, u.receiveOwnAuditEvents from users "
			+ " u right join user_scope us on us.uid = u.id "
			+ " right join ScopeTree s on us.scopeid = s.id "
			+ "where isHomeScope = 1 order by username";
	public List<User> getUsersAndParentUsers(int scopeId) {
		List<User> users = query(getUsersAndParentUsers,
				new Object[] { scopeId }, new UserRowMapper());
		populateUserPermissions(users);
		return users;
	}

	// 根据范围id查询用户个数
	public int getUserCount(int scopeId) {
		return ejt
				.queryForInt(
						"select count(id) from users where id in( select uid from user_scope where scopeid=? and isHomeScope=1)",
						new Object[] { scopeId });
	}

	// select user by email
	public List<User> getUserByEmail(String email) {
		List<User> user = query(USER_SELECT + "where email=?",
				new Object[] { email }, new UserRowMapper());
		return user;
	}

	public List<User> getActiveUsers() {
		List<User> users = query(USER_SELECT + "where disabled=?",
				new Object[] { boolToChar(false) }, new UserRowMapper());
		populateUserPermissions(users);
		return users;
	}

	private void populateUserPermissions(List<User> users) {
		for (User user : users)
			populateUserPermissions(user);
	}

	private static final String SELECT_DATA_SOURCE_PERMISSIONS = "select dataSourceId from dataSourceUsers where userId=?";
	private static final String SELECT_DATA_POINT_PERMISSIONS = "select dataPointId, permission from dataPointUsers where userId=?";

	public void populateUserPermissions(User user) {
		if (user == null)
			return;

		user.setDataSourcePermissions(queryForList(
				SELECT_DATA_SOURCE_PERMISSIONS, new Object[] { user.getId() },
				Integer.class));
		user.setDataPointPermissions(query(SELECT_DATA_POINT_PERMISSIONS,
				new Object[] { user.getId() },
				new GenericRowMapper<DataPointAccess>() {
					public DataPointAccess mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						DataPointAccess a = new DataPointAccess();
						a.setDataPointId(rs.getInt(1));
						a.setPermission(rs.getInt(2));
						return a;
					}
				}));
	}

	public void saveUser(final User user) {
		getTransactionTemplate().execute(
				new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(
							TransactionStatus status) {
						if (user.getId() == Common.NEW_ID)
							insertUser(user);
						else
							updateUser(user);
					}
				});
	}

	// 添加用户与范围(删除原有的用户范围关系)
	public void saveUserScope(final int userId, final List<Integer> scopeIds,
			final List<Boolean> isSets) {
		getTransactionTemplate().execute(
				new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(
							TransactionStatus status) {
						ejt.update(
								"delete user_scope where uid=? and isHomeScope=0",
								new Object[] { userId });
						for (int i = 0; i < scopeIds.size(); i++) {
							ejt.update(
									"insert into user_scope (uid,scopeid,isHomeScope,isSet) values(?,?,0,?)",
									new Object[] { userId, scopeIds.get(i),
											boolToChar(isSets.get(i)) });
						}
					}
				});

	}

	// 新加用户添加用户角色
	private void saveUserRole(User user) {
		int userId = user.getId();
		int scopeId = user.getHomeScope().getScopetype();
		String sqlBase = "insert into user_role (uid,rid,date,defaultRole) values";
		int factoryScope = 0;
		int subZone = 0;
		int zone = 0;
		int center = 0;

		int roleId = 0;

		if (scopeId == 0) {// 添加总部
			center = 1;
			roleId = 2;
			if (user.isAdmin()) {
				roleId = 1;
			}
			ejt.update(
			// 总部普通用户权限
					sqlBase + "(?," + roleId + "," + new Date().getTime()
							+ ",?)", new Object[] { userId, center });
			roleId += 2;
			ejt.update(
			// 区域普通用户权限
					sqlBase + "(?," + roleId + "," + new Date().getTime()
							+ ",?)", new Object[] { userId, zone });
			roleId += 2;
			ejt.update(
			// 子区域普通用户权限
					sqlBase + "(?," + roleId + "," + new Date().getTime()
							+ ",?)", new Object[] { userId, subZone });
			roleId += 2;
			ejt.update(sqlBase + "(?," + roleId + "," + new Date().getTime()
					+ ",?)", new Object[] { userId, factoryScope });

		} else if (scopeId == 1) {// 区域
			zone = 1;
			roleId = 4;
			if (user.isAdmin()) {
				roleId = 3;
			}
			ejt.update(sqlBase + "(?," + roleId + "," + new Date().getTime()
					+ ",?)", new Object[] { userId, zone });
			roleId += 2;
			ejt.update(sqlBase + "(?," + roleId + "," + new Date().getTime()
					+ ",?)", new Object[] { userId, subZone });
			roleId += 2;
			ejt.update(sqlBase + "(?," + roleId + "," + new Date().getTime()
					+ ",?)", new Object[] { userId, factoryScope });

		} else if (scopeId == 2) {
			subZone = 1;
			roleId = 6;
			if (user.isAdmin()) {
				roleId = 5;
			}
			ejt.update(sqlBase + "(?," + roleId + "," + new Date().getTime()
					+ ",?)", new Object[] { userId, subZone });
			roleId += 2;
			ejt.update(sqlBase + "(?," + roleId + "," + new Date().getTime()
					+ ",?)", new Object[] { userId, factoryScope });

		}// 添加普通用户
		else {
			factoryScope = 1;
			roleId = 8;
			if (user.isAdmin()) {
				roleId = 7;
				// insertUserAddLimit(userId,10);
			} else {
				addUserCount(user.getHomeScope().getId());
			}
			ejt.update(sqlBase + "(?," + roleId + "," + new Date().getTime()
					+ ",?)", new Object[] { userId, factoryScope });
		}
	}

	/*
	 * 添加用户注册范围
	 */
	private void saveUserHome(int userId, int scopeid) {
		ejt.update(
				"insert into user_scope(uid,scopeid,isHomeScope,isSet) values(?,?,1,'N')",
				new Object[] { userId, scopeid });

	}

	private static final String USER_INSERT = "insert into users ("
			+ "  username, password, email, phone, admin, disabled, receiveAlarmEmails, receiveOwnAuditEvents) "
			+ "values (?,?,?,?,?,?,?,?)";

	void insertUser(User user) {
		int id = doInsert(
				USER_INSERT,
				new Object[] { user.getUsername(), user.getPassword(),
						user.getEmail(), user.getPhone(),
						boolToChar(user.isAdmin()),
						boolToChar(user.isDisabled()),
						user.getReceiveAlarmEmails(),
						boolToChar(user.isReceiveOwnAuditEvents()) },
				new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
						Types.VARCHAR, Types.VARCHAR, Types.VARCHAR,
						Types.INTEGER, Types.VARCHAR });
		user.setId(id);
		// 添加默认注册范围
		saveUserHome(user.getId(), user.getHomeScope().getId());
		// 这里区别总部区域子区域与工厂用户添加的区别
		if (user.getHomeScope().getScopetype() >= 0
				&& user.getHomeScope().getScopetype() <= 3) {
			saveUserRole(user);
		} else {
			saveRelationalData(user);
		}
	}

	private static final String USER_UPDATE = "update users set "
			+ "  username=?, password=?, email=?, phone=?, admin=?, disabled=?, receiveAlarmEmails=?, "
			+ "  receiveOwnAuditEvents=? " + "where id=?";

	void updateUser(User user) {
		ejt.update(
				USER_UPDATE,
				new Object[] { user.getUsername(), user.getPassword(),
						user.getEmail(), user.getPhone(),
						boolToChar(user.isAdmin()),
						boolToChar(user.isDisabled()),
						user.getReceiveAlarmEmails(),
						boolToChar(user.isReceiveOwnAuditEvents()),
						user.getId() });
		saveRelationalData(user);
	}

	private void saveRelationalData(final User user) {
		// Delete existing permissions.
		ejt.update("delete from dataSourceUsers where userId=?",
				new Object[] { user.getId() });
		ejt.update("delete from dataPointUsers where userId=?",
				new Object[] { user.getId() });

		// Save the new ones.
		ejt.batchUpdate(
				"insert into dataSourceUsers (dataSourceId, userId) values (?,?)",
				new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return user.getDataSourcePermissions().size();
					}

					public void setValues(PreparedStatement ps, int i)
							throws SQLException {
						ps.setInt(1, user.getDataSourcePermissions().get(i));
						ps.setInt(2, user.getId());
					}
				});
		ejt.batchUpdate(
				"insert into dataPointUsers (dataPointId, userId, permission) values (?,?,?)",
				new BatchPreparedStatementSetter() {
					public int getBatchSize() {
						return user.getDataPointPermissions().size();
					}

					public void setValues(PreparedStatement ps, int i)
							throws SQLException {
						ps.setInt(1, user.getDataPointPermissions().get(i)
								.getDataPointId());
						ps.setInt(2, user.getId());
						ps.setInt(3, user.getDataPointPermissions().get(i)
								.getPermission());
					}
				});
	}

	public void deleteUser(final int userId) {
		getTransactionTemplate().execute(
				new TransactionCallbackWithoutResult() {
					@SuppressWarnings("synthetic-access")
					@Override
					protected void doInTransactionWithoutResult(
							TransactionStatus status) {
						Object[] args = new Object[] { userId };
						ejt.update(
								"update userComments set userId=null where userId=?",
								args);
						ejt.update(
								"delete from mailingListMembers where userId=?",
								args);
						ejt.update(
								"delete from watchListUsers where watchListId in (select id from watchLists where userId = ?)",
								args);
						ejt.update("delete from watchListUsers where userId=?",
								args);
						ejt.update("delete from watchLists where userId=?",
								args);
						ejt.update(
								"update pointValueAnnotations set sourceId=null where sourceId=? and sourceType="
										+ SetPointSource.Types.USER, args);
						ejt.update("delete from userEvents where userId=?",
								args);
						ejt.update(
								"update events set ackUserId=null, alternateAckSource="
										+ EventInstance.AlternateAcknowledgementSources.DELETED_USER
										+ " where ackUserId=?", args);
						// 删除用户-范围关系
						ejt.update(" delete from user_scope where uid = ? ",
								args);
						// 删除用户--角色关系
						ejt.update(" delete from user_role where uid = ? ",
								args);
						ejt.update("delete from users where id=?", args);
					}
				});
	}

	public void recordLogin(int userId) {
		ejt.update("update users set lastLogin=? where id=?", new Object[] {
				System.currentTimeMillis(), userId });
	}

	public void saveHomeUrl(int userId, String homeUrl) {
		ejt.update("update users set homeUrl=? where id=?", new Object[] {
				homeUrl, userId });
	}

	//
	//
	// User comments
	//
	private static final String USER_COMMENT_INSERT = "insert into userComments (userId, commentType, typeKey, ts, commentText) "
			+ "values (?,?,?,?,?)";

	public void insertUserComment(int typeId, int referenceId,
			UserComment comment) {
		comment.setComment(Functions.truncate(comment.getComment(), 1024));
		ejt.update(USER_COMMENT_INSERT, new Object[] { comment.getUserId(),
				typeId, referenceId, comment.getTs(), comment.getComment() });
	}

	// check the email address is exist or not
	public boolean emailAddrIsExist(int id, String emailAddr) {
		User user = queryForObject(USER_SELECT + "where email=? and id != ?",
				new Object[] { emailAddr, id }, new UserRowMapper(), null);
		populateUserPermissions(user);
		if (user == null)
			return false;
		else
			return true;
	}

	// check the phone is exist or not
	public boolean phoneIsExist(int id, String phone) {
		int count = queryForObject(
				"select count(id) from users where phone=? and id != ?",
				new Object[] { phone, id }, Integer.class, 0);
		if (count == 0)
			return false;
		else
			return true;
	}

	/**
	 * 把用户角色，当前角色，默认角色添加到用户里。
	 * 
	 * @param user
	 */
	public static void addUserRole(User user) {
		// 获取当前用户的角色集合
		user.setRoleList(new RoleDao().findByUser(user.getId()));
		// 获取当前用户所属范围ID
		user.setHomeScope(new ScopeDao().getScopeByUser(user.getId()));
		// 获取用户当前的角色ID(登陆时默认为最高权限的角色ID)
		user.setCurrentRole(new RoleDao().getDefaultRole(user.getRoleList()));
		// 获取用户默认角色
		user.setDefaultRole(new RoleDao().getDefaultRole(user.getRoleList()));
		// 获取用户当前所在范围ID(登陆时默认为注册所在范围ID)
		user.setCurrentScope(user.getHomeScope());
		// 获取用户当前角色的权限集合
		user.setCurrentRoleActionList(new ActionDao().findByRole(user
				.getCurrentRole().getId()));
		// 获取有当前用户有权限访问的自范围集合
		user.setChildScopeList(new ScopeDao().getScopesByUser(user.getId()));
	}

	/**
	 * 转换用户角色权限信息
	 * 
	 * @param scopeType
	 *            范围类型
	 * @param scopeId
	 *            范围ID
	 * @param user
	 *            为转换前的用户信息
	 */
	public static void changeRole(int scopeType, int scopeId, User user) {
		// 转换当前范围
		ScopeDao scopeDao = new ScopeDao();
		if (scopeType == ScopeVO.ScopeTypes.HQ) {
			user.setCurrentScope(scopeDao.findHQ());
		} else if (scopeType == ScopeVO.ScopeTypes.ZONE
				|| scopeType == ScopeVO.ScopeTypes.SUBZONE) {
			user.setCurrentScope(scopeDao.findZoneOrSubZoneById(scopeId));
		} else if (scopeType == ScopeVO.ScopeTypes.FACTORY) {
			user.setCurrentScope(scopeDao.findFactoryById(scopeId));
		}
		List<RoleVO> roleList = user.getRoleList();
		for (int i = 0; i < roleList.size(); i++) {
			RoleVO currentRole = roleList.get(i);
			if (currentRole.getScopeType() == scopeType) {
				// 转换当前角色
				user.setCurrentRole(currentRole);
				// 转换当前权限
				if (user.isTempAdmin())
					user.setCurrentRoleActionList(new ActionDao()
							.findByRole(currentRole.getId() - 1));
				else {
					user.setCurrentRoleActionList(new ActionDao()
							.findByRole(currentRole.getId()));
				}
				break;
			}
		}
	}

	/**
	 * 验证是否有权限查看List中所有的ScopeVO，只让该用户查看有权限查看的
	 * 
	 * @param scopeList
	 *            被验证的集合
	 * @param user
	 *            当前用户
	 */
	public static void validateScopes(List<ScopeVO> scopeList, User user) {
		if (scopeList == null) {
			return;
		}
		if (user.isAdmin())
			return;
		// 获取有当前用户有权限访问的自范围集合
		List<ScopeVO> childScopeList = user.getChildScopeList();
		if (childScopeList == null || childScopeList.size() == 0) {
			scopeList.clear();
			return;
		}
		ScopeDao scopeDao = new ScopeDao();
		// 获取用户注册范围
		ScopeVO homeScopeVO = user.getHomeScope();
		List<ScopeVO> deleteScopeVOs = new ArrayList<ScopeVO>(); // 存放要删除的行的下标
		for (int i = 0; i < scopeList.size(); i++) {// 循环验证集合中每个范围是否有权限
			boolean needDelete = true; // 标识下列循环中当前行是否没有权限访问需要从列表中删除
			// 获取当前范围，上级范围，上上级范围
			ScopeVO tempScope = scopeList.get(i);
			ScopeVO parentScope = null;
			ScopeVO grandParent = null;
			if (tempScope.getScopetype().equals(ScopeVO.ScopeTypes.ZONE)) {
				parentScope = scopeDao.findHQ();
				grandParent = null;
			} else if (tempScope.getScopetype().equals(
					ScopeVO.ScopeTypes.SUBZONE)) {
				parentScope = scopeDao.findZoneOrSubZoneById(tempScope
						.getParentScope().getId());
				grandParent = scopeDao.findHQ();
			} else if (tempScope.getScopetype().equals(
					ScopeVO.ScopeTypes.FACTORY)) {
				parentScope = scopeDao.findZoneOrSubZoneById(tempScope
						.getParentScope().getId());
				grandParent = scopeDao.findZoneOrSubZoneById(parentScope
						.getParentScope().getId());
			}
			for (int j = 0; j < childScopeList.size(); j++) {// 遍历所有拥有权限的范围，看当前范围是否被包含
				ScopeVO childScope = childScopeList.get(j);
				if (tempScope.getId().equals(childScope.getId())) {// 在子范围权限
																	// 内，则不删除
					needDelete = false;
					break;
				}
				if (parentScope != null) {
					if (parentScope.getId().equals(childScope.getId())) {// 在子子范围权限内，则不删除
						needDelete = false;
						break;
					}
				}
				if (grandParent != null) {
					if (grandParent.getId().equals(childScope.getId())) {// 在子子范围权限内，则不删除
						needDelete = false;
						break;
					}
				}
			}
			if (needDelete) {
				deleteScopeVOs.add(tempScope);
			}
		}
		// 删除没有权限的行
		for (ScopeVO vo : deleteScopeVOs) {
			scopeList.remove(vo);
		}
	}

	class UserRowMapperForLimit implements GenericRowMapper<User> {
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setId(rs.getInt(1));
			user.setUsername(rs.getString(2));
			user.setAdmin(charToBool(rs.getString(3)));
			ScopeVO homeScope = new ScopeVO();
			homeScope.setId(rs.getInt(4));
			homeScope.setScopetype(rs.getInt(5));
			user.setHomeScope(homeScope);
			return user;
		}
	}

	// 查询一个用户的scope admin?
	public User getUserForLimt(int id) {
		User user = queryForObject(
				"select u.id,u.username,u.admin,s.id,s.scopetype from users "
						+ "u left join user_scope us on us.uid=u.id left join scope s on us.scopeid=s.id where us.isHomeScope=1  and u.id=?",
				new Object[] { id }, new UserRowMapperForLimit(), null);
		return user;
	}

	private static final String UPDATE_USER_LIMIT = "update userAddLimit set limit=? where factoryId=?";
	private static final String INSERT_USER_ADD_LIMIT = "insert into userAddLimit(factoryId,insertCount,limit) values(?,?,?)";
	private static final String UPDATE_USER_COUNT = "update  userAddLimit set insertCount=insertCount+1 where factoryId=?";
	private static final String DELETE_USER_COUNT = "update  userAddLimit set insertCount=insertCount-1 where factoryId=?";
	private static final String SEARCH_USER_LIMIT = "select limit-insertCount from userAddLimit where factoryId=?";

	public void updateUserAddLimit(int limit, int factoryId) {
		ejt.update(UPDATE_USER_LIMIT, new Object[] { limit, factoryId });
	}

	public void insertUserAddLimit(int factoryId, int limit) {
		ejt.update(INSERT_USER_ADD_LIMIT, new Object[] { factoryId, 0, limit });
	}

	public void addUserCount(int factoryId) {
		ejt.update(UPDATE_USER_COUNT, new Object[] { factoryId });
	}

	// 删除一个Count
	public void deleteCount(int factoryId) {
		ejt.update(DELETE_USER_COUNT, new Object[] { factoryId });
	}

	public int getUserLimit(int factoryId) {
		int a = ejt.queryForInt(SEARCH_USER_LIMIT, new Object[] { factoryId },
				-1);
		return a;
	}

	public void deleteUserLimit(int factoryId) {
		ejt.update("delete userAddLimit where factoryId=?",
				new Object[] { factoryId });
	}
}
