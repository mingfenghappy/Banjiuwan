package com.ins.feast.utils;

import android.os.Handler;

import com.dd.CircularProgressButton;
import com.ins.feast.common.AppData;
import com.ins.feast.entity.User;
import com.shelwee.update.utils.VersionUtil;
import com.sobey.common.utils.ApplicationHelp;
import com.sobey.common.utils.StrUtils;
import com.sobey.common.view.PswView;
import com.sobey.common.view.virtualKeyboardView.VirtualKeyboardView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2016/8/9.
 */
public class AppHelper {

    public static boolean getStartUp() {
        int versionCodeSave = AppData.App.getVersionCode();
        int versionCode = VersionUtil.getAppVersionCode(ApplicationHelp.getApplicationContext());
        if (versionCode > versionCodeSave) {
            return false;
        } else {
            return true;
        }
    }

    public static void saveStartUp() {
        int versionCode = VersionUtil.getAppVersionCode(ApplicationHelp.getApplicationContext());
        AppData.App.saveVersionCode(versionCode);
    }

    public static void removeStartUp() {
        AppHelper.removeStartUp();
    }

    ////////////////////////////////////////
    ////////////////////////////////////////
    ////////////////////////////////////////

    public static String getRealImgPath(String path) {
        if (!StrUtils.isEmpty(path) && path.startsWith("upload")) {
            return AppData.Url.domain + path;
        } else {
            return path;
        }
    }

    public static void progError2dle(final CircularProgressButton btn_go) {
        btn_go.setProgress(-1);
        handlProgressButton(btn_go, null, 0);
    }

    public static void progOk2dle(final CircularProgressButton btn_go) {
        btn_go.setProgress(100);
        handlProgressButton(btn_go, null, 0);
    }

    public static void progOk(final CircularProgressButton btn_go) {
        btn_go.setProgress(100);
        handlProgressButton(btn_go, null, -2);
    }

    public static void progError2dle(final CircularProgressButton btn_go, final ProgressCallback callback) {
        btn_go.setProgress(-1);
        handlProgressButton(btn_go, callback, 0);
    }

    public static void progOk2dle(final CircularProgressButton btn_go, final ProgressCallback callback) {
        btn_go.setProgress(100);
        handlProgressButton(btn_go, callback, 0);
    }

    public static void progOk(final CircularProgressButton btn_go, final ProgressCallback callback) {
        btn_go.setProgress(100);
        handlProgressButton(btn_go, callback, -2);
    }

    public static void handlProgressButton(final CircularProgressButton btn_go, final ProgressCallback callback, final int value) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (btn_go != null) {
                    btn_go.setClickable(true);
                    if (value != -2) {
                        btn_go.setProgress(value);
                    }
                }
                if (callback != null) {
                    callback.callback();
                }
            }
        }, 1000);
    }


    public interface ProgressCallback {
        void callback();
    }
}
