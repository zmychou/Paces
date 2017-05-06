package com.zmychou.paces.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zmychou.paces.R;
import com.zmychou.paces.database.server.UserInfoEntryUtil;
import com.zmychou.paces.login.LoginActivity;

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
        public TextView imageView;
        public MyViewHolder(View view){
            super(view);
            textView = (TextView) view.findViewById(R.id.summarize_row_name);
            imageView = (TextView) view.findViewById(R.id.summarize_row_value);
        }

        /**
         * Set the  item's content of the RecyclerView
         * @param content An instance of class SummarizeProfile which contain the whole info of
         *                an item.
         */
        public void setViewContent(SummarizeProfile content){
            textView.setText(content.getKey());
            imageView.setText(content.getValue());
        }

    }
    public ProfileSummarizeAdapter(Context context){
        itemList = new ArrayList<>();
        SharedPreferences sp = context.getSharedPreferences(LoginActivity.TAG, Context.MODE_PRIVATE);
        itemList.add(new SummarizeProfile("昵称", sp.getString(UserInfoEntryUtil.NICK_NAME, "未知")));
        itemList.add(new SummarizeProfile("生日", sp.getString(UserInfoEntryUtil.BIRTHDAY, "未知")));
        itemList.add(new SummarizeProfile("身高", sp.getString(UserInfoEntryUtil.HEIGHT, "未知")));
        itemList.add(new SummarizeProfile("体重", sp.getString(UserInfoEntryUtil.WEIGHT, "未知")));
        itemList.add(new SummarizeProfile("位置", sp.getString(UserInfoEntryUtil.LOCATION, "未知")));

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
