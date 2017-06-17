package com.zmychou.paces.music;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zmychou.paces.DividerItemDecoration;
import com.zmychou.paces.R;

public class AudioListActivity extends AppCompatActivity implements
        View.OnClickListener, Observable{

    private TextView mCurrentSong;
    private ImageView play;
    private static Cursor mSongList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_activity_audio_list_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioListActivity.this.finish();
            }
        });
        mCurrentSong = (TextView) findViewById(R.id.tv_audio_list_current_song);
        play = (ImageView) findViewById(R.id.btn_audio_list_play);
        play.setOnClickListener(this);
        findViewById(R.id.btn_audio_list_next).setOnClickListener(this);
        findViewById(R.id.btn_audio_list_prev).setOnClickListener(this);

        if (mSongList == null) {
            mSongList = AudioPlaybackModel.getInstance().getAudios(this);
        }
        AudioListAdapter adapter
                = new AudioListAdapter(mSongList);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_audio_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);
        onSongChanged(AudioPlaybackModel.getCurrentSongUri());

        AudioPlaybackModel.getInstance().registerForNotify(this);
    }

    @Override
    public void onClick(View v) {
//        Intent intent = new Intent(this, AudioPlaybackService.class);
//        intent.putExtra(URI, getUri());
//        intent.putExtra(INDEX, getIndex());
//        v.getContext().startService(intent);
        switch (v.getId()) {
            case R.id.btn_audio_list_next:
                sendCommand(this, AudioPlaybackService.CMD_NEXT);
                break;
            case R.id.btn_audio_list_play:
                AudioPlaybackModel model = AudioPlaybackModel.getInstance();
                if (model.isPause()) {
                    sendCommand(this, AudioPlaybackService.CMD_RESTART);
                }else {
                    sendCommand(this, AudioPlaybackService.CMD_PAUSE);
                }
                if (model.isPlaying()) {
                    play.setImageResource(R.drawable.start);
                } else {
                    play.setImageResource(R.drawable.stop);
                }
                break;
            case R.id.btn_audio_list_prev:
                sendCommand(this, AudioPlaybackService.CMD_PREV);
            default:break;
        }
    }

    public static void sendCommand(Context context, int command) {
        Intent intent = new Intent(context, AudioPlaybackService.class);
        intent.putExtra(AudioPlaybackService.EXTRA_COMMAND, command);
        context.startService(intent);
    }

    @Override
    public void onSongChanged(String name) {
        if (mSongList == null) {
            return;
        }
        mSongList.moveToFirst();
        do {
            if (mSongList.getString(mSongList.getColumnIndex(MediaStore.Audio.Media.DATA))
                    .equals(name)) {

                mCurrentSong.setText(mSongList.getString(mSongList.getColumnIndex(
                        MediaStore.Audio.Media.TITLE
                )));
                play.setImageResource(R.drawable.stop);
                break;
            }
        }
        while (mSongList.moveToNext());
    }
}
