package com.ins.baidumapsdk;

import android.Manifest;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;

import com.baidu.location.Address;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;

/**
 * Created by Administrator on 2016/11/8.
 */

public class Locationer {

    private Context context;

    public BDLocation getBdLocation() {
        return new BDLocation(bdLocation);
    }

    private BDLocation bdLocation;

//    private MapView mapView;
//    private BaiduMap baiduMap;

    private MyLocationListenner locationListenner = new MyLocationListenner();
    public LocationClient locationClient;
    public boolean isFirstLoc = true; // 是否首次定位

    public Locationer(Context context) {
//        this.mapView = mapView;
//        this.baiduMap = this.mapView.getMap();
        this.context = context;
        setLocationConfig();
    }

    /**
     * 返回LocOption以用作设置
     *
     * @return {@link LocationClientOption}
     */
    public
    @Nullable
    LocationClientOption getLocOption() {
        return locationClient.getLocOption();
    }

    private void setLocationConfig() {
        // 定位初始化
        locationClient = new LocationClient(context);
        locationClient.registerLocationListener(locationListenner);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(5000);  //定位间隔时间，必须>1000，为0标示只定位一次

        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
//        option.setOpenGps(true);//可选，默认false,设置是否使用gps
//        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
//        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
//        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
//        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
//        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
//        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        locationClient.setLocOption(option);
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            Locationer.this.bdLocation = location;
            // map view 销毁后不在处理新接收的位置
            if (location == null) {
                return;
            }
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            if (callback != null) {
                callback.onLocation(latLng, location.getCity(), location.getAddrStr(), isFirstLoc);
            }
            isFirstLoc = false;
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    public void startlocation() {
        // 开启定位图层
        locationClient.start();
    }

    public void stopLocation() {
        locationClient.stop();
    }

    private LocationCallback callback;

    public void setCallback(LocationCallback callback) {
        this.callback = callback;
    }

    public interface LocationCallback {
        void onLocation(LatLng latLng, String city, String address, boolean isFirst);
    }

    public String getAddrStr() {
        if (bdLocation != null) {
            Address address = bdLocation.getAddress();
            if (address != null) {
                return address.district + address.street;
            } else {
                return "定位失败";
            }
        } else {
            return "定位失败";
        }
    }
}
