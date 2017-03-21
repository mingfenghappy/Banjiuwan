package com.ins.feast.ui.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.google.gson.reflect.TypeToken;
import com.ins.middle.common.AppData;
import com.ins.middle.common.CommonNet;
import com.sobey.common.utils.StrUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.xutils.http.RequestParams;

import java.util.LinkedHashMap;
import java.util.Map;

import io.dcloud.H52EEACAF.wxapi.WXPayEntryActivity;
import paytest.ins.com.library_alipay.PayResult;
import paytest.ins.com.library_alipay.SignUtils;

/**
 * Created by Administrator on 2017/3/7.
 */

public class PayHelper {

    /*微信支付*/
    public final static int PAY_WEIXIN = 0;
    /*支付宝支付*/
    public final static int PAY_ALIPAY = 1;
    public static String appid = "wxedb9473980fbecfe";
    private Activity activity;
    private IWXAPI api;

    public PayHelper(Activity activity) {
        this.activity = activity;
        //微信初始化
        api = WXAPIFactory.createWXAPI(activity, appid);
        api.registerApp(appid);
    }

    ///////////////////////////////
    //////// 对外方法
    ///////////////////////////////

    /**
     * 支付宝支付
     */
    public void netPayZhifubao(int orderId, String token) {
        RequestParams params = new RequestParams(AppData.Url.sign);
        params.addHeader("token", token);
        params.addBodyParameter("orderId", orderId + "");
        startPayZhifubao(params);
    }

    /**
     * 支付宝充值
     */
    public void netRechargeZhifubao(int rechargeId, String token) {
        RequestParams params = new RequestParams(AppData.Url.recharge);
        params.addHeader("token", token);
        params.addBodyParameter("rechargeId", rechargeId + "");
        startPayZhifubao(params);
    }

    /**
     * 微信支付
     */
    public void netPayWeixin(int orderId, String token) {
        if (!api.isWXAppInstalled()) {
            Toast.makeText(activity, "您还没有安装微信，请先安装微信在使用微信支付", Toast.LENGTH_SHORT).show();
            return;
        }
        RequestParams params = new RequestParams(AppData.Url.signWeixin);
        params.addHeader("token", token);
        params.addBodyParameter("orderId", orderId + "");
        params.addBodyParameter("flag", "0");
        //params.addBodyParameter("ip", "101.201.222.161");
        startPayWeixin(params);
    }

    /**
     * 微信充值
     */
    public void netRechargeWeixin(int rechargeId, String token) {
        if (!api.isWXAppInstalled()) {
            Toast.makeText(activity, "您还没有安装微信，请先安装微信在使用微信支付", Toast.LENGTH_SHORT).show();
            return;
        }
        RequestParams params = new RequestParams(AppData.Url.rechargeWeixin);
        params.addHeader("token", token);
        params.addBodyParameter("rechargeId", rechargeId + "");
        //params.addBodyParameter("ip", "101.201.222.161");
        startPayWeixin(params);
    }

    ///////////////////////////////
    //////// 请求支付参数并发起支付
    ///////////////////////////////

    private void startPayZhifubao(RequestParams params){
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
                        PayTask alipay = new PayTask(activity);
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
                Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void netStart(int status) {
                onPayStart();
            }
        });
    }

    private void startPayWeixin(RequestParams params){
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
                            Toast.makeText(activity, "返回错误" + map.get("retmsg"), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d("PAY_GET", "服务器请求错误");
                        Toast.makeText(activity, "服务器请求错误", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("PAY_GET", "异常：" + e.getMessage());
                    Toast.makeText(activity, "异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void netStart(int status) {
                onPayStart();
            }
        });
    }

    ///////////////////////////////
    //////// 支付回调过程
    ///////////////////////////////

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
                    //文档查询：https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.GpzTjL&treeId=204&articleId=105302&docType=1
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(activity, "支付成功", Toast.LENGTH_SHORT).show();
                        onPaySuccess();
                    } else {

                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(activity, "支付结果确认中", Toast.LENGTH_SHORT).show();
                            onPayFail();
                        } else if (TextUtils.equals(resultStatus, "6001")) {
                            // 用户主动取消支付
                            Toast.makeText(activity, "用户取消了支付", Toast.LENGTH_SHORT).show();
                            onPayCancel();
                        } else {
                            //其他值就可以判断为支付失败
                            onPayFail();
                            Toast.makeText(activity, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    ///////////////////////////////
    //////// 支付状态处理
    ///////////////////////////////

    private void onPayStart() {
    }

    private void onPaySuccess() {
        Intent intent = new Intent(activity, WXPayEntryActivity.class);
        intent.putExtra("type", 0);
        activity.startActivity(intent);
    }

    private void onPayFail() {
        Intent intent = new Intent(activity, WXPayEntryActivity.class);
        intent.putExtra("type", -1);
        activity.startActivity(intent);
    }

    private void onPayCancel() {
        Intent intent = new Intent(activity, WXPayEntryActivity.class);
        intent.putExtra("type", -2);
        activity.startActivity(intent);
    }

}
