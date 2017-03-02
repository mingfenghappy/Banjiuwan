package com.ins.feast.ui.activity;

import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.ins.feast.R;
import com.ins.feast.common.AppData;
import com.ins.feast.entity.NetStateChangedEvent;
import com.ins.feast.entity.Position;
import com.ins.feast.receiver.NetStateReceiver;
import com.ins.feast.ui.helper.HomeTitleHelper;
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


public class HomeActivity extends BaseMapActivity implements
        RadioGroup.OnCheckedChangeListener {

    private HomeWebView webView;
    //标题栏定位Tv
    private TextView title_location;

    private RadioGroup tabRg;

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
        startLocation();
        NetStateReceiver.registerAboveSDK21(this);
    }

    private HomeTitleHelper homeTitleHelper;

    private void initView() {
        title_location = (TextView) findViewById(R.id.title_location);
        webView = (HomeWebView) findViewById(R.id.webView);
        tabRg = (RadioGroup) findViewById(R.id.radioGroup);
        tabRg.setOnCheckedChangeListener(this);
        homeTitleHelper=new HomeTitleHelper(this);
    }
    private HomeActivityWebChromeClient webChromeClient;
    private HomeActivityWebViewClient webViewClient;
    private HomeJSInterface homeJsInterface;

    /**
     * WebView配置
     */
    private void initWebViewSetting() {
        webViewClient = new HomeActivityWebViewClient(this);
        webChromeClient = new HomeActivityWebChromeClient(this);

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
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowFileAccessFromFileURLs(true);
        }

        settings.setJavaScriptEnabled(true);
        homeJsInterface = new HomeJSInterface(this, webView);
        webView.addJavascriptInterface(homeJsInterface, JS_BRIDGE_NAME);


        webView.loadUrl(AppData.Url.app_home);

    }

    @Override
    public void onLocation(LatLng latLng, String city, String district, boolean isFirst) {
        stopLocation();
        title_location.setText(getAddStr());
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
        if (webView != null && webViewClient != null) {
            webView.clearHistory();
            webView.removeAllViews();
            webView.destroy();
            webViewClient.destroy();
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
        if (activeInfo != null) {
            if (activeInfo.isAvailable()) {
                startLocation();
                L.d("NetChanged:available");
            } else {
                L.d("NetChanged:not available");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        homeJsInterface.onActivityResult(requestCode, resultCode, data);
        webChromeClient.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        String url = null;
        switch (checkedId) {
            case R.id.rb_home:
                url = AppData.Url.app_home;
                homeTitleHelper.setTitleText("办酒碗");
                break;
            case R.id.rb_cart:
                url = AppData.Url.app_cart;
                homeTitleHelper.setTitleText("购物车");
                break;
            case R.id.rb_find:
                url = AppData.Url.app_find;
                homeTitleHelper.setTitleText("发现");
                break;
            case R.id.rb_customerService:
                url = AppData.Url.app_customer_service;
                homeTitleHelper.setTitleText("");
                break;
            case R.id.rb_mine:
                url = AppData.Url.app_mine;
                homeTitleHelper.setTitleText("我的");
                break;
        }
        if (!TextUtils.isEmpty(url) && webView != null) {
            homeTitleHelper.handleTitleWithUrl(url);
            webView.loadUrl(url);
        }
    }
}
