package com.ins.feast.ui.activity;

import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.ins.baidumapsdk.Locationer;
import com.ins.feast.R;
import com.ins.feast.common.AppData;
import com.ins.feast.entity.NetStateChangedEvent;
import com.ins.feast.entity.Position;
import com.ins.feast.receiver.NetStateReceiver;
import com.ins.feast.web.HomeActivityWebChromeClient;
import com.ins.feast.web.HomeActivityWebViewClient;
import com.ins.feast.web.HomeJSInterface;
import com.ins.feast.web.HomeWebView;
import com.shelwee.update.UpdateHelper;
import com.sobey.common.utils.L;
import com.sobey.common.utils.PermissionsUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class HomeActivity extends BaseMapActivity implements Locationer.LocationCallback {

    private final static String JS_BRIDGE_NAME = "native";
    private HomeWebView webView;
    //标题栏定位Tv
    private TextView title_location;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        EventBus.getDefault().register(this);
        setHandleLocationLifeCycleBySubclass(true);

        initBase();
        initView();
        initWebViewSetting();
    }

    private void initBase() {
        //双击返回键退出
        setNeedDoubleClickExit(true);
        //检查并申请权限
        PermissionsUtil.checkAndRequestPermissions(this);
        //检查更新
        new UpdateHelper.Builder(this).checkUrl(AppData.Url.version_passenger).isHintNewVersion(false).build().check();
        locationer.startlocation();
        NetStateReceiver.registerAboveSDK21(this);
    }

    private void initView() {
        title_location = (TextView) findViewById(R.id.title_location);
        webView = (HomeWebView) findViewById(R.id.webView);
    }


    private HomeActivityWebChromeClient webChromeClient;
    private HomeActivityWebViewClient webViewClient;
    private HomeJSInterface homeJsInterface;
    /**
     * WebView配置
     */
    private void initWebViewSetting() {
        webViewClient=new HomeActivityWebViewClient(this);
        webChromeClient=new HomeActivityWebChromeClient(this);

        webView.setWebViewClient(webViewClient);
        webView.setWebChromeClient(webChromeClient);

        WebSettings settings = webView.getSettings();
        ///
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        ///
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAppCachePath(webView.getContext().getCacheDir().getAbsolutePath());
        settings.setAllowFileAccess(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowFileAccessFromFileURLs(true);
        }

        settings.setJavaScriptEnabled(true);
        homeJsInterface =new HomeJSInterface(this,webView);
        webView.addJavascriptInterface(homeJsInterface, JS_BRIDGE_NAME);


        webView.loadUrl(AppData.Url.app_homepage);

    }

    @Override
    public void onLocation(LatLng latLng, String city, String district, boolean isFirst) {
        locationer.stopLocation();
        title_location.setText(locationer.getAddrStr());
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null) {
            webView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) {
            webView.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.clearHistory();
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 接受地理位置选择的结果
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceivePostition(Position position) {
        String key = position.getKey();
        title_location.setText(key);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveNetStateChanged(NetStateChangedEvent event) {
        NetworkInfo activeInfo = event.getActiveInfo();
        if (activeInfo != null && webView != null) {
            if (activeInfo.isAvailable()) {
                webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
                locationer.startlocation();
                webView.reload();
                L.d("NetChanged:available");
            } else {
                webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                L.d("NetChanged:not available");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        homeJsInterface.onActivityResult(requestCode,resultCode,data);
    }

}
