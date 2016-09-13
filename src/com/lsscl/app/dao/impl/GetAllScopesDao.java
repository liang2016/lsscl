package com.lsscl.app.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.lsscl.app.bean.QC;
import com.lsscl.app.bean.RSP;
import com.lsscl.app.bean.Scope;
import com.lsscl.app.bean.ScopesMsgBody;
import com.lsscl.app.dao.AppDao;
import com.serotonin.db.spring.GenericRowMapper;
/**
 * 区域关系列表
 * @author yxx
 *
 */
public class GetAllScopesDao extends AppDao{

	private static final String getAllScopes = "select id,scopename,scopetype,parentid from scope";
	@Override
	public RSP getRSP(QC qc) {
		RSP rsp = new RSP(qc.getMsgId());
		String version = qc.getMsgBody().get("VERSION");
		String currentVersion = getScopesVersion()+"";
		List<Scope>scopes = new ArrayList<Scope>();
		if(!currentVersion.equals(version)){//版本号不相同
			scopes = ejt.query(getAllScopes,new Object[]{},new ScopesMapper());
		}
		ScopesMsgBody msgBody = new ScopesMsgBody();
		msgBody.setVersion(currentVersion);
		msgBody.setScopes(scopes);
		rsp.setMsgBody(msgBody);
		return rsp;
	}

	private class ScopesMapper implements GenericRowMapper<Scope>{

		@Override
		public Scope mapRow(ResultSet rs, int i) throws SQLException {
			Scope scope = new Scope();
			scope.setId(rs.getInt("id"));
			scope.setName(rs.getString("scopename"));
			scope.setType(rs.getInt("scopetype"));
			scope.setParentId(rs.getInt("parentid"));
			return scope;
		}
		
	} 
	private static final String getScopesVersion = "select scopesVersion from appConfig";
	private String getScopesVersion() {
		String version = queryForObject(getScopesVersion, new Object[]{}, String.class,"");
		return version;
	}

}
