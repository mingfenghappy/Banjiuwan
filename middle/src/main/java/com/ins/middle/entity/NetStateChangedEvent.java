package com.ins.middle.entity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;

/**
 * author 边凌
 * date 2017/2/24 10:00
 * desc ${EventBus事件：网络状态改变}
 */

public class NetStateChangedEvent {
    private NetworkInfo activeInfo;

    private NetStateChangedEvent(NetworkInfo activeInfo) {
        this.activeInfo = activeInfo;
    }

    @NonNull
    public static NetStateChangedEvent getNetStateChangedEvent(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = manager.getActiveNetworkInfo();
        return new NetStateChangedEvent(activeInfo);
    }

    public boolean isAvailable(){
        return activeInfo!=null&&activeInfo.isAvailable();
    }
}
