package com.ins.feast.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ins.feast.R;
import com.ins.feast.entity.NetStateChangedEvent;
import com.ins.feast.receiver.NetStateReceiver;
import com.sobey.common.utils.L;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
        NetStateReceiver.registerAboveSDK21(this);
        EventBus.getDefault().register(this);
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.webView_detail);
    }

    private WebChromeClient chromeClient = new WebChromeClient() {

    };
    private WebViewClient viewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    };

    private void initWebViewSetting() {
        webView.setWebChromeClient(chromeClient);
        webView.setWebViewClient(viewClient);

        WebSettings settings = webView.getSettings();
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
        EventBus.getDefault().unregister(this);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveNetStateChanged(NetStateChangedEvent event) {
        NetworkInfo activeInfo = event.getActiveInfo();
        if (activeInfo != null && webView != null) {
            if (activeInfo.isAvailable()) {
                webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
                webView.reload();
                L.d("NetChanged:available");
            } else {
                webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                L.d("NetChanged:not available");
            }
        }
    }
}
