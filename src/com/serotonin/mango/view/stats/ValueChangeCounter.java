/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.view.stats;

import java.util.ArrayList;
import java.util.List;

import com.serotonin.mango.rt.dataImage.PointValueTime;
import com.serotonin.mango.rt.dataImage.types.AlphanumericValue;
import com.serotonin.mango.rt.dataImage.types.MangoValue;
import com.serotonin.util.ObjectUtils;

/**
 *  
 */
public class ValueChangeCounter implements StatisticsGenerator {
    // Calculated values.
    private int changes;

    // State values
    private MangoValue lastValue;

    public ValueChangeCounter(PointValueTime startValue, List<? extends IValueTime> values) {
        this(startValue == null ? null : startValue.getValue(), values);
    }

    public ValueChangeCounter(MangoValue startValue, List<? extends IValueTime> values) {
        this(startValue);
        for (IValueTime p : values)
            addValueTime(p);
        done();
    }

    public ValueChangeCounter(MangoValue startValue) {
        lastValue = startValue;
    }

    public void addValueTime(IValueTime vt) {
        if (!ObjectUtils.isEqual(lastValue, vt.getValue())) {
            changes++;
            lastValue = vt.getValue();
        }
    }

    public void done() {
        // no op
    }

    public int getChangeCount() {
        return changes;
    }

    public String getHelp() {
        return toString();
    }

    @Override
    public String toString() {
        return "{changeCount: " + changes + "}";
    }

    public static void main(String[] args) {
        AlphanumericValue startValue = new AlphanumericValue("asdf");
        List<PointValueTime> values = new ArrayList<PointValueTime>();
        values.add(new PointValueTime("asdf", 2000));
        values.add(new PointValueTime("zxcv", 3000));
        values.add(new PointValueTime("qwer", 4000));
        values.add(new PointValueTime("wert", 5000));
        values.add(new PointValueTime("wert", 6000));
        values.add(new PointValueTime("erty", 8000));

        System.out.println(new ValueChangeCounter(startValue, values));
        System.out.println(new ValueChangeCounter(startValue, values));
        System.out.println(new ValueChangeCounter((MangoValue) null, values));
        System.out.println(new ValueChangeCounter((MangoValue) null, values));
        System.out.println(new ValueChangeCounter((MangoValue) null, new ArrayList<PointValueTime>()));
        System.out.println(new ValueChangeCounter(startValue, new ArrayList<PointValueTime>()));
    }
}
