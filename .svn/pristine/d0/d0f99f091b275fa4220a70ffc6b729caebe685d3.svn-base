/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.db.dao;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
//import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.RejectedExecutionException;
import java.util.Date;
import javax.sql.DataSource;

import com.serotonin.mango.rt.dataImage.PointValueTime;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.ConcurrencyFailureException;
import org.springframework.transaction.TransactionStatus;

import com.serotonin.ShouldNeverHappenException;
import com.serotonin.db.IntValuePair;
import com.serotonin.db.MappedRowCallback;
import com.serotonin.db.spring.ExtendedJdbcTemplate;
import com.serotonin.db.spring.GenericIntValuePairRowMapper;
import com.serotonin.db.spring.GenericRowMapper;
import com.serotonin.db.spring.GenericTransactionCallback;
import com.serotonin.io.StreamUtils;
import com.serotonin.mango.Common;
import com.serotonin.mango.DataTypes;
import com.serotonin.mango.ImageSaveException;
import com.serotonin.mango.db.DatabaseAccess;
import com.serotonin.mango.rt.dataImage.AnnotatedPointValueTime;
import com.serotonin.mango.rt.dataImage.IdPointValueTime;
import com.serotonin.mango.rt.dataImage.PointValueTime;
import com.serotonin.mango.rt.dataImage.SetPointSource;
import com.serotonin.mango.rt.dataImage.types.AlphanumericValue;
import com.serotonin.mango.rt.dataImage.types.BinaryValue;
import com.serotonin.mango.rt.dataImage.types.ImageValue;
import com.serotonin.mango.rt.dataImage.types.MangoValue;
import com.serotonin.mango.rt.dataImage.types.MultistateValue;
import com.serotonin.mango.rt.dataImage.types.NumericValue;
import com.serotonin.mango.rt.maint.work.WorkItem;
import com.serotonin.mango.vo.HistoryPointInfo;
import com.serotonin.mango.vo.bean.LongPair;
import com.serotonin.monitor.IntegerMonitor;
import com.serotonin.util.queue.ObjectQueue;
import com.serotonin.mango.vo.statistics.StatisticsScriptVO;

public class PointValueDao extends BaseDao {
	private static List<UnsavedPointValue> UNSAVED_POINT_VALUES = new ArrayList<UnsavedPointValue>();

	private static final String POINT_VALUE_INSERT_START = "insert into pointValues (dataPointId, dataType, pointValue, ts) ";
//	private static final String POINT_VALUE_INSERT_VALUES = "(?,?,?,?)";
    private static final String POINT_VALUE_INSERT_VALUES = "select ?,?,?,? ";
	private static final int POINT_VALUE_INSERT_VALUES_COUNT = 4;
	private static final String POINT_VALUE_INSERT = POINT_VALUE_INSERT_START
			+ POINT_VALUE_INSERT_VALUES;
	private static final String POINT_VALUE_ANNOTATION_INSERT = "insert into pointValueAnnotations "
			+ "(pointValueId,pointId, textPointValueShort, textPointValueLong, sourceType, sourceId) values (?,?,?,?,?,?)";

	public PointValueDao() {
		super();
	}

	public PointValueDao(DataSource dataSource) {
		super(dataSource);
	}

	/**
	 * Only the PointValueCache should call this method during runtime. Do not
	 * use.
	 */
	public PointValueTime savePointValueSync(int pointId,
			PointValueTime pointValue, SetPointSource source) {
		long id = savePointValueImpl(pointId, pointValue, source, false);

		PointValueTime savedPointValue;
		int retries = 5;
		while (true) {
			try {
				savedPointValue = getPointValue(pointId,id);
				break;
			} catch (ConcurrencyFailureException e) {
				if (retries <= 0)
					throw e;
				retries--;
			}
		}

		return savedPointValue;
	}

	/**
	 * Only the PointValueCache should call this method during runtime. Do not
	 * use.
	 */
	public void savePointValueAsync(int pointId, PointValueTime pointValue1,
			SetPointSource source) {
		long id = savePointValueImpl(pointId, pointValue1, source, true);
		if (id != -1)
			clearUnsavedPointValues();
	}

	long savePointValueImpl(final int pointId, final PointValueTime pointValue,
			final SetPointSource source, boolean async) {
//
//		Calendar ca = Calendar.getInstance();
//		ca.setTimeInMillis(pointValue1.getTime());
//		if(source.getSetPointSourceType()==3){
//		//取出秒
//		int sc=ca.get(Calendar.SECOND);
//		//0-19秒 规整到0秒
//		if(sc>=0&&sc<=19){
//			ca.set(Calendar.SECOND, 0);
//		}
//		//20-39秒规整到 20秒
//		else if(sc>19&&sc<=39){
//			ca.set(Calendar.SECOND, 20);
//		}
//		//40-59秒规整到40秒
//		else{
//			ca.set(Calendar.SECOND, 40);
//		}
		//将日历实例的时间戳 赋给time变量
		//time=ca.getTimeInMillis();
		//final PointValueTime pointValue=new PointValueTime(pointValue1.getValue(),ca.getTimeInMillis());
		MangoValue value = pointValue.getValue();
		final int dataType = DataTypes.getDataType(value);
		double dvalue = 0;
		String svalue = null;
		
		if (dataType == DataTypes.IMAGE) {
			ImageValue imageValue = (ImageValue) value;
			dvalue = imageValue.getType();
			if (imageValue.isSaved())
				svalue = Long.toString(imageValue.getId());
		} else if (value.hasDoubleRepresentation())
			dvalue = value.getDoubleValue();
		else
			svalue = value.getStringValue();

		// Check if we need to create an annotation.
		long id;
		try {
			if (svalue != null || source != null || dataType == DataTypes.IMAGE) {
				final double dvalueFinal = dvalue;
				final String svalueFinal = svalue;

				// Create a transaction within which to do the insert.
				id = getTransactionTemplate().execute(
						new GenericTransactionCallback<Long>() {
							public Long doInTransaction(TransactionStatus status) {
								return savePointValue(pointId, dataType,
										dvalueFinal, pointValue.getTime(),
										svalueFinal, source, false);
							}
						});
			} else
				// Single sql call, so no transaction required.
				id = savePointValue(pointId, dataType, dvalue, pointValue
						.getTime(), svalue, source, async);
		} catch (ConcurrencyFailureException e) {
			// Still failed to insert after all of the retries. Store the data
			synchronized (UNSAVED_POINT_VALUES) {
				UNSAVED_POINT_VALUES.add(new UnsavedPointValue(pointId,
						pointValue, source));
			}
			return -1;
		}

		// Check if we need to save an image
		if (dataType == DataTypes.IMAGE) {
			ImageValue imageValue = (ImageValue) value;
			if (!imageValue.isSaved()) {
				imageValue.setId(id);

				File file = new File(Common.getFiledataPath(), imageValue
						.getFilename());

				// Write the file.
				FileOutputStream out = null;
				try {
					out = new FileOutputStream(file);
					StreamUtils.transfer(new ByteArrayInputStream(imageValue
							.getData()), out);
				} catch (IOException e) {
					// Rethrow as an RTE
					throw new ImageSaveException(e);
				} finally {
					try {
						if (out != null)
							out.close();
					} catch (IOException e) {
						// no op
					}
				}

				// Allow the data to be GC'ed
				imageValue.setData(null);
			}
		}

		return id;
	}

	private void clearUnsavedPointValues() {
		if (!UNSAVED_POINT_VALUES.isEmpty()) {
			synchronized (UNSAVED_POINT_VALUES) {
				while (!UNSAVED_POINT_VALUES.isEmpty()) {
					UnsavedPointValue data = UNSAVED_POINT_VALUES.remove(0);
					savePointValueImpl(data.getPointId(), data.getPointValue(),
							data.getSource(), false);
				}
			}
		}
	}

	long savePointValue(final int pointId, final int dataType, double dvalue,
			final long time, final String svalue, final SetPointSource source,
			boolean async) {
		// Apply database specific bounds on double values.
		dvalue = DatabaseAccess.getDatabaseAccess().applyBounds(dvalue);
		//实例化 日历
//		Calendar ca = Calendar.getInstance();
//		ca.setTimeInMillis(time);
////		if(null!=source){
////			if(source.getSetPointSourceType()==3){
//				//取出秒
//				int sc=ca.get(Calendar.SECOND);
//				//0-19秒 规整到0秒
//				if(sc>=0&&sc<=19){
//					ca.set(Calendar.SECOND, 0);
//				}
//				//20-39秒规整到 20秒
//				else if(sc>19&&sc<=39){
//					ca.set(Calendar.SECOND, 20);
//				}
//				//40-59秒规整到40秒
//				else{
//					ca.set(Calendar.SECOND, 40);
//				}
////			}
////		}
//		System.out.println(ca.getTime());
		if (async) {
			BatchWriteBehind.add(new BatchWriteBehindEntry(pointId, dataType,
					dvalue, time), ejt);
			return -1;
		}

		int retries = 5;
		while (true) {
			try {
				return savePointValueImpl(pointId, dataType, dvalue, time,
						svalue, source);
			} catch (ConcurrencyFailureException e) {
				if (retries <= 0)
					throw e;
				retries--;
			} catch (RuntimeException e) {
				throw new RuntimeException(
						"Error saving point value: dataType=" + dataType
								+ ", dvalue=" + dvalue, e);
			}
		}
	}

	private long savePointValueImpl(int pointId, int dataType, double dvalue,
			long time, String svalue, SetPointSource source) {
////		if(null!=source){
////			//检测数据源类型
////			if(source.getSetPointSourceType()==3){
//				//实例化 日历
//				Calendar ca = Calendar.getInstance();
//				ca.setTimeInMillis(time);
//				//取出秒
//				int sc=ca.get(Calendar.SECOND);
//				//0-19秒 规整到0秒
//				if(sc>=0&&sc<=19){
//					ca.set(Calendar.SECOND, 0);
//				}
//				//20-39秒规整到 20秒
//				else if(sc>19&&sc<=39){
//					ca.set(Calendar.SECOND, 20);
//				}
//				//40-59秒规整到40秒
//				else{
//					ca.set(Calendar.SECOND, 40);
//				}
//				//将日历实例的时间戳 赋给time变量
//				time=ca.getTimeInMillis();
////			}
////		}
		/**
		long id = doInsertLong(POINT_VALUE_INSERT, new Object[] { pointId,
				dataType, dvalue, time });
		 */
		String POINT_VALUE_INSERT_SQL="insert into pointvalues_"+pointId+" (dataType, pointValue, ts) select ?,?,? ";
		long id= doInsertLong(POINT_VALUE_INSERT_SQL, new Object[] {dataType, dvalue, time });
		
		if (svalue == null && dataType == DataTypes.IMAGE)
			svalue = Long.toString(id);

		// Check if we need to create an annotation.
		if (svalue != null || source != null) {
			Integer sourceType = null, sourceId = null;
			if (source != null) {
				sourceType = source.getSetPointSourceType();
				sourceId = source.getSetPointSourceId();
			}

			String shortString = null;
			String longString = null;
			if (svalue != null) {
				if (svalue.length() > 128)
					longString = svalue;
				else
					shortString = svalue;
			}

			ejt.update(POINT_VALUE_ANNOTATION_INSERT, new Object[] { id,pointId,
					shortString, longString, sourceType, sourceId }, new int[] {
					Types.INTEGER,Types.INTEGER, Types.VARCHAR, Types.CLOB, Types.SMALLINT,
					Types.INTEGER });
		}

		return id;
	}
//?
	private static final String POINT_VALUE_SELECT = "select pv.dataType, pv.pointValue, pva.textPointValueShort, pva.textPointValueLong, pv.ts, pva.sourceType, "
			+ "  pva.sourceId "
			+ "from pointValues pv "
			+ "  left join pointValueAnnotations pva on pv.id = pva.pointValueId";
//ok 
	public List<PointValueTime> getPointValues(int dataPointId, long since) {
		return pointValuesQuery("select pv.dataType, pv.pointValue, pva.textPointValueShort, pva.textPointValueLong, pv.ts, pva.sourceType, "
				+ "  pva.sourceId "
				+ "from pointValues_"+dataPointId+" pv "
				+ "  left join pointValueAnnotations pva on pv.id ="+dataPointId
				+ " and pva.pointId="+dataPointId+" where pv.ts >= ? order by ts",
				new Object[] { since }, 0);
	}
//ok 
	public PointValueTime getPointValuesForWarnCode(int dataPointId, long time) {
		return pointValuesQuery(
						"select top 1  pv.dataType, pv.pointValue, pva.textPointValueShort, pva.textPointValueLong, pv.ts, pva.sourceType, "
						+ "  pva.sourceId "
						+ "from pointValues_"+dataPointId+" pv "
						+ "  left join pointValueAnnotations pva on pv.id = pva.pointValueId and pva.pointId="+dataPointId+" where pv.ts<=? order by pv.ts desc",
						new Object[] { time },0).get(0);
	}
//ok	
	public List<PointValueTime> getPointValuesBetween(int dataPointId,
			long from, long to) {
		return pointValuesQuery("select pv.dataType, pv.pointValue, pva.textPointValueShort, pva.textPointValueLong, pv.ts, pva.sourceType, "
				+ "  pva.sourceId "
				+ "from pointValues_"+dataPointId+" pv "
				+ "  left join pointValueAnnotations pva on pv.id = pva.pointValueId and pva.pointId="+dataPointId
						+ " where  pv.ts >= ? and pv.ts<? order by ts",
				new Object[] {  from, to }, 0);
	}

	// 查询历史数据的基本sql
	private static final String HISTORY_POINT_VALUE_SELECT = "select pv.dataPointId, pv.ts,pv.pointValue from  HISTORY_VIEW pv";

	/**
	 * 获得历史数据
	 * 
	 * @param dataPointId
	 * @param from
	 * @param to
	 * @return
	 */
	public List<HistoryPointInfo> getHistoryPointValues(
			List<Integer> dataPointIds,int limit) {
		String ids = createDelimitedList(dataPointIds, ",", null);
		return historyPointValuesQuery(HISTORY_POINT_VALUE_SELECT
				+ " where pv.ts< ? and pv.dataPointId in (" + ids
				+ ") order by ts desc limit ?",
		// new Object[] { from, to }, 0);
				new Object[] {(new Date()).getTime(),limit}, 0);
	}

	/**
	 * 获得历史数据
	 * 
	 * @param dataPointId
	 * @param from
	 * @param to
	 * @return
	 */
	public List<HistoryPointInfo> getHistoryPointValuesBetween(
			List<Integer> dataPointIds, long from, long to, int firstLimit,
			int secondLimit) {
		String ids = createDelimitedList(dataPointIds, ",", null);
		return historyPointValuesQuery(HISTORY_POINT_VALUE_SELECT
				+ " where pv.dataPointId in (" + ids
				+ ") and pv.ts >= ? and pv.ts<? order by ts  limit ?,?",
				new Object[] { from, to, firstLimit, secondLimit }, 0);
	}
	/**
	 * 获得点设备数据的count
	 * 
	 * @param dataPointIds
	 * @param from
	 * @param to
	 * @return
	 */
	public long historyDateRangeCount(List<Integer> dataPointIds, long from,
			long to) {
		String ids = createDelimitedList(dataPointIds, ",", null);
		return ejt.queryForLong(
				"select count(*) from HISTORY_VIEW where dataPointId in(" + ids
						+ ") and ts>=? and ts<=?", new Object[] { from, to });
	}

	// query
	private List<HistoryPointInfo> historyPointValuesQuery(String sql,
			Object[] params, int limit) {
		List<HistoryPointInfo> result = query(sql, params,
				new HistoryPointValueRowMapper(), limit);
		// updateAnnotations(result);
		return result;
	}

	// 内部类
	class HistoryPointValueRowMapper implements
			GenericRowMapper<HistoryPointInfo> {
		public HistoryPointInfo mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			int dataPointId = rs.getInt(1);
			long time = rs.getLong(2);
			Double value = rs.getDouble(3);
			return new HistoryPointInfo(dataPointId, time, value);
		}
	}
//ok
	public List<PointValueTime> getLatestPointValues(int dataPointId, int limit) {
		return pointValuesQuery("select pv.dataType, pv.pointValue, pva.textPointValueShort, pva.textPointValueLong, pv.ts, pva.sourceType, "
				+ "  pva.sourceId "
				+ "from pointValues_"+dataPointId+" pv "
				+ "  left join pointValueAnnotations pva on pv.id = pva.pointValueId and pva.pointId="+dataPointId
				+ "  order by pv.ts desc",
				new Object[] { }, limit);
	}
//ok
	public List<PointValueTime> getLatestPointValues(int dataPointId,
			int limit, long before) {
		return pointValuesQuery("select pv.dataType, pv.pointValue, pva.textPointValueShort, pva.textPointValueLong, pv.ts, pva.sourceType, "
				+ "  pva.sourceId "
				+ "from pointValues_"+dataPointId+" pv "
				+ "  left join pointValueAnnotations pva on pv.id = pva.pointValueId  and pva.pointId="+dataPointId
				+ " where  pv.ts<? order by pv.ts desc",
				new Object[] {  before }, limit);
	}
//ok ?????????
	public PointValueTime getLatestPointValue(int dataPointId) {
		long maxTs = ejt.queryForLong(
				"select max(ts) from pointValues_"+dataPointId,
				new Object[] {},0);
		if (maxTs == 0)
			return null;
		return pointValueQuery("select pv.dataType, pv.pointValue, pva.textPointValueShort, pva.textPointValueLong, pv.ts, pva.sourceType, "
				+ "  pva.sourceId "
				+ "from pointValues_"+dataPointId+" pv "
				+ "  left join pointValueAnnotations pva on pv.id = pva.pointValueId  and pva.pointId="+dataPointId
				+ " where pv.ts=?", new Object[] { maxTs });
	}
//ok
	private PointValueTime getPointValue(int pointId,long id) {
		return pointValueQuery("select pv.dataType, pv.pointValue, pva.textPointValueShort, pva.textPointValueLong, pv.ts, pva.sourceType, "
				+ "  pva.sourceId "
				+ "from pointValues_"+pointId+" pv "
				+ "left join pointValueAnnotations pva on pv.id = pva.pointValueId  and pva.pointId="+pointId
				+ " where pv.id=?",
				new Object[] { id });
	}
//ok
	public PointValueTime getPointValueBefore(int dataPointId, long time) {
		Long valueTime = queryForObject(
				"select max(ts) from pointValues_"+dataPointId+" where ts<?",
				new Object[] { time }, Long.class, null);
		if (valueTime == null)
			return null;
		return getPointValueAt(dataPointId, valueTime);
	}
//ok
	public PointValueTime getPointValueAt(int dataPointId, long time) {
		return pointValueQuery("select pv.dataType, pv.pointValue, pva.textPointValueShort, pva.textPointValueLong, pv.ts, pva.sourceType, "
				+ "  pva.sourceId "
				+ "from pointValues_"+dataPointId+" pv "
				+ "left join pointValueAnnotations pva on pv.id = pva.pointValueId  and pva.pointId="+dataPointId
				+ " where pv.ts=?", new Object[] {time });
	}
//
	private PointValueTime pointValueQuery(String sql, Object[] params) {
		List<PointValueTime> result = pointValuesQuery(sql, params, 1);
		if (result.size() == 0)
			return null;
		return result.get(0);
	}
//
	private List<PointValueTime> pointValuesQuery(String sql, Object[] params,
			int limit) {
		List<PointValueTime> result = query(sql, params,
				new PointValueRowMapper(), limit);
		updateAnnotations(result);
		return result;
	}
//ok
	public void getPointValuesBetween(int dataPointId, long from, long to,
			MappedRowCallback<PointValueTime> callback) {
		query(
				"select pv.dataType, pv.pointValue, pva.textPointValueShort, pva.textPointValueLong, pv.ts, pva.sourceType, "
				+ "  pva.sourceId "
				+ "from pointValues_"+dataPointId+" pv "
				+ "left join pointValueAnnotations pva on pv.id = pva.pointValueId  and pva.pointId="+dataPointId
						+ " where pv.ts >= ? and pv.ts<?  order by ts",
				new Object[] {from, to },
				new PointValueRowMapper(), callback);
	}

	class PointValueRowMapper implements GenericRowMapper<PointValueTime> {
		public PointValueTime mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			MangoValue value = createMangoValue(rs, 1);
			long time = rs.getLong(5);

			int sourceType = rs.getInt(6);
			if (rs.wasNull())
				// No annotations, just return a point value.
				return new PointValueTime(value, time);

			// There was a source for the point value, so return an annotated
			// version.
			return new AnnotatedPointValueTime(value, time, sourceType, rs
					.getInt(7));
		}
	}

	MangoValue createMangoValue(ResultSet rs, int firstParameter)
			throws SQLException {
		int dataType = rs.getInt(firstParameter);
		MangoValue value;
		switch (dataType) {
		case (DataTypes.NUMERIC):
			value = new NumericValue(rs.getDouble(firstParameter + 1));
			break;
		case (DataTypes.BINARY):
			value = new BinaryValue(rs.getDouble(firstParameter + 1) == 1);
			break;
		case (DataTypes.MULTISTATE):
			value = new MultistateValue(rs.getInt(firstParameter + 1));
			break;
		case (DataTypes.ALPHANUMERIC):
			String s = rs.getString(firstParameter + 2);
			if (s == null)
				s = rs.getString(firstParameter + 3);
			value = new AlphanumericValue(s);
			break;
		case (DataTypes.IMAGE):
			value = new ImageValue(Integer.parseInt(rs
					.getString(firstParameter + 2)), rs
					.getInt(firstParameter + 3));
			break;
		default:
			value = null;
		}
		return value;
	}

	private void updateAnnotations(List<PointValueTime> values) {
		Map<Integer, List<AnnotatedPointValueTime>> userIds = new HashMap<Integer, List<AnnotatedPointValueTime>>();
		List<AnnotatedPointValueTime> alist;

		// Look for annotated point values.
		AnnotatedPointValueTime apv;
		for (PointValueTime pv : values) {
			if (pv instanceof AnnotatedPointValueTime) {
				apv = (AnnotatedPointValueTime) pv;
				if (apv.getSourceType() == SetPointSource.Types.USER) {
					alist = userIds.get(apv.getSourceId());
					if (alist == null) {
						alist = new ArrayList<AnnotatedPointValueTime>();
						userIds.put(apv.getSourceId(), alist);
					}
					alist.add(apv);
				}
			}
		}

		// Get the usernames from the database.
		if (userIds.size() > 0)
			updateAnnotations("select id, username from users where id in ",
					userIds);
	}

	private void updateAnnotations(String sql,
			Map<Integer, List<AnnotatedPointValueTime>> idMap) {
		// Get the description information from the database.
		List<IntValuePair> data = query(sql + "("
				+ createDelimitedList(idMap.keySet(), ",", null) + ")",
				new GenericIntValuePairRowMapper());

		// Collate the data with the id map, and set the values in the
		// annotations
		List<AnnotatedPointValueTime> annos;
		for (IntValuePair ivp : data) {
			annos = idMap.get(ivp.getKey());
			for (AnnotatedPointValueTime avp : annos)
				avp.setSourceDescriptionArgument(ivp.getValue());
		}
	}

	//
	//
	// Multiple-point callback for point history replays
	//
	private static final String POINT_ID_VALUE_SELECT = "select pv.dataPointId as dataPointId, pv.dataType, pv.pointValue, " //
			+ "pva.textPointValueShort, pva.textPointValueLong, pv.ts "
			+ "from pointValues pv "
			+ "  left join pointValueAnnotations pva on pv.id = pva.pointValueId";
//ok
	public void getPointValuesBetween(List<Integer> dataPointIds, long from,
			long to, MappedRowCallback<IdPointValueTime> callback) {
		//String ids = createDelimitedList(dataPointIds, ",", null);
		String sql="";
		for (int i = 0; i < dataPointIds.size(); i++) {
			int id=dataPointIds.get(i);
			sql+="select "+id+" as dataPointId, pv_"+id+".dataType, pv_"+id+".pointValue, " //
			+ "pva_"+id+".textPointValueShort, pva_"+id+".textPointValueLong, pv_"+id+".ts "
			+ "from pointValues_"+id
			+ " as "+"pv_"+id+" left join pointValueAnnotations pva_"+id+" on pv_"+id+".id = pva_"+id+".pointValueId and pva_"+id+".pointId ="+id
			+" where pv_"+id+".ts >= "+from+" and pv_"+id+".ts<"+to+" ";
			if(i<dataPointIds.size()-1){
				sql+=" union all ";	
			}
		}
		query(sql + " order by ts", new Object[] { }, new IdPointValueRowMapper(), callback);
	}

	/**
	 * Note: this does not extract source information from the annotation.
	 */
	class IdPointValueRowMapper implements GenericRowMapper<IdPointValueTime> {
		public IdPointValueTime mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			int dataPointId = rs.getInt(1);
			MangoValue value = createMangoValue(rs, 2);
			long time = rs.getLong(6);
			return new IdPointValueTime(dataPointId, value, time);
		}
	}

	//
	//
	// Point value deletions
	//
	//ok
	public long deletePointValuesBefore(int dataPointId, long time) {
		return deletePointValues(
				"delete from pointValues_"+dataPointId+" where  ts<?",
				new Object[] { time });
	}
	
	private long deletePointValuesByTime(int dataPointId, long time,long time2) {
		return deletePointValues(
				"delete from pointValues_"+dataPointId+" where ts between ? and ? ",
				new Object[] { time,time2});
	}
	
	public void deletePointValuesByTime(List<Integer> dataPointIds, long time,long time2) {
		for(int i=0;i<dataPointIds.size();i++){
			 deletePointValues(
						"delete from pointValues_"+dataPointIds.get(i)+" where ts between ? and ? ",
						new Object[] { time,time2});
		}
	}
	
	
//ok
	public long deletePointValues(int dataPointId) {
		return deletePointValues("delete from pointValues_"+dataPointId,
				new Object[] {});
	}
//ok
	public long deleteAllPointData() {
		
		
		
		//return deletePointValues("delete from pointValues_"+id, null);
		return 0;
	}
//ok
	public long deletePointValuesWithMismatchedType(int dataPointId,
			int dataType) {
		return deletePointValues(
				"delete from pointValues_"+dataPointId +" where dataType<>?",
				new Object[] {dataType });
	}

	public void compressTables() {
		Common.ctx.getDatabaseAccess().executeCompress(ejt);
	}

	private long deletePointValues(String sql, Object[] params) {
		int cnt;
		if (params == null)
			cnt = ejt.update(sql);
		else
			cnt = ejt.update(sql, params);
		clearUnsavedPointValues();
		return cnt;
	}
//ok
	public long dateRangeCount(int dataPointId, long from, long to) {
		return ejt
				.queryForLong(
						"select count(*) from pointValues_"+dataPointId+" where ts>=? and ts<=?",
						new Object[] { from, to });
	}
//ok
	public long getInceptionDate(int dataPointId) {
		return ejt.queryForLong(
				"select min(ts) from pointValues_"+dataPointId,
				new Object[] {}, -1);
	}
//ok
	public long getStartTime(List<Integer> dataPointIds) {
		if (dataPointIds.isEmpty())
			return -1;
		String sql="select min(ts)as ts from (";
		for (int i = 0; i < dataPointIds.size(); i++) {
			sql+="select min(ts) as ts from pointValues_"+dataPointIds.get(i);
			if(i<dataPointIds.size()-1)
				sql+=" union all ";
		}
		return ejt
				.queryForLong(sql+ ") as tsTable");
	}
//ok
	public long getEndTime(List<Integer> dataPointIds) {
		if (dataPointIds.isEmpty())
			return -1;
		String sql="select max(ts)as ts from (";
		for (int i = 0; i < dataPointIds.size(); i++) {
			sql+="select max(ts) as ts from pointValues_"+dataPointIds.get(i);
			if(i<dataPointIds.size()-1)
				sql+=" union all ";
		}
		return ejt
				.queryForLong(sql+") as tsTable");
	}
	
	//ok
	public boolean getEndDataTime(List<Integer> dataPointIds,long time) {
		if (dataPointIds.isEmpty())
			return false;
		String sql="select max(ts)as ts from (";
		for (int i = 0; i < dataPointIds.size(); i++) {
			sql+="select max(ts) as ts from pointValues_"+dataPointIds.get(i)+" where ts between "+(time-20000)+" and "+(time+20000);
			if(i<dataPointIds.size()-1)
				sql+=" union all ";
		}
		long ts= ejt
				.queryForLong(sql+") as tsTable");
		if(ts!=0.0){
			return true;
		}
		return false;
	}
	
	//ok
	public long getEndDataTime(List<Integer> dataPointIds) {
		if (dataPointIds.isEmpty())
			return 0;
		String sql="select max(ts)as ts from (";
		for (int i = 0; i < dataPointIds.size(); i++) {
			sql+="select max(ts) as ts from pointValues_"+dataPointIds.get(i);
			if(i<dataPointIds.size()-1)
				sql+=" union all ";
		}
		return ejt
				.queryForLong(sql+") as tsTable");
	}
	
//ok
	public LongPair getStartAndEndTime(List<Integer> dataPointIds) {
		if (dataPointIds.isEmpty())
			return null;
		String sql="select min(min) as min,max(max)as max from (";
		for (int i = 0; i < dataPointIds.size(); i++) {
			sql+="select min(ts) as min,max(ts) as max from pointValues_"+dataPointIds.get(i);
			if(i<dataPointIds.size()-1)
				sql+=" union all ";
		}
		
		return queryForObject(sql+") as tableTs",
				null, new GenericRowMapper<LongPair>() {
					@Override
					public LongPair mapRow(ResultSet rs, int index)
							throws SQLException {
						long l = rs.getLong(1);
						if (rs.wasNull())
							return null;
						return new LongPair(l, rs.getLong(2));
					}
				}, null);
	}
//??????
	public List<Long> getFiledataIds() {
		return queryForList(
				"select distinct id from ( " //
						+ "  select id as id from pointValues where dataType="
						+ DataTypes.IMAGE
						+ "  union"
						+ "  select d.pointValueId as id from reportInstanceData d "
						+ "    join reportInstancePoints p on d.reportInstancePointId=p.id"
						+ "  where p.dataType="
						+ DataTypes.IMAGE
						+ ") a order by 1", new Object[] {}, Long.class);
	}

	/**
	 * Class that stored point value data when it could not be saved to the
	 * database due to concurrency errors.
	 * 
	 * 
	 */
	class UnsavedPointValue {
		private final int pointId;
		private final PointValueTime pointValue;
		private final SetPointSource source;

		public UnsavedPointValue(int pointId, PointValueTime pointValue,
				SetPointSource source) {
			this.pointId = pointId;
			this.pointValue = pointValue;
			this.source = source;
		}

		public int getPointId() {
			return pointId;
		}

		public PointValueTime getPointValue() {
			return pointValue;
		}

		public SetPointSource getSource() {
			return source;
		}
	}

	class BatchWriteBehindEntry {
		private final int pointId;
		private final int dataType;
		private final double dvalue;
		private final long time;

		public BatchWriteBehindEntry(int pointId, int dataType, double dvalue,
				long time) {
			this.pointId = pointId;
			this.dataType = dataType;
			this.dvalue = dvalue;
			this.time = time;
		}
		public int getPointId() {
			return this.pointId;
		}
		
		public String outParams() {
			return this.dataType+","+this.dvalue+","+this.time;
		}
		public void writeInto(Object[] params, int index) {
			index *= POINT_VALUE_INSERT_VALUES_COUNT;
			params[index++] = pointId;
			params[index++] = dataType;
			params[index++] = dvalue;
			params[index++] = time;
		}
	}

	static class BatchWriteBehind implements WorkItem {
		private static final ObjectQueue<BatchWriteBehindEntry> ENTRIES = new ObjectQueue<PointValueDao.BatchWriteBehindEntry>();
		private static final CopyOnWriteArrayList<BatchWriteBehind> instances = new CopyOnWriteArrayList<BatchWriteBehind>();
		private static Log LOG = LogFactory.getLog(BatchWriteBehind.class);
		private static final int SPAWN_THRESHOLD = 10000;
		private static int MAX_ROWS = 1000;
		private static final IntegerMonitor ENTRIES_MONITOR = new IntegerMonitor(
				BatchWriteBehind.class.getName() + ".ENTRIES_MONITOR");
		private static final IntegerMonitor INSTANCES_MONITOR = new IntegerMonitor(
				BatchWriteBehind.class.getName() + ".INSTANCES_MONITOR");

		static {
			if (Common.ctx.getDatabaseAccess().getType() == DatabaseAccess.DatabaseType.DERBY)
				// This has not bee tested to be optimal
				MAX_ROWS = 1000;
			else if (Common.ctx.getDatabaseAccess().getType() == DatabaseAccess.DatabaseType.MSSQL)
				// MSSQL has max rows of 1000, and max parameters of 2100. In
				// this case that works out to...
				MAX_ROWS = 524;
			else if (Common.ctx.getDatabaseAccess().getType() == DatabaseAccess.DatabaseType.MYSQL)
				// This appears to be an optimal value
				MAX_ROWS = 2000;
			else
				throw new ShouldNeverHappenException("Unknown database type: "
						+ Common.ctx.getDatabaseAccess().getType());

			Common.MONITORED_VALUES.addIfMissingStatMonitor(ENTRIES_MONITOR);
			Common.MONITORED_VALUES.addIfMissingStatMonitor(INSTANCES_MONITOR);
		}

		static void add(BatchWriteBehindEntry e, ExtendedJdbcTemplate ejt) {
			synchronized (ENTRIES) {
				ENTRIES.push(e);
				ENTRIES_MONITOR.setValue(ENTRIES.size());
				if (instances.isEmpty() || ENTRIES.size() > SPAWN_THRESHOLD) {
					BatchWriteBehind bwb = new BatchWriteBehind(ejt);
					instances.add(bwb);
					INSTANCES_MONITOR.setValue(instances.size());
					try {
						Common.ctx.getBackgroundProcessing().addWorkItem(bwb);
					} catch (RejectedExecutionException ree) {
						instances.remove(bwb);
						INSTANCES_MONITOR.setValue(instances.size());
						throw ree;
					}
				}
			}
		}

		private final ExtendedJdbcTemplate ejt;

		public BatchWriteBehind(ExtendedJdbcTemplate ejt) {
			this.ejt = ejt;
		}

		public void execute() {
			try {
				BatchWriteBehindEntry[] inserts;
				while (true) {
					synchronized (ENTRIES) {
						if (ENTRIES.size() == 0)
							break;

						inserts = new BatchWriteBehindEntry[ENTRIES.size() < MAX_ROWS ? ENTRIES
								.size()
								: MAX_ROWS];
						ENTRIES.pop(inserts);
						ENTRIES_MONITOR.setValue(ENTRIES.size());
					}

					// Create the sql and parameters
					Object[] params = new Object[inserts.length
							* POINT_VALUE_INSERT_VALUES_COUNT];
					StringBuilder sb = new StringBuilder();
					String sql="";
					//sb.append(POINT_VALUE_INSERT_START);
					for (int i = 0; i < inserts.length; i++) {
						//if (i > 0)
						//	 sb.append(" union all ");
						//sb.append(POINT_VALUE_INSERT_VALUES);
						//inserts[i].writeInto(params, i);
						sql+="insert into pointValues_"+inserts[i].getPointId()+" (dataType, pointValue, ts) values("+inserts[i].outParams()+");";
					}

					// Insert the data
					int retries = 10;
					while (true) {
						try {
							ejt.update(sql);
							break;
						} catch (ConcurrencyFailureException e) {
							if (retries <= 0) {
								LOG
										.error("Concurrency failure saving "
												+ inserts.length
												+ " batch inserts after 10 tries. Data lost.");
								break;
							}

							int wait = (10 - retries) * 100;
							try {
								if (wait > 0) {
									synchronized (this) {
										wait(wait);
									}
								}
							} catch (InterruptedException ie) {
								// no op
							}

							retries--;
						} catch (RuntimeException e) {
							LOG.error("Error saving " + inserts.length
									+ " batch inserts. Data lost.", e);
							break;
						}
					}
				}
			} finally {
				instances.remove(this);
				INSTANCES_MONITOR.setValue(instances.size());
			}
		}

		public int getPriority() {
			return WorkItem.PRIORITY_HIGH;
		}
	}
	
	//根据数据点查找该点在以startTime时间点开始到timestamp间隔的值的集合
	public static final String SELECT_VALUE_BY_ID_IN_TIMESTAMP = " select pointValue from pointvalues where dataPointId = ? and ts > ? and  ts < ? ";
	
	/**
	 * 根据数据点查找该点在以startTime时间点开始到timestamp间隔的值的集合
	 * @param id 数据点ID
	 * @param startTime 开始时间
	 * @param timestamp 时间间隔
	 * @return 改点的值的集合
	 */
	//ok
	public List<Double> getValuesByPointInTimeStamp(long id,long endTime,long timestamp){
		List<Double> values = queryForList("select pointValue from pointValues_"+id+" where  ts > ? and  ts < ? ",new Object[]{
				endTime-timestamp,endTime
				},Double.class);
		return values;
	}
	
	/**
	 * 获取数据库中最早的[Modbus]数据点值得插入时间
	 * @return 最早的时间
	 */
	//ok
	public long getEarliestTime(){
		List<Integer> dataPointIds=new DataPointDao().getDataPointIds();
		String sql="select min(ts)as ts from (";
		for (int i = 0; i < dataPointIds.size(); i++) {
			sql+="select min(ts) as ts from pointValues_"+dataPointIds.get(i)+" where dataType = 3";
			if(i<dataPointIds.size()-1)
				sql+=" union all ";
		}
		long earliestTime = ejt.queryForLong(sql+") as tsTable", new Object[0],-1L);
		return earliestTime;
	}
	
	/**
	 * 获取某个点在两个时间点之间内最小的插入时间
	 * @param from 开始时间
	 * @param to   结束时间
	 * @param dpid 数据点编号
	 * @return 最小的插入时间
	 */
	//ok
	public long getMinTimeByDpid(long from ,long to,int dpid){
		long minTime = ejt.queryForLong(" select min(ts) from pointvalues_"+dpid+" where  ts > ? and ts < ? ", new Object[]{from,to},-1L);
		return minTime;
	}
	
	
}
