package com.zmychou.paces.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zmychou.paces.MainActivity;
import com.zmychou.paces.R;
import com.zmychou.paces.database.server.UserInfoEntryUtil;
import com.zmychou.paces.network.JsonKey;
import com.zmychou.paces.network.MsgTypeConstant;
import com.zmychou.paces.network.Requests;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TAG = "com.zmychou.paces.LoginActivity";
    public static final String KEY_AUTO_LOGIN = "com.zmychou.paces.LoginActivity.AUTO_LOGIN";
    public static final String HAS_LOGGED = "logged";
    private EditText userName;
    private EditText password;
    private CheckBox autoLogin;
    private Button mLogin;
    private Button mSignup;
    private TextView mVisitor;
    private TextView mForgetPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userName = (EditText) findViewById(R.id.et_login_username);
        password = (EditText) findViewById(R.id.et_login_password);
        mForgetPassword = (TextView) findViewById(R.id.tv_login_forget_password);
        mVisitor = (TextView) findViewById(R.id.tv_login_visitor);
        autoLogin = (CheckBox) findViewById(R.id.cb_login_auto_login);
        mLogin = (Button) findViewById(R.id.btn_login_confirm);
        mSignup = (Button) findViewById(R.id.btn_signup);
        mLogin.setOnClickListener(this);
        mSignup.setOnClickListener(this);
        mForgetPassword.setOnClickListener(this);
        mVisitor.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_login_confirm:
                login(true);
                break;
            case R.id.tv_login_forget_password:
                break;
            case R.id.tv_login_visitor:
                login(false);
                break;
            case R.id.btn_signup:
                startActivity(new Intent(this, SignupActivity.class));
                break;
            default:break;
        }
    }

    private void setAutoLogin(boolean autoLogin) {
        SharedPreferences sp = getSharedPreferences(TAG, Context.MODE_PRIVATE)        ;
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(KEY_AUTO_LOGIN, autoLogin);
    }

    private void loginCallback(boolean log) {
        if (!log) {
            Toast.makeText(this, "登录成功!", Toast.LENGTH_SHORT).show();
        }
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }
    private boolean login(boolean log) {
        if (!log) {
            setAutoLogin(false);
            loginCallback(log);
        }

        Requests requests = new Requests() {

            AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this)
                    .setView(R.layout.waiting_view)
                    .create();
            @Override
            protected void onPreExecute() {
                alertDialog.show();
            }

            @Override
            protected void onPostExecute(InputStream inputStream) {

                if (inputStream == null) {
                    return;
                }
                JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
                String content = null;
                try {
                    reader.beginObject();
                    while (reader.hasNext()) {
                        switch (reader.nextName()) {
                            case JsonKey.TEXT:
                                content = reader.nextString();
                                break;
                            default:reader.skipValue();
                                break;
                        }

                    }
                    reader.endObject();
                } catch (IOException e) {
                }
                alertDialog.dismiss();
                if (content == null) {
                    Toast.makeText(LoginActivity.this, "登录失败!", Toast.LENGTH_SHORT).show();
                    return;
                }
                String[] info = content.split("&");
                if (info.length < 6) {

                    Toast.makeText(LoginActivity.this, "登录失败!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferences preferences = getSharedPreferences(TAG, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(UserInfoEntryUtil._ID,info[0]);
                editor.putString(UserInfoEntryUtil.NICK_NAME,info[1]);
                editor.putString(UserInfoEntryUtil.BIRTHDAY,info[2]);
                editor.putString(UserInfoEntryUtil.LOCATION,info[3]);
                editor.putString(UserInfoEntryUtil.GENDER,info[4]);
                editor.putString(UserInfoEntryUtil.HEIGHT,info[5]);
                editor.putString(UserInfoEntryUtil.WEIGHT,info[6]);
                editor.putString(UserInfoEntryUtil.LEVEL,info[7]);
                editor.putString(UserInfoEntryUtil.AVATAR,info[8]);
                editor.putBoolean(KEY_AUTO_LOGIN, autoLogin.isChecked());
                editor.apply();
                loginCallback(true);
            }
        };
        HashMap<String, String> map = new HashMap<>();
        map.put(JsonKey.MSG_TYPE, MsgTypeConstant.TYPE_SIGNIN+"");
        map.put(UserInfoEntryUtil._ID, userName.getText().toString().trim());
        map.put(UserInfoEntryUtil.PASSWORD, password.getText().toString().trim());
        requests.execute(map);
        return false;
    }
}
