package com.zmychou.paces.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zmychou.paces.R;

/**
 * <pre>
 * Package    :com.zmychou.paces.customview
 * Author     : zmychou
 * Create time:17-4-18
 * Last Modify:
 * Email      :zmychou@gmail.com
 * Version    :V1.0
 * description:
 * </pre>
 */

public class ItemView extends LinearLayout {

    private TextView mItemName;
    private ImageView mImage;
    private String text;
    private int reference;
    public ItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs,R.styleable.ItemView,0,0);
        try {
            text = ta.getString(R.styleable.ItemView_item_name);
            reference = ta.getResourceId(R.styleable.ItemView_item_src,0);
        } finally {
            ta.recycle();
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.summarize_list_row, this);

        mItemName = (TextView) findViewById(R.id.summarize_row_name);
        mImage = (ImageView) findViewById(R.id.summarize_row_pic);

        setItemName(text);
        if (reference != 0)
            setImage(reference);
    }

    public void setItemName(String name) {
        mItemName.setText(name);
    }

    public void setImage(int res) {
        mImage.setImageResource(res);
    }

    public ImageView getImage() {
        return mImage;
    }
}
