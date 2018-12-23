package com.cijianyouqing.traveler.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.cijianyouqing.traveler.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiangpengfei on 2018/12/5.
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.tv_share)
    TextView tv_share;
    @BindView(R.id.tv_logout)
    TextView tv_logout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        addListener();
    }

    private void addListener() {
        tv_share.setOnClickListener(this);
        tv_logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_share:
                share();
                break;
            case R.id.tv_logout:
                logout();
                break;
        }
    }


}
