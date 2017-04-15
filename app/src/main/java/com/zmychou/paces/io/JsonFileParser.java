package com.zmychou.paces.io;

import android.content.Context;
import android.os.Environment;
import android.util.*;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;
import com.zmychou.paces.R;

import java.io.*;
import java.util.ArrayList;

public class JsonFileParser {
	protected File openFile(Context context,String fileName) {
        String[] ss = fileName.split("/");
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			File root = Environment.getExternalStorageDirectory();
			File path = new File(root,"Paces/file/json");
			if (!path.exists()){
				path.mkdirs();
			}
			return new File(path,ss[ss.length-1]);
		}
		Toast.makeText(context, R.string.external_device_invalid, Toast.LENGTH_SHORT).show();
		return null;
	}
    //"tmp_run_trace_20170411-180131.json"

    public ArrayList<ArrayList<LatLng>> parserLatLngArrays(Context context,String... fileNames) {
        ArrayList<ArrayList<LatLng>> ll = new ArrayList<>();
        for (String fileName : fileNames) {
            ll.add(parserLatLngArray(context,fileName));
        }
        return ll;
    }

    public ArrayList<LatLng> parserLatLngArray(Context context,String fileName) {
        ArrayList<LatLng> list = null;
        try {
            File file = openFile(context,fileName);
            JsonReader jr = new JsonReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            jr.beginObject();
            while (jr.hasNext()) {
                switch (jr.nextName()) {
                    case "latlngs":
                        list =  parserArray(jr);
                        break;
                    default:
                        jr.skipValue();
                        break;
                }
            }
            jr.endObject();
            jr.close();
        } catch (FileNotFoundException e) {
            // TODO: 17-4-11  log the exception
        } catch (IOException e) {
            // TODO: 17-4-11  log the exception
        }
        return list;
    }
	public ArrayList<String> parser(Context context,String fileName) {
        ArrayList<String> list = new ArrayList<>();
		try {
			File file = openFile(context,fileName);
		    JsonReader jr = new JsonReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			jr.beginObject();
			while (jr.hasNext()) {
                switch (jr.nextName()) {
                    case "timestamp":
                        Log.e("timestamp", jr.nextLong() + "");
                        break;
                    case "finish":
                        Log.e("finish", jr.nextLong() + "");
                        break;
                    case "start":
                        Log.e("start", jr.nextLong() + "");
                        break;
                    case "duration":
                        Log.e("duration", jr.nextInt() + "");
                        break;
                    case "distance":
                        Log.e("distance", jr.nextInt() + "");
                        break;
                    case "steps":
                        Log.e("steps", jr.nextInt() + "");
                        break;
//                    case "latlngs":
//                        ArrayList<LatLng> list =  parserArray(jr);
//                        for (LatLng ll : list) {
//                            Log.e("Latitude:",ll.latitude+"");
//                        }
//                        break;
                    default:
                        jr.skipValue();
                        break;
                }
			}
            jr.endObject();
            jr.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {

        }
		return null;
	}
    public ArrayList<LatLng> parserArray(JsonReader reader) {
        ArrayList<LatLng> list = new ArrayList<>();
        try {
            reader.beginArray();
            LatLng tmp ;
            while (reader.hasNext()) {
                tmp = parserLatLng(reader);
                if (tmp != null) {
                    list.add(tmp);
                }
            }
            reader.endArray();
        } catch (IOException e) {}
        return list;
    }
    public LatLng parserLatLng(JsonReader reader) {
        try {
            reader.beginObject();
            double lat = 0;
            double lng = 0;
            String name;
            while (reader.hasNext()) {
                name = reader.nextName();
                if ("lat".equals(name)) {
                    lat = reader.nextDouble();
                } else if ("lng".equals(name)) {
                    lng = reader.nextDouble();
                } else reader.skipValue();
            }
            reader.endObject();
            return new LatLng(lat,lng);
        } catch (Exception e) {}
        return null;
    }
}
