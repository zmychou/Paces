package com.zmychou.paces.profile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zmychou.paces.R;
import com.zmychou.paces.customview.DatePicker;
import com.zmychou.paces.database.server.UserInfoEntryUtil;
import com.zmychou.paces.login.LoginActivity;

import java.util.ArrayList;

/**
 * The adapter which connect the info summary of user profiles with the recyclerview.
 * Created by zmychou on 17-3-13.
 * Modify by zmychou on 2017/3/14
 */
public class ProfileSummarizeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    private ArrayList<SummarizeProfile> itemList;
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public TextView imageView;
        public MyViewHolder(View view){
            super(view);
            textView = (TextView) view.findViewById(R.id.summarize_row_name);
            imageView = (TextView) view.findViewById(R.id.summarize_row_value);
            view.setOnClickListener(new View.OnClickListener() {

                SharedPreferences sp = mContext.getSharedPreferences(LoginActivity.TAG,
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                AlertDialog alertDialog = null;
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                            .setTitle("请选择...")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    switch (textView.getText().toString()) {
                        case "身高":
                             builder = builder.setItems(R.array.height_array, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    clickHandler(dialog, which, 2);
                                }
                            });
                            break;
                        case "体重":
                            builder = builder.setItems(R.array.weight_array, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    clickHandler(dialog, which, 3);
                                }
                            });
                            break;
                        case "生日":
                            builder.setView(R.layout.date_picker_item);
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    clickHandler(dialog, which, 1);
                                }
                            });
                            break;
                        case "性别":
                            builder.setSingleChoiceItems(R.array.gender_array, -1,
                                    new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    clickHandler(dialog, which ,4);
                                }
                            });
                            break;
                        case "昵称":
                            builder.setView(R.layout.activity_edit_profile);
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    clickHandler(dialog, which, 0);
                                }
                            });
                            break;
                        case "位置":
                            builder.setItems(R.array.height_array, null);
                            break;
                        default:break;
                    }

                    alertDialog = builder.create();
                    alertDialog.show();
                }

                private void clickHandler(DialogInterface dialog, int which, int pos) {
                    String[] res = null;
                    switch (pos) {
                        case 0:
                            TextView edit = (EditText) alertDialog.findViewById(R.id.et_edit_profile);
                            String text = edit.getText().toString().trim();
                            itemList.add(pos, itemList.get(pos).setValue(text));
                            editor.putString(UserInfoEntryUtil.NICK_NAME, text);
                            break;
                        case 1:
                            DatePicker picker = (DatePicker) alertDialog.findViewById(R.id.date_picker);
                            String date = picker.getDate();
                            itemList.add(pos, itemList.get(pos).setValue(date));
                            editor.putString(UserInfoEntryUtil.BIRTHDAY, date);
                            break;

                        case 2:
                            res = mContext.getResources().getStringArray(
                                    R.array.height_array);
                            editor.putString(UserInfoEntryUtil.HEIGHT, res[which]);
                            break;
                        case 3:
                            res = mContext.getResources().getStringArray(
                                    R.array.weight_array);
                            editor.putString(UserInfoEntryUtil.WEIGHT, res[which]);
                            break;
                        case 4:
                            res = mContext.getResources().getStringArray(R.array.gender_array);
                            editor.putString(UserInfoEntryUtil.GENDER, res[which]);
                        default:break;
                    }
                    if (res != null) {
                        itemList.add(pos, itemList.get(pos).setValue(res[which]));
                    }
                    itemList.remove(pos + 1);
                    editor.apply();
                    ProfileSummarizeAdapter.this.notifyDataSetChanged();
                    dialog.dismiss();
                    Toast.makeText(mContext, "修改成功!", Toast.LENGTH_SHORT).show();
                }
            });
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
        mContext = context;
        itemList = new ArrayList<>();
        SharedPreferences sp = context.getSharedPreferences(LoginActivity.TAG, Context.MODE_PRIVATE);
        itemList.add(new SummarizeProfile("昵称", sp.getString(UserInfoEntryUtil.NICK_NAME, "未知")));
        itemList.add(new SummarizeProfile("生日", sp.getString(UserInfoEntryUtil.BIRTHDAY, "未知")));
        itemList.add(new SummarizeProfile("身高", sp.getString(UserInfoEntryUtil.HEIGHT, "未知")));
        itemList.add(new SummarizeProfile("体重", sp.getString(UserInfoEntryUtil.WEIGHT, "未知")));
        itemList.add(new SummarizeProfile("性别", sp.getString(UserInfoEntryUtil.GENDER, "未知")));
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
