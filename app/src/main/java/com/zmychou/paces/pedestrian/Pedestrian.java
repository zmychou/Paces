package com.zmychou.paces.pedestrian;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * <pre>
 * Package    :com.zmychou.paces.pedestrian
 * Author     : zmychou
 * Create time:17-4-3
 * Last Modify:
 * Email      :zmychou@gmail.com
 * Version    :V1.0
 * description:
 * </pre>
 */
public class Pedestrian implements SensorEventListener {

    private SensorManager mSensorManager;
    private ArrayList<Float> mList = new ArrayList<>();

    private float prev;

    public void start(Context context){
        Sensor sensor = hasAccelerator(context);
        if (sensor == null) {
            Toast.makeText(context, "Device don't ship any accelerometer", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        mSensorManager.registerListener(this,sensor,SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM);
    }
    public void pause(){}
    public void stop(){}
    public void restart(){}
    public int getStepCount(){
        return 0;
    }
    public int getStepRate(){
        return 0;
    }
    public int getIntensify(){
        return 0;
    }
    private Sensor hasAccelerator(Context context){
        mSensorManager
                = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        return sensor;
    }
    private boolean hasBarometer(){
        return false;
    }
    private void compute(){
        deriveAcceleratorData();
        verifyData();
        doCount();
    }

    public void doCount() {

    }

    public void verifyData() {

    }

    private void deriveAcceleratorData() {

    }

    public boolean saveToExternalStorage(){
        return false;
    }
    public boolean uploadToServer(){
        return false;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        prev = x;
//        mList.add()
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
