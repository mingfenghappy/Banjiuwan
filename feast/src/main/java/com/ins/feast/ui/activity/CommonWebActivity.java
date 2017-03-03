package com.ins.feast.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.ins.feast.R;
import com.ins.feast.ui.helper.CommonWebTitleHelper;
import com.ins.feast.web.BaseWebChromeClient;
import com.ins.feast.web.BaseWebViewClient;
import com.ins.feast.web.CommonWebJSInterface;
import com.sobey.common.utils.L;

public class CommonWebActivity extends BaseBackActivity {
    private final static String KEY_URL = "url";
    private String url;
    private WebView webView;
    private CommonWebTitleHelper titleHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim_jump_web);
        setNeedDoubleClickExit(false);
        initSetting();
        findView();
        initWebViewSetting();
    }

    private void initWebViewSetting() {
        webView.setWebChromeClient(new BaseWebChromeClient(this) {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                titleHelper.setTitleText(title);
            }
        });
        webView.setWebViewClient(new BaseWebViewClient(webView) {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                L.d(url);
                if (CommonWebActivity.this.url.contains("login")) {
                    finish();
                } else {
                    CommonWebActivity.start(CommonWebActivity.this, url);
                }
                return true;
            }
        });
        webView.addJavascriptInterface(new CommonWebJSInterface(), JS_BRIDGE_NAME);

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
        webView.loadUrl(url);
    }

    private void findView() {
        webView = (WebView) findViewById(R.id.webView);
    }

    private void initSetting() {
        url = getIntent().getStringExtra(KEY_URL);
        titleHelper = new CommonWebTitleHelper(this);
        titleHelper.handleTitleWithUrl(url);
    }

    public static void start(Context context, String url) {
        Intent starter = new Intent(context, CommonWebActivity.class);
        starter.putExtra(KEY_URL, url);
        context.startActivity(starter);
        //手动设置进场动画
        ((Activity)context).overridePendingTransition(R.anim.translate_enter, 0);
    }

    @Override
    public void finish() {
        super.finish();
        //手动设置出场动画
        overridePendingTransition(0, R.anim.translate_exit);
    }
}
