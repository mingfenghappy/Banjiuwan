package com.ins.feast.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/3/15.
 */

public class AreaData implements Serializable{

    @SerializedName("mapList")
    private List<Area> Areas;

    @SerializedName("categoryConfig")
    private List<CategoryConfig> configs;

    public List<Area> getAreas() {
        return Areas;
    }

    public void setAreas(List<Area> areas) {
        Areas = areas;
    }

    public List<CategoryConfig> getConfigs() {
        return configs;
    }

    public void setConfigs(List<CategoryConfig> configs) {
        this.configs = configs;
    }
}
