package com.cijianyouqing.traveler.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.cijianyouqing.traveler.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiangpengfei on 2018/8/30.
 */
public class MapRightView extends LinearLayout {
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;

    private Context mContext;
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    public MapRightView(Context context) {
        this(context, null);
    }

    public MapRightView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MapRightView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.map_right_view, this, true);
        ButterKnife.bind(this, this);

        addListener();

    }

    private void addListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.rb_satellite) {
                    setSatellite();
                } else if (checkedId == R.id.rb_2d) {
                    setPlain();
                } else if (checkedId == R.id.rb_3d) {
                    set3DPlain();
                }
            }
        });
    }

    public void setMapView(MapView mapView) {
        mMapView = mapView;
        if (mMapView != null) {
            mBaiduMap = mMapView.getMap();
        }
    }

    public void setSatellite() {
        if (mBaiduMap != null) {
            mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        }
    }

    public void setPlain() {
        if (mBaiduMap != null) {
            mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
            MapStatus ms = new MapStatus.Builder(mBaiduMap.getMapStatus()).overlook(0).rotate(0).build();
            MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(ms);
            mBaiduMap.animateMapStatus(u);
        }
    }

    public void set3DPlain() {
        if (mBaiduMap != null) {
            mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
            MapStatus ms = new MapStatus.Builder(mBaiduMap.getMapStatus()).zoom(mBaiduMap.getMaxZoomLevel() - 2).overlook(-45).rotate(0).build();
            MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(ms);
            mBaiduMap.animateMapStatus(u);
        }
    }

//    private void locationCurrent(){
//        if(LocationService.getBDLocation() != null){
//            BDLocation bdLocation = LocationService.getBDLocation();
//            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(new LatLng(bdLocation.getLatitude(),bdLocation.getLongitude()));
//            mBaiduMap.animateMapStatus(mapStatusUpdate);
//        }
//    }
}
