package com.ins.feast.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.google.gson.reflect.TypeToken;
import com.ins.baidumapsdk.BaiduMapUtil;
import com.ins.feast.R;
import com.ins.feast.entity.AreaData;
import com.ins.feast.entity.Position;
import com.ins.feast.entity.SaleDialogEntity;
import com.ins.feast.entity.Tabs;
import com.ins.feast.ui.dialog.DialogNotice;
import com.ins.feast.ui.dialog.DialogSale;
import com.ins.feast.ui.helper.HomeTitleHelper;
import com.ins.feast.utils.AppHelper;
import com.ins.feast.web.HomeActivityWebChromeClient;
import com.ins.feast.web.HomeActivityWebViewClient;
import com.ins.feast.web.HomeJSInterface;
import com.ins.middle.base.WebSettingHelper;
import com.ins.middle.common.AppData;
import com.ins.middle.common.CommonNet;
import com.ins.middle.entity.WebEvent;
import com.ins.middle.helper.BackCheckedHelper;
import com.ins.middle.helper.CommonAppHelper;
import com.shelwee.update.UpdateHelper;
import com.sobey.common.utils.PermissionsUtil;
import com.sobey.common.utils.StrUtils;
import com.tencent.smtt.sdk.WebView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends BaseMapActivity implements
        RadioGroup.OnCheckedChangeListener {

    public WebView webView;
    //标题栏定位Tv
    private TextView title_location;

    private HomeTitleHelper homeTitleHelper;
    private HomeActivityWebChromeClient webChromeClient;
    private HomeActivityWebViewClient webViewClient;
    private UpdateHelper updateHelper;

    //菜品配置
    public AreaData areaData;
    //用户当前位置
    public LatLng nowLatlng;

    public DialogNotice dialogNotice;
    private DialogSale dialogSale;

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
        initData();
        initWebViewSetting();
    }

    private void initData() {
        //请求菜品配置（地理围栏）
        netGetCategoryConfig();
        netGetSale();
    }

    private void initBase() {
        //双击返回键退出
        setNeedDoubleClickExit(true);
        //检查并申请权限
        PermissionsUtil.checkAndRequestPermissions(this);
        //检查更新
        updateHelper = new UpdateHelper.Builder(this).checkUrl(AppData.Url.version_feast).isHintNewVersion(false).build();
        updateHelper.check();
        //getLocOption().setScanSpan(0);//设置只定位一次
        startLocation();

        dialogNotice = new DialogNotice(this);
        dialogSale = new DialogSale(this);
        dialogSale.setOnDialogClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaleDialogEntity saleEntity = dialogSale.getSaleEntity();
                if (saleEntity != null) {
                    CommonWebActivity.start(HomeActivity.this, AppData.Url.news + "?isOpen=1");
                    dialogSale.hide();
                }
            }
        });
    }

    private void initView() {
        title_location = (TextView) findViewById(R.id.title_location);
        webView = (WebView) findViewById(R.id.webView);
        RadioGroup tabRg = (RadioGroup) findViewById(R.id.radioGroup);
        tabRg.setOnCheckedChangeListener(this);
        homeTitleHelper = new HomeTitleHelper(this);
    }

    /**
     * WebView配置
     */
    private void initWebViewSetting() {
        setWebViewLifeCycleSupport(webView);

        webViewClient = new HomeActivityWebViewClient(this);
        webChromeClient = new HomeActivityWebChromeClient(this);

        HomeJSInterface homeJsInterface = new HomeJSInterface(this, webView);
        WebSettingHelper.newInstance(webView).commonSetting()
                .setWebViewClient(webViewClient)
                .setWebChromeClient(webChromeClient)
                .addJavaScriptInterface(homeJsInterface, JS_BRIDGE_NAME);

        Tabs.init();
        //首次进入页面把当前页存入url回退列表
        BackCheckedHelper.addBackUrl(AppData.Url.app_home);
        //禁止WebView长按编辑
        CommonAppHelper.setWebViewNoLongClick(webView);
        webView.loadUrl(AppData.Url.app_home);
//        webView.loadUrl("http://www.baidu.com");
        //禁止WebView长按编辑
        CommonAppHelper.setWebViewNoLongClick(webView);
    }

    @Override
    public void onLocation(LatLng latLng, String city, String district, boolean isFirst) {
        if (!BaiduMapUtil.isLatlngEmpty(latLng)) {
            this.nowLatlng = latLng;
        } else {
            this.nowLatlng = null;
            Toast.makeText(this, "定位失败", Toast.LENGTH_SHORT).show();
        }
        //新需求：获取语义化的位置信息
        title_location.setText(getLocationDescribe());
        //只定位一次
        stopLocation();
    }

    @Override
    public void onBackPressed() {
        String backUrl = BackCheckedHelper.getBackUrl();
        if (!StrUtils.isEmpty(backUrl)) {
            RadioButton radioButton = Tabs.getRadioButtonByUrl(this, backUrl);
            if (radioButton != null) radioButton.setChecked(true);
        } else {
            super.onBackPressed();
        }
    }

//    /**
//     * 根据返回到的页面的url改变tab栏选中的tab
//     */
//    private void selectTabByUrl(String url) {
//        notLoad = true;
//        int buttonId = 0;
//
//        for (Tabs tabs : Tabs.values()) {
//            if (url.contains(tabs.getUrlTag())) {
//                buttonId = tabs.getButtonId();
//            }
//        }
//
//        RadioButton radioButton = (RadioButton) findViewById(buttonId);
//        if (radioButton != null) {
//            radioButton.setChecked(true);
//            handleTitleByCheckedId(buttonId);
//        }
//    }

    @Override
    protected void onDestroy() {
        if (webViewClient != null) webViewClient.destroy();
        if (dialogNotice != null) dialogNotice.dismiss();
        if (dialogSale != null) dialogSale.dismiss();
        updateHelper.onDestory();
        super.onDestroy();
    }

    /**
     * 接受地理位置选择的结果
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceivePosition(Position position) {
        if (position.getType() == 1) {
            String key = position.getKey();
            title_location.setText(key);
            if (position.getLatLng() != null) {
                nowLatlng = position.getLatLng();
            }
        }
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
            if (!radioButton.isChecked()) {
                radioButton.setChecked(true);
            } else {
                webView.reload();
            }
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
        BackCheckedHelper.addBackUrl(Tabs.getUrlById(checkedId));
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

        if (webView != null && !TextUtils.isEmpty(url)) {
            webView.stopLoading();
            webView.loadUrl(url);
        }
    }

    //请求菜品配置（地理围栏）
    public void netGetCategoryConfig() {
        RequestParams params = new RequestParams(AppData.Url.getCategoryConfig);
        params.addHeader("token", AppData.App.getToken());
        CommonNet.samplepost(params, AreaData.class, new CommonNet.SampleNetHander() {
            @Override
            public void netGo(final int code, Object pojo, String text, Object obj) {
                if (pojo == null) netSetError(code, "接口异常");
                else {
                    areaData = (AreaData) pojo;
                    AppHelper.removeArea();
                    AppHelper.saveArea(areaData);
                    AppHelper.setLatlogEntity(areaData);
                }
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(HomeActivity.this, text, Toast.LENGTH_SHORT).show();
                areaData = AppHelper.getArea();
            }
        });
    }

    //请求推荐弹窗数据
    public void netGetSale() {
        RequestParams params = new RequestParams(AppData.Url.sale);
        params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("flag", "1");
        CommonNet.samplepost(params, new TypeToken<List<SaleDialogEntity>>() {
        }.getType(), new CommonNet.SampleNetHander() {
            @Override
            public void netGo(final int code, Object pojo, String text, Object obj) {
                if (pojo == null) netSetError(code, "接口异常");
                else {
                    List<SaleDialogEntity> saleEntitys = (ArrayList<SaleDialogEntity>) pojo;
                    if (!StrUtils.isEmpty(saleEntitys)) {
                        if (dialogSale != null) {
                            dialogSale.setSaleEntity(saleEntitys.get(0));
                            dialogSale.show();
                        }
                    }
                }
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(HomeActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}