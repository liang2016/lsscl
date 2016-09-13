package com.serotonin.mango.db.dao.acp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import com.serotonin.db.spring.GenericRowMapper;
import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.BaseDao;
import com.serotonin.mango.db.dao.DataPointDao;
import com.serotonin.mango.db.dao.DataSourceDao;
import com.serotonin.mango.view.text.TextRenderer;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.vo.acp.ACPSystemMembersVO;
import com.serotonin.mango.vo.acp.ACPTypeAttrVO;
import com.serotonin.mango.vo.acp.ACPTypeVO;
import com.serotonin.mango.vo.acp.ACPVO;
import com.serotonin.mango.vo.dataSource.DataSourceVO;
import com.serotonin.mango.vo.dataSource.modbus.ModbusPointLocatorVO;
import com.serotonin.mango.vo.event.PointEventDetectorVO;
import com.serotonin.mango.web.dwr.DataSourceEditDwr;
import com.serotonin.mango.web.dwr.beans.DataPointDefaulter;
/**
 * 空压机表操作类
 * 
 * @author 王金阳
 * 
 */
public class ACPDao extends BaseDao {

	/**
	 * 查询的语句头
	 */
	public static final String SELECT_START = " select temp.*,   acpt.typename from  (  select acp.id,acp.xid,acp.acname,acp.offset,acp.factoryid,acp.type,acp.volume,acp.pressure,acp.actid as acptid from aircompressor acp  ";

	/**
	 * 查询的语句尾
	 */
	public static final String SELECT_END = " ) temp  left join  aircompressor_type acpt on temp.acptid = acpt.id  ";
	/**
	 * 查找所有空压机	
	 */
	public static final String SELECT_ACP=SELECT_START
			+ "  where acp.type = "+ACPVO.ACP_TYPE + SELECT_END;
	/**
	 * 查找所有系统点集合
	 */	
	public static final String SELECT_SYSTEM=SELECT_START
			+ "  where acp.type = "+ACPVO.SYSTEM_TYPE + SELECT_END;
	
	/**
	 * 查询所有语句
	 */
	public static final String SELECT_ALL = SELECT_START + SELECT_END;

	/**
	 * 根据ID查找
	 */
	public static final String SELECT_BY_ID = SELECT_START
			+ "  where acp.id = ? " + SELECT_END;

	/**
	 * 根据ID删除一行
	 */
	public static final String DELETE_BY_ID = " DELETE FROM aircompressor WHERE ID = ? ";

	/**
	 * 添加一行数据
	 */
	public static final String DO_INSERT = " INSERT INTO aircompressor(ACNAME,XID,ACTID,OFFSET,FACTORYID,TYPE,VOLUME,PRESSURE) VALUES (?,?,?,?,?,?,?,?) ";

	/**
	 * 更新一条数据
	 */
	public static final String DO_UPDATE = " UPDATE aircompressor SET ACNAME = ? ,XID = ?,ACTID = ?,OFFSET = ?,FACTORYID = ?,VOLUME = ? ,PRESSURE= ?  WHERE ID = ?	 ";

	/**
	 * 获取所有的空压机信息/系统点集合信息
	 */
	public static final String SELECT_BY_DATASOURCEID = " select acp.id,acp.xid,acp.acname,acp.offset,acp.factoryid,acp.type,acp.volume,acp.pressure,acpt.id as acptid,acpt.typename from aircompressor acp left join aircompressor_type acpt on acp.actid = acpt.id  where acp.type=? and acp.id in (select  temp.acid from ( select acpm.acid,acpm.dpid from aircompressor_members acpm where acaid is not null and dpid in (select id from datapoints where dataSourceId = ?) ) temp group by temp.acid )";
	/**
	 * 获取所有的空压机信息+系统点集合信息
	 */
	public static final String SELECT_BY_DATASOURCEID2 = " select acp.id,acp.xid,acp.acname,acp.offset,acp.factoryid,acp.type,acp.volume,acp.pressure,acpt.id as acptid,acpt.typename from aircompressor acp left join aircompressor_type acpt on acp.actid = acpt.id  where acp.id in (select  temp.acid from ( select acpm.acid,acpm.dpid from aircompressor_members acpm where acaid is not null and dpid in (select id from datapoints where dataSourceId = ?) ) temp group by temp.acid )";
	/**
	 * 空压机或者
	 */
	public static final String SELECT_COUNT_BY_DATASOURCEID = " select count(*) from aircompressor acp left join aircompressor_type acpt on acp.actid = acpt.id  where acp.id in (select  temp.acid from ( select acpm.acid,acpm.dpid from aircompressor_members acpm where dpid in (select id from datapoints where dataSourceId = ?) ) temp group by temp.acid ) "; 
	
	/**
	 * 保存时候验证是否xid已经存在
	 */
	public static final String SELECT_BY_XID_0 = " SELECT COUNT(*) FROM AIRCOMPRESSOR WHERE XID = ? ";
	/**
	 * 修改时候验证是否xid已经存在
	 */
	public static final String SELECT_BY_XID_1 = " SELECT COUNT(*) FROM AIRCOMPRESSOR WHERE XID = ? and id!=? ";
	
	/**
	 * 查找所有空压机	
	 */
	public static final String SELECT_ACP_FACTORY=SELECT_START
			+ "  where acp.type = "+ACPVO.ACP_TYPE  +" and factoryid=? "+ SELECT_END;
	
	
	/**
	 * 获取所有空压机根据工厂id
	 * @return
	 */
	public List<ACPVO> getAllAcpByFactoryId(int factoryId){
		List<ACPVO> result = query(SELECT_ACP_FACTORY, new Object[]{factoryId},
				new ACPRowMapper());
		return result;
	}
	
	/**
	 * 获取所有空压机
	 * @return
	 */
	public List<ACPVO> getAllAcp(){
		List<ACPVO> result = query(SELECT_ACP, new Object[0],
				new ACPRowMapper());
		return result;
	}
	/**
	 * 获取所有系统点集合
	 * @return
	 */
	public List<ACPVO> getAllSystem(){
		List<ACPVO> result = query(SELECT_ACP, new Object[0],
				new ACPRowMapper());
		return result;
	}
	
	
	/**
	 * 查找全部空压机
	 * 
	 * @return 全部空压机的集合
	 */
	public List<ACPVO> findAll() {
		List<ACPVO> result = query(SELECT_ALL, new Object[0],
				new ACPRowMapper());
		return result;
	}

	/**
	 * 根据ID查找空压机
	 * 
	 * @param id
	 *            空压机ID
	 * @return 空压机实体
	 */
	public ACPVO findById(int id) {
		List<ACPVO> result = query(SELECT_BY_ID, new Object[] { id },
				new ACPRowMapper());
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	public int acpid = -1;
	
	/**
	 * 保存一台空压机信息,以及自动生成数据点及其和空压机的关系
	 * 
	 * @param acpvo
	 *            空压机信息
	 * @return 此行数据的ID
	 */
	public int save(final ACPVO acpvo,final int dsid) {
		//final int acpid = -1;
		getTransactionTemplate().execute(
				new TransactionCallbackWithoutResult() {
					@SuppressWarnings("synthetic-access")
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus status) { 
						//添加空压机
						ACPVO tempAcpvo = acpvo;
						acpid = doInsert(DO_INSERT, new Object[] { tempAcpvo.getAcpname(),tempAcpvo.getXid(),tempAcpvo.getAcpTypeVO().getId(),tempAcpvo.getOffset(),tempAcpvo.getFactoryId(),tempAcpvo.getType(),tempAcpvo.getVolume(),tempAcpvo.getPressure()});
						//1.根据空压机型号查询此型号下的所有属性
						ACPTypeAttrDao acpTypeAttrDao = new ACPTypeAttrDao();
						List<ACPTypeAttrVO> configList = acpTypeAttrDao.findByType(tempAcpvo.getAcpTypeVO().getId());
						//添加数据点，在空压机成员表中添加该空压机的成员以及关系
						Random random = new Random();
						DataPointDao dataPointDao = new DataPointDao();
						for(int i=0;i < configList.size();i++){
							//获取默认配置
							ACPTypeAttrVO config = configList.get(i);
							DataSourceEditDwr tempDwr = new DataSourceEditDwr();
							DataPointVO dp = getPoint(dsid,null);
					        dp.setXid(dataPointDao.generateUniqueXid());
					        dp.setName(tempAcpvo.getAcpname()+"-"+config.getAcpAttrVO().getAttrname());
					        ModbusPointLocatorVO pointvo = new ModbusPointLocatorVO();
					        ModbusPointLocatorVO defaultConfig = (ModbusPointLocatorVO)config.getDataPointVO().getPointLocator();
					        //pointvo.setBit((byte)0);
					        //pointvo.setCharset("ASCII");
					        pointvo.setRange(defaultConfig.getRange());
					        pointvo.setModbusDataType(defaultConfig.getModbusDataType());
					        pointvo.setMultiplier(defaultConfig.getMultiplier());
					        pointvo.setAdditive(defaultConfig.getAdditive());
					        pointvo.setOffset(defaultConfig.getOffset()+acpvo.getOffset());
					        pointvo.setBit(defaultConfig.getBit());
					        //自动生成的点都为不可设置状态
					        pointvo.setSettableOverride(false);
					        //pointvo.setRegisterCount(1);
					        //pointvo.setSettableOverride(true);
					        //pointvo.setSlaveId(1);
					        //pointvo.setSlaveMonitor(false); 
					        dp.setPointLocator(pointvo);
					        TextRenderer temp = config.getDataPointVO().getTextRenderer();
					        dp.setTextRenderer(config.getDataPointVO().getTextRenderer());
					        
					        //FIXME 激活点
					        dp.setEnabled(true);
					        
					        //插入数据点
//					        Common.ctx.getRuntimeManager().saveDataPoint(dp);
					        new DataPointDao().saveDataPoint(dp);
					        //获取插入的数据点的编号ID
					        int dpid = ejt.queryForInt(" select id from datapoints where  xid= ?  ", new Object[] { dp.getXid() }, -1);
					        if(dpid!=-1){
					        	//把当前数据点关系到空压机成员表中
					        	ejt.update(" insert into aircompressor_members(acid,acaid,dpid) values(?,?,?) ",new Object[]{acpid,config.getAcpAttrVO().getId(),dpid});
					        } 					
						} 
					}
				}
		);
		return acpid;
	}

	
	private DataPointVO getPoint(int dsid, DataPointDefaulter defaulter) {
        DataSourceVO<?> ds = new DataSourceDao().getDataSource(dsid);
//FIXME 删除
        DataPointVO dp = new DataPointVO();
            dp.setXid(new DataPointDao().generateUniqueXid());
            dp.setDataSourceId(ds.getId());
            dp.setPointLocator(ds.createPointLocator());
            dp.setEventDetectors(new ArrayList<PointEventDetectorVO>(0));
            if (defaulter != null)
                defaulter.setDefaultValues(dp);
        return dp;
    }
	
	/**
	 * 根据ID删除一台空压机信息-- 以及空压机相关的数据点，以及空压机成员表中的关系
	 * 
	 * @param id 空压机ID
	 * @return 删除的行数
	 */
	public void delete(final int acpid) {
		getTransactionTemplate().execute(
				new TransactionCallbackWithoutResult() {
					@SuppressWarnings("synthetic-access")
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus status) { 
						int id = acpid;
						//根据ID获取该空压机信息
						ACPVO currentAcp =  findById(acpid);
						//根据空压机型号 查找 属性集合
						ACPTypeAttrDao acpTypeAttrDao = new ACPTypeAttrDao();
						List<ACPTypeAttrVO> configList = acpTypeAttrDao.findByType(currentAcp.getAcpTypeVO().getId());
						//获取相关数据点的编号
//						String getDpids = " select dpid from aircompressor_members where acid = ? and acaid in(";
//						List<Object> params = new ArrayList<Object>();//为获取相关数据点编号准备的参数
//						params.add(acpid);
//						for(int i = 0;i<configList.size();i++){
//							ACPTypeAttrVO config = configList.get(i);
//							getDpids+=" ? ";
//							params.add(config.getAcpAttrVO().getId());
//							if(i+1<configList.size()){
//								getDpids+=" , ";
//							}else{
//								getDpids+=" ) ";
//							} 			
//						}
						
						String getDpids = " select dpid from aircompressor_members where acid = ?";
						List<Object> params = new ArrayList<Object>();//为获取相关数据点编号准备的参数
						params.add(acpid);
						
						//获取相关点的ID
						List<Integer> dpidList = query(getDpids,params.toArray(),new TEMPRowMapper());
						//删除空压机成员关系表---------此操作讲放入删除数据点的事务中
						//ejt.update(" delete from aircompressor_members where acid = ? ",new Object[]{id} );
						//删除空压机相关数据点(只删除添加空压机时自动添加的点)//-->这里要修改为，删除空压机，讲删除空压机相关所有的点的关系，以及点的数据
						DataPointDao dataPointDao = new DataPointDao();
						for(int j =0 ;j <dpidList.size();j++){
							DataPointVO dp = dataPointDao.getDataPoint(dpidList.get(j));
							if (dp != null){
								Common.ctx.getRuntimeManager().deleteDataPoint(dp);
							}
						}
						
						
						//删除压缩空气系统统计配置
						ejt.update(" delete from aircompressor_system_members where membertype = ? and memberid = ?  ",new Object[]{ACPSystemMembersVO.MemberTypes.ACP,id});
						
						
						//删除空压机
						ejt.update(" delete from aircompressor where id = ?  ",new Object[]{id});
					}
				}
		); 
	}

	/**
	 * 更新一台空压机信息
	 * 
	 * @param acpvo
	 * @return
	 */
	public void update(final ACPVO acpvo,final int dsid) {
		getTransactionTemplate().execute(
				new TransactionCallbackWithoutResult() {
					@SuppressWarnings("synthetic-access")
					@Override
					protected void doInTransactionWithoutResult(TransactionStatus status) { 
						ejt.update(DO_UPDATE, new Object[] { 
								acpvo.getAcpname(),
								acpvo.getXid(),
								acpvo.getAcpTypeVO().getId(),
								acpvo.getOffset(),
								acpvo.getFactoryId(),
								acpvo.getVolume(),
								acpvo.getPressure(),
								acpvo.getId() 
						});
						updateAcpPoint(acpvo,dsid);	
						
					}});
	}
	
	private void updateAcpPoint(ACPVO acpvo,int dsid){
		DataPointDao dataPointDao = new DataPointDao();
		List<DataPointVO> pointOfSystemList = dataPointDao.getDataPointByAcpId(acpvo.getId() ,dsid);
		for (int i = 0; i < pointOfSystemList.size(); i++) {
			DataPointVO dp=pointOfSystemList.get(i);
			if(dp.getName().indexOf("-")!=-1)
				dp.setName(acpvo.getAcpname()+"-"+dp.getName().split("-")[1]);
			else
				dp.setName(acpvo.getAcpname()+"-"+dp.getName());
			Common.ctx.getRuntimeManager().saveDataPoint(dp);
		} 
	}
	/**
	 * 根据数据源ID查找空压机
	 * @param dsid 数据源编号
	 * @return 空压机集合
	 */
	public List<ACPVO> findAcpsByDataSourceId(int dsid) {
		List<ACPVO> result = query(SELECT_BY_DATASOURCEID, new Object[]{ACPVO.ACP_TYPE,dsid},
				new ACPRowMapper()); 
		return result; 
	}
	/**
	 * 根据数据源ID查询系统点集合
	 * @param dsid 数据源ID
	 * @return 系统集合
	 */
	public List<ACPVO> findSystemsByDataSourceId(int dsid) {
		List<ACPVO> result = query(SELECT_BY_DATASOURCEID, new Object[]{ACPVO.SYSTEM_TYPE,dsid},
				new ACPRowMapper()); 
		return result; 
	}
	/**
	 * 数据源下系统点集合或者空压机的集合
	 * @param dsid
	 * @return
	 */
	public List<ACPVO> findAcpsAndSystemsByDataSourceId(int dsid) {
		List<ACPVO> result = query(SELECT_BY_DATASOURCEID2, new Object[]{dsid},
				new ACPRowMapper()); 
		return result; 
	}
	
	/**
	 * 判断xid是否将会出现重复
	 * @param xid 输出编号
	 * @return 重复返回false，不重复true
	 */
	public boolean validateXid(int acpid,String xid){
		int count =0;
		if(acpid==-1){
			count = ejt.queryForInt(SELECT_BY_XID_0, new Object[] { xid }, 0);
		}else{
			count = ejt.queryForInt(SELECT_BY_XID_1, new Object[] { xid,acpid }, 0);
		}
		if(count==0) return true;
		else return false;
	}
	/**
	 * 获取一个唯一的xid
	 * @return xid
	 */
	public String getUniqueXid(){
		Random random = new Random();
		boolean unique = false;
		String xid = "";
		while(unique==false){
			xid = "ACP-"+random.nextInt(1000000);
			int count = ejt.queryForInt(" select count(*) from aircompressor where xid = ? ",new Object[]{xid},0);
			if(count==0){
				unique=true;
			}
		}
		return xid;
	}
	
	
	/**
	 * 该数据源下是否有空压机/系统点集合
	 * @param dataSourceId 数据源ID
	 * @param type 空压机0/系统点集合
	 * @return 是否有
	 */
	public boolean hasAcp(int dataSourceId){
		int count = 0;
		count = ejt.queryForInt(SELECT_COUNT_BY_DATASOURCEID, new Object[] {dataSourceId }, 0);
		if(count==0) return false;
		else return true;
	}
	 

	/**
	 * 处理查询的空压机数据
	 * 
	 * @author 王金阳
	 * 
	 */
	class ACPRowMapper implements GenericRowMapper<ACPVO> {
		public ACPVO mapRow(ResultSet rs, int rowNum) throws SQLException {
			ACPVO acpvo = new ACPVO();
			int i = 1;
			acpvo.setId(rs.getInt(i++));
			acpvo.setXid(rs.getString(i++));
			acpvo.setAcpname(rs.getString(i++));
			acpvo.setOffset(rs.getInt(i++));
			acpvo.setFactoryId(rs.getInt(i++));
			acpvo.setType(rs.getInt(i++));
			acpvo.setVolume(rs.getInt(i++));
			acpvo.setPressure(rs.getInt(i++));
			ACPTypeVO acpTypeVO = new ACPTypeVO();
			acpTypeVO.setId(rs.getInt(i++));
			acpTypeVO.setTypename(rs.getString(i++));
			acpvo.setAcpTypeVO(acpTypeVO);
			return acpvo;
		}
	}
	
	class TEMPRowMapper implements GenericRowMapper<Integer> {
		public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
			 return rs.getInt(1);
		}
	}
    //getacpIds
    public List<Integer> getAcpIds(int factory) {
        return queryForList("select id from aircompressor where factoryId=?", new Object[] { factory },Integer.class);
    }
    
    //getDpid by acaid
    public int getDataPointId(int acaid,int acid) {
        return ejt.queryForInt("select dpid from aircompressor_members where acaid=? and acid=?", new Object[] { acaid,acid },-1);
    }
}
