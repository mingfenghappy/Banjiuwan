package com.ins.feast.web;

import android.webkit.WebView;

import com.ins.feast.R;
import com.ins.feast.ui.activity.CommonWebActivity;
import com.ins.feast.ui.activity.HomeActivity;
import com.sobey.common.utils.L;

/**
 * author 边凌
 * date 2017/2/27 11:44
 * desc ${主页使用的{@link android.webkit.WebViewClient}}
 */

public class HomeActivityWebViewClient extends BaseWebViewClient {
    private HomeActivity homeActivity;

    public HomeActivityWebViewClient(HomeActivity homeActivity) {
        super((WebView) homeActivity.findViewById(R.id.webView));
        this.homeActivity = homeActivity;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        L.d(url);
        CommonWebActivity.start(homeActivity,url);
        return true;
    }

    /**
     * 在网页加载结束时处理标题栏样式
     */
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        String title = view.getTitle();
    }
}
