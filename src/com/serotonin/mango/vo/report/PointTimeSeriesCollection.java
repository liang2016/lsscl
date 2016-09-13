/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.vo.report;

import java.awt.Paint;
import java.util.ArrayList;
import java.util.List;

import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import com.serotonin.mango.rt.dataImage.PointValueTime;

/**
 *  
 */
public class PointTimeSeriesCollection {
    private TimeSeriesCollection numericTimeSeriesCollection;
    private List<Paint> numericPaint;
    private List<DiscreteTimeSeries> discreteTimeSeriesCollection;

    public void addNumericTimeSeries(TimeSeries numericTimeSeries) {
        addNumericTimeSeries(numericTimeSeries, null);
    }

    public void addNumericTimeSeries(TimeSeries numericTimeSeries, Paint paint) {
        if (numericTimeSeriesCollection == null) {
            numericTimeSeriesCollection = new TimeSeriesCollection();
            numericPaint = new ArrayList<Paint>();
        }
        numericTimeSeriesCollection.addSeries(numericTimeSeries);
        numericPaint.add(paint);
    }

    public void addDiscreteTimeSeries(DiscreteTimeSeries discreteTimeSeries) {
        if (discreteTimeSeriesCollection == null)
            discreteTimeSeriesCollection = new ArrayList<DiscreteTimeSeries>();
        discreteTimeSeriesCollection.add(discreteTimeSeries);
    }

    public boolean hasData() {
        return hasNumericData() || hasDiscreteData();
    }

    public boolean hasNumericData() {
        return numericTimeSeriesCollection != null;
    }

    public boolean hasDiscreteData() {
        return discreteTimeSeriesCollection != null;
    }

    public boolean hasMultiplePoints() {
        int count = 0;
        if (numericTimeSeriesCollection != null)
            count += numericTimeSeriesCollection.getSeriesCount();
        if (discreteTimeSeriesCollection != null)
            count += discreteTimeSeriesCollection.size();
        return count > 1;
    }

    public TimeSeriesCollection getNumericTimeSeriesCollection() {
        return numericTimeSeriesCollection;
    }

    public List<Paint> getNumericPaint() {
        return numericPaint;
    }

    public int getDiscreteValueCount() {
        int count = 0;

        if (discreteTimeSeriesCollection != null) {
            for (DiscreteTimeSeries dts : discreteTimeSeriesCollection)
                count += dts.getDiscreteValueCount();
        }

        return count;
    }

    public TimeSeriesCollection createTimeSeriesCollection(double numericMin, double spacingInterval) {
        TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection();

        int intervalIndex = 1;
        for (DiscreteTimeSeries dts : discreteTimeSeriesCollection) {
            TimeSeries ts = new TimeSeries(dts.getName(), null, null, Second.class);

            for (PointValueTime pvt : dts.getValueTimes())
                ImageChartUtils.addSecond(ts, pvt.getTime(), numericMin
                        + (spacingInterval * (dts.getValueIndex(pvt.getValue()) + intervalIndex)));

            timeSeriesCollection.addSeries(ts);

            intervalIndex += dts.getDiscreteValueCount();
        }

        return timeSeriesCollection;
    }

    public int getDiscreteSeriesCount() {
        return discreteTimeSeriesCollection.size();
    }

    public DiscreteTimeSeries getDiscreteTimeSeries(int index) {
        return discreteTimeSeriesCollection.get(index);
    }
}
