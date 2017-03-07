package com.ins.feast.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.reflect.TypeToken;
import com.ins.feast.R;
import com.ins.feast.ui.dialog.DialogLoading;
import com.ins.middle.common.AppData;
import com.ins.middle.common.CommonNet;
import com.ins.middle.ui.activity.BaseAppCompatActivity;
import com.ins.middle.ui.activity.BaseBackActivity;
import com.sobey.common.helper.WebPickerChromeClient;
import com.sobey.common.helper.WebPickerHelper;
import com.sobey.common.utils.StrUtils;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.xutils.http.RequestParams;

import java.util.LinkedHashMap;
import java.util.Map;

import io.dcloud.H52EEACAF.wxapi.WXPayEntryActivity;
import paytest.ins.com.library_alipay.PayResult;
import paytest.ins.com.library_alipay.SignUtils;

public class TestPayActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private PayHelper payHelper;

    private Button btn_pay_zhifubao;
    private Button btn_pay_weixin;

    private int orderId;
    private String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testpay);
        setToolbar(false);


        initBase();
        initView();
        initCtrl();
        initData();
    }

    private void initBase() {
        payHelper = new PayHelper(this);
    }


    private void initView() {
        btn_pay_zhifubao = (Button) findViewById(R.id.btn_pay_zhifubao);
        btn_pay_weixin = (Button) findViewById(R.id.btn_pay_weixin);
        btn_pay_zhifubao.setOnClickListener(this);
        btn_pay_weixin.setOnClickListener(this);
    }

    private void initData() {
    }

    private void initCtrl() {
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_pay_zhifubao:
                payHelper.netPayZhifubao(orderId, token);
                break;
            case R.id.btn_pay_weixin:
                payHelper.netPayWeixin(orderId, token);
                break;
        }
    }
}