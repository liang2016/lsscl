package com.serotonin.mango.db.dao.statistics;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.serotonin.db.spring.GenericRowMapper;
import com.serotonin.mango.db.dao.BaseDao;
import com.serotonin.mango.vo.statistics.StatisticsScriptVO;
import java.util.Random;

/**
 * StatisticsScriptVO实体对应的数据库操作类
 * @author 王金阳
 *
 */
public class StatisticsScriptDao extends BaseDao {
	
	/**
	 * 查询所有
	 */
	private static final String SELECT_BASE = " select id,xid,name,disabled,conditionText,startTs from statisticsScript ";
	
	/**
	 * 根据ID查找一行
	 */
	private static final String SELECT_BY_ID = SELECT_BASE+" where id =? "; 
	
	/**
	 * 删除语句
	 */
	private static final String DELETE_BASE = " delete from statisticsScript where id = ? "; 
	
	/**
	 * 保存一行数据
	 */
	private static final String DO_INSERT = " insert into statisticsScript(xid,name,disabled,conditionText,startTs) values(?,?,?,?,?) ";
	
	/**
	 * 更新一行数据
	 */
	private static final String DO_UPDATE = " update statisticsScript set xid=?,name=?,disabled=? where id = ? ";
	
	
	
	/**
	 * 查询所有行
	 * @return 返回所有行
	 */
	public List<StatisticsScriptVO> findAll(){
		List<StatisticsScriptVO> results = query(SELECT_BASE,new Object[0],new StatisticsScriptRowMapper());
		return results;		
	}
	
	/**
	 * 根据ID集合查询结果脚本集合
	 * @param ids ID集合
	 * @return 脚本集合
	 */
	public List<StatisticsScriptVO> findByIds(int[] ids){
		String sql = SELECT_BASE+" where id in( ";
		Object[] params = new Object[ids.length];
		for(int i=0;i<ids.length;i++){
			sql+=" ? ";
			if(i<ids.length-1){
				sql+=",";
			}
			params[i]=ids[i];
		}
		sql+=")";
		List<StatisticsScriptVO> results = query(sql,params,new StatisticsScriptRowMapper());
		return results;
	}
	
	/**
	 * 根据ID查找
	 * @param id ID
	 * @return ID为id的行
	 */
	public StatisticsScriptVO findById(int id){
		List<StatisticsScriptVO> results = query(SELECT_BY_ID,new Object[]{id},new StatisticsScriptRowMapper());
		if(results!=null&&results.size()>0){
			return results.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * 获取XX指数脚本
	 * @return XX指数脚本
	 */
	public StatisticsScriptVO getSomeIndex(String name){
		List<StatisticsScriptVO> results = query(SELECT_BASE+" where name=? ",new Object[]{name},new StatisticsScriptRowMapper());
		if(results!=null&&results.size()>0){
			return results.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * 根据ID删除一行
	 * @param id ID
	 */
	public void delete(int id){
		ejt.update(DELETE_BASE, new Object[]{id});
	}
	
	/**
	 * 保存一行数据
	 * @param scriptVO 统计脚本实体
	 */
	public int save(StatisticsScriptVO scriptVO){
		return doInsert(DO_INSERT,new Object[]{
				scriptVO.getXid(),
				scriptVO.getName(),
				boolToChar(scriptVO.isDisabled()),
				scriptVO.getConditionText(),
				scriptVO.getStartTime(),
		});
	}
	
	/**
	 * 更新一行数据
	 * @param scriptVO 统计脚本实体
	 */
	public void update(StatisticsScriptVO scriptVO){
		ejt.update(DO_UPDATE,new Object[]{
				scriptVO.getXid(),
				scriptVO.getName(),
				boolToChar(scriptVO.isDisabled()),
				scriptVO.getId()
		});
	}
	
	/**
	 * 获取一个唯一的xid
	 * @return xid
	 */
	public String getUniqueXid(){
		Random random = new Random();
		boolean unique = false;
		String xid = "";
		while(unique==false){
			xid = "SS-"+random.nextInt(1000000);
			int count = ejt.queryForInt(" select count(*) from statisticsScript where xid = ? ",new Object[]{xid},0);
			if(count==0){
				unique=true;
			}
		}
		return xid;
	}
	
	/**
	 * 该xid是否在表中是唯一的
	 * @param xid 
	 * @return 是否
	 */
	public boolean isUniqueXid(String xid){
		int count = ejt.queryForInt(" select count(*) from statisticsScript where xid = ? ",new Object[]{xid},0);
		if(count==0)return true; 
		else return false;
	}
	
	
	class StatisticsScriptRowMapper implements GenericRowMapper<StatisticsScriptVO> {
		public StatisticsScriptVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			int i = 1;
			return new StatisticsScriptVO(rs.getInt(i++),rs.getString(i++),rs.getString(i++),charToBool(rs.getString(i++)),rs.getString(i++),rs.getLong(i++));
		}
	}
}
