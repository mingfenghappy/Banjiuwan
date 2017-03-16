package com.ins.feast.entity;

import com.baidu.mapapi.model.LatLng;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/3/15.
 */

public class Area implements Serializable {
    private int id;
    private String mapList;
    private String reMarket;

    //本地字段
    private List<LatLng> latLngs;

    public List<LatLng> getLatLngs() {
        return latLngs;
    }

    public void setLatLngs(List<LatLng> latLngs) {
        this.latLngs = latLngs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMapList() {
        return mapList;
    }

    public void setMapList(String mapList) {
        this.mapList = mapList;
    }

    public String getReMarket() {
        return reMarket;
    }

    public void setReMarket(String reMarket) {
        this.reMarket = reMarket;
    }
}
