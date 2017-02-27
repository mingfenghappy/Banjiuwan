package com.ins.feast.web;

import android.content.Intent;
import android.support.annotation.CallSuper;
import android.webkit.WebView;

import com.ins.feast.ui.activity.WebPickerChromeClient;
import com.ins.feast.ui.activity.WebPickerHelper;

/**
 * author 边凌
 * date 2017/2/27 17:58
 * desc ${TODO}
 */

public class BaseWebChromeClient extends WebPickerChromeClient{
    public BaseWebChromeClient(WebPickerHelper webPickerHelper) {
        super(webPickerHelper);
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        webPickerHelper2.onActivityResult(requestCode,resultCode,intent);
    }
}
