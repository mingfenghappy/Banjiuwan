package com.ins.feast.entity;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.sug.SuggestionResult;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/27.
 */

public class Position implements Serializable{

    //城市
    private String city;
    //地址
    private String key;
    //区级名称
    private String district;
    //坐标
    private LatLng latLng;
    //是否在区域内
    private boolean isIn;

    public Position(LatLng latLng, String key, String city) {
        this.latLng = latLng;
        this.key = key;
        this.city = city;
    }

    public Position() {
    }

    public Position(String district) {
        this.district = district;
    }

    public Position(SuggestionResult.SuggestionInfo suggest) {
        key = suggest.key;
        city = suggest.city;
        district = suggest.district;
        latLng = suggest.pt;
    }
    public Position(PoiInfo poi) {
        key = poi.name;
        city = poi.city;
        district = poi.address;
        latLng = poi.location;
    }

    public boolean isIn() {
        return isIn;
    }

    public void setIn(boolean in) {
        isIn = in;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
