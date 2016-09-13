/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.view.graphic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.serotonin.mango.view.ImplDefinition;

@Deprecated
// Use ViewComponent instead
abstract public class BaseGraphicRenderer implements GraphicRenderer {
    private static List<ImplDefinition> definitions;

    public static List<ImplDefinition> getImplementations(int dataType) {
        if (definitions == null) {
            List<ImplDefinition> d = new ArrayList<ImplDefinition>();
            d.add(AnalogImageSetRenderer.getDefinition());
            d.add(BasicRenderer.getDefinition());
            d.add(BinaryImageSetRenderer.getDefinition());
            d.add(MultistateImageSetRenderer.getDefinition());
            d.add(BasicImageRenderer.getDefinition());
            d.add(ThumbnailRenderer.getDefinition());
            d.add(DynamicImageRenderer.getDefinition());
            d.add(ScriptRenderer.getDefinition());
            definitions = d;
        }

        List<ImplDefinition> impls = new ArrayList<ImplDefinition>();
        for (ImplDefinition def : definitions) {
            if (def.supports(dataType))
                impls.add(def);
        }
        return impls;
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
        in.readInt();
    }
}
