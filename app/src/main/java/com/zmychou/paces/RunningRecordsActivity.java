package com.zmychou.paces;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zmychou.paces.database.RunningEntryUtils;
import com.zmychou.paces.database.SqliteHelper;

public class RunningRecordsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_records);

        SqliteHelper helper = new SqliteHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        RunningEntryUtils utils = new RunningEntryUtils(this);
        RecyclerView.Adapter adapter = new RunningRecordsAdapter(utils.getAllSummarize());
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_running_record_summarize);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);


    }
}
