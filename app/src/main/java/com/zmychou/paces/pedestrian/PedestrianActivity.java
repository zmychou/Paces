package com.zmychou.paces.pedestrian;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.zmychou.paces.R;

public class PedestrianActivity extends AppCompatActivity {

    TextView steps;
    Pedestrian pedestrian;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedestrian_activity);

        steps = (TextView) findViewById(R.id.tv_pedestrian_steps);
        pedestrian = new Pedestrian();
        pedestrian.registerActivity(this);
        pedestrian.start(this);
    }

    public void setSteps(int steps) {
        this.steps.setText(steps+"");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pedestrian.stop();
    }
}
