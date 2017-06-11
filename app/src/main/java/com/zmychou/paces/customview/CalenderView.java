package com.zmychou.paces.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zmychou.paces.R;

import java.util.Calendar;

/**
 * Created by ming on 17-6-11.
 */
public class CalenderView extends LinearLayout implements View.OnClickListener {

    private TextView[] mTextViews = new TextView[35];

    private Calendar mCalender ;

    private void findViews() {
        mCalender = Calendar.getInstance();
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

        for (TextView tv : mTextViews) {
            tv.setOnClickListener(this);
        }

        for (int i = 3; i< 32; i++) {
            mTextViews[i].setText((i - 3) + "");
        }
    }

    public CalenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.calender_view, this);
        findViews();
    }

    public void markDay(int day) {

    }

    @Override
    public void onClick(View v) {
        Toast.makeText(v.getContext(), mCalender.getActualMinimum(Calendar.DAY_OF_WEEK_IN_MONTH) + "",
                Toast.LENGTH_SHORT).show();
    }
}
