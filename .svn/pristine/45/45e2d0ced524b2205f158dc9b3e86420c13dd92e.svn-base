package com.lsscl.app.dao.impl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.serotonin.db.spring.GenericRowMapper;

public class MapResultData implements GenericRowMapper<Map<String, String>> {

	@Override
	public Map<String, String> mapRow(ResultSet rs, int i) throws SQLException {
		Map<String, String> map = new HashMap<String, String>();
		if (rs != null) {
			ResultSetMetaData rsd = rs.getMetaData();

			int count = rsd.getColumnCount();
			for (int j = 1; j <= count; j++) {
				map.put(rsd.getColumnName(j), rs.getString(j));
			}
		}
		return map;
	}

}
