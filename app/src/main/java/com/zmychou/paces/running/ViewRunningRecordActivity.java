package com.zmychou.paces.running;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.PolylineOptions;
import com.zmychou.paces.R;
import com.zmychou.paces.database.RunningEntryUtils;
import com.zmychou.paces.io.JsonFileParser;

import java.util.ArrayList;

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

            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(
                    new LatLngBounds(new LatLng(mUpLatitude,mRightLongitude),
                            new LatLng(mDownLatitude,mRightLongitude)),0));
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

        ((TextView) findViewById(R.id.tv_tmp))
                .setText(intent.getStringExtra(RunningRecordsAdapter.TIME_STAMP));
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
