/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.vo.dataSource.http;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;

import com.serotonin.json.JsonRemoteEntity;
import com.serotonin.json.JsonRemoteProperty;
import com.serotonin.mango.rt.dataSource.DataSourceRT;
import com.serotonin.mango.rt.dataSource.http.HttpReceiverDataSourceRT;
import com.serotonin.mango.rt.event.type.AuditEventType;
import com.serotonin.mango.util.ExportCodes;
import com.serotonin.mango.vo.dataSource.DataSourceVO;
import com.serotonin.mango.vo.event.EventTypeVO;
import com.serotonin.util.ArrayUtils;
import com.serotonin.util.IpAddressUtils;
import com.serotonin.util.StringUtils;
import com.serotonin.web.dwr.DwrResponseI18n;
import com.serotonin.web.i18n.LocalizableMessage;

/**
 *  
 */
@JsonRemoteEntity
public class HttpReceiverDataSourceVO extends DataSourceVO<HttpReceiverDataSourceVO> {
    public static final Type TYPE = Type.HTTP_RECEIVER;

    @Override
    protected void addEventTypes(List<EventTypeVO> ets) {
        // no op
    }

    @Override
    public ExportCodes getEventCodes() {
        return null;
    }

    @Override
    public Type getType() {
        return TYPE;
    }

    @Override
    public LocalizableMessage getConnectionDescription() {
        if (ipWhiteList.length == 0 || deviceIdWhiteList.length == 0)
            return new LocalizableMessage("dsEdit.httpReceiver.dsconn.blocked");

        if (ArrayUtils.contains(deviceIdWhiteList, "*")) {
            if (ipWhiteList.length == 1)
                return new LocalizableMessage("dsEdit.httpReceiver.dsconn", ipWhiteList[0]);
            return new LocalizableMessage("dsEdit.httpReceiver.dsconn", ipWhiteList[0] + ", ...");
        }

        if (ArrayUtils.contains(ipWhiteList, "*.*.*.*")) {
            if (deviceIdWhiteList.length == 1)
                return new LocalizableMessage("dsEdit.httpReceiver.dsconn", deviceIdWhiteList[0]);
            return new LocalizableMessage("dsEdit.httpReceiver.dsconn", deviceIdWhiteList[0] + ", ...");
        }

        return new LocalizableMessage("dsEdit.httpReceiver.dsconn.combo");
    }

    @Override
    public DataSourceRT createDataSourceRT() {
        return new HttpReceiverDataSourceRT(this);
    }

    @Override
    public HttpReceiverPointLocatorVO createPointLocator() {
        return new HttpReceiverPointLocatorVO();
    }

    @JsonRemoteProperty
    private String[] ipWhiteList = new String[] { "*.*.*.*" };
    @JsonRemoteProperty
    private String[] deviceIdWhiteList = new String[] { "*" };

    public String[] getIpWhiteList() {
        return ipWhiteList;
    }

    public void setIpWhiteList(String[] ipWhiteList) {
        this.ipWhiteList = ipWhiteList;
    }

    public String[] getDeviceIdWhiteList() {
        return deviceIdWhiteList;
    }

    public void setDeviceIdWhiteList(String[] deviceIdWhiteList) {
        this.deviceIdWhiteList = deviceIdWhiteList;
    }

    @Override
    public void validate(DwrResponseI18n response) {
        super.validate(response);

        for (String ipmask : ipWhiteList) {
            String msg = IpAddressUtils.checkIpMask(ipmask);
            if (msg != null)
                response.addContextualMessage("ipWhiteList", "common.default", msg);
        }

        for (String deviceId : deviceIdWhiteList) {
            if (StringUtils.isEmpty(deviceId))
                response.addContextualMessage("deviceIdWhiteList", "validate.missingDeviceId");
        }
    }

    @Override
    protected void addPropertiesImpl(List<LocalizableMessage> list) {
        AuditEventType.addPropertyMessage(list, "dsEdit.httpReceiver.ipWhiteList", Arrays.toString(ipWhiteList));
        AuditEventType.addPropertyMessage(list, "dsEdit.httpReceiver.deviceWhiteList", Arrays
                .toString(deviceIdWhiteList));
    }

    @Override
    protected void addPropertyChangesImpl(List<LocalizableMessage> list, HttpReceiverDataSourceVO from) {
        if (Arrays.equals(from.ipWhiteList, ipWhiteList))
            AuditEventType.addPropertyChangeMessage(list, "dsEdit.httpReceiver.ipWhiteList", Arrays
                    .toString(from.ipWhiteList), Arrays.toString(ipWhiteList));
        if (Arrays.equals(from.deviceIdWhiteList, deviceIdWhiteList))
            AuditEventType.addPropertyChangeMessage(list, "dsEdit.httpReceiver.deviceWhiteList", Arrays
                    .toString(from.deviceIdWhiteList), Arrays.toString(deviceIdWhiteList));
    }

    //
    // /
    // / Serialization
    // /
    //
    private static final long serialVersionUID = -1;
    private static final int version = 2;

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeInt(version);
        out.writeObject(ipWhiteList);
        out.writeObject(deviceIdWhiteList);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        int ver = in.readInt();

        // Switch on the version of the class so that version changes can be elegantly handled.
        if (ver == 1) {
            ipWhiteList = (String[]) in.readObject();
            deviceIdWhiteList = new String[] { "*" };
        }
        else if (ver == 2) {
            ipWhiteList = (String[]) in.readObject();
            deviceIdWhiteList = (String[]) in.readObject();
        }
    }
}
