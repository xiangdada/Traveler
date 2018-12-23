package com.cijianyouqing.traveler.bean;

import com.baidu.mapapi.model.LatLng;
import com.cijianyouqing.traveler.util.TextUtil;

import java.io.Serializable;

/**
 * Created by xiangpengfei on 2018/9/26.
 */
public class MapSearchData implements Serializable {
    private boolean isSuggestion;
    private String address;
    private String city;
    private String district;
    private String key;
    private LatLng latLng;
    private String uid;
    private String area;
    private String name;
    private int detail;
    private boolean hasCaterDetail;
    private boolean isPano;
    private String province;
    private String street_id;

    public boolean isSuggestion() {
        return isSuggestion;
    }

    public void setSuggestion(boolean suggestion) {
        isSuggestion = suggestion;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDetail() {
        return detail;
    }

    public void setDetail(int detail) {
        this.detail = detail;
    }

    public boolean isHasCaterDetail() {
        return hasCaterDetail;
    }

    public void setHasCaterDetail(boolean hasCaterDetail) {
        this.hasCaterDetail = hasCaterDetail;
    }

    public boolean isPano() {
        return isPano;
    }

    public void setPano(boolean pano) {
        isPano = pano;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getStreet_id() {
        return street_id;
    }

    public void setStreet_id(String street_id) {
        this.street_id = street_id;
    }

    public String getTitle() {
        if (isSuggestion) {
            return key;
        } else {
            return name;
        }
    }

    public String getAddressDiscription() {
        String discription = "";
        if (isSuggestion) {
            addDiscription(discription, city);
            addDiscription(discription, district);
            addDiscription(discription, address);
        } else {
            addDiscription(discription,city);
            addDiscription(discription,area);
            addDiscription(discription,address);
        }
        if(discription.startsWith("-")){
            discription  = discription.substring(1,discription.length());
        }
        return discription;
    }

    private void addDiscription(String discription, String content) {
        if (!TextUtil.empty(content)) {
            discription = discription + "-" + content;
        }
    }
}
