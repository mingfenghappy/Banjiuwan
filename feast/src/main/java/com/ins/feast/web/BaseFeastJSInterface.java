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
    private Activity activity;
    private PayHelper helper;

    public BaseFeastJSInterface(Activity activity) {
        this.activity = activity;
        helper = new PayHelper(activity);
    }

    protected Activity getActivity() {
        return activity;
    }

    @JavascriptInterface
    public void goPayment(String orderId, String token,int payMethod) {
        try {
            L.d("tryPayMethod");
            int orderIdInt = Integer.parseInt(orderId);
            switch (payMethod) {
                case PayHelper.PAY_ALIPAY:
                    helper.netPayZhifubao(orderIdInt, token);
                    break;
                case PayHelper.PAY_WEIXIN:
                    helper.netPayWeixin(orderIdInt, token);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
