/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.view.stats;

import com.serotonin.mango.rt.dataImage.types.MangoValue;

/**
 *  
 */
public class MultistateDataQuantizer extends AbstractDataQuantizer {
    private MangoValue lastValue;

    public MultistateDataQuantizer(long start, long end, int buckets, DataQuantizerCallback callback) {
        super(start, end, buckets, callback);
    }

    @Override
    protected void periodData(MangoValue value) {
        lastValue = value;
    }

    @Override
    protected MangoValue donePeriod(int valueCounter) {
        return lastValue;
    }
    //    
    // private static List<ReportDataValue> quantize(List<ReportDataValue> data, long start, long end, int buckets) {
    // final List<ReportDataValue> result = new ArrayList<ReportDataValue>();
    //        
    // MultistateDataQuantizer mdq = new MultistateDataQuantizer(start, end, buckets, new DataQuantizerCallback() {
    // public void quantizedData(Number value, long time) {
    // result.add(new ReportDataValue(value, time));
    // }
    // });
    // for (ReportDataValue value : data)
    // mdq.data((Integer)value.getValue(), value.getTime());
    // mdq.done();
    //        
    // return result;
    // }
    //    
    // public static void main(String[] args) {
    // List<ReportDataValue> data = new ArrayList<ReportDataValue>();
    // data.add(new ReportDataValue(1, 1));
    // data.add(new ReportDataValue(1, 2));
    // data.add(new ReportDataValue(1, 3));
    // data.add(new ReportDataValue(2, 4));
    // data.add(new ReportDataValue(1, 10));
    // data.add(new ReportDataValue(1, 11));
    // data.add(new ReportDataValue(2, 15));
    // data.add(new ReportDataValue(2, 16));
    // data.add(new ReportDataValue(3, 17));
    // data.add(new ReportDataValue(1, 20));
    // data.add(new ReportDataValue(2, 21));
    // data.add(new ReportDataValue(2, 30));
    // data.add(new ReportDataValue(2, 50));
    // data.add(new ReportDataValue(3, 51));
    // data.add(new ReportDataValue(2, 52));
    // data.add(new ReportDataValue(2, 53));
    // data.add(new ReportDataValue(1, 54));
    // data.add(new ReportDataValue(3, 55));
    // data.add(new ReportDataValue(3, 56));
    // data.add(new ReportDataValue(3, 57));
    // data.add(new ReportDataValue(2, 58));
    // data.add(new ReportDataValue(1, 90));
    //        
    // System.out.println(quantize(data, 0, 100, 800));
    // System.out.println(quantize(data, 0, 100, 100));
    // System.out.println(quantize(data, 0, 100, 90));
    // System.out.println(quantize(data, 0, 100, 50));
    // System.out.println(quantize(data, 0, 100, 7));
    // System.out.println(quantize(data, 0, 100, 6));
    // System.out.println(quantize(data, 0, 100, 5));
    // System.out.println(quantize(data, 0, 100, 4));
    // System.out.println(quantize(data, 0, 100, 1));
    // System.out.println(quantize(data, 1, 90, 13));
    // }
}