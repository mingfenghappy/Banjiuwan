package com.ins.feast.ui.activity;

import android.net.Uri;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * Created by Administrator on 2017/2/27.
 */

public class WebPickerChromeClient extends WebChromeClient {

    protected WebPickerHelper webPickerHelper2;

    public WebPickerChromeClient(WebPickerHelper webPickerHelper) {
        this.webPickerHelper2 = webPickerHelper;
    }

    //扩展浏览器上传文件
    //3.0++版本
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        webPickerHelper2.openFileChooserImpl(uploadMsg);
    }

    //3.0--版本
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        webPickerHelper2.openFileChooserImpl(uploadMsg);
    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        webPickerHelper2.openFileChooserImpl(uploadMsg);
    }

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        webPickerHelper2.openFileChooserImplForAndroid(filePathCallback);
        return true;
    }
}
