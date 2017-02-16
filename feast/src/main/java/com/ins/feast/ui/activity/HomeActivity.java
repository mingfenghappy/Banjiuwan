package com.ins.feast.ui.activity;

import android.os.Build;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.ins.baidumapsdk.Locationer;
import com.ins.feast.R;
import com.ins.feast.common.AppData;
import com.ins.feast.jsbridge.JSInterface;
import com.shelwee.update.UpdateHelper;
import com.sobey.common.utils.L;
import com.sobey.common.utils.PermissionsUtil;

public class HomeActivity extends BaseAppCompatActivity implements Locationer.LocationCallback {

    private Locationer locationer;
    private final static String LOG_TAG = "HomeActivity";
    private final static String JS_BRIDGE_NAME = "native";
    private WebView webView;
    private TextView title_location;
    private TextView title_center;

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
        title_center = (TextView) findViewById(R.id.text_toolbar_title);
        title_location = (TextView) findViewById(R.id.title_location);
        initWebView();
    }

    /**
     * 自定义WebViewClient
     */
    private WebViewClient mClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (!view.getSettings().getLoadsImagesAutomatically()) {
                view.getSettings().setLoadsImagesAutomatically(true);
            }
        }
    };
    /**
     * 自定义WebChromeClient
     */
    private WebChromeClient mChromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            L.d(title);
            title_center.setText(title);
        }
    };

    private void initWebView() {
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(mClient);
        webView.setWebChromeClient(mChromeClient);
        WebSettings settings = webView.getSettings();

        settings.setLoadsImagesAutomatically(Build.VERSION.SDK_INT >= 19);
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

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
