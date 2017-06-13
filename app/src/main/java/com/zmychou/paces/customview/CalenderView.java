package com.zmychou.paces.customview;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zmychou.paces.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by ming on 17-6-11.
 */
public class CalenderView extends LinearLayout implements View.OnClickListener {

    private TextView[] mTextViews = new TextView[42];
    private TextView mDate;

    private Calendar mCalender ;

    private void findViews() {
        mCalender = Calendar.getInstance();
        mDate = (TextView) findViewById(R.id.tv_calender_view_title);
        mTextViews[0] = (TextView) findViewById(R.id.tv_calender_view_day_00);
        mTextViews[1] = (TextView) findViewById(R.id.tv_calender_view_day_01);
        mTextViews[2] = (TextView) findViewById(R.id.tv_calender_view_day_02);
        mTextViews[3] = (TextView) findViewById(R.id.tv_calender_view_day_03);
        mTextViews[4] = (TextView) findViewById(R.id.tv_calender_view_day_04);
        mTextViews[5] = (TextView) findViewById(R.id.tv_calender_view_day_05);
        mTextViews[6] = (TextView) findViewById(R.id.tv_calender_view_day_06);
        mTextViews[7] = (TextView) findViewById(R.id.tv_calender_view_day_10);
        mTextViews[8] = (TextView) findViewById(R.id.tv_calender_view_day_11);
        mTextViews[9] = (TextView) findViewById(R.id.tv_calender_view_day_12);
        mTextViews[10] = (TextView) findViewById(R.id.tv_calender_view_day_13);
        mTextViews[11] = (TextView) findViewById(R.id.tv_calender_view_day_14);
        mTextViews[12] = (TextView) findViewById(R.id.tv_calender_view_day_15);
        mTextViews[13] = (TextView) findViewById(R.id.tv_calender_view_day_16);
        mTextViews[14] = (TextView) findViewById(R.id.tv_calender_view_day_20);
        mTextViews[15] = (TextView) findViewById(R.id.tv_calender_view_day_21);
        mTextViews[16] = (TextView) findViewById(R.id.tv_calender_view_day_22);
        mTextViews[17] = (TextView) findViewById(R.id.tv_calender_view_day_23);
        mTextViews[18] = (TextView) findViewById(R.id.tv_calender_view_day_24);
        mTextViews[19] = (TextView) findViewById(R.id.tv_calender_view_day_25);
        mTextViews[20] = (TextView) findViewById(R.id.tv_calender_view_day_26);
        mTextViews[21] = (TextView) findViewById(R.id.tv_calender_view_day_30);
        mTextViews[22] = (TextView) findViewById(R.id.tv_calender_view_day_31);
        mTextViews[23] = (TextView) findViewById(R.id.tv_calender_view_day_32);
        mTextViews[24] = (TextView) findViewById(R.id.tv_calender_view_day_33);
        mTextViews[25] = (TextView) findViewById(R.id.tv_calender_view_day_34);
        mTextViews[26] = (TextView) findViewById(R.id.tv_calender_view_day_35);
        mTextViews[27] = (TextView) findViewById(R.id.tv_calender_view_day_36);
        mTextViews[28] = (TextView) findViewById(R.id.tv_calender_view_day_40);
        mTextViews[29] = (TextView) findViewById(R.id.tv_calender_view_day_41);
        mTextViews[30] = (TextView) findViewById(R.id.tv_calender_view_day_42);
        mTextViews[31] = (TextView) findViewById(R.id.tv_calender_view_day_43);
        mTextViews[32] = (TextView) findViewById(R.id.tv_calender_view_day_44);
        mTextViews[33] = (TextView) findViewById(R.id.tv_calender_view_day_45);
        mTextViews[34] = (TextView) findViewById(R.id.tv_calender_view_day_46);
        mTextViews[35] = (TextView) findViewById(R.id.tv_calender_view_day_50);
        mTextViews[36] = (TextView) findViewById(R.id.tv_calender_view_day_51);
        mTextViews[37] = (TextView) findViewById(R.id.tv_calender_view_day_52);
        mTextViews[38] = (TextView) findViewById(R.id.tv_calender_view_day_53);
        mTextViews[39] = (TextView) findViewById(R.id.tv_calender_view_day_54);
        mTextViews[40] = (TextView) findViewById(R.id.tv_calender_view_day_55);
        mTextViews[41] = (TextView) findViewById(R.id.tv_calender_view_day_56);

        for (TextView tv : mTextViews) {
            tv.setOnClickListener(this);
        }

        for (int i = 0; i < getMaximumDayOfCurrentMonth(); i++) {
            mTextViews[getDayInFirstWeek() - 1 + i].setText(i + 1 + "");
        }

        Date date = new Date(System.currentTimeMillis());
        markDay(R.drawable.btn_red_roundness, date.getDate() + "");
//        markDays(R.drawable.btn_green_roundness, "1", "3", "6", "9");
    }

    public CalenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.calender_view, this);
        findViews();
    }

    public void markDay(@DrawableRes int shape, String day) {

        mTextViews[getDayInFirstWeek() + Integer.parseInt(day) - 2].setBackgroundResource(shape);
    }

    public void markDays(@DrawableRes int shape, String... days) {
        for (String day : days) {
            markDay(shape, day);
        }
    }

    public int getDayInFirstWeek() {
        Date date = new Date(System.currentTimeMillis());
        mCalender.setTime(date);
        mDate.setText(date.getYear() + 1900 +"/"+ ((date.getMonth() + 1)));
        mCalender.set(date.getYear() + 1900, date.getMonth(), 1);
        return mCalender.get(Calendar.DAY_OF_WEEK);
    }

    private int getMaximumDayOfCurrentMonth() {
        return mCalender.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(v.getContext(), getMaximumDayOfCurrentMonth()+"",Toast.LENGTH_SHORT).show();
    }
}
