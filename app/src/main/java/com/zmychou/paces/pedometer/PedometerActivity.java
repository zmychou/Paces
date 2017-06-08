package com.zmychou.paces.pedometer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zmychou.paces.R;
import com.zmychou.paces.customview.StepCountView;

public class PedometerActivity extends AppCompatActivity {

    StepCountView steps;
    Pedometer pedometer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer_activity);

        steps = (StepCountView) findViewById(R.id.tv_pedestrian_steps);
        pedometer = new Pedometer();
        pedometer.registerActivity(this);
        pedometer.start(this);
    }

    public void setSteps(int steps) {
        this.steps.setText(steps+"");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pedometer.stop();
    }
}
