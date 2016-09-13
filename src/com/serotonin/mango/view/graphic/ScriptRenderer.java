/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.view.graphic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.serotonin.mango.DataTypes;
import com.serotonin.mango.view.ImplDefinition;
import com.serotonin.util.SerializationHelper;

/**
 *  
 */
@Deprecated
// Use ViewComponent instead
public class ScriptRenderer extends BaseGraphicRenderer {
    private static ImplDefinition definition = new ImplDefinition("graphicRendererScript", "SCRIPT", "graphic.script",
            new int[] { DataTypes.ALPHANUMERIC, DataTypes.BINARY, DataTypes.MULTISTATE, DataTypes.NUMERIC, });

    public static ImplDefinition getDefinition() {
        return definition;
    }

    public String getTypeName() {
        return definition.getName();
    }

    public ImplDefinition getDef() {
        return definition;
    }

    private String script;

    public ScriptRenderer(String script) {
        this.script = script;
    }

    public String getScript() {
        return script;
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
        SerializationHelper.writeSafeUTF(out, script);
    }

    private void readObject(ObjectInputStream in) throws IOException {
        int ver = in.readInt();

        // Switch on the version of the class so that version changes can be elegantly handled.
        if (ver == 1)
            script = SerializationHelper.readSafeUTF(in);
    }
}
