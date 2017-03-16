package com.ins.feast.entity;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/15.
 */

public class Address implements Serializable{

    @SerializedName("id")
    private int id;

    @SerializedName("contact")
    private String name;

    @SerializedName("contactPhone")
    private String phone;

    @SerializedName("address")
    private String address;

    @SerializedName("detailAddress")
    private String detailAddress;

    @SerializedName("lngLat")
    private String lngLat;

    @SerializedName("isDefault")
    private int isDefault;

    public String getLngLat() {
        return lngLat;
    }

    public void setLngLat(String lngLat) {
        this.lngLat = lngLat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }
}
