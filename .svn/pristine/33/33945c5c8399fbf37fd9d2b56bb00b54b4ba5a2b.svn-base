package com.serotonin.mango.db.dao.statistics;

import java.util.List;
import java.sql.ResultSet;
import com.serotonin.db.spring.GenericRowMapper;
import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.BaseDao;
import com.serotonin.mango.vo.acp.ACPVO;
import com.serotonin.mango.vo.statistics.ScheduledStatisticVO;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.TransactionStatus;
import java.sql.SQLException;
import java.util.logging.Logger;
import java.util.Date;

/**
 * 实时统计Dao
 * 
 * @author 王金阳
 * 
 */
public class ScheduledStatisticDao extends BaseDao {
	
	
	public static final Logger log = Logger.getLogger(ScheduledStatisticDao.class.toString());
	//如果查找的时间没有记录，则返回-1
	public static final long NO_RECORD = -1L;
	//插入一条统计结果
	public static final String DO_INSERT = " insert into scheduledStatistic(scriptId,value,ts,unitType,unitId,date) values(?,?,?,?,?,?) ";
	//删除某个点的某个脚本的统计记录
	public static final String DO_DELETE_ON_TIME = " delete from scheduledStatistic where scriptId = ? and ts = ? ";
	//获取某个脚本在下某个机器/系统的最后一次统计时间
	public static final String MAX_STATISTICTIME_BY_SCRIPT_AND_UNIT = " select max(ts) from scheduledStatistic where scriptId = ? and unitId = ? ";
	
	
	/**
	 * 保存统计完成的行
	 * @param vo 统计实体
	 * @return 新的ID
	 */
	public int save(ScheduledStatisticVO vo){
		return doInsert(DO_INSERT, new Object[] {
				vo.getScriptId(), 
				vo.getValue(),
				vo.getTimestamp(),
				vo.getUnitType(), 
				vo.getUnitId(), 
				new Date(vo.getTimestamp()).toLocaleString()
		});
	}
	 
	/**
	 * 根据脚本查找最后一次统计时间
	 * @param scriptId 脚本ID
	 * @return 最近一次统计时间
	 */
	public long getLastestStatisticTimeBySciptAndUnit(int scriptId,int unitId){
		return ejt.queryForLong(MAX_STATISTICTIME_BY_SCRIPT_AND_UNIT,new Object[]{scriptId,unitId},NO_RECORD);
	}
	
	/**
	 * 删除某个脚本某个时刻的统计记录
	 * @param scriptId 脚本Id
	 * @param ts 时刻
	 */
	public void deleteScriptStatisticOnTs(int scriptId,long ts){
		ejt.update(DO_DELETE_ON_TIME,new Object[]{scriptId,ts});
	}
	
	class ScheduledStatisticRowMapper implements GenericRowMapper<ScheduledStatisticVO> {
		public ScheduledStatisticVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ScheduledStatisticVO vo = new ScheduledStatisticVO();
			int i = 1;
			vo.setId(rs.getInt(i++));
			vo.setScriptId(rs.getInt(i++));
			vo.setValue(rs.getDouble(i++));
			vo.setTimestamp(rs.getLong(i++));
			vo.setUnitType(rs.getInt(i++));
			vo.setUnitId(rs.getInt(i++));
			return vo;
		}
	}

}
