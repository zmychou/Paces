package com.zmychou.paces.music;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zmychou.paces.DividerItemDecoration;
import com.zmychou.paces.R;

public class AudioListActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_list);

        findViewById(R.id.btn_audio_list_play).setOnClickListener(this);
        findViewById(R.id.btn_audio_list_next).setOnClickListener(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_audio_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(new AudioListAdapter(AudioPlaybackModel.getInstance().getAudios(this)));


    }

    @Override
    public void onClick(View v) {
//        Intent intent = new Intent(this, AudioPlaybackService.class);
//        intent.putExtra(URI, getUri());
//        intent.putExtra(INDEX, getIndex());
//        v.getContext().startService(intent);
        switch (v.getId()) {
            case R.id.btn_audio_list_next:
                AudioPlaybackModel.getInstance().next();
                break;
            case R.id.btn_audio_list_play:
                AudioPlaybackModel model = AudioPlaybackModel.getInstance();
                if (model.isPause()) {
                    model.restart();
                }else {
                    model.pause();
                }
                break;
            default:break;
        }
    }
}
