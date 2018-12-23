package com.cijianyouqing.traveler.fragment;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
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
import com.cijianyouqing.traveler.activity.MapSearchActivity;
import com.cijianyouqing.traveler.view.MapRightView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiangpengfei on 2018/8/29.
 */
public class MapFragment extends Fragment implements DrawerLayout.DrawerListener, View.OnClickListener, SensorEventListener {
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
    @BindView(R.id.iv_person)
    ImageView iv_person;
    @BindView(R.id.tv_search)
    TextView tv_search;
    @BindView(R.id.iv_voice)
    ImageView iv_voice;


    private BaiduMap baiduMap;
    private Context mContext;
    private UiSettings mUiSettings;

    private LocationClient mLocClient;
    public MyLocationListener myListener = new MyLocationListener();
    private BitmapDescriptor mCurrentMarker;
    private SensorManager mSensorManager;
    private double lastX = 0.0;
    private int mCurrentDirection = 0;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private float mCurrentAccracy;

    boolean isFirstLoc = true; // 是否首次定位
    private MyLocationData locData;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);

        init();
        addListener();

        return view;
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
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE); // 获取传感器管理服务
        // 开启定位图层
        baiduMap.setMyLocationEnabled(true);
        mLocClient = new LocationClient(mContext);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType("bd09ll");
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
        mLocClient.setLocOption(option);
        mLocClient.start();

        mapRightView.setMapView(mapView);
    }

    private void addListener() {
        drawerLayout.addDrawerListener(this);
        iv_overlay.setOnClickListener(this);
        iv_location.setOnClickListener(this);
        tv_search.setOnClickListener(this);
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
                location();
                break;
            case R.id.tv_search:
                startActivity(new Intent(mContext, MapSearchActivity.class));
                break;
        }
    }

    @Override
    public void onResume() {
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapView.onResume();
        super.onResume();

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
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        //取消注册传感器监听
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
        mapView = null;
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        baiduMap.setMyLocationEnabled(false);
        super.onDestroy();
    }


    public DrawerLayout getMapDrawerLayout() {
        return drawerLayout;
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
        if (Math.abs(mCurrentDirection - lastX) > 10) {
            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    .direction(mCurrentDirection)   // 此处设置开发者获取到的方向信息，顺时针0-360
                    .latitude(mCurrentLat)
                    .longitude(mCurrentLon)
                    .build();
            baiduMap.setMyLocationData(locData);
            lastX = mCurrentDirection;
        }

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

    /**
     * 定位到当前点
     */
    private void location() {
        LatLng ll = new LatLng(mCurrentLat, mCurrentLon);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(18.0f);
        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            // map view 销毁后不在处理新接收的位置
            if (bdLocation == null || mapView == null) {
                return;
            }
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
