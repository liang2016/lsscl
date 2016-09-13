package com.serotonin.mango.db.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import com.serotonin.db.spring.GenericRowMapper;
import com.serotonin.mango.vo.EmailTempVO;
import com.serotonin.web.i18n.LocalizableMessage;


public class EmailTempDao extends BaseDao{
	
	/**
	 * 查询系统时间--邮件发送失败的数据中触发时间和emailtemp表中触发时间相近
	 * 并且emailtemp邮箱地址和触发事件的邮箱地址一样，以此关联触发‘邮件发送失败’的用户
	 * @param email 邮箱地址
	 * @param ts 事件触发事件
	 * @return EmailTempVO
	 */
	public EmailTempVO getUidByEmailAndTs(LocalizableMessage message,long ts){
		Object[] args  = message.getArgs();
		String emailAddress = args[1].toString();
		List<EmailTempVO> list = query(" select id,uid,emailAddress,ts from eventTemp where ts > ?-60000 ",new Object[]{ts},new TempRowMapper());
		EmailTempVO result = null;
		for(EmailTempVO tempVO:list){
			if(emailAddress.indexOf(tempVO.getEmailAddress())!=-1){
				result =  tempVO;
			}
		}
		return result;
		//return query("select top 1 id,uid,emailAddress,ts from eventTemp where emailAddress = ? and ts > ?-60000 order by ts desc ",new Object[]{email,ts},-1);
	}
	
	/**
	 * 插入一条数据
	 * @param tempVO
	 */
	public void saveEmailTemp(EmailTempVO tempVO){
		doInsert(" insert into eventTemp(uid,emailAddress,ts) values(?,?,?)  ", 
				new Object[] {
				tempVO.getUid(),
				tempVO.getEmailAddress(),
				tempVO.getTs()
				}
		);
	}
	/**
	 * 是否有当前用户的数据
	 * @userId 用户ID
	 * @return 是否有数据
	 */
	public boolean hasData(int userId){
		int count = ejt.queryForInt(" select count(*) from eventtemp where uid = ? ",new Object[]{userId},0);
		if(0>=count){
			return false;
		}else{
			return true;
		}
	}
	
	
	class TempRowMapper implements GenericRowMapper<EmailTempVO> {
		public EmailTempVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			EmailTempVO tempVO = new EmailTempVO();
			int i = 1;
			tempVO.setId(rs.getInt(i++));
			tempVO.setUid(rs.getInt(i++));
			tempVO.setEmailAddress(rs.getString(i++));
			tempVO.setTs(rs.getLong(i++));
			return tempVO;
		}
	}
	
}
