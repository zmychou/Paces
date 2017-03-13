package com.zmychou.paces;

import android.content.Context;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zmychou.paces.models.SummarizeProfile;

import java.util.ArrayList;

/**
 * Created by ming on 17-3-13.
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
        public void setViewContent(SummarizeProfile content){
            textView.setText(content.getName());
        }

    }
    public ProfileSummarizeAdapter(){
        itemList = new ArrayList<>();
        itemList.add(new SummarizeProfile("周明阳"));
        itemList.add(new SummarizeProfile("燕山大学"));
        itemList.add(new SummarizeProfile("计算机"));
        Log.e("zmy","here"+itemList.size());
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
        //RecyclerView.ViewHolder viewHolder = ViewGroup;
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.summarize_list_row,parent,false);

        return new MyViewHolder(view);
    }
}
