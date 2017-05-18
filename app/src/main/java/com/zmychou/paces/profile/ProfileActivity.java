package com.zmychou.paces.profile;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.zmychou.paces.DividerItemDecoration;
import com.zmychou.paces.MainActivity;
import com.zmychou.paces.R;
import com.zmychou.paces.login.LoginActivity;

/**
 * To display the user's profiles ,such as id,name,height,wight and so on.
 *
 * create at: 2017/3/12
 * last modify:2017/3/14
 * @author zmychou
 *
 * */

public class ProfileActivity extends AppCompatActivity {

    ProfileSummarizeAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        RecyclerView list = (RecyclerView) findViewById(R.id.profile_summarize_list);
         adapter = new ProfileSummarizeAdapter(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        list.setLayoutManager(mLayoutManager);
        list.setItemAnimator(new DefaultItemAnimator());
        list.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        list.setAdapter(adapter);

        Button logout = (Button) findViewById(R.id.btn_profile_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSharedPreferences(LoginActivity.TAG, Context.MODE_PRIVATE)
                        .edit()
                        .clear()
                        .commit();
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(MainActivity.FLAG_EXTRA, MainActivity.FINISH_ACTIVITY);
                startActivity(intent);
                ProfileActivity.this.finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}
