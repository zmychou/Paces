package com.zmychou.paces.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.zmychou.paces.R;

/**
 * <pre>
 * Package    :com.zmychou.paces.customview
 * Author     : zmychou
 * Create time:17-5-12
 * Last Modify:
 * Email      :zmychou@gmail.com
 * Version    :V1.0
 * description:
 * </pre>
 */

public class DatePicker extends LinearLayout {

    NumberPicker year;
    NumberPicker month;
    NumberPicker day;

    public DatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.date_picker, this);
        year = (NumberPicker) findViewById(R.id.date_year);
        month = (NumberPicker) findViewById(R.id.date_month);
        day = (NumberPicker) findViewById(R.id.date_day);

        year.setMinValue(1970);
        month.setMinValue(1);
        day.setMinValue(1);
        year.setMaxValue(2017);
        month.setMaxValue(12);
        day.setMaxValue(31);
    }

    public String getDate() {
       return year.getValue() + "-" +month.getValue() + "-" + day.getValue();
    }
}
