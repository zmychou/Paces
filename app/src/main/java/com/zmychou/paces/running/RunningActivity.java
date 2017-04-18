package com.zmychou.paces.running;

import android.app.AlertDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.LocationManager;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.zmychou.paces.R;
import com.zmychou.paces.io.JsonFileParser;
import com.zmychou.paces.profile.ProfileActivity;

import java.util.ArrayList;

public class RunningActivity extends AppCompatActivity
        implements ServiceConnection {


    AMap mMap;
    private LatLng mLatLng;
    private AMapLocationListener locationListener;
    private AMapLocationClient locationClient;
    private AMapLocationClientOption clientOption;
    DataDeriveService runningService;
    private int mSatellites;
    private boolean hasStart;

    //UI View relative
    State mRunningState ;
    MapView mMapView = null;
    private TextView mDistance;
    private TextView mDuration;
    private TextView mVelocity;
    private TextView mCalories;
    private TextView mSteps;
    Button main;
    Button slave;
    public void changeState(State state){
        mRunningState = state;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running);

        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        mMapView.setVisibility(View.VISIBLE);
        mMap = mMapView.getMap();
        main = (Button) findViewById(R.id.btn_running_activity_main);
        slave = (Button) findViewById(R.id.btn_running_activity_slave);
        mDistance = (TextView) findViewById(R.id.btn_running_activity_distance);
        mDuration = (TextView) findViewById(R.id.btn_running_activity_duration);
        mVelocity = (TextView) findViewById(R.id.btn_running_activity_velocity);
        mCalories = (TextView) findViewById(R.id.btn_running_activity_calories);
        mSteps = (TextView) findViewById(R.id.btn_running_activity_steps);

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSatellites > 2 || hasStart) {
                    mRunningState.handle(RunningActivity.this);
                }
                else showAlertDialog();
            }
        });
        slave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runningService.stop();
                unbindService(RunningActivity.this);

//                if (runningService != null) {
////                    runningService
//                }
//                RunningActivity.this.finish();
            }
        });

        prepare();
    }

    @Override
    public void onStart(){
        super.onStart();
        if (locationClient != null) {
            locationClient.startLocation();
        }
    }

    /**
     * Update the UI ,let's user know there running state
     * @param distance How far have there gone
     * @param duration How long have there insist
     * @param velocity There velocity
     * @param calories How much calories have been burn
     * @param steps Steps
     */
    public void updateUi(String distance, String duration, String velocity,
                         String calories, String steps) {
        mDistance.setText(distance);
        mDuration.setText(duration);
        mVelocity.setText(velocity);
        mCalories.setText(calories);
        mSteps.setText(steps);
    }
    /**
     * When an activity create ,invoke this method to prepare the map ,such as show the user
     * where are they now.
     */
    public void prepare(){
        if (gpsReady()) {
            initMap();
            Intent intent = new Intent(this,DataDeriveService.class);
            startService(intent);
            bindService(intent,this, Service.BIND_AUTO_CREATE);

            setPolyline("tmp_run_trace_20170414-180527.json");
        }
    }

    public void setPolyline(@NonNull String... fileNames) {
        ArrayList<LatLng> list = new JsonFileParser().parserLatLngArray(this,fileNames[0]);
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.setPoints(list);
        polylineOptions.color(getResources().getColor(R.color.colorOrangeLight));
        polylineOptions.aboveMaskLayer(true);
        polylineOptions.width(8);
        mMap.addPolyline(polylineOptions);
    }

    /**
     * Fire an intent to start the activity of switch the GPS module on and
     * chose location accuracy if the device's GPS module is not in service now
     */
    public void openGps(){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    /**
     * Detect whether the device's GPS module is in service or not
     */
    public boolean gpsReady(){
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            openGps();
            finish();
            return false;
        }
        return true;
    }

    /**
     * Show an alert dialog to user ,tell them GPS is not ready at this time,let them  decide to
     * choose whether start now or wait a second
     */
    public void showAlertDialog(){
        new AlertDialog.Builder(this)
                .setMessage(R.string.gps_not_ready)
                .setNegativeButton(R.string.wait_gps, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.start_anyway, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mRunningState.handle(RunningActivity.this);
                        hasStart = true;
                    }
                })
                .show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationClient != null) {
            locationClient.unRegisterLocationListener(locationListener);
            locationClient.onDestroy();
        }
        if (runningService != null) {
            runningService.unregisterBindActivity();
        }
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (locationClient != null) {
            locationClient.stopLocation();
        }
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        runningService = ((DataDeriveService.MyBinder)service).getService();
        State state = runningService.getPrevState();
        if (state != null) {
            state.handle(this);
            hasStart = true;

        }
        else {
            mRunningState = new PreparationState();
        }
        runningService.registerBindActivity(this);

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        runningService.unregisterBindActivity();
        runningService = null;
    }

    /**
     * Before running ,we show the user the state of the app
     */
    public void initMap(){
        MyLocationStyle locationStyle = new MyLocationStyle();
        locationStyle.strokeWidth(0);
        locationStyle.strokeColor(R.color.colorTransparent);
        locationStyle.radiusFillColor(0x00fc0808);

        mMap.setMyLocationEnabled(true);
        mMap.setMyLocationStyle(locationStyle);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(18));
        locationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                mSatellites = aMapLocation.getSatellites();
                if (mSatellites > 0)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            new LatLng(aMapLocation.getLatitude(),aMapLocation.getLongitude()),18));
                else {
                    Log.e("Running:Location type:",aMapLocation.getLocationType()+"-"+aMapLocation.getErrorCode());
                }
                Log.e("Running:",this.getClass()+"Satellite:"+mSatellites);
            }
        };
        locationClient = new AMapLocationClient(this);
        clientOption = new AMapLocationClientOption();
        clientOption.setMockEnable(true);
        clientOption.setOnceLocation(false);
        clientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        clientOption.setLocationCacheEnable(true);
        clientOption.setGpsFirst(true);
        locationClient.setLocationOption(clientOption);
        locationClient.setLocationListener(locationListener);
    }
}
