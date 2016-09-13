package com.serotonin.mango.db.dao.scope;

import com.serotonin.db.spring.GenericRowMapper;
import com.serotonin.mango.db.dao.BaseDao;
import com.serotonin.mango.vo.scope.TradeVO;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 行业表数据库操作类
 * @author 王金阳
 *
 */
public class TradeDao extends BaseDao {

	/**
	 * 查询所有行业语句
	 */
	public static final String SELECT_BASE = " select id,tradename,description from trade ";

	/***
	 * 查询所有行业
	 * 
	 * @return
	 */
	public List<TradeVO> findAll() {
		List<TradeVO> tradeList = query(SELECT_BASE, new Object[0],
				new TradeRowMapper());
		return tradeList;

	}
	/**
	 * 根据ID查找
	 */
	public static final String SELECT_BY_ID = SELECT_BASE + " WHERE ID = ? ";
	
	/**
	 * 根据ID查找行业
	 * @param id 行业ID
	 * @return 对应行业
	 */
	public TradeVO findById(int id){
		List<TradeVO> tradeList = query(SELECT_BASE, new Object[]{id},new TradeRowMapper());
		if(tradeList!=null&&tradeList.size()>0){
			return tradeList.get(0);
		}else{ 
			return null;
		}
	}
	/**
	 * 插入语句
	 */
	public static final String INSERT_BASE = " INSERT INTO TRADE(TRADENAME,DESCRIPTION) VALUES (?,?) ";
	
	/**
	 * 新增一个行业
	 * @param tradeVO 新的行业
	 * @return 新增的行业的ID 
	 */
	public int save(TradeVO tradeVO){
		return doInsert(INSERT_BASE, new Object[] { tradeVO.getTradename(),tradeVO.getDescription() });
	}

	/**
	 * 更新语句
	 */
	public static final String UPDATE_BASE = " UPDATE TRADE SET TRADENAME = ? ,DESCRIPTION = ? WHERE ID = ?  "; 
	
	/**
	 * 更新一个行业
	 * @param tradeVO 预期的更新结果
	 * @return  执行行数
	 */
	public int update(TradeVO tradeVO){
		return ejt.update(UPDATE_BASE,new Object[]{tradeVO.getTradename(),tradeVO.getDescription(),tradeVO.getId()});
	}
	
	/**
	 * 删除语句
	 */
	public static final String DELETE_BASE = " DELETE FROM TRADE WHERE ID = ? ";
	
	/**
	 * 根据ID删除一个行业
	 * @param id 行业ID
	 * @return 执行行数
	 */
	public int delete(int id){
		return ejt.update(DELETE_BASE); 
	}
	
	/**
	 * 行业结果
	 * @author 王金阳
	 *
	 */
	class TradeRowMapper implements GenericRowMapper<TradeVO> {
		public TradeVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			TradeVO tradeVO = new TradeVO();
			int i = 1;
			tradeVO.setId(rs.getInt(i++));
			tradeVO.setTradename(rs.getString(i++));
			tradeVO.setDescription(rs.getString(i++));
			return tradeVO;
		}
	}

}
