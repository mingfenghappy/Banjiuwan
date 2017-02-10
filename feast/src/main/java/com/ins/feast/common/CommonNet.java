package com.ins.feast.common;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sobey.common.utils.ActivityUtil;
import com.sobey.common.utils.ApplicationHelp;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.ex.HttpException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;

/**
 * 网络请求执行过程，app中所有的网络请求都有通过此类执行访问，主要方法是send方法
 *
 * @author Administrator
 */
public class CommonNet {

    public static Callback.Cancelable samplepost(RequestParams params, Type entityClass, NetHander hander) {
        pritRarams(params);
        return x.http().post(params, new MyCommonCallback(hander, params, 0, entityClass, null));
    }

    public static Callback.Cancelable sampleget(RequestParams params, Type entityClass, NetHander hander) {
        pritRarams(params);
        return x.http().get(params, new MyCommonCallback(hander, params, 0, entityClass, null));
    }

    public static Callback.Cancelable post(NetHander hander, RequestParams params, int code, Type entityClass, Object obj) {
        pritRarams(params);
        return x.http().post(params, new MyCommonCallback(hander, params, code, entityClass, obj));
    }

    public static Callback.Cancelable get(NetHander hander, RequestParams params, int code, Type entityClass, Object obj) {
        pritRarams(params);
        return x.http().get(params, new MyCommonCallback(hander, params, code, entityClass, obj));
    }

    private static void pritRarams(RequestParams params) {
        if (params != null) {
            List<KeyValue> stringParams = params.getStringParams();
            if (stringParams != null) {
                for (KeyValue keyValue : stringParams) {
                    Log.e("nethander", keyValue.key + ":" + keyValue.getValueStr());
                }
            }
        }
    }

    private static class MyCommonCallback implements Callback.ProgressCallback<String> {
        private NetHander hander;
        private RequestParams params;
        private int code;
        private Type entityClass;
        private Object obj;

        public MyCommonCallback(NetHander hander, RequestParams params, int code, Type entityClass, Object obj) {
            this.hander = hander;
            this.params = params;
            this.code = code;
            this.entityClass = entityClass;
            this.obj = obj;
        }

        @Override
        public void onWaiting() {
        }

        @Override
        public void onStarted() {
            hander.netStart(code);
        }

        @Override
        public void onLoading(long total, long current, boolean isDownloading) {
            Log.d("netHander:", isDownloading ? "upload: " : "reply: " + current + "/" + total);
        }

        @Override
        public void onSuccess(String result) {
            Log.d("netHander:", "onSuccess");
            Log.e("netHander:", result);
//            System.out.println(result);
            try {
                JSONTokener jsonParser = new JSONTokener(result);
                JSONObject root;
                Integer status = null;
                String msg = "";
                String data = "";
                try {
                    root = (JSONObject) jsonParser.nextValue();
                    if (root.has("code")) status = root.getInt("code");
                    if (root.has("msg")) msg = root.getString("msg");
                    if (root.has("data")) data = root.getString("data");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Object pojo = null;
                if (!"".equals(data)) {
                    try {
                        pojo = new Gson().fromJson(data, entityClass);
                        System.out.println(pojo);
                        printValiCode(data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                switch (status) {
                    case 200:
                    case 1010:
                    case 1012:
                        hander.netGo(status, pojo, msg, obj);
                        break;
                    case 1005:
                    case 1007:
                        hander.netSetError(status, msg);
                        //如果用户数据为null（未登录）
//                        if (!ActivityUtil.isForeground(ApplicationHelp.getApplicationContext(), "com.ins.middle.ui.activity.LoginActivity")) {
//                            Intent intent = new Intent(ApplicationHelp.getApplicationContext(), LoginActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            ApplicationHelp.getApplicationContext().startActivity(intent);
//                        }
                        break;
                    default:
                        hander.netSetFalse(status, msg);
                        hander.netSetError(status, msg);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                hander.netException(code, "未知错误");
                Toast.makeText(ApplicationHelp.getApplicationContext(), "未知错误", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            Log.d("netHander:", "onError");
            try {
                if (ex instanceof HttpException) { // 网络错误
                    ex.printStackTrace();
                    HttpException nex = (HttpException) ex;
                    hander.netSetFail(code, nex.getCode(), nex.getMessage());
                    if (nex.getCode() == 0) {
                        hander.netSetError(code, "网络不太好额");
                    } else {
                        hander.netSetError(code, "服务器繁忙：" + nex.getCode());
                    }
                } else if (ex instanceof ConnectException) {
                    hander.netSetError(code, "请检查网络连接");
                } else if (ex instanceof SocketTimeoutException) {
                    hander.netSetError(code, "网络连接超时");
                } else {
                    hander.netSetError(code, "服务器开小差了~");
                }
            } catch (Exception e) {
                e.printStackTrace();
                //hander.netSetError(code, "未知错误");
                hander.netException(code, "未知错误");
            }
        }

        @Override
        public void onCancelled(CancelledException cex) {
            Log.d("netHander:", "onCancelled");
        }

        @Override
        public void onFinished() {
            Log.d("netHander:", "onFinished");
            hander.netEnd(code);
        }
    }

    /**
     * 打印出验证码
     */
    private static void printValiCode(String data) {
        try {
            JSONObject datajson = new JSONObject(data);
            if (datajson.has("valiCode")) {
                if (AppData.Config.showVali) {
                    Toast.makeText(ApplicationHelp.getApplicationContext(), datajson.getString("valiCode"), Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Log.e("CommonNet", "解析验证码失败");
        }
    }

    public interface NetHander {

        void netGo(int status, Object pojo, String text, Object obj);

        void netStart(int status);

        void netEnd(int status);

        void netSetFalse(int status, String text);

        void netSetFail(int status, int errorCode, String text);

        void netSetError(int status, String text);

        void netException(int status, String text);
    }

    public static abstract class SampleNetHander implements NetHander {

        @Override
        public void netStart(int status) {

        }

        @Override
        public void netEnd(int status) {

        }

        @Override
        public void netSetFalse(int status, String text) {

        }

        @Override
        public void netSetFail(int status, int errorCode, String text) {

        }

        @Override
        public void netException(int status, String text) {

        }
    }
}
