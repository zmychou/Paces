package com.zmychou.paces.io;

import android.content.Context;

import com.amap.api.maps.model.LatLng;
import com.zmychou.paces.database.RunningData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <pre>
 * Package    :com.zmychou.paces.io
 * Author     : zmychou
 * Create time:17-4-10
 * Last Modify:
 * Email      :zmychou@gmail.com
 * Version    :V1.0
 * description: Template pattern to implements this module
 * </pre>
 */
public class RunningDataJsonFileWriter extends AbstractMyFileWriter {
    RunningData mData;
    public RunningDataJsonFileWriter(Context context, RunningData data) {
        super(context);
        mData = data;
    }

    @Override
    protected String getSubdirectory() {
        return "file/json";
    }
    @Override
    protected String getFileName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd-HHmmss");
        return "tmp_run_trace_"+sdf.format(new Date(mData.getStartTime()))+".json";
    }

    @Override
    protected String getContent() {
        JSONObject jo = new JSONObject();
        try {
            JSONArray ja = new JSONArray();
            for (LatLng latLng : mData.getLatLngs()) {
                ja.put(new JSONObject().put("lat",latLng.latitude).put("lng",latLng.longitude));
            }
            jo.put("sequence",mData.getSequenceNumber());
            jo.put("timestamp",mData.getTimestamp());
            jo.put("start",mData.getStartTime());
            jo.put("finish",mData.getFinishTime());
            jo.put("distance",mData.getDistance());
            jo.put("duration",mData.getDuration());
            jo.put("calories",mData.getCalories());
            jo.put("steps",mData.getSteps());
            jo.put("latlngs",ja);

        } catch (JSONException e) {}

        return jo.toString();
    }
}
