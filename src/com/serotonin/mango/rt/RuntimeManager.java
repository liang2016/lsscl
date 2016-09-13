/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt;

import com.serotonin.mango.rt.statistic.StatisticsTask;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.Set;
import java.util.TimerTask;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import com.serotonin.ShouldNeverHappenException;
import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.CompoundEventDetectorDao;
import com.serotonin.mango.db.dao.DataPointDao;
import com.serotonin.mango.db.dao.DataSourceDao;
import com.serotonin.mango.db.dao.MaintenanceEventDao;
import com.serotonin.mango.db.dao.PointLinkDao;
import com.serotonin.mango.db.dao.PointValueDao;
import com.serotonin.mango.db.dao.PublisherDao;
import com.serotonin.mango.db.dao.ScheduledEventDao;
import com.serotonin.mango.rt.dataImage.DataPointEventMulticaster;
import com.serotonin.mango.rt.dataImage.DataPointListener;
import com.serotonin.mango.rt.dataImage.DataPointRT;
import com.serotonin.mango.rt.dataImage.PointValueTime;
import com.serotonin.mango.rt.dataImage.SetPointSource;
import com.serotonin.mango.rt.dataImage.types.MangoValue;
import com.serotonin.mango.rt.dataSource.DataSourceRT;
import com.serotonin.mango.rt.dataSource.meta.MetaDataSourceRT;
import com.serotonin.mango.rt.event.SimpleEventDetector;
import com.serotonin.mango.rt.event.compound.CompoundEventDetectorRT;
import com.serotonin.mango.rt.event.detectors.PointEventDetectorRT;
import com.serotonin.mango.rt.event.maintenance.MaintenanceEventRT;
import com.serotonin.mango.rt.event.schedule.ScheduledEventRT;
import com.serotonin.mango.rt.link.PointLinkRT;
import com.serotonin.mango.db.dao.statistics.StatisticsScriptDao;
import com.serotonin.mango.vo.statistics.StatisticsScriptVO;
import com.serotonin.mango.vo.statistics.StatisticsProgressVO;
import com.serotonin.mango.rt.publish.PublisherRT;
import com.serotonin.mango.rt.statistic.ScriptStatisticsRT;
import com.serotonin.mango.util.DateUtils;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.mango.vo.dataSource.DataSourceVO;
import com.serotonin.mango.vo.event.CompoundEventDetectorVO;
import com.serotonin.mango.vo.event.MaintenanceEventVO;
import com.serotonin.mango.vo.event.PointEventDetectorVO;
import com.serotonin.mango.vo.event.ScheduledEventVO;
import com.serotonin.mango.vo.link.PointLinkVO;
import com.serotonin.mango.vo.publish.PublishedPointVO;
import com.serotonin.mango.vo.publish.PublisherVO;
import com.serotonin.util.LifecycleException;
import com.serotonin.web.i18n.LocalizableException;
import com.serotonin.web.i18n.LocalizableMessage;
import com.serotonin.mango.db.dao.statistics.ScheduledStatisticDao;
import com.serotonin.mango.rt.statistic.common.EnergSavingTargetNo1RT;
import com.serotonin.mango.rt.statistic.common.EnergSavingTargetNo2RT;
import com.serotonin.mango.rt.statistic.common.EnergSavingTargetNo3RT;
import com.serotonin.mango.rt.statistic.common.HealthIndexRT;
import com.serotonin.mango.rt.statistic.common.TroubleHandleRateRT;
import com.serotonin.mango.rt.statistic.common.EnergSavingIndexRT;
import com.serotonin.mango.db.dao.PointValueDao;
import com.serotonin.mango.db.dao.DataPointDao;
import com.serotonin.modbus4j.code.RegisterRange;
import com.serotonin.mango.vo.dataSource.modbus.ModbusPointLocatorVO;
public class RuntimeManager {
	private static final Log LOG = LogFactory.getLog(RuntimeManager.class);

	private final List<DataSourceRT> runningDataSources = new CopyOnWriteArrayList<DataSourceRT>();

	/**
	 * Provides a quick lookup map of the running data points.
	 */
	private final Map<Integer, DataPointRT> dataPoints = new ConcurrentHashMap<Integer, DataPointRT>();

	/**
	 * The list of point listeners, kept here such that listeners can be
	 * notified of point initializations (i.e. a listener can register itself
	 * before the point is enabled).
	 */
	private final Map<Integer, DataPointListener> dataPointListeners = new ConcurrentHashMap<Integer, DataPointListener>();

	/**
	 * Store of enabled event detectors.
	 */
	private final Map<String, SimpleEventDetector> simpleEventDetectors = new ConcurrentHashMap<String, SimpleEventDetector>();

	/**
	 * Store of enabled compound event detectors.
	 */
	private final Map<Integer, CompoundEventDetectorRT> compoundEventDetectors = new ConcurrentHashMap<Integer, CompoundEventDetectorRT>();

	/**
	 * Store of enabled publishers
	 */
	private final List<PublisherRT<?>> runningPublishers = new CopyOnWriteArrayList<PublisherRT<?>>();

	/**
	 * Store of enabled point links
	 */
	private final List<PointLinkRT> pointLinks = new CopyOnWriteArrayList<PointLinkRT>();

	/**
	 * Store of maintenance events
	 */
	private final List<MaintenanceEventRT> maintenanceEvents = new CopyOnWriteArrayList<MaintenanceEventRT>();

	private boolean started = false;
	/**
	 * Store of statistics process
	 */
	private final TreeMap<Integer, StatisticsProgressVO> statisticsProgresses = new TreeMap<Integer, StatisticsProgressVO>();
	/**
	 * Sotre of statistics thread
	 */
	private final HashMap<Integer, ScriptStatisticsRT> statisticsThreads = new HashMap<Integer, ScriptStatisticsRT>();

	//
	// Lifecycle
	synchronized public void initialize(boolean safe) {
		if (started)
			throw new ShouldNeverHappenException(
					"RuntimeManager already started");

		// Set the started indicator to true.
		started = true;

		// Initialize data sources that are enabled.
		//FIXME 初始化数据源
		DataSourceDao dataSourceDao = new DataSourceDao();
		DataSourceVO<?>tcpDataSource = dataSourceDao.getDataSource(-1);
		
		List<DataSourceVO<?>> configs = /*dataSourceDao.getDataSources()*/ new ArrayList<DataSourceVO<?>>();
//		configs.add(tcpDataSource);
		List<DataSourceVO<?>> pollingRound = new ArrayList<DataSourceVO<?>>();
		for (DataSourceVO<?> config : configs) {
			if (config.isEnabled()) {
				if (safe) {
					config.setEnabled(false);
					dataSourceDao.saveDataSource(config);
				} else if (initializeDataSource(config))
					pollingRound.add(config);
			}
		}

		// Set up point links.
		PointLinkDao pointLinkDao = new PointLinkDao();
		for (PointLinkVO vo : pointLinkDao.getPointLinks()) {
			if (!vo.isDisabled()) {
				if (safe) {
					vo.setDisabled(true);
					pointLinkDao.savePointLink(vo);
				} else
					startPointLink(vo);
			}
		}

		// Tell the data sources to start polling. Delaying the polling start
		// gives the data points a chance to
		// initialize such that point listeners in meta points and set point
		// handlers can run properly.
		for (DataSourceVO<?> config : pollingRound)
			startDataSourcePolling(config);

		// Initialize the scheduled events.
		ScheduledEventDao scheduledEventDao = new ScheduledEventDao();
		List<ScheduledEventVO> scheduledEvents = scheduledEventDao
				.getScheduledEvents();
		for (ScheduledEventVO se : scheduledEvents) {
			if (!se.isDisabled()) {
				if (safe) {
					se.setDisabled(true);
					scheduledEventDao.saveScheduledEvent(se);
				} else
					startScheduledEvent(se);
			}
		}

		// Initialize the compound events.
		CompoundEventDetectorDao compoundEventDetectorDao = new CompoundEventDetectorDao();
		List<CompoundEventDetectorVO> compoundDetectors = compoundEventDetectorDao
				.getCompoundEventDetectors();
		for (CompoundEventDetectorVO ced : compoundDetectors) {
			if (!ced.isDisabled()) {
				if (safe) {
					ced.setDisabled(true);
					compoundEventDetectorDao.saveCompoundEventDetector(ced);
				} else
					startCompoundEventDetector(ced);
			}
		}

		// Start the publishers that are enabled
		PublisherDao publisherDao = new PublisherDao();
		List<PublisherVO<? extends PublishedPointVO>> publishers = publisherDao
				.getPublishers();
		for (PublisherVO<? extends PublishedPointVO> vo : publishers) {
			if (vo.isEnabled()) {
				if (safe) {
					vo.setEnabled(false);
					publisherDao.savePublisher(vo);
				} else
					startPublisher(vo);
			}
		}

		// // Start the maintenance events that are enabled
		// MaintenanceEventDao maintenanceEventDao = new MaintenanceEventDao();
		// for (MaintenanceEventVO vo :
		// maintenanceEventDao.getMaintenanceEvents()) {
		// if (!vo.isDisabled()) {
		// if (safe) {
		// vo.setDisabled(true);
		// maintenanceEventDao.saveMaintenanceEvent(vo);
		// }
		// else
		// startMaintenanceEvent(vo);
		// }
		// }

		 // Start the statistics
//		 StatisticsScriptDao scriptDao = new StatisticsScriptDao();
//		 ScheduledStatisticDao scheduledStatisticDao = new
//		 ScheduledStatisticDao();
//		 // 获取所有的脚本并进行循环
//		 List<StatisticsScriptVO> scriptVOList = scriptDao.findAll();
//		 // 上一个整点时间,开始这个时间之前的统计
//		 long statisticTime =
//		 StatisticsScriptVO.getPrevExcuteTime().getTime();
//		 for(StatisticsScriptVO scriptVO:scriptVOList){
//		 // 如果当前脚本没有被禁用
//		 startScriptStatistics(scriptVO,statisticTime);
//		 }
//
//		startCommonStatistics();

	}

	/**
	 * 每运行一个常用统计线程，就会被记录到该变量中
	 */
	List<Thread> commonStatisticThreads = new ArrayList<Thread>();

	private static Map<Long, Integer> readyStartIndex = new HashMap<Long, Integer>();

	public static void completedOneTarget(long endTime) {
		Integer completedCount = readyStartIndex.get(endTime);
		if (completedCount == null) {
			completedCount = 1;
		} else {
			completedCount++;
		}
		readyStartIndex.put(endTime, completedCount);
	}

	public static void completedIndex(long endTime) {
		readyStartIndex.remove(endTime);
	}

	public static boolean indexCanStatistics(long time) {
		Integer completedCount = readyStartIndex.get(time);
		if (completedCount ==3)//统计3个指数
			return true;
		else
			return false;
	}

	public static void indexStatistics() {
		/**
		 * 开启 节能指数 统计线程
		 */
		EnergSavingIndexRT energSavingIndexRT = new EnergSavingIndexRT();
		Thread energSavingIndexRTHandler = new Thread(energSavingIndexRT);
		energSavingIndexRTHandler.start();
	}

	/**
	 * 启动所有常用统计线程
	 */
	public void startCommonStatistics() {
		/**
		 * 开启节能指标一统计线程
		 */
		EnergSavingTargetNo1RT energSavingTargetNo1RT = new EnergSavingTargetNo1RT();
		Thread energSavingTargetNo1RTHandler = new Thread(
				energSavingTargetNo1RT);
		commonStatisticThreads.add(energSavingTargetNo1RT);
		energSavingTargetNo1RTHandler.start();

		/**
		 * 开启节能指标二统计线程
		 */
		EnergSavingTargetNo2RT energSavingTargetNo2RT = new EnergSavingTargetNo2RT();
		Thread energSavingTargetNo2RTHandler = new Thread(
				energSavingTargetNo2RT);
		commonStatisticThreads.add(energSavingTargetNo2RT);
		energSavingTargetNo2RTHandler.start();

		/**
		 * 开启节能指标三统计线程
		 */
		EnergSavingTargetNo3RT energSavingTargetNo3RT = new EnergSavingTargetNo3RT();
		Thread energSavingTargetNo3RTHandler = new Thread(
				energSavingTargetNo3RT);
		commonStatisticThreads.add(energSavingTargetNo3RT);
		energSavingTargetNo3RTHandler.start();

//		/**
//		 * 开启节能指标四统计线程
//		 */
//		EnergSavingTargetNo4RT energSavingTargetNo4RT = new EnergSavingTargetNo4RT();
//		Thread energSavingTargetNo4RTHandler = new Thread(
//				energSavingTargetNo4RT);
//		commonStatisticThreads.add(energSavingTargetNo4RT);
//		energSavingTargetNo4RTHandler.start();
//
//		/**
//		 * 开启节能指标五统计线程
//		 */
//		EnergSavingTargetNo5RT energSavingTargetNo5RT = new EnergSavingTargetNo5RT();
//		Thread energSavingTargetNo5RTHandler = new Thread(
//				energSavingTargetNo5RT);
//		commonStatisticThreads.add(energSavingTargetNo5RT);
//		energSavingTargetNo5RTHandler.start();

		/**
		 * 开启健康指数统计线程
		 */
		HealthIndexRT healthIndexRT = new HealthIndexRT();
		Thread healthIndexRTHandler = new Thread(healthIndexRT);
		commonStatisticThreads.add(healthIndexRT);
		healthIndexRTHandler.start();

		/**
		 * 开启 故障处理率 统计线程
		 */
		TroubleHandleRateRT troubleHandleRateRT = new TroubleHandleRateRT();
		Thread troubleHandleRateRTHandler = new Thread(troubleHandleRateRT);
		commonStatisticThreads.add(troubleHandleRateRT);
		troubleHandleRateRTHandler.start();

		//    	
		// /**
		// * 开启 节能指数 统计线程
		// */
		// EnergSavingIndexRT energSavingIndexRT = new EnergSavingIndexRT();
		// Thread energSavingIndexRTHandler = new Thread(energSavingIndexRT);
		// commonStatisticThreads.add(energSavingIndexRT);
		// energSavingIndexRTHandler.start();

		/**
		 * waiting...
		 */
	}

	/**
	 * 开启一个线程对脚本进行统计
	 * 
	 * @param scriptVO
	 *            统计脚本
	 * @param statisticTime
	 *            统计时间
	 */
	public void startScriptStatistics(StatisticsScriptVO scriptVO,
			long statisticTime) {
		// 加入 对 正在等待的脚本进行禁用的处理，得到它再次开始的时候再去判断
		StatisticsScriptVO currentScriptVO = new StatisticsScriptDao()
				.findById(scriptVO.getId());
		if (!currentScriptVO.isDisabled()) {
			if (scriptVO.getStartTime() > statisticTime) {
				// 对于未来的开启一个定时器，到统计时间执行
				Timer timer = new Timer();
				StatisticsTask task = new StatisticsTask(scriptVO, scriptVO
						.getStartTime());
				timer.schedule(task, scriptVO.getStartTime()
						- new Date().getTime());
			} else {
				ScriptStatisticsRT scriptStatisticsRT = new ScriptStatisticsRT(
						scriptVO, statisticTime);
				// 将线程放进HashMap中管理
				statisticsThreads.put(scriptVO.getId(), scriptStatisticsRT);
				Thread thread = new Thread(scriptStatisticsRT);
				thread.start();
			}
		} else {
			stopScriptStatistics(scriptVO);
		}
	}

	/**
	 * 关闭一个脚本的统计
	 * 
	 * @param scriptVO
	 *            脚本信息
	 */
	private void stopScriptStatistics(StatisticsScriptVO scriptVO) {
		if (statisticsThreads.get(scriptVO.getId()) != null) {
			statisticsThreads.get(scriptVO.getId()).shutdown();
			statisticsThreads.remove(scriptVO.getId());
		}
	}

	/**
	 * 保存一个脚本
	 * 
	 * @param vo
	 *            脚本信息
	 * @return 新的ID
	 */
	public int saveStatisticsScript(StatisticsScriptVO scriptVO) {
		int newId = new StatisticsScriptDao().save(scriptVO);
		scriptVO.setId(newId);
		// 上一个整点时间,开始这个时间之前的统计
		long statisticTime = StatisticsScriptVO.getPrevExcuteTime().getTime();
		startScriptStatistics(scriptVO, statisticTime);
		return newId;
	}

	/**
	 * 更新一个脚本
	 * 
	 * @param scriptVO
	 *            脚本信息
	 * @return 脚本ID
	 */
	public int updateStatisticsScript(StatisticsScriptVO scriptVO) {
		StatisticsScriptDao scriptDao = new StatisticsScriptDao();
		scriptDao.update(scriptVO);
		StatisticsScriptVO updated = scriptDao.findById(scriptVO.getId());
		long statisticTime = StatisticsScriptVO.getPrevExcuteTime().getTime();
		if (updated.isDisabled()) {
			stopScriptStatistics(updated);
		} else {
			if (statisticsThreads.get(updated.getId()) == null) {
				startScriptStatistics(updated, statisticTime);
			}
		}
		return scriptVO.getId();
	}

	/**
	 * 更新统计进度信息
	 * 
	 * @param statisticsProgressVO
	 *            某个脚本的统计进度信息
	 */
	public void updateStatisticsProgress(
			StatisticsProgressVO statisticsProgressVO) {
		synchronized (statisticsProgresses) {
			statisticsProgresses.put(
					statisticsProgressVO.getScriptVO().getId(),
					statisticsProgressVO);
		}
	}

	/**
	 * 获取所有脚本的统计进度
	 * 
	 * @return 进度列表
	 */
	public List<StatisticsProgressVO> getStatisticsProgresses() {
		List<StatisticsProgressVO> progressList = new ArrayList<StatisticsProgressVO>();
		synchronized (statisticsProgresses) {
			Iterator<Integer> it = statisticsProgresses.keySet().iterator();
			while (it.hasNext()) {
				progressList.add(statisticsProgresses.get(it.next()));
			}
		}
		return progressList;
	}

	synchronized public void terminate() {
		if (!started)
			throw new ShouldNeverHappenException(
					"RuntimeManager not yet started");

		started = false;
		for (MaintenanceEventRT me : maintenanceEvents)
			// stopMaintenanceEvent(me.getVo().getId());

			for (PublisherRT<? extends PublishedPointVO> publisher : runningPublishers)
				stopPublisher(publisher.getId());

		for (Integer id : compoundEventDetectors.keySet())
			stopCompoundEventDetector(id);

		for (PointLinkRT pointLink : pointLinks)
			stopPointLink(pointLink.getId());

		// First stop meta data sources.
		for (DataSourceRT dataSource : runningDataSources) {
			if (dataSource instanceof MetaDataSourceRT)
				stopDataSource(dataSource.getId());
		}
		// Then stop everything else.
		for (DataSourceRT dataSource : runningDataSources)
			stopDataSource(dataSource.getId());

		for (String key : simpleEventDetectors.keySet())
			stopSimpleEventDetector(key);
	}

	public void joinTermination() {
		for (DataSourceRT dataSource : runningDataSources) {
			try {
				dataSource.joinTermination();
			} catch (ShouldNeverHappenException e) {
				LOG
						.error("Error stopping data source "
								+ dataSource.getId(), e);
			}
		}
	}

	//
	//
	// Data sources
	//
	public DataSourceRT getRunningDataSource(int dataSourceId) {
		for (DataSourceRT dataSource : runningDataSources) {
			if (dataSource.getId() == dataSourceId)
				return dataSource;
		}
		return null;
	}

	public boolean isDataSourceRunning(int dataSourceId) {
		return getRunningDataSource(dataSourceId) != null;
	}

	public List<DataSourceVO<?>> getDataSources() {
		return new DataSourceDao().getDataSources();
	}

	public DataSourceVO<?> getDataSource(int dataSourceId) {
		return new DataSourceDao().getDataSource(dataSourceId);
	}

	public void deleteDataSource(int dataSourceId) {
		stopDataSource(dataSourceId);
		new DataSourceDao().deleteDataSource(dataSourceId);
		Common.ctx.getEventManager().cancelEventsForDataSource(dataSourceId);
	}

	public void saveDataSource(DataSourceVO<?> vo) {
		// If the data source is running, stop it.
		stopDataSource(vo.getId());

		// In case this is a new data source, we need to save to the database
		// first so that it has a proper id.
		new DataSourceDao().saveDataSource(vo);

		// If the data source is enabled, start it.
		if (vo.isEnabled()) {
			if (initializeDataSource(vo))
				startDataSourcePolling(vo);
		}
	}

	private boolean initializeDataSource(DataSourceVO<?> vo) {
		synchronized (runningDataSources) {
			// If the data source is already running, just quit.
			if (isDataSourceRunning(vo.getId()))
				return false;

			// Ensure that the data source is enabled.
			Assert.isTrue(vo.isEnabled());

			// Create and initialize the runtime version of the data source.
			DataSourceRT dataSource = vo.createDataSourceRT();
			dataSource.initialize();

			// Add it to the list of running data sources.
			runningDataSources.add(dataSource);

			// Add the enabled points to the data source.
			List<DataPointVO> dataSourcePoints = new DataPointDao()
					.getDataPoints(vo.getId(), null);
			for (DataPointVO dataPoint : dataSourcePoints) {
				if (dataPoint.isEnabled())
					startDataPoint(dataPoint);
			}

			LOG.info("Data source '" + vo.getName() + "' initialized");

			return true;
		}
	}

	private void startDataSourcePolling(DataSourceVO<?> vo) {
		DataSourceRT dataSource = getRunningDataSource(vo.getId());
		if (dataSource != null)
			dataSource.beginPolling();
	}

	public void startDataSource(DataSourceRT dataSource) {
		dataSource.initialize();

		// Add it to the list of running data sources.
		runningDataSources.add(dataSource);

		// Add the enabled points to the data source.
		List<DataPointVO> dataSourcePoints = new DataPointDao()
				.getDataPoints(dataSource.getId(), null);
		for (DataPointVO dataPoint : dataSourcePoints) {
			if (dataPoint.isEnabled())
				startDataPoint(dataPoint);
		}
	}
	
	public  void stopDataSourceForce(int id) {
		if(id==Common.NEW_ID)
			return;
		stopDataSource(id);
	}
	
	private void stopDataSource(int id) {
		synchronized (runningDataSources) {
			DataSourceRT dataSource = getRunningDataSource(id);
			if (dataSource == null)
				return;

			// Stop the data points.
			for (DataPointRT p : dataPoints.values()) {
				if (p.getDataSourceId() == id)
					stopDataPoint(p.getId());
			}

			runningDataSources.remove(dataSource);
			dataSource.terminate();

			dataSource.joinTermination();
			LOG.info("Data source '" + dataSource.getName() + "' stopped");
		}
	}

	//
	//
	// Data points
	//
	//FIXME RuntimeManager保存点数据
	public void saveDataPoint(DataPointVO point) {
		stopDataPoint(point.getId());

		// Since the point's data type may have changed, we must ensure that the
		// other attrtibutes are still ok with
		// it.
		int dataType = point.getPointLocator().getDataTypeId();

		// Chart renderer
		if (point.getChartRenderer() != null
				&& !point.getChartRenderer().getDef().supports(dataType))
			// Return to a default renderer
			point.setChartRenderer(null);

		// Text renderer
		if (point.getTextRenderer() != null
				&& !point.getTextRenderer().getDef().supports(dataType))
			// Return to a default renderer
			point.defaultTextRenderer();

		// Event detectors
		Iterator<PointEventDetectorVO> peds = point.getEventDetectors()
				.iterator();
		while (peds.hasNext()) {
			PointEventDetectorVO ped = peds.next();
			if (!ped.getDef().supports(dataType))
				// Remove the detector.
				peds.remove();
		}

		new DataPointDao().saveDataPoint(point);

		if (point.isEnabled())
			startDataPoint(point);
	}

	public void deleteDataPoint(DataPointVO point) {
		if (point.isEnabled())
			stopDataPoint(point.getId());
		new DataPointDao().deleteDataPoint(point.getId());
		Common.ctx.getEventManager().cancelEventsForDataPoint(point.getId());
	}

	private void startDataPoint(DataPointVO vo) {
		synchronized (dataPoints) {
			Assert.isTrue(vo.isEnabled());

			// Only add the data point if its data source is enabled.
			DataSourceRT ds = getRunningDataSource(vo.getDataSourceId());
			if (ds != null) {
				// Change the VO into a data point implementation.
				DataPointRT dataPoint = new DataPointRT(vo, vo
						.getPointLocator().createRuntime());

				// Add/update it in the data image.
				dataPoints.put(dataPoint.getId(), dataPoint);

				// Initialize it.
				dataPoint.initialize();
				DataPointListener l = getDataPointListeners(vo.getId());
				if (l != null)
					l.pointInitialized();

				// Add/update it in the data source.
				ds.addDataPoint(dataPoint);
			}
		}
	}

	private void stopDataPoint(int dataPointId) {
		synchronized (dataPoints) {
			// Remove this point from the data image if it is there. If not,
			// just quit.
			DataPointRT p = dataPoints.remove(dataPointId);

			// Remove it from the data source, and terminate it.
			if (p != null) {
				getRunningDataSource(p.getDataSourceId()).removeDataPoint(p);
				DataPointListener l = getDataPointListeners(dataPointId);
				if (l != null)
					l.pointTerminated();
				p.terminate();
			}
		}
	}

	public boolean isDataPointRunning(int dataPointId) {
		return dataPoints.get(dataPointId) != null;
	}

	public DataPointRT getDataPoint(int dataPointId) {
		return dataPoints.get(dataPointId);
	}

	public void addDataPointListener(int dataPointId, DataPointListener l) {
		DataPointListener listeners = dataPointListeners.get(dataPointId);
		dataPointListeners.put(dataPointId, DataPointEventMulticaster.add(
				listeners, l));
	}

	public void removeDataPointListener(int dataPointId, DataPointListener l) {
		DataPointListener listeners = DataPointEventMulticaster.remove(
				dataPointListeners.get(dataPointId), l);
		if (listeners == null)
			dataPointListeners.remove(dataPointId);
		else
			dataPointListeners.put(dataPointId, listeners);
	}

	public DataPointListener getDataPointListeners(int dataPointId) {
		return dataPointListeners.get(dataPointId);
	}

	//
	// Point values
	public void setDataPointValue(int dataPointId, MangoValue value,
			SetPointSource source) {
		setDataPointValue(dataPointId, new PointValueTime(value, System
				.currentTimeMillis()), source);
	}

	public void setDataPointValue(int dataPointId, PointValueTime valueTime,
			SetPointSource source) {
		DataPointRT dataPoint = dataPoints.get(dataPointId);
		if (dataPoint == null)
			throw new RTException("Point is not enabled");

		if (!dataPoint.getPointLocator().isSettable())
			throw new RTException("Point is not settable");

		// Tell the data source to set the value of the point.
		DataSourceRT ds = getRunningDataSource(dataPoint.getDataSourceId());
		// The data source may have been disabled. Just make sure.
		if (ds != null)
			ds.setPointValue(dataPoint, valueTime, source);
	}

	public void relinquish(int dataPointId) {
		DataPointRT dataPoint = dataPoints.get(dataPointId);
		if (dataPoint == null)
			throw new RTException("Point is not enabled");

		if (!dataPoint.getPointLocator().isSettable())
			throw new RTException("Point is not settable");
		if (!dataPoint.getPointLocator().isRelinquishable())
			throw new RTException("Point is not relinquishable");

		// Tell the data source to relinquish value of the point.
		DataSourceRT ds = getRunningDataSource(dataPoint.getDataSourceId());
		// The data source may have been disabled. Just make sure.
		if (ds != null)
			ds.relinquish(dataPoint);
	}

	public void forcePointRead(int dataPointId) {
		DataPointRT dataPoint = dataPoints.get(dataPointId);
		if (dataPoint == null)
			throw new RTException("Point is not enabled");

		// Tell the data source to read the point value;
		DataSourceRT ds = getRunningDataSource(dataPoint.getDataSourceId());
		if (ds != null)
			// The data source may have been disabled. Just make sure.
			ds.forcePointRead(dataPoint);
	}

	/**
	 * 补数据专用
	 * 
	 * @param dsId 数据源
	 * @param timeStart 开始时间
	 * @param timeEnd 结束时间
	 */
	public void forceSourceRead(int dsId, long timeStart, long timeEnd) {
		//先删除之前的数据
		List<DataPointVO>  de=new DataPointDao().getDataPointIds(dsId);
		List<Integer> dses=new ArrayList<Integer>();
		for(int i=0;i<de.size();i++){
			ModbusPointLocatorVO mv=de.get(i).getPointLocator();
			if(mv.getRange()==RegisterRange.HOLDING_REGISTER_88){
				dses.add(de.get(i).getId());
			}
		}
		new PointValueDao().deletePointValuesByTime(dses,timeStart,timeEnd);
		DataSourceRT ds = getRunningDataSource(dsId);
		if (ds != null)
			// The data source may have been disabled. Just make sure.
			ds.forceSourceRead(ds,timeStart,timeEnd);
	}

	/**
	 * 清空点数据
	 * @return
	 */
	public long purgeDataPointValues() {
		PointValueDao pointValueDao = new PointValueDao();
		long count = pointValueDao.deleteAllPointData();
		pointValueDao.compressTables();
		for (Integer id : dataPoints.keySet())
			updateDataPointValuesRT(id);
		return count;
	}

	public long purgeDataPointValues(int dataPointId, int periodType,
			int periodCount) {
		long before = DateUtils.minus(System.currentTimeMillis(), periodType,
				periodCount);
		return purgeDataPointValues(dataPointId, before);
	}

	public long purgeDataPointValues(int dataPointId) {
		long count = new PointValueDao().deletePointValues(dataPointId);
		updateDataPointValuesRT(dataPointId);
		return count;
	}

	public long purgeDataPointValues(int dataPointId, long before) {
		long count = new PointValueDao().deletePointValuesBefore(dataPointId,
				before);
		if (count > 0)
			updateDataPointValuesRT(dataPointId);
		return count;
	}

	private void updateDataPointValuesRT(int dataPointId) {
		DataPointRT dataPoint = dataPoints.get(dataPointId);
		if (dataPoint != null)
			// Enabled. Reset the point's cache.
			dataPoint.resetValues();
	}

	//
	//
	// Scheduled events
	//
	public void saveScheduledEvent(ScheduledEventVO vo) {
		// If the scheduled event is running, stop it.
		stopSimpleEventDetector(vo.getEventDetectorKey());

		new ScheduledEventDao().saveScheduledEvent(vo);

		// If the scheduled event is enabled, start it.
		if (!vo.isDisabled())
			startScheduledEvent(vo);
	}

	private void startScheduledEvent(ScheduledEventVO vo) {
		synchronized (simpleEventDetectors) {
			stopSimpleEventDetector(vo.getEventDetectorKey());
			ScheduledEventRT rt = vo.createRuntime();
			simpleEventDetectors.put(vo.getEventDetectorKey(), rt);
			rt.initialize();
		}
	}

	public void stopSimpleEventDetector(String key) {
		synchronized (simpleEventDetectors) {
			SimpleEventDetector rt = simpleEventDetectors.remove(key);
			if (rt != null)
				rt.terminate();
		}
	}

	//
	//
	// Point event detectors
	//
	public void addPointEventDetector(PointEventDetectorRT ped) {
		synchronized (simpleEventDetectors) {
			ped.initialize();
			simpleEventDetectors.put(ped.getEventDetectorKey(), ped);
		}
	}

	public void removePointEventDetector(String pointEventDetectorKey) {
		synchronized (simpleEventDetectors) {
			SimpleEventDetector sed = simpleEventDetectors
					.remove(pointEventDetectorKey);
			if (sed != null)
				sed.terminate();
		}
	}

	public SimpleEventDetector getSimpleEventDetector(String key) {
		return simpleEventDetectors.get(key);
	}

	//
	//
	// Compound event detectors
	//
	public boolean saveCompoundEventDetector(CompoundEventDetectorVO vo) {
		// If the CED is running, stop it.
		stopCompoundEventDetector(vo.getId());

		new CompoundEventDetectorDao().saveCompoundEventDetector(vo);

		// If the scheduled event is enabled, start it.
		if (!vo.isDisabled())
			return startCompoundEventDetector(vo);

		return true;
	}

	public boolean startCompoundEventDetector(CompoundEventDetectorVO ced) {
		stopCompoundEventDetector(ced.getId());
		CompoundEventDetectorRT rt = ced.createRuntime();
		try {
			rt.initialize();
			compoundEventDetectors.put(ced.getId(), rt);
			return true;
		} catch (LifecycleException e) {
			rt.raiseFailureEvent(new LocalizableMessage(
					"event.compound.exceptionFailure", ced.getName(),
					((LocalizableException) e.getCause())
							.getLocalizableMessage()));
		} catch (Exception e) {
			rt.raiseFailureEvent(new LocalizableMessage(
					"event.compound.exceptionFailure", ced.getName(), e
							.getMessage()));
		}
		return false;
	}

	public void stopCompoundEventDetector(int compoundEventDetectorId) {
		CompoundEventDetectorRT rt = compoundEventDetectors
				.remove(compoundEventDetectorId);
		if (rt != null)
			rt.terminate();
	}

	//
	//
	// Publishers
	//
	private PublisherRT<?> getRunningPublisher(int publisherId) {
		for (PublisherRT<?> publisher : runningPublishers) {
			if (publisher.getId() == publisherId)
				return publisher;
		}
		return null;
	}

	public boolean isPublisherRunning(int publisherId) {
		return getRunningPublisher(publisherId) != null;
	}

	public PublisherVO<? extends PublishedPointVO> getPublisher(int publisherId) {
		return new PublisherDao().getPublisher(publisherId);
	}

	public void deletePublisher(int publisherId) {
		stopPublisher(publisherId);
		new PublisherDao().deletePublisher(publisherId);
		Common.ctx.getEventManager().cancelEventsForPublisher(publisherId);
	}

	public void savePublisher(PublisherVO<? extends PublishedPointVO> vo) {
		// If the data source is running, stop it.
		stopPublisher(vo.getId());

		// In case this is a new publisher, we need to save to the database
		// first so that it has a proper id.
		new PublisherDao().savePublisher(vo);

		// If the publisher is enabled, start it.
		if (vo.isEnabled())
			startPublisher(vo);
	}

	private void startPublisher(PublisherVO<? extends PublishedPointVO> vo) {
		synchronized (runningPublishers) {
			// If the publisher is already running, just quit.
			if (isPublisherRunning(vo.getId()))
				return;

			// Ensure that the data source is enabled.
			Assert.isTrue(vo.isEnabled());

			// Create and start the runtime version of the publisher.
			PublisherRT<?> publisher = vo.createPublisherRT();
			publisher.initialize();

			// Add it to the list of running publishers.
			runningPublishers.add(publisher);
		}
	}

	private void stopPublisher(int id) {
		synchronized (runningPublishers) {
			PublisherRT<?> publisher = getRunningPublisher(id);
			if (publisher == null)
				return;

			runningPublishers.remove(publisher);
			publisher.terminate();
			publisher.joinTermination();
		}
	}

	//
	//
	// Point links
	//
	private PointLinkRT getRunningPointLink(int pointLinkId) {
		for (PointLinkRT pointLink : pointLinks) {
			if (pointLink.getId() == pointLinkId)
				return pointLink;
		}
		return null;
	}

	public boolean isPointLinkRunning(int pointLinkId) {
		return getRunningPointLink(pointLinkId) != null;
	}

	public void deletePointLink(int pointLinkId) {
		stopPointLink(pointLinkId);
		new PointLinkDao().deletePointLink(pointLinkId);
	}

	public void savePointLink(PointLinkVO vo) {
		// If the point link is running, stop it.
		stopPointLink(vo.getId());

		new PointLinkDao().savePointLink(vo);

		// If the point link is enabled, start it.
		if (!vo.isDisabled())
			startPointLink(vo);
	}

	private void startPointLink(PointLinkVO vo) {
		synchronized (pointLinks) {
			// If the point link is already running, just quit.
			if (isPointLinkRunning(vo.getId()))
				return;

			// Ensure that the point link is enabled.
			Assert.isTrue(!vo.isDisabled());

			// Create and start the runtime version of the point link.
			PointLinkRT pointLink = new PointLinkRT(vo);
			pointLink.initialize();

			// Add it to the list of running point links.
			pointLinks.add(pointLink);
		}
	}

	private void stopPointLink(int id) {
		synchronized (pointLinks) {
			PointLinkRT pointLink = getRunningPointLink(id);
			if (pointLink == null)
				return;

			pointLinks.remove(pointLink);
			pointLink.terminate();
		}
	}

	//
	//
	// Maintenance events
	//
	public MaintenanceEventRT getRunningMaintenanceEvent(int id) {
		for (MaintenanceEventRT rt : maintenanceEvents) {
			if (rt.getVo().getId() == id)
				return rt;
		}
		return null;
	}

	public boolean isActiveMaintenanceEvent(int dataSourceId) {
		for (MaintenanceEventRT rt : maintenanceEvents) {
			if (rt.getVo().getDataSourceId() == dataSourceId)
				return true;
		}
		return false;
	}

	public boolean isMaintenanceEventRunning(int id) {
		return getRunningMaintenanceEvent(id) != null;
	}

	// public void deleteMaintenanceEvent(int id) {
	// stopMaintenanceEvent(id);
	// new MaintenanceEventDao().deleteMaintenanceEvent(id);
	// }
	//
	// public void saveMaintenanceEvent(MaintenanceEventVO vo) {
	// // If the maintenance event is running, stop it.
	// stopMaintenanceEvent(vo.getId());
	//
	// new MaintenanceEventDao().saveMaintenanceEvent(vo);
	//
	// // If the maintenance event is enabled, start it.
	// if (!vo.isDisabled())
	// startMaintenanceEvent(vo);
	// }

	// private void startMaintenanceEvent(MaintenanceEventVO vo) {
	// synchronized (maintenanceEvents) {
	// // If the maintenance event is already running, just quit.
	// if (isMaintenanceEventRunning(vo.getId()))
	// return;
	//
	// // Ensure that the maintenance event is enabled.
	// Assert.isTrue(!vo.isDisabled());
	//
	// // Create and start the runtime version of the maintenance event.
	// MaintenanceEventRT rt = new MaintenanceEventRT(vo);
	// rt.initialize();
	//
	// // Add it to the list of running maintenance events.
	// maintenanceEvents.add(rt);
	// }
	// }

	// private void stopMaintenanceEvent(int id) {
	// synchronized (maintenanceEvents) {
	// MaintenanceEventRT rt = getRunningMaintenanceEvent(id);
	// if (rt == null)
	// return;
	//
	// maintenanceEvents.remove(rt);
	// rt.terminate();
	// }
	// }
}
