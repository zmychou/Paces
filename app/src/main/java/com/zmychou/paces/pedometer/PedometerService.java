package com.zmychou.paces.pedometer;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.zmychou.paces.database.PedometerDataEntryUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ming on 17-6-16.
 */
public class PedometerService extends Service implements DataChangeListener{

    private Pedometer mPedometer;
    private ArrayList<OnStepChangeListener> mObserver;
    private static String SAVE_STEP = "save_step";

    @Override
    public void onUpdateStepRate(int steps) {
    }

    @Override
    public void onUpdateSteps(int tmp) {
        notifyRegistrant(tmp);

    }

    class MyBinder extends Binder {
        public PedometerService getService() {
            return PedometerService.this;
        }
    }

    public interface OnStepChangeListener {
        void stepChange(int step);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mObserver = new ArrayList<>();
        mPedometer = new Pedometer();
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                SimpleDateFormat sdf = new SimpleDateFormat("mm");
                if ("59".equals(sdf.format(new Date(System.currentTimeMillis())))) {
                    Intent i = new Intent(context, PedometerService.class);
                    i.putExtra(SAVE_STEP, SAVE_STEP);
                    startService(i);
                }
            }
        };
        IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
        registerReceiver(receiver, filter);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String extra = intent.getStringExtra(SAVE_STEP);
        if (SAVE_STEP.equals(extra)) {
            long timestamp = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            String date = sdf.format(timestamp);
            sdf.applyPattern("HH");
            PedometerDataEntryUtil.insert(this, timestamp, date, sdf.format(timestamp), 0, 0L, 0);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void start() {
        mPedometer.start(this);
        mPedometer.registerDataChangeListener(this);
    }

    public void stop() {
        mPedometer.stop();
        mPedometer.unregisterDataChangeListener();
    }

    public void restart() {
        mPedometer.restart();
    }

    public void pause() {
        mPedometer.pause();
    }

    public void clear() {
        mPedometer.clear();
    }

    public int getStepCount() {
        return mPedometer.getStepCount();
    }

    public void registerForStepChange(OnStepChangeListener l) {
        if (!mObserver.contains(l)) {
            mObserver.add(l);
        }
    }

    public void unregisterForStepChange(OnStepChangeListener l) {
        mObserver.remove(l);
    }

    public void notifyRegistrant(int step) {
        if (mObserver == null) {
            return;
        }
        for (OnStepChangeListener reg : mObserver) {
            reg.stepChange(step);
        }
    }
}
