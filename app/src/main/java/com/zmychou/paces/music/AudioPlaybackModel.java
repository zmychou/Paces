package com.zmychou.paces.music;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.IOException;

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

public class AudioPlaybackModel implements MediaPlayer.OnPreparedListener {

    private MediaPlayer mMediaPlayer;
    private static AudioPlaybackModel instance = new AudioPlaybackModel();

    private boolean isPause = false;

    private AudioPlaybackModel() {}
    public static AudioPlaybackModel getInstance() {
        return instance;
    }
    public void start( String uri) {
        mMediaPlayer.reset();
        try {
            mMediaPlayer.setDataSource(uri);
            mMediaPlayer.prepareAsync();
        } catch (IOException e)  {
            //Toast.makeText(context, "无法播放该歌曲", Toast.LENGTH_SHORT).show();
        }
    }

    public void pause() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            isPause = true;
        }
    }

    public void restart() {
        if (isPause) {
            mMediaPlayer.start();
        }
    }

    public void stop() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
    }

    public void next(String uri) {
        start(uri);
    }

    public void prev(String uri) {
        start(uri);
    }

    public static Cursor getAudios(Context context) {
        ContentResolver resolver = context.getContentResolver();
        Uri audioUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] columns = {MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.SIZE, MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.TITLE
        };
        String whereClause = MediaStore.Audio.Media.IS_MUSIC+"=?";
        String[] args = {"1"}; // 1 is true

        return resolver.query(audioUri,columns,whereClause,args,null);
    }

    public void init() {
        if (mMediaPlayer != null)
            return;
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(this);
    }

    @Override
    public void onPrepared(MediaPlayer player) {
        player.start();
    }
}
