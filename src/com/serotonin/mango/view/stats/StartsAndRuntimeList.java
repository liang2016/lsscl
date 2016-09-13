/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.view.stats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.serotonin.mango.rt.dataImage.PointValueTime;
import com.serotonin.mango.rt.dataImage.types.BinaryValue;
import com.serotonin.mango.rt.dataImage.types.MangoValue;
import com.serotonin.mango.rt.dataImage.types.MultistateValue;
import com.serotonin.util.ObjectUtils;

/**
 *  
 */
public class StartsAndRuntimeList implements StatisticsGenerator {
    private final List<StartsAndRuntime> data = new ArrayList<StartsAndRuntime>();

    public StartsAndRuntimeList(PointValueTime startValue, List<? extends IValueTime> values, long start, long end) {
        this(startValue == null ? null : startValue.getValue(), values, start, end);
    }

    public StartsAndRuntimeList(MangoValue startValue, List<? extends IValueTime> values, long start, long end) {
        this(startValue, start, end);
        for (IValueTime vt : values)
            addValueTime(vt);
        done();
    }

    private final long end;
    private long lastTime = -1;
    private long realStart = -1;
    private MangoValue lastValue;
    private StartsAndRuntime sar;

    public StartsAndRuntimeList(MangoValue startValue, long start, long end) {
        this.end = end;
        if (startValue != null) {
            lastTime = start;
            lastValue = startValue;
            sar = get(startValue);
        }
    }

    public void addValueTime(IValueTime vt) {
        if (lastTime == -1)
            lastTime = vt.getTime();

        if (realStart == -1)
            realStart = lastTime;

        if (!ObjectUtils.isEqual(vt.getValue(), lastValue)) {
            // Update the last value stats, if any.
            if (sar != null)
                sar.runtime += vt.getTime() - lastTime;

            lastValue = vt.getValue();
            lastTime = vt.getTime();

            sar = get(lastValue);
            sar.starts++;
        }
    }

    public void done() {
        if (sar != null)
            sar.runtime += end - lastTime;

        if (realStart == -1)
            realStart = lastTime;

        // Calculate runtime percentages.
        for (StartsAndRuntime s : data)
            s.calculateRuntimePercentage(end - realStart);

        // Sort by value.
        Collections.sort(data, new Comparator<StartsAndRuntime>() {
            public int compare(StartsAndRuntime o1, StartsAndRuntime o2) {
                return o1.value.compareTo(o2.value);
            }
        });
    }

    public long getEnd() {
        return end;
    }

    public long getRealStart() {
        return realStart;
    }

    public Map<Object, StartsAndRuntime> getStartsAndRuntime() {
        Map<Object, StartsAndRuntime> result = new HashMap<Object, StartsAndRuntime>();
        for (StartsAndRuntime sar : data)
            result.put(sar.getValue(), sar);
        return result;
    }

    public List<StartsAndRuntime> getData() {
        return data;
    }

    public StartsAndRuntime get(Object value) {
        return get(MangoValue.objectToValue(value));
    }

    public StartsAndRuntime get(MangoValue value) {
        for (StartsAndRuntime sar : data) {
            if (ObjectUtils.isEqual(sar.value, value))
                return sar;
        }

        StartsAndRuntime sar = new StartsAndRuntime();
        sar.value = value;
        data.add(sar);

        return sar;
    }

    public String getHelp() {
        return toString();
    }

    @Override
    public String toString() {
        return "{realStart: " + realStart + ", end: " + end + ", data: " + data.toString() + "}";
    }

    public static void main(String[] args) {
        {
            MultistateValue startValue = new MultistateValue(3);
            List<PointValueTime> values = new ArrayList<PointValueTime>();
            values.add(new PointValueTime(1, 2000));
            values.add(new PointValueTime(2, 3000));
            values.add(new PointValueTime(2, 5000));
            values.add(new PointValueTime(3, 8000));
            values.add(new PointValueTime(1, 9000));
            values.add(new PointValueTime(3, 10000));
            values.add(new PointValueTime(3, 12000));
            values.add(new PointValueTime(2, 16000));

            System.out.println(new StartsAndRuntimeList(startValue, values, 1000, 21000));
            System.out.println(new StartsAndRuntimeList(startValue, values, 1500, 26000));
            System.out.println(new StartsAndRuntimeList((MangoValue) null, values, 1000, 21000));
            System.out.println(new StartsAndRuntimeList((MangoValue) null, values, 1500, 26000));

            System.out.println(new StartsAndRuntimeList((MangoValue) null, new ArrayList<PointValueTime>(), 0, 30000));
            System.out.println(new StartsAndRuntimeList(startValue, new ArrayList<PointValueTime>(), 0, 30000));
        }

        System.out.println();

        {
            BinaryValue startValue = BinaryValue.ONE;
            List<PointValueTime> values = new ArrayList<PointValueTime>();
            values.add(new PointValueTime(true, 2000));
            values.add(new PointValueTime(false, 3000));
            values.add(new PointValueTime(false, 5000));
            values.add(new PointValueTime(false, 8000));
            values.add(new PointValueTime(true, 9000));
            values.add(new PointValueTime(true, 10000));
            values.add(new PointValueTime(false, 12000));
            values.add(new PointValueTime(true, 16000));

            System.out.println(new StartsAndRuntimeList(startValue, values, 1000, 21000));
            System.out.println(new StartsAndRuntimeList(startValue, values, 1500, 26000));
            System.out.println(new StartsAndRuntimeList((MangoValue) null, values, 1000, 21000));
            System.out.println(new StartsAndRuntimeList((MangoValue) null, values, 1500, 26000));

            System.out.println(new StartsAndRuntimeList((MangoValue) null, new ArrayList<PointValueTime>(), 0, 30000));
            System.out.println(new StartsAndRuntimeList(startValue, new ArrayList<PointValueTime>(), 0, 30000));
        }
    }
}
