package com.ins.feast.web;

import android.content.Context;
import android.webkit.JavascriptInterface;

/**
 * author 边凌
 * date 2017/2/16 10:56
 * desc ${与js交互的类}
 */

public class HomeJSInterface extends BaseJSInterface {
    private HomeWebView homeWebView;
    private Context context;

    public HomeJSInterface(Context context, HomeWebView homeWebView) {
        this.context = context;
        this.homeWebView = homeWebView;
    }

    @JavascriptInterface
    public void test(){

    }

}
