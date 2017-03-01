package com.ins.feast.web;

import android.webkit.WebView;

import com.ins.feast.ui.activity.HomeActivity;
import com.sobey.common.helper.WebPickerHelper;

/**
 * author 边凌
 * date 2017/2/24 14:52
 * desc ${主页使用的{@link android.webkit.WebChromeClient}}
 */

public class HomeActivityWebChromeClient extends BaseWebChromeClient {
//    private ProgressBar bar;

    public HomeActivityWebChromeClient(HomeActivity homeActivity) {
        super(new WebPickerHelper(homeActivity));
//        bar = (ProgressBar) homeActivity.findViewById(R.id.progress);
    }


    /**
     * 网页加载进度条设置
     */
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
//        bar.setProgress(newProgress);
//        if (newProgress == 100) {
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    bar.setVisibility(View.GONE);
//                }
//            }, 200);
//        } else {
//            bar.setVisibility(View.VISIBLE);
//        }
        super.onProgressChanged(view, newProgress);
    }
}
