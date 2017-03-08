package com.ins.feast.web;

import com.ins.feast.ui.activity.HomeActivity;
import com.ins.middle.base.BaseWebChromeClient;

/**
 * author 边凌
 * date 2017/2/24 14:52
 * desc ${主页使用的{@link android.webkit.WebChromeClient}}
 */

public class HomeActivityWebChromeClient extends BaseWebChromeClient {
//    private ProgressBar bar;

    public HomeActivityWebChromeClient(HomeActivity homeActivity) {
        super(homeActivity);
    }

}
