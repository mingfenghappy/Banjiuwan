package com.ins.feast.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.ins.feast.R;
import com.ins.feast.entity.Position;
import com.ins.feast.entity.Tabs;
import com.ins.feast.ui.helper.HomeTitleHelper;
import com.ins.feast.web.HomeActivityWebChromeClient;
import com.ins.feast.web.HomeActivityWebViewClient;
import com.ins.feast.web.HomeJSInterface;
import com.ins.feast.web.HomeWebView;
import com.ins.middle.base.WebSettingHelper;
import com.ins.middle.common.AppData;
import com.ins.middle.entity.WebEvent;
import com.shelwee.update.UpdateHelper;
import com.sobey.common.utils.PermissionsUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class HomeActivity extends BaseMapActivity implements
        RadioGroup.OnCheckedChangeListener {

    private HomeWebView webView;
    //标题栏定位Tv
    private TextView title_location;

    private HomeTitleHelper homeTitleHelper;
    private HomeActivityWebChromeClient webChromeClient;
    private HomeActivityWebViewClient webViewClient;
    private HomeJSInterface homeJsInterface;
    private UpdateHelper updateHelper;
    private boolean notLoad = false;

    /**
     * 注：该Activity启动模式为singleTask
     */
    public static void start(Context context) {
        Intent starter = new Intent(context, HomeActivity.class);
        context.startActivity(starter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setEventBusSupport();
        setHandleLocationLifeCycleBySubclass(true);

        initBase();
        initView();
        initWebViewSetting();
    }

    private void initBase() {
        //双击返回键退出
        setNeedDoubleClickExit(true);
        //检查并申请权限
        PermissionsUtil.checkAndRequestPermissions(this);
        //检查更新
        updateHelper = new UpdateHelper.Builder(this).checkUrl(AppData.Url.version_feast).isHintNewVersion(false).build();
        updateHelper.check();
        startLocation();
    }

    private void initView() {
        title_location = (TextView) findViewById(R.id.title_location);
        webView = (HomeWebView) findViewById(R.id.webView);
        RadioGroup tabRg = (RadioGroup) findViewById(R.id.radioGroup);
        tabRg.setOnCheckedChangeListener(this);
        homeTitleHelper = new HomeTitleHelper(this);

        /*notLoad属性用于解决连续按返回键时在最后两个页面循环切换无法结束应用的问题*/
        /*这是由于在onBackPressed中WebView调用goBack方法时，RadioGroup改变选中按钮id，而在onCheckChanged中会加载一遍当前tab的url*/
        /*因此WebView始终可以goBack，加入notLoad属性判断后可以解决这个问题*/
        for (Tabs tabs : Tabs.values()) {
            findViewById(tabs.getButtonId()).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notLoad = false;
                }
            });
        }
    }

    /**
     * WebView配置
     */
    private void initWebViewSetting() {
        setWebViewLifeCycleSupport(webView);

        webViewClient = new HomeActivityWebViewClient(this);
        webChromeClient = new HomeActivityWebChromeClient(this);

        homeJsInterface = new HomeJSInterface(this, webView);
        WebSettingHelper.newInstance(webView).commonSetting()
                .setWebViewClient(webViewClient)
                .setWebChromeClient(webChromeClient)
                .addJavaScriptInterface(homeJsInterface, JS_BRIDGE_NAME);

        webView.loadUrl(AppData.Url.app_home);
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
    }

    @Override
    public void onLocation(LatLng latLng, String city, String district, boolean isFirst) {
        stopLocation();
        title_location.setText(getAddStr());
    }

    @Override
    public void onBackPressed() {

        if (webView != null && webView.canGoBack()) {
            webView.goBack();
            String originalUrl = webView.getOriginalUrl();
            selectTabByUrl(originalUrl);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 根据返回到的页面的url改变tab栏选中的tab
     */
    private void selectTabByUrl(String url) {
        notLoad = true;
        int buttonId = 0;

        for (Tabs tabs : Tabs.values()) {
            if (url.contains(tabs.getUrlTag())) {
                buttonId = tabs.getButtonId();
            }
        }

        RadioButton radioButton = (RadioButton) findViewById(buttonId);
        if (radioButton != null) {
            radioButton.setChecked(true);
            handleTitleByCheckedId(buttonId);
        }
    }

    @Override
    protected void onDestroy() {
        if (webViewClient != null) {
            webViewClient.destroy();
        }
        updateHelper.onDestory();
        super.onDestroy();
    }

    /**
     * 接受地理位置选择的结果
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceivePostition(Position position) {
        String key = position.getKey();
        title_location.setText(key);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWebEvent(WebEvent event) {
        switch (event) {
            case shouldRefresh:
                webView.reload();
                break;
            case jumpToCarTab:
                switchTab(R.id.rb_cart);
                break;
            case reLocation:
                startLocation();
                break;
        }
    }

    private void switchTab(@IdRes int tabId) {
        RadioButton radioButton = (RadioButton) findViewById(tabId);
        if (radioButton != null) {
            radioButton.setChecked(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        webChromeClient.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        handleTitleByCheckedId(checkedId);
    }

    private void handleTitleByCheckedId(int checkedId) {
        String url = null;

        for (Tabs tabs : Tabs.values()) {
            if (checkedId == tabs.getButtonId()) {
                url = tabs.getUrl();
                homeTitleHelper.handleTitleStyleByTag(tabs.getTitleType());
                homeTitleHelper.setTitleText(tabs.getTitle());
            }
        }

        if (webView != null && !notLoad) {
            webView.loadUrl(url);
        }
    }
}
