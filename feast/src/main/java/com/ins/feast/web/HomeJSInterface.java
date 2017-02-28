package com.ins.feast.web;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.webkit.JavascriptInterface;

import com.ins.feast.ui.dialog.DialogPopupPhoto;
import com.sobey.common.helper.CropHelperSys;

/**
 * author 边凌
 * date 2017/2/16 10:56
 * desc ${与js交互的类}
 */

public class HomeJSInterface {
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
