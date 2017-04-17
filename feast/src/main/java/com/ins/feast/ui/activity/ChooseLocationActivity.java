package com.ins.feast.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.google.gson.reflect.TypeToken;
import com.ins.feast.R;
import com.ins.feast.entity.Address;
import com.ins.feast.entity.Position;
import com.ins.feast.ui.adapter.ChooseLocationAdapter;
import com.ins.feast.utils.AppHelper;
import com.ins.feast.utils.NetCouldOrderHelper;
import com.ins.feast.utils.RxViewUtils;
import com.ins.middle.common.AppData;
import com.ins.middle.common.CommonNet;
import com.ins.middle.entity.User;
import com.sobey.common.common.LoadingViewUtil;
import com.sobey.common.interfaces.OnRecycleItemClickListener;
import com.sobey.common.utils.ClickUtils;
import com.sobey.common.utils.L;
import com.sobey.common.utils.StrUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import static com.ins.feast.R.id.searchLocation;
import static com.ins.feast.R.id.thirdListView;

/**
 * type：0 收货地址打开，1：首页右上角打开 ，2：下单切换地址 默认0
 * 收获地址打开只更收获地址页面数据，首页打开只更改首页地址信息
 */
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

    private View lay_defult_address;
    private TextView text_defult_name;
    private TextView text_defult_phone;
    private TextView text_defult_address;
    private TextView text_defult_detailaddress;

    private Address defaultAddress;

    private int type;

    public static void start(Context context) {
        start(context, 0);
    }

    public static void start(Context context, int type) {
        //防止重复点击过快打开页面
        if (ClickUtils.isFastDoubleClick()) return;
        Intent starter = new Intent(context, ChooseLocationActivity.class);
        starter.putExtra("type", type);
        context.startActivity(starter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);
        setEventBusSupport();
        initBase();
        initView();
        initSetting();
        netGetAddress();    //请求默认地址
    }

    private void initBase() {
        if (getIntent().hasExtra("type")) {
            type = getIntent().getIntExtra("type", 0);
        }
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
        lay_defult_address = findViewById(R.id.lay_defult_address);
        text_defult_name = (TextView) findViewById(R.id.text_defult_name);
        text_defult_phone = (TextView) findViewById(R.id.text_defult_phone);
        text_defult_address = (TextView) findViewById(R.id.text_defult_address);
        text_defult_detailaddress = (TextView) findViewById(R.id.text_defult_detailaddress);
        lay_defult_address.setOnClickListener(this);

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
        SearchLocationActivity.start(this, city, type);
    }

    private void startSearchAddressActivity() {
        Intent intent = new Intent(ChooseLocationActivity.this, SearchAddressActivity.class);
        intent.putExtra("type", type);
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
//        nowLocation.setText(getAddStr(true));
        nowLocation.setText(getLocationDescribe());
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
            case R.id.lay_defult_address:
                if (defaultAddress != null) {
                    Position position = new Position(defaultAddress);
                    postAndFinishActivity(position);
                }
                break;
        }
    }

    private void chooseNowLocation() {
        Position position = new Position();
        position.setKey(nowLocation.getText().toString());
        position.setAddress(nowLocation.getText().toString());
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
        if (poiList != null&&poiList.size()>=3) {
            poiList = poiList.subList(0, 3);
        }
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

    private void postAndFinishActivity(final Position position) {
        position.setCity(city);
        position.setType(type);

        if (type==2) {
            //新需求导致的畸形逻辑，返回前请求服务器是否可以点餐
            NetCouldOrderHelper.netCouldOrder(this, position.getLatLng(), new NetCouldOrderHelper.CouldOrderCallback() {
                @Override
                public void succese(int couldOrder) {
                    if (couldOrder == 1) {
                        EventBus.getDefault().post(position);
                        finish();
                    } else {
                        Toast.makeText(ChooseLocationActivity.this, "当前位置无法点餐", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            EventBus.getDefault().post(position);
            finish();
        }
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

    private void setDefaultAddress(Address address) {
        defaultAddress = address;
        if (address == null) {
            lay_defult_address.setVisibility(View.GONE);
        } else {
            lay_defult_address.setVisibility(View.VISIBLE);
            text_defult_name.setText(address.getName() + " " + (address.getGender() == 0 ? "先生" : "女士"));
            text_defult_phone.setText(address.getPhone());
            text_defult_address.setText(address.getAddress());
            text_defult_detailaddress.setText(address.getDetailAddress());
        }
    }

    ///////////////////////
    /////////网络请求接口
    ///////////////////////

    public void netGetAddress() {
        //如果没有token 代表用户没登录，不发起请求
        String token = AppData.App.getToken();
        if (StrUtils.isEmpty(token)) {
            setDefaultAddress(null);
            return;
        }
        RequestParams params = new RequestParams(AppData.Url.getAddress);
        params.addHeader("token", token);
        params.addBodyParameter("pageNO", "1");
        params.addBodyParameter("pageSize", "100");
        CommonNet.samplepost(params, new TypeToken<List<Address>>() {
        }.getType(), new CommonNet.SampleNetHander() {
            @Override
            public void netGo(int code, Object pojo, String text, Object obj) {
                if (pojo == null) netSetError(code, "接口异常");
                else {
                    List<Address> addresses = (ArrayList<Address>) pojo;
                    setDefaultAddress(AppHelper.getDefaultAddressInList(addresses));
                }
            }

            @Override
            public void netSetError(int code, String text) {
                Toast.makeText(ChooseLocationActivity.this, text, Toast.LENGTH_SHORT).show();
                setDefaultAddress(null);
            }
        });
    }
}
