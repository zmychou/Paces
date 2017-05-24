package com.zmychou.paces.weather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.zmychou.paces.R;

import java.util.HashMap;

public class WeatherDetailsActivity extends AppCompatActivity implements WeatherListener{

    private TextView mTemperature;
    private TextView mCityAndWeather;
    private TextView mWind;
    private TextView mWindSpeed;
    private TextView mHumidity;
    private TextView mAqi;
    private TextView mPM2_5;
    private TextView mPM10;
    private TextView mNO2;
    private TextView mCO;
    private TextView mSO2;
    private TextView mO3;
    private TextView mSport;
    private TextView mUV;
    private TextView mTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_details);

        mTemperature = (TextView) findViewById(R.id.tv_weather_detail_temperature);
        mCityAndWeather = (TextView) findViewById(R.id.tv_weather_detail_city_and_weather);
        mWind = (TextView) findViewById(R.id.tv_weather_detail_wind);
        mWindSpeed = (TextView) findViewById(R.id.tv_weather_detail_wind_speed);
        mHumidity = (TextView) findViewById(R.id.tv_weather_detail_humidity);
        mAqi = (TextView) findViewById(R.id.tv_weather_detail_aqi);
        mPM2_5 = (TextView) findViewById(R.id.tv_weather_detail_PM25);
        mPM10 = (TextView) findViewById(R.id.tv_weather_detail_PM10);
        mNO2 = (TextView) findViewById(R.id.tv_weather_detail_NO2);
        mCO = (TextView) findViewById(R.id.tv_weather_detail_CO);
        mSO2 = (TextView) findViewById(R.id.tv_weather_detail_SO2);
        mO3 = (TextView) findViewById(R.id.tv_weather_detail_O3);
        mSport = (TextView) findViewById(R.id.tv_weather_detail_sport);
        mUV = (TextView) findViewById(R.id.tv_weather_detail_uv);
        mTime = (TextView) findViewById(R.id.tv_weather_detail_time);

        WeatherSearch search = new WeatherSearch();
        search.registerWeatherListener(this);
        search.execute("qinhuangdao");
    }

    private void setViewsText(HashMap<String, String> result) {
        if (result == null) {
            return;
        }
        mTemperature.setText(result.get(WeatherResultParser.TEMPERATURE) + "°C");
        mCityAndWeather.setText(result.get(WeatherResultParser.CITY) + " | "
                + result.get(WeatherResultParser.WEATHER));
        mWind.setText(result.get(WeatherResultParser.WIND));
        mWindSpeed.setText(result.get(WeatherResultParser.WIND_SPEED) + "级");
        mHumidity.setText(result.get(WeatherResultParser.HUMIDITY));
        mAqi.setText(result.get(WeatherResultParser.AQI));
        mPM2_5.setText(result.get(WeatherResultParser.PM2_5));
        mPM10.setText(result.get(WeatherResultParser.PM10));
        mNO2.setText(result.get(WeatherResultParser.NO2));
        mCO.setText(result.get(WeatherResultParser.CO));
        mSO2.setText(result.get(WeatherResultParser.SO2));
        mO3.setText(result.get(WeatherResultParser.O3));
        mSport.setText(result.get(WeatherResultParser.SPORT));
        mUV.setText(result.get(WeatherResultParser.UV));
        mTime.setText(result.get(WeatherResultParser.TIME));
    }

    @Override
    public void onWeatherSearchResult(HashMap<String, String> result, String state) {
        setViewsText(result);
    }
}
