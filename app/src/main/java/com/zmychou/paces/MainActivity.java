package com.zmychou.paces;

import android.content.Context;
import android.content.Intent;
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

import com.zmychou.paces.fragments.HomePageFragment;
import com.zmychou.paces.fragments.MorePageFragment;
import com.zmychou.paces.fragments.PersonalPageFragment;
import com.zmychou.paces.login.LoginActivity;
import com.zmychou.paces.music.AudioListActivity;
import com.zmychou.paces.music.AudioPlaybackModel;
import com.zmychou.paces.pedestrian.PedestrianActivity;
import com.zmychou.paces.running.RunningActivity;

import java.util.List;

/**
 * Home page activity.Display some info about the user and its activity history.
 * create:2017/3/11
 * modify:2017/3/14
 */
public class MainActivity extends AppCompatActivity {

    public static final String FLAG_EXTRA = "com.zmychou.paces.FLAG_EXTRA";
    public static final int FINISH_ACTIVITY = 0x01;
    private final int HOME = 0x01;
    private final int MESSAGE = 0x02;
    private final int MORE = 0x03;
    private final int PERSONAL = 0x04;

    ImageView running;
    ImageView more;
    ImageView message;
    ImageView personal;
    ImageView home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent  = getIntent();
        int msg = intent.getIntExtra(FLAG_EXTRA, 0x00);
        if (msg == FINISH_ACTIVITY) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        final Fragment homePage = new HomePageFragment();
        final Fragment personalPage = new PersonalPageFragment();
        final Fragment morePage = new MorePageFragment();
        fragmentTransaction.replace(R.id.fragment_holder, homePage);
        fragmentTransaction.commit();

        running = (ImageView) findViewById(R.id.iv_main_running);
        more = (ImageView) findViewById(R.id.iv_main_more);
        personal = (ImageView) findViewById(R.id.iv_main_personal);
        home = (ImageView) findViewById(R.id.iv_main_home);
        message = (ImageView) findViewById(R.id.iv_main_message);
        running.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,RunningActivity.class));
            }
        });

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_holder,morePage)
                        .commit();
                changeBottomBar(MORE);
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AudioListActivity.class));
            }
        });

        personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_holder,personalPage)
                        .commit();
                changeBottomBar(PERSONAL);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_holder, homePage)
                        .commit();
                changeBottomBar(HOME);
            }
        });
//        detectSensorType();
        AudioPlaybackModel.getInstance().getAudios(this);

    }

    public void detectSensorType() {
        SensorManager sMgr = ( SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> sensors = sMgr.getSensorList(Sensor.TYPE_ALL);
        for (Sensor sensor : sensors) {
            Log.e("sensor type :",sensor.getName());
        }
    }

    private void changeBottomBar(int which) {

        home.setImageResource(R.mipmap.homepage);
        message.setImageResource(R.mipmap.message);
        more.setImageResource(R.mipmap.manage);
        personal.setImageResource(R.mipmap.user);
        switch (which) {
            case HOME:
                home.setImageResource(R.mipmap.homepage_fill);
                break;
            case MESSAGE:
                message.setImageResource(R.mipmap.message_fill);
                break;
            case MORE:
                more.setImageResource(R.mipmap.manage_fill);
                break;
            case PERSONAL:
                personal.setImageResource(R.mipmap.user_fill);
                break;
            default:break;
        }
    }
}
