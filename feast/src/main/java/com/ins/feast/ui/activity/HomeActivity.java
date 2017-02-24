package com.ins.feast.ui.activity;

import android.net.NetworkInfo;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.ins.baidumapsdk.Locationer;
import com.ins.feast.R;
import com.ins.feast.common.AppData;
import com.ins.feast.entity.NetStateChangedEvent;
import com.ins.feast.entity.Position;
import com.ins.feast.jsbridge.JSInterface;
import com.ins.feast.receiver.NetStateReceiver;
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
        new UpdateHelper.Builder(this).checkUrl(AppData.Url.version_passenger).isHintNewVersion(false).build().check();
        locationer.startlocation();
        titleViewHelper = new TitleViewHelper(this);
        NetStateReceiver.registerAboveSDK21(this);
    }

    //用于处理标题栏样式
    private TitleViewHelper titleViewHelper;

    private void initView() {
        title_center = (TextView) findViewById(R.id.text_toolbar_title);
        title_location = (TextView) findViewById(R.id.title_location);
        webView = (WebView) findViewById(R.id.webView);
    }


    /**
     * WebView配置
     */
    private void initWebViewSetting() {
        webView.setWebViewClient(mClient);
        webView.setWebChromeClient(mChromeClient);

        WebSettings settings = webView.getSettings();
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAppCachePath(webView.getContext().getCacheDir().getAbsolutePath());

        settings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JSInterface(this), JS_BRIDGE_NAME);

        webView.loadUrl(AppData.Url.app_homepage);

    }

    /**
     * 自定义WebViewClient
     */
    private WebViewClient mClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            L.d(url);
            if (url.toLowerCase().contains("detail")) {
                //详情页面跳转处理
                DetailActivity.start(HomeActivity.this, url);
            } else {
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            String title = view.getTitle();
            titleViewHelper.processTitleWithUrl(url, title);
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

    @Override
    public void onLocation(LatLng latLng, String city, String district, boolean isFirst) {
        locationer.stopLocation();
        title_location.setText(locationer.getAddrStr());
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (webView != null) {
            webView.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) {
            webView.onResume();
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
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveNetStateChanged(NetStateChangedEvent event) {
        NetworkInfo activeInfo = event.getActiveInfo();
        if (activeInfo != null && webView != null) {
            if (activeInfo.isAvailable()) {
                webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
                locationer.startlocation();
                webView.reload();
                L.d("NetChanged:available");
            } else {
                webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                L.d("NetChanged:not available");
            }
        }
    }
}
