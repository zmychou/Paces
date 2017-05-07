package com.zmychou.paces.running;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.PolylineOptions;
import com.zmychou.paces.R;
import com.zmychou.paces.database.RunningEntryUtils;
import com.zmychou.paces.database.server.RunningDataEntryUtil;
import com.zmychou.paces.database.server.UserInfoEntryUtil;
import com.zmychou.paces.io.JsonFileParser;
import com.zmychou.paces.login.LoginActivity;
import com.zmychou.paces.network.JsonKey;
import com.zmychou.paces.network.MsgTypeConstant;
import com.zmychou.paces.network.UploadFileRequests;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewRunningRecordActivity extends AppCompatActivity {

    MapView mMapView;
    AMap mMap;

    //Use to get the max rectangle that user had reach on the map
    private double mRightLongitude;
    private double mLeftLongitude;
    private double mUpLatitude;
    private double mDownLatitude;

    class PrepareDataTask extends AsyncTask<String,Void,ArrayList<ArrayList<LatLng>>> {
        @Override
        protected ArrayList<ArrayList<LatLng>> doInBackground(String... params) {
            Cursor cursor = getRecord(params[0]);
            int count = cursor.getCount();
            String[] filePath = new String[count];
            for (int i = 0; i < count; i++) {
                cursor.moveToPosition(i);
                filePath[i]
                        = cursor.getString(cursor.getColumnIndex(RunningEntryUtils.LATLNG_FILE_PATH));
            }

            JsonFileParser parser = new JsonFileParser();
            ArrayList<ArrayList<LatLng>> ll = parser.parserLatLngArrays(
                    ViewRunningRecordActivity.this,filePath[0]);
            if (ll != null) {
                setStartPosition(ll.get(0).get(0));
            }
            for (ArrayList<LatLng> latLngs : ll) {
                for (LatLng latLng : latLngs) {
                    setMaxRectangle(latLng);
                }
            }
            return ll;
        }

        @Override
        protected void onPostExecute(ArrayList<ArrayList<LatLng>> arrayLists) {
            super.onPostExecute(arrayLists);

            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.color(getResources().getColor(R.color.colorOrangeLight));
            polylineOptions.width(8);
            for (ArrayList l : arrayLists) {
                polylineOptions.setPoints(l);
            }
            mMap.addPolyline(polylineOptions);
            LatLng center = new LatLng((mUpLatitude + mDownLatitude) / 2,
                    (mRightLongitude + mLeftLongitude) / 2);


            mMap.moveCamera(CameraUpdateFactory.newLatLngBoundsRect(
                    new LatLngBounds(new LatLng(mUpLatitude,mRightLongitude),
                            new LatLng(mDownLatitude,mRightLongitude)),1,1,1,1));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(center));
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_running_record);
        Intent intent = getIntent();
        String timestamp = intent.getStringExtra(RunningRecordsAdapter.TIME_STAMP);

        mMapView = (MapView) findViewById(R.id.mv_details_record);
        mMapView.onCreate(savedInstanceState);
        mMap = mMapView.getMap();

        new PrepareDataTask().execute(timestamp);

        uploadFile(this, timestamp);

        ((TextView) findViewById(R.id.tv_tmp))
                .setText(intent.getStringExtra(RunningRecordsAdapter.TIME_STAMP));
    }

    /**
     * Upload the running record files to the server
     * @param context the context
     * @param id An timestamp which identify an specific running record
     */
    private void uploadFile(final Context context, String id) {
        SharedPreferences sp = context.getSharedPreferences(LoginActivity.TAG, Context.MODE_PRIVATE);

        UploadFileRequests upload = new UploadFileRequests() {
            @Override
            protected void onPostExecute(InputStream inputStream) {
                JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
                try {
                    reader.beginObject();
                    while (reader.hasNext()) {
                        switch (reader.nextName()) {
                            case JsonKey.TEXT:
                                Toast.makeText(context, reader.nextString(), Toast.LENGTH_SHORT).show();
                                break;
                            default:reader.skipValue();
                                break;
                        }
                    }
                } catch (IOException e) {

                }
            }
        };
        Cursor cursor = new RunningEntryUtils(context).getSummarize(id);
        cursor.moveToFirst();
        upload.getRecords(this, id);
        HashMap<String, String> map = new HashMap<>();
        map.put(RunningDataEntryUtil.USER_ID, sp.getString(UserInfoEntryUtil._ID, "default"));
        map.put(RunningDataEntryUtil._ID, id);
        map.put(RunningDataEntryUtil.TIMESTAMP, id);
        map.put("_distance", cursor.getFloat(cursor.getColumnIndex("sum("
                + RunningEntryUtils.DISTANCE + ")")) +"");
        map.put("_duration", cursor.getLong(cursor.getColumnIndex("sum("
                + RunningEntryUtils.DURATION + ")")) + "");
        map.put(JsonKey.MSG_TYPE, MsgTypeConstant.TYPE_UPLOAD_FILE+"");
        upload.execute(map);

    }

    public Cursor getRecord(String timestamp) {
       return new RunningEntryUtils(this).getSpecificData(timestamp);
    }

    /**
     * Start position of the user's sport records
     * @param latLng A LatLng object include latitude and longitude
     */
    public void setStartPosition(LatLng latLng) {
        mLeftLongitude = latLng.longitude;
        mRightLongitude = latLng.longitude;
        mUpLatitude = latLng.latitude;
        mDownLatitude = latLng.latitude;
    }

    /**
     * Set the max rectangle that contains all the latitude and longitude data ,which make up
     * the trace of the user movement
     * @param lat latitude
     * @param lng longitude
     */
    public void setMaxRectangle(double lat,double lng) {
        mLeftLongitude = mLeftLongitude > lng ? mLeftLongitude : lng;
        mRightLongitude = mRightLongitude > lng ? mRightLongitude : lng;
        mUpLatitude = mUpLatitude > lat ? mUpLatitude : lat;
        mDownLatitude = mDownLatitude > lat ? mDownLatitude : lat;
    }


    /**
     * Set the max rectangle that contains all the latitude and longitude data ,which make up
     * the trace of the user movement
     * @param latLng
     */
    public void setMaxRectangle(LatLng latLng) {
        setMaxRectangle(latLng.latitude,latLng.longitude);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
}
