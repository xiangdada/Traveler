package com.cijianyouqing.traveler.application;

import android.app.Application;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;

import org.litepal.LitePal;

/**
 * Created by xiangpengfei on 2018/8/29.
 */
public class MyApplication extends Application {
    private static  MyApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);

        LitePal.initialize(this);
    }

    public static MyApplication getInstance(){
        return instance;
    }

}
