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
import com.serotonin.mango.DataTypes;
import com.serotonin.mango.rt.dataSource.galil.CommandPointTypeRT;
import com.serotonin.mango.rt.dataSource.galil.PointTypeRT;
import com.serotonin.web.dwr.DwrResponseI18n;
import com.serotonin.web.i18n.LocalizableMessage;

/**
 *  
 */
@JsonRemoteEntity
public class CommandPointTypeVO extends PointTypeVO {
    @Override
    public PointTypeRT createRuntime() {
        return new CommandPointTypeRT(this);
    }

    @Override
    public int typeId() {
        return Types.COMMAND;
    }

    @Override
    public int getDataTypeId() {
        return DataTypes.ALPHANUMERIC;
    }

    @Override
    public LocalizableMessage getDescription() {
        return new LocalizableMessage("dsEdit.galil.pointType.command");
    }

    @Override
    public boolean isSettable() {
        return true;
    }

    @Override
    public void validate(DwrResponseI18n response) {
        // no op
    }

    @Override
    public void addProperties(List<LocalizableMessage> list) {
        // no op
    }

    @Override
    public void addPropertyChanges(List<LocalizableMessage> list, Object from) {
        // no op
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
    }

    private void readObject(ObjectInputStream in) throws IOException {
        int ver = in.readInt();

        // Switch on the version of the class so that version changes can be elegantly handled.
        if (ver == 1) {
            // no op
        }
    }
}
