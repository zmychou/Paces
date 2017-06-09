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
import android.view.View;

import com.zmychou.paces.DividerItemDecoration;
import com.zmychou.paces.R;
import com.zmychou.paces.constant.NetworkConstant;
import com.zmychou.paces.database.RunningEntryUtils;
import com.zmychou.paces.database.SqliteHelper;
import com.zmychou.paces.database.server.UserInfoEntryUtil;
import com.zmychou.paces.login.LoginActivity;

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
        DownloadRecordTask task = new DownloadRecordTask();
        task.execute();
    }

    class DownloadRecordTask extends AsyncTask<Void, Void, ArrayList<RunningRecords>> {

        @Override
        protected ArrayList<RunningRecords> doInBackground(Void... params) {
            try {
                URL url = new URL(NetworkConstant.SERVER_ADDR_BASE + "MyServlet");
                HttpURLConnection huc = (HttpURLConnection) url.openConnection();
                huc.setDoOutput(true);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(huc.getOutputStream()));
                SharedPreferences sp = getSharedPreferences(LoginActivity.TAG, Context.MODE_PRIVATE);
                writer.write("{\"_id\":\"");
                writer.write(sp.getString(UserInfoEntryUtil._ID, null) + "\",");
                writer.write("\"msgType\":\"");
                writer.write(14 + "\"}");
                writer.flush();
                writer.close();

                InputStream in = huc.getInputStream();
                JsonReader jr = new JsonReader(new InputStreamReader(in));
                return parseArray(jr);

            } catch (MalformedURLException e) {

            } catch (IOException e) {}
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<RunningRecords> records) {
            mAdapter = new RunningRecordsAdapter(records);
            mAdapter.registerActivity(RunningRecordsActivity.this);

            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

        }

        private ArrayList<RunningRecords> parseArray(JsonReader reader) {
            ArrayList<RunningRecords> list = new ArrayList<>();
            try {
                reader.beginArray();
                while (reader.hasNext()) {
                    list.add(parseObject(reader));
                }
                reader.endArray();
                return list;
            } catch (IOException e) {}
            return null;
        }

        private RunningRecords parseObject(JsonReader reader) {
            RunningRecords records = new RunningRecords();
            try {
                reader.beginObject();
                while (reader.hasNext()) {
                    switch (reader.nextName()) {
                        case "_timestamp":
                            records.setTimestamp(reader.nextString());
                            break;
                        case "_distance":
                            records.setDistance(reader.nextString());
                            break;
                        case "_duration":
                            records.setDuration(reader.nextString());
                            break;
                        case "latLngFile":
                            records.setLatLngFile(reader.nextString());
                            break;
                        default:reader.skipValue();
                            break;
                    }
                }
                reader.endObject();
                return records;
            } catch (IOException e) {}

            return null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.unregisterActivity();
    }
}
