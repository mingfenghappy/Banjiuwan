package com.ins.middle.utils;

import android.net.Uri;

import com.sobey.common.utils.StrUtils;

/**
 * Created by Administrator on 2017/3/14.
 * 从url中获取参数的工具类
 */

public class ParamUtil {

    //通过参数名获取int型参数，处理默认值 0
    public static int getParamInt(String url, String name) {
        return getParamInt(url, name, 0);
    }

    //通过参数名获取int型参数，可设置默认值
    public static int getParamInt(String url, String name, int defult) {
        int para;
        String paraStr = Uri.parse(url).getQueryParameter(name);
        if (StrUtils.isEmpty(paraStr)) {
            para = defult;
        } else {
            para = Integer.parseInt(paraStr);
        }
        return para;
    }
}
