package com.ins.chef.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.ins.chef.R;
import com.ins.middle.base.BaseWebChromeClient;
import com.ins.middle.base.BaseWebViewClient;
import com.ins.middle.base.WebSettingHelper;
import com.ins.middle.ui.activity.BaseFeastActivity;
import com.sobey.common.utils.L;
import com.sobey.common.utils.PhoneUtils;

public class CommonWebActivity extends BaseFeastActivity {

    private final static String KEY_URL = "urlOfThisPage";
    private String urlOfThisPage;
    private WebView webView;
    private TextView toolbar_title;
    private BaseWebViewClient webViewClient;
    private BaseWebChromeClient webChromeClient;

    public static void start(Context context, String url) {
        Intent starter = new Intent(context, CommonWebActivity.class);
        starter.putExtra(KEY_URL, url);
        L.d("startCommonWebActivity:" + url);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_web);
        urlOfThisPage = getIntent().getStringExtra(KEY_URL);
        initView();
        initWebView();
    }

    private void initWebView() {
        webChromeClient = new BaseWebChromeClient(this) {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                toolbar_title.setText(title);
            }
        };
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

                if (urlOfThisPage.contains("cookLogin")
                        || urlOfThisPage.contains("cookMy")
                        || urlOfThisPage.contains("cookMyOrder")) {
                    finish();
                    return true;
                }

                CommonWebActivity.start(CommonWebActivity.this, url);
                return true;
            }
        };
        webView.setWebViewClient(webViewClient);
        webView.setWebChromeClient(webChromeClient);
        WebSettingHelper.newInstance(webView).commonSetting();
        webView.loadUrl(urlOfThisPage);
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.webView);
        setWebViewLifeCycleSupport(webView);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        findViewById(R.id.toolbar_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webViewClient.destroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        webChromeClient.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
