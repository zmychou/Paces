package com.zmychou.paces.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zmychou.paces.R;
import com.zmychou.paces.network.ImageLoader;

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

public class MomentItemView extends LinearLayout implements View.OnClickListener{

    private ImageView mAvatar;
    private ImageView mImage;
    private ImageView mPraise;
    private TextView mUserName;
    private TextView mTime;
    private TextView mPraiseNum;
    private int mPraises;
    private boolean mIsPraise;
    private String mUserId;
    private String mFriendId;
    private String mMomentId;

    public MomentItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        int avatar;
        int img;
        String userName;
        String time;
        int praise;
        TypedArray ta = context.getTheme().obtainStyledAttributes(R.styleable.MomentItemView);
        try {
            avatar = ta.getResourceId(R.styleable.MomentItemView_moment_avatar, 0);
            img = ta.getResourceId(R.styleable.MomentItemView_moment_avatar, 0);
            time = ta.getString(R.styleable.MomentItemView_moment_time);
            praise = ta.getInteger(R.styleable.MomentItemView_moment_praise, 0);

        } finally {
            ta.recycle();
        }
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.moment_item,this);
        mAvatar = (ImageView) findViewById(R.id.moment_item_avatar);
        mImage = (ImageView) findViewById(R.id.moment_item_img);
        mPraise = (ImageView) findViewById(R.id.moment_item_praise);
        mUserName = (TextView) findViewById(R.id.moment_item_name);
        mPraiseNum = (TextView) findViewById(R.id.moment_item_praise_num);
        mTime = (TextView) findViewById(R.id.moment_item_time);

        mPraise.setOnClickListener(this);
    }

    public void setAvatar(int reference) {
        mAvatar.setImageResource(reference);
    }

    public void setAvatar(String url) {
        ImageLoader.getOne()
                .from(url)
                .into(mAvatar)
                .load();
    }

    public void setImage(int reference) {
        mAvatar.setImageResource(reference);
    }

    public void setImage(String url) {
        ImageLoader.getOne()
                .from(url)
                .into(mAvatar)
                .load();
    }

    public void setPraise(int reference) {
        mAvatar.setImageResource(reference);
    }

    public void setUserName(String name) {
        mUserName.setText(name);
    }

    public void setPublishTime(String tiem) {
        mTime.setText(tiem);
    }

    public void setPraiseNum(int num) {
        mPraiseNum.setText("获得" + num + "次赞");

    }

    public void setMomentId(String momentId) {
        mMomentId = momentId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public void setFriendId(String friendId) {
        mFriendId = friendId;
    }

    public void changePraiseAmount(boolean add) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.moment_item_praise:
                if (!mIsPraise) {
                    mIsPraise = true;
                    mPraises++;
                    mPraise.setImageResource(R.mipmap.praise_fill);
                    setPraiseNum(mPraises);
                    changePraiseAmount(mIsPraise);
                    break;
                }
                mIsPraise = false;
                mPraises--;
                mPraise.setImageResource(R.mipmap.praise);
                setPraiseNum(mPraises);
                changePraiseAmount(mIsPraise);
                break;
            default:break;
        }
    }
}
