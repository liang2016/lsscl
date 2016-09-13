/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.view.component;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.serotonin.json.JsonRemoteEntity;
import com.serotonin.json.JsonRemoteProperty;
import com.serotonin.mango.view.ImplDefinition;
import com.serotonin.mango.vo.User;
import com.serotonin.util.SerializationHelper;

/**
 *  
 */
@JsonRemoteEntity
public class HtmlComponent extends ViewComponent {
    public static ImplDefinition DEFINITION = new ImplDefinition("html", "HTML", "graphic.html", null);

    @JsonRemoteProperty
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public ImplDefinition definition() {
        return DEFINITION;
    }

    @Override
    public void validateDataPoint(User user, boolean makeReadOnly) {
        // no op
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public boolean containsValidVisibleDataPoint(int dataPointId) {
        return false;
    }

    public String getStaticContent() {
        return content;
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

        SerializationHelper.writeSafeUTF(out, content);
    }

    private void readObject(ObjectInputStream in) throws IOException {
        int ver = in.readInt();

        // Switch on the version of the class so that version changes can be elegantly handled.
        if (ver == 1)
            content = SerializationHelper.readSafeUTF(in);
    }
}
