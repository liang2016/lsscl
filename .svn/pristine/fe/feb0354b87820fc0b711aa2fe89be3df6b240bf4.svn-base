/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.dwr;

import java.util.List;
import com.serotonin.mango.Common;
import com.serotonin.mango.vo.User;
import com.serotonin.mango.vo.permission.Permissions;
import com.serotonin.util.StringUtils;
import com.serotonin.web.dwr.DwrResponseI18n;
import com.serotonin.web.i18n.LocalizableMessage;
import com.serotonin.mango.db.dao.statistics.StatisticsScriptDao;
import com.serotonin.mango.db.dao.PointValueDao;
import com.serotonin.mango.vo.statistics.StatisticsScriptVO;
import com.serotonin.mango.vo.statistics.StatisticsVO;
import com.serotonin.mango.vo.statistics.StatisticsProgressVO;
import com.serotonin.mango.db.dao.statistics.StatisticsDao;
import javax.script.ScriptException;
import com.serotonin.mango.db.dao.statistics.ScheduledStatisticDao;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.serotonin.mango.rt.statistic.ScriptStatisticsRT;



/**
 * 统计脚本页面DWR
 * @author 王金阳
 *
 */
public class StatisticsScriptDwr extends BaseDwr {
	
	/**
	 * 获取初始化需要的数据
	 * @return model(数据封装到model中传递)
	 */
    public DwrResponseI18n init() {
    	User user = Common.getUser();
    	Permissions.hasStatisticsScriptPermission(user);
    	DwrResponseI18n response = new DwrResponseI18n();
        StatisticsScriptDao scriptDao = new StatisticsScriptDao();
        StatisticsDao statisticsDao = new StatisticsDao();
        //脚本列表
        response.addData("ssptlist",scriptDao.findAll());
        //机器统计参数列表
        response.addData("acpParams",statisticsDao.getSystemStatistics(StatisticsVO.UseTypes.USE_ACP));
        //系统统计参数列表
        response.addData("acpSystemParams",statisticsDao.getSystemStatistics(StatisticsVO.UseTypes.USE_ACPSYSTEM));
        return response;
    }

    /**
     * 根据ID查找一个统计脚本详情
     * @param id 统计脚本ID
     * @return 统计脚本详情
     */
    public DwrResponseI18n getStatisticsScriptById(int id) {
    	User user = Common.getUser();
    	Permissions.hasStatisticsScriptPermission(user);
    	DwrResponseI18n response = new DwrResponseI18n();
        StatisticsScriptDao scriptDao = new StatisticsScriptDao();
    	StatisticsScriptVO statisticsScript = scriptDao.findById(id);
    	//获取当前脚本信息
    	response.addData("scriptVO",statisticsScript);
    	//获取当前脚本进度信息是
    	response.addData("statisticsProgresses",Common.ctx.getRuntimeManager().getStatisticsProgresses());
    	return response;
    }

    /**
     * 保存一个统计脚本
     * @param id 编号
     * @param xid 输出编号
     * @param name 脚本名称
     * @param disabled 是否禁止
     * @param conditionText 脚本内容
     * @param int 
     * @return 添加之后刷新页面
     */
    public DwrResponseI18n saveStatisticsScript(int id,String xid, String name,boolean disabled,String conditionText,String startTimeStr){
    	User user = Common.getUser();
        Permissions.hasStatisticsScriptPermission(user);
        StatisticsScriptDao scriptDao = new StatisticsScriptDao();
        DwrResponseI18n response = new DwrResponseI18n();
        // 验证xid是否为空
        if(StringUtils.isEmpty(xid)){
        	response.addContextualMessage("xid", "validate.required");
        }
        // 验证脚本名字为空
        if(StringUtils.isEmpty(name)){
        	response.addContextualMessage("name", "validate.required");
        }
        // 输入日期整理并验证
        long startTime = -1L;
        if(startTimeStr.equals("-1")){
        	//最早有点被采集到值的时候及开始时间
        	startTime = StatisticsScriptVO.getNextExcuteTime(new PointValueDao().getEarliestTime()).getTime();
        }else if(startTimeStr.equals("0")){
        	//从现在开始下一个整点统计
        	startTime = StatisticsScriptVO.getNextExcuteTime().getTime();
        }else if(startTimeStr.equals("2")){
        	//修改脚本，开始时间不变
        }else{
        	//从指定时间
        	if((startTime = isLegalDate(startTimeStr))==-1){
            	response.addContextualMessage("startTime", "statistic.script.startTime.error");
            }
        } 
        StatisticsScriptVO scriptVO = new StatisticsScriptVO(xid, name, disabled,conditionText,startTime);
        // 验证脚本是否为空
        if(StringUtils.isEmpty(conditionText)){
        	response.addContextualMessage("conditionText", "validate.required");
        }else{
            if(scriptVO.getUnitType()==StatisticsScriptVO.UnitTypes.STATISTIC_UNIT_NONE){
            	//没有选择机器统计参数，也没有选择系统统计参数
            	response.addContextualMessage("conditionText", "statistic.condition.unit.null");
            }else if(scriptVO.getUnitType()==StatisticsScriptVO.UnitTypes.STATISTIC_UNIT_DOUBLE){
            	//既选择了机器统计参数，也选择了系统统计参数
            	response.addContextualMessage("conditionText", "statistic.condition.unit.unique");
            }
        }
        int newId = id;
        if(id==-1){//新增的行
        	if(scriptDao.isUniqueXid(xid)==false){ 
            	response.addContextualMessage("xid", "validate.xidUsed");
            }
        	if (!response.getHasMessages()){
        		newId = Common.ctx.getRuntimeManager().saveStatisticsScript(scriptVO);
        		response.addData("startTime",scriptVO.getStartTime());
        	}
        }else{//更新的行
        	 if (!response.getHasMessages()){
        		 scriptVO = new StatisticsScriptVO(id,xid, name, disabled); 
        		 Common.ctx.getRuntimeManager().updateStatisticsScript(scriptVO);
        	 }
        }
        response.addData("ssptlist",scriptDao.findAll());
        response.addData("newId",newId);
        return response;
    }
    
    /**
     * 根据ID删除一个统计脚本
     * @param id ID
     */
    public DwrResponseI18n deleteStatisticsScriptById(int id) {
    	User user = Common.getUser();
        Permissions.hasStatisticsScriptPermission(user);
        StatisticsScriptDao scriptDao = new StatisticsScriptDao();
        scriptDao.delete(id);
        DwrResponseI18n response = new DwrResponseI18n();
        response.addData("ssptlist",scriptDao.findAll());
        return response;
    }
    
    /**
     * 获取唯一的xid
     * @return xid
     */
    public String getUniqueXid(){
    	return new StatisticsScriptDao().getUniqueXid();
    }
    

    /**
     * 对表达是的验证 
     * @param condition 脚本表达式
     * @return 范围验证信息
     */
    public DwrResponseI18n validateCondition(String condition) {
    	StatisticsScriptVO scriptVO = new StatisticsScriptVO();
    	scriptVO.setConditionText(condition);
    	User user = Common.getUser();
        Permissions.hasStatisticsScriptPermission(user);
        DwrResponseI18n response = new DwrResponseI18n();
        LocalizableMessage message = null;
        if(scriptVO.getUnitType()==StatisticsScriptVO.UnitTypes.STATISTIC_UNIT_NONE){
        	response.addContextualMessage("conditionText", "statistic.condition.unit.null");
        }else if(scriptVO.getUnitType()==StatisticsScriptVO.UnitTypes.STATISTIC_UNIT_DOUBLE){
        	response.addContextualMessage("conditionText", "statistic.condition.unit.unique");
        }else{
        	try{
        		Double result = ScriptStatisticsRT.getTestValue(scriptVO);
        		response.addData("value",result);
        	}catch(ScriptException e){
        		response.addData("scriptError",formatScriptMessage(e.getMessage()));
        	}catch(NoSuchMethodException e){
        		response.addData("scriptError",formatScriptMessage(e.getMessage()));
        	}
        }
        return response;
    }
    
    /**
     * 获取当前运行的所有统计相关的线程
     * @return
     */
    public List<StatisticsProgressVO> getAllStatisticProgress(){
    	User user = Common.getUser();
        Permissions.hasStatisticsScriptPermission(user);
		return Common.ctx.getRuntimeManager().getStatisticsProgresses();
    }
    
    
    /**
     * 格式化javascript异常信息
     * @param message 异常信息
     * @return 格式化后的异常信息
     */
    private String formatScriptMessage(String message){
    	
    	return message; 
    }
    
    
    /**
     * 判断输入的日期是否是合法的
     * @param date 日期字符串形式
     * @return 日期的毫秒数
     */
    private long isLegalDate(String date){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	try {
			return StatisticsScriptVO.getPrevExcuteTime(sdf.parse(date).getTime()).getTime();
		} catch (ParseException e) {
			return -1l;
		}
    }
    
}
