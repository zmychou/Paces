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
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.zmychou.paces.R;

import java.util.ArrayList;

public class DataDeriveService extends Service implements  Runnable {

    private int mNotificationId = 1;
    private int mRequestCode = 1;
    private Notification mNotification;
    private AMapLocationListener mLocationRecordListener;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private State mPrevState;
    private Thread mWorker;
    private ArrayList<LatLng> mLatLngs;

    //records
    private float mDistance;
    private long mPerMileStartTime;
    private LatLng mPrevLatLng;

    //Use to get the max rectangle that user had reach on the map
    private double mRightLongitude;
    private double mLeftLongitude;
    private double mUpLatitude;
    private double mDownLatitude;

    //Flags
    private boolean isFinish;
    private boolean isRunning ;

    class MyBinder extends Binder{
        public DataDeriveService getService() {
            return DataDeriveService.this;
        }
    }
    class LocationChangeListener implements AMapLocationListener{
        @Override
        public void onLocationChanged(AMapLocation location) {
            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
            mDistance += AMapUtils.calculateLineDistance(mPrevLatLng,latLng);
            if (mDistance > 1000) {
                mLatLngs.clear();
                mDistance = 0;
                mPerMileStartTime = location.getTime();
            }
            mLatLngs.add(latLng);
            setMaxRectangle(latLng);
            Log.e("DataDeriveService","++++++get data"+mLatLngs.size());
        }
    }

    @Override
    public void run() {
    }
    /**
     *
     */
    public DataDeriveService() {
    }

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
        mLatLngs = new ArrayList<>();
        mWorker = new Thread(this,"worker thread-----");
//        mWorker.start();
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
    public void stop() {
        stopForeground(true);
        isFinish = false;
        //Do some cleaning job,then stopSelf
        stopSelf();
    }

    /**
     * Start position of the user's sport records
     * @param latLng A LatLng object include latitude and longitude
     */
    public void setStartPosition(LatLng latLng) {
        mPrevLatLng = latLng;
        mLeftLongitude = latLng.longitude;
        mRightLongitude = latLng.longitude;
        mUpLatitude = latLng.latitude;
        mDownLatitude = latLng.latitude;
    }

    /**
     * Set the max rectangle that contains all the latitude and longitude data ,which make up
     * the trace of the user movement
     * @param latLng
     */
    public void setMaxRectangle(LatLng latLng) {
        setMaxRectangle(latLng.latitude,latLng.longitude);
    }
    /**
     * Set the max rectangle that contains all the latitude and longitude data ,which make up
     * the trace of the user movement
     * @param lat latitude
     * @param lng longitude
     */
    public void setMaxRectangle(double lat,double lng) {
        mLeftLongitude = mLeftLongitude > lat ? mLeftLongitude : lat;
        mRightLongitude = mRightLongitude > lat ? mRightLongitude : lat;
        mUpLatitude = mUpLatitude > lng ? mUpLatitude : lng;
        mDownLatitude = mDownLatitude > lng ? mDownLatitude : lng;
    }

    /**
     * Save the previous state for restore the bind client to the right state
     * @return Previous state
     */
    public State getPrevState(){
        return mPrevState;
    }

    /**
     * Update the foreground notification
     * @param debugMsg
     */
    public void updateNotification(String debugMsg){
        //todo: set the remoteiews values
        mNotification.contentView.setTextViewText(R.id.remote_text,debugMsg);
        NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(mNotificationId,mNotification);
    }

    /**
     * Build and fire the foreground notification
     */
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

    /**
     * Setup the LocationClient
     */
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
        isRunning = true;
        mLocationClient.startLocation();
        //doRecord();
    }
    public void stopLocation(){
        isRunning = false;
        mLocationClient.stopLocation();

    }
    public void saveToExternal() {

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        isFinish = true;
        Log.e("DataDeriveService",this.toString()+"-----My job done!");
    }
}