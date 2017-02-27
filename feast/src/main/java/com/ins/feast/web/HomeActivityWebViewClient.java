package com.ins.feast.web;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ins.feast.R;
import com.ins.feast.ui.activity.DetailActivity;
import com.ins.feast.ui.activity.HomeActivity;
import com.ins.feast.ui.activity.TitleViewHelper;
import com.ins.feast.ui.view.NoNetView;
import com.sobey.common.utils.L;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * author 边凌
 * date 2017/2/27 11:44
 * desc ${TODO}
 */

public class HomeActivityWebViewClient extends WebViewClient {
    private HomeActivity homeActivity;
    private TitleViewHelper titleViewHelper;
    private WebView webView;
    private View noNetRoot;
    private NoNetView noNetView;

    public HomeActivityWebViewClient(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
        this.titleViewHelper = new TitleViewHelper(homeActivity);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        L.d(url);
        String lowerCaseUrl = url.toLowerCase();
        if (lowerCaseUrl.contains("detail") && !lowerCaseUrl.contains("orderdetail")) {
            //详情页面跳转处理
            DetailActivity.start(homeActivity, url);
        } else {
            view.loadUrl(url);
        }
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        String title = view.getTitle();
        titleViewHelper.processTitleWithUrl(url, title);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        String key = "http://localhost";
        if (url.contains(key)) {
            String imgPath = url.replace(key, "");
            try {
                FileInputStream fileInputStream = new FileInputStream(new File(imgPath));
                return new WebResourceResponse("image/png", "UTF-8", fileInputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return super.shouldInterceptRequest(view, url);
            }
        } else {
            return super.shouldInterceptRequest(view, url);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        if (error.getErrorCode() == ERROR_CONNECT) {
            noNetProcess();
        }

    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        if (errorCode == ERROR_CONNECT) {
            noNetProcess();
        }
    }

    /**
     * 无网络处理
     */
    private void noNetProcess() {
    }
}
