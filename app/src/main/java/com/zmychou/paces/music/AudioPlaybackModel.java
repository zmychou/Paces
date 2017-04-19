package com.zmychou.paces.music;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

/**
 * <pre>
 * Package    :com.zmychou.paces.music
 * Author     : zmychou
 * Create time:17-4-19
 * Last Modify:
 * Email      :zmychou@gmail.com
 * Version    :V1.0
 * description:
 * </pre>
 */

public class AudioPlaybackModel {

    public void start() {}

    public void pause() {}

    public void restart() {}

    public void stop() {}

    public void next() {}

    public void prev() {}

    public void showList() {}

    public void init(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        MediaPlayer player = new MediaPlayer();
//        player.set
    }
}
