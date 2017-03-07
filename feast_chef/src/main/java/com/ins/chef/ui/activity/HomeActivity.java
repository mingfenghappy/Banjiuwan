package com.ins.chef.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ins.chef.R;
import com.ins.middle.base.BaseWebChromeClient;
import com.ins.middle.base.BaseWebViewClient;
import com.ins.middle.common.AppData;
<<<<<<< HEAD
=======
import com.ins.chef.web.BaseWebChromeClient;
import com.ins.chef.web.BaseWebViewClient;
import com.shelwee.update.UpdateHelper;
>>>>>>> 27f4b66228882481c9d445b7eacf647da066b0c9
import com.sobey.common.base.BaseAppCompatActivity;
import com.sobey.common.utils.L;
import com.sobey.common.utils.PermissionsUtil;

public class HomeActivity extends BaseAppCompatActivity implements RadioGroup.OnCheckedChangeListener {
    private WebView webView;
    private TextView toolbar_title;
    private RadioGroup rg;
    private RadioButton mine, mineOrderForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setNeedDoubleClickExit(true);
        initBase();
        initView();
        initWeb();
        initSetting();
    }

    private void initBase() {
        //检查并申请权限
        PermissionsUtil.checkAndRequestPermissions(this);
        //检查更新
        new UpdateHelper.Builder(this).checkUrl(AppData.Url.version_chef).isHintNewVersion(false).build().check();
    }

    private void initSetting() {
        rg.setVisibility(View.GONE);
        rg.setOnCheckedChangeListener(this);
    }

    private void initWeb() {
        webView.setWebChromeClient(new BaseWebChromeClient(this) {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                toolbar_title.setText(title);
            }
        });
        webView.setWebViewClient(new BaseWebViewClient(webView) {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                L.d("shouldOverrideUrlLoading(HomeActivity):\n" + url);
                if (TextUtils.equals(url, AppData.Url.FEAST_CHEF_MINE)
                        || TextUtils.equals(url, AppData.Url.FEAST_CHEF_MINE_ORDERFORM)) {
                    view.loadUrl(url);
                } else {
                    CommonWebActivity.start(HomeActivity.this, url);
                }
                handleTabsByUrl(url);
                return true;
            }
        });
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.loadUrl(AppData.Url.FEAST_CHEF_HOMEPAGE);
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.webView);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        rg = (RadioGroup) findViewById(R.id.rg);
        mine = (RadioButton) findViewById(R.id.rg_mine);
        mineOrderForm = (RadioButton) findViewById(R.id.rg_orderForm);
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
            String originalUrl = webView.getOriginalUrl();
            handleTabsByUrl(originalUrl);
        } else {
            super.onBackPressed();
        }

    }

    private void handleTabsByUrl(String url) {
        if (TextUtils.equals(url, AppData.Url.FEAST_CHEF_MINE)) {
            rg.setVisibility(View.VISIBLE);
            if (!mine.isChecked()) {
                mine.setChecked(true);
            }
        } else if (TextUtils.equals(url, AppData.Url.FEAST_CHEF_MINE_ORDERFORM)) {
            rg.setVisibility(View.VISIBLE);
            if (!mineOrderForm.isChecked()) {
                mineOrderForm.setChecked(true);
            }
        } else {
            rg.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        String url = null;
        switch (checkedId) {
            case R.id.rg_mine:
                url = AppData.Url.FEAST_CHEF_MINE;
                break;
            case R.id.rg_orderForm:
                url = AppData.Url.FEAST_CHEF_MINE_ORDERFORM;
                break;
        }
        if (!TextUtils.isEmpty(url)) {
            webView.loadUrl(url);
        }
    }
}
