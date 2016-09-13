/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.vo.dataSource.virtual;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;

import com.serotonin.json.JsonRemoteEntity;
import com.serotonin.mango.rt.dataSource.virtual.ChangeTypeRT;
import com.serotonin.mango.rt.dataSource.virtual.IncrementMultistateChangeRT;
import com.serotonin.mango.rt.event.type.AuditEventType;
import com.serotonin.web.i18n.LocalizableMessage;

@JsonRemoteEntity
public class IncrementMultistateChangeVO extends ChangeTypeVO {
    public static final LocalizableMessage KEY = new LocalizableMessage("dsEdit.virtual.changeType.increment");

    private int[] values = new int[0];
    private boolean roll;

    @Override
    public int typeId() {
        return Types.INCREMENT_MULTISTATE;
    }

    @Override
    public LocalizableMessage getDescription() {
        return KEY;
    }

    @Override
    public ChangeTypeRT createRuntime() {
        return new IncrementMultistateChangeRT(this);
    }

    public boolean isRoll() {
        return roll;
    }

    public void setRoll(boolean roll) {
        this.roll = roll;
    }

    public int[] getValues() {
        return values;
    }

    public void setValues(int[] values) {
        this.values = values;
    }

    @Override
    public void addProperties(List<LocalizableMessage> list) {
        super.addProperties(list);
        AuditEventType.addPropertyMessage(list, "dsEdit.virtual.values", Arrays.toString(values));
        AuditEventType.addPropertyMessage(list, "dsEdit.virtual.roll", roll);
    }

    @Override
    public void addPropertyChanges(List<LocalizableMessage> list, Object o) {
        super.addPropertyChanges(list, o);
        IncrementMultistateChangeVO from = (IncrementMultistateChangeVO) o;
        if (Arrays.equals(from.values, values))
            AuditEventType.addPropertyChangeMessage(list, "dsEdit.virtual.values", Arrays.toString(from.values), Arrays
                    .toString(values));
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
        out.writeObject(values);
        out.writeBoolean(roll);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        int ver = in.readInt();

        // Switch on the version of the class so that version changes can be elegantly handled.
        if (ver == 1) {
            values = (int[]) in.readObject();
            roll = in.readBoolean();
        }
    }
}
