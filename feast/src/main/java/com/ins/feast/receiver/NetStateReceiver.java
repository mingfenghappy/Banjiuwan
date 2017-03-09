package com.ins.feast.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;

import com.ins.middle.entity.NetStateChangedEvent;
import com.sobey.common.utils.L;

import org.greenrobot.eventbus.EventBus;

/**
 * author 边凌
 * date 2017/2/24 9:52
 * desc ${TODO}
 */

public class NetStateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        L.d("receiveNetStateChanged");
        if (Build.VERSION.SDK_INT < 21) {
            postNetState(context);
        }
    }

    /**
     * 当在SDK21之上时使用如下方法监听网络变化，之下时使用广播接收器
     */
    public static void registerAboveSDK21(final Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            connectivityManager.requestNetwork(new NetworkRequest.Builder().build(), new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    L.d("NetWorkCallBack:onAvailable");
                    super.onAvailable(network);
                    postNetState(context);
                }

                @Override
                public void onLost(Network network) {
                    L.d("NetWorkCallBack:onLost");
                    super.onLost(network);
                    postNetState(context);
                }

                @Override
                public void onLosing(Network network, int maxMsToLive) {
                    L.d("NetWorkCallBack:onLosing");
                    super.onLosing(network, maxMsToLive);
                    postNetState(context);
                }

            });
        }
    }

    private static void postNetState(Context context) {
        NetStateChangedEvent netStateChangedEvent = NetStateChangedEvent.getNetStateChangedEvent(context);
        EventBus.getDefault().post(netStateChangedEvent);
    }
}
