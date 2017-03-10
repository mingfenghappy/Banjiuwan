package com.sobey.common.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PhoneUtils {
    public static void call(Context context, String number) {
        if (!StrUtils.isEmpty(number)) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            Uri data = Uri.parse("tel:" + number);
            intent.setData(data);
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "电话号码为空", Toast.LENGTH_SHORT).show();
        }
    }

    public static void callByUrl(Context context, String url) {
        if (!StrUtils.isEmpty(url)) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            Uri data = Uri.parse(url);
            intent.setData(data);
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "电话号码为空", Toast.LENGTH_SHORT).show();
        }
    }
}
