package com.zmychou.paces.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zmychou.paces.R;
import com.zmychou.paces.RunningRecordsActivity;
import com.zmychou.paces.running.RunningActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomePage extends Fragment {

    private ImageView mSummarize;
    private Activity mOwingActivity;
    public HomePage() {
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

        mSummarize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOwingActivity.startActivity(new Intent(mOwingActivity, RunningRecordsActivity.class));
            }
        });
        mOwingActivity.findViewById(R.id.user_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mOwingActivity,RunningActivity.class));
            }
        });
    }

}
