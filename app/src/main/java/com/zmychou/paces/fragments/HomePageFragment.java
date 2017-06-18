package com.zmychou.paces.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zmychou.paces.R;
import com.zmychou.paces.customview.CalenderView;
import com.zmychou.paces.database.RunningEntryUtils;
import com.zmychou.paces.database.server.UserInfoEntryUtil;
import com.zmychou.paces.login.LoginActivity;
import com.zmychou.paces.network.DownloadRecordTask;
import com.zmychou.paces.network.ImageLoader;
import com.zmychou.paces.profile.ProfileActivity;
import com.zmychou.paces.running.RunningRecords;
import com.zmychou.paces.weather.WeatherDetailsActivity;
import com.zmychou.paces.weather.WeatherListener;
import com.zmychou.paces.weather.WeatherResultParser;
import com.zmychou.paces.weather.WeatherSearch;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomePageFragment extends Fragment implements WeatherListener , View.OnClickListener{

    private static boolean hasSynchronized = false;
    private ImageView mSummarize;
    private ImageView mUserPic;
    private ImageView mWeather;
    private ImageView mUser;
    private TextView mDistance;
    private TextView mDuration;
    private TextView mTimes;
    private Activity mOwingActivity;
    private CalenderView mCalender;
    private ProgressBar mProgressBar;

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
        mDuration = (TextView) mOwingActivity.findViewById(R.id.tv_home_page_fragment_duration);
        mCalender = (CalenderView) mOwingActivity.findViewById(R.id.cv_home_page_fragment_calender);
        mProgressBar = (ProgressBar) mOwingActivity.findViewById(R.id.pb_home_page_fragment_load_weather);


        mSummarize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOwingActivity.startActivity(new Intent(mOwingActivity, RunningRecordsActivity.class));
            }
        });

        //检查网络状况
        ConnectivityManager connManager
                = (ConnectivityManager) mOwingActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();

        //显示用户信息
        SharedPreferences preferences = mOwingActivity.getSharedPreferences(
                LoginActivity.TAG, Context.MODE_PRIVATE);
        String userName = preferences.getString(UserInfoEntryUtil.NICK_NAME,"用户");
        String userId = preferences.getString(UserInfoEntryUtil._ID, "--");
        TextView name = (TextView) mOwingActivity.findViewById(R.id.nickname);
        TextView id = (TextView) mOwingActivity.findViewById(R.id.user_id);
        name.setText(userName);
        id.setText(userId);

        //显示天气信息
        if (networkInfo != null && networkInfo.getState() == NetworkInfo.State.CONNECTED) {
            showWeather("qinhuangdao");
            loadAvatar(preferences);
        } else {
            Toast.makeText(mOwingActivity, "网络链接错误!", Toast.LENGTH_SHORT).show();
        }

        //设置点击监听事件
        mUserPic.setOnClickListener(this);
        mWeather.setOnClickListener(this);
        mUser.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        setRunningHistory();
    }

    /**
     * 在日历上显示历史记录,如果本地数据库为空,为了防止是用户清除了本地数据或者用户新登录,与服务器同步一次数据
     */
    private void setRunningHistory() {

        RunningEntryUtils utils = new RunningEntryUtils(mOwingActivity);
        Cursor cursor = utils.getAllSummarize();

        //如果本地数据为空切未与服务器同步,则进行同步,否则说明本地有数据或服务器无历史记录
        if (!cursor.moveToFirst() && !hasSynchronized) {
            final AlertDialog dialog = new AlertDialog.Builder(mOwingActivity)
                    .setView(R.layout.waiting_view)
                    .setTitle(R.string.synchronize_title)
                    .create();
            dialog.show();
            DownloadRecordTask task = new DownloadRecordTask(mOwingActivity) {
                @Override
                protected void onPostExecute(ArrayList<RunningRecords> runningRecordses) {
                    setRunningHistory();
                    dialog.dismiss();
                }
            };
            task.execute();
            hasSynchronized = true;
            return;
        }


        mDistance.setText((utils.getTotalDistance() / 1000)+"");
        mTimes.setText(utils.getTotalTimes()+"");
        long duration = utils.getTotalDuration();
        duration /= 1000;
        long hour = duration / 3600;
        long minute = duration % 3600 /60;
        long second = duration % 60;
        StringBuilder sb = new StringBuilder();
        sb.append(hour > 9 ? hour : "0" + hour);
        sb.append(":");
        sb.append(minute > 9 ? minute : "0" + minute);
        sb.append(":");
        sb.append(second > 9 ? second : "0" + second);
        mDuration.setText(sb.toString());
        ArrayList<String>  timestamps = new ArrayList<>();
        if (!cursor.moveToFirst()) {
            return;
        }

        Log.e("Home page fragment", "debug");
        //
        do {
            timestamps.add(cursor.getString(cursor.getColumnIndex(RunningEntryUtils.TIME_STAMP)));

        } while (cursor.moveToNext());

        //设置历史记录在日历上显示
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Date date = new Date();
        for (String timestamp : timestamps) {
            date.setTime(Long.parseLong(timestamp));
            String day = sdf.format(date);
            Log.e("home fragmentl", day);
            mCalender.markDay(R.drawable.btn_green_roundness, day);
        }

    }

    /**
     * 加载头像图片
     * @param sp 存储用户信息的SharePreferences
     */
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

    /**
     * 显示天气信息
     */
    private void showWeather(String city) {
        mProgressBar.setVisibility(View.VISIBLE);
        WeatherSearch weatherSearch = new WeatherSearch(mOwingActivity);
        weatherSearch.registerWeatherListener(this);
        city = city == null ? "beijing" : city;
        weatherSearch.searchLiveWeather(city);
    }

    /**
     * 获取天气信息后的回调
     * @param result 包含天气信息的结果
     * @param state 表示是否获取成功的状态
     */

    @Override
    public void onWeatherSearchResult(HashMap<String, String> result, String state) {
        mProgressBar.setVisibility(View.INVISIBLE);
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

    /**
     * 设置是否已经与服务器同步的标志变量 hasSynchronized
     * @param b 布尔类型变量
     */
    public static void setHasSynchronized(boolean b) {
        hasSynchronized = b;
    }

    /**
     * 修改头像,获得本地图片后的回调
     * @param requestCode
     * @param resultCode
     * @param data
     */
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
                    Bitmap bitmap;
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

    /**
     * 修改头像的图片来源的选择弹窗,可以选择从相册或者拍照获取照片
     */
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

    /**
     * 修改头像,将头新像上传服务器的的工作线程类
     */
    class UpdateAvatarTask extends AsyncTask<String, Void, HashMap<String, String>> {

        /**
         *
         * @param params
         * @return
         */
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

        /**
         * 上传头像
         * @param filePath 资源图片路径
         * @return 服务器反馈处理结果的输入流对象
         */
        public InputStream updateAvatar(String filePath) {
            try {
                FileInputStream in = new FileInputStream(new File(filePath));
                URL url = new URL("http://10.42.0.1:8080/paces/MyServlet");
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                SharedPreferences sp = mOwingActivity.getSharedPreferences(
                        LoginActivity.TAG, Context.MODE_PRIVATE );
                conn.setRequestProperty("_id", sp.getString(UserInfoEntryUtil._ID, "unknown"));
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
