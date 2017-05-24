package com.ins.feast.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ins.middle.entity.NetStateChangedEvent;
import com.sobey.common.utils.L;

import org.greenrobot.eventbus.EventBus;

/**
 * author 边凌
 * date 2017/2/24 9:52
 * desc ${TODO}
 */

public class NetStateReceiver extends BroadcastReceiver {

    private static void postNetState(Context context) {
        NetStateChangedEvent netStateChangedEvent = NetStateChangedEvent.create(context);
        if (netStateChangedEvent.isAvailable()) {
            L.d("NetState available");
        } else {
            L.d("NetState not available");
        }
        EventBus.getDefault().post(netStateChangedEvent);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        L.d("receiveNetStateChanged");
        postNetState(context);
    }
}
