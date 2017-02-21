package com.ins.feast.utils;

import android.view.View;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * author 边凌
 * date 2017/2/21 11:48
 * desc ${RxView工具}
 */

public class RxViewUtils {
    //默认快速点击过滤时间为1秒
    private final static int DEFAULT_INTERVAL=1000;

    /**
     * 快速点击过滤
     * @param view
     * @param listener
     */
    public static void throttleFirst(final View view, final View.OnClickListener listener) {
        RxView.clicks(view).throttleFirst(DEFAULT_INTERVAL, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (listener != null) {
                    listener.onClick(view);
                }
            }
        });
    }
}
