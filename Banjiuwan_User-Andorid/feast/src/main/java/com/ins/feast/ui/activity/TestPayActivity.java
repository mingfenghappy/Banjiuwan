package com.ins.feast.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.ins.feast.R;
import com.ins.feast.ui.helper.PayHelper;
import com.ins.middle.common.AppData;
import com.ins.middle.common.CommonNet;
import com.ins.middle.ui.activity.BaseFeastActivity;
import com.sobey.common.utils.StrUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.xutils.http.RequestParams;

import java.util.LinkedHashMap;
import java.util.Map;

public class TestPayActivity extends BaseFeastActivity implements View.OnClickListener {

    private PayHelper payHelper;

    private int orderId;
    private String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testpay);
        setToolbar(false);

        api = WXAPIFactory.createWXAPI(this, appid);
        api.registerApp(appid);


        initBase();
        initView();
        initCtrl();
        initData();
    }

    private void initBase() {
//        payHelper = new PayHelper(this);
    }


    private void initView() {

        text_log = (TextView) findViewById(R.id.text_log);
        Button appayBtn = (Button) findViewById(R.id.appay_btn);
        appayBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                netPay("387cda4f88f84ed3a6edfc128dac98d2",221);
            }
        });

//        findViewById(R.id.btn_pay_zhifubao).setOnClickListener(this);
//        findViewById(R.id.btn_pay_weixin).setOnClickListener(this);
//        findViewById(R.id.btn_recharge_zhifubao).setOnClickListener(this);
//        findViewById(R.id.btn_recharge_weixin).setOnClickListener(this);
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
            case R.id.btn_recharge_zhifubao:
                //这里测试填写死数据 rechargeId:3 touken:a32bbb9732f94c6589bc973e0fdd00a6
                payHelper.netRechargeZhifubao(3, "a32bbb9732f94c6589bc973e0fdd00a6");
                break;
            case R.id.btn_recharge_weixin:
                payHelper.netRechargeWeixin(3, "a32bbb9732f94c6589bc973e0fdd00a6");
                break;
        }
    }


    public static String appid = "wxedb9473980fbecfe";

    private IWXAPI api;

    private TextView text_log;

    public void netPay(String token, int orderId) {
        RequestParams params = new RequestParams(AppData.Url.signWeixin);
        params.addHeader("token", token);
        params.addBodyParameter("orderId", orderId + "");
        CommonNet.samplepost(params, new TypeToken<LinkedHashMap<String, String>>() {
        }.getType(), new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                Map<String, String> map = (LinkedHashMap<String, String>) pojo;

                try {
                    if (!StrUtils.isEmpty(map)) {
                        if (!map.containsKey("retcode")) {
                            final PayReq req = new PayReq();
                            req.appId = appid;
                            req.partnerId = map.get("partnerid");
                            req.prepayId = map.get("prepayid");
                            req.nonceStr = map.get("noncestr");
                            req.timeStamp = map.get("timestamp");
                            req.packageValue = map.get("package");
                            req.sign = map.get("sign");

                            String log = "req.appId:\n" + req.appId +
                                    "\nreq.partnerId:\n" + req.partnerId +
                                    "\nreq.prepayId:\n" + req.prepayId +
                                    "\nreq.nonceStr:\n" + req.nonceStr +
                                    "\nreq.timeStamp:\n" + req.timeStamp +
                                    "\nreq.packageValue:\n" + req.packageValue +
                                    "\nreq.sign:\n" + req.sign;
                            Log.e("pay", log);

                            text_log.setText(log);

                            api.sendReq(req);
                        } else {
                            Log.d("PAY_GET", "返回错误" + map.get("retmsg"));
                            Toast.makeText(TestPayActivity.this, "返回错误" + map.get("retmsg"), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d("PAY_GET", "服务器请求错误");
                        Toast.makeText(TestPayActivity.this, "服务器请求错误", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("PAY_GET", "异常：" + e.getMessage());
                    Toast.makeText(TestPayActivity.this, "异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(TestPayActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }
}