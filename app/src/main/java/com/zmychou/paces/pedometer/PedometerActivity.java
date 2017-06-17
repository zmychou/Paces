package com.zmychou.paces.pedometer;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.zmychou.paces.R;
import com.zmychou.paces.customview.StepCountView;
import com.zmychou.paces.database.server.UserInfoEntryUtil;
import com.zmychou.paces.login.LoginActivity;
import com.zmychou.paces.util.Format;

public class PedometerActivity extends AppCompatActivity
        implements PedometerService.OnStepChangeListener, ServiceConnection{

    StepCountView steps;
    TextView distance;
    TextView duration;
    Pedometer pedometer;
    PedometerService mService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_activity_pedometer_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = new Intent(this, PedometerService.class);
        startService(intent);
        bindService(intent, this, Service.BIND_AUTO_CREATE);
        steps = (StepCountView) findViewById(R.id.tv_pedestrian_steps);
        distance = (TextView) findViewById(R.id.tv_pedometer_activity_distance);
        duration = (TextView) findViewById(R.id.tv_pedometer_activity_duration);
//        pedometer = new Pedometer();
//        pedometer.registerActivity(this);
//        pedometer.start(this);
    }

//    public void setSteps(int steps) {
//        this.steps.setText(steps+"");
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        pedometer.stop();
    }


    @Override
    public void stepChange(int step) {
        steps.setText(step + "");
        distance.setText(mService.calculateDistance(step) + "");
        duration.setText(Format.formatTime(mService.getDuration()));

    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mService = ((PedometerService.MyBinder) service ).getService();
        mService.registerForStepChange(this);
        mService.start();

        steps.setText(mService.getStepCount() + "");
        distance.setText(mService.calculateDistance(mService.getStepCount()) + "");
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

}
