package com.ins.middle.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ins.middle.R;
import com.ins.middle.ui.activity.BaseBackActivity;


public class WebActivity extends BaseBackActivity {

    private WebView webView;
    private ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String url = getIntent().getStringExtra("url");
        String title = getIntent().getStringExtra("title");
        //getSupportActionBar().setTitle(title);
        TextView text_toolbar = (TextView)findViewById(R.id.toolbar_title);
        if (text_toolbar!=null) text_toolbar.setText(title);

        webView = (WebView) findViewById(R.id.webview);
        bar = (ProgressBar) findViewById(R.id.progress);

        final WebSettings setting = webView.getSettings();
        setting.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                bar.setProgress(newProgress);
                if (newProgress == 100) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            bar.setVisibility(View.GONE);
                        }
                    },200);
                } else {
                    bar.setVisibility(View.VISIBLE);
                }
                super.onProgressChanged(view, newProgress);
            }

        });
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }
        });

        webView.loadUrl(url);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
