package com.sobey.common.utils;

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
    }

    public static void v(Object message) {
        v(TAG, message);
    }

    public static void v(String tag, Object message) {
        if (!DEBUG) {
            return;
        }
        try {
            Log.v(tag, message.toString());
        } catch (NullPointerException e) {
            Log.e(tag, "L.v(Object message),object is null");
            e.printStackTrace();
        }
    }

    public static void i(Object message) {
        i(TAG, message);
    }

    public static void i(String tag, Object message) {
        if (!DEBUG) {
            return;
        }

        try {
            Log.i(tag, message.toString());
        } catch (NullPointerException e) {
            Log.e(tag, "L.i(Object message),object is null");
            e.printStackTrace();
        }
    }

    public static void e(Object message) {
        e(TAG, message);
    }

    public static void e(String tag, Object message) {
        if (!DEBUG) {
            return;
        }

        try {
            Log.e(tag, message.toString());
        } catch (NullPointerException e) {
            Log.e(tag, "L.e(Object message),object is null");
            e.printStackTrace();
        }
    }

    public static void d(Object message) {
        d(TAG, message);
    }

    public static void d(String tag, Object message) {
        if (!DEBUG) {
            return;
        }

        try {
            Log.d(tag, message.toString());
        } catch (NullPointerException e) {
            Log.e(tag, "L.d(Object message),object is null");
            e.printStackTrace();
        }
    }
}
