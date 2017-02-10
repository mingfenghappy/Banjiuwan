package com.sobey.common.utils;

/**
 * Created by Administrator on 2016/12/5.
 */

public class ClickUtils {
    //最短间隔时间
    private static int clipTime = 800;
    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < clipTime) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
