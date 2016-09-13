/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.vo.dataSource.galil;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import com.serotonin.json.JsonRemoteEntity;
import com.serotonin.json.JsonRemoteProperty;
import com.serotonin.mango.DataTypes;
import com.serotonin.mango.rt.dataSource.galil.OutputPointTypeRT;
import com.serotonin.mango.rt.dataSource.galil.PointTypeRT;
import com.serotonin.mango.rt.event.type.AuditEventType;
import com.serotonin.web.dwr.DwrResponseI18n;
import com.serotonin.web.i18n.LocalizableMessage;

/**
 *  
 */
@JsonRemoteEntity
public class OutputPointTypeVO extends PointTypeVO {
    @JsonRemoteProperty
    private int outputId = 1;

    @Override
    public PointTypeRT createRuntime() {
        return new OutputPointTypeRT(this);
    }

    @Override
    public int typeId() {
        return Types.OUTPUT;
    }

    @Override
    public int getDataTypeId() {
        return DataTypes.BINARY;
    }

    @Override
    public LocalizableMessage getDescription() {
        return new LocalizableMessage("dsEdit.galil.pointType.output");
    }

    @Override
    public boolean isSettable() {
        return true;
    }

    @Override
    public void validate(DwrResponseI18n response) {
        if (outputId < 1 || outputId > 80)
            response.addContextualMessage("outputPointType.outputId", "validate.1to80");
    }

    public int getOutputId() {
        return outputId;
    }

    public void setOutputId(int outputId) {
        this.outputId = outputId;
    }

    @Override
    public void addProperties(List<LocalizableMessage> list) {
        AuditEventType.addPropertyMessage(list, "dsEdit.galil.outputNumber", outputId);
    }

    @Override
    public void addPropertyChanges(List<LocalizableMessage> list, Object o) {
        OutputPointTypeVO from = (OutputPointTypeVO) o;
        AuditEventType.maybeAddPropertyChangeMessage(list, "dsEdit.galil.outputNumber", from.outputId, outputId);
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
        out.writeInt(outputId);
    }

    private void readObject(ObjectInputStream in) throws IOException {
        int ver = in.readInt();

        // Switch on the version of the class so that version changes can be elegantly handled.
        if (ver == 1) {
            outputId = in.readInt();
        }
    }
}
