package com.ins.middle.base;

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
    private NetworkInfo mobileInfo;
    private NetworkInfo wifiInfo;
    private NetworkInfo activeInfo;

    public NetStateChangedEvent(NetworkInfo mobileInfo, NetworkInfo wifiInfo, NetworkInfo activeInfo) {
        this.mobileInfo = mobileInfo;
        this.wifiInfo = wifiInfo;
        this.activeInfo = activeInfo;
    }

    public NetworkInfo getMobileInfo() {
        return mobileInfo;
    }

    public NetworkInfo getWifiInfo() {
        return wifiInfo;
    }

    public NetworkInfo getActiveInfo() {
        return activeInfo;
    }
    @NonNull
    public static NetStateChangedEvent getNetStateChangedEvent(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo activeInfo = manager.getActiveNetworkInfo();
        return new NetStateChangedEvent(mobileInfo, wifiInfo, activeInfo);
    }
}
