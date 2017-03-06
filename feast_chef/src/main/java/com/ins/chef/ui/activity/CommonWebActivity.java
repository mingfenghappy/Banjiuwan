package com.ins.chef.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.ins.chef.R;
import com.sobey.common.base.BaseAppCompatActivity;
import com.sobey.common.utils.L;

public class CommonWebActivity extends BaseAppCompatActivity {

    private String url;
    private WebView webView;
    private TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_web);
        url = getIntent().getStringExtra(KEY_URL);
        initView();
        initWebView();
    }

    private void initWebView() {
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                toolbar_title.setText(title);
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                CommonWebActivity.start(CommonWebActivity.this, url);
                return true;
            }
        });
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.webView);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        findViewById(R.id.toolbar_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private final static String KEY_URL = "url";

    public static void start(Context context, String url) {
        Intent starter = new Intent(context, CommonWebActivity.class);
        starter.putExtra(KEY_URL, url);
        L.d("startCommonWebActivity:"+url);
        context.startActivity(starter);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
