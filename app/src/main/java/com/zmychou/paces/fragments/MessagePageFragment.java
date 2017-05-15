package com.zmychou.paces.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zmychou.paces.DividerItemDecoration;
import com.zmychou.paces.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessagePageFragment extends Fragment {


    private Activity mHostActivity;
    public MessagePageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message_page, container, false);
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        mHostActivity = getActivity();

        MomentAdapter adapter = new MomentAdapter();
        RecyclerView recyclerView =
                (RecyclerView) mHostActivity.findViewById(R.id.rv_fragment_message_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(mHostActivity));
        recyclerView.addItemDecoration(new DividerItemDecoration(mHostActivity, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }



}
