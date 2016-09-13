package com.serotonin.mango.view.component;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import com.serotonin.json.JsonRemoteEntity;
import com.serotonin.json.JsonRemoteProperty;
import com.serotonin.mango.Common;
import com.serotonin.mango.DataTypes;
import com.serotonin.mango.rt.dataImage.PointValueTime;
import com.serotonin.mango.view.ImplDefinition;
import com.serotonin.util.SerializationHelper;

@JsonRemoteEntity
public class FlashReportPointComponent extends PointComponent {

	public static ImplDefinition DEFINITION = new ImplDefinition("flashReport",
			"FLASH_REPORT", "graphic.flashReport", new int[] {
					DataTypes.BINARY, DataTypes.MULTISTATE, DataTypes.NUMERIC,
					DataTypes.ALPHANUMERIC });

	@JsonRemoteProperty
	private String linkUrl = "http://";
	
	@JsonRemoteProperty
	private int durationPeriods = 1;

	@JsonRemoteProperty
	private long time = 0;
	
	@JsonRemoteProperty
	private long duration = 0;
	
	@JsonRemoteProperty
	private int durationType = Common.TimePeriods.DAYS;

	@JsonRemoteProperty
	private boolean flashColor = true;

	@JsonRemoteProperty
	private boolean flashflag = false;

	@JsonRemoteProperty
	private double max = 0;

	@JsonRemoteProperty
	private double min = 0;

	@JsonRemoteProperty
	private boolean displayPointName = true;

	@Override
	public void addDataToModel(Map<String, Object> model,
			PointValueTime pointValue) {
		// TODO Auto-generated method stub
		model.put("displayPointName", displayPointName);
		model.put("flashColor", flashColor);
		model.put("flashflag", flashflag);
		model.put("max", max);
		model.put("min", min);
		model.put("duration",duration);
		model.put("durationPeriods",durationPeriods);
		model.put("durationType",durationType);
		model.put("linkUrl",linkUrl);
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

	private void flashPage() {
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
		duration = Common.getMillis(durationType, durationPeriods);
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
		//out.writeLong(time);
		out.writeLong(duration);
		out.writeInt(durationPeriods);
		out.writeInt(durationType);
		out.writeUTF(linkUrl);
	}

	private void readObject(ObjectInputStream in) throws IOException {
		int ver = in.readInt();

		time = System.currentTimeMillis();
		// Switch on the version of the class so that version changes can be
		// elegantly handled.
		if (ver == 1) {
			flashflag = in.readBoolean();
			min = in.readDouble();
			max = in.readDouble();
			flashColor = in.readBoolean();
			displayPointName = in.readBoolean();
			duration = in.readLong();
			durationPeriods = in.readInt();
			durationType = in.readInt();
			linkUrl = in.readUTF();
		}
	}


	public int getDurationPeriods() {
		return durationPeriods;
	}

	public void setDurationPeriods(int durationPeriods) {
		this.durationPeriods = durationPeriods;
	}
	
	public boolean isFlashflag() {
		return flashflag;
	}

	public void setFlashflag(boolean flashflag) {
		this.flashflag = flashflag;
	}


	public int getDurationType() {
		return durationType;
	}

	public void setDurationType(int durationType) {
		this.durationType = durationType;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public long getDuration() {
		return  duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
}
