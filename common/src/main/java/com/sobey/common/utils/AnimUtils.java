package com.sobey.common.utils;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lsy on 15-6-14.
 */
public class AnimUtils {

    private static ValueAnimator animator;

    public static void show(final View v, int height) {
        show(v, height, null);
    }

    public static void show(final View v, int height, final OnAnimCallBack callBack) {
        Log.e("show", "animator != null:" + (animator != null) + "animator.isRunning():" + (animator != null ? animator.isRunning() : "null"));
        if (animator != null && animator.isRunning()) {
            animator.cancel();
            Log.e("show", "cancel");
        }
        int starth = v.getLayoutParams().height;
        Log.e("show", starth + "->" + height);
        v.setVisibility(View.VISIBLE);
        animator = ValueAnimator.ofInt(starth, height);
        animator.setDuration(300);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
                v.getLayoutParams().height = value;
                v.setLayoutParams(v.getLayoutParams());
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (callBack != null) callBack.onAnimEnd();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    public static void hide(final View v) {
        Log.e("hide", "animator != null:" + (animator != null) + "animator.isRunning():" + (animator != null ? animator.isRunning() : "null"));
        if (animator != null && animator.isRunning()) {
            animator.cancel();
            Log.e("hide", "cancel");
        }
        int starth = v.getLayoutParams().height;
        Log.e("hide", starth + "->0");
        animator = ValueAnimator.ofInt(starth, 0);
        animator.setDuration(300);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
                if (value == 0) {
                    v.setVisibility(View.GONE);
                }
                v.getLayoutParams().height = value;
                v.setLayoutParams(v.getLayoutParams());
            }
        });
        animator.start();
    }

    public interface OnAnimCallBack {
        void onAnimEnd();
    }
}
