package com.zmychou.paces.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zmychou.paces.MainActivity;
import com.zmychou.paces.R;
import com.zmychou.paces.constant.NetworkConstant;
import com.zmychou.paces.database.SqliteHelper;
import com.zmychou.paces.database.server.UserInfoEntryUtil;
import com.zmychou.paces.fragments.HomePageFragment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ChangePasswordActivity extends AppCompatActivity
        implements View.OnClickListener, TextWatcher{

    private EditText mCurrent;
    private EditText mNew;
    private EditText mNewCheck;
    private EditText mCode;
    private Button mCommit;
    private TextView mTips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        //导航栏
        setToolbar();

        //
        findViews();

    }

    /**
     * 设置导航栏
     */
    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_activity_change_password_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangePasswordActivity.this.finish();
            }
        });

    }
    /**
     * 找到控件
     */
    private void findViews() {
        mCode = (EditText) findViewById(R.id.et_change_password_code);
        mCommit = (Button) findViewById(R.id.btn_change_password_commit);
        mNewCheck = (EditText) findViewById(R.id.et_change_password_new_check);
        mNew = (EditText) findViewById(R.id.et_change_password_new);
        mCurrent = (EditText) findViewById(R.id.et_change_password_current);
        mTips = (TextView) findViewById(R.id.tv_change_password_activity_tips);

        mCommit.setOnClickListener(this);
//        mCode.addTextChangedListener(this);
//        mCurrent.addTextChangedListener(this);
        mNewCheck.addTextChangedListener(this);
        mNew.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        ChangePasswordTask task = new ChangePasswordTask();
        task.execute(mNew.getText().toString().trim(), mCurrent.getText().toString().trim(),
                mCode.getText().toString());

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() == 0) {
            mCommit.setEnabled(false);
            mTips.setText(R.string.change_password_activity_password_not_null);
            return;
        }
        String current = mCurrent.getText().toString().trim();
        if ("".equals(current)) {
            mTips.setText(R.string.change_password_activity_input_your_password);
            return;
        }
        String newOne = mNew.getText().toString().trim();
        String newTwo = mNewCheck.getText().toString().trim();
        if (newOne.equals(newTwo)) {
            mTips.setText("");
            mCommit.setEnabled(true);
        } else {
            mTips.setText(R.string.change_password_activity_password_not_identical);
            mCommit.setEnabled(false);
        }
    }

    class ChangePasswordTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {
            InputStream inputStream = null;
            SharedPreferences sp = ChangePasswordActivity.this.getSharedPreferences(
                    LoginActivity.TAG, Context.MODE_PRIVATE );
            try {
                URL url = new URL(NetworkConstant.SERVER_ADDR_BASE + "MyServlet");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestProperty(UserInfoEntryUtil._ID, sp.getString(UserInfoEntryUtil._ID, "-"));
                conn.setRequestProperty("msgType", "18");
                conn.setRequestProperty(UserInfoEntryUtil.PASSWORD, params[0]);
                conn.setRequestProperty("current", params[1]);
                OutputStream os = conn.getOutputStream();
                os.write("dd".getBytes());
                os.flush();
                os.close();
                inputStream = conn.getInputStream();
            } catch (MalformedURLException e) {

            } catch (IOException e) {}

            if (inputStream == null) {
                Toast.makeText(ChangePasswordActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                return null;
            }
            String[] result = new String[2];
            JsonReader jr = new JsonReader(new InputStreamReader(inputStream));
            try {
                jr.beginObject();
                while (jr.hasNext()) {
                    switch (jr.nextName()) {
                        case "state":
                            result[0] = jr.nextString();
                            break;

                        case "msg":
                            result[1] = jr.nextString();
                            break;
                        default:
                            jr.skipValue();
                            break;
                    }
                }
                jr.endObject();
            } catch (IOException e) {}

            return result;

        }

        @Override
        protected void onPostExecute(String[] strings) {
                String str = null;
                if ("ok".equals(strings[0])) {
                    str = "修改成功";
                    Toast.makeText(ChangePasswordActivity.this, str, Toast.LENGTH_SHORT).show();

                    //清除用户信息
                    getSharedPreferences(LoginActivity.TAG, Context.MODE_PRIVATE)
                            .edit()
                            .clear()
                            .commit();

                    //清空数据库
                    ChangePasswordActivity.this.deleteDatabase(SqliteHelper.DB_NAME);

                    //修正HomePageFragment 下的 hasSynchronized 变量为 false,因为退出登陆后可能
                    //再次登录,需要进行同步,如果此变量为true ,会跳过同步步骤
                    HomePageFragment.setHasSynchronized(false);

                    //跳转登录界面
                    Intent intent = new Intent(ChangePasswordActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra(MainActivity.FLAG_EXTRA, MainActivity.FINISH_ACTIVITY);
                    startActivity(intent);
                    ChangePasswordActivity.this.finish();
                } else {str = "修改失败,请检查原密码是否正确!";}
                Toast.makeText(ChangePasswordActivity.this, str, Toast.LENGTH_SHORT).show();
        }
    }
}
