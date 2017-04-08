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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.zmychou.paces.R;

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

    MapView mMapView = null;
    State mRunningState ;
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

            }
        });

        prepare();
    }
    @Override
    public void onStart(){
        super.onStart();
        locationClient.startLocation();
    }
    public void prepare(){
        if (gpsReady()) {
            initMap();
            Intent intent = new Intent(this,DataDeriveService.class);
            startService(intent);
            bindService(intent,this, Service.BIND_AUTO_CREATE);
        }
    }

    public void openGps(){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    //Detect whether the GPS module is in service.
    public boolean gpsReady(){
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            openGps();
            finish();
            return false;
        }
        return true;
    }

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
        locationClient.unRegisterLocationListener(locationListener);
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
        locationClient.stopLocation();
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
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        runningService = null;
    }
    //Before running ,we show the user the state of the app
    public void initMap(){
        locationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                mSatellites = aMapLocation.getSatellites();
                if (mSatellites > 0)
                    mMap.moveCamera(CameraUpdateFactory.changeLatLng(
                            new LatLng(
                                    aMapLocation.getLatitude(),
                                    aMapLocation.getLongitude())
                    ));
                Log.e("Running:",this.getClass()+"Satellite:"+mSatellites);
            }
        };
        locationClient = new AMapLocationClient(this);
        clientOption = new AMapLocationClientOption();
        clientOption.setMockEnable(true);
        clientOption.setOnceLocation(false);
        clientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        locationClient.setLocationOption(clientOption);
        locationClient.setLocationListener(locationListener);
    }
}
