package com.serotonin.mango.db.dao;

import com.serotonin.db.spring.GenericRowMapper;
import com.serotonin.mango.vo.RenderVo;
import com.serotonin.mango.view.text.TextRenderer;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.TransactionStatus;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import com.serotonin.util.SerializationHelper;
import com.serotonin.mango.Common;

public class TextRenderDao extends BaseDao {
	private static final String SELECT = "select id,name,datatype,render from textRenderSetting";
	private static final String SELECT_BASE = "select id,name,datatype from textRenderSetting";
	private static final String SELECT_RENDER = "select id,name,datatype,render from textRenderSetting";
	public static final String INSERT_BASE = " INSERT INTO textRenderSetting (name,datatype,render) VALUES(?,?,?) ";
	public static final String UPDATE_BASE = " update textRenderSetting  set name=?,datatype=?,render=? where id=?";
	public static final String DELETE = "delete from textRenderSetting   where id=?";
	//查询所有
	public List<RenderVo> findAll() {
		List<RenderVo> result = query(SELECT_BASE, new Object[] {},
				new RenderVoRowMapper());
		return result;
	}
	//按照id查询
	public RenderVo findById(int id) {
		List<RenderVo> result = query(SELECT_RENDER + " where id=?",
				new Object[] { id }, new TextRenderRowMapper());
		if (result.size() > 0)
			return result.get(0);
		else
			return null;
	}
	//删除
	public void delete(final int id){
		getTransactionTemplate().execute(
				new TransactionCallbackWithoutResult() {
					@SuppressWarnings("synthetic-access")
					@Override
					protected void doInTransactionWithoutResult(
							TransactionStatus status) {
						ejt.update(DELETE,new Object[]{id});
					}
				}); 
	}
	
	class TextRenderRowMapper implements GenericRowMapper<RenderVo> {
		public RenderVo mapRow(ResultSet rs, int rowNum) throws SQLException {
			RenderVo renderVo = new RenderVo();
			renderVo.setId(rs.getInt(1));
			renderVo.setName(rs.getString(2));
			renderVo.setDataType(rs.getInt(3));
			TextRenderer textRender = (TextRenderer) SerializationHelper
					.readObject(rs.getBlob(4).getBinaryStream());
			renderVo.setTextRenderer(textRender);
			return renderVo;
		}
	}

	class RenderVoRowMapper implements GenericRowMapper<RenderVo> {
		public RenderVo mapRow(ResultSet rs, int rowNum) throws SQLException {
			RenderVo renderVo = new RenderVo();
			renderVo.setId(rs.getInt(1));
			renderVo.setName(rs.getString(2));
			renderVo.setDataType(rs.getInt(3));
			return renderVo;
		}
	}

	int id = 0;

	private int save(final RenderVo vo) {
		getTransactionTemplate().execute(
				new TransactionCallbackWithoutResult() {
					@SuppressWarnings("synthetic-access")
					@Override
					protected void doInTransactionWithoutResult(
							TransactionStatus status) {
						id = doInsert(INSERT_BASE, new Object[] {
								vo.getName(),
								vo.getDataType(),
								SerializationHelper.writeObject(vo
										.getTextRenderer()) }, new int[] {
								Types.VARCHAR, Types.INTEGER, Types.BLOB });

					}
				});
		return id;
	}

	private int update(final RenderVo vo) {
		getTransactionTemplate().execute(
				new TransactionCallbackWithoutResult() {
					@SuppressWarnings("synthetic-access")
					@Override
					protected void doInTransactionWithoutResult(
							TransactionStatus status) {
						ejt.update(UPDATE_BASE, new Object[] {
								vo.getName(),
								vo.getDataType(),
								SerializationHelper.writeObject(vo
										.getTextRenderer()) ,vo.getId()}, new int[] {
								Types.VARCHAR, Types.INTEGER, Types.BLOB ,Types.INTEGER});
					}
				});
		return vo.getId();
	}

	
	public int edit(RenderVo vo) {
		if(vo.getId()==Common.NEW_ID)
			save(vo);
		else
			update(vo);
		return vo.getId();

	}
}
