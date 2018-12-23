package com.cijianyouqing.traveler.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.cijianyouqing.traveler.R;

import butterknife.ButterKnife;

/**
 * Created by xiangpengfei on 2018/11/9.
 */
public class PersonCenterActivity extends BaseActivity implements View.OnClickListener {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_center);
        ButterKnife.bind(this);

        addListener();
    }

    private void addListener() {
        
    }

    @Override
    public void onClick(View v) {
        
    }
}
