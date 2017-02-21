package com.ins.feast.ui.activity;

import android.os.Bundle;

import com.ins.baidumapsdk.Locationer;
import com.sobey.common.utils.L;

/**
 * author 边凌
 * date 2017/2/21 10:04
 * desc ${TODO}
 */

public abstract class BaseMapActivity extends BaseAppCompatActivity implements Locationer.LocationCallback {
    protected Locationer locationer;

    private boolean handleLocationLifeCycleBySubclass=false;

    protected void setHandleLocationLifeCycleBySubclass(boolean handleLocationLifeCycleBySubclass) {
        this.handleLocationLifeCycleBySubclass = handleLocationLifeCycleBySubclass;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationer = new Locationer(this);
        locationer.setCallback(this);
        L.d("startLocationer");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!handleLocationLifeCycleBySubclass) {
            L.d("stopLocation");
            locationer.stopLocation();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!handleLocationLifeCycleBySubclass) {
            L.d("startLocation");
            locationer.startlocation();
        }
    }
}
