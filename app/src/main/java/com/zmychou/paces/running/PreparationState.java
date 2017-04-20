package com.zmychou.paces.running;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

import com.zmychou.paces.R;

/**
 * <pre>
 * Package    :com.zmychou.paces.running
 * Author     : zmychou
 * Create time:17-4-8
 * Last Modify:
 * Email      :zmychou@gmail.com
 * Version    :V1.0
 * description:
 * </pre>
 */
public class PreparationState implements State {

    @Override
    public void handle(RunningActivity activity) {
        start(activity);
    }

    @Override
    public void start(RunningActivity activity) {
        activity.main.setText("Pause");
        activity.main.setBackgroundResource(R.drawable.btn_orange_roundness);
        activity.changeState(OnRunningState.getInstance());
//        activity.runningService.
        activity.runningService.start(this);
    }

    @Override
    public void stop(RunningActivity activity) {

    }

    @Override
    public void pause(RunningActivity activity) {

    }

    @Override
    public void restart(RunningActivity activity) {

    }
}
