/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ins.chef.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.ins.chef.BuildConfig;
import com.ins.chef.R;
import com.ins.middle.common.AppData;
import com.sobey.common.utils.ApplicationHelp;
import com.sobey.common.utils.L;
import com.sobey.common.utils.StrUtils;

import org.xutils.x;

import cn.jpush.android.api.JPushInterface;
import im.fir.sdk.FIR;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class ChefApplication extends Application {


    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        ApplicationHelp.getApplicationContext(this);

        initFonts();
        initJPush();
        initXUtils();
        initBugHd();
        initSetting();
    }

    private void initSetting() {
        L.setDEBUG(BuildConfig.DEBUG);
        L.setTAG(getString(R.string.app_name));
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initJPush() {
        JPushInterface.setDebugMode(BuildConfig.DEBUG);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
        saveJPushId();
    }

    private void saveJPushId() {
        String registrationID = JPushInterface.getRegistrationID(getApplicationContext());
        if (!StrUtils.isEmpty(registrationID)) {
            AppData.App.saveJpushId(registrationID);
        }
    }

    private void initXUtils() {
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.
    }

    private void initFonts() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/ltx.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }


    private void initBugHd() {
        FIR.init(this);
    }

}
