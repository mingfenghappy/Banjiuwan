package com.ins.feast.ui.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.ins.middle.common.AppData;
import com.ins.middle.common.CommonNet;
import com.sobey.common.utils.StrUtils;

import org.xutils.http.RequestParams;

/**
 * Created by Administrator on 2017/3/6.
 * 获取服务器基础配置信息的service类
 * 有些请求需要在activity关闭后任然能够正常处理服务器返回，这样的请求交给service完成
 * 需要在mainfests文件中声明
 */

public class NetService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        netGetPhone(startId);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //获取客服电话
    private void netGetPhone(final int startId) {
        RequestParams params = new RequestParams(AppData.Url.getInfo);
        params.addHeader("token", AppData.App.getToken());
        CommonNet.samplepost(params, String.class, new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                if (pojo == null) netSetError(code, "接口异常");
                else {
                    String phone = (String)pojo;
                    if (!StrUtils.isEmpty(phone)) {
                        AppData.App.removePhone();
                        AppData.App.savePhone(phone);
                        stopSelf(startId);
                    }
                }
            }
            @Override
            public void netSetError(int code, String text) {
            }
        });
    }
}
