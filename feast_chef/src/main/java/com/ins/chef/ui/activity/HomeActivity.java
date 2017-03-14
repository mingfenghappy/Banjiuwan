package com.ins.chef.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ins.chef.R;
import com.ins.chef.web.ChefJSInterface;
import com.ins.middle.base.BaseWebChromeClient;
import com.ins.middle.base.BaseWebViewClient;
import com.ins.middle.base.WebSettingHelper;
import com.ins.middle.common.AppData;
import com.ins.middle.helper.CommonAppHelper;
import com.ins.middle.ui.activity.BaseFeastActivity;
import com.shelwee.update.UpdateHelper;
import com.sobey.common.utils.L;
import com.sobey.common.utils.PermissionsUtil;
import com.sobey.common.utils.PhoneUtils;
import com.sobey.common.utils.UrlUtil;

public class HomeActivity extends BaseFeastActivity implements RadioGroup.OnCheckedChangeListener {
    private WebView webView;
    private TextView toolbar_title;
    private RadioGroup rg;
    private RadioButton mine, mineOrderForm;
    private BaseWebChromeClient homeWebChromeClient;
    private UpdateHelper updateHelper;
    private boolean notLoad = false;
    private BaseWebViewClient webViewClient;
    private boolean first = true;

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
        updateHelper = new UpdateHelper.Builder(this).checkUrl(AppData.Url.version_chef).isHintNewVersion(false).build();
        updateHelper.check();
    }

    private void initSetting() {
        rg.setVisibility(View.VISIBLE);
        rg.setOnCheckedChangeListener(this);
    }

    private void initWeb() {
        homeWebChromeClient = new BaseWebChromeClient(this) {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                toolbar_title.setText(title);
            }
        };
        webViewClient = new BaseWebViewClient(webView) {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

//                Toast.makeText(HomeActivity.this,"load home",Toast.LENGTH_SHORT).show();
                L.d(url);

                if (url.startsWith("tel:")) {
                    PhoneUtils.callByUrl(HomeActivity.this, url);
                    return true;
                }

                if (TextUtils.equals(webView.getUrl(), url)) {
                    webView.loadUrl(url);
                    return true;
                }

                //如果登录（启动源生页面，并关闭当前）
                if (UrlUtil.matchUrl(url, AppData.Url.loginPageCook)) {
                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
                }

                if (url.contains("cookMy")
                        || url.contains("cookMyOrder")) {
                    view.loadUrl(url);
                    handleTabsByUrl(url);
                    if (first) {
                        first = false;
                        notLoad = false;
                    }
                    return true;
                }

                CommonWebActivity.start(HomeActivity.this, url);
                handleTabsByUrl(url);
                rg.setVisibility(View.VISIBLE);
                return true;
            }
        };

        WebSettingHelper
                .newInstance(webView)
                .commonSetting()
                .setWebChromeClient(homeWebChromeClient)
                .setWebViewClient(webViewClient)
                .addJavaScriptInterface(new ChefJSInterface(), JS_BRIDGE_NAME);

        //禁止WebView长按编辑
        CommonAppHelper.setWebViewNoLongClick(webView);
        webView.loadUrl(AppData.Url.FEAST_CHEF_MINE);
    }

    private void initView() {
        webView = (WebView) findViewById(R.id.webView);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        rg = (RadioGroup) findViewById(R.id.rg);
        mine = (RadioButton) findViewById(R.id.rg_mine);
        mineOrderForm = (RadioButton) findViewById(R.id.rg_orderForm);

        setWebViewLifeCycleSupport(webView);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notLoad = false;
            }
        };
        findViewById(R.id.rg_mine).setOnClickListener(listener);
        findViewById(R.id.rg_orderForm).setOnClickListener(listener);
    }

    //厨师端不处理回退
//    @Override
//    public void onBackPressed() {
//        if (webView != null && webView.canGoBack()) {
//            webView.goBack();
//            String originalUrl = webView.getOriginalUrl();
//            handleTabsByUrl(originalUrl);
//        } else {
//            super.onBackPressed();
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateHelper.onDestory();
        webViewClient.destroy();
    }

    private void handleTabsByUrl(String url) {
        notLoad = true;
        if (url.contains("cookMyOrder")) {
            rg.setVisibility(View.VISIBLE);
            if (!mineOrderForm.isChecked()) {
                mineOrderForm.setChecked(true);
            }
        } else if (url.contains("cookMy")) {
            rg.setVisibility(View.VISIBLE);
            if (!mine.isChecked()) {
                mine.setChecked(true);
            }
        } else {
            rg.setVisibility(View.VISIBLE);
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
        if (!TextUtils.isEmpty(url) && !notLoad) {
            webView.loadUrl(url);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        homeWebChromeClient.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) {
            webView.reload();
        }
    }
}
