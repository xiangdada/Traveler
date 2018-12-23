package com.cijianyouqing.traveler.test;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.cijianyouqing.traveler.R;
import com.cijianyouqing.traveler.activity.BaseActivity;
import com.cijianyouqing.traveler.view.MapRightView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiangpengfei on 2018/9/4.
 */
public class TestActivity extends BaseActivity implements DrawerLayout.DrawerListener, View.OnClickListener, SensorEventListener {
    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.ll_tools_rightTop)
    LinearLayout ll_tools_rightTop;
    @BindView(R.id.iv_overlay)
    ImageView iv_overlay;
    @BindView(R.id.ll_tools_bottomLeft)
    LinearLayout ll_tools_bottomLeft;
    @BindView(R.id.iv_location)
    ImageView iv_location;
    @BindView(R.id.mapRightView)
    MapRightView mapRightView;

    private BaiduMap baiduMap;
    private UiSettings mUiSettings;


    private LocationClient mLocClient;
    //    public MyLocationListener myListener = new MyLocationListener();
    public MyLocationListener1 myListener1 = new MyLocationListener1();
    private BitmapDescriptor mCurrentMarker;
    private SensorManager mSensorManager;
    private Double lastX = 0.0;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;

    boolean isFirstLoc = true; // 是否首次定位
    private MyLocationData locData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_map);
        ButterKnife.bind(this);

        init();
        addListener();
    }

    private void init() {
        // 关闭手势滑动
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        baiduMap = mapView.getMap();
        // 开启交通图
        baiduMap.setTrafficEnabled(true);

        mUiSettings = baiduMap.getUiSettings();
        // 不显示指南针
        mUiSettings.setCompassEnabled(false);

        setLocationMode();

        mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.arrow);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE); // 获取传感器管理服务
        // 开启定位图层
        baiduMap.setMyLocationEnabled(true);
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener1);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType("bd0911");
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();

        mapRightView.setMapView(mapView);
    }

    private void addListener() {
        drawerLayout.addDrawerListener(this);
        iv_overlay.setOnClickListener(this);
        iv_location.setOnClickListener(this);
    }

    /**
     * 设置定位模式，先设置为跟随模式再设置为普通模式的话，
     * 可以实现显示方向，但滑动屏幕后不会自动回到定位点
     */
    private void setLocationMode() {
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.overlook(0);    // 俯视角
        baiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.FOLLOWING, true, mCurrentMarker));
        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        baiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.NORMAL, true, mCurrentMarker));
        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    @Override
    public void onDrawerSlide(@NonNull View view, float v) {

    }

    @Override
    public void onDrawerOpened(@NonNull View view) {
        // 打开手势滑动
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    @Override
    public void onDrawerClosed(@NonNull View view) {
        // 关闭手势滑动
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public void onDrawerStateChanged(int i) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_overlay:
                drawerLayout.openDrawer(Gravity.RIGHT);
                break;
            case R.id.iv_location:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapView.onResume();

        //为系统的方向传感器注册监听器，过时
//        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
//                SensorManager.SENSOR_DELAY_UI);
        // 磁力传感器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_UI);
        // 加速度传感器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapView.onPause();
    }

    @Override
    public void onStop() {
        //取消注册传感器监听
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
        mapView = null;
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        baiduMap.setMyLocationEnabled(false);

    }

    float[] accelerometerValues = new float[3];
    float[] magneticValues = new float[3];

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                accelerometerValues = sensorEvent.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                magneticValues = sensorEvent.values.clone();
                break;
            default:
                break;
        }

        float[] r = new float[9];
        float[] values = new float[3];
        // 第一个是方向角度参数，第二个参数是倾斜角度参数
        SensorManager.getRotationMatrix(r, null, accelerometerValues, magneticValues);
        SensorManager.getOrientation(r, values);
        mCurrentDirection = (int) Math.toDegrees(values[0]);
        locData = new MyLocationData.Builder()
                .accuracy(mCurrentAccracy)
                .direction(mCurrentDirection)   // 此处设置开发者获取到的方向信息，顺时针0-360
                .latitude(mCurrentLat)
                .longitude(mCurrentLon)
                .build();
        baiduMap.setMyLocationData(locData);

        // 过时
//        double x = sensorEvent.values[SensorManager.DATA_X];
//        if (Math.abs(x - lastX) > 1.0) {
//            mCurrentDirection = (int) x;
//            locData = new MyLocationData.Builder()
//                    .accuracy(mCurrentAccracy)
//                    .direction(mCurrentDirection)   // 此处设置开发者获取到的方向信息，顺时针0-360
//                    .latitude(mCurrentLat)
//                    .longitude(mCurrentLon)
//                    .build();
//            baiduMap.setMyLocationData(locData);
//        }
//        lastX = x;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            // map view 销毁后不在处理新接收的位置
            if (bdLocation == null || mapView == null) {
                return;
            }
//            Log.e("测试", "onReceiveLocation");

            mCurrentLat = bdLocation.getLatitude();
            mCurrentLon = bdLocation.getLongitude();
            mCurrentAccracy = bdLocation.getRadius();
            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    .direction(mCurrentDirection)
                    .latitude(mCurrentLat)
                    .longitude(mCurrentLon)
                    .build();
            baiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }

        }
    }

    public class MyLocationListener1 implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            // map view 销毁后不在处理新接收的位置
            if (bdLocation == null || mapView == null) {
                return;
            }
//            Log.e("测试", "onReceiveLocation");

            mCurrentLat = bdLocation.getLatitude();
            mCurrentLon = bdLocation.getLongitude();
            mCurrentAccracy = bdLocation.getRadius();
            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    .direction(mCurrentDirection)
                    .latitude(mCurrentLat)
                    .longitude(mCurrentLon)
                    .build();
            baiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }
    }
}
