package com.ins.feast.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ins.feast.R;
import com.ins.middle.ui.activity.BaseAppCompatActivity;

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