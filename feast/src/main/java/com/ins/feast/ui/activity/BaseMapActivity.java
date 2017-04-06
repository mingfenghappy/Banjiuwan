package com.ins.feast.ui.activity;

import android.Manifest;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClientOption;
import com.ins.baidumapsdk.Locationer;
import com.ins.middle.entity.NetStateChangedEvent;
import com.ins.middle.ui.activity.BaseFeastActivity;
import com.ins.middle.ui.dialog.DialogLoading;
import com.sobey.common.utils.L;
import com.sobey.common.utils.PermissionsUtil;
import com.sobey.common.utils.StrUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * author 边凌
 * date 2017/2/21 10:04
 * desc ${使用到地图的Activity的基类}
 */

public abstract class BaseMapActivity extends BaseFeastActivity implements Locationer.LocationCallback {
    private Locationer locationer;
    private DialogLoading loading;

    private boolean showLocationLoadProgress = false;

    private boolean handleLocationLifeCycleBySubclass = false;

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
//        super.onCreate(savedInstanceState, persistentState);
//        setEventBusSupport();
//    }

    /**
     * 开始定位时是否展示进度弹窗
     */
    protected void setShowLocationLoadProgress(boolean showLocationLoadProgress) {
        this.showLocationLoadProgress = showLocationLoadProgress;
    }

    /**
     * 是否取消该类的定位时期控制，既是否由子类自主的决定何时开始定位，何时结束定位
     */
    protected void setHandleLocationLifeCycleBySubclass(boolean handleLocationLifeCycleBySubclass) {
        this.handleLocationLifeCycleBySubclass = handleLocationLifeCycleBySubclass;
    }

    /**
     * 开始定位
     */
    @CallSuper
    public void startLocation() {
        L.d("startLocation");
        if (PermissionsUtil.requsetLocation(this, null)) {
            locationer.startlocation();
        }
        if (showLocationLoadProgress) {
            loading.show();
        }
    }

    /**
     * 结束定位
     */
    @CallSuper
    public void stopLocation() {
        L.d("stopLocation");
        locationer.stopLocation();
        if (showLocationLoadProgress && loading.isShowing()) {
            loading.dismiss();
        }
    }

    /**
     * 获得定位选项，可以进行设置
     */
    protected LocationClientOption getLocOption() {
        return locationer.getLocOption();
    }

    /**
     * 获得位置字符串
     */
    protected String getAddStr() {
        return getAddStr(false);
    }

    /**
     * 获得位置字符串
     * needCity 是否带上城市前缀
     */
    protected String getAddStr(boolean needCity) {
        return locationer.getAddrStr(needCity);
    }

    protected String getLocationDescribe(){
        String locationDescribe = getBdLocation().getLocationDescribe();
        if(!StrUtils.isEmpty(locationDescribe)){
            return StrUtils.subFirstChart(locationDescribe, "在");
        }else {
            return getAddStr();
        }
    }

    /**
     * 获得百度定位实体
     */
    protected BDLocation getBdLocation() {
        return locationer.getBdLocation();
    }

    @CallSuper
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setEventBusSupport();
        locationer = new Locationer(this);
        locationer.setCallback(this);
        L.d("initLocationer");
        loading = new DialogLoading(this);
    }

    @CallSuper
    @Override
    protected void onPause() {
        super.onPause();
        if (!handleLocationLifeCycleBySubclass) {
            stopLocation();
        }
    }

    @CallSuper
    @Override
    protected void onResume() {
        super.onResume();
        if (!handleLocationLifeCycleBySubclass) {
            startLocation();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (loading != null) loading.dismiss();
    }

    /**
     * 网络状态可用时重新定位
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveNetStateChanged(NetStateChangedEvent event) {
        if (event.isAvailable()) {
            startLocation();
        }
    }

}
