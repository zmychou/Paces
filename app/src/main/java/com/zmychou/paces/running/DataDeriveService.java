package com.zmychou.paces.running;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
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
import com.zmychou.paces.profile.ProfileActivity;

import java.util.ArrayList;

public class DataDeriveService extends Service {

    private int mNotificationId = 1;
    private int mRequestCode = 1;
    private Notification mNotification;
    private AMapLocationListener mLocationRecordListener;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private State mPrevState;
    private ArrayList<LatLng> mLatLngs;
    private RunningActivity mBindActivity;

    //records
    private float mDistance;
    private long mPerMileStartTime;
    //Use to save how long have running before pause
    private long mTimeAccumulate;
    private long mTotalTime;
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
        private int updateNotificationElapse ;
        @Override
        public void onLocationChanged(AMapLocation location) {
            updateNotificationElapse++;
            updateUi();
            if (updateNotificationElapse > 5) {
                updateNotification("have run:"+mDistance+"meter");
                updateNotificationElapse = 0;
            }
            LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
            mDistance += AMapUtils.calculateLineDistance(mPrevLatLng,latLng);
            if (mDistance > 1000) {
                saveRunningDate(1000,location.getTime());
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
            //Save to the external storage media with the json data format
            RunningDataJsonFileWriter writer
                    = new RunningDataJsonFileWriter(DataDeriveService.this,params[0]);
            String filePath = writer.save();
            if (filePath != null) {
                params[0].setLatLngFile(filePath);
                params[0].setStepsFile(filePath);
                //Save to the SQLite database
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

    /**
     * When user pause running ,set the running flag and calculate the time usage
     * @param state
     */
    public void pause(State state) {
        isRunning = false;
        mPrevState = state;
        mTimeAccumulate += (System.currentTimeMillis() - mPerMileStartTime);
        stopLocation();
        updateNotification("Location pause");
    }

    /**
     * After user stop running ,the last stage of the service
     */
    public void stop() {
        stopForeground(true);
        isFinish = false;
        showSaveFileAlertDialog();
    }

    /**
     *
     */
    private void showSaveFileAlertDialog() {
        AlertDialog dialog = new AlertDialog.Builder(mBindActivity)
                .setTitle("Stop running?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveRunningDate(mDistance, System.currentTimeMillis());
                        stopLocation();
                        if (mLocationClient != null) {
                            mLocationClient.unRegisterLocationListener(mLocationRecordListener);
                        }
                        //Do some cleaning job,then stopSelf
                        mBindActivity.startActivity(new Intent(mBindActivity, ProfileActivity.class));
                        mBindActivity.finish();
                        DataDeriveService.this.stopSelf();
                    }
                }).create();
                dialog.show();
    }

    /**
     * Start an worker thread to save the running data to the external storage
     * and to the SQLite database
     * @param distance How long this period have run
     * @param currentTime How much time has elapsed
     */
    public void saveRunningDate(float distance,long currentTime) {
        mRunningData.setLatLngs(mLatLngs);
        mRunningData.setDistance(distance);
        mRunningData.setFinishTime(currentTime);
        mRunningData.setStartTime(mPerMileStartTime);
        mRunningData.setDuration((mTimeAccumulate += (currentTime - mPerMileStartTime)));
        mRunningData.setSequenceNumber(++mMiles);
//                mRunningData.setSteps();
//                mRunningData.setCalories();
        SaveDataWorker worker = new SaveDataWorker();
        worker.execute(mRunningData);

        //Reset
        mTotalTime += mTimeAccumulate;
        mLatLngs = new ArrayList<>();
        mDistance = 0;
        mPerMileStartTime = currentTime;
        mTimeAccumulate = 0;

    }

    /**
     * Update the Running activity's ui content if mBindActivity,which if a RunningActivity
     * reference that bind to this Service,is not null
     */
    public void updateUi() {
        if (mBindActivity != null) {
            //Total distance
            String distance = mMiles+"."+(((int)mDistance));
            mBindActivity.updateUi(distance, formatDuration(), getVelocity(),
                    calculateCalories(),233+"");
        }
    }

    private String getVelocity() {
        return 10.5+"";
    }
    private String calculateCalories() {
        // TODO: 17-4-12  a stub to calculate calories
        return 123+"";
    }

    private String formatDuration() {
        long totalSeconds = (mTotalTime + mTimeAccumulate
                + System.currentTimeMillis() - mPerMileStartTime) / 1000;
        int secondsInMinute = (int) totalSeconds % 60;
        int minutesInHour = (int) totalSeconds / 60 % 60;
        int hours = (int) totalSeconds / 3600;
        StringBuffer sb = new StringBuffer();
        sb.append(hours);
        sb.append(":");
        if (minutesInHour < 10) {
            sb.append("0");
        }
        sb.append(minutesInHour);
        sb.append(":");
        if (totalSeconds < 10) {
            sb.append("0");
        }
        sb.append(secondsInMinute);
        return sb.toString();
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
                .setContentTitle("Paces")
                .setContentText("Notification")
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
        if (mLocationClient != null) {
            mLocationClient.startLocation();
        }
    }
    public void stopLocation(){
        isRunning = false;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        isFinish = true;
        Log.e("DataDeriveService",this.toString()+"-----My job done!");
    }
}