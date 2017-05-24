package com.sobey.common.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

public class PhoneUtils {
    public static void phone(Context context, String url) {
        if (!StrUtils.isEmpty(url)) {
            if (!url.startsWith("tel:")) {
                url = "tel:" + url;
            }
            Intent intent = new Intent(Intent.ACTION_DIAL);
            Uri data = Uri.parse(url);
            intent.setData(data);
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "电话号码为空", Toast.LENGTH_SHORT).show();
        }
    }

    public static void call(Activity context, String url) {
        if (!StrUtils.isEmpty(url)) {
            if (!url.startsWith("tel:")) {
                url = "tel:" + url;
            }
            Intent intent = new Intent(Intent.ACTION_CALL);
            Uri data = Uri.parse(url);
            intent.setData(data);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                PermissionsUtil.requestPermissions(context, new String[]{Manifest.permission.CALL_PHONE});
                return;
            }
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "电话号码为空", Toast.LENGTH_SHORT).show();
        }
    }
}
