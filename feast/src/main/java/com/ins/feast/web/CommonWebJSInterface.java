package com.ins.feast.web;

import android.app.Activity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.ins.feast.ui.activity.ChooseLocationActivity;
import com.ins.feast.ui.activity.CommonWebActivity;
import com.ins.feast.ui.activity.SearchAddressActivity;
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
        AppData.App.saveToken(token);
    }

    @JavascriptInterface
    public void removeToken() {
        AppData.App.removeToken();
    }

    @JavascriptInterface
    public void openAddressPage() {
        //客户变更了需求：不需要打开地址选择页面，直接进入地图选择页
//        ChooseLocationActivity.start(activity);
        SearchAddressActivity.start(activity);
    }
}
