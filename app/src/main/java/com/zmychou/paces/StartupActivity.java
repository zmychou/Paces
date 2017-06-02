package com.zmychou.paces;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zmychou.paces.database.server.UserInfoEntryUtil;
import com.zmychou.paces.login.LoginActivity;

public class StartupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = getSharedPreferences(
                        LoginActivity.TAG, Context.MODE_PRIVATE);
                String userId = preferences.getString(UserInfoEntryUtil._ID, "--");
                boolean logged = preferences.getBoolean(LoginActivity.HAS_LOGGED, false);
                if ("--".equals(userId)) {
                    startActivity(new Intent(StartupActivity.this, LoginActivity.class));
                    finish();
                    return;
                }
                startActivity(new Intent(StartupActivity.this, MainActivity.class));
                finish();
            }
        }, 2000);
    }
}
