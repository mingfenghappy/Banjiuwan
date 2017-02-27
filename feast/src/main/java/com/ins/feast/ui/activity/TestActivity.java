package com.ins.feast.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ins.feast.R;

public class TestActivity extends AppCompatActivity {

    private WebPickerHelper webPickerHelper = new WebPickerHelper(this);

    private WebView webView;
    private String url = "http://192.168.118.206:8080/Banjiuwan/app/page/orderEvaluate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initWebView();
    }

    private void initWebView() {
        webView = (WebView) findViewById(R.id.web);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebPickerChromeClient(webPickerHelper));
        webView.loadUrl(url);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        webPickerHelper.onActivityResult(requestCode, resultCode, intent);
    }


}