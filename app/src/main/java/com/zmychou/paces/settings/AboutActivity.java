package com.zmychou.paces.settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.zmychou.paces.R;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Button check = (Button) findViewById(R.id.btn_about_check_for_update);
        check.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(AboutActivity.this, "已是最新版本!", Toast.LENGTH_LONG).show();
    }
}
