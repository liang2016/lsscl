/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.vo.dataSource.virtual;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.serotonin.json.JsonRemoteEntity;
import com.serotonin.mango.rt.dataSource.virtual.AlternateBooleanChangeRT;
import com.serotonin.mango.rt.dataSource.virtual.ChangeTypeRT;
import com.serotonin.web.i18n.LocalizableMessage;

@JsonRemoteEntity
public class AlternateBooleanChangeVO extends ChangeTypeVO {
    public static final LocalizableMessage KEY = new LocalizableMessage("dsEdit.virtual.changeType.alternate");

    @Override
    public int typeId() {
        return Types.ALTERNATE_BOOLEAN;
    }

    @Override
    public LocalizableMessage getDescription() {
        return KEY;
    }

    @Override
    public ChangeTypeRT createRuntime() {
        return new AlternateBooleanChangeRT();
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
        in.readInt(); // Read the version. Value is currently not used.
    }
}
