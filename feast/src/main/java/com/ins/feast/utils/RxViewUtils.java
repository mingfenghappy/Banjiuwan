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
    //默认快速点击过滤时间为1000毫秒
    private final static int DEFAULT_INTERVAL = 1000;

    /**
     * 快速点击过滤
     */
    public static void throttleFirst(final View view, final View.OnClickListener listener) {
        throttleFirst(view, DEFAULT_INTERVAL, listener);
    }

    /**
     * 可设置点击间隔的快速点击过滤
     *
     * 注意：这里过滤的间隔时间不会累加计算，
     * 例第一下0ms,第二下100ms，第三下200ms，假设间隔时间为200ms，第三下仍然不会生效
     * 因为第三下相对它的上一次间隔为100ms小于200ms，也即必须是距离最近一次的点击200ms后再次点击才能生效
     * 也即点击事件的触发时间不是按照时间线以200ms为间隔等分而触发的。
     * @param interval 单位：毫秒
     */
    public static void throttleFirst(final View view, int interval, final View.OnClickListener listener) {
        RxView.clicks(view).throttleFirst(interval, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (listener != null) {
                    listener.onClick(view);
                }
            }
        });
    }
}
