package com.ins.feast.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.ins.feast.R;
import com.sobey.common.utils.L;


public class ChooseLocationActivity extends BaseMapActivity implements OnGetPoiSearchResultListener {

    public static void start(Context context) {
        Intent starter = new Intent(context, ChooseLocationActivity.class);
        context.startActivity(starter);
    }

    private TextView nowLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);
        initView();
        initSetting();
    }

    private void initSetting() {
        LocationClientOption locOption = locationer.getLocOption();
        if (locOption != null) {
            locOption.setScanSpan(0);
        }
        setHandleLocationLifeCycleBySubclass(false);

        locationer.startlocation();
    }

    private void initView() {
        nowLocation = (TextView) findViewById(R.id.nowLocation);
    }

    @Override
    public void onLocation(LatLng latLng, String city, String district, boolean isFirst) {
        nowLocation.setText(locationer.getAddrStr());
        locationer.stopLocation();
        PoiSearch pS=PoiSearch.newInstance();
        PoiNearbySearchOption option=new PoiNearbySearchOption();
        option.location(latLng);
        String networkLocationType = locationer.getBdLocation().getNetworkLocationType();
        L.d(networkLocationType);
        option.keyword(networkLocationType);
        option.radius(1000);
        option.pageCapacity(10).pageNum(1);
        pS.setOnGetPoiSearchResultListener(this);
        L.d("startSearch");
        pS.searchNearby(option);
    }

    public void relocation(View view) {
            locationer.startlocation();
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        L.d("onGetPoiResult");
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        L.d("onGetPoiDetailResult");
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
        L.d("onGetPoiIndoorResult");
    }
}
