package com.zmychou.paces.music;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zmychou.paces.DividerItemDecoration;
import com.zmychou.paces.R;

public class AudioListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_list);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_audio_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(new AudioListAdapter(AudioPlaybackModel.getAudios(this)));
    }
}
