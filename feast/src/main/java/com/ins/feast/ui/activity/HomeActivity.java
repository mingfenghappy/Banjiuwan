package com.ins.feast.ui.activity;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.ins.baidumapsdk.Locationer;
import com.ins.feast.R;
import com.ins.feast.common.AppData;
import com.ins.feast.entity.Position;
import com.ins.feast.jsbridge.JSInterface;
import com.shelwee.update.UpdateHelper;
import com.sobey.common.utils.L;
import com.sobey.common.utils.PermissionsUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class HomeActivity extends BaseMapActivity implements Locationer.LocationCallback {

    private final static String JS_BRIDGE_NAME = "native";
    private WebView webView;
    //标题栏定位Tv
    private TextView title_location;
    //标题栏中间title
    private TextView title_center;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        EventBus.getDefault().register(this);
        setHandleLocationLifeCycleBySubclass(true);

        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        initBase();
        initView();
    }

    private void initBase() {
        //双击返回键退出
        setNeedDoubleClickExit(true);
        //检查并申请权限
        PermissionsUtil.checkAndRequestPermissions(this);
        //检查更新
        new UpdateHelper.Builder(this).checkUrl(AppData.Url.version_passenger).isHintNewVersion(false).build().check();
        locationer.startlocation();
    }

    private TitleViewHelper titleViewHelper;

    private void initView() {
        title_center = (TextView) findViewById(R.id.text_toolbar_title);
        title_location = (TextView) findViewById(R.id.title_location);
        titleViewHelper = new TitleViewHelper(this);

        initWebView();
    }

    /**
     * 自定义WebViewClient
     */
    private WebViewClient mClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            L.d(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            titleViewHelper.processTitleWithUrlWhenGoNewPage(url);
        }
    };

    /**
     * 自定义WebChromeClient
     */
    private WebChromeClient mChromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            title_center.setText(title);
        }
    };

    /**
     * WebView配置
     */
    private void initWebView() {
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(mClient);
        webView.setWebChromeClient(mChromeClient);

        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);
        webView.loadUrl(AppData.Url.app_homepage);
        webView.addJavascriptInterface(new JSInterface(this), JS_BRIDGE_NAME);
    }

    @Override
    public void onLocation(LatLng latLng, String city, String district, boolean isFirst) {
        locationer.stopLocation();
        title_location.setText(locationer.getAddrStr());
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
            titleViewHelper.processTitleWithUrlWhenGoBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.clearHistory();
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 接受地理位置选择的结果
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceivePostition(Position position) {
        String key = position.getKey();
        title_location.setText(key);
        L.d("receivePositionIn-- HomeActivity:" + key);
    }

}
