package com.ins.feast.web;

import android.os.Handler;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
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
    private ProgressBar bar;
    public HomeActivityWebChromeClient(HomeActivity homeActivity) {
        title_center = (TextView) homeActivity.findViewById(R.id.text_toolbar_title);
        bar= (ProgressBar) homeActivity.findViewById(R.id.progress);
    }
    @Override
    public void onReceivedTitle(WebView view, String title) {
        title_center.setText(title);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        bar.setProgress(newProgress);
        if (newProgress == 100) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bar.setVisibility(View.GONE);
                }
            },200);
        } else {
            bar.setVisibility(View.VISIBLE);
        }
        super.onProgressChanged(view, newProgress);
    }
}
