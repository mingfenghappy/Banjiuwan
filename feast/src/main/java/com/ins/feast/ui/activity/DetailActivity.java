package com.ins.feast.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ins.feast.R;

public class DetailActivity extends BaseAppCompatActivity {

    private WebView webView;
    private final static String KEY_URL = "URL";
    private String url;

    public DetailActivity() {
    }

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
            webView=null;
        }
    }

    @Override
    public void onBackPressed() {
        if (webView != null&&webView.canGoBack()) {
            webView.goBack();
        }else {
            super.onBackPressed();
        }
    }

    public static void start(Context context, String url) {
        Intent starter = new Intent(context, DetailActivity.class);
        starter.putExtra(KEY_URL, url);
        context.startActivity(starter);
    }

}
