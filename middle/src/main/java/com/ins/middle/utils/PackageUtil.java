package com.ins.middle.utils;

import android.content.Context;
import android.content.Intent;

import com.sobey.common.utils.ApplicationHelp;

/**
 * Created by Administrator on 2016/11/4.
 */

public class PackageUtil {
    /**
     * 判断当前是车主端还是乘客端
     */
    public static boolean isClient() {
        Context context = ApplicationHelp.getApplicationContext();
        String packageName = context.getPackageName();
        if ("io.dcloud.H52EEACAF".equals(packageName)) {
            //客户端
            return true;
        } else {
            //厨师端
            return false;
        }
    }

    /**
     * 判断当前是车主端还是乘客端，然后起调当前端的对应activity
     */
    public static Intent getSmIntent(String activity) {
        String uri;
        if (PackageUtil.isClient()) {
            uri = "feast." + activity;
        } else {
            uri = "chef." + activity;
        }
        return new Intent(uri);
    }
}
