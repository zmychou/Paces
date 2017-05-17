package com.zmychou.paces.fragments;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zmychou.paces.customview.MomentItemView;
import com.zmychou.paces.database.server.MomentEntryUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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

    private ArrayList<HashMap<String, String>> mItemList;
    class MyViewHolder extends RecyclerView.ViewHolder{

        private MomentItemView itemView;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = (MomentItemView) itemView;
        }
        public MomentItemView getItemView() {
            return itemView;
        }
    }

    public void setItemList(ArrayList<HashMap<String, String>> list) {
        mItemList = list;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if (mItemList == null) {
            return 0;
        }
        return mItemList.size();
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        MomentItemView itemView = holder.getItemView();
        HashMap<String, String> item = mItemList.get(position);
        itemView.setPraiseNum(Integer.parseInt(item.get(MomentEntryUtil.FAVOR)));
        itemView.setUserName(item.get(MomentEntryUtil.USER_ID));
        itemView.setContents(item.get(MomentEntryUtil.CONTENT));
        itemView.setPublishTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date(Long.parseLong(item.get(MomentEntryUtil.PUBLIC_TIME)))));
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(new MomentItemView(parent.getContext(), null));
    }
}
