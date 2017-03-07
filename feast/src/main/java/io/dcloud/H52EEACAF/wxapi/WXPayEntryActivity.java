package io.dcloud.H52EEACAF.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.ins.feast.R;
import com.ins.feast.ui.activity.PayHelper;
import com.ins.middle.ui.activity.BaseAppCompatActivity;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

//type:0 支付定金 1：支付定金成功 2：支付尾款 3：支付尾款成功
public class WXPayEntryActivity extends BaseAppCompatActivity implements IWXAPIEventHandler {

    private IWXAPI api;

    private TextView text_pay_result;

    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        setToolbar(false);

        initBase();
        initView();
        initData();

        //微信初始化
        api = WXAPIFactory.createWXAPI(this, PayHelper.appid);
        //api.registerApp(appid);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    private void initBase() {
        if (getIntent().hasExtra("type")) {
            type = getIntent().getIntExtra("type", -1);
        }
    }

    private void initView() {
        text_pay_result = (TextView) findViewById(R.id.text_pay_result);
    }

    private void initData() {
        setPayData();
    }

    private void setPayData() {
        switch (type){
            case 0:
                text_pay_result.setText("支付成功");
                break;
            case -1:
                text_pay_result.setText("支付失败");
                break;
            case -2:
                text_pay_result.setText("用户取消了支付");
                break;
        }

    }

    /**
     * 微信支付回调
     */
    @Override
    public void onReq(BaseReq baseReq) {
    }

    /**
     * 微信支付回调
     */
    @Override
    public void onResp(BaseResp resp) {
        Log.d("weixin", "onPayFinish, errCode = " + resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            type = resp.errCode;
            switch (resp.errCode) {
                case 0:
                    //成功
                    setPayData();
                    break;
                case -1:
                    //失败
                    setPayData();
                    break;
                case -2:
                    //用户取消
                    setPayData();
                    break;
            }
        }
    }
}
