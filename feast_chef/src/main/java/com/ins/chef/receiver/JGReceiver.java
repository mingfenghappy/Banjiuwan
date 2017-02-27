package com.ins.chef.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.sobey.common.utils.StrUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * 自定义接收器
 * <p/>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JGReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            processIDMessage(context, regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//            processCustomMessage(context, bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

            //打开自定义的Activity
            processNotifyClick(context, bundle);

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    private void processIDMessage(Context context, String id) {
        Log.e("push", "push id:" + id);
    }

    //send msg to MainActivity
//    private void processCustomMessage(Context context, Bundle bundle) {
//        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//        Log.e("push", "push message:" + message + extras);
//        if (AppData.Config.showTestToast) {
//            Toast.makeText(context, "push message:" + message + extras, Toast.LENGTH_SHORT).show();
//        }
//        if (!StrUtils.isEmpty(extras)) {
//            try {
//                JSONObject datajson = new JSONObject(extras);
//                //订单推送
//                if (datajson.has("aboutOrder")) {
//                    EventOrder eventOrder = new EventOrder();
//                    eventOrder.setMsg(message);
//                    if (datajson.has("aboutOrder")) {
//                        String aboutOrder = datajson.getString("aboutOrder");
//                        eventOrder.setAboutOrder(aboutOrder);
//                    }
//                    if (datajson.has("advance_order_id")) {
//                        int orderId = Integer.parseInt(datajson.getString("advance_order_id"));
//                        eventOrder.setOrderId(orderId);
//                    }
//                    EventBus.getDefault().post(eventOrder);
//                }
//                //系统推送
//                else if (datajson.has("abloutIdentify")) {
//                    EventIdentify eventIdentify = new EventIdentify();
//                    eventIdentify.setMsg(message);
//                    if (datajson.has("abloutIdentify")) {
//                        String aboutIdentify = datajson.getString("abloutIdentify");
//                        eventIdentify.setAboutsystem(aboutIdentify);
//                    }
//                    EventBus.getDefault().post(eventIdentify);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                Log.e("startapp", "启动消息页面失败");
//            }
//        }
//    }

    private void processNotifyClick(Context context, Bundle bundle) {
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Log.e("push", "push message:" + extras);

        if (!StrUtils.isEmpty(extras)) {
            //有拖拽字段，跳转消息页面
            try {
                JSONObject datajson = new JSONObject(extras);
                if (datajson.has("info_type")) {
                    String type = datajson.getString("info_type");
                    Intent intent = new Intent();
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                    if ("2".equals(type)) {
                        //跳转订单消息页面
                    } else if ("1".equals(type)) {
                        //系统消息
                        context.startActivity(intent);
                    } else if ("3".equals(type)) {
                        //丁一定
                    } else if ("4".equals(type)) {
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("startapp", "启动消息页面失败");
            }
        } else {
            //////////启动app
//            try {
//                String appPackageName = "com.ins.wojia";
//                boolean runningApp = AppUtils.isRunningApp(context, appPackageName);
//                if (!runningApp) {
//                    AppUtils.startAPP(context, appPackageName);
//                } else {
//                    Log.e("liao", "isRunning");
//                }
//            } catch (Exception e) {
//                Log.e("startapp", "启动app失败");
//                e.printStackTrace();
//            }
        }
    }
}
