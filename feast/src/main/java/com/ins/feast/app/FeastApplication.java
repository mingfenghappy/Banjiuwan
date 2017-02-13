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
package com.ins.feast.app;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.baidu.mapapi.SDKInitializer;
import com.ins.feast.BuildConfig;
import com.ins.feast.R;
import com.sobey.common.utils.ApplicationHelp;

import org.xutils.x;

import cn.jpush.android.api.JPushInterface;
import im.fir.sdk.FIR;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class FeastApplication extends Application {


    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        ApplicationHelp.getApplicationContext(this);

        initFonts();
        initJPush();
        initXUtils();
        initBugHd();
//        initBaiduMap();
        initSetting();
    }

    /**
     * 初始化设置
     */
    private void initSetting() {


    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initJPush() {
        JPushInterface.setDebugMode(BuildConfig.DEBUG);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
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


    private void initBugHd(){
        FIR.init(this);
    }

    private void initBaiduMap(){
        SDKInitializer.initialize(this);
    }
}
