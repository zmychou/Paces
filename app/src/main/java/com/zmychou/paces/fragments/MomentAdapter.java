package com.zmychou.paces.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zmychou.paces.customview.MomentItemView;

import java.util.ArrayList;

/**
 * <pre>
 * Package    :com.zmychou.paces.fragments
 * Author     : zmychou
 * Create time:17-5-15
 * Last Modify:
 * Email      :zmychou@gmail.com
 * Version    :V1.0
 * description:
 * </pre>
 */

public class MomentAdapter extends RecyclerView.Adapter<MomentAdapter.MyViewHolder> {

    private ArrayList<MomentItemView> mItemList;
    class MyViewHolder extends RecyclerView.ViewHolder{

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
    @Override
    public int getItemCount() {
        return 3;// mItemList.size();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(new MomentItemView(parent.getContext(), null));
    }
}
