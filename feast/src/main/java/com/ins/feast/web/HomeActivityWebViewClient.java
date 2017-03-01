package com.ins.feast.web;

import android.webkit.WebView;

import com.ins.feast.R;
import com.ins.feast.ui.activity.DetailActivity;
import com.ins.feast.ui.activity.HomeActivity;
import com.ins.feast.ui.activity.TitleViewHelper;
import com.sobey.common.utils.L;

/**
 * author 边凌
 * date 2017/2/27 11:44
 * desc ${主页使用的{@link android.webkit.WebViewClient}}
 */

public class HomeActivityWebViewClient extends BaseWebViewClient {
    private HomeActivity homeActivity;
    private TitleViewHelper titleViewHelper;

    public HomeActivityWebViewClient(HomeActivity homeActivity) {
        super((WebView) homeActivity.findViewById(R.id.webView));
        this.homeActivity = homeActivity;
        this.titleViewHelper = new TitleViewHelper(homeActivity);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        L.d(url);
//        String lowerCaseUrl = url.toLowerCase();
//        if (lowerCaseUrl.contains("detail") && !lowerCaseUrl.contains("orderdetail")) {
//            //详情页面跳转处理
//            DetailActivity.start(homeActivity, url);
//        } else {
        view.loadUrl(url);
//        }
        return true;
    }

    /**
     * 在网页加载结束时处理标题栏样式
     */
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        String title = view.getTitle();
        titleViewHelper.processTitleWithUrl(url, title);
    }

//    @Override
//    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
//        String key = "http://localhost";
//        if (url.contains(key)) {
//            String imgPath = url.replace(key, "");
//            try {
//                FileInputStream fileInputStream = new FileInputStream(new File(imgPath));
//                return new WebResourceResponse("image/png", "UTF-8", fileInputStream);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//                return super.shouldInterceptRequest(view, url);
//            }
//        } else {
//            return super.shouldInterceptRequest(view, url);
//        }
//    }

}
