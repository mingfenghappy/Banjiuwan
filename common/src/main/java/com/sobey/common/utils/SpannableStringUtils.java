package com.sobey.common.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;

import com.sobey.common.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by lsy on 15-6-14.
 */
public class SpannableStringUtils {
    public static SpannableString create(Context context, String[] strs, int[] colors) {
        String strall = "";
        for (String str : strs) {
            strall += str;
        }
        SpannableString strSpan = new SpannableString(strall);
        int lengh = 0;
        for (int i = 0; i < strs.length; i++) {
            int start = lengh;
            lengh += strs[i].length();
            strSpan.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, colors[i])), start, lengh, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return strSpan;
    }
}
