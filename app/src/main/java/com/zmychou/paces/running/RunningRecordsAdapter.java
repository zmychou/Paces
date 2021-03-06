package com.zmychou.paces.running;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zmychou.paces.R;
import com.zmychou.paces.database.RunningEntryUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    public static final String TIME_STAMP = "com.zmychou.paces.TIME_STAMP";
    public static final String EXTRA_DISTANCE = "com.zmychou.paces.DISTANCE";
    public static final String EXTRA_DURATION = "com.zmychou.paces.DURATION";
    private Cursor mCursor;
    private Activity mActivity;
    private ArrayList<RunningRecords> mList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView date;
        private TextView distance;
        private TextView duration;
        private TextView timestamp;
//        private TextView date;
        public MyViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.tv_running_record_date);
            distance = (TextView) itemView.findViewById(R.id.tv_running_record_distance);
            duration = (TextView) itemView.findViewById(R.id.tv_running_record_duration);
            timestamp = (TextView) itemView.findViewById(R.id.tv_record_timestamp);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity,ViewRunningRecordActivity.class);
                    intent.putExtra(TIME_STAMP,timestamp.getText());
                    intent.putExtra(EXTRA_DURATION,duration.getText());
                    intent.putExtra(EXTRA_DISTANCE,distance.getText());
                    mActivity.startActivity(intent);
                }
            });
        }

        public void setDate(String date) {
            this.date.setText(date);
        }

        public void setDistance(String distance) {
            this.distance.setText(distance);
        }

        public void setDuration(String duration) {
            this.duration.setText(duration);
        }

        public void setTimestamp(String timestamp) {
            this.timestamp.setText(timestamp);
        }
    }

    public RunningRecordsAdapter(Cursor cursor) {
        mCursor = cursor;
    }
    public RunningRecordsAdapter(ArrayList<RunningRecords> list) {
        mList = list;
    }

    public void registerActivity(Activity activity) {
        mActivity = activity;
    }
    public void unregisterActivity() {
        mActivity = null;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.running_record_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        long timestamp = 0;
        float distance = 0;
        long duration  = 0;
        if (mCursor != null) {
            mCursor.moveToPosition(position);
            String[] names = mCursor.getColumnNames();
            for (String s : names)
                Log.e("names", s);
            timestamp = mCursor.getLong(mCursor.getColumnIndex(RunningEntryUtils.TIME_STAMP));
            distance = mCursor.getFloat(mCursor.getColumnIndex("sum(" + RunningEntryUtils.DISTANCE + ")"));
            duration = mCursor.getLong(mCursor.getColumnIndex("sum(" + RunningEntryUtils.DURATION + ")"));
        } else {
            RunningRecords record = mList.get(position);
            try {
                timestamp = Long.parseLong(record.getTimestamp());
                distance = Float.parseFloat(record.getDistance());
                duration = Long.parseLong(record.getDuration());
            } catch (NumberFormatException e) {}
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd  HH:mm");
        holder.setDate(sdf.format(new Date(timestamp)));
        holder.setDistance((distance / 1000) + "");
        holder.setDuration(formatDuration(duration));
        holder.setTimestamp(timestamp+"");
    }

    @Override
    public int getItemCount() {
        if (mCursor != null) {
            return mCursor.getCount();
        }
        return mList.size();
    }

    private String formatDuration(long duration) {
        long totalSeconds = duration / 1000;
        int secondsInMinute = (int) totalSeconds % 60;
        int minutesInHour = (int) totalSeconds / 60 % 60;
        int hours = (int) totalSeconds / 3600;
        StringBuffer sb = new StringBuffer();
        sb.append(hours);
        sb.append(":");
        if (minutesInHour < 10) {
            sb.append("0");
        }
        sb.append(minutesInHour);
        sb.append(":");
        if (totalSeconds < 10) {
            sb.append("0");
        }
        sb.append(secondsInMinute);
        return sb.toString();
    }
}
