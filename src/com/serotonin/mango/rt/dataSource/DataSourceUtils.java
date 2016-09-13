/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.serotonin.mango.DataTypes;
import com.serotonin.mango.rt.dataImage.types.AlphanumericValue;
import com.serotonin.mango.rt.dataImage.types.BinaryValue;
import com.serotonin.mango.rt.dataImage.types.MangoValue;
import com.serotonin.mango.rt.dataImage.types.NumericValue;
import com.serotonin.mango.view.text.MultistateRenderer;
import com.serotonin.mango.view.text.MultistateValue;
import com.serotonin.mango.view.text.TextRenderer;
import com.serotonin.web.i18n.LocalizableException;
import com.serotonin.web.i18n.LocalizableMessage;

/**
 *  
 */
public class DataSourceUtils {
    public static MangoValue getValue(Pattern valuePattern, String data, int dataTypeId, String binary0Value,
            TextRenderer textRenderer, DecimalFormat valueFormat, String pointName) throws LocalizableException {
        if (data == null)
            throw new LocalizableException(new LocalizableMessage("event.valueParse.noData", pointName));

        Matcher matcher = valuePattern.matcher(data);
        if (matcher.find()) {
            String valueStr = matcher.group(1);
            if (valueStr == null)
                valueStr = "";

            return getValue(valueStr, dataTypeId, binary0Value, textRenderer, valueFormat, pointName);
        }

        throw new NoMatchException(new LocalizableMessage("event.valueParse.noValue", pointName));
    }

    public static long getValueTime(long time, Pattern timePattern, String data, DateFormat timeFormat, String pointName)
            throws LocalizableException {
        if (data == null)
            throw new LocalizableException(new LocalizableMessage("event.valueParse.noData", pointName));

        // Get the time.
        long valueTime = time;
        if (timePattern != null) {
            Matcher matcher = timePattern.matcher(data);
            if (matcher.find()) {
                String timeStr = matcher.group(1);
                try {
                    valueTime = timeFormat.parse(timeStr).getTime();
                }
                catch (ParseException e) {
                    if (pointName == null)
                        throw new LocalizableException(new LocalizableMessage("event.valueParse.timeParse", timeStr));
                    throw new LocalizableException(new LocalizableMessage("event.valueParse.timeParsePoint", timeStr,
                            pointName));
                }
            }
            else
                throw new LocalizableException(new LocalizableMessage("event.valueParse.noTime", pointName));
        }

        return valueTime;
    }

    public static MangoValue getValue(String valueStr, int dataTypeId, String binary0Value, TextRenderer textRenderer,
            DecimalFormat valueFormat, String pointName) throws LocalizableException {
        if (dataTypeId == DataTypes.ALPHANUMERIC)
            return new AlphanumericValue(valueStr);

        if (dataTypeId == DataTypes.BINARY)
            return new BinaryValue(!valueStr.equals(binary0Value));

        if (dataTypeId == DataTypes.MULTISTATE) {
            if (textRenderer instanceof MultistateRenderer) {
                List<MultistateValue> multistateValues = ((MultistateRenderer) textRenderer).getMultistateValues();
                for (MultistateValue multistateValue : multistateValues) {
                    if (multistateValue.getText().equalsIgnoreCase(valueStr))
                        return new com.serotonin.mango.rt.dataImage.types.MultistateValue(multistateValue.getKey());
                }
            }

            try {
                return com.serotonin.mango.rt.dataImage.types.MultistateValue.parseMultistate(valueStr);
            }
            catch (NumberFormatException e) {
                if (pointName == null)
                    throw new LocalizableException(new LocalizableMessage("event.valueParse.textParse", valueStr));
                throw new LocalizableException(new LocalizableMessage("event.valueParse.textParsePoint", valueStr,
                        pointName));
            }
        }

        if (dataTypeId == DataTypes.NUMERIC) {
            try {
                if (valueFormat != null)
                    return new NumericValue(valueFormat.parse(valueStr).doubleValue());
                return NumericValue.parseNumeric(valueStr);
            }
            catch (NumberFormatException e) {
                if (pointName == null)
                    throw new LocalizableException(new LocalizableMessage("event.valueParse.numericParse", valueStr));
                throw new LocalizableException(new LocalizableMessage("event.valueParse.numericParsePoint", valueStr,
                        pointName));
            }
            catch (ParseException e) {
                if (pointName == null)
                    throw new LocalizableException(new LocalizableMessage("event.valueParse.generalParse", e
                            .getMessage(), valueStr));
                throw new LocalizableException(new LocalizableMessage("event.valueParse.generalParsePoint", e
                        .getMessage(), valueStr, pointName));
            }
        }

        return null;
    }
}
