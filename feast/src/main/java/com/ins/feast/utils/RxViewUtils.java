package com.ins.feast.utils;

import android.view.View;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.MainThreadSubscription;
import rx.functions.Action1;

import static rx.android.MainThreadSubscription.verifyMainThread;

/**
 * author 边凌
 * date 2017/2/21 11:48
 * desc ${RxView工具}
 */

public class RxViewUtils {
    //默认快速点击过滤时间为1000毫秒
    private final static int DEFAULT_INTERVAL = 1000;

    private RxViewUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * 快速点击过滤
     */
    public static Subscription throttleFirst(final View view, final View.OnClickListener listener) {
        return throttleFirst(view, DEFAULT_INTERVAL, listener);
    }

    /**
     * 可设置点击间隔的快速点击过滤
     * <p>
     * 注意1：这里过滤的间隔时间不会累加计算，
     * 例第一下0ms,第二下100ms，第三下200ms，假设间隔时间为200ms，第三下仍然不会生效
     * 因为第三下相对它的上一次间隔为100ms小于200ms，也即必须是距离最近一次的点击200ms后再次点击才能生效
     * 也即点击事件的触发时间不是按照时间线以200ms为间隔等分而触发的。
     * <p>
     * 注意2：由于RxView源码通过{@link android.view.View.OnClickListener},因此若对view设置多次listener会导致该方法失效
     * 注意3：该方法的返回值持有view的强引用
     *
     * @param interval 单位：毫秒
     */
    public static Subscription throttleFirst(final View view, int interval, final View.OnClickListener listener) {
        return RxView.clicks(view).throttleFirst(interval, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (listener != null) {
                    listener.onClick(view);
                }
            }
        });
    }

    // TODO: 2017/3/13  
    public static Subscription throttleFirst(int interval, final View.OnClickListener listenerfinal,View...views){
        Observable.create(new ViewsClickOnSubscribe(views));
        return null;
    }

    private static final class ViewsClickOnSubscribe implements Observable.OnSubscribe<View> {
        final View[] views;

        private ViewsClickOnSubscribe(View... view) {
            this.views = view;
        }

        @Override
        public void call(final Subscriber<? super View> subscriber) {
            verifyMainThread();

            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(v);
                    }
                }
            };

            subscriber.add(new MainThreadSubscription() {
                @Override
                protected void onUnsubscribe() {
                    setListener(null);
                }
            });

            setListener(listener);
        }

        private void setListener(View.OnClickListener listener) {
            if (views != null) {
                for (View view : views) {
                    view.setOnClickListener(listener);
                }
            }
        }
    }

}
