package com.zmychou.paces.moment;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zmychou.paces.R;
import com.zmychou.paces.database.server.UserInfoEntryUtil;
import com.zmychou.paces.login.LoginActivity;
import com.zmychou.paces.network.JsonKey;
import com.zmychou.paces.network.MsgTypeConstant;
import com.zmychou.paces.network.Requests;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class PublishMomentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_moment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_activity_publish_moment_toolbar);
        setSupportActionBar(toolbar);

        final EditText content = (EditText) findViewById(R.id.et_publish_moment_input);
        Button send = (Button) findViewById(R.id.btn_publish_moment_publish);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publish(content);
            }
        });
    }

    private void publish(EditText editText) {
        String text = editText.getText().toString().trim();
        if ("".equals(text)) {
            return;
        }
        HashMap<String, String> map = new HashMap<>();
        SharedPreferences sp = getSharedPreferences(LoginActivity.TAG, Context.MODE_PRIVATE);
        map.put(UserInfoEntryUtil._ID, sp.getString(UserInfoEntryUtil._ID, " "));
        map.put(JsonKey.MSG_TYPE, 13 + "");
        map.put(JsonKey.USER_ID, sp.getString(UserInfoEntryUtil._ID, ""));
        map.put(JsonKey.CONTENT, text);
        Requests requests = new Requests(){
            @Override
            protected void onPreExecute() {
                showWaitingDialog(PublishMomentActivity.this);
            }

            @Override
            protected void onPostExecute(InputStream inputStream) {
                dismissWaitingDialog();
                if (inputStream == null) {
                    Toast.makeText(PublishMomentActivity.this, "网络错误!", Toast.LENGTH_SHORT).show();
                    return;
                }
                JsonReader jr = new JsonReader(new InputStreamReader(inputStream));
                try {
                    jr.beginObject();
                    while (jr.hasNext()) {
                        switch (jr.nextName()) {
                            case JsonKey.TEXT:
                                Toast.makeText(PublishMomentActivity.this, jr.nextString(),
                                        Toast.LENGTH_SHORT).show();
                                PublishMomentActivity.this.finish();
                                break;
                            default:jr.skipValue();
                                break;
                        }
                    }
                } catch (IOException e) {}
            }
        };
        requests.execute(map);
    }
}
