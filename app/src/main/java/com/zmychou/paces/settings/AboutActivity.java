package com.zmychou.paces.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zmychou.paces.R;
import com.zmychou.paces.constant.CheckForUpdateMsgConstant;
import com.zmychou.paces.constant.NetworkConstant;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener{


    class CheckUpdateTask extends AsyncTask<Void, Void, HashMap<String, String>> {
        @Override
        protected HashMap<String, String> doInBackground(Void... params) {

            InputStream inputStream = null;
            try {
                URL url = new URL(NetworkConstant.SERVER_ADDR_BASE + "version/version.json");
                inputStream = url.openStream();
            } catch (MalformedURLException e) {
                Log.e("about_activity_debug", "malformedurl");

            } catch (IOException e) {

                Log.e("about_activity_debug", "open in error");
            }
            if (inputStream == null) {
                return null;
            }
            JsonReader jr = new JsonReader(new InputStreamReader(inputStream));
            try {
                jr.beginObject();
                HashMap<String, String> map = new HashMap<>();
                while (jr.hasNext()) {
                    switch (jr.nextName()) {
                        case CheckForUpdateMsgConstant.STATE:
                            map.put(CheckForUpdateMsgConstant.STATE, jr.nextString());
                            break;
                        case CheckForUpdateMsgConstant.APK_URL:
                            map.put(CheckForUpdateMsgConstant.APK_URL, jr.nextString());
                            break;
                        case CheckForUpdateMsgConstant.TEXT:
                            map.put(CheckForUpdateMsgConstant.TEXT, jr.nextString());
                            break;
                        default:jr.skipValue();
                            break;
                    }
                }
                return map;
            } catch (IOException e) {

                Log.e("about_activity_debug", "parse error");
            }
            return null;
    }

        @Override
        protected void onPostExecute(HashMap<String, String> map) {
            if (map == null) {
                Toast.makeText(AboutActivity.this,
                        R.string.about_activity_network_error, Toast.LENGTH_SHORT).show();
                Toast.makeText(AboutActivity.this, "map null", Toast.LENGTH_SHORT).show();
                return;
            }
            if (CheckForUpdateMsgConstant.STATE_UP_TO_DATE.equals(
                    map.get(CheckForUpdateMsgConstant.STATE))) {
                Toast.makeText(AboutActivity.this, R.string.about_activity_up_to_date, Toast.LENGTH_LONG).show();
                return;
            }

        Log.e("about_activity_debug", "here error");
            showUpdateDialog(map.get(CheckForUpdateMsgConstant.TEXT),
                    map.get(CheckForUpdateMsgConstant.APK_URL));
        }

        private void showUpdateDialog(String msg, final String url) {
            AlertDialog dialog = new AlertDialog.Builder(AboutActivity.this)
                    .setTitle(R.string.about_activity_new_version)
                    .setMessage(msg)
                    .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton(R.string.dialog_confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    }).create();
            dialog.show();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Button check = (Button) findViewById(R.id.btn_about_check_for_update);
        TextView author = (TextView) findViewById(R.id.tv_about_activity_author);
        TextView github = (TextView) findViewById(R.id.tv_about_activity_github);
        author.setOnClickListener(this);
        github.setOnClickListener(this);
        check.setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_activity_about_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutActivity.this.finish();
            }
        });
    }

    private Intent createIntent(String action, Uri data, boolean startActivity) {

        Intent intent = new Intent();
        intent.setAction(action);
        intent.setData(data);
        if (startActivity && intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
            return null;
        }
        return intent;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_about_check_for_update:
                new CheckUpdateTask().execute();
                break;
            case R.id.tv_about_activity_github:
                createIntent(Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/zmychou/Paces"), true);
                break;
            case R.id.tv_about_activity_author:
                sendMail(this);
                break;
            default:break;
        }
    }

    public static void sendMail(Context context) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SENDTO);
        intent.setType("text/plain");
        intent.setData(Uri.parse("mailto:"));
        String[] to = {"zmychou@gmail.com"};
        intent.putExtra(Intent.EXTRA_EMAIL, to);
        intent.putExtra(Intent.EXTRA_SUBJECT, "问题反馈");
        intent.putExtra(Intent.EXTRA_TEXT, "收集些用户手机信息和版本信息!");
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }
}
