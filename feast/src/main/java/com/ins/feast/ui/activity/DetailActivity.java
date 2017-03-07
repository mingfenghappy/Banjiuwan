package com.ins.feast.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.ins.feast.R;
import com.ins.feast.receiver.NetStateReceiver;
import com.ins.feast.web.BaseWebViewClient;
import com.ins.middle.ui.activity.BaseAppCompatActivity;
import com.sobey.common.utils.PermissionsUtil;

public class DetailActivity extends BaseAppCompatActivity {

    private WebView webView;
    private final static String KEY_URL = "URL";
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initBase();
        initView();
        initWebViewSetting();
    }

    private void initBase() {
        url = getIntent().getStringExtra(KEY_URL);
        if (PermissionsUtil.requsetSetting(this, findViewById(R.id.showingroup))) {
            NetStateReceiver.registerAboveSDK21(this);
        }
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.webView_detail);
    }

    private WebChromeClient chromeClient = new WebChromeClient() {

    };

    private void initWebViewSetting() {
        webView.setWebChromeClient(chromeClient);
        webView.setWebViewClient(new BaseWebViewClient(webView) {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        WebSettings settings = webView.getSettings();
        ///
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        ///
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);//开启DOM缓存
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAppCachePath(webView.getContext().getCacheDir().getAbsolutePath());
        webView.loadUrl(url);
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
        super.onDestroy();
        if (webView != null) {
            webView.clearHistory();
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    public static void start(Context context, String url) {
        Intent starter = new Intent(context, DetailActivity.class);
        starter.putExtra(KEY_URL, url);
        context.startActivity(starter);
    }

}
