package com.serotonin.mango.rt.statistic;

import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.acp.ACPDao;
import com.serotonin.mango.db.dao.acp.CompressedAirSystemDao;
import com.serotonin.mango.db.dao.DataPointDao;
import com.serotonin.mango.db.dao.PointValueDao;
import com.serotonin.mango.db.dao.statistics.ScheduledStatisticDao;
import com.serotonin.mango.db.dao.statistics.StatisticsScriptDao;
import com.serotonin.mango.db.dao.statistics.StatisticsDao;
import com.serotonin.mango.vo.acp.ACPVO;
import com.serotonin.mango.vo.acp.ACPSystemVO;
import com.serotonin.mango.vo.statistics.StatisticsScriptVO;
import com.serotonin.mango.vo.statistics.StatisticsProgressVO;
import com.serotonin.mango.vo.statistics.StatisticsVO;
import com.serotonin.mango.vo.statistics.ScheduledStatisticVO;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import com.serotonin.mango.rt.statistic.StatisticsTask;
import java.util.logging.Logger;

/**
 * 脚本统计线程
 * @author 王金阳
 *
 */
public class ScriptStatisticsRT extends Thread{
	
	public static final Logger log = Logger.getLogger(ScriptStatisticsRT.class.toString());
	
	//定时统计时间间隔-一个小时
	public static final long STATISTIC_INTERVAL = 1000*60*60;
	//一个小时每个点应该采集到的值的个数
	public static final int  COUNT_IN_HOUR = 3*60;
	//要查找的时间没有记录，则返回-1
	public static final long NO_RECORD = -1L;
	//脚本类型为JavaScript脚本类型
	public static final String STATISTIC_SCRIPT_TYPE = "javascript";
	//JavaScript脚本方法名称
	public static final String STATISTIC_SCRIPT_FUNCTION_NAME = "getStatisticResult";
	//JavaScript脚本前缀
	public static final String STATISTIC_SCRIPT_PREFIX = " function "+STATISTIC_SCRIPT_FUNCTION_NAME+"(){ var result = 0; var count = "+COUNT_IN_HOUR+"; ";
	//JavaScript脚本后缀
	public static final String STATISTIC_SCRIPT_SUFFIX = " return result; } ";
	//返回那些数据延迟的点的ID时候作为字符串的前缀，来识别是统计结果还是数据点ID
	public static final String DATAPOINT_PREV = "DP_";
	//统计脚本
	private StatisticsScriptVO scriptVO;
	//统计时间(统计时间都是一个整点时刻，比如统计时间为1点整，则表示去统计0点到1点之间的数据)
	private long statisticTime;
	//构造函数，通过创建实例把要统计的脚本对象以及统计时间传递过来
	public ScriptStatisticsRT(StatisticsScriptVO scriptVO,long statisticTime){
		this.scriptVO = scriptVO;
		this.statisticTime = statisticTime;
	}
	
	@Override
	public void run() {
//		log.info("脚本："+scriptVO.getName()+"--开启一个线程进行统计");
		ACPDao acpDao = new ACPDao();
		ScheduledStatisticDao scheduledStatisticDao = new ScheduledStatisticDao();
		CompressedAirSystemDao acpSystemDao = new CompressedAirSystemDao();
		PointValueDao pointValueDao = new PointValueDao();
		//记录统计进度信息
		StatisticsProgressVO statisticsProgressVO = new StatisticsProgressVO();
		statisticsProgressVO.setRunning(true);////
		statisticsProgressVO.setScriptVO(scriptVO);////
		Common.ctx.getRuntimeManager().updateStatisticsProgress(statisticsProgressVO);
		if(scriptVO.getUnitType()==StatisticsScriptVO.UnitTypes.STATISTIC_UNIT_SYSTEM){
//			log.info("脚本："+scriptVO.getName()+"--是以系统为统计单位的");
			//以系统为统计单位，获取当前发现的所有系统
			List<ACPSystemVO> acpSystemList =  acpSystemDao.getAllACPSystem();
//			log.info("脚本："+scriptVO.getName()+"--有"+acpSystemList.size()+"个系统等待统计");
			int i=0;/////////
			//循环统计每个系统
			for(ACPSystemVO acpSystemVO:acpSystemList){
				i++;//////////
//				log.info("脚本："+scriptVO.getName()+"--正在系统第"+i+"个系统。它的名字是:"+acpSystemVO.getSystemname());
				statisticsProgressVO.setUnit(acpSystemVO.getSystemname());////
				long lastTime = scheduledStatisticDao.getLastestStatisticTimeBySciptAndUnit(scriptVO.getId(),acpSystemVO.getId());
				if(lastTime==NO_RECORD){
					//没有统计记录，就从用户选择的开始时间进行统计
					lastTime = scriptVO.getStartTime();
				}else{
					lastTime=lastTime+STATISTIC_INTERVAL;
				}
//				log.info("脚本："+scriptVO.getName()+"--正在统计第"+i+"个系统。它是从"+new Date(lastTime).toLocaleString()+"开始的");
				//对当前系统从上次统计时间lastTime到本次统计时间statisticTime逐个小时进行统计
				int j = 0;/////////////////////
				while(lastTime<=statisticTime){
					j++;///////////////////////
					statisticsProgressVO.setStatisticTime(lastTime);////
					//获取statisticTime前一个小时该系统的统计结果
					String result = getStatisticResult(acpSystemVO,lastTime);
					if(!result.substring(0,3).equals(DATAPOINT_PREV)){
						//统计成功则插入统计记录表中
						scheduledStatisticDao.save(new ScheduledStatisticVO(scriptVO.getId(),Double.parseDouble(result),lastTime,scriptVO.getUnitType(),acpSystemVO.getId()));
						//准备进行下个小时的统计
						lastTime+=STATISTIC_INTERVAL;
						j++;
					}else{
						Integer dpid = Integer.parseInt(result.substring(3));
						//dpid这个数据点数据延迟，查询这个点，在当前最后一次统计时间lastTime之后和统计时间statisticTime之前出现的最早的数据采集到的时间
						long nextTime = pointValueDao.getMinTimeByDpid(lastTime,statisticTime,dpid);
						if(nextTime==NO_RECORD){
							//之后一直都没有数据了，就继续下一台机器
							lastTime = statisticTime+1;
						}else{
							//从nextTime接下来的整点开始统计
							lastTime = StatisticsScriptVO.getNextExcuteTime(nextTime).getTime();
						}
					}
				}
//				log.info("脚本："+scriptVO.getName()+"--此系统完成"+j+"个小时的统计。它到"+new Date(statisticTime).toLocaleString()+"结束了");
			}
		}else if(scriptVO.getUnitType()==StatisticsScriptVO.UnitTypes.STATISTIC_UNIT_MACHINE){
//			log.info("脚本："+scriptVO.getName()+"--是以机器为统计单位的");
			//以机器为统计单位，获取当前发现的所有机器
			List<ACPVO> acpList = acpDao.getAllAcp();
//			log.info("脚本："+scriptVO.getName()+"--有"+acpList.size()+"个机器等待统计");
			int i=0;/////////
			//循环统计每个机器
			for(ACPVO acpVO:acpList){
				i++;//////////
//				log.info("脚本："+scriptVO.getName()+"--正在统计第"+i+"个机器。它的名字是:"+acpVO.getAcpname());
				statisticsProgressVO.setUnit(acpVO.getAcpname());////
				//获取该机器在此次统计之前最后一次被统计的时间
				long lastTime = scheduledStatisticDao.getLastestStatisticTimeBySciptAndUnit(scriptVO.getId(),acpVO.getId());
				if(lastTime==NO_RECORD){
					//没有统计记录，就从用户选择的开始时间进行统计
					lastTime = scriptVO.getStartTime();
				}else{
					lastTime=lastTime+STATISTIC_INTERVAL;
				}
//				log.info("脚本："+scriptVO.getName()+"--正在统计第"+i+"个机器。它是从"+new Date(lastTime).toLocaleString()+"开始的");
				//对当前机器从上次统计时间lastTime到本次统计时间statisticTime逐个小时进行统计
				int j = 0;/////////////////////
				while(lastTime<=statisticTime){
					statisticsProgressVO.setStatisticTime(lastTime);////
					//获取statisticTime前一个小时该机器的统计结果
					String result = getStatisticResult(acpVO,lastTime);
					if(!result.substring(0,3).equals(DATAPOINT_PREV)){
						//统计成功则插入统计记录表中
						scheduledStatisticDao.save(new ScheduledStatisticVO(scriptVO.getId(),Double.parseDouble(result),lastTime,scriptVO.getUnitType(),acpVO.getId()));
						//准备进行下个小时的统计
						lastTime+=STATISTIC_INTERVAL;
						j++;///////
					}else{
						Integer dpid = Integer.parseInt(result.substring(3));
						//dpid这个数据点数据延迟，查询这个点，在当前最后一次统计时间lastTime之后和统计时间statisticTime之前出现的最早的数据采集到的时间
						long nextTime = pointValueDao.getMinTimeByDpid(lastTime,statisticTime,dpid);
						if(nextTime==NO_RECORD){
							//之后一直都没有数据了，就继续下一台机器
							lastTime = statisticTime+1;
						}else{
							//从nextTime接下来的整点开始统计
							lastTime = StatisticsScriptVO.getNextExcuteTime(nextTime).getTime();
						}
					}
				}
//				log.info("脚本："+scriptVO.getName()+"--此机器完成"+j+"个小时的统计。它到"+new Date(statisticTime).toLocaleString()+"结束了");
			}
		}
		statisticsProgressVO.setStatisticTime(statisticTime);////
		statisticsProgressVO.setRunning(false);////
		//计划修改statisticTime变量的值来控制线程是否终止，当设置statisticTime值为-1l的时候，上面循环将不再继续，下面的代码不再执行。
		if(statisticTime!=NO_RECORD){
			//本次统计完成之后，拿结束时间和当前时间比较
			//获取上一个的整点时间
			long prev_hour = StatisticsScriptVO.getPrevExcuteTime().getTime();
			//创建一个定时器准备下一次统计
			Timer timer = new Timer();
			if(prev_hour-statisticTime>=STATISTIC_INTERVAL){//大于等于一个小时
				StatisticsTask task = new StatisticsTask(scriptVO,prev_hour);
				timer.schedule(task,0);
//				log.info("脚本："+scriptVO.getName()+"--完成了本次统计，由于距离上次统计时间已经大于一个小时了，所以马上开启另一个统计继续统计，知道赶上当前时间");
			}else{//小于一个小时
				//等到下个整点时刻开启一个线程统计之前一个小时的统计
				StatisticsTask task = new StatisticsTask(scriptVO,prev_hour+STATISTIC_INTERVAL);
				timer.schedule(task,prev_hour+STATISTIC_INTERVAL-new Date().getTime());
//				log.info("脚本："+scriptVO.getName()+"--完成了本次统计，那现在时间和上次时间一比较，哈哈，终于赶上当前时间了。我现在开启一个定时统计，等到"+new Date(prev_hour+STATISTIC_INTERVAL).toLocaleString()+"开启下个统计");
			}
		}else{
//			log.info("统计被用户手动终止了。");
		}
	}
	
	/**
	 * 获取统计结果
	 * @param acpVO 需要统计的机器信息
	 * @param statisticTime 统计时间
	 * @return 统计结果
	 */
	private String getStatisticResult(ACPVO acpVO,long statisticTime){
		String javaScript = initJavaScript(acpVO,statisticTime);
		if(javaScript.substring(0,3).equals(DATAPOINT_PREV)) return javaScript;
		return executeJavaScript(javaScript);
	}
	
	/**
	 * 获取统计结果
	 * @param acpVO 需要统计的系统信息
	 * @param statisticTime 统计时间
	 * @return 统计结果
	 */
	private String getStatisticResult(ACPSystemVO acpSystemVO,long statisticTime){
		String javaScript = initJavaScript(acpSystemVO,statisticTime);
		if(javaScript.substring(0,3).equals(DATAPOINT_PREV)) return javaScript;
		return executeJavaScript(javaScript);
	}
	
	/**
	 * 执行JavaScript脚本
	 * @param javaScript 脚本内容
	 * @return 脚本计算结果
	 */
	private String executeJavaScript(String javaScript){
		//计算结果
		String result = null;
		//获取脚本管理对象
		ScriptEngineManager manager = new ScriptEngineManager();
		//设置脚本类型为javascript类型
		ScriptEngine engine = manager.getEngineByName(STATISTIC_SCRIPT_TYPE);
		try {
			//加载javascript
			engine.eval(javaScript);
			//解析javascript
			Invocable inv = (Invocable) engine;
			//计算脚本结果
			result = ((Double)inv.invokeFunction(STATISTIC_SCRIPT_FUNCTION_NAME)).toString();
		} catch (ScriptException e) {
			e.printStackTrace(); 
		} catch(NoSuchMethodException e) {
			e.printStackTrace();
		} 
		return result;
	}
	
	/**
	 * 初始化机器acpVO在statisticTime前一个小时的脚本信息
	 * @param acpVO 机器信息
	 * @param statisticTime 统计时间
	 * @return 脚本内容
	 */
	private String initJavaScript(ACPVO acpVO,long statisticTime){
		StatisticsDao statisticsDao = new StatisticsDao();
		PointValueDao pointValueDao = new PointValueDao();
		DataPointDao dataPointDao = new DataPointDao();
		//存放本次统计JavaScript中需要的所有变量以及值的字符串形式
		StringBuffer vars = new StringBuffer();
		//存放JavaScript中一个变量及其值得字符串形式
		StringBuffer var = new StringBuffer();
		//获取脚本所用到的统计参数
		List<StatisticsVO> acpParams = statisticsDao.getStatisticsParamByScript(scriptVO.getStatisticParamIds());
		//循环参数获取该参数对应的空压机系统指定时间内的数据点的值的数组
		for (StatisticsVO vo : acpParams) {
			/******************************************************************
			 * 	生成JavaScript脚本需要的变量的字符串形式						  *
			 * 			 													  *
			 * 			var A65 = new Array(12.56,89.02); 					  *
			 *****************************************************************/
			var.append(" var ");
			var.append(StatisticsScriptVO.ACP_PARAM_PREFIX);
			var.append(vo.getId());
			var.append(" = ");
			//获取该统计参数在该系统下对应的点
			int dpid = dataPointDao.getPointFromAcpMember(acpVO.getId(),vo.getId());
			//根据数据点查找该点在statisticTime之前的一个小时内的值的集合
			List<Double> values = pointValueDao.getValuesByPointInTimeStamp(dpid, statisticTime, STATISTIC_INTERVAL);
			if(values==null){
				//没有发现采集到数据点，则返回null
				return DATAPOINT_PREV+dpid;
			}else{
				//一个小时内的点的值的个数跟预计的不同
				if (values.size()!=COUNT_IN_HOUR) {
					//不再继续操作,返回null
					return DATAPOINT_PREV+dpid;
				}
			}
			var.append("new Array(");
			for (int i = 0; i < values.size(); i++) {
				var.append(values.get(i));
				if (i + 1 < values.size()) {
					var.append(",");
				}
			}
			var.append(");");
			vars.append(var);
		}
		//存放脚本内容
		StringBuffer javaScript = new StringBuffer();
		//脚本加上前缀
		javaScript.append(STATISTIC_SCRIPT_PREFIX);
		//脚本加上初始化变量
		javaScript.append(vars);
		//脚本加入用户逻辑代码
		javaScript.append(scriptVO.getConditionText());
		//脚本加上后缀
		javaScript.append(STATISTIC_SCRIPT_SUFFIX);
		return javaScript.toString();
	}
	
	/**
	 * 初始化系统acpSystemVO在statisticTime前一个小时的脚本信息
	 * @param acpSystemVO 系统信息
	 * @param statisticTime 统计时间
	 * @return 脚本内容
	 */
	private String initJavaScript(ACPSystemVO acpSystemVO,long statisticTime){
		StatisticsDao statisticsDao = new StatisticsDao();
		PointValueDao pointValueDao = new PointValueDao();
		DataPointDao dataPointDao = new DataPointDao();
		//存放本次统计JavaScript中需要的所有变量以及值的字符串形式
		StringBuffer vars = new StringBuffer();
		//存放JavaScript中一个变量及其值得字符串形式
		StringBuffer var = new StringBuffer();
		//获取脚本所用到的统计参数
		List<StatisticsVO> acpSystemParams = statisticsDao.getStatisticsParamByScript(scriptVO.getStatisticParamIds());
		//循环参数获取该参数对应的空压机系统指定时间内的数据点的值的数组
		for (StatisticsVO vo : acpSystemParams) {
			/******************************************************************
			 * 	生成JavaScript脚本需要的变量的字符串形式						  *
			 * 			 													  *
			 * 			var S95 = new Array(59.36,12.85); 					  *
			 *****************************************************************/
			var.append(" var ");
			var.append(StatisticsScriptVO.ACPSYSTEM_PARAM_PREFIX);
			var.append(vo.getId());
			var.append(" = ");
			//获取该统计参数在该系统下对应的点
			int dpid = dataPointDao.getPointFromAcpSystemMember(acpSystemVO.getId(),vo.getId());
			//根据数据点查找该点在statisticTime之前的一个小时内的值的集合
			List<Double> values = pointValueDao.getValuesByPointInTimeStamp(dpid, statisticTime, STATISTIC_INTERVAL);
			if(values==null){
				//没有发现采集到数据点，则返回null
				return DATAPOINT_PREV+dpid;
			}else{
				//一个小时内的点的值的个数跟预计的不同
				if (values.size()!=COUNT_IN_HOUR) {
					//不再继续操作,返回null
					return DATAPOINT_PREV+dpid;
				}
			}
			var.append("new Array(");
			for (int i = 0; i < values.size(); i++) {
				var.append(values.get(i));
				if (i + 1 < values.size()) {
					var.append(",");
				}
			}
			var.append(");");
			vars.append(var);
		}
		//存放脚本内容
		StringBuffer javaScript = new StringBuffer();
		//脚本加上前缀
		javaScript.append(STATISTIC_SCRIPT_PREFIX);
		//脚本加上初始化变量
		javaScript.append(vars);
		//脚本加入用户逻辑代码
		javaScript.append(scriptVO.getConditionText());
		//脚本加上后缀
		javaScript.append(STATISTIC_SCRIPT_SUFFIX);
		return javaScript.toString();
	}
	
	/**
	 * 停止统计
	 */
	public void shutdown(){
		setStatisticTime(NO_RECORD);
	}
	
	
	/**
	 * 测试脚本
	 * @return 返回测试结果
	 */
	public static Double getTestValue(StatisticsScriptVO scriptVO) throws ScriptException,NoSuchMethodException{
		StatisticsDao statisticsDao = new StatisticsDao();
		//******************************************为统计准备数据******************************************************/
		StringBuffer vars = new StringBuffer();
		StringBuffer var = new StringBuffer();
		Random random = new Random();
		List<StatisticsVO> params = statisticsDao.getStatisticsParamByScript(scriptVO.getStatisticParamIds());
		for (StatisticsVO vo : params) {
			var.append(" var ");
			var.append(scriptVO.getUnitType()==StatisticsScriptVO.UnitTypes.STATISTIC_UNIT_MACHINE?StatisticsScriptVO.ACP_PARAM_PREFIX:StatisticsScriptVO.ACPSYSTEM_PARAM_PREFIX);
			var.append(vo.getId());
			var.append(" = ");
			var.append("new Array(");
			for (int i = 0; i < 180; i++) {
				var.append(random.nextInt(100)+random.nextDouble());
				if (i + 1 < 180) {
					var.append(",");
				}
			}
			var.append(");");
			vars.append(var);
		}
		//*****************************************拼接脚本字符串******************************************************/
		StringBuffer javaScript = new StringBuffer();
		javaScript.append(STATISTIC_SCRIPT_PREFIX);
		javaScript.append(vars);
		javaScript.append(scriptVO.getConditionText());
		javaScript.append(STATISTIC_SCRIPT_SUFFIX);
		//*************************************执行脚本得出结果********************************************************/
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName(STATISTIC_SCRIPT_TYPE);
		engine.eval(javaScript.toString());
		Invocable inv = (Invocable) engine;
		Double result = (Double)inv.invokeFunction(STATISTIC_SCRIPT_FUNCTION_NAME);
		return result;
	}
	
	
	

	public StatisticsScriptVO getScriptVO() {
		return scriptVO;
	}

	public void setScriptVO(StatisticsScriptVO scriptVO) {
		this.scriptVO = scriptVO;
	}

	public long getStatisticTime() {
		return statisticTime;
	}

	public void setStatisticTime(long statisticTime) {
		this.statisticTime = statisticTime;
	}
}
