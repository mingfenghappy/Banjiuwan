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
        initSetting();
        initWebView();
    }

    private void initSetting() {
        webView = (WebView) findViewById(R.id.webView_detail);
        url = getIntent().getStringExtra(KEY_URL);
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

    private void initWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(chromeClient);
        webView.setWebViewClient(viewClient);
        webView.loadUrl(url);
    }

    public static void start(Context context, String url) {
        Intent starter = new Intent(context, DetailActivity.class);
        starter.putExtra(KEY_URL, url);
        context.startActivity(starter);
    }

}
