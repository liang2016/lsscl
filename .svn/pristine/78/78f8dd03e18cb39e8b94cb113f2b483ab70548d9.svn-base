/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.rt.dataSource.onewire;

import java.util.ArrayList;
import java.util.List;

import com.dalsemi.onewire.container.ADContainer;
import com.dalsemi.onewire.container.HumidityContainer;
import com.dalsemi.onewire.container.OneWireContainer;
import com.dalsemi.onewire.container.OneWireContainer1D;
import com.dalsemi.onewire.container.PotentiometerContainer;
import com.dalsemi.onewire.container.SwitchContainer;
import com.dalsemi.onewire.container.TemperatureContainer;
import com.dalsemi.onewire.utils.Address;
import com.serotonin.mango.vo.dataSource.onewire.OneWirePointLocatorVO;

/**
 *  
 */
public class OneWireContainerInfo {
    private Long address;
    private String description;
    private List<OneWireContainerAttribute> attributes;

    public Long getAddress() {
        return address;
    }

    public void setAddress(Long address) {
        this.address = address;
    }

    public String getAddressString() {
        return Address.toString(address);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<OneWireContainerAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<OneWireContainerAttribute> attributes) {
        this.attributes = attributes;
    }

    public void inspect(OneWireContainer container, byte[] state) {
        description = container.getAlternateNames() + " (" + container.getName() + ")";
        attributes = new ArrayList<OneWireContainerAttribute>();

        if (container instanceof TemperatureContainer)
            attributes.add(new OneWireContainerAttribute(OneWirePointLocatorVO.AttributeTypes.TEMPURATURE));

        if (container instanceof HumidityContainer)
            attributes.add(new OneWireContainerAttribute(OneWirePointLocatorVO.AttributeTypes.HUMIDITY));

        if (container instanceof ADContainer) {
            ADContainer ac = (ADContainer) container;
            OneWireContainerAttribute attr = new OneWireContainerAttribute(
                    OneWirePointLocatorVO.AttributeTypes.AD_VOLTAGE, 0, ac.getNumberADChannels());
            attributes.add(attr);
        }

        if (container instanceof SwitchContainer) {
            SwitchContainer sc = (SwitchContainer) container;
            OneWireContainerAttribute attr = new OneWireContainerAttribute(
                    OneWirePointLocatorVO.AttributeTypes.LATCH_STATE, 0, sc.getNumberChannels(state));
            attributes.add(attr);
        }

        if (container instanceof PotentiometerContainer) {
            PotentiometerContainer pc = (PotentiometerContainer) container;
            OneWireContainerAttribute attr = new OneWireContainerAttribute(
                    OneWirePointLocatorVO.AttributeTypes.WIPER_POSITION, 0, pc.numberOfPotentiometers(state));
            attributes.add(attr);
        }

        if (container instanceof OneWireContainer1D) {
            OneWireContainerAttribute attr = new OneWireContainerAttribute(
                    OneWirePointLocatorVO.AttributeTypes.COUNTER, 12, 4);
            attributes.add(attr);
        }
    }
}
