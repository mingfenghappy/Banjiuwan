package com.ins.feast.web;

import android.text.TextUtils;
import android.widget.Toast;

import com.ins.feast.R;
import com.ins.feast.ui.activity.CardActivity2;
import com.ins.feast.ui.activity.CommonWebActivity;
import com.ins.feast.ui.activity.HomeActivity;
import com.ins.feast.utils.AppHelper;
import com.ins.middle.base.BaseWebViewClient;
import com.ins.middle.common.AppData;
import com.ins.middle.entity.WebEvent;
import com.ins.middle.utils.ParamUtil;
import com.sobey.common.utils.PhoneUtils;
import com.sobey.common.utils.UrlUtil;
import com.tencent.smtt.sdk.WebView;

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
        if (AppData.Config.showTestToast)
            Toast.makeText(webView.getContext(), "捕获链接:" + url, Toast.LENGTH_LONG).show();

        //首先检查连接是否需要进行地理拦截，对需要进行拦截的链接进行捕获和弹窗提示
        if (!AppHelper.couldEnter(homeActivity.areaData, url, homeActivity.nowLatlng, homeActivity.dialogNotice, homeActivity.couldOrder)) {
            //不可进入的链接和地理位置
            if (homeActivity.dialogNotice != null) homeActivity.dialogNotice.show();
//            Toast.makeText(homeActivity, "你不在可下单范围内", Toast.LENGTH_SHORT).show();
            return true;
        }

        if (url.startsWith("tel:")) {
            PhoneUtils.phone(homeActivity, url);
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
        //pageType:5 关闭所有页面并回到主页
        int pageType = ParamUtil.getParamInt(url, "pageType", 0);
        if (pageType == 1) {
            webView.loadUrl(url);
        } else if (pageType == 3) {
            EventBus.getDefault().post(WebEvent.shouldRefresh);
            CommonWebActivity.start(homeActivity, url);
        } else if (pageType == 5) {
            EventBus.getDefault().post(WebEvent.finishActivity);
            homeActivity.switchTab(R.id.rb_home);
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
            if (UrlUtil.matchUrl(url, cardType.getTag())) {
                CardActivity2.start(homeActivity, cardType);
                return true;
            }
        }
        return false;
    }
}