package com.zmychou.paces.profile;

import android.content.Context;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zmychou.paces.R;
import com.zmychou.paces.models.SummarizeProfile;

import java.util.ArrayList;

/**
 * The adapter which connect the info summary of user profiles with the recyclerview.
 * Created by zmychou on 17-3-13.
 * Modify by zmychou on 2017/3/14
 */
public class ProfileSummarizeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<SummarizeProfile> itemList;
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public ImageView imageView;
        public MyViewHolder(View view){
            super(view);
            textView = (TextView) view.findViewById(R.id.summarize_row_name);
            imageView = (ImageView) view.findViewById(R.id.summarize_row_pic);
        }

        /**
         * Set the  item's content of the RecyclerView
         * @param content An instance of class SummarizeProfile which contain the whole info of
         *                an item.
         */
        public void setViewContent(SummarizeProfile content){
            textView.setText(content.getName());
        }

    }
    public ProfileSummarizeAdapter(){
        itemList = new ArrayList<>();
        itemList.add(new SummarizeProfile("周明阳"));
        itemList.add(new SummarizeProfile("燕山大学"));
        itemList.add(new SummarizeProfile("计算机"));
    }
    @Override
    public int getItemCount(){
        return itemList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder,int position){
        SummarizeProfile content = itemList.get(position);

        if (viewHolder instanceof MyViewHolder) {
            ((MyViewHolder) viewHolder).setViewContent(content);
        }
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.summarize_list_row,parent,false);
        return new MyViewHolder(view);
    }
}
