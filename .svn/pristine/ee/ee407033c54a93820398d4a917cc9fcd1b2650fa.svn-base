package com.serotonin.mango.db.dao.acp;

import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.serotonin.db.spring.GenericRowMapper;
import com.serotonin.mango.db.dao.BaseDao;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.vo.acp.ACPTypeVO;
import com.serotonin.mango.vo.acp.ACPAttrVO;
import com.serotonin.mango.vo.acp.ACPTypeAttrVO;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.transaction.TransactionStatus;
import com.serotonin.util.SerializationHelper;
import java.sql.Types;

/**
 * 空压机型号-属性表数据库操作类
 * @author 王金阳
 *
 */
public class ACPTypeAttrDao extends BaseDao {
	
	/**
	 * 型号表-型号属性关系表-属性表：链表查询
	 */
	public static final String SELECT_BASE = " select acpta.id,acpta.data,acpa.id as aid,acpa.attrname,acpa.description as adescription,acpt.id as tid,acpt.typename,acpt.description as tdescription from aircompressor_attr acpa,aircompressor_type_attr acpta,aircompressor_type acpt where acpa.id = acpta.acaid and acpt.id = acpta.actid  ";
	
	/**
	 * 根据ID修改一行数据
	 */
	public static final String UPDATE_BASE = " update aircompressor_type_attr set data=? where id = ? ";
	
	/**
	 * 修改属性名称
	 */
	public static final String UPDATE_ATTR = " update aircompressor_attr set attrname = ? where id = ? ";
	
	public static final String INSERT_ATTR = " INSERT INTO AIRCOMPRESSOR_ATTR(ATTRNAME) VALUES(?) ";
	
	/**
	 * 插入一行数据
	 */
	public static final String INSERT_BASE = " INSERT INTO aircompressor_type_attr (ACTID,ACAID,DATA) VALUES(?,?,?) ";
	
	/**
	 * 根据型号查询
	 * @param typeId 型号ID
	 * @return 结果集合
	 */
	public List<ACPTypeAttrVO> findByType(int typeId){
		List<ACPTypeAttrVO> result = query(SELECT_BASE+" AND acpt.id=? ",new Object[]{typeId},new ACPTypeAttrRowMapper());
		return  result;
	}
	
	/**
	 * 根据ID查询
	 * @param id 编号
	 * @return 唯一行的结果
	 */
	public ACPTypeAttrVO findById(int id){
		List<ACPTypeAttrVO> result = query(SELECT_BASE+" and acpta.id=? ",new Object[]{id},new ACPTypeAttrRowMapper());
		if(result!=null&&result.size()>0){
			return result.get(0);
		}else{
			return null;
		}
	}
	
	/**
	 * 更新一条数据
	 * @return 受影响行数
	 */
	public void update(final ACPTypeAttrVO acpTypeAttrVO){
		getTransactionTemplate().execute(
				new TransactionCallbackWithoutResult() {
					@SuppressWarnings("synthetic-access")
					@Override
					protected void doInTransactionWithoutResult(
							TransactionStatus status) {
						ACPTypeAttrVO vo = acpTypeAttrVO;
						//修改关系以及配置
						ejt.update(UPDATE_BASE, new Object[] {
								SerializationHelper.writeObject(vo.getDataPointVO()),
								vo.getId()
								},new int[]{
								Types.BLOB,
								Types.INTEGER
						});
						//修改属性名称
						ejt.update(UPDATE_ATTR,new Object[]{
								vo.getAcpAttrVO().getAttrname(),
								vo.getAcpAttrVO().getId()
						});
					}
				});
	}
	
	public int id; //返回新的ID
	/**
	 * 添加一行型号属性配置(包括添加aircompressor_type_attr表和aircompressor_attr表)
	 * @param acpTypeAttrVO 配置信息
	 */
	public int save(final ACPTypeAttrVO acpTypeAttrVO){
		getTransactionTemplate().execute(
				new TransactionCallbackWithoutResult() {
					@SuppressWarnings("synthetic-access")
					@Override
					protected void doInTransactionWithoutResult(
							TransactionStatus status) {
						ACPTypeAttrVO vo = acpTypeAttrVO;
						//添加属性名称
						int attrId = doInsert(INSERT_ATTR,new Object[]{
								vo.getAcpAttrVO().getAttrname()
						});
						//添加关系以及配置
						id = doInsert(INSERT_BASE, new Object[] {
								vo.getAcpTypeVO().getId(),
								attrId,
								SerializationHelper.writeObject(vo.getDataPointVO())
								},
								new int[]{
								Types.INTEGER,
								Types.INTEGER,
								Types.BLOB
								}
						);
						
					}
				});
		return id;
	}
	
	/**
	 * 根据ID删除一行默认配置
	 */
	public static final String DELETE_BASE = " delete from aircompressor_type_attr where id =? "; 
	/**
	 * 根据ID删除属性行
	 */
	public static final String DELETE_ATTR_BY_ID = " delete from aircompressor_attr where id = ? ";
	
	/**
	 * 删除一行型号-属性默认配置+删除属性表的对应行
	 * @param id 默认配置行的ID
	 * @param attrId 属性名称ID
	 */
	public void delete(final int id,final int attrId){
		getTransactionTemplate().execute(
				new TransactionCallbackWithoutResult() {
					@SuppressWarnings("synthetic-access")
					@Override
					protected void doInTransactionWithoutResult(
							TransactionStatus status) {
						int deleteConfigId = id;
						int deleteAttrId = attrId;
						//删除一行型号-属性默认配置
						ejt.update(DELETE_BASE,new Object[]{deleteConfigId});
						//删除统计配置表行
						ejt.update(" delete statisticsConfiguration where acpaid = ? ",new Object[]{deleteAttrId});
						//删除属性表的对应行
						ejt.update(DELETE_ATTR_BY_ID,new Object[]{deleteAttrId});
					}
				}); 
	}
	
	/**
	 * 删除一个型号+型号相关的默认配置+配置相关的属性名称
	 * @param typeId 型号ID
	 */
	public void deleteType(final int typeId){
		getTransactionTemplate().execute(
				new TransactionCallbackWithoutResult() {
					@SuppressWarnings("synthetic-access")
					@Override
					protected void doInTransactionWithoutResult(
							TransactionStatus status) {
						int deleteTypeId = typeId;
						List<ACPTypeAttrVO> deleteVO = findByType(deleteTypeId);
						ACPTypeAttrDao temp = new ACPTypeAttrDao();
						for(ACPTypeAttrVO vo:deleteVO){
							delete(vo.getId(),vo.getAcpAttrVO().getId());
						}
						ejt.update(" delete from AIRCOMPRESSOR_TYPE where id = ? ",new Object[]{deleteTypeId});
					}
				});  
	}
//	
//	public static final String FIND_ATTRID_BY_ID = " SELECT ACAID FROM AIRCOMPRESSOR_TYPE_ATTR WHERE ID =? ";
//	
//	/**
//	 * 根据默认配置ID查找对应的属性ID
//	 * @param id 配置ID
//	 * @return 属性ID
//	 */
//	public int findAttrIdById(int id){
//		return ejt.queryForInt(FIND_ATTRID_BY_ID,new Object[]{id},-1);
//	}
//	
	class ACPTypeAttrRowMapper implements GenericRowMapper<ACPTypeAttrVO> {
		public ACPTypeAttrVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ACPTypeAttrVO acpTypeAttrVO = new ACPTypeAttrVO();
				int i = 1;
				acpTypeAttrVO.setId(rs.getInt(i++));
//				acpTypeAttrVO.setOffset(rs.getInt(i++));  
//				acpTypeAttrVO.setMultiplier(rs.getFloat(i++));  
//				acpTypeAttrVO.setAdditive(rs.getFloat(i++));  
//				acpTypeAttrVO.setDataType(rs.getInt(i++));  
//				acpTypeAttrVO.setRange(rs.getInt(i++));  
//				acpTypeAttrVO.setRenderertType(rs.getInt(i++));  
//				acpTypeAttrVO.setLayout(rs.getString(i++));  
//				acpTypeAttrVO.setSuffix(rs.getString(i++));  
				DataPointVO dp = (DataPointVO) SerializationHelper.readObject(rs.getBlob(i++).getBinaryStream());
				acpTypeAttrVO.setDataPointVO(dp);
				ACPAttrVO acpAttrVO = new ACPAttrVO();
				acpAttrVO.setId(rs.getInt(i++));
				acpAttrVO.setAttrname(rs.getString(i++));
				acpAttrVO.setDescription(rs.getString(i++));
				acpTypeAttrVO.setAcpAttrVO(acpAttrVO);
				ACPTypeVO acpTypeVO = new ACPTypeVO();
				acpTypeVO.setId(rs.getInt(i++));
				acpTypeVO.setTypename(rs.getString(i++));
				acpTypeVO.setDescription(rs.getString(i++));
				acpTypeAttrVO.setAcpTypeVO(acpTypeVO);
			return acpTypeAttrVO;
		}
	}
	
	

}
