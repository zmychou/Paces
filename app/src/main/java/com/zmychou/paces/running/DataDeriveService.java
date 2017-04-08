package com.zmychou.paces.running;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.zmychou.paces.R;

public class DataDeriveService extends Service {

    private int mNotificationId = 1;
    private int mRequestCode = 1;
    private Notification mNotification;
    private AMapLocationListener mLocationRecordListener;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private State mPrevState;


    class MyBinder extends Binder{
        public DataDeriveService getService() {
            return DataDeriveService.this;
        }
    }
    class LocationChangeListener implements AMapLocationListener{
        @Override
        public void onLocationChanged(AMapLocation location) {
            Log.e("DataDeriveService",this.getClass()+"++++++get data");
        }
    }

    /**
     *
     */
    public DataDeriveService() {
    }
//@42a945b8
//@42a945b8@42b8a110
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("DataDeriveService",this.toString()+"----------------I start serving");
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("DataDeriveService","----------------some one bind me");
        setupClient();
        showNotification();
        return new MyBinder();
    }
    public void start(State state) {
        mPrevState = state;
        startLocation();
        updateNotification("Location start");
    }
    public void pause(State state) {
        mPrevState = state;
        stopLocation();
        updateNotification("Location pause");
    }
    public State getPrevState(){
        return mPrevState;
    }
    public void updateNotification(String debugMsg){
        //todo: set the remoteiews values
        mNotification.contentView.setTextViewText(R.id.remote_text,debugMsg);
        NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(mNotificationId,mNotification);
    }
    public void showNotification(){
        Intent i = new Intent(this,RunningActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this,mRequestCode,i
                ,PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews remote = new RemoteViews("com.zmychou.paces",
                R.layout.notification_content );
        mNotification = new NotificationCompat.Builder(this)
                .setContentTitle("Test")
                .setContentText("this is content")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContent(remote)
                .setContentIntent(pi).build();
        this.startForeground(mNotificationId,mNotification);
    }
    public void setupClient(){

        mLocationRecordListener = new LocationChangeListener();
        mLocationClient = new AMapLocationClient(this);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setMockEnable(true);
        mLocationOption.setOnceLocation(false);
        mLocationOption.setInterval(2000);
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.setLocationListener(mLocationRecordListener);
    }
    public void startLocation(){
        mLocationClient.startLocation();
        doRecord();
    }
    public void stopLocation(){
        mLocationClient.stopLocation();

    }
    public void doRecord(){}
}