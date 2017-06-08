package com.zmychou.paces.pedometer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * <pre>
 * Package    :com.zmychou.paces.pedometer
 * Author     : zmychou
 * Create time:17-4-3
 * Last Modify:
 * Email      :zmychou@gmail.com
 * Version    :V1.0
 * description:
 * </pre>
 */
 
 /*
 * 关于计步器的一些设想:
 * 1. 分离重力,计算重力在三个轴向上的值,估算书当前手机的姿态,例如如果是走路看手机,x轴的重力分量会很小
 * 2. 对不同姿态,赋予不同的轴向的值不同的权重
 * 3. 对明确制定的场合,如跑步,制定专门的赛选方案
 */
public class Pedometer implements SensorEventListener {

    private SensorManager mSensorManager;
    private ArrayList<Float> mList = new ArrayList<>();
    private PedometerActivity mActivity;
    private Sensor mSensor;

    private long mStartTimestamp;
    private int mStartSteps;

    private DataChangeListener mDataChangeListener;

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
    private float[][] mMaxPoints = new float[3][10];


    /**
     * 存储最近十步的十个最小值
     */
    private float[][] mMinPoints = new float[3][10];

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
     * Steps of axises,which is x , y , z and total
     */
    private int[] mSteps = new int[4];


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
    private static final byte AXIS_TOTAL = 0x03;

    private static final int ONE_MINUTE = 1000 * 60;

    private int mCounter;

    private int mWatchDogCounter;
    private static final int WATCH_DOG_COUNT = 50;
    private long mIgnorePeriodicStart;
    private static final long IGNORE_PERIODIC = 1000 * 2; //3 seconds
    private float mAccuracy = 3;

    public void start(Context context){

        init();
        mSensor = hasAccelerator(context);
        if (mSensor == null) {
            Toast.makeText(context, "Device don't ship any accelerometer", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
//        mSensorManager.registerListener()
        mSensorManager.registerListener(this,mSensor,SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM);
    }
    public void pause(){
        mSensorManager.unregisterListener(this);
    }
    public void stop(){
        mSensorManager.unregisterListener(this);
    }
    public void restart(){
        mSensorManager.registerListener(this,mSensor,SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM);
    }

    private void init() {
        state[AXIS_X] = STATE_UP;
        state[AXIS_Y] = STATE_UP;
        state[AXIS_Z] = STATE_UP;
        mStartTimestamp = System.currentTimeMillis();
        getAccuracyFormSettings();
    }

    public int getStepCount(){
        return mSteps[AXIS_TOTAL];
    }

    public int getRunningSteps() {
        return mSteps[AXIS_TOTAL] / 2;
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

    /**
     * Calculate the steps on x axis
     * @param x The sensor value of x axis
     */
    private void xAxis(float x) {
        job(x, AXIS_X);
    }
    private void yAxis(float y) {
        job(y, AXIS_Y);
    }
    private void zAxis(float z) {
        job(z, AXIS_Z);
    }


    private void job(float value, byte whichAxis) {

        if (state[whichAxis] == STATE_UP) {
            setMinPoint(value, whichAxis);
            if (value > mUpperThreshold[whichAxis]) {
//                mCounter++;
                mSteps[whichAxis]++;
                mMinPoints[whichAxis][getPointer(whichAxis)] = mMinPoint[whichAxis];
                mMinPoint[whichAxis] = mLowerThreshold[whichAxis];
                state[whichAxis] = STATE_DOWN;
                calculateAverage(whichAxis);
            }
        } else if (state[whichAxis] == STATE_DOWN) {
            setMaxPoint(value, whichAxis);
            if (value < mLowerThreshold[whichAxis]) {
                mMaxPoints[whichAxis][getPointer(whichAxis)] = mMaxPoint[whichAxis];
                mMaxPoint[whichAxis] = mUpperThreshold[whichAxis];
                state[whichAxis] = STATE_UP;
            }
        }
    }


    private void setMaxPoint(float candidate, byte axis) {
        mMaxPoint[axis] = candidate > mMaxPoint[axis] ? candidate : mMaxPoint[axis];
    }

    private void setMinPoint(float candidate, byte axis) {
        mMinPoint[axis] = candidate < mMinPoint[axis] ? candidate : mMinPoint[axis];
    }

    private byte getPointer(byte axis) {
        mPointer[axis]++;
        return mPointer[axis] %= 10;
    }

    /**
     *
     * @param axis
     */
    private void calculateAverage(byte axis) {
        mCounter++;
        if (mCounter > 10) {
            for (int i = 0; i < 10; i++) {
                mUpperThreshold[axis] += mMaxPoints[axis][i];
                mLowerThreshold[axis] += mMinPoints[axis][i];
            }
            mUpperThreshold[axis] /= 10;
            float tmp = mUpperThreshold[axis] > 0 ? 0.9f : 1.1f;
            mUpperThreshold[axis] *= tmp; //90%
            mLowerThreshold[axis] /= 10;
            tmp = mLowerThreshold[axis] > 0 ? 1.1f : 0.9f;
            mLowerThreshold[axis] *= tmp;
            mCounter = 0;
        }
    }

    int tmp;
    @Override
    public void onSensorChanged(SensorEvent event) {

        watchDog(event.timestamp);
        //one minute count, update step rate
        if (mDataChangeListener != null) {
            if (event.timestamp - mStartTimestamp > ONE_MINUTE) {
                mStartTimestamp = event.timestamp;
                mDataChangeListener.onUpdateStepRate(mSteps[AXIS_TOTAL] - mStartSteps);
                mStartSteps = mSteps[AXIS_TOTAL];
            }
        }

        xAxis(event.values[AXIS_X]);
        yAxis(event.values[AXIS_Y]);
        zAxis(event.values[AXIS_Z]);
        /**
         * 接下来需要解决静止后几步问题

         */
        if (event.timestamp - mIgnorePeriodicStart < IGNORE_PERIODIC) {
            return;
        }
        float[] values = differAbsoluteValues(mMaxPoint, mMinPoint);
        if (maxAbsolute(values) < mAccuracy) {
            mSteps[AXIS_TOTAL] = tmp;
            mSteps[AXIS_X] = tmp;
            mSteps[AXIS_Y] = tmp;
            mSteps[AXIS_Z] = tmp;
            return;
        }
        tmp = mSteps[maxAbsoluteAxis(differAbsoluteValues(mUpperThreshold, mLowerThreshold))];//maxSum(mSteps);//max(max(mSteps[0], mSteps[1]), mSteps[2]);
        if (tmp > mSteps[AXIS_TOTAL]) {
            mWatchDogCounter = WATCH_DOG_COUNT;
            if (mDataChangeListener != null) {
                //a hook
                mDataChangeListener.onUpdateSteps(tmp);
            }
            mSteps[AXIS_TOTAL] = tmp;
            mSteps[AXIS_X] = tmp;
            mSteps[AXIS_Y] = tmp;
            mSteps[AXIS_Z] = tmp;
            if (mActivity != null) {
                mActivity.setSteps(mSteps[AXIS_TOTAL]);
            }
        }
    }

    /**
     * 看门狗程序.如果借鉴硬件看门狗电路思路,若果超过规定时间未设置 mWatchDogCounter ,则表明无法计步
     * 此时调整计步阈值
     * @param timestamp
     */
    private void watchDog(long timestamp) {
        mWatchDogCounter--;
        if (mWatchDogCounter < 0) {
            mUpperThreshold[AXIS_X] = -10;
            mUpperThreshold[AXIS_Y] = -10;
            mUpperThreshold[AXIS_Z] = -10;
            mLowerThreshold[AXIS_X] = 10;
            mLowerThreshold[AXIS_Y] = 10;
            mLowerThreshold[AXIS_Z] = 10;
            mWatchDogCounter = WATCH_DOG_COUNT;
            mIgnorePeriodicStart = timestamp;
        }
    }

    private void getAccuracyFormSettings() {
        // TODO: 17-4-21 set the mAccuracy value,from the use settings
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private float maxAbsolute(float[] values) {
        float tmp = values[AXIS_X] > values[AXIS_Y] ? values[AXIS_X] : values[AXIS_Y];
        return tmp > values[AXIS_Z] ? tmp : values[AXIS_Z];
    }


    /**
     * the axis which mUpperThreshold-mLowerThreshold 's absolute value is max
     * @return
     */
    private byte maxAbsoluteAxis(float[] values) {

        if (values[AXIS_X] > values[AXIS_Y] && values[AXIS_X] > values[AXIS_Z]) {
            return AXIS_X;
        }else if (values[AXIS_Y] > values[AXIS_X] && values[AXIS_Y] > values[AXIS_Z]) {
            return AXIS_Y;
        }else {
            return AXIS_Z;
        }
    }

    private float[] differAbsoluteValues(float[] up, float[] low) {
        float[] values = new float[3];
        values[AXIS_X] = up[AXIS_X] - low[AXIS_X];
        values[AXIS_Y] = up[AXIS_Y] - low[AXIS_Y];
        values[AXIS_Z] = up[AXIS_Z] - low[AXIS_Z];
        return values;
    }
    public void registerActivity(PedometerActivity activity) {
        mActivity = activity;
    }

    public void unregisterActivity() {
        mActivity = null;
    }

    public void registerDataChangeListener(DataChangeListener listener) {
        mDataChangeListener = listener;
    }

    public void unregisterDataChangeListener() {
        mDataChangeListener = null;
    }
}
