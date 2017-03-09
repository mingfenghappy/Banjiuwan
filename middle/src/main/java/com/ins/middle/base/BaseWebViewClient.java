package com.ins.middle.base;

import android.graphics.Bitmap;
import android.net.NetworkInfo;
import android.support.annotation.CallSuper;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ins.middle.common.AppData;
import com.ins.middle.entity.NetStateChangedEvent;
import com.ins.middle.entity.WebEvent;
import com.ins.middle.ui.dialog.DialogLoading;
import com.sobey.common.utils.L;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.ins.middle.common.AppData.Config.ERROR_PAGE_URL;


/**
 * author 边凌
 * date 2017/2/27 16:36
 * desc {@link WebViewClient}的基类，包含无网络情况时的提示处理
 */

public class BaseWebViewClient extends WebViewClient {
    private WebView webView;
    private DialogLoading loading;
    /**
     * 保存最近一次非{@link AppData.Config#ERROR_PAGE_URL}的URL，用于在网络恢复后重新访问
     */
    private String lastUrl;

    public BaseWebViewClient(final WebView webView) {
        this.webView = webView;
        //点击无网络页重载网页
        webView.setOnTouchListener(new View.OnTouchListener() {
            /**
             * 判断为无网络页时，点击WebView则重新加载最近一次的URL
             */
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP &&
                        TextUtils.equals(ERROR_PAGE_URL, webView.getUrl())) {
                    L.d("onTouch,reload:" + lastUrl);
                    webView.loadUrl(lastUrl);
                    EventBus.getDefault().post(WebEvent.reLocation);
                    return true;
                }
                return false;
            }
        });

        try {
            EventBus.getDefault().register(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        loading=new DialogLoading(webView.getContext());
    }

    /**
     * 反注册EventBus
     */
    public final void destroy() {
        try {
            EventBus.getDefault().unregister(this);
            loading.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载URL错误时的回调
     */
    @CallSuper
    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        netErrorProcess(errorCode, failingUrl);
    }

    /**
     * 网络错误时的本地处理
     *
     * @param errorCode  错误码
     * @param failingUrl 被保存的最近一次访问的URL
     */
    private void netErrorProcess(int errorCode, String failingUrl) {
        if (!TextUtils.equals(failingUrl, ERROR_PAGE_URL)) {
            lastUrl = failingUrl;
        }
        switch (errorCode) {
            case ERROR_CONNECT:
            case ERROR_HOST_LOOKUP:
                webView.loadUrl(ERROR_PAGE_URL);
                break;
        }
    }

    /**
     * 接受网络状态改变广播，收到网络恢复的广播时，自动加载最近一次访问的URL
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public final void onReceiveNetStateChanged(NetStateChangedEvent event) {
        NetworkInfo activeInfo = event.getActiveInfo();
        if (activeInfo != null && webView != null) {
            if (activeInfo.isAvailable()) {
                webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
                L.d("onReceiveNetStateChanged,loadUrl:" + lastUrl);
                webView.loadUrl(lastUrl);
            } else {
                webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            }
        }
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        loading.show();
        webView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        loading.hide();
        webView.setVisibility(View.VISIBLE);
    }
}
