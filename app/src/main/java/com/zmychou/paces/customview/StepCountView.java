package com.zmychou.paces.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.ColorRes;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.zmychou.paces.R;

/**
 * Created by ming on 17-6-8.
 */
public class StepCountView extends TextView {

    private Paint mPaint;
    private RectF mRect;
    private int mMaxSteps = 1000;
    private int mRealTimeStep = 0;

    public StepCountView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(getColor(R.color.colorGrayLight));
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(10);
        mPaint.setStyle(Paint.Style.STROKE);
        mRect = new RectF();
    }

    public void setText(String text) {
        try {
            mRealTimeStep = Integer.parseInt(text);
        } catch (NumberFormatException e) {
            Log.e("parseInt", e.toString());
        }
        super.setText(text);
    }

    private int getColor(@ColorRes int res) {
        return getContext().getResources().getColor(res);
    }
    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        int height = getHeight();
        int width = getWidth();
        //Get the minimal value of X-axis and Y-axis
        int maxRadius = (height < width ? height : width) / 2;
        int centerInX = width / 2;
        int centerInY = height / 2;

        int strokeWidth = (int)(maxRadius * 0.15);
        mPaint.setStrokeWidth(strokeWidth);
        canvas.drawCircle(centerInX, centerInY, maxRadius - strokeWidth, mPaint);
        mPaint.setColor(getColor(R.color.colorOrangeLight));
        mRect.set((float)(centerInX - maxRadius + strokeWidth),
                (float)(centerInY - maxRadius + strokeWidth),
                (float)(centerInX + maxRadius - strokeWidth),
                (float)(centerInY + maxRadius - strokeWidth));
        float angle = ((float)mRealTimeStep / mMaxSteps) * 360;
        Log.e("proportion", ((float) 12/ mMaxSteps + ""));
        if (angle < 5) {
            angle = 3;
        }
        canvas.drawArc(mRect, -90f, angle, false, mPaint);
        mPaint.setStyle(Paint.Style.FILL);
        float textSize = maxRadius * 0.3f;
        mPaint.setTextSize(textSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(getText(), 0, getText().length(), centerInX, centerInY + textSize / 2, mPaint);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(getColor(R.color.colorGrayLight));
    }
}
