package com.ins.feast.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;

import com.ins.feast.R;
import com.ins.feast.common.JSFunctionUrl;
import com.ins.feast.ui.helper.CommonWebTitleHelper;
import com.ins.feast.web.CommonWebJSInterface;
import com.ins.middle.base.BaseWebChromeClient;
import com.ins.middle.base.BaseWebViewClient;
import com.ins.middle.base.WebSettingHelper;
import com.ins.middle.entity.WebEvent;
import com.ins.middle.ui.activity.BaseBackActivity;
import com.sobey.common.utils.L;
import com.sobey.common.utils.PhoneUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class CommonWebActivity extends BaseBackActivity {
    private final static String KEY_URL = "urlOfThisPage";
    private String urlOfThisPage;
    private WebView webView;
    private CommonWebTitleHelper titleHelper;
    private BaseWebViewClient webViewClient;
    private BaseWebChromeClient webChromeClient;

    public static void start(Context context, String url) {
        Intent starter = new Intent(context, CommonWebActivity.class);
        starter.putExtra(KEY_URL, url);
        context.startActivity(starter);
        //手动设置进场动画
        ((Activity) context).overridePendingTransition(R.anim.translate_enter, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim_jump_web);
        setNeedDoubleClickExit(false);
        setEventBusSupport();
        initSetting();
        findView();
        initWebViewSetting();
    }

    private void initWebViewSetting() {
        setWebViewLifeCycleSupport(webView);
        webClient();
        webSetting();
        webView.loadUrl(urlOfThisPage);
    }

    private void webClient() {

        webChromeClient = new BaseWebChromeClient(this) {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                titleHelper.setTitleText(title);
            }
        };
        /*moreAddress*/
        webViewClient = new BaseWebViewClient(webView) {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.startsWith("tel:")) {
                    PhoneUtils.callByUrl(CommonWebActivity.this, url);
                    return true;
                }

                if (TextUtils.equals(urlOfThisPage, url)) {
                    L.d("refresh:Load " + url);
                    webView.loadUrl(url);
                    return true;
                }

                if (urlOfThisPage.contains("login")) {
                    finish();
                    EventBus.getDefault().post(WebEvent.shouldRefresh);
                    return true;
                }

                L.d("startCommonWebActivity:" + url);
                CommonWebActivity.start(CommonWebActivity.this, url);
                return true;
            }
        };
        webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(webViewClient);
    }

    private void webSetting() {
        webView.addJavascriptInterface(new CommonWebJSInterface(this), JS_BRIDGE_NAME);
        WebSettingHelper.newInstance(webView).commonSetting();
    }

    private void findView() {
        webView = (WebView) findViewById(R.id.webView);
    }

    private void initSetting() {
        urlOfThisPage = getIntent().getStringExtra(KEY_URL);
        L.d(urlOfThisPage);
        titleHelper = new CommonWebTitleHelper(this);
        titleHelper.handleTitleWithUrl(urlOfThisPage);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWebEvent(WebEvent event) {
        switch (event) {
            case shouldRefresh:
                webView.reload();
                break;
            case payCanceled:
            case payFailed:
                webView.loadUrl(JSFunctionUrl.PAY_FAILED);
                break;
            case paySuccess:
                webView.loadUrl(JSFunctionUrl.PAY_SUCCESS);
                break;
            case finishActivity:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        webChromeClient.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webViewClient.destroy();
    }

    @Override
    public void finish() {
        super.finish();
        //手动设置出场动画
        overridePendingTransition(0, R.anim.translate_exit);
    }
}
