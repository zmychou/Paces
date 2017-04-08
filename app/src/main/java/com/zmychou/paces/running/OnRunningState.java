package com.zmychou.paces.running;

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
public class OnRunningState implements State {
    private static OnRunningState instance;
    private OnRunningState(){}
    public static OnRunningState getInstance() {
        if (instance == null) {
            synchronized (OnRunningState.class) {
                if (instance == null) {
                    instance = new OnRunningState();
                }
            }
        }
        return instance;
    }

    @Override
    public void handle(RunningActivity activity) {

        activity.main.setText("Continue");

        activity.main.setBackgroundResource(R.drawable.btn_green_roundness);
        activity.changeState(PauseState.getInstance());
        activity.runningService.pause(this);
    }

    @Override
    public void start(RunningActivity activity) {

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
