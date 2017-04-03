package com.zmychou.paces;



import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zmychou.paces.fragments.HomePage;
import com.zmychou.paces.profile.ProfileActivity;

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
        fragmentTransaction.replace(R.id.fragment_holder,homePage);
        fragmentTransaction.commit();
        startActivity(new Intent(this,ProfileActivity.class));
    }
}
