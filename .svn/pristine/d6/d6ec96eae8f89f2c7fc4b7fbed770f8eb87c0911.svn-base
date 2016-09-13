package com.serotonin.mango.db.dao.scope;

import com.serotonin.db.spring.GenericRowMapper;
import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.BaseDao;
import com.serotonin.mango.vo.event.EventHandlerVO;
import com.serotonin.util.SerializationHelper;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.TransactionStatus;
import org.springframework.jdbc.core.ResultSetExtractor;
import java.sql.Types;
import java.util.Map;
import java.util.HashMap;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.serotonin.mango.rt.event.type.AuditEventType;
import com.serotonin.db.spring.GenericRowMapperResultSetExtractor;
import java.util.List;
public class ScopeSettingDao extends BaseDao {

	void insertSetting(int scopeId, EventHandlerVO handler) {
		handler.setId(doInsert("insert into scopeSendSetting (scopeId, data) values (?,?)",
				new Object[] { scopeId,SerializationHelper.writeObject(handler) }, new int[] {
						Types.INTEGER, Types.BLOB }));
	}

	
	
	void updateSetting(int scopeId, EventHandlerVO handler) {
		 ejt.update("update  scopeSendSetting set data=? where scopeId=?",
				new Object[] { SerializationHelper.writeObject(handler),
						scopeId }, new int[] { Types.BLOB, Types.INTEGER });
	}

	public EventHandlerVO saveSendSetting(final int id, final int scopeId,
			final EventHandlerVO handler) {
		getTransactionTemplate().execute(
				new TransactionCallbackWithoutResult() {
					@Override
					protected void doInTransactionWithoutResult(
							TransactionStatus status) {
						if (id == Common.NEW_ID)
							insertSetting(scopeId, handler);
						else
							updateSetting(scopeId, handler);
					}
				});
		return handler;
	}

	/**
	 * 根据scopeid获得配置
	 * 
	 * @param scopeId
	 *            范围id
	 * @return
	 */
	public EventHandlerVO getScopeSendSetting(int scopeId) {
		String sql = "select id,data from scopeSendSetting where scopeid=?";
		List<EventHandlerVO> handlers=query(sql, new Object[] { scopeId }, new EventHandlerRowMapper());
		if(handlers.size()<1){
			return new EventHandlerVO();
		}
		return handlers.get(0);
		 
	}

	class EventHandlerRowMapper implements GenericRowMapper<EventHandlerVO> {
        public EventHandlerVO mapRow(ResultSet rs, int rowNum) throws SQLException {
            EventHandlerVO h = (EventHandlerVO) SerializationHelper.readObject(rs.getBlob(2).getBinaryStream());
            h.setId(rs.getInt(1));
            return h;
        }
    }
	//删除Setting 主要为工厂
	public void deleteSetting(int scopeId) {
	/*	ejt.update("delete from factorysetting where factoryId=?",
					new Object[] { scopeId });*/
		ejt.update("delete from scopeSendSetting where scopeId=?",
				new Object[] { scopeId });

	}
	
}
