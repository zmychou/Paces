package com.zmychou.paces.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.JsonReader;

import com.zmychou.paces.constant.NetworkConstant;
import com.zmychou.paces.database.RunningData;
import com.zmychou.paces.database.RunningEntryUtils;
import com.zmychou.paces.database.server.UserInfoEntryUtil;
import com.zmychou.paces.io.JsonFileParser;
import com.zmychou.paces.io.RunningDataJsonFileWriter;
import com.zmychou.paces.login.LoginActivity;
import com.zmychou.paces.running.RunningRecords;
import com.zmychou.paces.running.RunningRecordsActivity;
import com.zmychou.paces.running.RunningRecordsAdapter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ming on 17-6-13.
 */
public class DownloadRecordTask extends AsyncTask<Void, Void, ArrayList<RunningRecords>> {

    private Context mContext;

    public DownloadRecordTask(Context context) {
        mContext = context;
    }

    @Override
    protected ArrayList<RunningRecords> doInBackground(Void... params) {
        try {
            URL url = new URL(NetworkConstant.SERVER_ADDR_BASE + "MyServlet");
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setDoOutput(true);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(huc.getOutputStream()));
            SharedPreferences sp = mContext.getSharedPreferences(LoginActivity.TAG, Context.MODE_PRIVATE);
            writer.write("{\"_id\":\"");
            writer.write(sp.getString(UserInfoEntryUtil._ID, null) + "\",");
            writer.write("\"msgType\":\"");
            writer.write(14 + "\"}");
            writer.flush();
            writer.close();

            InputStream in = huc.getInputStream();
            JsonReader jr = new JsonReader(new InputStreamReader(in));
            return parseArray(jr);

        } catch (MalformedURLException e) {

        } catch (IOException e) {}
        return null;
    }

//        @Override
//        protected void onPostExecute(ArrayList<RunningRecords> records) {
//            mAdapter = new RunningRecordsAdapter(records);
//            mAdapter.registerActivity(RunningRecordsActivity.this);
//
//            mRecyclerView.setAdapter(mAdapter);
//            mAdapter.notifyDataSetChanged();
//
//        }

    private ArrayList<RunningRecords> parseArray(JsonReader reader) {
        ArrayList<RunningRecords> list = new ArrayList<>();
        try {
            reader.beginArray();
            while (reader.hasNext()) {
                list.add(parseObject(reader));
            }
            reader.endArray();
            return list;
        } catch (IOException e) {}
        return null;
    }

    private RunningRecords parseObject(JsonReader reader) {
        RunningRecords records = new RunningRecords();
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                switch (reader.nextName()) {
                    case "_timestamp":
                        records.setTimestamp(reader.nextString());
                        break;
                    case "_distance":
                        records.setDistance(reader.nextString());
                        break;
                    case "_duration":
                        records.setDuration(reader.nextString());
                        break;
                    case "latLngFile":
                        records.setLatLngFile(reader.nextString());
                        getFiles(records.getLatLngFile());
                        break;
                    default:reader.skipValue();
                        break;
                }
            }
            reader.endObject();
            return records;
        } catch (IOException e) {}

        return null;
    }

    private ArrayList<String> getFiles(String pathOffset) {
        ArrayList<String> list = new ArrayList<>();
        try {
            URL url = new URL(NetworkConstant.SERVER_ADDR_BASE + pathOffset);
            InputStream in = url.openStream();
            JsonReader jr = new JsonReader(new InputStreamReader(in));
            jr.beginObject();
            while (jr.hasNext()) {
                switch (jr.nextName()) {
                    case "files":

                        readArray(jr);
                        break;
                    default:jr.skipValue();
                        break;
                }
            }
            jr.endObject();

        } catch (MalformedURLException e) {

        } catch (IOException e) {}
        return null;
    }

    private void readArray(JsonReader reader) {
        try {
            reader.beginArray();
            RunningEntryUtils utils = new RunningEntryUtils(mContext);
            RunningData data = null;
            while (reader.hasNext()) {
                data = readObject(reader);
                RunningDataJsonFileWriter writer = new RunningDataJsonFileWriter(
                        mContext, data
                );
                data.setLatLngFile(writer.save());
                utils.insert(data);
            }
            reader.endArray();
        } catch (IOException e) {}
    }

    private RunningData readObject(JsonReader reader) {
        RunningData data = RunningData.getInstance();
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                switch (reader.nextName()) {
                    case "timestamp":
                        data.setTimestamp(reader.nextLong());
                        break;
                    case "finish":
                        data.setFinishTime(reader.nextLong());
                        break;
                    case "duration":
                        data.setDuration(reader.nextLong());
                        break;
                    case "distance":
                        data.setDistance(reader.nextInt());
                        break;
                    case "start":
                        data.setStartTime(reader.nextLong());
                        break;
                    case "sequence":
                        data.setSequenceNumber(reader.nextInt());
                        break;
                    case "calories":
                        data.setCalories(reader.nextInt());
                        break;
                    case "steps":
                        data.setSteps(reader.nextInt());
                        break;
                    case "latlngs":
                        data.setLatLngs(JsonFileParser.parserArray(reader));
                        break;
                    default:reader.skipValue();
                        break;
                }
            }
            reader.endObject();
            return data;
        } catch (IOException e) {}
        return null;

    }
}

