package com.ins.feast.web;

import android.app.Activity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.ins.middle.common.AppData;

/**
 * author 边凌
 * date 2017/3/2 11:48
 * desc ${TODO}
 */

public class CommonWebJSInterface extends BaseFeastJSInterface {

    private Activity activity;

    public CommonWebJSInterface(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @JavascriptInterface
    public void saveToken(String token) {
//        Log.e("liao token",token);
//        Toast.makeText(activity, token, Toast.LENGTH_SHORT).show();
        AppData.App.saveToken(token);
    }

    @JavascriptInterface
    public void removeToken() {
        AppData.App.removeToken();
    }
}
