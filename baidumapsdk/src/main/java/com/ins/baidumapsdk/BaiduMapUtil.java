package com.ins.baidumapsdk;

import com.baidu.mapapi.model.LatLng;

/**
 * Created by Administrator on 2017/4/6.
 */

public class BaiduMapUtil {
    public static boolean isLatlngEmpty(LatLng latLng) {
        if (latLng == null) {
            return true;
        } else if (latLng.latitude == 4.9E-324 && latLng.longitude == 4.9E-324) {
            return true;
        } else {
            return false;
        }
    }
}
