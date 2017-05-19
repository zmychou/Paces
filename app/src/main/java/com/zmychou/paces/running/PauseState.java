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
public class PauseState implements State {
    private static PauseState instance;
    private PauseState(){}
    public static PauseState getInstance() {
        if (instance == null) {
            synchronized (OnRunningState.class) {
                if (instance == null) {
                    instance = new PauseState();
                }
            }
        }
        return instance;
    }

    @Override
    public void handle(RunningActivity activity) {
        activity.changeState(OnRunningState.getInstance());
        activity.main.setText("Pause");
        activity.main.setBackgroundResource(R.drawable.btn_orange_roundness);
        activity.runningService.restart(this);
        if (PreferenceManager.getDefaultSharedPreferences(activity)
                .getBoolean(SettingsActivity.MUSIC_SYNCHRONIZE_WITH_RUNNING, false)) {

            Intent intent = new Intent(activity, AudioPlaybackService.class);
            intent.putExtra(AudioPlaybackService.EXTRA_COMMAND, AudioPlaybackService.CMD_RESTART);
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
