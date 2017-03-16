package com.ins.feast.entity;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.ins.feast.utils.MapHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/27.
 */

public class Position implements Serializable {

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

    //地理位置详细描述
    private String address;

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

    public Position(PoiInfo poi) {
        key = poi.name;
        city = poi.city;
        district = poi.address;
        latLng = poi.location;
        address = poi.address;
    }

    public Position(Address address) {
        this.address = address.getAddress();
        this.latLng = MapHelper.str2LatLng(address.getLngLat());
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
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

    /**
     * 将一个PoiInfo的List转为Position的List
     */
    public static List<Position> asPositionList(List<PoiInfo> poiInfos) {
        List<Position> positions = new ArrayList<>();
        for (PoiInfo poiInfo : poiInfos) {
            Position position = new Position(poiInfo);
            positions.add(position);
        }
        return positions;
    }
}
