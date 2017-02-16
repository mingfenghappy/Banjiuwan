package com.sobey.common.utils;

import android.text.TextUtils;
import android.util.Log;

/**
 * LOG工具类
 */
public class L {

    private static String TAG = "LOG";

    private static boolean DEBUG = true;

    public static void setDEBUG(boolean DEBUG) {
        L.DEBUG = DEBUG;
    }

    private L() {
        throw new UnsupportedOperationException();
    }

    public static void i(String tag, String message) {
        if (!DEBUG) {
            return;
        }
        Log.i(tag, message);
    }

    public static void i(String message) {
        i(TAG, message);
    }

    public static void e(String tag, String message) {
        if (!DEBUG) {
            return;
        }
        Log.e(tag, message);
    }

    public static void e(String message) {
        e(TAG, message);
    }

    public static void d(String tag, Object message) {
        if (!DEBUG) {
            return;
        }

        if (message != null && !TextUtils.isEmpty(message.toString())) {
            Log.d(tag, message.toString());
        } else if (message == null) {
            Log.e(tag, "L.d(String tag,Object message),object is null");
        }
    }

    public static void d(Object message) {
        d(TAG, message);
    }
}
