package com.serotonin.mango.db.dao.acp;

import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import com.serotonin.db.spring.GenericRowMapper;
import com.serotonin.mango.db.dao.BaseDao;
import com.serotonin.mango.vo.acp.ACPAttrVO;
import com.serotonin.mango.vo.acp.ACPVO;

/**
 * 空压机属性数据表 操作类
 * @author 王金阳
 *
 */
public class ACPAttrDao extends BaseDao{
	
	public static final String SELECT_BY_ACPTPYE = " select  acpa.* from aircompressor_attr acpa,aircompressor_type acpt,aircompressor_type_attr acpta where acpt.id = acpta.actid and acpta.acaid = acpa.id and acpt.id = ? ";
	
	/**
	 * 根据空压机型号查询属性
	 * @typeId 空压机型号ID
	 * @return 此型号空压机的属性集合
	 */
	public List<ACPAttrVO> findByACPType(int typeId){
		List<ACPAttrVO> result = query(SELECT_BY_ACPTPYE,new Object[]{typeId},new ACPAttrRowMapper());
		return result;
	}
	
	/**
	 * 是否可以删除
	 * @return 是否可以删除
	 */
	public boolean canDelete(int attrId){
		//在空压机成员表中是否有记录
		int count = ejt.queryForInt("select count(*) from aircompressor_members where acaid = ?",new Object[]{attrId},-1);
		if(count>0) return false;
		else return true;
	}
	
	
	class ACPAttrRowMapper implements GenericRowMapper<ACPAttrVO> {
		public ACPAttrVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ACPAttrVO acpAttrVO = new ACPAttrVO();
			int i = 1;
			acpAttrVO.setId(rs.getInt(i++));
			acpAttrVO.setAttrname(rs.getString(i++));
			acpAttrVO.setDescription(rs.getString(i++)); 
			return acpAttrVO;
		}
	}

}
