/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.web.taglib;

import java.util.regex.Pattern;

import com.serotonin.mango.Common;
import com.serotonin.mango.DataTypes;
import com.serotonin.mango.rt.dataImage.PointValueTime;
import com.serotonin.mango.rt.dataImage.types.MangoValue;
import com.serotonin.mango.rt.dataImage.types.NumericValue;
import com.serotonin.mango.view.text.TextRenderer;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.util.StringUtils;
import com.serotonin.web.taglib.DateFunctions;

public class Functions {
    public static String getHtmlText(DataPointVO point, PointValueTime pointValue) {
        String text = point.getTextRenderer().getText(pointValue, TextRenderer.HINT_FULL);
        String colour = point.getTextRenderer().getColour(pointValue);
        return getHtml(colour, text, point.getPointLocator().getDataTypeId() == DataTypes.ALPHANUMERIC);
    }
    public static String getHtmlTextForSMS(DataPointVO point, PointValueTime pointValue) {
        String text = point.getTextRenderer().getText(pointValue, TextRenderer.HINT_FULL);
        return text;
    }
    public static String getRenderedText(DataPointVO point, PointValueTime pointValue) {
        return point.getTextRenderer().getText(pointValue, TextRenderer.HINT_FULL);
    }

    public static String getRawText(DataPointVO point, PointValueTime pointValue) {
        String result = point.getTextRenderer().getText(pointValue, TextRenderer.HINT_RAW);
        if (!StringUtils.isEmpty(result))
            return encodeDQuot(result);
        return result;
    }

    public static String getHtmlTextValue(DataPointVO point, MangoValue value) {
        return getHtmlTextValue(point, value, TextRenderer.HINT_FULL);
    }

    public static String getSpecificHtmlTextValue(DataPointVO point, double value) {
        return getHtmlTextValue(point, new NumericValue(value), TextRenderer.HINT_SPECIFIC);
    }

    private static String getHtmlTextValue(DataPointVO point, MangoValue value, int hint) {
        String text = point.getTextRenderer().getText(value, hint);
        String colour = point.getTextRenderer().getColour(value);
        return getHtml(colour, text, point.getPointLocator().getDataTypeId() == DataTypes.ALPHANUMERIC);
    }

    private static String getHtml(String colour, String text, boolean detectOverflow) {
        String result;

        if (text != null && detectOverflow && text.length() > 30) {
            text = encodeDQuot(text);
            if (StringUtils.isEmpty(colour))
                result = "<input type='text' readonly='readonly' class='ovrflw' value=\"" + text + "\"/>";
            else
                result = "<input type='text' readonly='readonly' class='ovrflw' style='color:" + colour + ";' value=\""
                        + text + "\"/>";
        }
        else {
            if (StringUtils.isEmpty(colour))
                result = text;
            else
                result = "<span style='color:" + colour + ";'>" + text + "</span>";
        }

        return result;
    }

    public static String getTime(PointValueTime pointValue) {
        if (pointValue != null)
            return DateFunctions.getTime(pointValue.getTime());
        return null;
    }

    public static String padZeros(int i, int len) {
        return StringUtils.pad(Integer.toString(i), '0', len);
    }

    public static String encodeDQuot(String s) {
        return s.replaceAll("\"", "&quot;");
    }

    public static String escapeScripts(String s) {
        String result = Pattern.compile("<script", Pattern.CASE_INSENSITIVE).matcher(s).replaceAll("&lt;script");
        result = Pattern.compile("</script", Pattern.CASE_INSENSITIVE).matcher(result).replaceAll("&lt;/script");
        return result;
    }

    public static String envString(String key, String defaultValue) {
        return Common.getEnvironmentProfile().getString(key, defaultValue);
    }

    public static boolean envBoolean(String key, boolean defaultValue) {
        return Common.getEnvironmentProfile().getBoolean(key, defaultValue);
    }
}
