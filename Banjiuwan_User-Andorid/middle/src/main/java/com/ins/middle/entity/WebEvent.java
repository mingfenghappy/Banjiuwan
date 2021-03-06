package com.ins.middle.entity;

/**
 * author 边凌
 * date 2017/3/7 14:39
 * desc ${用于被EventBus发送的和WebView相关的事件}
 */

public enum WebEvent {
    /*主页跳转购物车*/
    jumpToCarTab,
    /*刷新界面*/
    shouldRefresh,
    /*支付成功*/
    paySuccess,
    /*支付失败*/
    payFailed,
    /*支付取消*/
    payCanceled,
    /*重新开始定位*/
    reLocation,
    /*结束非主页的页面*/
    finishActivity,
    /*厨师端登陆成功(用于解决一个bug)*/
    loginSuccess_chef,
    /*单独关闭下单页面*/
    finishAddOrder,
    /*传递couldOrder*/
    receivedCouldOrder,
    /*刷新除开CommonWebActivity*/
    shouldRefreshExceptCommonWeb
}
