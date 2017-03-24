package com.ins.feast.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.tencent.smtt.sdk.WebView;

import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.ins.baidumapsdk.Locationer;
import com.ins.feast.R;
import com.ins.feast.common.JSFunctionUrl;
import com.ins.feast.entity.Position;
import com.ins.feast.ui.helper.CommonWebTitleHelper;
import com.ins.feast.web.CommonWebJSInterface;
import com.ins.middle.base.BaseWebChromeClient;
import com.ins.middle.base.BaseWebViewClient;
import com.ins.middle.base.WebSettingHelper;
import com.ins.middle.common.AppData;
import com.ins.middle.entity.WebEvent;
import com.ins.middle.helper.CommonAppHelper;
import com.ins.middle.ui.activity.BaseBackActivity;
import com.ins.middle.utils.ParamUtil;
import com.sobey.common.utils.ClickUtils;
import com.sobey.common.utils.L;
import com.sobey.common.utils.PermissionsUtil;
import com.sobey.common.utils.PhoneUtils;
import com.sobey.common.utils.StrUtils;
import com.sobey.common.utils.UrlUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

//com.tencent.smtt.export.external.interfaces
//com.tencent.smtt.export.external.interfaces

public class CommonWebActivity extends BaseBackActivity {

    private Locationer locationer;

    private final static String KEY_URL = "urlOfThisPage";
    private String urlOfThisPage;
    private WebView webView;
    private CommonWebTitleHelper titleHelper;
    private BaseWebViewClient webViewClient;
    private BaseWebChromeClient webChromeClient;

    public static void start(Context context, String url) {
        //防止重复点击过快打开页面
        if (ClickUtils.isFastDoubleClick()) return;

        Intent starter = new Intent(context, CommonWebActivity.class);
        starter.putExtra(KEY_URL, url);
        context.startActivity(starter);
        //手动设置进场动画
        ((Activity) context).overridePendingTransition(R.anim.translate_enter, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim_jump_web);
        setNeedDoubleClickExit(false);
        setEventBusSupport();
        initSetting();
        findView();
        initWebViewSetting();
    }

    private void initWebViewSetting() {
        setWebViewLifeCycleSupport(webView);
        webClient();
        webSetting();
        webView.loadUrl(urlOfThisPage);
        overrideUrlLoadingFirst(webView, urlOfThisPage);    //特殊的调用：见方法注释
        //禁止WebView长按编辑
        CommonAppHelper.setWebViewNoLongClick(webView);
    }

    private void webClient() {
        webChromeClient = new BaseWebChromeClient(this) {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                titleHelper.setTitleText(title);
            }
        };
        webViewClient = new BaseWebViewClient(webView) {
            @Override
            public boolean shouldOverrideUrlLoading(final WebView webView, String url) {
                if (AppData.Config.showTestToast)
                    Toast.makeText(webView.getContext(), "捕获链接:" + url, Toast.LENGTH_LONG).show();
                overrideUrlLoading(webView, url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                overrideUrlLoadingFinish(view, url);
            }
        };
        webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(webViewClient);
    }

    private void webSetting() {
        webView.addJavascriptInterface(new CommonWebJSInterface(this), JS_BRIDGE_NAME);
        WebSettingHelper.newInstance(webView).commonSetting();
    }

    private void findView() {
        webView = (WebView) findViewById(R.id.webView);
    }

    private void initSetting() {
        urlOfThisPage = getIntent().getStringExtra(KEY_URL);
        L.d(urlOfThisPage);
        titleHelper = new CommonWebTitleHelper(this);
        titleHelper.handleTitleWithUrl(urlOfThisPage);
    }

    /**
     * 接受地理位置选择的结果，新增地址页面需要接受定位参数，因为定位功能由app提供，web不做处理
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceivePosition(Position position) {
        String address = position.getKey();
        LatLng latLng = position.getLatLng();
//        Toast.makeText(CommonWebActivity.this, address, Toast.LENGTH_SHORT).show();
        webView.loadUrl(JSFunctionUrl.setAddress(address, latLng.latitude + "", latLng.longitude + ""));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onWebEvent(WebEvent event) {
        switch (event) {
            case shouldRefresh:
                webView.reload();
                break;
            case payCanceled:
            case payFailed:
                //客户端处理了回调，不再需要web端处理
                //webView.loadUrl(JSFunctionUrl.PAY_FAILED);
                break;
            case paySuccess:
                //客户端处理了回调，不再需要web端处理
                //webView.loadUrl(JSFunctionUrl.PAY_SUCCESS);
                break;
            case finishActivity:
                finish();
                break;
            case finishAddOrder:
                //如果当前是下单页面，则关闭当前页
                Log.e("liao", urlOfThisPage + "========" + AppData.Url.addOrder);
                if (UrlUtil.matchUrl(urlOfThisPage, AppData.Url.addOrder)) {
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        webChromeClient.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webViewClient != null) webViewClient.destroy();
        if (locationer != null) locationer.stopLocation();
    }

    @Override
    public void finish() {
        super.finish();
        //手动设置出场动画
        overridePendingTransition(0, R.anim.translate_exit);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////URL拦截处理//////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * TODO：拦截Url 在加载完成后的点击等事件，但是无法拦截第一次加载，即调用webView.loadUrl()加载的链接无法被拦截
     */
    private void overrideUrlLoading(final WebView webView, String url) {
        //打电话
        if (url.startsWith("tel:")) {
            PhoneUtils.call(CommonWebActivity.this, url);
            return;
        }

        //加载链接和当前链接一样：刷新操作
        if (TextUtils.equals(webView.getUrl(), url)) {
            webView.loadUrl(url);
            return;
        }

        //这里不需要再拦截了（web端逻辑改了）
//        //如果是选择更多地址页面（启动源生页面）
//        if (UrlUtil.matchUrl(url, AppData.Url.moreAddress)) {
//            ChooseLocationActivity.start(CommonWebActivity.this);
//            return;
//        }

        //根据pageType决定其打开页面的方式
        //pageType:0 打开新activity 显示页面
        //pageType:1 在当前activity 显示页面
        //pageType:2 关闭当前页面并刷新上一级页面
        //pageType:3 打开新activity 显示页面并刷新当前页面 (注销)
        //pageType:4 在当前activity 显示页面并刷新上一页 (添加订单)
        //pageType:5 在当前activity 关闭所有二级页面并跳转到首页购物车 (商品结算)
        int pageType = ParamUtil.getParamInt(url, "pageType", 0);
        if (pageType == 1) {
            webView.loadUrl(url);
            urlOfThisPage = url;
        } else if (pageType == 2) {
            finish();
            EventBus.getDefault().post(WebEvent.shouldRefresh);
        } else if (pageType == 3) {
            EventBus.getDefault().post(WebEvent.shouldRefresh);
            CommonWebActivity.start(CommonWebActivity.this, url);
        } else if (pageType == 4) {
            webView.loadUrl(url);
            urlOfThisPage = url;
            EventBus.getDefault().post(WebEvent.shouldRefresh);
        } else if (pageType == 5) {
            EventBus.getDefault().post(WebEvent.jumpToCarTab);
            EventBus.getDefault().post(WebEvent.finishActivity);
        } else {
            CommonWebActivity.start(CommonWebActivity.this, url);
        }

        return;
    }

    /**
     * TODO：为了捕获webView.loadUrl()加载的链接，在调用loadUrl之前手动调用该方法（当前页面只有onCreate中有一处）
     */
    private void overrideUrlLoadingFirst(final WebView webView, String url) {
        //需求变动：不再需要右滑退出的功能了，所以二级页面全部取消滑动退出功能
        //如果是我的订单页面 取消右滑返回功能（滑动冲突）
//        if (UrlUtil.matchUrl(url, AppData.Url.myOrder)) {
            getSwipeBackLayout().setEnablePullToBack(false);
//        }
    }

    /**
     * TODO：页面加载完成后，判断url作特殊处理（web页面在加载过程中调用其js方法不会成功，需要加载完成后调用）
     */
    private void overrideUrlLoadingFinish(final WebView webView, String url) {
        //如果是新增地址页面，注册定位管理器进行定位（动态注册，不需要定位功能的页面不注册）
        if (UrlUtil.matchUrl(url, AppData.Url.addAddress)) {
            locationer = new Locationer(CommonWebActivity.this);
            locationer.setCallback(new Locationer.LocationCallback() {
                @Override
                public void onLocation(LatLng latLng, String city, String address, boolean isFirst) {
                    if (StrUtils.isEmpty(address)) {
                        Toast.makeText(CommonWebActivity.this, "定位失败，请到信号较好的地方稍后再试", Toast.LENGTH_SHORT).show();
                    } else {
                        address = StrUtils.subFirstChart(address, "中国");
                        webView.loadUrl(JSFunctionUrl.setAddress(address, latLng.latitude + "", latLng.longitude + ""));
                        locationer.stopLocation();  //定位成功后马上释放
                    }
                }
            });
            if (PermissionsUtil.requsetLocation(this, null)) {
                locationer.startlocation();
            }
        }
    }
}
