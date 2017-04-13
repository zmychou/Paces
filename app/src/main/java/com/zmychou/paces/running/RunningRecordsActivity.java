package com.zmychou.paces.running;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zmychou.paces.DividerItemDecoration;
import com.zmychou.paces.R;
import com.zmychou.paces.database.RunningEntryUtils;
import com.zmychou.paces.database.SqliteHelper;

public class RunningRecordsActivity extends AppCompatActivity {

    private RunningRecordsAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_records);

        SqliteHelper helper = new SqliteHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        RunningEntryUtils utils = new RunningEntryUtils(this);
        mAdapter = new RunningRecordsAdapter(utils.getAllSummarize());
        mAdapter.registerActivity(this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_running_record_summarize);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.unregisterActivity();
    }
}
