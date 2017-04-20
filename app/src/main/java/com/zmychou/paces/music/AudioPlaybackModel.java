package com.zmychou.paces.music;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

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

public class AudioPlaybackModel implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener {

    private MediaPlayer mMediaPlayer;
    private ArrayList<String> mPlayList;
    private int mCurrentPosition;
    private static AudioPlaybackModel instance = new AudioPlaybackModel();

    private boolean mIsPause = false;

    private AudioPlaybackModel() {}
    public static AudioPlaybackModel getInstance() {
        return instance;
    }
    public void start( String uri, int position) {
        mCurrentPosition = position;
        mMediaPlayer.reset();
        try {
            mMediaPlayer.setDataSource(uri);
            mMediaPlayer.prepareAsync();
            Log.e("where am i",Thread.currentThread().getName());
        } catch (IOException e)  {
            //Toast.makeText(context, "无法播放该歌曲", Toast.LENGTH_SHORT).show();
        }
    }

    public void pause() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mIsPause = true;
        }
    }

    public void restart() {
        if (mIsPause) {
            mMediaPlayer.start();
            mIsPause = false;
        }
    }

    public boolean isPause() {
        return mIsPause;
    }

    public void stop() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
    }

    public void next() {
        String uri = getNext();
        start(uri, mCurrentPosition);
    }

    public void prev() {
        String uri = getNext();
        start(uri, mCurrentPosition);
    }

    public Cursor getAudios(Context context) {
        ContentResolver resolver = context.getContentResolver();
        Uri audioUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] columns = {MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE
        };
        String whereClause = MediaStore.Audio.Media.IS_MUSIC+"=?";
        String[] args = {"1"}; // 1 is true

        Cursor cursor = resolver.query(audioUri,columns,whereClause,args,null);
        mPlayList = new ArrayList<>();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            mPlayList.add(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
        }
        return cursor;
    }

    public void init() {
        if (mMediaPlayer != null)
            return;
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public void onPrepared(MediaPlayer player) {
        player.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        start(getNext(), mCurrentPosition);
    }

    private String getNext() {
        mCurrentPosition++;
        mCurrentPosition %= mPlayList.size();
        return mPlayList.get(mCurrentPosition);
    }
    private String getPrev() {
        mCurrentPosition--;
        if (mCurrentPosition < 0)
        mCurrentPosition = mPlayList.size() - 1;
        return mPlayList.get(mCurrentPosition);
    }

}
