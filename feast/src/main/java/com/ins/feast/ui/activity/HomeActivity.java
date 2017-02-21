package com.ins.feast.ui.activity;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.ins.baidumapsdk.Locationer;
import com.ins.feast.R;
import com.ins.feast.common.AppData;
import com.ins.feast.entity.Position;
import com.ins.feast.jsbridge.JSInterface;
import com.jakewharton.rxbinding.view.RxView;
import com.shelwee.update.UpdateHelper;
import com.sobey.common.utils.L;
import com.sobey.common.utils.PermissionsUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;


public class HomeActivity extends BaseMapActivity implements Locationer.LocationCallback, View.OnClickListener {

    private final static String JS_BRIDGE_NAME = "native";
    private WebView webView;
    //标题栏定位Tv
    private TextView title_location;
    //标题栏中间title
    private TextView title_center;
    private View iconRight;
    private ImageView iconLeft;
    private View appBarLayout;
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

    private void initView() {
        title_center = (TextView) findViewById(R.id.text_toolbar_title);
        title_location = (TextView) findViewById(R.id.title_location);
        iconLeft= (ImageView) findViewById(R.id.icon_left);
        appBarLayout=findViewById(R.id.appBarLayout);
        iconRight=findViewById(R.id.icon_right);
        RxView.clicks(title_location).throttleFirst(1, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                ChooseLocationActivity.start(HomeActivity.this);
            }
        });
        iconLeft.setOnClickListener(this);

        initWebView();
    }

    /**
     * 自定义WebViewClient
     */
    private WebViewClient mClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            processPageTitleBarWithUrl(url);
            return true;
        }


    };

    private int height_webView;
    private int height_barLayout;
    @Override
    protected void onResume() {
        super.onResume();
        height_webView =webView.getHeight();
        height_barLayout =appBarLayout.getHeight();
    }

    private final static String KEY_HOME = "app/page/index";
    private final static String KEY_CAR = "app/page/car";
    private final static String KEY_FIND = "app/page/find";
    private final static String KEY_CUSTOMER = "app/page/customer";
    private final static String KEY_MY = "app/page/my";

    /**
     * 根据拦截的URL处理标题栏图标和文字配置
     */
    private void processPageTitleBarWithUrl(String url) {
        if (url.contains(KEY_HOME)) {
            showTitleBarHome();
        } else if (url.contains(KEY_CAR)) {
            showTitleBarCar();
        } else if (url.contains(KEY_FIND)) {
            showTitleBarFind();
        } else if (url.contains(KEY_CUSTOMER)) {
            showTitleBarCustomer();
        } else if (url.contains(KEY_MY)) {
            showTitleBarMy();
        }
    }

    //我的
    private void showTitleBarMy() {
        setWebViewHeight(height_webView);
        appBarLayout.setVisibility(View.VISIBLE);
        setIconVisibility(View.GONE);
    }

    //客服
    private void showTitleBarCustomer() {
        setWebViewHeight(height_webView+height_barLayout);
        appBarLayout.setVisibility(View.GONE);
    }

    //中间
    private void showTitleBarFind() {
        setWebViewHeight(height_webView);
        appBarLayout.setVisibility(View.VISIBLE);
        setIconVisibility(View.GONE);
    }

    //购物车
    private void showTitleBarCar() {
        setWebViewHeight(height_webView);
        appBarLayout.setVisibility(View.VISIBLE);
        iconLeft.setImageResource(R.mipmap.ic_leftarrow_white);
        iconLeft.setVisibility(View.VISIBLE);
        iconRight.setVisibility(View.GONE);
        title_location.setVisibility(View.GONE);
    }

    //首页
    private void showTitleBarHome() {
        setWebViewHeight(height_webView);
        appBarLayout.setVisibility(View.VISIBLE);
        iconLeft.setImageResource(R.mipmap.ic_mark);
        setIconVisibility(View.VISIBLE);
    }

    private void setIconVisibility(int visible) {
        iconLeft.setVisibility(visible);
        iconRight.setVisibility(visible);
        title_location.setVisibility(visible);
    }

    public void setWebViewHeight(int height){

//        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) webView.getLayoutParams();
//        layoutParams.height=height;
//        webView.setLayoutParams(layoutParams);
    }

    /**
     * 自定义WebChromeClient
     */
    private WebChromeClient mChromeClient = new WebChromeClient() {
        @Override
        public void onReceivedTitle(WebView view, String title) {
            title_center.setText(title);
        }
    };

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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.icon_left:
                onBackPressed();
                break;
        }
    }
}
