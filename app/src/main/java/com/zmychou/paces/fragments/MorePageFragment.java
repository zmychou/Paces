package com.zmychou.paces.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zmychou.paces.ProfileSummarizeAdapter;
import com.zmychou.paces.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MorePageFragment extends Fragment {


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
    public void onViewCreated(View view,Bundle savedInstanceState){

    }
}
