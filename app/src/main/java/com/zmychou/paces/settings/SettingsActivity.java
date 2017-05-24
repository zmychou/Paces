package com.zmychou.paces.settings;

import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zmychou.paces.R;

public class SettingsActivity extends PreferenceActivity {

    public static final String AUTO_PLAY_MUSIC_WHEN_RUNNING = "cbp_settings_music_auto_play";
    public static final String MUSIC_SYNCHRONIZE_WITH_RUNNING
            = "cbp_settings_music_synchronize_with_running";
    public static final String WEATHER_INFO_SOURCE = "sp_settings_get_weather_info_from_server";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_layout);
    }
}
