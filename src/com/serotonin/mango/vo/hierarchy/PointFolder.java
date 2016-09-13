/*
    LssclM2M - http://www.lsscl.com
    Copyright (C) 2006-2011 Lsscl ES Technologies Inc.
     
    
     
     
     
     

     
     
     
     

     
    
 */
package com.serotonin.mango.vo.hierarchy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.serotonin.db.IntValuePair;
import com.serotonin.json.JsonArray;
import com.serotonin.json.JsonException;
import com.serotonin.json.JsonObject;
import com.serotonin.json.JsonReader;
import com.serotonin.json.JsonRemoteEntity;
import com.serotonin.json.JsonRemoteProperty;
import com.serotonin.json.JsonSerializable;
import com.serotonin.json.JsonValue;
import com.serotonin.mango.Common;
import com.serotonin.mango.db.dao.DataPointDao;
import com.serotonin.mango.util.LocalizableJsonException;
import com.serotonin.mango.vo.DataPointVO;

/**
 *  
 * 
 */
@JsonRemoteEntity
public class PointFolder implements JsonSerializable {
    private int id = Common.NEW_ID;
    @JsonRemoteProperty
    private String name;
    /*
     *scopeId
     */
    private int scopeId;
    @JsonRemoteProperty(innerType = PointFolder.class)
    private List<PointFolder> subfolders = new ArrayList<PointFolder>();

    private List<IntValuePair> points = new ArrayList<IntValuePair>();

    public PointFolder() {
        // no op
    }

    public PointFolder(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addSubfolder(PointFolder subfolder) {
        subfolders.add(subfolder);
    }

    public void addDataPoint(IntValuePair point) {
        points.add(point);
    }

    public void removeDataPoint(int dataPointId) {
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).getKey() == dataPointId) {
                points.remove(i);
                return;
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<IntValuePair> getPoints() {
        return points;
    }

    public void setPoints(List<IntValuePair> points) {
        this.points = points;
    }

    public List<PointFolder> getSubfolders() {
        return subfolders;
    }

    public void setSubfolders(List<PointFolder> subfolders) {
        this.subfolders = subfolders;
    }

    boolean findPoint(List<PointFolder> path, int pointId) {
        boolean found = false;
        for (IntValuePair point : points) {
            if (point.getKey() == pointId) {
                found = true;
                break;
            }
        }

        if (!found) {
            for (PointFolder subfolder : subfolders) {
                found = subfolder.findPoint(path, pointId);
                if (found)
                    break;
            }
        }

        if (found)
            path.add(this);

        return found;
    }

    void copyFoldersFrom(PointFolder that) {
        for (PointFolder thatSub : that.subfolders) {
            PointFolder thisSub = new PointFolder(thatSub.getId(), thatSub.getName());
            thisSub.copyFoldersFrom(thatSub);
            subfolders.add(thisSub);
        }
    }

    public PointFolder getSubfolder(String name) {
        for (PointFolder subfolder : subfolders) {
            if (subfolder.name.equals(name))
                return subfolder;
        }
        return null;
    }

    //
    //
    // Serialization
    //
    @Override
    public void jsonSerialize(Map<String, Object> map) {
        DataPointDao dataPointDao = new DataPointDao();
        List<String> pointList = new ArrayList<String>();
        for (IntValuePair p : points) {
            DataPointVO dp = dataPointDao.getDataPoint(p.getKey());
            if (dp != null)
                pointList.add(dp.getXid());
        }
        map.put("points", pointList);
    }

    @Override
    public void jsonDeserialize(JsonReader reader, JsonObject json) throws JsonException {
        JsonArray jsonPoints = json.getJsonArray("points");
        if (jsonPoints != null) {
            points.clear();
            DataPointDao dataPointDao = new DataPointDao();

            for (JsonValue jv : jsonPoints.getElements()) {
                String xid = jv.toJsonString().getValue();

                DataPointVO dp = dataPointDao.getDataPoint(xid);
                if (dp == null)
                    throw new LocalizableJsonException("emport.error.missingPoint", xid);

                points.add(new IntValuePair(dp.getId(), dp.getName()));
            }
        }
    }

	public int getScopeId() {
		return scopeId;
	}

	public void setScopeId(int scopeId) {
		this.scopeId = scopeId;
	}
}
