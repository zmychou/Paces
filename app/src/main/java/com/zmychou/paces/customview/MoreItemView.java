package com.zmychou.paces.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zmychou.paces.R;

import java.util.zip.Inflater;

/**
 * <pre>
 * Package    :com.zmychou.paces.customview
 * Author     : zmychou
 * Create time:17-5-13
 * Last Modify:
 * Email      :zmychou@gmail.com
 * Version    :V1.0
 * description:
 * </pre>
 */

public class MoreItemView extends LinearLayout {

    ImageView imageView;
    TextView textView;
    public MoreItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs,R.styleable.MoreItemView,0,0);
        int res;
        String text;
        try {
            res = ta.getResourceId(R.styleable.MoreItemView_src, 0) ;
            text =ta.getString(R.styleable.MoreItemView_text);
        } finally {
            ta.recycle();
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.list_item, this);
        imageView = (ImageView) findViewById(R.id.img);
        textView = (TextView) findViewById(R.id.text);
        imageView.setImageResource(res);
        textView.setText(text);
    }
}
