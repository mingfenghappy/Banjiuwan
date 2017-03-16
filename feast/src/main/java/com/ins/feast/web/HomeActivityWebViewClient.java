package com.ins.feast.web;

import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.Toast;

import com.ins.feast.R;
import com.ins.feast.ui.activity.CardActivity;
import com.ins.feast.ui.activity.CommonWebActivity;
import com.ins.feast.ui.activity.HomeActivity;
import com.ins.middle.base.BaseWebViewClient;
import com.ins.middle.common.AppData;
import com.ins.middle.entity.WebEvent;
import com.ins.middle.utils.ParamUtil;
import com.sobey.common.utils.L;
import com.sobey.common.utils.PhoneUtils;

import org.greenrobot.eventbus.EventBus;

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
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
//        Toast.makeText(homeActivity, "load home", Toast.LENGTH_SHORT).show();
        if (url.startsWith("tel:")) {
            PhoneUtils.callByUrl(homeActivity, url);
            return true;
        }
        if (TextUtils.equals(webView.getUrl(), url)) {
            webView.loadUrl(url);
            return true;
        }
        if (jumpedIfIsCardDetail(url)) {
            return true;
        }
        //根据pageType决定其打开页面的方式
        //pageType:0 打开新activity 显示页面
        //pageType:1 在当前activity 显示页面
        //pageType:2 关闭当前页面并刷新上一级页面（home页面不会有这种情况）
        //pageType:3 打开新activity 显示页面并刷新当前页面
        int pageType = ParamUtil.getParamInt(url, "pageType", 0);
        if (pageType == 1) {
            webView.loadUrl(url);
        } else if (pageType == 3) {
            EventBus.getDefault().post(WebEvent.shouldRefresh);
            CommonWebActivity.start(homeActivity, url);
        } else {
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

        for (AppData.CardType cardType : AppData.CardType.values()) {
            if (url.contains(cardType.getTag())) {
                CardActivity.start(homeActivity, cardType);
                return true;
            }
        }
        return false;
    }

}
