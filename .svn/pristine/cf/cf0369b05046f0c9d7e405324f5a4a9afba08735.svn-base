package com.serotonin.mango.db.dao.scope;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.serotonin.db.spring.GenericRowMapper;
import com.serotonin.mango.db.dao.BaseDao;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.vo.WatchList;
import com.serotonin.mango.vo.scope.ScopeVO;
import com.serotonin.mango.web.mvc.form.ScopeForm;
import java.sql.ResultSet;
import java.util.Date;
import java.sql.SQLException;
import com.serotonin.mango.db.dao.UserDao;
import com.serotonin.mango.vo.scope.TradeVO;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

public class ScopeDao extends BaseDao {

	public static final String SELECT_BASE_HEADER = " select s.id ,s.scopename,s.address ,s.lon , s.lat,s.enlargenum ,s.description,s.scopetype,s.parentid,s.logo,us.uid,u.username,us.isHomeScope ";

	public static final String SELECT_BASE = SELECT_BASE_HEADER
			+ "  from scope s left join user_scope us on us.scopeid =s.id left join users u on u.id = us.uid where u.admin='Y'  and s.scopetype = ? and us.isHomeScope = 1 ";

	/**
	 * 查询区域语句
	 */
	public static final String SELECT_ZONE = SELECT_BASE;
	/**
	 * 根据区域查找子区域
	 */
	public static final String SELECT_SUBZONE_BY_ZONE = SELECT_BASE
			+ "  and s.parentid = ?  ";

	/**
	 * 查询所有子区域列表
	 */
	public static final String SELECT_SUBZONE = SELECT_BASE;

	/**
	 * 查询工厂语句头,结合用户查询
	 */
	public static final String SELECT_FACTORY_BASE = " select s.id ,s.code,s.scopename,s.address,s.disabled,s.lon , s.lat,s.enlargenum ,s.description,s.code,s.scopetype,s.logo,u.id as uid,u.username,s.tradeid,t.tradename, "
			+ " t.description as tradedescription,s.parentid as pid ,p.scopename as pscopename,gp.id as gpid,gp.scopename as gpscopename,us.isHomeScope "
			+ " from scope s "
			+ " left join user_scope us		on s.id			= us.scopeid "
			+ " left join users		 u		on us.uid		= u.id "
			+ " left join trade		 t		on s.tradeid	= t.id "
			+ " left join scope		 p		on s.parentid	= p.id "
			+ " left join scope		 gp		on p.parentid	= gp.id "
			+ " where s.scopetype = 3 "
			+ " and   u.admin	    = 'Y' "
			+ " and   us.isHomeScope = 1 ";
	/**
	 * 查询一个范围,不根据用户,
	 */
	public static final String SELECT_SCOPE=" select  s.id ,  s.scopename,s.address,s.disabled,s.lon ,s.lat,s.enlargenum ,s.description,s.scopetype, " +
			"s.parentid as pid ,p.scopename as pscopename," +
			"gp.id as gpid,gp.scopename as gpscopename  from scope s left join scope p on s.parentid= p.id  left join scope		 gp		on p.parentid	= gp.id  where s.scopetype = 3  ";
	/**
	 * 根据子区域查找工厂语句
	 */
	public static final String SELECT_FACTORY_BY_SUBZONE = SELECT_FACTORY_BASE
			+ " and p.id = ? ";
	/**
	 * 根据区域查找工厂语句
	 */
	public static final String SELECT_FACTORY_BY_ZONE = SELECT_FACTORY_BASE
			+ " and	  gp.id = ? ";
	/**
	 * 根据总部查找工厂语句
	 */
	public static final String SELECT_FACTORY_BY_HQ = SELECT_FACTORY_BASE;
	/**
	 * 插入语句
	 */
	public static final String INSERT_BASE = " insert into scope(scopename,address,lon,lat,enlargenum,description,code,parentid,scopetype,tradeid,logo) values (?,?,?,?,?,?,?,?,?,?,'images/mangoLogoMed.png') ";
	/**
	 * 更新语句
	 */
	public static final String UPDATE_BASE = " update scope set scopename = ?,address = ?, disabled=?,lon = ? ,lat = ?,enlargenum = ?,description = ? ,code=?,scopetype = ?,tradeid = ? where id = ? ";
	/**
	 * 根据ID查找类型
	 */
	public static final String SELECT_TYPE_BY_ID = " select scopetype from scope where id = ? ";
	/**
	 * 更新所属上级
	 */
	public static final String CHANGE_PARENT = " UPDATE SCOPE SET PARENTID = ? WHERE ID = ? ";
	/**
	 * 根据ID查找工厂语句
	 */
	public static final String SELECT_FACTORY_BY_ID = SELECT_FACTORY_BASE
			+ " and   s.id = ? ";
	/**
	 * 根据ID查找[子]区域语句
	 */
	public static final String SELECT_ZONEORSUBZONE_BY_ID = SELECT_BASE_HEADER
			+ " from scope s "
			+ " left join user_scope us		on s.id			= us.scopeid "
			+ " left join users		 u		on us.uid		= u.id  "
			+ " where s.scopetype in(?,?)  " + " and   u.admin	  = 'Y' "
			+ " and   s.id = ? " + " and   us.isHomeScope = 1 ";
	/**
	 * 删除语句
	 */
	public static final String DELETE_BASE = " delete from scope where id= ?  ";
	/**
	 * 获取用户注册所在区域的编号
	 */
	public static final String USER_SCOPE_SELECT = " SELECT scopeid FROM user_scope WHERE uid = ? ";

	/**
	 * 根据范围ID查找该范围下所有用户的ID
	 */
	public static final String SELECT_USERID_BY_SCOPE = " select uid from user_scope where scopeid = ? and isHomeScope=1";

	/**
	 * 根据用户ID查找注册范围信息
	 */
	public static final String SELECT_SCOPE_BY_USER = SELECT_BASE_HEADER
			+ "  from scope s left join user_scope us on us.scopeid =s.id left join users u on u.id = us.uid where u.id = ? and us.isHomeScope = 1 ";
	/**
	 * 根据用户查找不是自己的注册范围但是自己的管辖范围
	 */
	public static final String SELECT_CHILDSCOPE_BY_USER = SELECT_BASE_HEADER
			+ "  from scope s left join user_scope us on us.scopeid =s.id left join users u on u.id = us.uid where u.id = ? and us.isHomeScope != 1  ";

	/**
	 * 查询此ID的范围是否是彼ID范围的子范围
	 */
	public static final String IS_MY_CHILD = " select count(*) from scope factory "
			+ " left join scope subzone	on factory.parentid	= subzone.id "
			+ " left join scope zone		on subzone.parentid	= zone.id "
			+ " left join scope hq			on zone.parentid = hq.id "
			+ " where  (subzone.id = ? or zone.id = ? or hq.id = ?) and factory.id =? ";

	/**
	 * 获取区域集合
	 * 
	 * @return 区域集合
	 */
	public List<ScopeVO> getZoneList() {
		List<ScopeVO> zoneList = query(SELECT_ZONE,
				new Object[] { ScopeVO.ScopeTypes.ZONE }, new ZoneRowMapper());
		return zoneList;
	}

	/**
	 * 获取区域集合
	 * 
	 * @return 区域集合
	 */
	public List<ScopeVO> getZoneListByPage(int pageNo,int pageSize) {
		int rowno = 0;
		rowno = (pageNo-1)*pageSize;
		
		String SELECT_PAGE = "select top " +pageSize+" id ,scopename,address ,lon , lat,enlargenum ,description,scopetype,parentid,logo,uid,username,isHomeScope from (select row_number()over(order by tempcolumn)temprownumber,* from ( " 
				+ " select tempcolumn=0,s.id ,s.scopename,s.address ,s.lon , s.lat,s.enlargenum ,s.description,s.scopetype,s.parentid,s.logo,us.uid,u.username,us.isHomeScope "
				+ " from scope s left join user_scope us on us.scopeid =s.id left join users u on u.id = us.uid where u.admin='Y'  and s.scopetype = ? and us.isHomeScope = 1 " 
				+ " )t)tt where temprownumber> " + rowno;
		List<ScopeVO> zonelistmp = query(SELECT_PAGE,
				new Object[] { ScopeVO.ScopeTypes.ZONE}, new ZoneRowMapper());
		List<ScopeVO> zoneList = getResultList(ScopeVO.ScopeTypes.ZONE,zonelistmp);
		return zoneList;
	}	

	/**
	 * 获取含有分级的结果集合
	 * 
	 * @param 需要转换的集合
	 *            
	 * @return 含有分级的结果集合
	 */
	private List<ScopeVO> getResultList(int type,List<ScopeVO> zonelistmp){
		List<ScopeVO> zoneList = new ArrayList<ScopeVO>();
		for (ScopeVO scope : zonelistmp) {
			Map<String, Integer> map = new HashMap<String, Integer>();
			map = getEventCountByScope(type,scope.getId());//1表示区域

			if(scope.isDisabled())
				continue;
			for (int j = 0; j < 3; j++) {
				scope.setWarnCount(map.get("yellow"));
				scope.setWarnUnderThreeDays(map.get("orange"));
				scope.setWarnUnderSevenDays(map.get("red"));
			}
			zoneList.add(scope);
		}

		return zoneList;
	}
	
	/**
	 * 获取某个区域下的所有子区域集合
	 * 
	 * @param zoneId
	 *            区域ID
	 * @return 子区域集合
	 */
	public List<ScopeVO> getsubZoneList(int zoneId) {
		List<ScopeVO> subZoneList = query(SELECT_SUBZONE_BY_ZONE, new Object[] {
				ScopeVO.ScopeTypes.SUBZONE, zoneId }, new ZoneRowMapper());
		return subZoneList;
	}

	/**
	 * 获取某个区域下的所有子区域集合
	 * 
	 * @param zoneId
	 *            区域ID
	 * @return 子区域集合
	 */
	public List<ScopeVO> getsubZonePageList(int zoneId,int pageNo,int pageSize) {
		int rowno = 0;
		rowno = (pageNo-1)*pageSize;
		
		String SELECT_PAGE = "select top " +pageSize+" id ,scopename,address ,lon , lat,enlargenum ,description,scopetype,parentid,logo,uid,username,isHomeScope from (select row_number()over(order by tempcolumn)temprownumber,* from ( " 
				+ " select tempcolumn=0,s.id ,s.scopename,s.address ,s.lon , s.lat,s.enlargenum ,s.description,s.scopetype,s.parentid,s.logo,us.uid,u.username,us.isHomeScope "
				+ " from scope s left join user_scope us on us.scopeid =s.id left join users u on u.id = us.uid where u.admin='Y'  and s.scopetype = ? and us.isHomeScope = 1  and s.parentid = ? " 
				+ " )t)tt where temprownumber> " + rowno;
		List<ScopeVO> zonelistmp = query(SELECT_PAGE,
				new Object[] { ScopeVO.ScopeTypes.SUBZONE, zoneId}, new ZoneRowMapper());
		List<ScopeVO> subZoneList = getResultList(ScopeVO.ScopeTypes.SUBZONE,zonelistmp);
		return subZoneList;
	}
	
	/**
	 * 获取所有子区域
	 * 
	 * @return 子区域列表
	 */
	public List<ScopeVO> getsubZoneList() {
		List<ScopeVO> subZoneList = query(SELECT_SUBZONE,
				new Object[] { ScopeVO.ScopeTypes.SUBZONE },
				new ZoneRowMapper());
		return subZoneList;
	}

	/**
	 * 根据子区域获取工厂集合
	 * 
	 * @param subZoneId
	 *            子区域ID
	 * @return 工厂集合
	 */
	public List<ScopeVO> getFactoryBySubZone(int subZoneId) {
		List<ScopeVO> factoryList = query(SELECT_FACTORY_BY_SUBZONE,
				new Object[] { subZoneId }, new FactoryRowMapper());
		return factoryList;
	}

	/**
	 * 根据区域获取工厂集合
	 * 
	 * @param zoneId
	 *            区域ID
	 * @return 工厂集合
	 */
	public List<ScopeVO> getFactoryByZone(int zoneId) {
		List<ScopeVO> factoryList = query(SELECT_FACTORY_BY_ZONE,
				new Object[] { zoneId }, new FactoryRowMapper());
		return factoryList;
	}

	/**
	 * 根据区域获取工厂集合
	 * 
	 * @param zoneId
	 *            区域ID
	 * @return 工厂集合
	 */
	public List<ScopeVO> getFactoryPageByZone(int zoneId,int pageNo,int pageSize) {
		int rowno = 0;
		rowno = (pageNo-1)*pageSize;
		
		String SELECT_PAGE = "select top " +pageSize+"" + "id ,code,scopename,address,disabled,lon , lat,enlargenum ,description,code,scopetype,logo,uid,username,tradeid,tradename, "
				+ " tradedescription,pid ,pscopename,gpid,gpscopename,isHomeScope "
				+ " from (select row_number()over(order by tempcolumn)temprownumber,* from ( " 
				+ " select tempcolumn=0, s.id ,s.scopename,s.address,s.disabled,s.lon , s.lat,s.enlargenum ,s.description,s.code,s.scopetype,s.logo,u.id as uid,u.username,s.tradeid,t.tradename, "
				+ " t.description as tradedescription,s.parentid as pid ,p.scopename as pscopename,gp.id as gpid,gp.scopename as gpscopename,us.isHomeScope "
				+ " from scope s "
				+ " left join user_scope us		on s.id			= us.scopeid "
				+ " left join users		 u		on us.uid		= u.id "
				+ " left join trade		 t		on s.tradeid	= t.id "
				+ " left join scope		 p		on s.parentid	= p.id "
				+ " left join scope		 gp		on p.parentid	= gp.id "
				+ " where s.scopetype = 3 "
				+ " and   u.admin	    = 'Y' "
				+ " and   us.isHomeScope = 1  and p.id = ? "
				+ " )t)tt where temprownumber> " + rowno;
		List<ScopeVO> zonelistmp = query(SELECT_PAGE,
				new Object[] { zoneId}, new FactoryRowMapper());
		List<ScopeVO> factoryList = getResultList(ScopeVO.ScopeTypes.FACTORY,zonelistmp);
		return factoryList;
	}
	
	/**
	 * 获取全部工厂集合
	 * 
	 * @return 全部工厂集合
	 */
	public List<ScopeVO> getFactoryByHq() {
		List<ScopeVO> factoryList = query(SELECT_FACTORY_BY_HQ, new Object[0],
				new FactoryRowMapper());
		return factoryList;
	}

	/**
	 * 查询工厂
	 * 
	 * @param zoneId
	 * @param subZoneId
	 * @param tradeId
	 * @param factoryName
	 * @return
	 */
	public List<ScopeVO> searchFactory(User user,int zoneId, int subZoneId, int tradeId,String code,
			String factoryName) {
		List<ScopeVO> result = null;
		// String search_base = " select temp2.*,p2.id as gpid,p2.scopename as
		// gpscopename from ( select temp.*, p.id as pid , p.scopename as
		// pscopename ,p.parentid as gparentid from ( select s.id
		// ,s.scopename,s.address ,s.lon , s.lat,s.enlargenum ,s.description,
		// s.parentid, u.id as
		// uid,u.username,s.tradeid,t.tradename,t.description as
		// tradedescription from scope s,users u, user_scope us ,trade t where
		// s.id = us.scopeid and u.id = us.uid and s.tradeid = t.id and
		// s.scopetype = 3 ";
		String sql = "";
		if (zoneId == -1) {// 没有选择区域
			if (tradeId == -1) {// 没有选择行业
				if (factoryName.equals("")) {// 没有选择工厂名称
					// 无条件
					sql = SELECT_FACTORY_BASE;
					if(!code.equals("")){
						sql+=" and s.code='"+code+"'";
					}
					// + " ) temp , scope p where temp.parentid = p.id ) temp2 ,
					// scope p2 where temp2.gparentid = p2.id ";
					result = query(sql, new Object[0], new FactoryRowMapper());
				} else {// 选择了工厂名称
					// 工厂名称
					sql = SELECT_FACTORY_BASE + "  and s.scopename like '%"
							+ factoryName + "%'";
					if(!code.equals("")){
						sql+=" and s.code='"+code+"'";
					}
					result = query(sql, new Object[0], new FactoryRowMapper());
				}
			} else {// 选择了行业
				if (factoryName.equals("")) {
					// 行业
					sql = SELECT_FACTORY_BASE + "  and s.tradeid = ? ";
					if(!code.equals("")){
						sql+=" and s.code='"+code+"'";
					}
					result = query(sql, new Object[] { tradeId },
							new FactoryRowMapper());
				} else {
					// 行业+工厂名称
					sql = SELECT_FACTORY_BASE
							+ "  and s.tradeid = ? and  s.scopename like '%"
							+ factoryName + "%' ";
					if(!code.equals("")){
						sql+=" and s.code='"+code+"'";
					}
					result = query(sql, new Object[] { tradeId },
							new FactoryRowMapper());
				}
			}
		} else {// 选择了区域
			if (subZoneId == -1) { // 没有选择子区域
				if (tradeId == -1) {// 没有选择行业
					if (factoryName.equals("")) {
						// 区域
						sql = SELECT_FACTORY_BASE + " and gp.id = ?  ";
						if(!code.equals("")){
							sql+=" and s.code='"+code+"'";
						}
						result = query(sql, new Object[] { zoneId },
								new FactoryRowMapper());
					} else {
						// 区域+工厂名字
						sql = SELECT_FACTORY_BASE + "  and s.scopename like '%"
								+ factoryName + "%'  and gp.id = ?   ";
						if(!code.equals("")){
							sql+=" and s.code='"+code+"'";
						}
						result = query(sql, new Object[] { zoneId },
								new FactoryRowMapper());
					}
				} else {// 选择了行业
					if (factoryName.equals("")) {
						// 区域+行业
						sql = SELECT_FACTORY_BASE
								+ "  and s.tradeid = ? and gp.id = ?  ";
						if(!code.equals("")){
							sql+=" and s.code='"+code+"'";
						}
						result = query(sql, new Object[] { tradeId, zoneId },
								new FactoryRowMapper());
					} else {
						// 区域+行业+工厂名字
						sql = SELECT_FACTORY_BASE
								+ "  and s.tradeid = ? and s.scopename like '%"
								+ factoryName + "%' and gp.id = ?  ";
						if(!code.equals("")){
							sql+=" and s.code='"+code+"'";
						}
						result = query(sql, new Object[] { tradeId, zoneId },
								new FactoryRowMapper());
					}
				}
			} else {// 选择了子区域
				if (tradeId == -1) {// 没有选择行业
					if (factoryName.equals("")) {
						// 区域+子区域
						sql = SELECT_FACTORY_BASE
								+ "   and p.id = ? and  gp.id = ? ";
						if(!code.equals("")){
							sql+=" and s.code='"+code+"'";
						}
						result = query(sql, new Object[] { subZoneId, zoneId },
								new FactoryRowMapper());
					} else {
						// 区域+子区域+工厂名字
						sql = SELECT_FACTORY_BASE + "  and s.scopename like '%"
								+ factoryName
								+ "%'  and p.id = ? and gp.id = ? ";
						if(!code.equals("")){
							sql+=" and s.code='"+code+"'";
						}
						result = query(sql, new Object[] { subZoneId, zoneId },
								new FactoryRowMapper());
					}
				} else {// 选择了行业
					if (factoryName.equals("")) {
						// 区域+子区域+行业
						sql = SELECT_FACTORY_BASE
								+ "  and s.tradeid = ? and p.id = ?  and gp.id = ? ";
						if(!code.equals("")){
							sql+=" and s.code='"+code+"'";
						}
						result = query(sql, new Object[] { tradeId, subZoneId,
								zoneId }, new FactoryRowMapper());
					} else {
						// 区域+子区域+行业+工厂名字
						sql = SELECT_FACTORY_BASE
								+ "  and s.tradeid = ? and s.scopename like '%"
								+ factoryName
								+ "%' and p.id = ? and gp.id = ? ";
						if(!code.equals("")){
							sql+=" and s.code='"+code+"'";
						}
						result = query(sql, new Object[] { tradeId, subZoneId,
								zoneId }, new FactoryRowMapper());
					}
				}
			}
		}
		if(result==null&&result.size()==0){
			return null;
		}
		UserDao.validateScopes(result,user);
		return result;
	}

	/**
	 * 保存数据
	 * 
	 * @param scopeVO
	 */
	public int save(ScopeVO scopeVO) {
		return doInsert(INSERT_BASE, new Object[] {
				scopeVO.getScopename(),
				scopeVO.getAddress(),
				scopeVO.getLon(),
				scopeVO.getLat(),
				scopeVO.getEnlargenum(),
				scopeVO.getDescription(),
				(scopeVO.getCode() == null ? null : scopeVO.getCode()),
				scopeVO.getParentScope().getId(),
				scopeVO.getScopetype(),
				(scopeVO.getTradeVO() == null ? null : scopeVO.getTradeVO()
						.getId()) });
	}

	/**
	 * 保存新工厂
	 * 
	 * @param scopeVO
	 */
	public int saveFactory(ScopeForm factory) {
		return doInsert(INSERT_BASE, new Object[] { factory.getScopename(),
				factory.getAddress(), factory.getLon(), factory.getLat(),
				factory.getEnlargenum(), factory.getDescription(),
				factory.getParentId(), factory.getScopetype(),
				factory.getTradeId() });
	}

	/**
	 * 更新数据
	 * 
	 * @param scopeVO
	 * @return
	 */
	public int update(ScopeVO scopeVO) {
		return ejt.update(UPDATE_BASE, new Object[] {
				scopeVO.getScopename(),
				scopeVO.getAddress(),
				boolToChar(scopeVO.isDisabled()),
				scopeVO.getLon(),
				scopeVO.getLat(),
				scopeVO.getEnlargenum(),
				scopeVO.getDescription(),
				(scopeVO.getCode() == null ? null : scopeVO.getCode()),
				scopeVO.getScopetype(),
				(scopeVO.getTradeVO() == null ? null : scopeVO.getTradeVO()
						.getId()), scopeVO.getId() });
	}

//	/**
//	 * 更新一个工厂
//	 * 
//	 * @param scopeVO
//	 * @return
//	 */
//	public int updateFactory(ScopeForm factory) {
//		return ejt
//				.update(UPDATE_BASE, new Object[] { factory.getScopename(),
//						factory.getAddress(), factory.getLon(),
//						factory.getLat(), factory.getEnlargenum(),
//						factory.getDescription(), factory.getScopetype(),
//						factory.getTradeId(), factory.getId() });
//	}

	/**
	 * 删除数据根据ID
	 * 
	 * @param id
	 * @return
	 */
	public int delete(int id) {
		return ejt.update(DELETE_BASE, new Object[] { id });
	}

	/**
	 * 根据工厂ID查找
	 * 
	 * @param factoryId
	 *            工厂ID
	 * @return 工厂信息
	 */
	public ScopeVO findFactoryById(int factoryId) {
		List<ScopeVO> result = query(SELECT_FACTORY_BY_ID,
				new Object[] { factoryId, }, new FactoryRowMapper());
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	public ScopeVO findFactoryByDataSourceId(int dataSouceId) {
		List<ScopeVO> result = query(SELECT_FACTORY_BASE +"and s.id=(select factoryId from dataSources where id =?)",
				new Object[] { dataSouceId, }, new FactoryRowMapper());
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}
	/**
	 * 根据ID查找区域[子区域]
	 * 
	 * @param zoneOrSubZoneId
	 *            区域[子区域]
	 * @return 区域[子区域]信息
	 */
	public ScopeVO findZoneOrSubZoneById(int zoneOrSubZoneId) {
		List<ScopeVO> result = query(SELECT_ZONEORSUBZONE_BY_ID, new Object[] {
				ScopeVO.ScopeTypes.SUBZONE, ScopeVO.ScopeTypes.ZONE,
				zoneOrSubZoneId }, new ZoneRowMapper());
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 查找总部信息
	 * 
	 * @return 总部信息
	 */
	public ScopeVO findHQ() {
		List<ScopeVO> result = query(SELECT_BASE,
				new Object[] { ScopeVO.ScopeTypes.HQ }, new ZoneRowMapper());
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 根据ID查找类型
	 * 
	 * @param id
	 *            编号
	 * @return 类型
	 */
	public int getTypeById(int id) {
		return ejt.queryForInt(SELECT_TYPE_BY_ID, new Object[] { id }, -1);
	}

	/**
	 * 重新指定上级
	 * 
	 * @param scopeId
	 *            当前范围ID
	 * @param newParentId
	 *            新的上级ID
	 * @return 更新行数
	 */
	public int changeParentScope(int scopeId, int newParentId) {
		int count = ejt.update(CHANGE_PARENT, new Object[] { newParentId,
				scopeId });
		return count;
	}

	/**
	 * 获取用户注册所在区域的编号
	 * 
	 * @return 区域编号
	 */
	public int getScopeIdByUser(int userId) {
		Integer id = queryForObject(USER_SCOPE_SELECT, new Object[] { userId },
				Integer.class, 0);
		return id;
	}

	/**
	 * 根据用户ID查找注册范围信息
	 * 
	 * @param userId
	 *            用户ID
	 * @return 范围信息
	 */
	public ScopeVO getScopeByUser(int userId) {
		List<ScopeVO> result = query(SELECT_SCOPE_BY_USER,
				new Object[] { userId }, new ZoneRowMapper());
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 根据用户查找有权限访问的子范围集合
	 * 
	 * @param userId
	 *            用户ID
	 * @return 子范围集合
	 */
	public List<ScopeVO> getScopesByUser(int userId) {
		return query(SELECT_CHILDSCOPE_BY_USER, new Object[] { userId },
				new ZoneRowMapper());
	}

	/**
	 * 获取某个范围的所有用户ID
	 * 
	 * @param scopeId
	 *            范围ID
	 * @return 用户ID数组
	 */
	public List<Integer> getUserByScope(int scopeId) {
		List<Integer> userIdList = query(SELECT_USERID_BY_SCOPE,
				new Object[] { scopeId }, new UserIds());
		return userIdList;
	}

	/**
	 * 获取用户ID
	 * 
	 * @author 王金阳
	 * 
	 */
	class UserIds implements GenericRowMapper<Integer> {
		public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
			Integer userId = rs.getInt(1);
			return userId;
		}
	}

	/**
	 * 区域子区域查询结果
	 * 
	 * @author 王金阳
	 * 
	 */
	class ZoneRowMapper implements GenericRowMapper<ScopeVO> {
		public ScopeVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ScopeVO scopeVO = new ScopeVO();
			int i = 1;
			scopeVO.setId(rs.getInt(i++));
			scopeVO.setScopename(rs.getString(i++));
			scopeVO.setAddress(rs.getString(i++));
			scopeVO.setLon(rs.getDouble(i++));
			scopeVO.setLat(rs.getDouble(i++));
			scopeVO.setEnlargenum(rs.getInt(i++));
			scopeVO.setDescription(rs.getString(i++));
			scopeVO.setScopetype(rs.getInt(i++));
			ScopeVO parentScope = new ScopeVO();
			parentScope.setId(rs.getInt(i++));
			scopeVO.setParentScope(parentScope);
			scopeVO.setBackgroundFilename(rs.getString(i++));
			User scopeMaster = new User();
			scopeMaster.setId(rs.getInt(i++));
			scopeMaster.setUsername(rs.getString(i++));
			scopeVO.setScopeUser(scopeMaster);
			scopeVO.setHomeScope(rs.getInt(i++) == 1 ? true : false);
			return scopeVO;
		}
	}

	/**
	 * 工厂查询结果(包含上级子区域，区域信息，以及行业信息)
	 * 
	 * @author 王金阳
	 * 
	 */
	class FactoryRowMapper implements GenericRowMapper<ScopeVO> {
		public ScopeVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ScopeVO scopeVO = new ScopeVO();
			int i = 1;
			scopeVO.setId(rs.getInt(i++));
			scopeVO.setCode(rs.getString(i++));
			scopeVO.setScopename(rs.getString(i++));
			scopeVO.setAddress(rs.getString(i++));
			scopeVO.setDisabled(charToBool(rs.getString(i++)));
			scopeVO.setLon(rs.getDouble(i++));
			scopeVO.setLat(rs.getDouble(i++));
			scopeVO.setEnlargenum(rs.getInt(i++));
			scopeVO.setDescription(rs.getString(i++));
			scopeVO.setCode(rs.getString(i++));
			scopeVO.setScopetype(rs.getInt(i++));
			scopeVO.setBackgroundFilename(rs.getString(i++));
			User scopeMaster = new User();
			scopeMaster.setId(rs.getInt(i++));
			scopeMaster.setUsername(rs.getString(i++));
			scopeVO.setScopeUser(scopeMaster);

			TradeVO tradeVO = new TradeVO();
			tradeVO.setId(rs.getInt(i++));
			tradeVO.setTradename(rs.getString(i++));
			tradeVO.setDescription(rs.getString(i++));
			scopeVO.setTradeVO(tradeVO);

			ScopeVO parentScope = new ScopeVO();
			parentScope.setId(rs.getInt(i++));
			parentScope.setScopename(rs.getString(i++));
			scopeVO.setParentScope(parentScope);
			ScopeVO grantParentScope = new ScopeVO();
			grantParentScope.setId(rs.getInt(i++));
			grantParentScope.setScopename(rs.getString(i++));
			scopeVO.setGrandParent(grantParentScope);
			scopeVO.setHomeScope(rs.getInt(i++) == 1 ? true : false);
			return scopeVO;
		}
	}

	/**
	 * 重新指定上级
	 * 
	 * @param scopeId
	 * @param newParentId
	 * @return
	 */
	public void changeParentScope(final int[] scopeIds,
			final int[] newParentIds, final int zoneId) {
		getTransactionTemplate().execute(
				new TransactionCallbackWithoutResult() {
					@SuppressWarnings("synthetic-access")
					@Override
					protected void doInTransactionWithoutResult(
							TransactionStatus status) {
						UserDao userDao = new UserDao();
						if(scopeIds.length>0){
							for (int i = 0; i < scopeIds.length; i++) {
								// 先转移子区域
								changeParentScope(scopeIds[i], newParentIds[i]);
							}
						}
						// 获取区域下的用户homscope=1的
						List<Integer> userids = getUserByScope(zoneId);
						for (int i = 0; i < userids.size(); i++) {
							// 删除用户
							userDao.deleteUser(userids.get(i));
						}
						//删除管理该区域的用户关系(homescope=0)
						deleteUserScope(zoneId);
						// 删除区域
						int c = delete(zoneId);
					}

				});

	}
	class UserZoneRowMapper implements GenericRowMapper<ScopeVO> {
		public ScopeVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ScopeVO scopeVO = new ScopeVO();
			int i = 1;
			scopeVO.setId(rs.getInt(i++));
			scopeVO.setScopename(rs.getString(i++));
			scopeVO.setAddress(rs.getString(i++));
			scopeVO.setLon(rs.getDouble(i++));
			scopeVO.setLat(rs.getDouble(i++));
			scopeVO.setEnlargenum(rs.getInt(i++));
			scopeVO.setDescription(rs.getString(i++));
			scopeVO.setScopetype(rs.getInt(i++));
			return scopeVO;
		}
	}
	class UserZoneRowMapperForSetScope implements GenericRowMapper<ScopeVO> {
		public ScopeVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ScopeVO scopeVO = new ScopeVO();
			int i = 1;
			scopeVO.setId(rs.getInt(i++));
			scopeVO.setScopename(rs.getString(i++));
			scopeVO.setAddress(rs.getString(i++));
			scopeVO.setLon(rs.getDouble(i++));
			scopeVO.setLat(rs.getDouble(i++));
			scopeVO.setEnlargenum(rs.getInt(i++));
			scopeVO.setDescription(rs.getString(i++));
			scopeVO.setScopetype(rs.getInt(i++));
			scopeVO.setUserIsSet(charToBool(rs.getString(i++)));
			return scopeVO;
		}
	}
	/**
	 *删除管理该区域的用户关系(homescope=0)
	 * 
	 * @param scopeId
	 * @return
	 */
	public int deleteUserScope(int scopeId) {
		return ejt.update("delete user_scope where isHomeScope=0 and scopeid=?", new Object[] {scopeId});
	}
	/**
	 * 根据用户编号查询用户管理的范围
	 * 
	 * @param userId
	 *            用户编号
	 * @return
	 */
	public List<ScopeVO> getUserZoneList(int userId) {
		String sql = "select s.id ,s.scopename,s.address ,s.lon , s.lat,s.enlargenum ,s.description,s.scopetype ,us.isSet " +
				" from scope s left join user_scope us on us.scopeid=s.id"+
				" where  us.uid=?  and isHomeScope=0";
		List<ScopeVO> zoneList = query(sql, new Object[] { userId },
				new UserZoneRowMapperForSetScope());
		return zoneList;
	}

	/**
	 * 查询一个范围管辖的子集
	 * 
	 * @param scopeId
	 *            范围id
	 * @param scopeType
	 *            范围类型
	 * @return
	 */
	public List<ScopeVO> getChildScope(int scopeId, int scopeType) {
		String sql = "select s.id ,s.scopename,s.address ,s.lon , s.lat,s.enlargenum ,s.description,s.scopetype from scope s where  parentid in (select id from scope where scopeType=? and id=?)";
		List<ScopeVO> scopeList = query(sql,
				new Object[] { scopeType, scopeId }, new UserZoneRowMapper());
		return scopeList;
	}

	/**
	 * ID为childId的范围是否属于ID为id范围的子范围
	 * 
	 * @param id
	 *            父节点
	 * @param childId
	 *            子节点
	 * @return 是否有关系
	 */
	public boolean isMyChild(int id, int childId) {
		int count = ejt.queryForInt(IS_MY_CHILD, new Object[] { id, id, id,
				childId }, -1);
		if (count > 0)
			return true;
		else
			return false;
	}

	// ////////////根据一个范围取出下面所有工厂
	public List<Integer> getChildScopeIds(int scopetype, int scopeid) {
		// 总部
		if (scopetype == ScopeVO.ScopeTypes.HQ) {
			String sql = "select id from scope  where scopetype=3";
			List<Integer> scopeIds = query(sql, new Object[] {},
					new ScopeIdsMapper());
			return scopeIds;
		} else if (scopetype == ScopeVO.ScopeTypes.ZONE) {
			String sql = "select s1.id from scope s1 where s1.scopetype=3  and s1.parentid in(select id from scope where parentid in(?))";
			List<Integer> scopeIds = query(sql, new Object[] { scopeid },
					new ScopeIdsMapper());
			return scopeIds;
		} else if (scopetype == ScopeVO.ScopeTypes.SUBZONE) {
			String sql = "select s1.id from scope s1 where s1.scopetype=3  and s1.parentid=?";
			List<Integer> scopeIds = query(sql, new Object[] { scopeid },
					new ScopeIdsMapper());
			return scopeIds;
		} else {
			List<Integer> scopeIds = new ArrayList<Integer>();
			scopeIds.add(scopeid);
			return scopeIds;
		}

	}
   //取出一个范围的所有子范围
	public List<Integer> getChildScopeIds2(int scopetype, int scopeid) {
		List<Integer> scopeIds =new ArrayList<Integer>();
		scopeIds.add(scopeid);
		// 总部
		if (scopetype == ScopeVO.ScopeTypes.HQ) {
			String sql = " select * from scope  where scopetype=1";
			List<Integer> zoneIds = query(sql, new Object[] {},
					new ScopeIdsMapper());
			     scopeIds.addAll(zoneIds);
				if(zoneIds .size()<1){
					return scopeIds;
				}
			     for (int i = 0; i < zoneIds .size(); i++) {
					String sql1 = " select * from scope  where scopetype=2 and parentid=?";
					List<Integer> subZoneIds = query(sql1, new Object[] {zoneIds.get(i)},
							new ScopeIdsMapper());
					if(subZoneIds .size()<1){
						return scopeIds;
					}
					scopeIds.addAll(subZoneIds);
					for (int j = 0; j < subZoneIds .size(); j++) {
						String sql2 = " select * from scope  where scopetype=3 and parentid=?";
						List<Integer> factoryIds = query(sql2, new Object[] {subZoneIds.get(j)},
								new ScopeIdsMapper());
						scopeIds.addAll(factoryIds);
					}
				}
		} else if (scopetype == ScopeVO.ScopeTypes.ZONE) {
			String sql1 = " select * from scope  where scopetype=2 and parentid=?";
			List<Integer> subZoneIds = query(sql1, new Object[] {scopeid},
					new ScopeIdsMapper());
			scopeIds.addAll(subZoneIds);
			if(subZoneIds .size()<1){
				return scopeIds;
			}
			for (int j = 0; j < subZoneIds.size(); j++) {
				String sql2 = " select * from scope  where scopetype=3 and parentid=?";
				List<Integer> factoryIds = query(sql2, new Object[] {subZoneIds.get(j)},
						new ScopeIdsMapper());
				scopeIds.addAll(factoryIds);
			}
			
		} else if (scopetype == ScopeVO.ScopeTypes.SUBZONE) {
			String sql1 = "select s1.id from scope s1 where s1.scopetype=3  and s1.parentid=?";
			scopeIds.addAll(query(sql1, new Object[] { scopeid },
					new ScopeIdsMapper()));
		} else {
			scopeIds.add(scopeid);
		}
		return scopeIds;
	}


	class ScopeIdsMapper implements GenericRowMapper<Integer> {
		public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
			Integer scopeId = rs.getInt(1);
			return scopeId;
		}
	}

	/**
	 * 根据数据点查找范围ID
	 */
	public int findScopeIdByDataPoint(int dataPointId) {
		return ejt
				.queryForInt(
						" select ds.factoryId from datapoints dp left join datasources ds on dp.dataSourceId = ds.id where dp.id = ? ",
						new Object[] { dataPointId }, -1);
	}

	/**
	 * 根据数据源查找范围ID
	 */
	public int findScopeIdByDataSource(int dataSourceId) {
		return ejt.queryForInt(
				" select factoryId from dataSources where id= ? ",
				new Object[] { dataSourceId }, -1);
	}

	// /**
	// * 根据系统事件查找范围ID
	// */
	// public int findScopeIdBySystem(int ){
	// return ejt.queryForInt("",new Object[]{dataPointId},-1);
	// }

	/**
	 * 根据组合事件查找范围ID
	 */
	public int findScopeIdByCompound(int compoundId) {
		return ejt.queryForInt(
				" select scopeId from compoundEventDetectors where id = ? ",
				new Object[] { compoundId }, -1);
	}

	/**
	 * 根据定时事件查找范围ID
	 */
	public int findScopeIdByScheduled(int scheduleId) {
		return ejt.queryForInt(
				" select scopeId from scheduledEvents where id = ? ",
				new Object[] { scheduleId }, -1);
	}

	/**
	 * 根据点连接查找范围ID
	 */
	public int findScopeIdByPointLink(int pointLinkId) {
		return ejt.queryForInt(" select scopeId from pointlinks where id = ? ",
				new Object[] { pointLinkId }, -1);
	}

	/**
	 * 根据用户查找范围ID
	 */
	public int findScopeIdByUser(int userId) {
		return ejt
				.queryForInt(
						" select scopeid from user_scope where isHomeScope = 1 and uid = ? ",
						new Object[] { userId }, -1);
	}

	/**
	 * 计算一个范围的事件总数
	 * 
	 * @param scopeIds
	 * @return
	 */
	public int countEvent(List<Integer> scopeIds) {
		String ids = "";
		for (int i = 0; i < scopeIds.size(); i++) {
			ids += scopeIds.get(i);
			if(i<scopeIds.size()-1){
				ids += ",";	
			}
		}
		return ejt.queryForInt(
				" select count(id) from Events where scopeid in(" + ids+") and typeid!=4 ", new Object[] {});
	}
	
	
	/**
	 * 查询一个时间内的事件
	 * 
	 * @param scopeIds
	 * @return
	 */
	public int countEvent(List<Integer> scopeIds, long time) {
		String ids = "";
		for (int i = 0; i < scopeIds.size(); i++) {
			ids += scopeIds.get(i);
			if(i<scopeIds.size()-1){
				ids += ",";	
			}
		}
		return ejt
				.queryForInt(
						" select count(id) from Events where activeTs>? and  scopeid in("
								+ ids
								+ ") and ( ackTs is null or (rtnApplicable='Y' and rtnTs is null))  and typeid!=4  ",
						new Object[] { time });
	}
	/**
	 * 查询一个时间内的事件
	 * 
	 * @param scopeIds
	 * @return
	 */
	public int countEvent(List<Integer> scopeIds, long startTs,boolean fromNone,long endTs,boolean toNone,int emailHandler) {
		String ids = "";
		for (int i = 0; i < scopeIds.size(); i++) {
			ids += scopeIds.get(i);
			if(i<scopeIds.size()-1){
				ids += ",";	
			}
		}
		if(fromNone){
			if(toNone){return ejt
				.queryForInt(" select count(id) from Events where emailHandler is not null " +
						" and ( ackTs is null or (rtnApplicable='Y' and rtnTs is null))  and typeid!=4 and emailHandler=?  and " +
						" scopeid in("+ids+")",new Object[]{emailHandler});
			}
			else{
				return ejt
				.queryForInt("select count(id) from Events where emailHandler is not null and activeTs< ?" +
						" and ( ackTs is null or (rtnApplicable='Y' and rtnTs is null))  and typeid!=4 and emailHandler=?  and " +
						" scopeid in("+ids+")",new Object[]{endTs,emailHandler});
			}
		}
		else{
			if(toNone){
				return ejt
					.queryForInt(" select count(id) from Events where emailHandler is not null and activeTs > ?" +
							" and ( ackTs is null or (rtnApplicable='Y' and rtnTs is null))  and typeid!=4 and emailHandler=?  and " +
							" scopeid in("+ids+")",new Object[]{startTs,emailHandler});
				}
				else{
					return ejt
					.queryForInt("select count(id) from Events where emailHandler is not null and activeTs between ? and ?" +
							" and ( ackTs is null or (rtnApplicable='Y' and rtnTs is null))  and typeid!=4 and emailHandler=?  and " +
							" scopeid in("+ids+")",new Object[]{startTs,endTs,emailHandler});
				}
			}
		}
	/**
	 * 查询一个区域内的事件(总是,3,7天未处理)
	 * 
	 * @return
	 */
	public Map<String, Integer> getEventCountByScope(int scopeType, int scopeId) {
		Date now = new Date();
		Map<String, Integer> map = new HashMap<String, Integer>();
		List<Integer> scopeIds = getChildScopeIds(scopeType,scopeId);
		if (scopeIds.size() == 0) {
			map.put("yellow", 0);
			map.put("orange", 0);
			map.put("red", 0);
			return map;
		}
		int yellow = countEvent(scopeIds,0,true,0,true,1);
		int orange = countEvent(scopeIds,0,true,0,true,2);
		int red = countEvent(scopeIds,0,true,0,true,3);
		map.put("yellow", yellow);
		map.put("orange", orange);
		map.put("red", red);
		return map;
	}
	
	/**
	 * 查询一个区域内的事件(激活事件,升级1,升级2...)
	 * 
	 * @return
	 */
	public Map<String, Integer> getEventCountByScope(int scopeType, int scopeId,long startTs,boolean fromNone,long endTs,boolean toNone,int emailHandler) {

		Map<String, Integer> map = new HashMap<String, Integer>();
		List<Integer> scopeIds = getChildScopeIds(scopeType,scopeId);
		if (scopeIds.size() == 0) {
			map.put("warn", 0);
			return map;
		}
		int warn = countEvent(scopeIds,startTs,fromNone,endTs,toNone,emailHandler);
		map.put("warn", warn);
		return map;
	}
	
	
	public int getScopeParentId(int scopeId){
		String sql="select parentId from  scope  where id=?";
	   return ejt.queryForInt(sql,new Object[] { scopeId});
	}
	/**
	 * 检测名称是否已经存在
	 * @param name
	 * @param excludeId
	 * @param tableName
	 * @return
	 */
	public boolean isNameUnique(String name, int excludeId, String tableName) {
        return ejt.queryForInt("select count(*) from " + tableName + " where scopename=? and id<>?", new Object[] { name,
                excludeId }) == 0;
    }
	
	/**
	 * 检测名称是否已经存在
	 * @param name
	 * @param excludeId
	 * @param tableName
	 * @return
	 */
	public boolean isCodeUnique(String code, int excludeId, String tableName) {
        return ejt.queryForInt("select count(*) from " + tableName + " where code=? and id<>?", new Object[] { code,
                excludeId }) == 0;
    }
	
	/**
	 * 
	 * @param userId 用户编号
	 * @return 区域集合
	 */
	public List<ScopeVO> getZoneByNormalUser(int userId,int scopeId) {
		List<ScopeVO> zoneList = query(SELECT_ZONE+" and s.id in(select scopeid from user_scope where uid=? and isHomeScope=0)",
				new Object[] { scopeId,userId }, new ZoneRowMapper());
		return zoneList;
	}
	
	/**
	 * 根据ID查询一个范围信息
	 * @param id 范围ID
	 * @return 范围信息
	 */
	public ScopeVO findById(int id) {
		List<ScopeVO> result = query("select s.id ,s.scopename,s.address ,s.lon , s.lat,s.enlargenum ,s.description,s.scopetype from scope s where id = ? ",
				new Object[] { id}, new UserZoneRowMapper());
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}
	
	
	public List<ScopeVO> findByIds(List<Integer> ids){
		String sql = " select s.id ,s.scopename,s.address ,s.lon , s.lat,s.enlargenum ,s.description,s.scopetype from scope s where id in (  ";
		for(int i =0;i<ids.size();i++){
			sql+=" ? ";
			if(i+1<ids.size()){
				sql+=" , ";
			}
		}
		sql+=" ) "; 
		return query(sql,ids.toArray(),new UserZoneRowMapper());
	}
	/**
	 * 根据用户查询用户所在的区域/用户管理的区域
	 * @param userId 用户编号
	 * @param homeScopeType
	 * @return
	 */

	public List<ScopeVO> getZonesByUser(int userId,int homeScopeType) {
		String sql="select s.id ,s.scopename,s.address ,s.lon , s.lat,s.enlargenum ,s.description,s.scopetype from scope s ";
		switch (homeScopeType) {
		case 0://总部普通用户
			 sql+="where id in(select scopeId from user_scope where uid=? and isHomeScope=0)";
			break;
		case 1://区域普通用户
			 sql+="where id in(select scopeId from user_scope where uid=?  and isHomeScope=1)";
			break;
		case 2://子区域用户
			 sql+="where id=(select parentid from scope where id=(select scopeid from user_scope where uid=?  and isHomeScope=1))";
			break;
		default:
			break;
		}
		return query(sql,new Object[]{userId},new UserZoneRowMapper());
	}
	public List<UserSetClass> isUserSetScope(int userId){
		return query("select us.uid,us.scopeid, s.scopetype, us.isSet from user_scope us left join scope s on s.id=us.scopeid where us.uid=?  and us.isHomeScope=0 and us.isSet='Y' ",
				new Object[]{userId},new UserIsSetMapper());
	}
	public class UserSetClass{
		private int userId;
		private int scopeId;
		private int scopeType;
		private boolean isSet;
		public int getUserId() {
			return userId;
		}
		public void setUserId(int userId) {
			this.userId = userId;
		}
		public int getScopeId() {
			return scopeId;
		}
		public void setScopeId(int scopeId) {
			this.scopeId = scopeId;
		}
		public boolean isSet() {
			return isSet;
		}
		public void setSet(boolean isSet) {
			this.isSet = isSet;
		}
		public int getScopeType() {
			return scopeType;
		}
		public void setScopeType(int scopeType) {
			this.scopeType = scopeType;
		}
	}
	class UserIsSetMapper implements GenericRowMapper<UserSetClass> {
		  public UserSetClass mapRow(ResultSet rs, int rowNum) throws SQLException {
			  	UserSetClass us=new UserSetClass();
			  	us.setUserId(rs.getInt(1));
			  	us.setScopeId(rs.getInt(2));
			  	us.setScopeType(rs.getInt(3));
			  	us.setSet(charToBool(rs.getString(4)));
			  	return us;
	        }
	}
	 
	public boolean updateScopeLogo(int scopeId,String logoName) {
		//更新所有下属区域的logo
		String sql = 
"WITH ScopeTree"+
" AS"+
"("+
"SELECT id,parentid,scopename,address,lon,lat,enlargenum,description,scopetype,tradeid,disabled,code,logo,"+
        "0 AS Level,"+
        "CAST(CASE WHEN parentid IS NULL THEN 'Root' END AS VARCHAR(50)) ParentList "+
   "FROM scope "+
  "WHERE parentid IS NULL "+
 "UNION ALL "+
 "SELECT C.id,C.parentid,C.scopename,C.address,C.lon,C.lat,C.enlargenum,C.description,C.scopetype,C.tradeid,C.disabled,C.code,C.logo,"+
        "P.Level+1 AS Level,"+
        "CAST(CAST(P.ParentList AS VARCHAR(50))+'->'+CAST(C.id AS VARCHAR(10)) AS VARCHAR(50)) ParentList "+
  "FROM ScopeTree P,scope C "+
  "WHERE C.parentid=P.id "+
	")" +
	"update scope set logo = ? where id in (SELECT id FROM ScopeTree WHERE ParentList LIKE (SELECT ParentList FROM ScopeTree WHERE id=?)+'%')";
//		int count = ejt.update("update scope set logo=? where id=?", new Object[] { logoName,
//				scopeId });
		int count = ejt.update(sql,new Object[]{logoName,scopeId});
		return count==1;
	}
	

	public boolean updateScopeLogoByscope(int scopeId,String logoName) {
		int count = ejt.update("update scope set logo=? where id=?", new Object[] { logoName,
				scopeId });
		return count==1;
	}
}
