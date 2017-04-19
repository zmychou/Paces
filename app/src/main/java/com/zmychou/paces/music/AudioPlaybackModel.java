package com.zmychou.paces.music;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

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

    private AudioManager mAudioManager;
    private MediaPlayer mMediaPlayer;
    public void start() {}

    public void pause() {}

    public void restart() {}

    public void stop() {}

    public void next() {}

    public void prev() {}

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
        Cursor audioCursor = resolver.query(audioUri,columns,whereClause,args,null);
        if (audioCursor != null) {
            for ( int i = 0; i < audioCursor.getCount(); i++ ) {
                audioCursor.moveToPosition(i);
                StringBuffer sb = new StringBuffer();
                for ( int j = 0; j < audioCursor.getColumnCount(); j++ ) {
                    sb.append(audioCursor.getString(j)+":");
                }
                Log.e("media", sb.toString());

            }
        }
        return audioCursor;
    }

    public void init(Context context) {
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        mMediaPlayer.setDataSource();
//        mMediaPlayer.prepareAsync();
//        player.set
    }
}
