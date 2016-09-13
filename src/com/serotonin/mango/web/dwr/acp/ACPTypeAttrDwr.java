package com.serotonin.mango.web.dwr.acp;

import java.util.List;
import java.util.ArrayList;
import com.serotonin.mango.db.dao.acp.ACPTypeDao;
import com.serotonin.mango.db.dao.acp.ACPAttrDao;
import com.serotonin.mango.db.dao.acp.ACPTypeAttrDao;
import com.serotonin.mango.vo.acp.ACPTypeVO;
import com.serotonin.mango.vo.acp.ACPAttrVO;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.vo.acp.ACPTypeAttrVO;
import com.serotonin.mango.web.dwr.BaseDwr;
import com.serotonin.web.dwr.DwrResponseI18n;
import com.serotonin.mango.Common;
import com.serotonin.mango.DataTypes;
import com.serotonin.mango.vo.dataSource.modbus.ModbusPointLocatorVO;
import com.serotonin.mango.view.ImplDefinition;
import com.serotonin.mango.view.text.*;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.vo.statistics.ACPAttrStatisticsVO;
import com.serotonin.mango.db.dao.statistics.ACPStatisticsDao;
import com.serotonin.mango.db.dao.statistics.StatisticsDao;
import com.serotonin.mango.vo.statistics.StatisticsVO;
import com.serotonin.mango.db.dao.acp.AcpMetaDao;
import com.serotonin.mango.vo.acp.AcpMetaVo;
import com.serotonin.mango.vo.dataSource.meta.MetaPointLocatorVO;
import com.serotonin.db.IntValuePair;
import com.serotonin.mango.web.dwr.beans.DataPointDefaulter;
import com.serotonin.mango.vo.dataSource.DataSourceVO;
import com.serotonin.mango.vo.dataSource.meta.MetaDataSourceVO;
import com.serotonin.mango.vo.event.PointEventDetectorVO;
import com.serotonin.util.StringUtils;
import com.serotonin.mango.vo.dataSource.PointLocatorVO;
/**
 * 空压机型号-属性操作DWR
 * @author 王金阳
 *
 */
public class ACPTypeAttrDwr extends BaseDwr{
//	/**
//	 * 查询所有型号
//	 * @return 所有型号集合
//	 */
//	public List<ACPTypeVO> findAllType(){
//		ACPTypeDao acpTypeDao = new ACPTypeDao();
//		return acpTypeDao.findAll();
//	}
//	
//	/**
//	 * 根据型号ID查询所有属性集合
//	 * @return 某个型号的属性集合
//	 */
//	public List<ACPAttrVO> findAttrsByType(int typeId){
//		ACPAttrDao  acpAttrDao = new ACPAttrDao();
//		return acpAttrDao.findByACPType(typeId);
//	}
	/**
	 * 获取 所有型号-->所有型号下的属性集合
	 * @return
	 */
	public DwrResponseI18n getTree() {
        DwrResponseI18n response = new DwrResponseI18n();
        ACPTypeDao acpTypeDao = new ACPTypeDao();
        ACPAttrDao  acpAttrDao = new ACPAttrDao();
        List<ACPTypeVO> types = acpTypeDao.findAll();
        List<List<ACPAttrVO>> attrsList = new ArrayList<List<ACPAttrVO>>();
        for(int i =0;i<types.size();i++){
        	attrsList.add(acpAttrDao.findByACPType(types.get(i).getId()));    		
        }
        response.addData("types", types);
        response.addData("attrsList", attrsList);
        return response;
	}
	
	/**
	 * 根据型号ID查询默认配置
	 * @param typeId
	 * @return
	 */
	public DwrResponseI18n findTypeAttrRelationByType(int typeId){
		DwrResponseI18n response = new DwrResponseI18n();
		ACPTypeAttrDao acpTypeAttrDao = new ACPTypeAttrDao();
		List<ACPTypeAttrVO> result = acpTypeAttrDao.findByType(typeId);
		List<ACPTypeAttrVO> typeattrList = new ArrayList<ACPTypeAttrVO>();
		List<ModbusPointLocatorVO> locatorList = new ArrayList<ModbusPointLocatorVO>();
		List<String> rendererTypeList = new ArrayList<String>();
		for(ACPTypeAttrVO config :result){
			ACPTypeAttrVO typeattr = new ACPTypeAttrVO();
			typeattr.setId(config.getId());
			ACPAttrVO acpAttrVO = new ACPAttrVO();
			acpAttrVO.setId(config.getAcpAttrVO().getId());
			acpAttrVO.setAttrname(config.getAcpAttrVO().getAttrname());
			typeattr.setAcpAttrVO(acpAttrVO);
			ACPTypeVO acpTypeVO = new ACPTypeVO();
			acpTypeVO.setId(config.getAcpTypeVO().getId());
			acpTypeVO.setTypename(config.getAcpTypeVO().getTypename());
			typeattr.setAcpTypeVO(config.getAcpTypeVO());
			ModbusPointLocatorVO localtor = (ModbusPointLocatorVO)config.getDataPointVO().getPointLocator();
			typeattrList.add(typeattr);
			locatorList.add(localtor);
			rendererTypeList.add(config.getDataPointVO().getTextRenderer().getTypeName());
		}
		response.addData("typeattrList", typeattrList);
		response.addData("locatorList", locatorList);
		response.addData("rendererTypeList", rendererTypeList);
		return response;
	}
	
	/**
	 * 根据ID查找
	 * @param id 编号
	 * @return 
	 */
	public DwrResponseI18n findById(int id){
		DwrResponseI18n response = new DwrResponseI18n();
		ACPTypeAttrDao acpTypeAttrDao = new ACPTypeAttrDao();
	    ACPTypeAttrVO result = acpTypeAttrDao.findById(id);
	    User user = Common.getUser();
	    ModbusPointLocatorVO localtor = (ModbusPointLocatorVO)result.getDataPointVO().getPointLocator();
	    user.setEditPoint(result.getDataPointVO());
	    result.setDataPointVO(null);
	    response.addData("typeattr",result);
	    response.addData("localtor",localtor);
	    return response;
	}
	
	/**
	 * 更新
	 */
	public ACPTypeAttrVO edit(int id,int typeid,int attrid,String attrname,int dataType,int range,double multiplier,double additive,int offset,int bit){
		int returnId = -1;
		ACPTypeAttrDao acpTypeAttrDao = new ACPTypeAttrDao();
		ACPTypeAttrVO acpTypeAttrVO = new ACPTypeAttrVO();
		DataPointVO dp = new DataPointVO();//数据点对象
		ModbusPointLocatorVO pointLocator = new ModbusPointLocatorVO();//pointLocator对象
		pointLocator.setOffset(offset);
		if(bit!=-1){
			pointLocator.setBit((byte)bit);
		}
		pointLocator.setMultiplier(multiplier);
		pointLocator.setAdditive(additive);
		pointLocator.setModbusDataType(dataType);
		pointLocator.setRange(range); 
		dp.setPointLocator(pointLocator);//pointLocator对象 放入 数据点对象中
		User user = Common.getUser();
		DataPointVO textRendererIn = user.getEditPoint();
		TextRenderer textRenderer = textRendererIn.getTextRenderer();//TextRenderer 对象
		dp.setTextRenderer(textRenderer);//TextRenderer 对象 放入 数据点对象中
		acpTypeAttrVO.setDataPointVO(dp);
//		acpTypeAttrVO.setOffset(offset);  
//		acpTypeAttrVO.setMultiplier(multiplier);  
//		acpTypeAttrVO.setAdditive(additive);  
//		acpTypeAttrVO.setDataType(dataType);  
//		acpTypeAttrVO.setRange(range);  
//		acpTypeAttrVO.setRenderertType(renderertType);  
//		acpTypeAttrVO.setLayout(layout);  
//		acpTypeAttrVO.setSuffix(suffix);  
		
		ACPAttrVO acpAttrVO = new ACPAttrVO();
		acpAttrVO.setId(attrid);
		acpAttrVO.setAttrname(attrname);
		acpTypeAttrVO.setAcpAttrVO(acpAttrVO);
		ACPTypeVO acpTypeVO = new ACPTypeVO();
		acpTypeVO.setId(typeid);
		acpTypeAttrVO.setAcpTypeVO(acpTypeVO);
		if(id!=-1){
			acpTypeAttrVO.setId(id);
			returnId = acpTypeAttrVO.getId();
			acpTypeAttrDao.update(acpTypeAttrVO);
		}else{
			returnId = acpTypeAttrDao.save(acpTypeAttrVO);
		}
		return acpTypeAttrDao.findById(returnId);
	}
	
	/**
	 * 更新或者保存一个型号信息
	 * @param id 型号ID
	 * @param typename 型号名称
	 * @return 当前型号的ID
	 */
	public ACPTypeVO editType(int id,String typename,int type,String warnCount,String alarmCount){
		ACPTypeVO acpTypeVO = new ACPTypeVO();
		acpTypeVO.setId(id);
		acpTypeVO.setTypename(typename);
		acpTypeVO.setType(type);
		acpTypeVO.setAlarmCount(alarmCount);
		acpTypeVO.setWarnCount(warnCount);
		ACPTypeDao acpTypeDao = new ACPTypeDao();
		if(id==Common.NEW_ID){
			return acpTypeDao.findById(acpTypeDao.save(acpTypeVO));
		}else{
			acpTypeDao.update(acpTypeVO);
			return acpTypeVO;
		}
	}
	
	/**
	 * 验证该型号名称是否已经存在
	 * @param typeId 型号ID
	 * @param typename 型号名称
	 * @return 是否存在
	 */
	public int validateType(int typeId,String typename){
		ACPTypeVO acpTypeVO = new ACPTypeVO();
		ACPTypeDao acpTypeDao = new ACPTypeDao();
		acpTypeVO.setId(typeId);
		acpTypeVO.setTypename(typename);
		return acpTypeDao.nameisExist(acpTypeVO);
	}
	
	/**
	 * 删除机器类型
	 * @param typeId 机器类型
	 */
	public void deleteType(int typeId){
		ACPTypeAttrDao acpTypeAttrDao = new ACPTypeAttrDao();
		acpTypeAttrDao.deleteType(typeId);  
	}
	
	/**
	 * 删除一行配置
	 * @param id 配置行ID
	 * @param attrId 属性名称
	 */
	public void deleteConfig(int id,int attrId){
		ACPTypeAttrDao acpTypeAttrDao = new ACPTypeAttrDao();
		acpTypeAttrDao.delete(id,attrId); 
	}
	
	/**
	 * 属性是否可以被删除
	 * @param attrId 属性ID
	 */
	public boolean canDeleteAttr(int attrId){
		ACPAttrDao acpAttrDao = new ACPAttrDao();
		return acpAttrDao.canDelete(attrId);
	}
	
	/**
	 * 判断当前机器类型是否可以被删除
	 * @param typeId 类型ID
	 */
	public boolean canDeleteType(int typeId){
		ACPTypeDao acpTypeDao = new ACPTypeDao();
		return acpTypeDao.canDelete(typeId);
	}
	
	/**
	 * 根据数据类型获取的渲染器类型集合
	 * @return 类型集合
	 */
	public DwrResponseI18n showTextRenderer(int dataType){
		DwrResponseI18n response = new DwrResponseI18n();
		User user = Common.getUser();
		DataPointVO dp =user.getEditPoint(); 
		if(dp==null||dp.getPointLocator()==null){
			dp=new DataPointVO();
			ModbusPointLocatorVO modbusLocatorVO = new ModbusPointLocatorVO();
			modbusLocatorVO.setModbusDataType(dataType);
			dp.setPointLocator(modbusLocatorVO);
			response.addData("textRenderer",null);
		}else{
			ModbusPointLocatorVO modbusLocatorVO = dp.getPointLocator();
			modbusLocatorVO.setModbusDataType(dataType);
			dp.setPointLocator(modbusLocatorVO);
			response.addData("textRenderer",dp.getTextRenderer());
		}
		List<ImplDefinition> definitionList = BaseTextRenderer.getImplementation(dp.getPointLocator().getDataTypeId());
		response.addData("definitionList",definitionList);
		return response;
	}
	
	public void newEditPoint(){
		User user = Common.getUser();
		user.setEditPoint(new DataPointVO());
	}
	
	/**
	 * 获取当前编辑的点（此处是记录讲渲染器暂时记录在点对象中）
	 * @return 点
	 */
	public DataPointVO getEditPoint(){
		User user = Common.getUser();
		return user.getEditPoint();	
	}
	/**
	 * 讲不同类型的渲染器存放到正在编辑的点对象中红，下面的方法一样作用
	 * @param format 构造器需要的参数
	 * @param suffix 构造器需要的参数
	 */
    public void setAnalogTextRenderer(String format, String suffix) {
    	getEditPoint().setTextRenderer(new AnalogRenderer(format, suffix));
    }

    public void setBinaryTextRenderer(String zeroLabel, String zeroColour, String oneLabel, String oneColour) {
    	getEditPoint().setTextRenderer(new BinaryTextRenderer(zeroLabel, zeroColour, oneLabel, oneColour));
    }

    public void setMultistateRenderer(List<MultistateValue> values) {
        MultistateRenderer r = new MultistateRenderer();
        for (MultistateValue v : values)
            r.addMultistateValue(v.getKey(), v.getText(), v.getColour());
        getEditPoint().setTextRenderer(r);
    }

    public void setNoneRenderer() {
    	getEditPoint().setTextRenderer(new NoneRenderer());
    }

    public void setPlainRenderer(String suffix) {
    	getEditPoint().setTextRenderer(new PlainRenderer(suffix));
    }

    public void setRangeRenderer(String format, List<RangeValue> values) {
        RangeRenderer r = new RangeRenderer(format);
        for (RangeValue v : values)
            r.addRangeValues(v.getFrom(), v.getTo(), v.getText(), v.getColour());
        getEditPoint().setTextRenderer(r);
    }

    public void setTimeTextRenderer(String format, int conversionExponent) {
    	getEditPoint().setTextRenderer(new TimeRenderer(format, conversionExponent));
    }
//    
//    /**
//     * 返回统计配置列表
//     */
//    pubilc List<StatisticsConfiguration> ShowStatisticsConfiguration(){
//    	return null;
//    }
//	
    /**
	 * 查询一个空压机型号的属性和对应的统计
	 * 
	 * @param acptId
	 * @return
	 */
	public DwrResponseI18n getACPAttrStatisticsVOByACPId(int acptId) {
		DwrResponseI18n response = new DwrResponseI18n();
		List<ACPAttrStatisticsVO> result = new ArrayList<ACPAttrStatisticsVO>();
		List<ModbusPointLocatorVO> locatorList = new ArrayList<ModbusPointLocatorVO>();
		ACPStatisticsDao dao = new ACPStatisticsDao();
		result = dao.getACPAttrStatisticsVOByACPId(acptId);
		for (ACPAttrStatisticsVO config : result) {
			ModbusPointLocatorVO localtor = (ModbusPointLocatorVO) config
					.getAttrVO().getDp().getPointLocator();
			locatorList.add(localtor);
		}
		response.addData("result", result);
		response.addData("locatorList", locatorList);
		return response;
	}

	/**
	 * 查询一个空压机型号的属性
	 * 
	 * @param acptId
	 *            空压机型号编号
	 * @return
	 */
	public DwrResponseI18n getACPAttrId(int acptId) {
		DwrResponseI18n response = new DwrResponseI18n();
		ACPStatisticsDao dao = new ACPStatisticsDao();
		List<ACPAttrVO> list = dao.getACPAttrId(acptId);
		List<ModbusPointLocatorVO> locatorList = new ArrayList<ModbusPointLocatorVO>();
		for (ACPAttrVO config : list) {
			ModbusPointLocatorVO localtor = (ModbusPointLocatorVO) config.getDp().getPointLocator();
			locatorList.add(localtor);
		}
		response.addData("list", list);
		response.addData("locatorList", locatorList);
		return response;
	}
	/**
	 * 根据id获取类型的基本信息
	 * @param id
	 * @return
	 */
	public DwrResponseI18n getAcpTypeBase(int id){
		DwrResponseI18n response = new DwrResponseI18n();
		ACPTypeVO acpType=new ACPTypeDao().findById(id);
		response.addData("type", acpType);
		return response;
	}
	/**
	 * 查询指定类型的统计参数
	 * 
	 * @param useType
	 *            适用类型
	 * @return
	 */
	public List<StatisticsVO> getSystemStatistics(int useType) {
		StatisticsDao statisticsDao = new StatisticsDao();
		List<StatisticsVO> list = statisticsDao.getSystemStatistics(useType);
		return list;
	}

	/**
	 * 删除修改添加空压机型号配置
	 * 
	 * @param statisticsId
	 *            统计参数
	 * @param newAttrId
	 *            新的属性参数
	 * @param oldAttrId
	 *            旧的属性参数
	 */
	public void updataACPTypeConfig(int statisticsId, int newAttrId,
			int oldAttrId) {
		ACPStatisticsDao dao = new ACPStatisticsDao();
		dao.updataACPTypeConfig(statisticsId, newAttrId, oldAttrId);
	}
	
	
	/**
	 * 更新元数据模版
	 */
	public DwrResponseI18n editMetaModel(int id,int acpTypeId,String attrname,MetaPointLocatorVO locator){
	    DwrResponseI18n response = new DwrResponseI18n();
        DataPointVO dp = new DataPointVO();//数据点对象
		dp.setPointLocator(locator);
		AcpMetaVo acpMetaVo=new AcpMetaVo(id,acpTypeId,attrname,dp);
		validatePoint(acpMetaVo,response);
		if (!response.getHasMessages()) {
			AcpMetaDao acpMetaDao=new AcpMetaDao();
			if(id==Common.NEW_ID){
				//save
				acpMetaVo.setId(acpMetaDao.save(acpMetaVo));
			}
			else{
				//update
				acpMetaDao.update(acpMetaVo);
			}
			List<AcpMetaVo> points=acpMetaDao.findMetaPoints(acpTypeId);
			response.addData("metaPoint",points);
		 }
		return response;
	}
	
	public List<AcpMetaVo> getPointModels(int acpTypeId) {
		AcpMetaDao acpMetaDao=new AcpMetaDao();
		List<AcpMetaVo> points=acpMetaDao.findMetaPoints(acpTypeId);
		return points;
	}
	
	public DwrResponseI18n getPointModel(int pointId) {
	    return getPointModel(pointId, null);
	}
    private DwrResponseI18n getPointModel(int pointId, DataPointDefaulter defaulter) {
    	DwrResponseI18n response = new DwrResponseI18n();
        DataSourceVO<?> ds = new MetaDataSourceVO();
        DataPointVO  dp = new DataPointVO();
        AcpMetaVo meta;
        if (pointId == Common.NEW_ID) {
        	meta=new AcpMetaVo();
        	meta.setId(Common.NEW_ID);
           // dp.setDataSourceId(ds.getId());
            dp.setPointLocator(ds.createPointLocator());
            dp.setEventDetectors(new ArrayList<PointEventDetectorVO>(0));
            meta.setDp(dp);
            if (defaulter != null)
                defaulter.setDefaultValues(dp);
        }
	    else {
	    	meta=new AcpMetaDao().findMetaPoint(pointId);
	    }
        response.addData("meta",meta);
        return response;
    }
    private DwrResponseI18n validatePoint(AcpMetaVo acpMetaVo,DwrResponseI18n response ) {

         if (StringUtils.isLengthGreaterThan(acpMetaVo.getMetaName(), 20))
            response.addContextualMessage("acpMetaName", "validate.notLongerThan", 20);
         else if (StringUtils.isEmpty(acpMetaVo.getMetaName()))
            response.addContextualMessage("acpMetaName", "validate.required");
         if(acpMetaVo.getAcpTypeId()<1)
        	response.addContextualMessage("acpTypeId", "validate.acpTypeId.required");
         response.addData("metamodel",acpMetaVo);
        return response;
    }
    
	
	public DwrResponseI18n deletePointModels(int id) {
		DwrResponseI18n response = new DwrResponseI18n();
		AcpMetaDao acpMetaDao=new AcpMetaDao();
		acpMetaDao.delete(id);
		return response;
	}
}
