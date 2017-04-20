package com.zmychou.paces.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zmychou.paces.R;
import com.zmychou.paces.database.RunningEntryUtils;
import com.zmychou.paces.weather.WeatherListener;
import com.zmychou.paces.weather.WeatherResult;
import com.zmychou.paces.weather.WeatherSearch;
import com.zmychou.paces.pedestrian.PedestrianActivity;
import com.zmychou.paces.running.RunningRecordsActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomePageFragment extends Fragment implements WeatherListener {

    private ImageView mSummarize;
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
        showWeather("qinhuangdao");


    }

    private void showWeather(String city) {
        WeatherSearch weatherSearch = new WeatherSearch();
        weatherSearch.registerWeatherListener(this);
        city = city == null ? "beijing" : city;
        weatherSearch.searchLiveWeather(city);
    }

    @Override
    public void onWeatherSearchResult(WeatherResult result, String state) {
        if (WeatherResult.STATE_OK.equals(state)) {
            TextView location = (TextView) mOwingActivity.findViewById(R.id.tv_weather_location);
            TextView weather = (TextView) mOwingActivity.findViewById(R.id.tv_weather);
            TextView temperature = (TextView) mOwingActivity.findViewById(R.id.tv_temperature);
            TextView aqi = (TextView) mOwingActivity.findViewById(R.id.tv_PM2_5);
            if (location != null && weather != null && temperature != null && aqi != null) {
                location.setText(result.getCity());
                weather.setText(result.getWeather());
                temperature.setText(result.getTemperature() + "°C");
                aqi.setText(result.getPm2_5());
            }
        }else {
            Toast.makeText(mOwingActivity, "天气信息获取失败", Toast.LENGTH_SHORT).show();
        }

    }
}