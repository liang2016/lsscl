package com.serotonin.mango.vo;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.serotonin.db.spring.GenericRowMapper;
import com.serotonin.util.SerializationHelper;

public class ResultData implements GenericRowMapper<Map<String, Object>> {

	@Override
	public Map<String, Object> mapRow(ResultSet rs, int i) throws SQLException {
		Map<String, Object> map = new HashMap<String, Object>();
		if (rs != null) {
			ResultSetMetaData rsd = rs.getMetaData();
            
			int count = rsd.getColumnCount();
			for (int j = 1; j <= count; j++) {
				
				String typeName = rsd.getColumnTypeName(j);
				if("image".equals(typeName)){
					map.put(rsd.getColumnName(j), SerializationHelper.readObject(rs.getBlob(j).getBinaryStream()));
				}else{
					map.put(rsd.getColumnName(j), rs.getObject(j));
				}
			}
		}
		return map;
	}

}