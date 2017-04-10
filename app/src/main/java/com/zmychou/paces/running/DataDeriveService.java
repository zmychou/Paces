package com.zmychou.paces.running;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.amap.api.maps.model.LatLng;
import com.zmychou.paces.R;
import com.zmychou.paces.database.RunningData;
import com.zmychou.paces.database.RunningEntryUtils;
import com.zmychou.paces.io.RunningDataJsonFileWriter;

import java.util.ArrayList;

public class DataDeriveService extends Service {

    private int mNotificationId = 1;
    private int mRequestCode = 1;
    private Notification mNotification;
    private AMapLocationListener mLocationRecordListener;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private State mPrevState;
    private Thread mWorker;
    private ArrayList<LatLng> mLatLngs;
    private RunningActivity mBindActivity;

    //records
    private float mDistance;
    private long mPerMileStartTime;
    //Use to save how long have running before pause
    private long mTimeAccumulate;
    private LatLng mPrevLatLng ;
    private RunningData mRunningData;
    private int mMiles ;

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
//            if (mDistance > 1000) {
            if (mLatLngs.size() > 10) {
                mRunningData.setLatLngs(mLatLngs);
                mRunningData.setDistance(1000);
                mRunningData.setFinishTime(location.getTime());
                mRunningData.setStartTime(mPerMileStartTime);
                mRunningData.setDuration(mTimeAccumulate
                        += (location.getTime() - mPerMileStartTime));
                mRunningData.setSequenceNumber(++mMiles);
//                mRunningData.setSteps();
                SaveDataWorker worker = new SaveDataWorker();
                worker.execute(mRunningData);
                mLatLngs = new ArrayList<>();
                mDistance = 0;
                mPerMileStartTime = location.getTime();
            }
            mPrevLatLng = latLng;
            mLatLngs.add(latLng);
            setMaxRectangle(latLng);
            Log.e("DataDeriveService","++++++get data"+mLatLngs.size()+":::dstance"+mDistance);

        }

    }
    class SaveDataWorker extends AsyncTask<RunningData,Void,Void> {

        @Override
        protected Void doInBackground(RunningData... params) {
            RunningEntryUtils utils = new RunningEntryUtils(DataDeriveService.this);
            RunningDataJsonFileWriter writer
                    = new RunningDataJsonFileWriter(DataDeriveService.this,params[0]);
            String filePath = writer.save();
            if (filePath != null) {
                params[0].setLatLngFile(filePath);
                params[0].setStepsFile(filePath);
                utils.insert(params[0]);
                params[0].clear();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //super.onPostExecute(aVoid);
            Toast.makeText(DataDeriveService.this, R.string.save_success, Toast.LENGTH_SHORT).show();
        }
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
        mRunningData = RunningData.getInstance();
        mRunningData.setTimestamp(System.currentTimeMillis());
        mRunningData.setSequenceNumber(mMiles);
        mLatLngs = new ArrayList<>();
        return new MyBinder();
    }
    public void registerBindActivty(RunningActivity activity) {
        mBindActivity = activity;
    }
    public void unregisterBindActivity() {
        mBindActivity = null;
    }
    protected void updateBindActivityInfo() {
        if (mBindActivity == null)
            return;

    }
    public void start(State state) {
        isRunning = true;
        mPrevState = state;
        mPerMileStartTime = System.currentTimeMillis();
        startLocation();
        updateNotification("Location start");
    }

    public void pause(State state) {
        isRunning = false;
        mPrevState = state;
        mTimeAccumulate += (System.currentTimeMillis() - mPerMileStartTime);
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