package com.zmychou.paces.fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zmychou.paces.R;
import com.zmychou.paces.customview.MoreItemView;
import com.zmychou.paces.music.AudioListActivity;
import com.zmychou.paces.pedestrian.PedestrianActivity;
import com.zmychou.paces.settings.ArticleActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MorePageFragment extends Fragment implements View.OnClickListener{


    private Activity mHostActivity;
    public MorePageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more_page, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mHostActivity = getActivity();

        MoreItemView pedestrian = (MoreItemView)
                mHostActivity.findViewById(R.id.miv_fragment_more_pedestrian);
        MoreItemView chat = (MoreItemView)
                mHostActivity.findViewById(R.id.miv_fragment_more_chat);
        MoreItemView article = (MoreItemView)
                mHostActivity.findViewById(R.id.miv_fragment_more_article);
        MoreItemView music = (MoreItemView)
                mHostActivity.findViewById(R.id.miv_fragment_more_music);
        pedestrian.setOnClickListener(this);
        chat.setOnClickListener(this);
        article.setOnClickListener(this);
        music.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.miv_fragment_more_pedestrian:
                startActivity(new Intent(mHostActivity, PedestrianActivity.class));
                break;
            case R.id.miv_fragment_more_music:
                startActivity(new Intent(mHostActivity, AudioListActivity.class));
                break;
            case R.id.miv_fragment_more_article:
                startActivity(new Intent(mHostActivity, ArticleActivity.class));
                break;
            case R.id.miv_fragment_more_chat:
                break;
            default:break;
        }
    }
}
