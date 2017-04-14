package com.zmychou.paces.running;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.zmychou.paces.R;
import com.zmychou.paces.database.RunningEntryUtils;
import com.zmychou.paces.io.JsonFileParser;

import java.util.ArrayList;

public class ViewRunningRecordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_running_record);
        Intent intent = getIntent();
        String timestamp = intent.getStringExtra(RunningRecordsAdapter.TIME_STAMP);
        Cursor cursor = getRecord(timestamp);
        int count = cursor.getCount();
        String[] filePath = new String[count];

        JsonFileParser parser = new JsonFileParser();
        ArrayList<ArrayList<LatLng>> ll = parser.parserLatLngArrrays(this,filePath);


        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            filePath[i]
                    = cursor.getString(cursor.getColumnIndex(RunningEntryUtils.LATLNG_FILE_PATH));
        }
        for (String s : filePath) {
            Log.e("file path is :",s);
        }
        Log.e("file path is :","finish");
        MapView mapView = (MapView) findViewById(R.id.mv_details_record);
        AMap map = mapView.getMap();

        ((TextView) findViewById(R.id.tv_tmp))
                .setText(intent.getStringExtra(RunningRecordsAdapter.TIME_STAMP));
    }

    public Cursor getRecord(String timestamp) {
       return new RunningEntryUtils(this).getSpecificData(timestamp);
    }
}
