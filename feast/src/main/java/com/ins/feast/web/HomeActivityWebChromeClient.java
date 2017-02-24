package com.ins.feast.web;

import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import com.ins.feast.R;
import com.ins.feast.ui.activity.HomeActivity;

/**
 * author 边凌
 * date 2017/2/24 14:52
 * desc ${TODO}
 */

public class HomeActivityWebChromeClient extends WebChromeClient {
    //标题栏title
    private TextView title_center;

    public HomeActivityWebChromeClient(HomeActivity homeActivity) {
        title_center = (TextView) homeActivity.findViewById(R.id.text_toolbar_title);
    }
    @Override
    public void onReceivedTitle(WebView view, String title) {
        title_center.setText(title);
    }

}
