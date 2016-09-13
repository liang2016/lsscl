package com.serotonin.mango.view.component;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import com.serotonin.json.JsonRemoteEntity;
import com.serotonin.json.JsonRemoteProperty;
import com.serotonin.mango.DataTypes;
import com.serotonin.mango.rt.dataImage.PointValueTime;
import com.serotonin.mango.view.ImplDefinition;
import com.serotonin.util.SerializationHelper;

@JsonRemoteEntity
public class FlashPointComponent extends PointComponent {
	
   public static ImplDefinition DEFINITION = new ImplDefinition("flashSimple", "FLASH_SIMPLE", "graphic.flashSimple", new int[] {
            DataTypes.BINARY, DataTypes.MULTISTATE, DataTypes.NUMERIC, DataTypes.ALPHANUMERIC });


   	@JsonRemoteProperty
   	private boolean flashColor = true;
   	
   	@JsonRemoteProperty
   	private boolean flashflag = false;

	public boolean isFlashflag() {
		return flashflag;
	}

	public void setFlashflag(boolean flashflag) {
		this.flashflag = flashflag;
	}

	@JsonRemoteProperty
   	private double max = 0;
   
   	@JsonRemoteProperty
   	private double min = 0;
   
    @JsonRemoteProperty
    private boolean displayPointName=true;

    @JsonRemoteProperty
    private String styleAttribute;
	
	@Override
	public void addDataToModel(Map<String, Object> model,
			PointValueTime pointValue) {
		// TODO Auto-generated method stub
        model.put("displayPointName", displayPointName);
        model.put("styleAttribute", styleAttribute);
        model.put("flashColor", flashColor);
        model.put("flashflag", flashflag);
        model.put("max", max);
        model.put("min", min);
        model.put("flashflag", flashflag);

	}

	@Override
	public String snippetName() {
		// TODO Auto-generated method stub
        return "basicContent";
	}

	@Override
	public ImplDefinition definition() {
		// TODO Auto-generated method stub
        return DEFINITION;
	}
	
	private void flashPage(){
		double pointValue = 0;
		if (this.tgetDataPoint() == null) {
			pointValue = 0;			
		}else if (this.tgetDataPoint().lastValue()==null){
			pointValue = 0;
		}else if(this.tgetDataPoint().lastValue().getValue() == null) {
			pointValue = 0;
		}else {
			pointValue = this.tgetDataPoint().lastValue().getValue().getDoubleValue();
		}
		
		if (flashColor && (pointValue < min || pointValue > max)) {
			flashflag = true;
		}else {
			flashflag = false;
		}
	}
	
   	public boolean isFlashColor() {
		return flashColor;
	}

	public void setFlashColor(boolean flashColor) {
		this.flashColor = flashColor;
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

	public boolean isDisplayPointName() {
		return displayPointName;
	}

	public void setDisplayPointName(boolean displayPointName) {
		this.displayPointName = displayPointName;
	}

	public String getStyleAttribute() {
		return styleAttribute;
	}

	public void setStyleAttribute(String styleAttribute) {
		this.styleAttribute = styleAttribute;
	}
	
    //
    // /
    // / Serialization
    // /
    //
    private static final long serialVersionUID = -1;
    private static final int version = 1;

    private void writeObject(ObjectOutputStream out) throws IOException {
        flashPage();
        out.writeInt(version);
        out.writeBoolean(flashflag);
        out.writeDouble(min);
        out.writeDouble(max);
        out.writeBoolean(flashColor);
        out.writeBoolean(displayPointName);
        SerializationHelper.writeSafeUTF(out, styleAttribute);
        
    }

    private void readObject(ObjectInputStream in) throws IOException {
        int ver = in.readInt();

        // Switch on the version of the class so that version changes can be elegantly handled.
        if (ver == 1) {
        	flashflag = in.readBoolean();
            min = in.readDouble();
            max = in.readDouble();
        	flashColor = in.readBoolean();
            displayPointName = in.readBoolean();
            styleAttribute = SerializationHelper.readSafeUTF(in);
        }
    }

}
