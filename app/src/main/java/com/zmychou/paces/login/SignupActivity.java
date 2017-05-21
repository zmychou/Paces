package com.zmychou.paces.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.zmychou.paces.R;
import com.zmychou.paces.database.server.UserInfoEntryUtil;
import com.zmychou.paces.network.JsonKey;
import com.zmychou.paces.network.MsgTypeConstant;
import com.zmychou.paces.network.Requests;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText mUserName;
    private EditText mPassword;
    private EditText mPasswordCheck;
    private Button mSignup;
    private CheckBox mAgreement;
    private boolean agreed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mUserName = (EditText) findViewById(R.id.et_signup_username);
        mPassword = (EditText) findViewById(R.id.et_signup_password);
        mPasswordCheck = (EditText) findViewById(R.id.et_signup_password_check);
        mAgreement = (CheckBox) findViewById(R.id.cb_signup_agreement);
        mSignup = (Button) findViewById(R.id.btn_signup);
        mSignup.setOnClickListener(this);
        mAgreement.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_signup:
                String p1 = mPassword.getText().toString().trim();
                String p2 = mPasswordCheck.getText().toString().trim();
                String u = mUserName.getText().toString().trim();
                if (p1.equals(p2) && !"".equals(u)) {
                    Requests requests = new Requests() {
                        @Override
                        protected void onPostExecute(InputStream inputStream) {

                            JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
                            int content = 0;
                            String msg = null;
                            try {
                                reader.beginObject();
                                while (reader.hasNext()) {
                                    switch (reader.nextName()) {
                                        case JsonKey.MSG_STATE:
                                            content = reader.nextInt();
                                            break;
                                        case JsonKey.TEXT:
                                            msg = reader.nextString();
                                            break;
                                        default:
                                            reader.skipValue();
                                            break;
                                    }
                                }
                                reader.endObject();
                            } catch (IOException e) {
                            }
                            Toast.makeText(SignupActivity.this, msg, Toast.LENGTH_SHORT).show();
                            if (content == JsonKey.STATE_OK) {
                                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                SignupActivity.this.finish();
                            }
                        }
                    };

                    HashMap<String, String> map = new HashMap<>();
                    map.put(JsonKey.MSG_TYPE, MsgTypeConstant.TYPE_SIGNUP + "");
                    map.put(UserInfoEntryUtil._ID, u);
                    map.put(UserInfoEntryUtil.PASSWORD, p1);
                    requests.execute(map);
                } else {
                    Toast.makeText(this, "用户名为空或密码不一致!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.cb_signup_agreement:
                if (mAgreement.isChecked()) {
                    mSignup.setEnabled(true);
                    break;
                }
                mSignup.setEnabled(false);
                break;
            default:break;
        }
    }
}
