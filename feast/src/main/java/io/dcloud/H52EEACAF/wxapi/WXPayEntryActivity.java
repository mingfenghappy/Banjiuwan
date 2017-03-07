package io.dcloud.H52EEACAF.wxapi;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.reflect.TypeToken;
import com.ins.feast.R;
import com.ins.middle.common.AppData;
import com.ins.middle.common.CommonNet;
import com.ins.middle.ui.activity.BaseAppCompatActivity;
import com.ins.feast.ui.dialog.DialogLoading;
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

import paytest.ins.com.library_alipay.PayResult;
import paytest.ins.com.library_alipay.SignUtils;

//type:0 支付定金 1：支付定金成功 2：支付尾款 3：支付尾款成功
public class WXPayEntryActivity extends BaseAppCompatActivity implements View.OnClickListener, IWXAPIEventHandler {

    //微信支付appId
//    public static String appid = "wxa3fb670d7394832e";
    public static String appid = "wx39b6f80d336ca5e4";
    private IWXAPI api;


    private Button btn_pay_zhifubao;
    private Button btn_pay_weixin;


    private int type;
    private int payType;    //支付方式 0:支付宝 1:微信 2:余额支付
    private int orderId;

    private DialogLoading dialogLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        setToolbar(false);

        //微信初始化
        api = WXAPIFactory.createWXAPI(this, appid);
        api.registerApp(appid);
        api.handleIntent(getIntent(), this);

        initBase();
        initView();
        initCtrl();
        initData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialogLoading != null) dialogLoading.dismiss();
    }

    private void initBase() {
        dialogLoading = new DialogLoading(this);
    }

    private void initView() {
        btn_pay_zhifubao = (Button) findViewById(R.id.btn_pay_zhifubao);
        btn_pay_weixin = (Button) findViewById(R.id.btn_pay_weixin);
        btn_pay_zhifubao.setOnClickListener(this);
        btn_pay_weixin.setOnClickListener(this);
    }

    private void initData() {
//        netGetPayData();
    }

    private void initCtrl() {
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_pay_zhifubao:
                netPayZhifubao(orderId, 0);
                break;
            case R.id.btn_pay_weixin:
                netPayWeixin(orderId, 0);
                break;
        }
    }


    /**
     * 支付成功后的业务逻辑
     */
    private void paySuccess() {
        Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
    }

    /**
     * 支付宝支付
     */
    public void netPayZhifubao(int orderId, final int flag) {
        RequestParams params = new RequestParams(AppData.Url.sign);
        params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("orderId", orderId + "");
        params.addBodyParameter("flag", flag + "");
        CommonNet.samplepost(params, new TypeToken<LinkedHashMap<String, String>>() {
        }.getType(), new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                Map<String, String> map = (LinkedHashMap<String, String>) pojo;

                final String orderInfo = SignUtils.getOrderInfo(map);
                Log.e("liao", "orderInfo:" + orderInfo);

                Runnable payRunnable = new Runnable() {

                    @Override
                    public void run() {
                        // 构造PayTask 对象
                        PayTask alipay = new PayTask(WXPayEntryActivity.this);
                        // 调用支付接口，获取支付结果
                        String result = alipay.pay(orderInfo, true);

                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                };

                // 必须异步调用
                Thread payThread = new Thread(payRunnable);
                payThread.start();
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(WXPayEntryActivity.this, text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void netStart(int status) {
                btn_pay_zhifubao.setEnabled(false);
            }
        });
    }

    /**
     * 支付宝支付回调
     */
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(WXPayEntryActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        paySuccess();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(WXPayEntryActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(WXPayEntryActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
                default:
                    break;
            }
            btn_pay_zhifubao.setEnabled(true);
        }

        ;
    };

    /**
     * 微信支付
     */
    public void netPayWeixin(int orderId, final int flag) {
        if (!api.isWXAppInstalled()) {
            Toast.makeText(this, "您还没有安装微信，请先安装微信在使用微信支付", Toast.LENGTH_SHORT).show();
            return;
        }
        RequestParams params = new RequestParams(AppData.Url.signWeixin);
        params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("orderId", orderId + "");
        params.addBodyParameter("flag", flag + "");
        params.addBodyParameter("ip", "101.201.222.161");
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

                            api.sendReq(req);
                        } else {
                            Log.d("PAY_GET", "返回错误" + map.get("retmsg"));
                            Toast.makeText(WXPayEntryActivity.this, "返回错误" + map.get("retmsg"), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d("PAY_GET", "服务器请求错误");
                        Toast.makeText(WXPayEntryActivity.this, "服务器请求错误", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("PAY_GET", "异常：" + e.getMessage());
                    Toast.makeText(WXPayEntryActivity.this, "异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(WXPayEntryActivity.this, text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void netStart(int status) {
                btn_pay_weixin.setEnabled(false);
            }
        });
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
            switch (resp.errCode) {
                case 0:
                    //成功
                    paySuccess();
                    break;
                case -1:
                    //失败
                    Toast.makeText(this, "支付失败", Toast.LENGTH_SHORT).show();
                    break;
                case -2:
                    //用户取消
                    //Toast.makeText(this, "用户取消：-2", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
        btn_pay_weixin.setEnabled(true);
    }
}
