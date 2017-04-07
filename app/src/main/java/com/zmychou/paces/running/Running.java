package com.zmychou.paces.running;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.LocationManager;
import android.media.audiofx.BassBoost;
import android.os.IBinder;
import android.provider.Settings;
import android.telecom.ConnectionService;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.zmychou.paces.Sport;

/**
 * <pre>
 * Package    :com.zmychou.paces.running
 * Author     : zmychou
 * Create time:17-4-3
 * Last Modify:
 * Email      :zmychou@gmail.com
 * Version    :V1.0
 * description:
 * </pre>
 */
public class Running implements Sport,ServiceConnection {

    private Activity mContext;
    private AMap mMap;
    private LatLng mLatLng;
    private AMapLocationListener locationListener;
    private AMapLocationClient locationClient;
    private AMapLocationClientOption clientOption;
    private DataDeriveService runningService;

//    private
    public Running(Activity activity, AMap map){
        mContext = activity;
        mMap = map;
        mLatLng = new LatLng(0,0);
    }
    @Override
    public void start() {
        //Once we start running ,we unregister this listener,because it has fulfill its destiny.
        //Start a service to do the dirty job
        locationClient.startLocation();
        Intent intent = new Intent(mContext,DataDeriveService.class);
        mContext.bindService(intent,this, Service.BIND_AUTO_CREATE);
    }

    @Override
    public void pause() {
      //  runningService.Test();
        locationClient.stopLocation();
    }

    @Override
    public void stop() {

    }

    @Override
    public void restart() {

    }

    @Override
    public void doCount() {

    }

    @Override
    public void verifyData() {

    }
    public void prepare(){
        if (gpsReady()) {
            initMap();
            Intent intent = new Intent(mContext,DataDeriveService.class);
            mContext.startService(intent);
        }
    }

    public void openGps(){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }
    //Detect whether the GPS module is in service.
    public boolean gpsReady(){
        LocationManager lm = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            openGps();
            mContext.finish();
            return false;
        }
        return true;
    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        runningService = ((DataDeriveService.MyBinder)service).getService();
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
                mMap.moveCamera(CameraUpdateFactory.changeLatLng(
                        new LatLng(
                                aMapLocation.getLatitude(),
                                aMapLocation.getLongitude())
                ));
                Log.e("Running:",this.getClass()+"");
            }
        };
        locationClient = new AMapLocationClient(mContext);
        clientOption = new AMapLocationClientOption();
        clientOption.setMockEnable(true);
        clientOption.setOnceLocation(false);
        clientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        locationClient.setLocationOption(clientOption);
        locationClient.setLocationListener(locationListener);
    }
    public void registerDisplayLocationListener(){
        locationClient.startLocation();
    }
    public void unregisterDisplayLocationListener(){
        locationClient.stopLocation();
    }

}
