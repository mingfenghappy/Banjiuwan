package com.ins.feast.common;

/**
 * author 边凌
 * date 2017/3/8 11:11
 * desc ${存储被调用的js页面方法url}
 */

public class JSFunctionUrl {
    public final static String PAY_SUCCESS = "javascript:payCallback('" + 1 + "')";
    public final static String PAY_FAILED = "javascript:payCallback('" + 0 + "')";

    public static String setAddress(String address, String lat, String lng) {
        return "javascript:setAddress('" + address + "','" + lat + "','" + lng + "')";
    }
    //不再有这个方法了 ，token由登录时接收
//    public static String getToken() {
//        return "javascript:getToken()";
//    }
}
