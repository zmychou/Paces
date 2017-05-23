package com.zmychou.paces.running;

import android.content.Intent;
import android.preference.PreferenceManager;

import com.zmychou.paces.R;
import com.zmychou.paces.music.AudioPlaybackService;
import com.zmychou.paces.settings.SettingsActivity;

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

        activity.main.setText(R.string.running_continue);

        activity.main.setBackgroundResource(R.drawable.btn_green_roundness);
        activity.changeState(PauseState.getInstance());
        activity.runningService.pause(this);
        if (PreferenceManager.getDefaultSharedPreferences(activity)
                .getBoolean(SettingsActivity.MUSIC_SYNCHRONIZE_WITH_RUNNING, false)) {
            Intent intent = new Intent(activity, AudioPlaybackService.class);
            intent.putExtra(AudioPlaybackService.EXTRA_COMMAND, AudioPlaybackService.CMD_PAUSE);
            activity.startService(intent);
        }
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
