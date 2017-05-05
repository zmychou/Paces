package com.zmychou.paces;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zmychou.paces.database.server.UserInfoEntryUtil;
import com.zmychou.paces.fragments.HomePageFragment;
import com.zmychou.paces.fragments.MorePageFragment;
import com.zmychou.paces.music.AudioListActivity;
import com.zmychou.paces.music.AudioPlaybackModel;
import com.zmychou.paces.pedestrian.PedestrianActivity;
import com.zmychou.paces.running.RunningActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Home page activity.Display some info about the user and its activity history.
 * create:2017/3/11
 * modify:2017/3/14
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        final Fragment homePage = new HomePageFragment();
        final Fragment morePage = new MorePageFragment();
        fragmentTransaction.replace(R.id.fragment_holder, homePage);
        fragmentTransaction.commit();

        ImageView running = (ImageView) findViewById(R.id.iv_main_running);
        ImageView pedestrian = (ImageView) findViewById(R.id.iv_main_pedestrian);
        ImageView more = (ImageView) findViewById(R.id.iv_main_more);
        ImageView home = (ImageView) findViewById(R.id.iv_main_home);
        ImageView music = (ImageView) findViewById(R.id.iv_main_music);
        running.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RunningActivity.class));
            }
        });

        pedestrian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,PedestrianActivity.class));
            }
        });

        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AudioListActivity.class));
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_holder,morePage)
                        .commit();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_holder, homePage)
                        .commit();
            }
        });
        detectSensorType();
        AudioPlaybackModel.getInstance().getAudios(this);

    }

    public void detectSensorType() {
        SensorManager sMgr = ( SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors = sMgr.getSensorList(Sensor.TYPE_ALL);
        for (Sensor sensor : sensors) {
            Log.e("sensor type :",sensor.getName());
        }
    }
}
