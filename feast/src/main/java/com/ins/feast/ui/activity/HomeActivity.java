package com.ins.feast.ui.activity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.ins.baidumapsdk.Locationer;
import com.ins.feast.R;
import com.ins.feast.common.AppData;
import com.ins.feast.jsbridge.JSInterface;
import com.shelwee.update.UpdateHelper;
import com.sobey.common.utils.PermissionsUtil;

public class HomeActivity extends BaseAppCompatActivity implements Locationer.LocationCallback {

    private Locationer locationer;
    private final static String LOG_TAG = "HomeActivity";
    private final static String JS_BRIDGE_NAME = "native";
    private WebView webView;
    private TextView title_location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initBase();
        initView();
    }

    private void initBase() {
        //初始化定位
        locationer = new Locationer(this);
        locationer.setCallback(this);
        //双击返回键退出
        setNeedDoubleClickExit(true);
        //检查并申请权限
        PermissionsUtil.checkAndRequestPermissions(this);
        //检查更新
        new UpdateHelper.Builder(this).checkUrl(AppData.Url.version_passenger).isHintNewVersion(false).build().check();
    }

    private void initView() {
        title_location = (TextView) findViewById(R.id.title_location);
        initWebView();
    }

    private void initWebView() {
        webView = (WebView) findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.loadUrl(AppData.Url.app_homepage);
        webView.addJavascriptInterface(new JSInterface(this), JS_BRIDGE_NAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationer.stopLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationer.startlocation();
    }

    @Override
    public void onLocation(LatLng latLng, String city, String district, boolean isFirst) {
        title_location.setText(locationer.getAddrStr());
    }
}
