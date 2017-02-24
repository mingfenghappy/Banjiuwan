package com.ins.feast.web;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * author 边凌
 * date 2017/2/24 16:10
 * desc ${TODO}
 */

public class HomeWebView extends WebView {
    public HomeWebView(Context context) {
        super(context);
    }

    public HomeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public HomeWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public HomeWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
        super(context, attrs, defStyleAttr, privateBrowsing);
    }

    /**
     * 打开图片选择器拿到结果后，在这里通知h5页面
     */
    public void setImageSelectResult(String path) {
        // TODO: 2017/2/24
    }
}
