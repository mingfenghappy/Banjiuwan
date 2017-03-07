package com.ins.chef.web;

import android.webkit.JavascriptInterface;

import com.ins.middle.common.AppData;

/**
 * author 边凌
 * date 2017/2/28 11:08
 * desc ${TODO}
 */

public class BaseJSInterface {
    @JavascriptInterface
    public int getDeviceType() {
        return 0;
    }

    //返回token，值为JpushId
    @JavascriptInterface
    public String getDeviceToken() {
        return AppData.App.getJpushId();
    }
}
