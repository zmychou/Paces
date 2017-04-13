package com.zmychou.paces;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zmychou.paces.database.RunningEntryUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <pre>
 * Package    :com.zmychou.paces
 * Author     : zmychou
 * Create time:17-4-13
 * Last Modify:
 * Email      :zmychou@gmail.com
 * Version    :V1.0
 * description:
 * </pre>
 */
public class RunningRecordsAdapter extends RecyclerView.Adapter<RunningRecordsAdapter.MyViewHolder> {

    private Cursor mCursor;
    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView date;
        private TextView distance;
//        private TextView date;
        public MyViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.tv_running_record_date);
            distance = (TextView) itemView.findViewById(R.id.tv_running_record_distance);
        }

        public void setDate(String date) {
            this.date.setText(date);
        }

        public void setDistance(String distance) {
            this.distance.setText(distance);
        }
    }

    public RunningRecordsAdapter(Cursor cursor) {
        mCursor = cursor;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.running_record_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        long timestamp = mCursor.getLong(mCursor.getColumnIndex(RunningEntryUtils.TIME_STAMP));
        float distance = mCursor.getFloat(mCursor.getColumnIndex(RunningEntryUtils.DISTANCE));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd  HH:mm");
        holder.setDate(sdf.format(new Date(timestamp)));
        holder.setDistance(distance+"");
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }
}
