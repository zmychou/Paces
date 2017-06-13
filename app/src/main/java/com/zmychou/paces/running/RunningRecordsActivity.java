package com.zmychou.paces.running;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;

import com.zmychou.paces.DividerItemDecoration;
import com.zmychou.paces.R;
import com.zmychou.paces.constant.NetworkConstant;
import com.zmychou.paces.database.RunningData;
import com.zmychou.paces.database.RunningEntryUtils;
import com.zmychou.paces.database.SqliteHelper;
import com.zmychou.paces.database.server.UserInfoEntryUtil;
import com.zmychou.paces.io.JsonFileParser;
import com.zmychou.paces.io.RunningDataJsonFileWriter;
import com.zmychou.paces.login.LoginActivity;
import com.zmychou.paces.network.DownloadRecordTask;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class RunningRecordsActivity extends AppCompatActivity {

    private RunningRecordsAdapter mAdapter;
    private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_records);


        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_running_record_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RunningRecordsActivity.this.finish();
            }
        });
        SqliteHelper helper = new SqliteHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        RunningEntryUtils utils = new RunningEntryUtils(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_running_record_summarize);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        Cursor cursor = utils.getAllSummarize();
        if (cursor.getCount() > 0) {
            mAdapter = new RunningRecordsAdapter(cursor);
            mAdapter.registerActivity(this);
            mRecyclerView.setAdapter(mAdapter);
            return;
        }
        DownloadRecordTask task = new DownloadRecordTask(this);
        task.execute();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.unregisterActivity();
    }
}
