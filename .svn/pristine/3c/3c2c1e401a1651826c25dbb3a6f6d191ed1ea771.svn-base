/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.vo.dataSource.virtual;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import com.serotonin.json.JsonRemoteEntity;
import com.serotonin.json.JsonRemoteProperty;
import com.serotonin.mango.rt.dataSource.virtual.ChangeTypeRT;
import com.serotonin.mango.rt.dataSource.virtual.IncrementAnalogChangeRT;
import com.serotonin.mango.rt.event.type.AuditEventType;
import com.serotonin.web.i18n.LocalizableMessage;

@JsonRemoteEntity
public class IncrementAnalogChangeVO extends ChangeTypeVO {
    public static final LocalizableMessage KEY = new LocalizableMessage("dsEdit.virtual.changeType.increment");

    @JsonRemoteProperty
    private double min;
    @JsonRemoteProperty
    private double max;
    @JsonRemoteProperty
    private double change;
    @JsonRemoteProperty
    private boolean roll;

    @Override
    public int typeId() {
        return Types.INCREMENT_ANALOG;
    }

    @Override
    public LocalizableMessage getDescription() {
        return KEY;
    }

    @Override
    public ChangeTypeRT createRuntime() {
        return new IncrementAnalogChangeRT(this);
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public boolean isRoll() {
        return roll;
    }

    public void setRoll(boolean roll) {
        this.roll = roll;
    }

    @Override
    public void addProperties(List<LocalizableMessage> list) {
        super.addProperties(list);
        AuditEventType.addPropertyMessage(list, "dsEdit.virtual.min", min);
        AuditEventType.addPropertyMessage(list, "dsEdit.virtual.max", max);
        AuditEventType.addPropertyMessage(list, "dsEdit.virtual.change", change);
        AuditEventType.addPropertyMessage(list, "dsEdit.virtual.roll", roll);
    }

    @Override
    public void addPropertyChanges(List<LocalizableMessage> list, Object o) {
        super.addPropertyChanges(list, o);
        IncrementAnalogChangeVO from = (IncrementAnalogChangeVO) o;
        AuditEventType.maybeAddPropertyChangeMessage(list, "dsEdit.virtual.min", from.min, min);
        AuditEventType.maybeAddPropertyChangeMessage(list, "dsEdit.virtual.max", from.max, max);
        AuditEventType.maybeAddPropertyChangeMessage(list, "dsEdit.virtual.change", from.change, change);
        AuditEventType.maybeAddPropertyChangeMessage(list, "dsEdit.virtual.roll", from.roll, roll);
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
        out.writeDouble(min);
        out.writeDouble(max);
        out.writeDouble(change);
        out.writeBoolean(roll);
    }

    private void readObject(ObjectInputStream in) throws IOException {
        int ver = in.readInt();

        // Switch on the version of the class so that version changes can be elegantly handled.
        if (ver == 1) {
            min = in.readDouble();
            max = in.readDouble();
            change = in.readDouble();
            roll = in.readBoolean();
        }
    }
}
