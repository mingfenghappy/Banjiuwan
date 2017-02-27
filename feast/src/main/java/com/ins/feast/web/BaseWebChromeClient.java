package com.ins.feast.web;

import android.support.annotation.CallSuper;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * author 边凌
 * date 2017/2/27 17:58
 * desc ${TODO}
 */

public class BaseWebChromeClient extends WebChromeClient{
    @CallSuper
    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
    }
}
