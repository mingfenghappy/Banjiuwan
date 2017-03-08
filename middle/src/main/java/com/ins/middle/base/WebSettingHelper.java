package com.ins.middle.base;

import android.os.Build;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * author 边凌
 * date 2017/3/8 16:16
 * desc ${TODO}
 */

public class WebSettingHelper {
    private WebView webView;
    private WebSettings settings;

    private WebSettingHelper(WebView webView) {
        this.webView = webView;
    }

    public static WebSettingHelper newInstance(WebView webView) {
        return new WebSettingHelper(webView);
    }

    public WebSettings getSettings() {
        return settings;
    }

    public WebSettingHelper commonSetting() {
        settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
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
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowFileAccessFromFileURLs(true);
        }
        return this;
    }

    public WebSettingHelper setWebChromeClient(WebChromeClient webChromeClient) {
        webView.setWebChromeClient(webChromeClient);
        return this;
    }

    public WebSettingHelper setWebViewClient(WebViewClient webViewClient) {
        webView.setWebViewClient(webViewClient);
        return this;
    }

    public WebSettingHelper addJavaScriptInterface(Object jsInterface, String jsBridgeName) {
        webView.addJavascriptInterface(jsInterface, jsBridgeName);
        return this;
    }

}
