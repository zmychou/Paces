package com.zmychou.paces.running;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.zmychou.paces.R;

public class ViewRunningRecordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_running_record);
        Intent intent = getIntent();
        ((TextView) findViewById(R.id.tv_tmp))
                .setText(intent.getStringExtra(RunningRecordsAdapter.TIME_STAMP));
    }
}
