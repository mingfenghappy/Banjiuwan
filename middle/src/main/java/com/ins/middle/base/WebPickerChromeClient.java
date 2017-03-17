package com.ins.middle.base;

import android.net.Uri;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;

/**
 * Created by Administrator on 2017/2/27.
 */

public class WebPickerChromeClient extends WebChromeClient {

    protected WebPickerHelper webPickerHelper;

    public WebPickerChromeClient(WebPickerHelper webPickerHelper) {
        this.webPickerHelper = webPickerHelper;
    }

    //扩展浏览器上传文件
    //3.0++版本
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        webPickerHelper.openFileChooserImpl(uploadMsg);
    }

    //3.0--版本
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        webPickerHelper.openFileChooserImpl(uploadMsg);
    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        webPickerHelper.openFileChooserImpl(uploadMsg);
    }

    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        webPickerHelper.onenFileChooseImpleForAndroid(filePathCallback);
        return true;
    }
}
