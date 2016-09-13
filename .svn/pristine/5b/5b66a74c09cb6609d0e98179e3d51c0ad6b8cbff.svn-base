/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.vo.report;

import java.io.PrintWriter;
import java.util.ResourceBundle;
import  java.util.List;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.serotonin.mango.view.export.CsvWriter;
import com.serotonin.mango.view.text.TextRenderer;
import com.serotonin.mango.vo.DataPointVO;
import com.serotonin.web.i18n.I18NUtils;

/**
 *  
 */
public class ReportCsvStreamer implements ReportDataStreamHandler {
    private final PrintWriter out;

    // Working fields
    private TextRenderer textRenderer;
    private final String[] data = new String[6];
    private final DateTimeFormatter dtf = DateTimeFormat.forPattern("MM-dd-yyyy HH:mm:ss");
    private final CsvWriter csvWriter = new CsvWriter();
    private List<DataPointVO> pointNames;
    public List<DataPointVO> getPointNames() {
		return pointNames;
	}
	public void setPointNames(List<DataPointVO> pointNames) {
		this.pointNames = pointNames;
	}
	public ReportCsvStreamer(PrintWriter out, ResourceBundle bundle) {
        this.out = out;

        // Write the headers.
        data[0] = I18NUtils.getMessage(bundle, "reports.pointName");
        data[1] = I18NUtils.getMessage(bundle, "common.time");
        data[2] = I18NUtils.getMessage(bundle, "common.value");
        data[3] = I18NUtils.getMessage(bundle, "reports.rendered");
        data[4] = I18NUtils.getMessage(bundle, "common.annotation");
        out.write(csvWriter.encodeRow(data));
    }
    public ReportCsvStreamer(PrintWriter out, ResourceBundle bundle,List<DataPointVO> pointNames) {
        this.out = out;
        this.pointNames=pointNames;
        // Write the headers.

        data[0] = I18NUtils.getMessage(bundle, "common.time");
        for (int i = 0; i < pointNames.size(); i++) {
    		if(i==pointNames.size()){
    			data[i+1]=pointNames.get(i)+"\r\n";
    		}
    		else{
    			data[i+1]=pointNames.get(i).getName();
    		}
    	}
        out.write(csvWriter.encodeRow(data));
    }
    
    
    public void startPoint(ReportPointInfo pointInfo) {
       // data[0] = pointInfo.getExtendedName();
       // textRenderer = pointInfo.getTextRenderer();
    }

    public void pointData(ReportDataValue rdv) {
        data[0] = dtf.print(new DateTime(rdv.getTime())).toString();
        if (rdv.getValue() == null)
            data[1] = data[2] = null;
        else {
            data[1] = rdv.getValue().toString();
           // data[2] = textRenderer.getText(rdv.getValue(), TextRenderer.HINT_FULL);
        }

        //data[4] = rdv.getAnnotation();
    }
    public void pointData(ReportDataValue rdv,ReportDataValue oldRdv) {
    	if(oldRdv.getReportPointId()==0){
    		data[0] =dtf.print(new DateTime(rdv.getTime())).toString();
    	}
    	else if(rdv.getReportPointId()==oldRdv.getReportPointId()){
    		out.write("\r\n");
    		out.write(csvWriter.encodeRow(data));
    		data[0] =dtf.print(new DateTime(rdv.getTime())).toString();
    	}
    	else{
    		long time=Math.abs(rdv.getTime()-oldRdv.getTime());
    		if(time<5000)
    		{
    			// data[0] = "\n"+dtf.print(new DateTime(rdv.getTime()));
    		}
    		else{
    			out.write("\r\n");
    			out.write(csvWriter.encodeRow(data));
    			data[0] =dtf.print(new DateTime(rdv.getTime())).toString();
    		}
    	}
    	for (int i = 0; i <pointNames.size();i++) {
			if(pointNames.get(i).getId()==rdv.getReportPointId()){
				   data[i+1] = rdv.getValue().toString();
			}
			else{
				  // data[i+1] =null;
			}
			if(data[i+1]==pointNames.get(i).getName()){
				data[i+1] =null;
			}
		}
    }
    public void done() {
        out.flush();
        out.close();
    }
}
