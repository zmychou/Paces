package com.zmychou.paces.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zmychou.paces.R;
import com.zmychou.paces.customview.ItemView;
import com.zmychou.paces.customview.MoreItemView;
import com.zmychou.paces.database.server.UserInfoEntryUtil;
import com.zmychou.paces.login.LoginActivity;
import com.zmychou.paces.network.ImageLoader;
import com.zmychou.paces.profile.ProfileActivity;
import com.zmychou.paces.settings.AboutActivity;
import com.zmychou.paces.settings.SettingsActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalPageFragment extends Fragment implements View.OnClickListener{

    private Activity mHost;
    public PersonalPageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personal_page, container, false);
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        mHost = getActivity();

        ItemView profile = (ItemView) mHost.findViewById(R.id.itv_profile);
        MoreItemView settings = (MoreItemView) mHost.findViewById(R.id.itv_settings);
        MoreItemView about = (MoreItemView) mHost.findViewById(R.id.miv_fragment_personal_about);

        profile.setOnClickListener(this);

        SharedPreferences sp = mHost.getSharedPreferences(LoginActivity.TAG, Context.MODE_PRIVATE);
        ImageLoader.getOne()
                .from(sp.getString(UserInfoEntryUtil.AVATAR, "holder"))
                .into(profile.getImage())
                .load();

        about.setOnClickListener(this);
        settings.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.itv_profile:
                startActivity(new Intent(mHost, ProfileActivity.class));
                break;
            case R.id.itv_settings:
                startActivity(new Intent(mHost, SettingsActivity.class));
                break;
            case R.id.miv_fragment_personal_about:
                startActivity(new Intent(mHost, AboutActivity.class));
                break;
            default:break;

        }

    }
}
