/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.meta;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import com.serotonin.ShouldNeverHappenException;
import com.serotonin.db.IntValuePair;
import com.serotonin.io.StreamUtils;
import com.serotonin.mango.Common;
import com.serotonin.mango.DataTypes;
import com.serotonin.mango.rt.RuntimeManager;
import com.serotonin.mango.rt.dataImage.DataPointRT;
import com.serotonin.mango.rt.dataImage.IDataPoint;
import com.serotonin.mango.rt.dataImage.PointValueTime;
import com.serotonin.mango.rt.dataImage.types.AlphanumericValue;
import com.serotonin.mango.rt.dataImage.types.BinaryValue;
import com.serotonin.mango.rt.dataImage.types.MangoValue;
import com.serotonin.mango.rt.dataImage.types.MultistateValue;
import com.serotonin.mango.rt.dataImage.types.NumericValue;
import com.serotonin.web.i18n.LocalizableMessage;

/**
 *  
 */
public class ScriptExecutor {
	private static final String SCRIPT_PREFIX = "function __scriptExecutor__() {";
	private static final String SCRIPT_SUFFIX = "\r\n}\r\n__scriptExecutor__();";
	private static String SCRIPT_FUNCTION_PATH;
	private static String FUNCTIONS;

	public static void setScriptFunctionPath(String path) {
		SCRIPT_FUNCTION_PATH = path;
	}

	public Map<String, IDataPoint> convertContext(List<IntValuePair> context)
			throws DataPointStateException {
		RuntimeManager rtm = Common.ctx.getRuntimeManager();

		Map<String, IDataPoint> converted = new HashMap<String, IDataPoint>();
		for (IntValuePair contextEntry : context) {
			DataPointRT point = rtm.getDataPoint(contextEntry.getKey());
			if (point == null)
				throw new DataPointStateException(contextEntry.getKey(),
						new LocalizableMessage("event.meta.pointMissing"));
			converted.put(contextEntry.getValue(), point);
		}

		return converted;
	}

	public PointValueTime execute(String script,
			Map<String, IDataPoint> context, long runtime, int dataTypeId,
			long timestamp) throws ScriptException, ResultTypeException {
		ensureFunctions();

		// Create the script engine.
		ScriptEngineManager manager;
		try {
			manager = new ScriptEngineManager();
		} catch (Exception e) {
			throw new ScriptException(e);
		}
		ScriptEngine engine = manager.getEngineByName("js");
		// engine.getContext().setErrorWriter(new PrintWriter(System.err));
		// engine.getContext().setWriter(new PrintWriter(System.out));

		// Create the wrapper object context.
		WrapperContext wrapperContext = new WrapperContext(runtime);

		// Add constants to the context.
		engine.put("SECOND", Common.TimePeriods.SECONDS);
		engine.put("MINUTE", Common.TimePeriods.MINUTES);
		engine.put("HOUR", Common.TimePeriods.HOURS);
		engine.put("DAY", Common.TimePeriods.DAYS);
		engine.put("WEEK", Common.TimePeriods.WEEKS);
		engine.put("MONTH", Common.TimePeriods.MONTHS);
		engine.put("YEAR", Common.TimePeriods.YEARS);
		engine.put("CONTEXT", wrapperContext);

		// Put the context variables into the engine with engine scope.
		for (String varName : context.keySet()) {
			IDataPoint point = context.get(varName);
			int dt = point.getDataTypeId();
			if (dt == DataTypes.BINARY)
				engine.put(varName, new BinaryPointWrapper(point,
						wrapperContext));
			else if (dt == DataTypes.MULTISTATE)
				engine.put(varName, new MultistatePointWrapper(point,
						wrapperContext));
			else if (dt == DataTypes.NUMERIC)
				engine.put(varName, new NumericPointWrapper(point,
						wrapperContext));
			else if (dt == DataTypes.ALPHANUMERIC)
				engine.put(varName, new AlphanumericPointWrapper(point,
						wrapperContext));
			else
				throw new ShouldNeverHappenException("Unknown data type id: "
						+ point.getDataTypeId());
		}

		boolean ispd = script.contains("power density");
		if (ispd) {

			int istart = script.indexOf("PDSEARCH_START,");
			int iend = script.indexOf(",PDSEARCH_END");
			if (istart > 0 && iend > 0) {

				String pdStr = script.substring(istart + 15, iend);
				String[] pralist = pdStr.split(",");

				try {
					File f = new File("E:\\work\\副本节能量计算表.xls");
					FileInputStream fis;
					fis = new FileInputStream(f);
					jxl.Workbook rwb = Workbook.getWorkbook(Common.getFis());
					Sheet[] sheet = rwb.getSheets();
					if (sheet.length == 2) {
						int ibar = 0;
						int ikw = 0;
						if (pralist.length == 3) {
							Sheet rs;
							if (pralist[0].equals("1")) {
								rs = rwb.getSheet(0);
							} else {
								rs = rwb.getSheet(1);
							}

							IDataPoint pointbar = context.get(pralist[1]);
							IDataPoint pointkw = context.get(pralist[2]);

							double bar = new BigDecimal(pointbar
									.getPointValue().getDoubleValue())
									.setScale(1, BigDecimal.ROUND_HALF_UP)
									.doubleValue();
							int kw = new BigDecimal(pointkw.getPointValue()
									.getDoubleValue()).setScale(0,
									BigDecimal.ROUND_HALF_UP).intValue();

							if (rs.getRows() > 1) {
								Cell[] row1 = rs.getRow(0);
								for (int j = 1; j < row1.length; j++) {
									String barstr = row1[j].getContents().trim();
									if (!barstr.equals("") && bar==Double.valueOf(barstr)){
										ibar = j;
										break;
									}
								}
							}

							if (rs.getColumns() > 1) {
								Cell[] col1 = rs.getColumn(0);
								for (int k = 1; k < col1.length; k++) {
									String kwstr = col1[k].getContents().trim();
									if (!kwstr.equals("") && Integer.valueOf(kwstr) == kw) {
										ikw = k;
										break;
									}
								}
							}
							String searchResult = "0";
							if (ibar == 0 || ikw == 0) {
							} else {
								searchResult = rs.getCell(ibar, ikw)
										.getContents();
							}
							// "PDSEARCH_START,1,p7768,p7770,PDSEARCH_END"
							String aa = "\"PDSEARCH_START," + pdStr
									+ ",PDSEARCH_END\"";
							int asssa = script.indexOf(aa);
							script = script.replace(aa, searchResult);
							System.out.print(searchResult);
						}
					}
					fis.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		// Create the script.
		script = SCRIPT_PREFIX + script + SCRIPT_SUFFIX + FUNCTIONS;

		// Execute.
		Object result;
		try {
			result = engine.eval(script);
		} catch (ScriptException e) {
			throw prettyScriptMessage(e);
		}

		// Check if a timestamp was set
		Object ts = engine.get("TIMESTAMP");
		if (ts != null) {
			// Check the type of the object.
			if (ts instanceof Number)
				// Convert to long
				timestamp = ((Number) ts).longValue();
			// else if (ts instanceof ScriptableObject &&
			// "Date".equals(((ScriptableObject)ts).getClassName())) {
			// // A native date
			// // It turns out to be a crazy hack to try and get the value from
			// a native date, and the Rhino source
			// // code FTP server is not responding, so, going to have to leave
			// this for now.
			// }
		}

		MangoValue value;
		if (result == null) {
			if (dataTypeId == DataTypes.BINARY)
				value = new BinaryValue(false);
			else if (dataTypeId == DataTypes.MULTISTATE)
				value = new MultistateValue(0);
			else if (dataTypeId == DataTypes.NUMERIC)
				value = new NumericValue(0);
			else if (dataTypeId == DataTypes.ALPHANUMERIC)
				value = new AlphanumericValue("");
			else
				value = null;
		} else if (result instanceof AbstractPointWrapper)
			value = ((AbstractPointWrapper) result).getValueImpl();
		// See if the type matches.
		else if (dataTypeId == DataTypes.BINARY && result instanceof Boolean)
			value = new BinaryValue((Boolean) result);
		else if (dataTypeId == DataTypes.MULTISTATE && result instanceof Number)
			value = new MultistateValue(((Number) result).intValue());
		else if (dataTypeId == DataTypes.NUMERIC && result instanceof Number)
			value = new NumericValue(((Number) result).doubleValue());
		else if (dataTypeId == DataTypes.ALPHANUMERIC
				&& result instanceof String)
			value = new AlphanumericValue((String) result);
		else
			// If not, ditch it.
			throw new ResultTypeException(new LocalizableMessage(
					"event.script.convertError", result,
					DataTypes.getDataTypeMessage(dataTypeId)));

		return new PointValueTime(value, timestamp);
	}

	public static ScriptException prettyScriptMessage(ScriptException e) {
		while (e.getCause() instanceof ScriptException)
			e = (ScriptException) e.getCause();

		// Try to make the error message look a bit nicer.
		List<String> exclusions = new ArrayList<String>();
		exclusions.add("sun.org.mozilla.javascript.internal.EcmaError: ");
		exclusions
				.add("sun.org.mozilla.javascript.internal.EvaluatorException: ");
		String message = e.getMessage();
		for (String exclude : exclusions) {
			if (message.startsWith(exclude))
				message = message.substring(exclude.length());
		}
		return new ScriptException(message, e.getFileName(), e.getLineNumber(),
				e.getColumnNumber());
	}

	private static void ensureFunctions() {
		if (FUNCTIONS == null) {
			if (SCRIPT_FUNCTION_PATH == null)
				SCRIPT_FUNCTION_PATH = Common.ctx.getServletContext()
						.getRealPath("WEB-INF/scripts/scriptFunctions.js");
			StringWriter sw = new StringWriter();
			FileReader fr = null;
			try {
				fr = new FileReader(SCRIPT_FUNCTION_PATH);
				StreamUtils.transfer(fr, sw);
			} catch (Exception e) {
				throw new ShouldNeverHappenException(e);
			} finally {
				try {
					if (fr != null)
						fr.close();
				} catch (IOException e) {
					// no op
				}
			}
			FUNCTIONS = sw.toString();
		}
	}
}
