package com.zmychou.paces.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.zmychou.paces.R;

/**
 * <pre>
 * Package    :com.zmychou.paces.customview
 * Author     : zmychou
 * Create time:17-5-14
 * Last Modify:
 * Email      :zmychou@gmail.com
 * Version    :V1.0
 * description:
 * </pre>
 */

public class MomentItemView extends LinearLayout {

    public MomentItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.getTheme().obtainStyledAttributes(R.styleable.MomentItemView);
        try {

        } finally {
            ta.recycle();
        }
    }
}
