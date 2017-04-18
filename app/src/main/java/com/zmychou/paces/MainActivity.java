package com.zmychou.paces;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.zmychou.paces.fragments.HomePage;
import com.zmychou.paces.io.JsonFileParser;
import com.zmychou.paces.pedestrian.PedestrianActivity;
import com.zmychou.paces.running.RunningActivity;
import com.zmychou.paces.weather.WeatherSearch;

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
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment homePage = new HomePage();
        fragmentTransaction.replace(R.id.fragment_holder, homePage);
        fragmentTransaction.commit();
//
//        JsonFileParser parser = new JsonFileParser();
//        parser.parser(this, "tmp_run_trace_20170411-180131.json");

        ImageView running = (ImageView) findViewById(R.id.iv_main_running);
        ImageView pedestrian = (ImageView) findViewById(R.id.iv_main_pedestrian);
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
    }
}
