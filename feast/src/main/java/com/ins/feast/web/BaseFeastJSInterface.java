package com.ins.feast.web;

import android.app.Activity;
import android.webkit.JavascriptInterface;

import com.ins.feast.ui.helper.PayHelper;
import com.ins.middle.base.BaseJSInterface;
import com.sobey.common.utils.L;

/**
 * author 边凌
 * date 2017/3/8 9:17
 * desc ${客户端的基类JSInterface，封装了被js调用的支付方法}
 */

public class BaseFeastJSInterface extends BaseJSInterface {
    private final static long interval = 300;
    private Activity activity;
    private PayHelper helper;
    private String lastOrderId;
    private MethodInvokeTime lastGoPayment_Alipay, lastGoPayment_Weixin;
    private MethodInvokeTime lastGoRecharge_Alipay, lastGoRecharge_Weixin;

    public BaseFeastJSInterface(Activity activity) {
        this.activity = activity;
        helper = new PayHelper(activity);
    }

    protected Activity getActivity() {
        return activity;
    }

    @JavascriptInterface
    public void goPayment(String orderId, String token, int payMethod) {
        goPayment(orderId, token, payMethod, 0);
    }

    /**
     * 支付
     */
    @JavascriptInterface
    public void goPayment(String orderId, String token, int payMethod, int type) {
        if (lastGoPayment_Alipay == null) {
            lastGoPayment_Alipay = new MethodInvokeTime();
            lastGoPayment_Weixin = new MethodInvokeTime();
        }
        try {
            L.d("tryGoPayment");
            int orderIdInt = Integer.parseInt(orderId);
            switch (payMethod) {
                case PayHelper.PAY_ALIPAY:
                    if (isFastClick(lastGoPayment_Alipay)) return;
                    if (type == 2) {
                        helper.netPayZhifubao(orderIdInt, token, type);
                    } else {
                        helper.netPayZhifubao(orderIdInt, token);
                    }
                    break;
                case PayHelper.PAY_WEIXIN:
                    if (isFastClick(lastGoPayment_Weixin)) return;
                    if (type == 2) {
                        helper.netPayWeixin(orderIdInt, token, type);
                    } else {
                        helper.netPayWeixin(orderIdInt, token);
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isFastClick(MethodInvokeTime methodInvokeTime) {
        if (methodInvokeTime.lastInvokeTime == 0) {
            methodInvokeTime.lastInvokeTime = System.currentTimeMillis();
        } else if (System.currentTimeMillis() - methodInvokeTime.lastInvokeTime < interval) {
            methodInvokeTime.lastInvokeTime = System.currentTimeMillis();
            return true;
        }
        return false;
    }

    /**
     * 充值
     */
    @JavascriptInterface
    public void goRecharge(String rechargeId, String token, int payMethod) {
        if (lastGoRecharge_Alipay == null) {
            lastGoRecharge_Alipay = new MethodInvokeTime();
            lastGoRecharge_Weixin = new MethodInvokeTime();
        }
        try {
            L.d("tryGoRecharge");
            int rechargeIdInt = Integer.parseInt(rechargeId);
            switch (payMethod) {
                case PayHelper.PAY_ALIPAY:
                    if (isFastClick(lastGoRecharge_Alipay)) return;
                    helper.netRechargeZhifubao(rechargeIdInt, token);
                    break;
                case PayHelper.PAY_WEIXIN:
                    if (isFastClick(lastGoRecharge_Weixin)) return;
                    helper.netRechargeWeixin(rechargeIdInt, token);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private class MethodInvokeTime {
        private long lastInvokeTime;
    }
}
