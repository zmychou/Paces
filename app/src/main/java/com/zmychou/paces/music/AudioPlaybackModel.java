package com.zmychou.paces.music;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
        MediaPlayer.OnCompletionListener, Subject {

    private MediaPlayer mMediaPlayer;
    private ArrayList<AudionInfo> mPlayList;
    private ArrayList<Observable> mRegistrants;
    private int mCurrentPosition;
    private static AudioPlaybackModel instance = new AudioPlaybackModel();

    private boolean mIsPause = false;

    class AudionInfo {
        public String path;
        public String title;

        public AudionInfo(String path, String title) {
            this.path = path;
            this.title = title;
        }
    }
    private AudioPlaybackModel() {
        mRegistrants = new ArrayList<>();
    }
    public static AudioPlaybackModel getInstance() {
        return instance;
    }

    public boolean isPlaying() {
        if (mMediaPlayer == null) {
            init();
            return false;
        }
        return mMediaPlayer.isPlaying();
    }
    public void start( String uri, int position) {
        String threadName = Thread.currentThread().getName();
        //Ensure we are use a worker thread to play music
        if (!"music_thread".equals(threadName)) {
            return;
        }
        mCurrentPosition = position;
        if (mMediaPlayer == null) {
            init();
        }
        mMediaPlayer.reset();
        try {
            mMediaPlayer.setDataSource(uri);
            mMediaPlayer.prepareAsync();
            notifyRegistrant(mPlayList.get(mCurrentPosition).title);
            Log.e("where am i",Thread.currentThread().getName());
        } catch (IOException e)  {
            //Toast.makeText(context, "无法播放该歌曲", Toast.LENGTH_SHORT).show();
        }
    }

    public void pause() {
        if (mMediaPlayer == null) {
            init();
        }
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
        mIsPause = false;
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
            mPlayList.add(new AudionInfo(
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)),
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))));
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
        return mPlayList.get(mCurrentPosition).path;
    }
    private String getPrev() {
        mCurrentPosition--;
        if (mCurrentPosition < 0)
        mCurrentPosition = mPlayList.size() - 1;
        return mPlayList.get(mCurrentPosition).path;
    }

    private void notifyRegistrant(final String name) {
        Handler handler = new Handler(Looper.getMainLooper());
        for (final Observable observer : mRegistrants) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    observer.onSongChanged(name);
                }
            });

        }
    }

    @Override
    public void registerForNotify(Observable o) {
        if (!mRegistrants.contains(o))
            mRegistrants.add(o);
    }

    @Override
    public void unregisterForNotify(Observable o) {
        if (!mRegistrants.contains(o))
            mRegistrants.remove(o);
    }
}
