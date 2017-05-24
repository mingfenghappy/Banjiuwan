package com.ins.feast.utils;

import android.content.Context;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.google.gson.reflect.TypeToken;
import com.ins.feast.ui.activity.HomeActivity;
import com.ins.middle.common.AppData;
import com.ins.middle.common.CommonNet;

import org.xutils.http.RequestParams;

/**
 * Created by Administrator on 2017/4/7.
 */

public class NetCouldOrderHelper {

    //请求服务器告知当前用户是否可以点餐
    public static void netCouldOrder(final Context context, LatLng latlng, final CouldOrderCallback callback) {
        RequestParams params = new RequestParams(AppData.Url.judgeIsPoint);
        params.addHeader("token", AppData.App.getToken());
        params.addBodyParameter("latLng", MapHelper.LatLng2Str(latlng));
        CommonNet.samplepost(params, new TypeToken<Integer>() {
        }.getType(), new CommonNet.SampleNetHander() {
            @Override
            public void netGo(final int code, Object pojo, String text, Object obj) {
                if (pojo == null) netSetError(code, "接口异常");
                else {
                    int couldOrder = (Integer) pojo;
                    if (AppData.Config.showTestToast)
                        Toast.makeText(context, couldOrder == 1 ? "可以下单：1" : "不能下单：0", Toast.LENGTH_SHORT).show();
                    if (callback != null) {
                        callback.succese(couldOrder);
                    }
                }
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface CouldOrderCallback {
        void succese(int couldOrder);
    }
}
