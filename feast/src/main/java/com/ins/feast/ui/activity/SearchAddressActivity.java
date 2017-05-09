package com.ins.feast.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
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
import com.baidu.mapapi.map.MapStatusUpdateFactory;
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
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
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
import com.ins.feast.utils.NetCouldOrderHelper;
import com.ins.feast.utils.ThrowableUtil;
import com.ins.middle.ui.activity.BaseFeastActivity;
import com.liaoinstan.springview.container.AliFooter;
import com.liaoinstan.springview.container.AliHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.sobey.common.common.LoadingViewUtil;
import com.sobey.common.interfaces.OnRecycleItemClickListener;
import com.sobey.common.utils.ClickUtils;
import com.sobey.common.utils.StrUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static com.baidu.location.b.g.P;

public class SearchAddressActivity extends BaseMapActivity implements OnRecycleItemClickListener, OnGetPoiSearchResultListener, View.OnClickListener, OnGetGeoCoderResultListener {

    private MapView mapView;
    private BaiduMap baiduMap;

    private RecyclerView recyclerView;
    private List<Position> results = new ArrayList<>();
    private RecycleAdapterSearchAddress adapter;

    private SpringView springView;

    private EditText edit_search;
    private TextView btn_cancel;
    private TextView btn_go_left;
    private View btn_relocation;


    private ViewGroup showingroup;
    private View showin;

    private int type;

    //搜索建议
    private PoiSearch mPoiSearch = null;
    //反向地理编码
    private GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用

    //默认成都市
    private String city = "成都市";
    private LatLng latLng;

    //分页参数
    private int page;
    private final int PAGE_COUNT = 15;

    public static void start(Context context) {
        start(context, 0);
    }

    public static void start(Context context, int type) {
        //防止重复点击过快打开页面
        if (ClickUtils.isFastDoubleClick()) return;
        Intent intent = new Intent(context, SearchAddressActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchaddress);
        setToolbar(null, true);

        initBase();
        initView();
        initCtrl();
        setData();
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
        //该页面已经独立，定位数据由定位器提供，不再需要页面传递定位参数
//        if (getIntent().hasExtra("city")) {
//            city = getIntent().getStringExtra("city");
//        }
//        if (getIntent().hasExtra("latLng")) {
//            latLng = getIntent().getParcelableExtra("latLng");
//        }
        if (getIntent().hasExtra("type")) {
            type = getIntent().getIntExtra("type", 0);
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
        springView = (SpringView) findViewById(R.id.spring);
        edit_search = (EditText) findViewById(R.id.edit_search);
        btn_cancel = (TextView) findViewById(R.id.btn_cancel);
        btn_go_left = (TextView) findViewById(R.id.btn_go_left);
        btn_relocation = findViewById(R.id.btn_relocation);
        mapView = (MapView) findViewById(R.id.mapView);
        baiduMap = mapView.getMap();

        btn_cancel.setOnClickListener(this);
        btn_go_left.setOnClickListener(this);
        btn_relocation.setOnClickListener(this);
    }

    private void initData() {
        if (latLng != null) geo(latLng);
    }

    private void initCtrl() {
        //初始化百度地图
        baiduMap.getUiSettings().setRotateGesturesEnabled(false);        //禁止旋转手势
        baiduMap.getUiSettings().setOverlookingGesturesEnabled(false);   //禁止俯视手势
        baiduMap.getUiSettings().setCompassEnabled(false);               //禁止指南针图层
        mapView.showZoomControls(false);                                 //禁止缩放控件
        mapView.showScaleControl(false);                                 //禁止比例尺
        baiduMap.setMyLocationEnabled(true);                             //启用用户位置
        //设置移动监听
        //设置移动监听
        baiduMap.setOnMapStatusChangeListener(onMapStatusChangeListener);

        adapter = new RecycleAdapterSearchAddress(this, results);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        springView.setHeader(new AliHeader(this, false));
        springView.setFooter(new AliFooter(this, false));
        springView.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                String key = edit_search.getText().toString();
                if (!StrUtils.isEmpty(key)) {
                    page = 0;
                    search(key);
                } else {
                    springView.onFinishFreshAndLoad();
                    Snackbar.make(showingroup, "没有更多的数据了", Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onLoadmore() {
                String key = edit_search.getText().toString();
                if (!StrUtils.isEmpty(key)) {
                    page++;
                    search(key);
                } else {
                    springView.onFinishFreshAndLoad();
                    Snackbar.make(showingroup, "没有更多的数据了", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

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
                if (s.length() <= 0) {
                    geo(latLng);
                    return;
                }
                /**
                 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                 */
                if (showin == null)
                    showin = LoadingViewUtil.showin(showingroup, R.layout.layout_loading, showin);
                page = 0;
                search(s.toString());
            }
        });
    }

    private void setData() {
        if (latLng != null) {
            //设置自己位置
            baiduMap.setMyLocationData(new MyLocationData.Builder().latitude(latLng.latitude).longitude(latLng.longitude).build());
            MapHelper.zoomByPoint(baiduMap, latLng, 18);
        }
        if (!StrUtils.isEmpty(city)) {
            //左上角城市设置数据
            btn_go_left.setText(city);
        }
    }

    private void freshCtrl() {
        adapter.notifyDataSetChanged();
    }

    //发起POI检索
    private void search(String key) {
        //mSuggestionSearch.requestSuggestion((new SuggestionSearchOption()).keyword(s.toString()).city(city));
        //mPoiSearch.searchInCity(new PoiCitySearchOption().city(AppHelper.getSearchCity(city)).keyword(s.toString()).pageCapacity(15).pageNum(0));
//        mPoiSearch.searchNearby(new PoiNearbySearchOption().location(latLng).radius(1000).keyword(key).pageCapacity(PAGE_COUNT).pageNum(page).sortType(PoiSortType.distance_from_near_to_far));//.sortType(PoiSortType.distance_from_near_to_far)
        mPoiSearch.searchInCity(new PoiCitySearchOption().city(city).keyword(key));//.sortType(PoiSortType.distance_from_near_to_far)
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
            case R.id.btn_relocation:
                startLocation();
                break;
        }
    }

    //item点击事件回调
    @Override
    public void onItemClick(final RecyclerView.ViewHolder viewHolder) {
        final Position position = adapter.getResults().get(viewHolder.getAdapterPosition());
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
                        Toast.makeText(SearchAddressActivity.this, "当前位置无法点餐", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            EventBus.getDefault().post(position);
            finish();
        }
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

        springView.onFinishFreshAndLoad();

        if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            if (page == 0) {
//                Toast.makeText(SearchAddressActivity.this, "未找到结果", Toast.LENGTH_LONG).show();
//                adapter.getResults().clear();
//                freshCtrl();
                setlackPage();
            } else {
                Snackbar.make(showingroup, "没有更多的数据了", Snackbar.LENGTH_SHORT).show();
            }
            return;
        } else {
            LoadingViewUtil.showout(showingroup, showin);
            showin = null;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            List<Position> positions = new ArrayList<>();
            for (PoiInfo poi : result.getAllPoi()) {
                if (city.contains(poi.city)) {
                    Position position = new Position(poi);
                    positions.add(position);
                }
            }
            if (page == 0) adapter.getResults().clear();
            adapter.getResults().addAll(positions);
            if (StrUtils.isEmpty(adapter.getResults())) {
                setlackPage();
            } else {
                freshCtrl();
            }
        }
    }

    private void setlackPage() {
        showin = LoadingViewUtil.showin(showingroup, R.layout.layout_lack, showin, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showin = LoadingViewUtil.showin(showingroup, R.layout.layout_loading, showin);
                search(edit_search.getText().toString());
            }
        });
    }

    //反检索回调
    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        LoadingViewUtil.showout(showingroup, showin);
        showin = null;
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
//            Toast.makeText(SearchAddressActivity.this, "未找到结果", Toast.LENGTH_LONG).show();
//            adapter.getResults().clear();
//            freshCtrl();
            setlackPage();
        } else {
            List<Position> positions = new ArrayList<>();
            List<PoiInfo> poiList = result.getPoiList();
            try {
                if (poiList == null) {
                    return;
                }
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
            }catch (Exception e){
                ThrowableUtil.handleThrowable(e);
            }
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
                latLng = mapStatus.target;
                page = 0;
                search(key);
            }
        }
    };

    @Override
    public void onLocation(LatLng latLng, String city, String district, boolean isFirst) {
        //左上角城市设置数据
        this.latLng = latLng;
        this.city = city;
        MapHelper.zoomByPoint(baiduMap, latLng, 18);
        setData();
        geo(latLng);
        //定位成功后马上停止
        stopLocation();
    }

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