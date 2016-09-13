/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.view.chart;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import com.serotonin.json.JsonRemoteEntity;
import com.serotonin.json.JsonRemoteProperty;
import com.serotonin.mango.Common;
import com.serotonin.mango.DataTypes;
import com.serotonin.mango.rt.dataImage.DataPointRT;
import com.serotonin.mango.view.ImplDefinition;
import com.serotonin.mango.vo.DataPointVO;

/**
 *  
 */
@JsonRemoteEntity
public class ImageFlipbookRenderer extends BaseChartRenderer {
    private static ImplDefinition definition = new ImplDefinition("chartRendererImageFlipbook", "FLIPBOOK",
            "chartRenderer.flipbook", new int[] { DataTypes.IMAGE });

    public static ImplDefinition getDefinition() {
        return definition;
    }

    public String getTypeName() {
        return definition.getName();
    }

    @JsonRemoteProperty
    private int limit;

    public ImageFlipbookRenderer() {
        // no op
    }

    public ImageFlipbookRenderer(int limit) {
        this.limit = limit;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void addDataToModel(Map<String, Object> model, DataPointVO point) {
        DataPointRT rt = Common.ctx.getRuntimeManager().getDataPoint(point.getId());
        if (rt != null)
            model.put("chartData", rt.getLatestPointValues(limit));
    }

    public ImplDefinition getDef() {
        return definition;
    }

    //
    // /
    // / Serialization
    // /
    //
    private static final long serialVersionUID = -1;
    private static final int version = 1;

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(version);
        out.writeInt(limit);
    }

    private void readObject(ObjectInputStream in) throws IOException {
        int ver = in.readInt();

        // Switch on the version of the class so that version changes can be elegantly handled.
        if (ver == 1) {
            limit = in.readInt();
        }
    }
}
