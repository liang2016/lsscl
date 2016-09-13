/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataImage;

import java.io.Serializable;
import java.util.Calendar;

import com.serotonin.mango.rt.dataImage.types.AlphanumericValue;
import com.serotonin.mango.rt.dataImage.types.BinaryValue;
import com.serotonin.mango.rt.dataImage.types.MangoValue;
import com.serotonin.mango.rt.dataImage.types.MultistateValue;
import com.serotonin.mango.rt.dataImage.types.NumericValue;
import com.serotonin.mango.view.stats.IValueTime;
import com.serotonin.util.ObjectUtils;
import com.serotonin.web.taglib.DateFunctions;

/**
 * The simple value of a point at a given time.
 * 
 * @see AnnotatedPointValueTime
 *  
 */
public class PointValueTime implements Serializable, IValueTime {
    private static final long serialVersionUID = -1;

    public static boolean equalValues(PointValueTime pvt1, PointValueTime pvt2) {
        if (pvt1 == null && pvt2 == null)
            return true;
        if (pvt1 == null || pvt2 == null)
            return false;
        return ObjectUtils.isEqual(pvt1.getValue(), pvt2.getValue());
    }

    public static MangoValue getValue(PointValueTime pvt) {
        if (pvt == null)
            return null;
        return pvt.getValue();
    }

    private final MangoValue value;
    private final long time;
    private Calendar ca = Calendar.getInstance();
    public PointValueTime(MangoValue value, long time) {
        this.value = value;
//		ca.setTimeInMillis(time);
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
        this.time = time;
    }

    public PointValueTime(boolean value, long time) {
        this(new BinaryValue(value), time);
    }

    public PointValueTime(int value, long time) {
        this(new MultistateValue(value), time);
    }

    public PointValueTime(double value, long time) {
        this(new NumericValue(value), time);
    }

    public PointValueTime(String value, long time) {
        this(new AlphanumericValue(value), time);
    }

    public long getTime() {
        return time;
    }

    public MangoValue getValue() {
        return value;
    }

    public boolean isAnnotated() {
        return false;
    }

    public double getDoubleValue() {
        return value.getDoubleValue();
    }

    public String getStringValue() {
        return value.getStringValue();
    }

    public int getIntegerValue() {
        return value.getIntegerValue();
    }

    public boolean getBooleanValue() {
        return value.getBooleanValue();
    }

    @Override
    public boolean equals(Object o) {
        PointValueTime that = (PointValueTime) o;
        if (time != that.time)
            return false;
        return ObjectUtils.isEqual(value, that.value);
    }

    @Override
    public String toString() {
        return "PointValueTime(" + value + "@" + DateFunctions.getTime(time) + ")";
    }
}
