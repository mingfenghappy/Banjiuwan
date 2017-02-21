package com.ins.feast.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.ins.feast.R;
import com.ins.feast.entity.Position;
import com.ins.feast.ui.adapter.RecycleAdapterSearchAddress;
import com.ins.feast.utils.MapHelper;
import com.sobey.common.common.LoadingViewUtil;
import com.sobey.common.interfaces.OnRecycleItemClickListener;
import com.sobey.common.utils.StrUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchAddressActivity extends BaseAppCompatActivity implements OnRecycleItemClickListener, OnGetPoiSearchResultListener, View.OnClickListener, OnGetGeoCoderResultListener {

    private MapView mapView;
    private BaiduMap baiduMap;

    private RecyclerView recyclerView;
    private List<Position> results = new ArrayList<>();
    private RecycleAdapterSearchAddress adapter;

    private EditText edit_search;
    private TextView btn_cancel;
    private TextView btn_go_left;

    private ViewGroup showingroup;
    private View showin;

    //搜索建议
    private PoiSearch mPoiSearch = null;
    //反向地理编码
    private GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用

    //默认成都市
    private String city = "成都市";
    private LatLng latLng;
    //地理围栏
    public List<List<LatLng>> ptsArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchaddress);
        setToolbar(null, false);

        initBase();
        initView();
        initCtrl();
        initData();
    }

    @Override
    protected void onDestroy() {
        mPoiSearch.destroy();
        baiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        mapView = null;
        super.onDestroy();
    }

    private void initBase() {
        if (getIntent().hasExtra("city")) {
            city = getIntent().getStringExtra("city");
        }
        if (getIntent().hasExtra("latLng")) {
            latLng = getIntent().getParcelableExtra("latLng");
        }
        // 初始化POI搜索模块，注册建议搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        // 初始化反地理编码模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
    }

    private void initView() {
        showingroup = (ViewGroup) findViewById(R.id.showingroup);
        recyclerView = (RecyclerView) findViewById(R.id.recycle);
        edit_search = (EditText) findViewById(R.id.edit_search);
        btn_cancel = (TextView) findViewById(R.id.btn_cancel);
        btn_go_left = (TextView) findViewById(R.id.btn_go_left);
        mapView = (MapView) findViewById(R.id.mapView);
        baiduMap = mapView.getMap();

        btn_cancel.setOnClickListener(this);
        btn_go_left.setOnClickListener(this);
    }

    private void initData() {
        geo(latLng);
    }

    private void initCtrl() {
        //初始化百度地图
        baiduMap.getUiSettings().setRotateGesturesEnabled(false);        //禁止旋转手势
        baiduMap.getUiSettings().setOverlookingGesturesEnabled(false);   //禁止俯视手势
        baiduMap.getUiSettings().setCompassEnabled(false);               //禁止指南针图层
        mapView.showZoomControls(false);                                 //禁止缩放控件
        mapView.showScaleControl(false);                                 //禁止比例尺
        baiduMap.setMyLocationEnabled(true);                             //启用用户位置
        //设置自己位置
        baiduMap.setMyLocationData(new MyLocationData.Builder().latitude(latLng.latitude).longitude(latLng.longitude).build());
        MapHelper.zoomByPoint(baiduMap, latLng);
        //设置移动监听
        //设置移动监听
        baiduMap.setOnMapStatusChangeListener(onMapStatusChangeListener);

        adapter = new RecycleAdapterSearchAddress(this, results);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

        btn_go_left.setText(city);

        /**
         * 当输入关键字变化时，动态更新建议列表
         */
        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 0) return;
                /**
                 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                 */

                if (showin == null) {
                    showin = LoadingViewUtil.showin(showingroup, R.layout.layout_loading, showin);
                }
                search(latLng, s.toString());
            }
        });
    }

    private void freshCtrl() {
        adapter.notifyDataSetChanged();
    }

    //发起POI检索
    private void search(LatLng latLng, String key) {
        //mSuggestionSearch.requestSuggestion((new SuggestionSearchOption()).keyword(s.toString()).city(city));
        //mPoiSearch.searchInCity(new PoiCitySearchOption().city(AppHelper.getSearchCity(city)).keyword(s.toString()).pageCapacity(15).pageNum(0));
        mPoiSearch.searchNearby(new PoiNearbySearchOption().location(latLng).radius(1000).keyword(key).pageCapacity(15).pageNum(0).sortType(PoiSortType.distance_from_near_to_far));//.sortType(PoiSortType.distance_from_near_to_far)
    }

    //发起Geo反地理编码查询关注点
    private void geo(LatLng latLng) {
        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
    }
    ////////////////////////////////////
    /////////监听回调
    ////////////////////////////////////


    //点击事件回调
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_go_left:
//                intent.setClass(this, CityActivity.class);
//                intent.putExtra("city", nowCity);
//                intent.putExtra("latlng", MapHelper.LatLng2Str(latLng));
//                startActivityForResult(intent, RESULT_CITY);
                break;
        }
    }

    //item点击事件回调
    @Override
    public void onItemClick(RecyclerView.ViewHolder viewHolder) {
//        Position position = adapter.getResults().get(viewHolder.getLayoutPosition());
//        position.setCity(city);
//        EventBus.getDefault().post(position);
//        finish();
        Position position = adapter.getResults().get(viewHolder.getLayoutPosition());
        MapHelper.zoomByPoint(baiduMap, position.getLatLng());
    }

    //POI检索回调
    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
    }
    //POI检索回调
    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {
    }
    //POI检索回调
    @Override
    public void onGetPoiResult(PoiResult result) {
        LoadingViewUtil.showout(showingroup, showin);
        showin = null;
        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(SearchAddressActivity.this, "未找到结果", Toast.LENGTH_LONG).show();
            adapter.getResults().clear();
            freshCtrl();
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            List<Position> positions = new ArrayList<>();
            for (PoiInfo poi : result.getAllPoi()) {
                if (city.contains(poi.city)) {
                    Position position = new Position(poi);
//                    position.setIn(MapHelper.isInAreas(ptsArray, poi.location));
                    positions.add(position);
                }
            }
            adapter.getResults().clear();
            adapter.getResults().addAll(positions);
            freshCtrl();
        }
    }

    //反检索回调
    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        LoadingViewUtil.showout(showingroup, showin);
        showin = null;
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(SearchAddressActivity.this, "未找到结果", Toast.LENGTH_LONG).show();
            adapter.getResults().clear();
            freshCtrl();
        } else {
            List<Position> positions = new ArrayList<>();
            List<PoiInfo> poiList = result.getPoiList();
            for (PoiInfo poi : poiList) {
//                if (city.contains(poi.city)) {
                Position position = new Position(poi);
//                    position.setIn(MapHelper.isInAreas(ptsArray, poi.location));
                positions.add(position);
            }
//            }
            adapter.getResults().clear();
            adapter.getResults().addAll(positions);
            freshCtrl();
        }
    }

    //检索回调
    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
    }

    //百度地图状态监听回调
    private BaiduMap.OnMapStatusChangeListener onMapStatusChangeListener = new BaiduMap.OnMapStatusChangeListener() {
        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus) {
        }

        @Override
        public void onMapStatusChange(MapStatus mapStatus) {
        }

        @Override
        public void onMapStatusChangeFinish(MapStatus mapStatus) {
            if (edit_search == null) return;
            String key = edit_search.getText().toString();
            if (StrUtils.isEmpty(key)) {
                geo(mapStatus.target);
            } else {
                search(mapStatus.target, key);
            }
        }
    };

//    public void netGetArea(String city) {
//        RequestParams params = new RequestParams(AppData.Url.getArea);
//        params.addHeader("token", AppData.App.getToken());
//        params.addBodyParameter("cityName", city);
//        CommonNet.samplepost(params, new TypeToken<List<List<String>>>() {
//        }.getType(), new CommonNet.SampleNetHander() {
//            @Override
//            public void netGo(final int code, Object pojo, String text, Object obj) {
//                List<List<String>> areas = (ArrayList<List<String>>) pojo;
//                ptsArray = MapHelper.str2LatLngsArray(areas);
//                search(latLng, edit_search.getText().toString());
//            }
//
//            @Override
//            public void netSetError(int code, String text) {
//                Toast.makeText(SearchAddressActivity.this, text, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void netEnd(int status) {
//                edit_search.setEnabled(true);
//                LoadingViewUtil.showout(showingroup, showin);
//                showin = null;
//            }
//
//            @Override
//            public void netStart(int status) {
//                edit_search.setEnabled(false);
//                showin = LoadingViewUtil.showin(showingroup, R.layout.layout_loading, showin);
//            }
//        });
//    }
}