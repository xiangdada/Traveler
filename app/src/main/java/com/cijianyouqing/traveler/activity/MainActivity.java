package com.cijianyouqing.traveler.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.LinearLayout;

import com.cijianyouqing.traveler.R;
import com.cijianyouqing.traveler.eventbus.LoginEvent;
import com.cijianyouqing.traveler.fragment.MainFragment;
import com.cijianyouqing.traveler.view.LeftView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements DrawerLayout.DrawerListener {
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.centerContent)
    LinearLayout centerContent;
    @BindView(R.id.leftview)
    LeftView leftview;

    private MainFragment mainFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

        init();
        addListener();
    }

    private void init() {
        mainFragment = new MainFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.centerContent, mainFragment);
        fragmentTransaction.commit();
    }

    private void addListener() {
        drawerLayout.addDrawerListener(this);
    }

    @Override
    public void onDrawerSlide(@NonNull View view, float v) {
    }

    @Override
    public void onDrawerOpened(@NonNull View view) {

    }

    @Override
    public void onDrawerClosed(@NonNull View view) {

    }

    @Override
    public void onDrawerStateChanged(int i) {
        if (mainFragment.getMapDrawerLayout() != null) {
            mainFragment.getMapDrawerLayout().closeDrawers();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginEvent(LoginEvent event) {
        if (event != null) {
            if (event.isLoginin()) {    // 登录成功之后刷新视图
                if(leftview!=null){
                    leftview.refreshUserInfo();
                }
            }else{  // 退出登录成功之后刷新师徒
                if(leftview!=null){
                    leftview.refreshUserInfo();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
