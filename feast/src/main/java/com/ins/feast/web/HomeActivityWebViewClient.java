package com.ins.feast.web;

import android.webkit.WebView;

import com.ins.feast.R;
import com.ins.middle.common.AppData;
import com.ins.feast.ui.activity.CardActivity;
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
        boolean jumped = jumpedIfIsCardDetail(url);
        if (!jumped) {
            //没跳转的情况下跳转CommonWeb
            CommonWebActivity.start(homeActivity, url);
        }
        return true;
    }

    /**
     * 处理卡片式布局的相关跳转
     *
     * @param url 被解析的url
     * @return 是否已经跳转
     */
    private boolean jumpedIfIsCardDetail(String url) {

        for (AppData.CardDetail cardDetail : AppData.CardDetail.values()) {
            if (url.contains(cardDetail.getTag())) {
                CardActivity.start(homeActivity, cardDetail);
                return true;
            }
        }
        return false;
    }

}
