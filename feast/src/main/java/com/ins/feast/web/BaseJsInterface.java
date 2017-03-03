package com.ins.feast.web;

import android.webkit.JavascriptInterface;

import com.ins.feast.common.AppData;

/**
 * author 边凌
 * date 2017/2/28 10:23
 * desc ${Js桥基类}
 */

class BaseJSInterface {
    //返回设备id，0代表android
    @JavascriptInterface
    public int getDeviceType() {
        return AppData.Config.DEVICE_TYPE;
    }

    //返回token，值为JpushId
    @JavascriptInterface
    public String getDeviceToken() {
        return AppData.App.getJpushId();
    }
}
