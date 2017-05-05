package com.zmychou.paces;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zmychou.paces.database.server.UserInfoEntryUtil;
import com.zmychou.paces.login.LoginActivity;

public class StartupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        SharedPreferences preferences = getSharedPreferences(
                LoginActivity.TAG, Context.MODE_PRIVATE);
        String userId = preferences.getString(UserInfoEntryUtil._ID, "--");
        if ("--".equals(userId)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
