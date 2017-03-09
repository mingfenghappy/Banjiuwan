package com.ins.feast.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ins.feast.R;
import com.ins.feast.ui.helper.PayHelper;
import com.ins.middle.ui.activity.BaseAppCompatActivity;

public class TestPayActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private PayHelper payHelper;

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
        findViewById(R.id.btn_pay_zhifubao).setOnClickListener(this);
        findViewById(R.id.btn_pay_weixin).setOnClickListener(this);
        findViewById(R.id.btn_recharge_zhifubao).setOnClickListener(this);
        findViewById(R.id.btn_recharge_weixin).setOnClickListener(this);
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
}