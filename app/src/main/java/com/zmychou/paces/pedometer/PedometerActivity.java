package com.zmychou.paces.pedometer;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.zmychou.paces.R;
import com.zmychou.paces.customview.StepCountView;

public class PedometerActivity extends AppCompatActivity
        implements PedometerService.OnStepChangeListener, ServiceConnection{

    StepCountView steps;
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
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mService = ((PedometerService.MyBinder) service ).getService();
        mService.registerForStepChange(this);
        mService.start();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}
