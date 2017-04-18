package com.zmychou.paces.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zmychou.paces.R;
import com.zmychou.paces.customview.ItemView;
import com.zmychou.paces.profile.ProfileActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MorePageFragment extends Fragment {

    private Activity mHost;
    public MorePageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more_page, container, false);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        mHost = getActivity();

        ItemView profile = (ItemView) mHost.findViewById(R.id.itv_profile);
        ItemView settings = (ItemView) mHost.findViewById(R.id.itv_settings);
//        ItemView about = (ItemView) mHost.findViewById(R.id.it);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHost.startActivity(new Intent(mHost, ProfileActivity.class));
            }
        });

    }
}
