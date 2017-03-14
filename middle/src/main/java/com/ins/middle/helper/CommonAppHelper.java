package com.ins.middle.helper;

import android.view.View;
import android.webkit.WebView;

import com.sobey.common.utils.StrUtils;

/**
 * Created by Administrator on 2017/3/13.
 * 通用app帮助类，两端通用
 */

public class CommonAppHelper {
    //取消webview的长点击事件
    public static void setWebViewNoLongClick(WebView webView){
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
    }
}
