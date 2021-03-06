package com.zmychou.paces;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.zmychou.paces.database.server.UserInfoEntryUtil;
import com.zmychou.paces.login.LoginActivity;

public class StartupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
        );
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
                if (preferences.getBoolean(LoginActivity.KEY_AUTO_LOGIN, false)){
                    startActivity(new Intent(StartupActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(StartupActivity.this, LoginActivity.class));
                }
                finish();
            }
        }, 2000);
    }
}
