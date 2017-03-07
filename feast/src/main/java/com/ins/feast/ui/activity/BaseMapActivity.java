package com.ins.feast.ui.activity;

import android.os.Bundle;
import android.support.annotation.CallSuper;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClientOption;
import com.ins.baidumapsdk.Locationer;
import com.ins.feast.ui.view.LoadProgressDialog;
import com.ins.middle.ui.activity.BaseAppCompatActivity;
import com.sobey.common.utils.L;

/**
 * author 边凌
 * date 2017/2/21 10:04
 * desc ${使用到地图的Activity的基类}
 */

public abstract class BaseMapActivity extends BaseAppCompatActivity implements Locationer.LocationCallback {
    private Locationer locationer;
    private LoadProgressDialog progressDialog;

    private boolean showLocationLoadProgress = false;

    private boolean handleLocationLifeCycleBySubclass = false;

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
    protected void startLocation() {
        L.d("startLocation");
        locationer.startlocation();
        if (showLocationLoadProgress) {
            progressDialog.show();
        }
    }

    /**
     * 结束定位
     */
    @CallSuper
    protected void stopLocation() {
        L.d("stopLocation");
        locationer.stopLocation();
        if (showLocationLoadProgress && progressDialog.isShowing()) {
            progressDialog.dismiss();
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
        return locationer.getAddrStr();
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
        locationer = new Locationer(this);
        locationer.setCallback(this);
        L.d("initLocationer");
        progressDialog = new LoadProgressDialog(this);
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
    }

}
