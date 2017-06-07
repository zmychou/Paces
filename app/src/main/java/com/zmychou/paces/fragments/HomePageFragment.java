package com.zmychou.paces.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.telecom.ConnectionService;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zmychou.paces.R;
import com.zmychou.paces.database.RunningEntryUtils;
import com.zmychou.paces.database.server.UserInfoEntryUtil;
import com.zmychou.paces.login.LoginActivity;
import com.zmychou.paces.network.ImageLoader;
import com.zmychou.paces.profile.ProfileActivity;
import com.zmychou.paces.weather.WeatherDetailsActivity;
import com.zmychou.paces.weather.WeatherListener;
import com.zmychou.paces.weather.WeatherResult;
import com.zmychou.paces.weather.WeatherResultParser;
import com.zmychou.paces.weather.WeatherSearch;
import com.zmychou.paces.pedestrian.PedestrianActivity;
import com.zmychou.paces.running.RunningRecordsActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomePageFragment extends Fragment implements WeatherListener , View.OnClickListener{

    private ImageView mSummarize;
    private ImageView mUserPic;
    private ImageView mWeather;
    private ImageView mUser;
    private TextView mDistance;
    private TextView mTimes;
    private Activity mOwingActivity;
    public HomePageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_page, container, false);
    }
    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);
        mOwingActivity = getActivity();
        mSummarize = (ImageView) mOwingActivity.findViewById(R.id.summarize);
        mWeather = (ImageView) mOwingActivity.findViewById(R.id.weather_bg_img);
        mUser = (ImageView) mOwingActivity.findViewById(R.id.user_info_bgimg);
        mUserPic = (ImageView) mOwingActivity.findViewById(R.id.user_img);
        mDistance = (TextView) mOwingActivity.findViewById(R.id.tv_home_total_distance);
        mTimes = (TextView) mOwingActivity.findViewById(R.id.tv_home_times);

        RunningEntryUtils utils = new RunningEntryUtils(mOwingActivity);
        mDistance.setText((utils.getTotalDistance() / 1000)+"");
        mTimes.setText(utils.getTotalTimes()+"");
        mSummarize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOwingActivity.startActivity(new Intent(mOwingActivity, RunningRecordsActivity.class));
            }
        });
        ConnectivityManager connManager
                = (ConnectivityManager) mOwingActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        SharedPreferences preferences = mOwingActivity.getSharedPreferences(
                LoginActivity.TAG, Context.MODE_PRIVATE);
        String userName = preferences.getString(UserInfoEntryUtil.NICK_NAME,"用户");
        String userId = preferences.getString(UserInfoEntryUtil._ID, "--");
        TextView name = (TextView) mOwingActivity.findViewById(R.id.nickname);
        TextView id = (TextView) mOwingActivity.findViewById(R.id.user_id);
        name.setText(userName);
        id.setText(userId);
        if (networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED) {
            showWeather("qinhuangdao");
            loadAvatar(preferences);
        } else {
            Toast.makeText(mOwingActivity, "网络链接错误!", Toast.LENGTH_SHORT).show();
        }

        mUserPic.setOnClickListener(this);
        mWeather.setOnClickListener(this);
        mUser.setOnClickListener(this);

    }

    private void loadAvatar(SharedPreferences sp) {
        ImageLoader loader = new ImageLoader();
        File file = new File(Environment.getExternalStorageDirectory(), "Paces/cache/avatar/paces");
        if(!file.exists()) {
            loader.from(sp.getString(UserInfoEntryUtil.AVATAR, "holder"), false);
        } else {
            loader.from("file://"+file.getPath(), true);
        }

        loader.into(mUserPic)
                .load();
    }

    private void showWeather(String city) {
        WeatherSearch weatherSearch = new WeatherSearch(mOwingActivity);
        weatherSearch.registerWeatherListener(this);
        city = city == null ? "beijing" : city;
        weatherSearch.searchLiveWeather(city);
    }

    @Override
    public void onWeatherSearchResult(HashMap<String, String> result, String state) {
        if (result.get(WeatherResultParser.STATE).equals(WeatherResultParser.STATE_OK)) {
            TextView location = (TextView) mOwingActivity.findViewById(R.id.tv_weather_location);
            TextView weather = (TextView) mOwingActivity.findViewById(R.id.tv_weather);
            TextView temperature = (TextView) mOwingActivity.findViewById(R.id.tv_temperature);
            TextView aqi = (TextView) mOwingActivity.findViewById(R.id.tv_PM2_5);
            if (location != null && weather != null && temperature != null && aqi != null) {
                location.setText(result.get(WeatherResultParser.CITY));
                weather.setText(result.get(WeatherResultParser.WEATHER));
                temperature.setText(result.get(WeatherResultParser.TEMPERATURE) + "°C");
                aqi.setText(result.get(WeatherResultParser.AQI));
            }
        }else {
            Toast.makeText(mOwingActivity, "天气信息获取失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_img:
                showSourceDialog();
                break;
            case R.id.user_info_bgimg:
                startActivity(new Intent(mOwingActivity, ProfileActivity.class));
                break;
            case R.id.weather_bg_img:
                startActivity(new Intent(mOwingActivity, WeatherDetailsActivity.class));
                break;
            default:break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            UpdateAvatarTask task = new UpdateAvatarTask();
            switch (requestCode) {
                case 1:
                task.execute(data.getData().getPath());
                Log.e("result data:", data.getData().getEncodedPath());
                    break;
                case 2:
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap =null;
                    if ((bitmap = (Bitmap) bundle.get("data")) != null) {
                        File filePath = new File(Environment.getExternalStorageDirectory(), "Paces/tmp");
                        if (!filePath.exists()) {
                            filePath.mkdirs();
                        }
                        try {
                            File file = File.createTempFile("tempFile", ".jpg", filePath);
                            FileOutputStream out = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
                            task.execute(file.getPath());

                        } catch (IOException e) {}
                    } else {
                        Log.e("capture photo", "failed");
                    }
                        break;
                default:break;
            }
            return;
        }
        Toast.makeText(mOwingActivity, R.string.cancel_operate, Toast.LENGTH_SHORT).show();
    }

    public void showSourceDialog() {
        AlertDialog dialog = new AlertDialog.Builder(mOwingActivity)
                .setItems(R.array.choose_avatar_from_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        switch (which) {
                            case 0:
                                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, 2);
                                break;
                            case 1:
                                intent.setAction(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(intent, 1);
                                break;
                            default:break;
                        }
                        dialog.dismiss();
                    }
                })
                .create();
        dialog.show();
    }

    class UpdateAvatarTask extends AsyncTask<String, Void, HashMap<String, String>> {
        @Override
        protected HashMap<String, String> doInBackground(String... params) {

            InputStream in = updateAvatar(params[0]);
            if (in == null) {
                Log.e("result", "debug");
                return null;
            }
            InputStreamReader isw = new InputStreamReader(in);

            BufferedReader br = new BufferedReader(isw);
            try {

                Log.e("result",br.readLine());
            } catch (IOException e) {}
//            JsonReader jr = new JsonReader(new InputStreamReader(in));
//            try {
//                jr.beginObject();
//            }
            return null;
        }

        public InputStream updateAvatar(String filePath) {
            try {
                FileInputStream in = new FileInputStream(new File(filePath));
                URL url = new URL("http://10.42.0.1:8080/paces/MyServlet");
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestProperty("_id", "zmychou");
                conn.setRequestProperty("msgType", "17");
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(osw);

                byte[] buff = new byte[1024];
                for (int len = 0; (len = in.read(buff)) != -1; ) {
                    os.write(buff, 0, len);
                }
                osw.flush();
                osw.close();

                return conn.getInputStream();
            }catch(MalformedURLException e) {
                Log.e("mal","debug");
            }catch(IOException e) {
                Log.e("ioe", e.toString());
            }
            return null;
        }
    }

}
