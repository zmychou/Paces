package com.zmychou.paces.running;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
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
public class PreparationState implements State {

    @Override
    public void handle(RunningActivity activity) {
        start(activity);
        if (PreferenceManager.getDefaultSharedPreferences(activity)
                .getBoolean(SettingsActivity.AUTO_PLAY_MUSIC_WHEN_RUNNING, false)
                || PreferenceManager.getDefaultSharedPreferences(activity)
                .getBoolean(SettingsActivity.MUSIC_SYNCHRONIZE_WITH_RUNNING, false)) {
            Intent intent = new Intent(activity, AudioPlaybackService.class);
            intent.putExtra(AudioPlaybackService.EXTRA_COMMAND, AudioPlaybackService.CMD_NEXT);
            activity.startService(intent);
        }
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
