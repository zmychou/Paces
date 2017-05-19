package com.zmychou.paces.music;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_list);
        mCurrentSong = (TextView) findViewById(R.id.tv_audio_list_current_song);
        play = (ImageView) findViewById(R.id.btn_audio_list_play);
        play.setOnClickListener(this);
        findViewById(R.id.btn_audio_list_next).setOnClickListener(this);

        AudioListAdapter adapter
                = new AudioListAdapter(AudioPlaybackModel.getInstance().getAudios(this));
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_audio_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

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
                sendCommand(AudioPlaybackService.CMD_NEXT);
                break;
            case R.id.btn_audio_list_play:
                AudioPlaybackModel model = AudioPlaybackModel.getInstance();
                if (model.isPause()) {
                    sendCommand(AudioPlaybackService.CMD_RESTART);
                }else {
                    sendCommand(AudioPlaybackService.CMD_PAUSE);
                }
                if (model.isPlaying()) {
                    play.setImageResource(R.mipmap.play);
                } else {
                    play.setImageResource(R.mipmap.pause);
                }
                break;
            default:break;
        }
    }

    private void sendCommand(int command) {
        Intent intent = new Intent(this,AudioPlaybackService.class);
        intent.putExtra(AudioPlaybackService.EXTRA_COMMAND, command);
        startService(intent);
    }

    @Override
    public void onSongChanged(String name) {
        mCurrentSong.setText(name);
    }
}
