package com.ins.feast.web;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.webkit.JavascriptInterface;

import com.sobey.common.utils.PermissionsUtil;

/**
 * author 边凌
 * date 2017/2/16 10:56
 * desc ${与js交互的类}
 */

public class HomeJSInterface extends
        BaseJSInterface {
    private HomeWebView homeWebView;
    private Activity context;

    public HomeJSInterface(Activity context, HomeWebView homeWebView) {
        this.context = context;
        this.homeWebView = homeWebView;
    }

    @JavascriptInterface
    public void call(String phoneNumber) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            PermissionsUtil.requestPermissions(context, new String[]{Manifest.permission.CALL_PHONE});
            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        context.startActivity(intent);
    }
}
