package com.ins.feast.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.ins.feast.R;
import com.ins.feast.entity.Position;
import com.ins.feast.ui.adapter.ChooseLocationAdapter;
import com.ins.feast.utils.RxViewUtils;
import com.sobey.common.interfaces.OnRecycleItemClickListener;
import com.sobey.common.utils.L;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import static com.ins.feast.R.id.searchLocation;


public class ChooseLocationActivity extends BaseMapActivity implements
        View.OnClickListener,
        OnGetGeoCoderResultListener, OnRecycleItemClickListener {

    private TextView nowLocation;
    private RecyclerView nearbyLocations;
    private ChooseLocationAdapter adapter;
    private String city = "成都市";
    private LatLng latLng = new LatLng(30.560514, 104.075222);
    private String district = "";
    private GeoCoder gC;

    public static void start(Context context) {
        Intent starter = new Intent(context, ChooseLocationActivity.class);
        context.startActivity(starter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);
        setEventBusSupport();
        initView();
        initSetting();
    }

    private void initSetting() {
        LocationClientOption locOption = getLocOption();
        if (locOption != null) {
            locOption.setScanSpan(0);
        }
        setHandleLocationLifeCycleBySubclass(false);

        startLocation();
        setShowLocationLoadProgress(true);
    }

    private void initView() {
        nowLocation = (TextView) findViewById(R.id.nowLocation);
        nearbyLocations = (RecyclerView) findViewById(R.id.nearbyLocationList);
        nearbyLocations.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        View moreLocation = findViewById(R.id.moreLocationRoot);
        RxViewUtils.throttleFirst(moreLocation, this);
        RxViewUtils.throttleFirst(findViewById(R.id.searchLocation), this);
        RxViewUtils.throttleFirst(nowLocation, this);
        RxViewUtils.throttleFirst(findViewById(R.id.relocation), 300, this);
    }

    private void startSearchLocationActivity() {
        SearchLocationActivity.start(this, city);
    }

    private void startSearchAddressActivity() {
        Intent intent = new Intent(ChooseLocationActivity.this, SearchAddressActivity.class);
        intent.putExtra("city", city);
        intent.putExtra("latLng", latLng);
        startActivity(intent);
    }

    @Override
    public void onLocation(LatLng latLng, String city, String district, boolean isFirst) {
        stopLocation();
        saveLocationInfoAndChangeUI(latLng, city, district);
        searchNearbyLocation(latLng);
    }

    private void saveLocationInfoAndChangeUI(LatLng latLng, String city, String district) {
        this.latLng = latLng;
        this.city = city;
        this.district = district;
        nowLocation.setText(getAddStr());
    }

    private void searchNearbyLocation(LatLng latLng) {
        if (gC == null) {
            gC = GeoCoder.newInstance();
            gC.setOnGetGeoCodeResultListener(this);
        }
        gC.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
    }

    public void onClick_relocation(View view) {
        startLocation();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.moreLocationRoot:
                startSearchAddressActivity();
                break;
            case searchLocation:
                startSearchLocationActivity();
                break;
            case R.id.nowLocation:
                chooseNowLocation();
                break;
            case R.id.relocation:
                startLocation();
                break;
        }
    }

    private void chooseNowLocation() {
        Position position = new Position();
        position.setKey(nowLocation.getText().toString());
        position.setCity(city);
        position.setLatLng(latLng);
        position.setDistrict(district);
        postAndFinishActivity(position);
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        List<PoiInfo> poiList =
                reverseGeoCodeResult.getPoiList();
        poiList = poiList.subList(0, 3);
        if (adapter == null) {
            adapter = new ChooseLocationAdapter(this, poiList);
            adapter.setOnItemClickListener(this);
            nearbyLocations.setAdapter(adapter);
        } else {
            adapter.resetData(poiList);
        }
    }

    @Override
    public void onItemClick(RecyclerView.ViewHolder viewHolder) {
        PoiInfo poiInfo = adapter.getPoiInfoList().get(viewHolder.getAdapterPosition());
        Position position = new Position(poiInfo);
        postAndFinishActivity(position);
    }

    private void postAndFinishActivity(Position position) {
        position.setCity(city);
        EventBus.getDefault().post(position);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Subscribe
    public void onReceivePosition(Position position) {
        L.d("receivePosition-- ChooseLocationActivity:" + position.getKey());
        finish();
    }

    public void onClick_back(View view) {
        onBackPressed();
    }
}
