package com.zmychou.paces.pedestrian;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
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
    private PedestrianActivity mActivity;

    /**
     * Hold the max value since last step
     * mMaxPoint[0] is the max value of x axis
     * mMaxPoint[1] is the max value of y axis
     * mMaxPoint[2] is the max value of z axis
     */
    private float[] mMaxPoint = new float[3];


    /**
     * Hold the min value since last step
     * mMaxPoint[0] is the max value of x axis
     * mMaxPoint[1] is the max value of y axis
     * mMaxPoint[2] is the max value of z axis
     */
    private float[] mMinPoint = new float[3];


    /**
     * 存储最近十步的十个最大值
     */
    private float[] mXMaxPoints = new float[10];


    /**
     * 存储最近十步的十个最小值
     */
    private float[] mXMinPoints = new float[10];

    /**
     * Point to the position of mXMaxPoints or mXMinPoints ,which will be write next time
     * mPointer[0] is the x axis pointer of mXMaxPoints or mXMinPoints
     * mPointer[1] is the y axis pointer of mXMaxPoints or mXMinPoints
     * mPointer[2] is the z axis pointer of mXMaxPoints or mXMinPoints
     */
    private byte[] mPointer = new byte[3];

    /**
     * If the sensor report values greater than mUpperThreshold ,that indicate that
     * one more step should be count ,
     */
    private float[] mUpperThreshold = new float[3];
    private float[] mLowerThreshold = new float[3];

    /**
     * Steps of axises,which is x , y , z
     */
    private int[] mSteps = new int[3];


    /**
     * Indicate which threshold we should compare to ,mUpperThreshold or mLowerThreshold ,
     * if it STATE_UP ,we should compare with mUpperThreshold ,
     * else we compare with mLowerThreshold
     */
    private byte[] state = new byte[3];

    private static final byte STATE_UP = 0x01;
    private static final byte STATE_DOWN = 0x02;

    private static final byte AXIS_X = 0x00;
    private static final byte AXIS_Y = 0x01;
    private static final byte AXIS_Z = 0x02;

    private int mCounter;

    public void start(Context context){
        state[AXIS_X] = STATE_UP;
        mUpperThreshold[AXIS_X] = 15f;
        mLowerThreshold[AXIS_X] = -10f;
        Sensor sensor = hasAccelerator(context);
        if (sensor == null) {
            Toast.makeText(context, "Device don't ship any accelerometer", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        mSensorManager.registerListener(this,sensor,SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM);
    }
    public void pause(){}
    public void stop(){
        mSensorManager.unregisterListener(this);
    }
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

    public void runningMode(SensorEvent event) {

    }

    private void xAxis(float x) {
        job(x, AXIS_X);
    }
    private void yAxis(float y) {}
    private void zAxis(float z) {}

    private void job(float value, byte whichAxis) {

        if (state[whichAxis] == STATE_UP) {
            setMinPoint(value, whichAxis);
            if (value > mUpperThreshold[whichAxis]) {
                mCounter++;
                mSteps[whichAxis]++;
                mXMinPoints[getPointer(whichAxis)] = mMinPoint[whichAxis];
                mActivity.setSteps(mSteps[whichAxis]);
                Log.e("debug","debug+x+"+mSteps[whichAxis]);

                state[whichAxis] = STATE_DOWN;
                calculateAverage(whichAxis);
            }
        } else if (state[whichAxis] == STATE_DOWN) {
            setMaxPoint(value, whichAxis);
            if (value < mLowerThreshold[whichAxis]) {
                mXMaxPoints[getPointer(whichAxis)] = mMaxPoint[whichAxis];
                state[whichAxis] = STATE_UP;
            }
        }
    }


    private void setMaxPoint(float candidate, byte axis) {
        mMaxPoint[axis] = candidate > mMaxPoint[axis] ? candidate : mMaxPoint[axis];
    }

    private void setMinPoint(float candidate, byte axis) {
        mMaxPoint[axis] = candidate < mMaxPoint[axis] ? candidate : mMaxPoint[axis];
    }

    private byte getPointer(byte axis) {
        return (byte) (mPointer[axis] % 10);
    }

    private void calculateAverage(byte axis) {
        if (mCounter > 10) {
            for (int i=0; i<10 ;i++) {
                mUpperThreshold[axis] = mXMaxPoints[i];
                mLowerThreshold[axis] = mXMinPoints[i];
            }
            mUpperThreshold[axis] /= 10;
            mLowerThreshold[axis] /= 10;
            mCounter = 0;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float x = event.values[AXIS_X];
        xAxis(x);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void registerActivity(PedestrianActivity activity) {
        mActivity = activity;
    }

    public void unregisterActivity() {
        mActivity = null;
    }

}
