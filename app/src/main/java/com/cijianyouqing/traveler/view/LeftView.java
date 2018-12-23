package com.cijianyouqing.traveler.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cijianyouqing.traveler.R;
import com.cijianyouqing.traveler.activity.LoginActivity;
import com.cijianyouqing.traveler.activity.PersonCenterActivity;
import com.cijianyouqing.traveler.activity.SettingActivity;
import com.cijianyouqing.traveler.util.TextUtil;
import com.cijianyouqing.traveler.util.UserUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiangpengfei on 2018/8/29.
 */
public class LeftView extends LinearLayout implements View.OnClickListener {
    @BindView(R.id.ll_person)
    LinearLayout ll_person;
    @BindView(R.id.iv_pic)
    ImageView iv_pic;
    @BindView(R.id.tv_person)
    TextView tv_person;
    @BindView(R.id.tv_vip)
    TextView tv_vip;
    @BindView(R.id.tv_setting)
    TextView tv_setting;

    private Context mContext;

    public LeftView(Context context) {
        this(context, null);
    }

    public LeftView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LeftView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        LayoutInflater.from(context).inflate(R.layout.leftview, this, true);
        ButterKnife.bind(this);

        addListener();
        initUserInfo();
    }

    private void addListener() {
        ll_person.setOnClickListener(this);
        tv_setting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_person:
                if (UserUtil.isLoginin()) {
                    ((Activity) mContext).startActivity(new Intent(mContext, PersonCenterActivity.class));
                } else {
                    ((Activity) mContext).startActivity(new Intent(mContext, LoginActivity.class));
                }
                break;
            case R.id.tv_setting:
                ((Activity) mContext).startActivity(new Intent(mContext, SettingActivity.class));
                break;
        }

    }

    private void initUserInfo() {
        if (TextUtil.empty(UserUtil.getNickname())) {
            if (UserUtil.isLoginin()) {
                tv_person.setText("未知");
            } else {
                tv_person.setText("登录/注册");
            }
        } else {
            tv_person.setText(UserUtil.getNickname());
        }
        if (TextUtil.empty(UserUtil.getVipLevel())) {
            tv_vip.setText("VIP0");
        } else {
            tv_vip.setText("VIP" + UserUtil.getVipLevel());
        }

    }

    public void refreshUserInfo() {
        initUserInfo();
    }
}
