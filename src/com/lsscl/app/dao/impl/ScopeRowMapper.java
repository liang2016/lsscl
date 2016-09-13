package com.lsscl.app.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.lsscl.app.bean.Scope;
import com.serotonin.db.spring.GenericRowMapper;

public class ScopeRowMapper implements GenericRowMapper<Scope> {
	public static final String selectByParentId2 = "select s.id,s.scopename,s.scopetype,e.L1,e.L2,e.L3 from scope s "
			+ "left join eventStatistics e on s.id = e.scopeId "
			+ "where s.parentid = ?";
	public static final String getRootScope = "select s.id,s.scopename,s.scopetype,e.L1,e.L2,e.L3 from scope s "
			+ "left join eventStatistics e on s.id = e.scopeId "
			+ "left join user_scope us on us.scopeid = s.id "
			+ "left join users u on us.uid = u.id "
			+ "where u.phone = ? and u.password = ? and us.isHomeScope=0";

	@Override
	public Scope mapRow(ResultSet rs, int i) throws SQLException {
		Scope s = new Scope();
		s.setId(rs.getInt("id"));
		s.setName(rs.getString("scopename"));
		s.setType(rs.getInt("scopetype"));
		String sL1 = rs.getString("L1");
		String sL2 = rs.getString("L2");
		String sL3 = rs.getString("L3");
		int l1 = sL1!=null?Integer.parseInt(sL1):0;
		int l2 = sL2!=null?Integer.parseInt(sL2):0;
		int l3 = sL3!=null?Integer.parseInt(sL3):0;
		s.setL1(l1);
		s.setL2(l2);
		s.setL3(l3);
		return s;
	}
}