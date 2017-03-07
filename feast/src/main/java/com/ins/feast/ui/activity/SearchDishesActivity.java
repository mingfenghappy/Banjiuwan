package com.ins.feast.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ins.feast.R;
import com.ins.middle.ui.activity.BaseAppCompatActivity;

public class SearchDishesActivity extends BaseAppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_dishes);
        ViewCompat.setTransitionName(findViewById(R.id.transitionView_whiteBg), "transitionSearch");
        initView();
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.webView_searchDishes);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    public void onClick_cancel(View view) {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.removeAllViews();
            webView.destroy();
        }
    }

    public static void start(HomeActivity homeActivity) {
        Intent starter = new Intent(homeActivity, SearchDishesActivity.class);
        View transitionView = homeActivity.findViewById(R.id.icon_right);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(homeActivity, Pair.create(transitionView, transitionView.getTransitionName()));
//            ActivityCompat.startActivity(homeActivity, starter, activityOptionsCompat.toBundle());
//        } else {
            homeActivity.startActivity(starter);
//        }
    }
}
