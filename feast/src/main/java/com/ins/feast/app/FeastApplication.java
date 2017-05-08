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
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.google.gson.reflect.TypeToken;
import com.ins.feast.BuildConfig;
import com.ins.feast.R;
import com.ins.middle.common.AppData;
import com.ins.middle.common.CommonNet;
import com.ins.middle.entity.MsgConfig;
import com.sobey.common.utils.ApplicationHelp;
import com.sobey.common.utils.L;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;

import org.greenrobot.eventbus.EventBus;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.List;

import cn.jpush.android.api.JPushInterface;
import im.fir.sdk.FIR;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

//import android.support.multidex.MultiDex;


public class FeastApplication extends Application {

    @Override
    public void onCreate() {
//        MultiDex.install(this);
        super.onCreate();
        ApplicationHelp.getApplicationContext(this);

        initSetting();

        initFonts();
        initJPush();
        initXUtils();
        initBugHd();
        initBaiduMap();
        initTBS();
        initMsgConfig();
    }

    private void initMsgConfig() {
        readStorageMsgConfig();
        RequestParams params = new RequestParams(AppData.Url.msgConfig);
        CommonNet.samplepost(params, new TypeToken<List<MsgConfig>>() {
        }.getType(), new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int status, Object pojo, String text, Object obj) {
                List<MsgConfig> msgConfigs = (List<MsgConfig>) pojo;
                if (msgConfigs != null) {
                    for (MsgConfig msgConfig : msgConfigs) {
                        if (msgConfig.id == AppData.App.SPECIFIC_MSG_ID) {
                            assignmentAndSaveMsgConfig(msgConfig);
                            break;
                        }
                    }
                }
            }

            @Override
            public void netSetError(int status, String text) {

            }
        });
    }

    /**
     * 读取存储的消息配置以防接口请求失败
     */
    private void readStorageMsgConfig() {
        SharedPreferences sharedPreferences = FeastApplication.this.getSharedPreferences("config", MODE_PRIVATE);
        String msgConfig = sharedPreferences.getString("msgConfig", AppData.App.SPECIFIC_MSG);
        if (!TextUtils.equals(msgConfig, AppData.App.SPECIFIC_MSG)) {
            AppData.App.SPECIFIC_MSG = msgConfig;
        }
    }

    /**
     * 当接口请求成功，重新赋值并存储消息配置
     *
     * @param msgConfig
     */
    private void assignmentAndSaveMsgConfig(MsgConfig msgConfig) {
        AppData.App.SPECIFIC_MSG = msgConfig.msg;
        SharedPreferences sharedPreferences = FeastApplication.this.getSharedPreferences("config", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        if (edit != null) {
            edit.putString("msgConfig", AppData.App.SPECIFIC_MSG).apply();
        }
    }

    private void initSetting() {
        L.setDEBUG(BuildConfig.DEBUG);
        String logTag = getString(R.string.app_name);
        L.setTAG(logTag);
        EventBus.TAG = logTag;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }

    private void initJPush() {
        JPushInterface.setDebugMode(BuildConfig.DEBUG);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush x
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

    private void initBaiduMap() {
        SDKInitializer.initialize(this);
    }

    private void initTBS() {
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        //TbsDownloader.needDownload(getApplicationContext(), false);

        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                Log.e("app", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub

            }
        };
        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
                Log.d("app", "onDownloadFinish is " + i);
            }

            @Override
            public void onInstallFinish(int i) {
                Log.d("app", "onInstallFinish is " + i);
            }

            @Override
            public void onDownloadProgress(int i) {
                Log.d("app", "onDownloadProgress:" + i);
            }
        });

        QbSdk.initX5Environment(getApplicationContext(), cb);
    }
}
